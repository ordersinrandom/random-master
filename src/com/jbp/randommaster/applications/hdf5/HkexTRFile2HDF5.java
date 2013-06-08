package com.jbp.randommaster.applications.hdf5;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.jbp.randommaster.utils.ZipUtils;

/**
 * 
 * This tool takes a folder of HKEX TR files, transform them into HDF5 and then
 * save to another output folder.
 * 
 */
public class HkexTRFile2HDF5 {

	static Logger log = Logger.getLogger(HkexTRFile2HDF5.class);


	private File inputFolder, outputFolder;
	
	public HkexTRFile2HDF5(File inputFolder, File outputFolder) {
		
		this.inputFolder=inputFolder;
		this.outputFolder=outputFolder;
		

	}
	
	
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
			
			try {
				// open the zip file
				ZipFile z =new ZipFile(srcZipFile);
				
				Map<String, File> tempUnzippedFiles=ZipUtils.unzipToTempFiles(z, "HkexTRFile2HDF5_temp_", true);
				
				
				

			} catch (IOException e1) {
				log.warn("Unable to unzip the file "+srcZipFile.getAbsolutePath()+". SKIPPED", e1);
			}
			
			
		}
		
	}
	
	

	
	
	
	
	public static void main(String[] args) {
		String inputZipFilesFolderName = args[0];
		String outputFolderName = args[1];

		log.info("Input zip files folder: " + inputZipFilesFolderName);
		log.info("Output folder: " + outputFolderName);

		File inputFolder = new File(inputZipFilesFolderName);
		File outputFolder = new File(outputFolderName);

		if (!inputFolder.isDirectory() || !outputFolder.isDirectory())
			throw new IllegalArgumentException("Input folders or output folders are not directories");

		HkexTRFile2HDF5 app=new HkexTRFile2HDF5(inputFolder, outputFolder);
		app.processFiles();

	}

}
