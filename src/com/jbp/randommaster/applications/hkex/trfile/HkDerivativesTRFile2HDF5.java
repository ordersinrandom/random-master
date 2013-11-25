package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.hdf5builders.HkDerivativesTRHDF5Builder;

/**
 * 
 * This tool takes a folder of HKEX Derivatives TR files, transform them into HDF5 and then
 * save to another output folder.
 * 
 */
public class HkDerivativesTRFile2HDF5 extends HkDerivativesTRZipFilesProcessor {
	
	static Logger log = Logger.getLogger(HkDerivativesTRFile2HDF5.class);

	private File outputFolder;
	
	/**
	 * Create an instance of HkDerivativesTRFile2HDF5.
	 * @param inputFolder The input folder that contains a number of zipped HKEX Derivatives TR CSV Files.
	 * @param outputFolder The target output folder where we will save down the corresponding HDF5 Files.
	 */
	public HkDerivativesTRFile2HDF5(File inputFolder, File outputFolder) {
		
		super(inputFolder);
		
		this.outputFolder=outputFolder;

	}

	/**
	 * Implements base class abstract method to create HDF5 file entry.
	 * 
	 * @param srcZipFile The original input zip file.
	 * @param zipEntryKey The entry key within the source zip file.
	 * @param loadedData The TR record loaded from the csv file of that entry.
	 */
	@Override 
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {
		
		String sp = System.getProperty("file.separator");
		
		String outputHDF5Filename=outputFolder.getAbsolutePath()+sp+zipEntryKey+".hdf5";
		
		File outputHDF5File=new File(outputHDF5Filename);
		if (outputHDF5File.exists()) {
			log.warn("Target output file "+outputHDF5Filename+" already exists. SKIPPED");
			return;
		}
		
		(new File(outputHDF5File.getParent())).mkdirs();
		
		log.info("building HDF5 File: "+outputHDF5Filename);
		HkDerivativesTRHDF5Builder builder = new HkDerivativesTRHDF5Builder(outputHDF5Filename);
		builder.createOrOpen();
		builder.createCompoundDatasetsForTRData(loadedData);
		builder.closeFile();
		log.info("HDF5 File ("+outputHDF5Filename+")data written and closed");
		
		
	}

	/* OLD code to be removed.
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
				
				String sp = System.getProperty("file.separator");
				
				for (Map.Entry<String, File> en : tempUnzippedFiles.entrySet()) {
					
					String outputHDF5Filename=outputFolder.getAbsolutePath()+sp+en.getKey()+".hdf5";
					
					File outputHDF5File=new File(outputHDF5Filename);
					if (outputHDF5File.exists()) {
						log.warn("Target output file "+outputHDF5Filename+" already exists. SKIPPED");
						continue;
					}
					
					
					(new File(outputHDF5File.getParent())).mkdirs();
					
					File inputFile=en.getValue();
					
					// now read the source file
					try (HkDerivativesTRFileSource src = new HkDerivativesTRFileSource(inputFile.getAbsolutePath())) {

						log.info("Loading data from "+inputFile.getAbsolutePath());
						
						Iterable<HkDerivativesTR> loadedData=src.getData();

						log.info("building HDF5 File: "+outputHDF5Filename);
						HkDerivativesTRHDF5Builder builder = new HkDerivativesTRHDF5Builder(outputHDF5Filename);
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
		
	}*/
	
	
	
	public static void main(String[] args) {
		String inputZipFilesFolderName = args[0];
		String outputFolderName = args[1];

		log.info("Input zip files folder: " + inputZipFilesFolderName);
		log.info("Output folder: " + outputFolderName);

		File inputFolder = new File(inputZipFilesFolderName);
		File outputFolder = new File(outputFolderName);

		if (!inputFolder.isDirectory() || !outputFolder.isDirectory())
			throw new IllegalArgumentException("Input folders or output folders are not directories");

		HkDerivativesTRFile2HDF5 app=new HkDerivativesTRFile2HDF5(inputFolder, outputFolder);
		app.processFiles();

	}

}