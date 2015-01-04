package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;

import org.apache.log4j.Logger;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.hdf5builders.HkDerivativesTRHDF5Builder;

/**
 * 
 * This tool takes a folder of HKEX Derivatives TR files, transform them into
 * HDF5 and then save to another output folder.
 * 
 */
public class HkDerivativesTRFile2HDF5 extends HkDerivativesTRZipFilesProcessor {

	static Logger log = Logger.getLogger(HkDerivativesTRFile2HDF5.class);

	private File outputFolder;

	/**
	 * Create an instance of HkDerivativesTRFile2HDF5.
	 * 
	 * @param inputFolder
	 *            The input folder that contains a number of zipped HKEX
	 *            Derivatives TR CSV Files.
	 * @param outputFolder
	 *            The target output folder where we will save down the
	 *            corresponding HDF5 Files.
	 */
	public HkDerivativesTRFile2HDF5(File inputFolder, File outputFolder) {

		super(inputFolder);

		this.outputFolder = outputFolder;

	}

	/**
	 * Implements base class abstract method to create HDF5 file entry.
	 * 
	 * @param srcZipFile
	 *            The original input zip file.
	 * @param zipEntryKey
	 *            The entry key within the source zip file.
	 * @param loadedData
	 *            The TR record loaded from the csv file of that entry.
	 */
	@Override
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {

		String sp = System.getProperty("file.separator");

		String outputHDF5Filename = outputFolder.getAbsolutePath() + sp + zipEntryKey + ".hdf5";

		File outputHDF5File = new File(outputHDF5Filename);
		if (outputHDF5File.exists()) {
			log.warn("Target output file " + outputHDF5Filename + " already exists. SKIPPED");
			return;
		}

		(new File(outputHDF5File.getParent())).mkdirs();

		log.info("building HDF5 File: " + outputHDF5Filename);
		HkDerivativesTRHDF5Builder builder = new HkDerivativesTRHDF5Builder(outputHDF5Filename);
		builder.createOrOpen();
		builder.createCompoundDatasetsForTRData(loadedData);
		builder.closeFile();
		log.info("HDF5 File (" + outputHDF5Filename + ")data written and closed");

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

		HkDerivativesTRFile2HDF5 app = new HkDerivativesTRFile2HDF5(inputFolder, outputFolder);
		app.processFiles();

	}

}
