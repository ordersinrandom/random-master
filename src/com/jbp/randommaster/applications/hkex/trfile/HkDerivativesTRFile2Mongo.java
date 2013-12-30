package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.mongodb.MongoClient;

/**
 * 
 * HkDerivativesTRFile2Mongo converts HkDerivatives trade record csv file to mongodb.
 *
 */
public class HkDerivativesTRFile2Mongo extends HkDerivativesTRZipFilesProcessor {

	static Logger log = Logger.getLogger(HkDerivativesTRFile2Mongo.class);
	
	public HkDerivativesTRFile2Mongo(File inputFolder, MongoClient mongoClient) {
		super(inputFolder);
		
		
	}
	
	
	@Override
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {
		
		
		
	}

	
	
}
