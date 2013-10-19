package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;

/**
 * 
 * HkDerivativesTRFile2JDBC takes a folder of ZIPPED HKEX TR CSV Files and insert them into JDBC database.
 *
 */
public class HkDerivativesTRFile2JDBC extends HkDerivativesTRZipFilesProcessor {

	
	static Logger log = Logger.getLogger(HkDerivativesTRFile2JDBC.class);
	
	public HkDerivativesTRFile2JDBC(File inputFolder) {
		super(inputFolder);
		
	}
	

	@Override
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}
