package com.jbp.randommaster.hdf5;

import java.io.File;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * 
 * Provides utility function for HDF5 file creation and update.
 * 
 */
public class HDF5Builder {
	
	// logger instance
	static Logger log=Logger.getLogger(HDF5Builder.class);

	private String targetFilename;
	private H5File h5File;
	
	protected HDF5Builder(String targetFilename) {
		this.targetFilename=targetFilename;
	}
	
	public String getTargetFilename() {
		return targetFilename;
	}
	
	/**
	 * Get the H5File object. Only available after calling createOrOpen()
	 */
	protected H5File getHDF5File() {
		return h5File;
	}
	
	/**
	 * Close the working file object. It is supposed to be called by the user of the builder
	 */
	public void closeFile() {
		if (h5File!=null) {
			try {
				h5File.close();
				log.info(targetFilename+" closed");
			} catch (Exception e1) {
				log.warn("unable to close "+targetFilename, e1);
				throw new HDF5BuilderException("unable to close "+targetFilename, e1);
			} finally {
				h5File=null;
			}
		}
	}
	
	
	/**
	 * Create or open the target file.
	 * 
	 * @throws HDF5BuilderException in case it cannot create or open the file.
	 */
	public void createOrOpen() {
		
		FileFormat format=FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		
		File fileObj=new File(targetFilename);
		if (fileObj.exists()) {
			log.info("Opening HDF5 target file: "+targetFilename);
			try {
				h5File=(H5File) format.createInstance(targetFilename, FileFormat.WRITE);
				//h5File.setWritable(true);
			} catch (Exception e1) {
				log.fatal("Unable to open HDF5 target file: "+targetFilename, e1);
				throw new HDF5BuilderException("Unable to open HDF5 target file: "+targetFilename, e1);
			} finally {
				if (h5File!=null) {
					try {
						h5File.close();
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
				h5File = (H5File) format.createFile(targetFilename,  FileFormat.FILE_CREATE_DELETE);
			} catch (Exception e1) {
				log.fatal("Unable to create HDF5 target file: "+targetFilename, e1);
				throw new HDF5BuilderException("Unable to create HDF5 target file: "+targetFilename, e1);
			} finally {
				if (h5File!=null) {
					try {
						h5File.close();
					} catch (Exception e2) {
						log.warn("Unable to close HDF5 target file on excepiton case: "+targetFilename, e2);
						// just log and discard this error.
					}
				}
			}
			log.info("HDF5 target file: "+targetFilename+" created");
		}

		
	}
	
	/**
	 * Create or get the relevant groups for a given instrument code and a date.
	 * 
	 * The path of the group is /instrumentCode/yearStr/monthStr/dayStr
	 * 
	 * @return The H5Group object
	 * 
	 */
	protected H5Group createOrGetInstrumentAndDateGroup(String instrumentType, String underlying, LocalDate date) {
		
		if (h5File==null || !h5File.canWrite())
			throw new IllegalStateException("Unable to createInstrumentAndDateGroups(). The file is null or read-only");

		String yearStr=date.toString(DateTimeFormat.forPattern("yyyy"));
		String monthStr=date.toString(DateTimeFormat.forPattern("MM"));
		String dayStr=date.toString(DateTimeFormat.forPattern("dd"));
		
		try {
			String path = "/" + instrumentType + "/" + underlying + "/" + yearStr + "/" + monthStr + "/" + dayStr;
			log.info("Trying to search the group: "+path);
			
			H5Group existingGroup = (H5Group) h5File.get(path);
			if (existingGroup!=null)
				return existingGroup;
			
			H5Group root=(H5Group) h5File.get("/");
			H5Group instrGroup=getOrCreateSubGroup(root, instrumentType);
			H5Group underlyingGroup=getOrCreateSubGroup(instrGroup, underlying);
			H5Group yearGroup=getOrCreateSubGroup(underlyingGroup, yearStr);
			H5Group monthGroup=getOrCreateSubGroup(yearGroup, monthStr);
			H5Group dayGroup=getOrCreateSubGroup(monthGroup, dayStr);
			
			
			return dayGroup;
			
			
		} catch (Exception e1) {
			
			log.fatal("Unable to createInstrumentAndDateGroups("+instrumentType+", "+underlying+", "+date+")", e1);
			throw new HDF5BuilderException("Unable to createInstrumentAndDateGroups("+instrumentType+", "+underlying+", "+date+")", e1);
		}
		
		
	}
	
	/**
	 * Helper function for subclasses
	 * @param parentGroup The group to attach a subgroup
	 * @param name The name of the subgroup.
	 * @return The existing group under parent group, or create a new one.
	 */
	protected H5Group getOrCreateSubGroup(H5Group parentGroup, String name) {
		

		if (h5File==null || !h5File.canWrite())
			throw new IllegalStateException("Unable to getOrCreateSubGroup(). The file is null or read-only");
		
		
		H5Group targetGroup = null;

		try {
			HObject targetObj = h5File.get(parentGroup.getFullName()+"/"+name);
			if (targetObj!=null && targetObj instanceof H5Group)
				targetGroup = (H5Group) targetObj;
		} catch (Exception e1) {
			// just treat it as null
			targetGroup = null;
		}
		
		if (targetGroup==null) {
		
			try {
				targetGroup=(H5Group) h5File.createGroup(name, parentGroup);
			} catch (Exception e1) {
				throw new HDF5BuilderException("unable to create group: "+name, e1);
			}
		}
		
		return targetGroup;
		
	}
	
	
	
}
