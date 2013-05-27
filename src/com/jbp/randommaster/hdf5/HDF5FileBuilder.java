package com.jbp.randommaster.hdf5;

import java.io.File;

import org.apache.log4j.Logger;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

/**
 * 
 * Provides utility function for HDF5 file creation and update
 * 
 */
public class HDF5FileBuilder {
	
	// logger instance
	static Logger log=Logger.getLogger(HDF5FileBuilder.class);

	private String targetFilename;
	
	public HDF5FileBuilder(String targetFilename) {
		this.targetFilename=targetFilename;
	}
	
	public String getTargetFilename() {
		return targetFilename;
	}
	
	/**
	 * Create or open the target file.
	 * 
	 * @return The opened H5File object
	 * 
	 * @throws HDF5FileBuilderException in case it cannot create or open the file.
	 */
	public H5File createOrOpen() {
		
		FileFormat format=FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		H5File resultFile=null;
		
		File fileObj=new File(targetFilename);
		if (fileObj.exists()) {
			log.info("Opening HDF5 target file: "+targetFilename);
			try {
				resultFile=(H5File) format.createInstance(targetFilename, FileFormat.WRITE);
			} catch (Exception e1) {
				log.fatal("Unable to open HDF5 target file: "+targetFilename, e1);
				throw new HDF5FileBuilderException("Unable to open HDF5 target file: "+targetFilename, e1);
			} finally {
				if (resultFile!=null) {
					try {
						resultFile.close();
					} catch (Exception e2) {
						log.warn("Unable to close HDF5 target file on excepiton case: "+targetFilename, e2);
						// just log and discard this error.
					}
				}
			}
			log.info("HDF5 target file: "+targetFilename+" opened");
			
		}
		else {
			log.info("Creating HDF5 file: "+targetFilename);
			try {
				resultFile = (H5File) format.createFile(targetFilename,  FileFormat.FILE_CREATE_DELETE);
			} catch (Exception e1) {
				log.fatal("Unable to create HDF5 target file: "+targetFilename, e1);
				throw new HDF5FileBuilderException("Unable to create HDF5 target file: "+targetFilename, e1);
			} finally {
				if (resultFile!=null) {
					try {
						resultFile.close();
					} catch (Exception e2) {
						log.warn("Unable to close HDF5 target file on excepiton case: "+targetFilename, e2);
						// just log and discard this error.
					}
				}
			}
			log.info("HDF5 target file: "+targetFilename+" created");
		}

		return resultFile;
	}
	
	
}
