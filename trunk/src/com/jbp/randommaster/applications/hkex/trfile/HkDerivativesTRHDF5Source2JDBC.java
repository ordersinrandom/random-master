package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jbp.randommaster.database.MasterDatabaseConnections;
import com.jbp.randommaster.datasource.historical.HistoricalDataSource;
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

/**
 * 
 * HkDerivativesTRHDF5Source2JDBC takes HDF5 files and consolidate them as interval records and save to database.
 *
 */
public class HkDerivativesTRHDF5Source2JDBC  {

	static Logger log = Logger.getLogger(HkDerivativesTRHDF5Source2JDBC.class);

	private MasterDatabaseConnections connectionSrc;
	private File[] allHDF5Files;

	public HkDerivativesTRHDF5Source2JDBC(File[] allHDF5Files, MasterDatabaseConnections connectionSrc) {
		this.connectionSrc = connectionSrc;
		this.allHDF5Files = allHDF5Files;
		
		TreeMap<YearMonth, File> spotMonth2HDF5 = new TreeMap<>();
		
		for (File inputHDF5File : allHDF5Files) {
			String inputFilenamePrefix = inputHDF5File.getName().substring(0, 6);
			YearMonth spotMonth = YearMonth.parse(inputFilenamePrefix, DateTimeFormat.forPattern("yyyyMM"));
			spotMonth2HDF5.put(spotMonth, inputHDF5File);
		}
		
		DateTimeFormatter df=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for (Map.Entry<YearMonth, File> en : spotMonth2HDF5.entrySet()) {
			YearMonth spotMonth = en.getKey();
			File inputHDF5File = en.getValue();
			
			HistoricalDataSource<? extends TimeConsolidatedTradeRecord> src = getDataSource(inputHDF5File, "Futures", "HSI", spotMonth, 5*60);
			
			// iterate through the data for this month
			for (TimeConsolidatedTradeRecord data : src.getData()) {
				/*
				plotSeries.add(
						RegularTimePeriod.createInstance(Second.class, data.getTimestamp().toDate(), TimeZone.getDefault()),
						data.getFirstTradedPrice(),
						data.getMaxTradedPrice(),
						data.getMinTradedPrice(),
						data.getLastTradedPrice());
				 		*/
				
				
				System.out.println(data.getTimestamp().toString(df)+" - last = "+data.getLastTradedPrice()+", vol = "+data.getTradedVolume());
				
			}			
			
		}
		
		
	}


	private HistoricalDataSource<? extends TimeConsolidatedTradeRecord> getDataSource(
			File inputHDF5File, String futuresOrOptions, String underlying, YearMonth spotMonth, int frequencySeconds) {
		
		String inputHDF5Filename = inputHDF5File.getAbsolutePath();
		LocalDate firstDayOfMonth = spotMonth.toLocalDate(1);
		LocalDate lastDayOfMonth = spotMonth.plusMonths(1).toLocalDate(1).minusDays(1);
		
		TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR> consolidatedSrc = null;
		
		try (
			// raw data source
			HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(
					new String[] { inputHDF5Filename }, futuresOrOptions, underlying);
			
			// filtered by expiry month
			FilteredHistoricalDataSource<HkDerivativesTR> expMonthFilteredSource = 
					new FilteredHistoricalDataSource<HkDerivativesTR>(
					originalSrc, new ExpiryMonthFilter<HkDerivativesTR>(spotMonth));
				
			// filtered by trade type (Normal)
			FilteredHistoricalDataSource<HkDerivativesTR> filteredSource =
					new FilteredHistoricalDataSource<HkDerivativesTR>(
					expMonthFilteredSource, new HkDerivativesTRTradeTypeFilter(TradeType.Normal));
		) {
		
			HkDerivativesTRConsolidator consolidator = new HkDerivativesTRConsolidator();
			LocalDateTime start = new LocalDateTime(spotMonth.getYear(), spotMonth.getMonthOfYear(), firstDayOfMonth.getDayOfMonth(), 9, 30, 0);
			LocalDateTime end = new LocalDateTime(spotMonth.getYear(), spotMonth.getMonthOfYear(), lastDayOfMonth.getDayOfMonth(), 16, 15, 0);

			// we consolidated by number of seconds.
			Period interval = new Period(0, 0, frequencySeconds, 0);
			
			consolidatedSrc = new TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR>(
								consolidator, filteredSource, start, end, interval);
		}
		
		return consolidatedSrc;
	}
		
	
	
	
	public static void main(String[] args) {
		try {
			String inputHDF5FolderName = args[0];
			String dbConfPath = args[1];
			
			log.info("Input HDF5 files folder: " + inputHDF5FolderName);
			log.info("configFilePath: "+dbConfPath);
	
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
			
			HkDerivativesTRHDF5Source2JDBC app = new HkDerivativesTRHDF5Source2JDBC(allHDF5Files, conn);
			
			
			log.info("all files processed");
			
		} catch (Exception e) {
			log.fatal("Unable to connect to database", e);
		}


	}

}
