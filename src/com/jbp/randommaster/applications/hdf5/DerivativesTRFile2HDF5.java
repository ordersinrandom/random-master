package com.jbp.randommaster.applications.hdf5;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.DerivativesTRFileData;
import com.jbp.randommaster.datasource.historical.DerivativesTRFileSource;
import com.jbp.randommaster.hdf5.HDF5DerivativesTRFileBuilder;
import com.jbp.randommaster.utils.ZipUtils;

/**
 * 
 * This tool takes a folder of HKEX TR files, transform them into HDF5 and then
 * save to another output folder.
 * 
 */
public class DerivativesTRFile2HDF5 {

	static Logger log = Logger.getLogger(DerivativesTRFile2HDF5.class);


	private File inputFolder, outputFolder;
	
	public DerivativesTRFile2HDF5(File inputFolder, File outputFolder) {
		
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
			
			Map<String, File> tempUnzippedFiles = null;
			ZipFile zip=null;
			try {
				log.info("Unzipping "+srcZipFile.getAbsolutePath());
				// open the zip file
				zip =new ZipFile(srcZipFile);
				tempUnzippedFiles=ZipUtils.unzipToTempFiles(zip, "HkexTRFile2HDF5_temp_", true);
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
				
				String sp = System.getProperty("file.separator");
				
				for (Map.Entry<String, File> en : tempUnzippedFiles.entrySet()) {
					
					String outputHDF5Filename=outputFolder.getAbsolutePath()+sp+en.getKey()+".hdf5";
					
					File outputHDF5File=new File(outputHDF5Filename);
					(new File(outputHDF5File.getParent())).mkdirs();
					
					File inputFile=en.getValue();
					
					try {
						// now read the source file
						DerivativesTRFileSource src = new DerivativesTRFileSource(inputFile.getAbsolutePath());

						log.info("Loading data from "+inputFile.getAbsolutePath());
						
						Iterable<DerivativesTRFileData> loadedData=src.getData();

						log.info("building HDF5 File: "+outputHDF5Filename);
						HDF5DerivativesTRFileBuilder builder = new HDF5DerivativesTRFileBuilder(outputHDF5Filename);
						builder.createOrOpen();
						builder.createCompoundDatasetsForTRData(loadedData);
						builder.closeFile();
						log.info("HDF5 File ("+outputHDF5Filename+")data written and closed");
					} catch (FileNotFoundException e1) {
						log.warn("unable to find input file: "+inputFile.getAbsolutePath()+". HDF5 file not generated");
					}
					
				}
			}
			
			log.info("Zip file processing finished: "+srcZipFile.getAbsolutePath());
			
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

		DerivativesTRFile2HDF5 app=new DerivativesTRFile2HDF5(inputFolder, outputFolder);
		app.processFiles();

	}

}