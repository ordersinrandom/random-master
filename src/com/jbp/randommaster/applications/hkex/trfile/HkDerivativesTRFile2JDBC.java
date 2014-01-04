package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.jbp.randommaster.database.MasterDatabaseConnections;
import com.jbp.randommaster.datasource.historical.HkDerivativesTR;

/**
 * 
 * HkDerivativesTRFile2JDBC takes a folder of ZIPPED HKEX TR CSV Files and
 * insert them into JDBC database.
 * 
 */
public class HkDerivativesTRFile2JDBC extends HkDerivativesTRZipFilesProcessor {

	static Logger log = Logger.getLogger(HkDerivativesTRFile2JDBC.class);

	private MasterDatabaseConnections connectionSrc;

	private int numberOfRowsUpdated;

	public HkDerivativesTRFile2JDBC(File inputFolder, MasterDatabaseConnections connectionSrc) {
		super(inputFolder);
		this.connectionSrc = connectionSrc;
		numberOfRowsUpdated = 0;
	}

	@Override
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {

		// not yet implemented.
		/*
create table price5min(
instrumentcode varchar(10) not null, 
recordtimestamp timestamp without time zone not null,
open numeric not null, 
high numeric not null, 
low numeric not null, 
close numeric not null, 
average numeric not null, 
primary key(instrumentcode, recordtimestamp)
);
		 */
		
		
		
		log.info("Processing source file: "+srcZipFile.getAbsolutePath());
		
		String sql = "Insert into hkextrsource(entryhash,underlying,futuresoptions,expirymonth,strike,callput,tradetimestamp,price,quantity,tradetype,source) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

		try (Connection conn = connectionSrc.getConnection(); PreparedStatement st = conn.prepareStatement(sql);) {

			conn.setAutoCommit(true);

			for (HkDerivativesTR tr : loadedData) {

				byte[] hash = MessageDigest.getInstance("SHA1").digest(UUID.randomUUID().toString().getBytes("ISO-8859-1"));
				String key = Base64.encodeBase64String(hash);

				st.setString(1, key);
				st.setString(2, tr.getUnderlying());
				st.setString(3, tr.getFuturesOrOptions());
				st.setTimestamp(4, new Timestamp(tr.getExpiryMonth().toLocalDate(1).toDate().getTime()));
				st.setDouble(5, tr.getStrikePrice());
				st.setString(6, tr.getCallPut());
				st.setTimestamp(7, new Timestamp(tr.getTradeTimestamp().toDate().getTime()));
				st.setDouble(8, tr.getPrice());
				st.setDouble(9, tr.getQuantity());
				st.setString(10, tr.getTradeType());
				st.setString(11, srcZipFile.getName());

				int row = st.executeUpdate();
				numberOfRowsUpdated += row;

				if (numberOfRowsUpdated % 2000 == 0) {
					log.info("CSV File: " + srcZipFile.getName() + " number of rows inserted: " + numberOfRowsUpdated);
				}
			}

		} catch (SQLException sqle) {
			log.fatal("SQL Exception. Unable to update database", sqle);
		} catch (NoSuchAlgorithmException e) {
			log.fatal("Unable to get hashing algorithm.", e);
		} catch (UnsupportedEncodingException e) {
			log.fatal("Unable to convert UUID bytes to ISO-8859-1 string format", e);
		}

		log.info("Source file: "+srcZipFile.getAbsolutePath()+" inserted into database");
	}

	public int getRowsUpdated() {
		return numberOfRowsUpdated;
	}

	public static void main(String[] args) {
		try {
			String inputZipFilesFolderName = args[0];
			String configFilePath = args[1];
	
			log.info("Input zip files folder: " + inputZipFilesFolderName);
			log.info("configFilePath: "+configFilePath);
			
	
			File inputFolder = new File(inputZipFilesFolderName);
	
			if (!inputFolder.isDirectory())
				throw new IllegalArgumentException("Input folder is not a directory");
	
			
			MasterDatabaseConnections conn = new MasterDatabaseConnections(configFilePath);
			
			HkDerivativesTRFile2JDBC app = new HkDerivativesTRFile2JDBC(inputFolder,  conn);
			app.processFiles();
			
			log.info("all files processed");
			
		} catch (Exception e) {
			log.fatal("Unable to connect to database", e);
		}


	}

}