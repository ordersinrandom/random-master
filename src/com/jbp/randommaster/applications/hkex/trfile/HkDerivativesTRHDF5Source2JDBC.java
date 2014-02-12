package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;

import com.jbp.randommaster.database.MasterDatabaseConnections;
import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRHDF5Source;
import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesConsolidatedData;
import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesTRConsolidator;
import com.jbp.randommaster.datasource.historical.consolidation.TimeConsolidatedTradeRecord;
import com.jbp.randommaster.datasource.historical.consolidation.TimeIntervalConsolidatedTRSource;
import com.jbp.randommaster.datasource.historical.filters.ExpiryMonthFilter;
import com.jbp.randommaster.datasource.historical.filters.FilteredHistoricalDataSource;
import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter;
import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter.TradeType;
import com.jbp.randommaster.datasource.historical.filters.HolidaysFilter;
import com.jbp.randommaster.datasource.historical.filters.MarketHoursFilter;
import com.jbp.randommaster.utils.HolidaysList;
import com.jbp.randommaster.utils.MarketHours;

/**
 * 
 * HkDerivativesTRHDF5Source2JDBC takes HDF5 files and consolidate them as
 * interval records and save to database.
 * 
 */
public class HkDerivativesTRHDF5Source2JDBC {

	static Logger log = Logger.getLogger(HkDerivativesTRHDF5Source2JDBC.class);

	private MasterDatabaseConnections connectionSrc;
	private File[] allHDF5Files;
	private int intervalMinutes;
	private String tableName;
	private List<String> underlyingsList;

	/**
	 * Create a new instance of HkDerivativesTRHDF5Source2JDBC batch.
	 * 
	 * @param allHDF5Files
	 *            All the HDF5 files to be added to master database.
	 * @param connectionSrc
	 *            The connection details
	 * @param intervalMinutes
	 */
	public HkDerivativesTRHDF5Source2JDBC(File[] allHDF5Files, MasterDatabaseConnections connectionSrc, int intervalMinutes) {
		this.connectionSrc = connectionSrc;
		this.allHDF5Files = allHDF5Files;
		this.intervalMinutes = intervalMinutes;
		this.tableName = "price" + intervalMinutes + "min";

		underlyingsList = new ArrayList<>();
		underlyingsList.add("HSI");
		underlyingsList.add("HHI");
		underlyingsList.add("MHI");
		underlyingsList.add("MCH");
	}

	private boolean hasExistingDataForThisFile(File inputHDF5File, Statement stat) throws SQLException {
		int counter = -1;
		try (ResultSet rs = stat.executeQuery("select count(*) from " + tableName + " where datasource='" + inputHDF5File.getName() + "'");) {
			while (rs.next()) {
				counter = rs.getInt(1);
			}
		}

		return counter > 0;
	}

	private void dropExistingDataForThisFile(File inputHDF5File, Statement stat) throws SQLException {
		stat.executeUpdate("delete from " + tableName + " where datasource='" + inputHDF5File.getName() + "'");
	}
	
	private YearMonth getSpotMonthByHDF5FileName(File inputHDF5File) {
		String inputFilenamePrefix = inputHDF5File.getName().substring(0, 6);
		YearMonth spotMonth = YearMonth.parse(inputFilenamePrefix, DateTimeFormat.forPattern("yyyyMM"));
		return spotMonth;
	}
	

