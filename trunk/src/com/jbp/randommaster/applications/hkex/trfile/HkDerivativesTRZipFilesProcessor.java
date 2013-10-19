package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRFileSource;
import com.jbp.randommaster.utils.ZipUtils;

public abstract class HkDerivativesTRZipFilesProcessor {
	
	static Logger log = Logger.getLogger(HkDerivativesTRZipFilesProcessor.class);
	
	private File inputFolder;

	public HkDerivativesTRZipFilesProcessor(File inputFolder) {
		this.inputFolder = inputFolder;
	}
	
	public File getInputFolder() {
		return inputFolder;
	}
	
	/**
	 * Unzip the input files and then processes each entry one by one.
	 */
	public void processFiles() {

		File[] allZipFiles = inputFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getAbsolutePath().toLowerCase().endsWith(".zip");
			}
		});

		// loop through all the files and process it.
		for (File srcZipFile : allZipFiles) {
			log.info("Processing "+srcZipFile.getAbsolutePath());
			
			Map<String, File> tempUnzippedFiles = null;
			ZipFile zip=null;
			try {
				log.info("Unzipping "+srcZipFile.getAbsolutePath());
				// open the zip file
				zip =new ZipFile(srcZipFile);
				tempUnzippedFiles=ZipUtils.unzipToTempFiles(zip, "DerivativesTRFile2HDF5_temp_", true);
				log.info("Unzip "+srcZipFile.getAbsolutePath()+" finished");
			} catch (IOException e1) {
				log.warn("Unable to unzip the file "+srcZipFile.getAbsolutePath()+". SKIPPED", e1);
			} finally {
				try {
					zip.close();
				} catch (Exception e1) {
					log.warn("Unable to close the zip file: "+srcZipFile.getAbsolutePath(), e1);
				}
			}
			
			if (tempUnzippedFiles!=null) {
				
				for (Map.Entry<String, File> en : tempUnzippedFiles.entrySet()) {
					
					File inputFile=en.getValue();
					
					// now read the source file
					try (HkDerivativesTRFileSource src = new HkDerivativesTRFileSource(inputFile.getAbsolutePath())) {

						log.info("Loading data from "+inputFile.getAbsolutePath());
						
						Iterable<HkDerivativesTR> loadedData=src.getData();
						
						// invoke subclass to handle this
						processHkDerivativesTRInput(srcZipFile, en.getKey(), loadedData);
						
						
					} catch (FileNotFoundException fnf) {
						log.warn("Unable to find the unzipped temp file: "+inputFile.getAbsolutePath()+". File discarded.", fnf);
					}
				}
			}
			
			log.info("Zip file processing finished: "+srcZipFile.getAbsolutePath());
			
		}
		
	}
	
	/**
	 * Subclass implementing this to handle each zip entry.
	 * 
	 * @param srcZipFile The zip file that we are processing.
	 * @param zipEntryKey The zip entry key within the zip file
	 * @param loadedData The loaded HkDerivativesTR raw data.
	 */
	protected abstract void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData);
		

}
