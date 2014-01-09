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
public class HkDerivativesTRHDF5Source2JDBC  {

	static Logger log = Logger.getLogger(HkDerivativesTRHDF5Source2JDBC.class);

	private MasterDatabaseConnections connectionSrc;

	public HkDerivativesTRHDF5Source2JDBC(File inputFolder, MasterDatabaseConnections connectionSrc) {
		this.connectionSrc = connectionSrc;
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
			
			HkDerivativesTRHDF5Source2JDBC app = new HkDerivativesTRHDF5Source2JDBC(inputFolder,  conn);
			
			
			// not yet implemented
			
			log.info("all files processed");
			
		} catch (Exception e) {
			log.fatal("Unable to connect to database", e);
		}


	}

}
