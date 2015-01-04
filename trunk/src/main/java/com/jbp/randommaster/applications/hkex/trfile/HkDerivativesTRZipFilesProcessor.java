package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
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

	private boolean isZip(File f) {
		return f.isFile() && f.getAbsolutePath().toLowerCase().endsWith(".zip");
	}

	private boolean isGZip(File f) {
		return f.isFile() && f.getAbsolutePath().toLowerCase().endsWith(".gz");
	}

	/**
	 * Unzip the input files and then processes each entry one by one.
	 */
	public void processFiles() {

		File[] allCompressedFiles = inputFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File givenFile) {
				return isZip(givenFile) || isGZip(givenFile);
			}
		});

		// loop through all the files and process it.
		for (File compressedFile : allCompressedFiles) {
			log.info("Processing " + compressedFile.getAbsolutePath());

			Map<String, File> tempUnzippedFiles = null;
			ZipFile zip = null;
			try {
				log.info("Unzipping " + compressedFile.getAbsolutePath());
				if (isZip(compressedFile)) {
					// open the zip file
					zip = new ZipFile(compressedFile);
					tempUnzippedFiles = ZipUtils.unzipToTempFiles(zip, "DerivativesTRFile2HDF5_temp_", true);
					log.info("Unzip " + compressedFile.getAbsolutePath() + " finished");
				} else if (isGZip(compressedFile)) {
					zip = null; // make sure it doesn't close something not in
								// this loop
					tempUnzippedFiles = new TreeMap<>();
					File tempFile = ZipUtils.gunzipToTempFile(compressedFile, "DerivativesTRFile2HDF5_temp_", true);
					log.info("GUnzipped " + compressedFile.getAbsolutePath() + " finished");
					// remove the ".gz" at the end to make it consistent with
					// zip output.
					String gzipFilename = compressedFile.getName();
					if (gzipFilename.toLowerCase().endsWith(".gz"))
						gzipFilename = gzipFilename.substring(0, gzipFilename.length() - 3);
					tempUnzippedFiles.put(gzipFilename, tempFile);
				}
			} catch (IOException e1) {
				log.warn("Unable to unzip the file " + compressedFile.getAbsolutePath() + ". SKIPPED", e1);
			} finally {
				try {
					if (zip != null)
						zip.close();
				} catch (Exception e1) {
					log.warn("Unable to close the zip file: " + compressedFile.getAbsolutePath(), e1);
				}
			}

			if (tempUnzippedFiles != null) {

				for (Map.Entry<String, File> en : tempUnzippedFiles.entrySet()) {

					File inputFile = en.getValue();

					// now read the source file
					try (HkDerivativesTRFileSource src = new HkDerivativesTRFileSource(inputFile.getAbsolutePath())) {

						log.info("Loading data from " + inputFile.getAbsolutePath());

						Iterable<HkDerivativesTR> loadedData = src.getData();

						// invoke subclass to handle this
						processHkDerivativesTRInput(compressedFile, en.getKey(), loadedData);

					} catch (FileNotFoundException fnf) {
						log.warn("Unable to find the unzipped temp file: " + inputFile.getAbsolutePath() + ". File discarded.", fnf);
					}
				}
			}

			log.info("File processing finished: " + compressedFile.getAbsolutePath());

		}

	}

	/**
	 * Subclass implementing this to handle each zip entry.
	 * 
	 * @param srcZipFile
	 *            The zip file that we are processing.
	 * @param zipEntryKey
	 *            The zip entry key within the zip file
	 * @param loadedData
	 *            The loaded HkDerivativesTR raw data.
	 */
	protected abstract void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData);

}
