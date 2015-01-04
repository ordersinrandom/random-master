package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.net.UnknownHostException;

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
		
		System.out.println(srcZipFile.getName());
		
		// LATER
		
		// Current design
		/*

Contract: { Underlying:HHI, OptionsOrFutures: F, Expiry: 1306, Strike: 0 }
TradeType: 020
Start: 2013-06-11 09:20:00
End: 2013-06-11 09:24:59
high:
low:
open:
close:
avg:
ticks: {
        
	min0:
	min1:
	min2:
	min3:
	min4: {
		    minute=24,
		   { 
			sec0:  
			{
				second=0, traderecords: { item1: {price: 10259, qty: 2 }, item2: {price: ... } ... }
                         }
			sec1:  
                         {      
				second=1, traderecords: {...




HHI,F,1306,0,,20130603,091400,10259,1,020
MHI,F,1312,0,,20130603,091400,21888,1,020
HHI,F,1306,0,,20130603,091400,10259,1,020
HHI,F,1306,0,,20130603,091400,10259,2,020
HHI,F,1306,0,,20130603,091400,10259,1,020

		 */
		
		
	}


	public static void main(String[] args) {
		try {
			String inputZipFilesFolderName = args[0];
			String host = args[1];
			int port = Integer.valueOf(args[2]).intValue();
	
			log.info("Input zip files folder: " + inputZipFilesFolderName);
			log.info("Output db: " + host+":"+port);
	
			File inputFolder = new File(inputZipFilesFolderName);
	
			if (!inputFolder.isDirectory())
				throw new IllegalArgumentException("Input folder is not a directory");
	
			
			MongoClient mongoClient = new MongoClient(host, port);
			
			HkDerivativesTRFile2Mongo app = new HkDerivativesTRFile2Mongo(inputFolder, mongoClient);
			app.processFiles();
			
			log.info("all files processed");
			
		} catch (UnknownHostException e) {
			log.fatal("Unable to connect to database", e);
		}

	}	
	
}