	public void processAllHDF5Files() throws SQLException {

		log.info("Adding HDF5 files to table " + tableName);
		for (File f : allHDF5Files)
			log.info("HDF5 file: " + f.getName());

		// sort the input file by the month (using filename)
		TreeMap<YearMonth, File> spotMonth2HDF5 = new TreeMap<>();
		for (File inputHDF5File : allHDF5Files) {
			//String inputFilenamePrefix = inputHDF5File.getName().substring(0, 6);
			//YearMonth spotMonth = YearMonth.parse(inputFilenamePrefix, DateTimeFormat.forPattern("yyyyMM"));

			YearMonth spotMonth = getSpotMonthByHDF5FileName(inputHDF5File);
			spotMonth2HDF5.put(spotMonth, inputHDF5File);
		}

		int frequencySeconds = intervalMinutes * 60;

		String futuresOrOptions = "Futures";

		Connection conn = null;
		Statement stat = null;
		PreparedStatement pstat = null;
		try {
			conn = connectionSrc.getConnection();
			conn.setAutoCommit(true);
			stat = conn.createStatement();

			pstat = conn.prepareStatement("insert into " + tableName
					+ "(instrumentcode,recordtimestamp,open,high,low,close,average,tradedvolume,datasource)  values(?,?,?,?,?,?,?,?,?)");

			for (Map.Entry<YearMonth, File> en : spotMonth2HDF5.entrySet()) {
				YearMonth spotMonth = en.getKey();
				File inputHDF5File = en.getValue();

				String dataSourceName = inputHDF5File.getName();

				if (hasExistingDataForThisFile(inputHDF5File, stat)) {
					log.warn("Dropping existing data for " + dataSourceName);
					dropExistingDataForThisFile(inputHDF5File, stat);
					log.warn("Existing data for " + dataSourceName + " removed");
				}

				for (String underlying : underlyingsList) {

					String inputHDF5Filename = inputHDF5File.getAbsolutePath();
					LocalDate firstDayOfMonth = spotMonth.toLocalDate(1);
					LocalDate lastDayOfMonth = spotMonth.plusMonths(1).toLocalDate(1).minusDays(1);

					// HSIc0, HHIc0 etc etc (Reuter's convention)
					String instrName = underlying + "c0";

					log.info("Inserting data for " + instrName + " from " + dataSourceName);
					int rowsCount = 0;

					try (
							// raw data source
							HkDerivativesTRHDF5Source originalSrc = new HkDerivativesTRHDF5Source(new String[] { inputHDF5Filename }, futuresOrOptions,
									underlying);
							// filtered by expiry month
							FilteredHistoricalDataSource<HkDerivativesTR> filteredSrc1 = new FilteredHistoricalDataSource<>(
									originalSrc, new ExpiryMonthFilter<HkDerivativesTR>(spotMonth));
							// filtered by trade type (Normal)
							FilteredHistoricalDataSource<HkDerivativesTR> filteredSrc2 = new FilteredHistoricalDataSource<>(
									filteredSrc1, new HkDerivativesTRTradeTypeFilter(TradeType.Normal));
							) {

						// we consolidated by 5 minutes
						HkDerivativesTRConsolidator consolidator = new HkDerivativesTRConsolidator();
						LocalDateTime start = new LocalDateTime(spotMonth.getYear(), spotMonth.getMonthOfYear(), firstDayOfMonth.getDayOfMonth(), 0,
								0, 0);
						LocalDateTime end = new LocalDateTime(spotMonth.getYear(), spotMonth.getMonthOfYear(), lastDayOfMonth.getDayOfMonth(), 23,
								55, 0);

						Period consolidationInterval = new Period(0, 0, frequencySeconds, 0);

						// consolidated every 5 minute from 0000 to 2355 including holidays (so that data got extrapolated on both sides)
						TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR> consolidatedSrc = new TimeIntervalConsolidatedTRSource<>(
								consolidator, filteredSrc2, start, end, consolidationInterval);
						
						
						// filter for holidays
						HolidaysFilter<HkDerivativesConsolidatedData> holidaysFilter = new HolidaysFilter<>(HolidaysList.HongKong);
						MarketHoursFilter<HkDerivativesConsolidatedData> marketHoursFilter = new MarketHoursFilter<>(MarketHours.HongKongDerivatives, true, false);
						
						// the consolidated source is now filtered by market hours and holidays.
						FilteredHistoricalDataSource<HkDerivativesConsolidatedData> filteredConsolidatedSrc = new FilteredHistoricalDataSource<>(consolidatedSrc, holidaysFilter);
						FilteredHistoricalDataSource<HkDerivativesConsolidatedData> filteredConsolidatedSrc2 = new FilteredHistoricalDataSource<>(filteredConsolidatedSrc, marketHoursFilter);
						

						// iterate through the data for this month
						for (TimeConsolidatedTradeRecord data : filteredConsolidatedSrc2.getData()) {

							/*
							 * CREATE TABLE price5min ( instrumentcode character
							 * varying(10) NOT NULL, recordtimestamp timestamp
							 * without time zone NOT NULL, open numeric NOT
							 * NULL, high numeric NOT NULL, low numeric NOT
							 * NULL, close numeric NOT NULL, average numeric NOT
							 * NULL, tradedvolume numeric NOT NULL, datasource
							 * character varying(50), CONSTRAINT price5min_pkey
							 * PRIMARY KEY (instrumentcode, recordtimestamp) )
							 */

							// log.info("Inserting " + instrName +
							// ", data object=" + data.toString());

							pstat.setString(1, instrName);
							pstat.setTimestamp(2, new java.sql.Timestamp(data.getTimestamp().toDate().getTime()));
							pstat.setDouble(3, data.getFirstTradedPrice());
							pstat.setDouble(4, data.getMaxTradedPrice());
							pstat.setDouble(5, data.getMinTradedPrice());
							pstat.setDouble(6, data.getLastTradedPrice());
							pstat.setDouble(7, data.getAveragedPrice());
							pstat.setDouble(8, data.getTradedVolume());
							pstat.setString(9, inputHDF5File.getName());
							pstat.executeUpdate();

							rowsCount++;
							// log.info("Data Inserted");

						}

					}
					// the input HDF5File should be closed by this point
					log.info("Finished inserting data (" + rowsCount + ") for " + instrName + " from " + dataSourceName);
				}

			}

		} finally {
			if (stat != null)
				stat.close();
			if (pstat != null)
				pstat.close();
			if (conn != null)
				conn.close();
		}

	}

	public static void main(String[] args) {
		try {
			String inputHDF5FolderName = args[0];
			String dbConfPath = args[1];
			int intervalMinutes = Integer.valueOf(args[2]).intValue();

			log.info("Input HDF5 files folder: " + inputHDF5FolderName);
			log.info("configFilePath: " + dbConfPath);
			log.info("Consolidation interval: " + intervalMinutes + " minutes");

			File inputFolder = new File(inputHDF5FolderName);

			if (!inputFolder.isDirectory())
				throw new IllegalArgumentException("Input folder is not a directory");

			File[] allHDF5Files = inputFolder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File givenFile) {
					String name = givenFile.getName().toLowerCase();
					return name.endsWith(".hdf5") || name.endsWith(".h5");
				}
			});

			MasterDatabaseConnections conn = new MasterDatabaseConnections(dbConfPath);

			HkDerivativesTRHDF5Source2JDBC app = new HkDerivativesTRHDF5Source2JDBC(allHDF5Files, conn, intervalMinutes);
			app.processAllHDF5Files();

			log.info("all files processed");

		} catch (Exception e) {
			log.fatal("Unable to connect to database", e);
		}

	}

}
