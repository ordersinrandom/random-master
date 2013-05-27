package com.jbp.randommaster.hdf5;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * 
 * Provides utility function for HDF5 file creation and update
 * 
 */
public class HDF5FileBuilder {
	
	// logger instance
	static Logger log=Logger.getLogger(HDF5FileBuilder.class);

	private String targetFilename;
	private H5File h5File;
	
	public HDF5FileBuilder(String targetFilename) {
		this.targetFilename=targetFilename;
	}
	
	public String getTargetFilename() {
		return targetFilename;
	}
	
	/**
	 * Get the H5File object. Only available after calling createOrOpen()
	 */
	public H5File getHDF5File() {
		return h5File;
	}
	
	
	
	/**
	 * Create or open the target file.
	 * 
	 * @throws HDF5FileBuilderException in case it cannot create or open the file.
	 */
	public void createOrOpen() {
		
		FileFormat format=FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		
		File fileObj=new File(targetFilename);
		if (fileObj.exists()) {
			log.info("Opening HDF5 target file: "+targetFilename);
			try {
				h5File=(H5File) format.createInstance(targetFilename, FileFormat.WRITE);
			} catch (Exception e1) {
				log.fatal("Unable to open HDF5 target file: "+targetFilename, e1);
				throw new HDF5FileBuilderException("Unable to open HDF5 target file: "+targetFilename, e1);
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
				throw new HDF5FileBuilderException("Unable to create HDF5 target file: "+targetFilename, e1);
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
	 * Create the relevant groups for a given instrument code and a date.
	 * 
	 * @return The H5Group object
	 * 
	 */
	public H5Group createInstrumentAndDateGroups(String instrumentCode, LocalDate date) {
		
		if (h5File==null || !h5File.canWrite())
			throw new IllegalStateException("Unable to createInstrumentAndDateGroups(). The file is null or read-only");

		String yearStr=date.toString(DateTimeFormat.forPattern("yyyy"));
		String monthStr=date.toString(DateTimeFormat.forPattern("MM"));
		String dayStr=date.toString(DateTimeFormat.forPattern("dd"));
		
		
		
		
		try {
			String path = "/"+instrumentCode+"/"+yearStr+"/"+monthStr+"/"+dayStr;
			log.info("Trying to search the group: "+path);
			
			H5Group existingGroup = (H5Group) h5File.get(path);
			if (existingGroup!=null)
				return existingGroup;
			
			H5Group root=(H5Group) h5File.get("/");
			H5Group instrGroup=getOrCreateSubGroup(root, instrumentCode);
			H5Group yearGroup=getOrCreateSubGroup(instrGroup, yearStr);
			H5Group monthGroup=getOrCreateSubGroup(yearGroup, monthStr);
			H5Group dayGroup=getOrCreateSubGroup(monthGroup, dayStr);
			
			
			return dayGroup;
			
			
		} catch (Exception e1) {
			
			log.fatal("Unable to createInstrumentAndDateGroups("+instrumentCode+", "+date+")", e1);
			throw new HDF5FileBuilderException("Unable to createInstrumentAndDateGroups("+instrumentCode+", "+date+")", e1);
		}
		
		
	}
	
	
	public H5Group getOrCreateSubGroup(H5Group parentGroup, String name) {
		

		if (h5File==null || !h5File.canWrite())
			throw new IllegalStateException("Unable to getOrCreateSubGroup(). The file is null or read-only");
		
		
		List<HObject> members=parentGroup.getMemberList();
		for (HObject o : members) {
			if (o instanceof H5Group) {
				H5Group g = (H5Group) o;
				if (name.equals(g.getName())) {
					return g;
				}
			}
		}
		
		try {
			H5Group result=(H5Group) h5File.createGroup(name, parentGroup);
			return result;
		} catch (Exception e1) {
			throw new HDF5FileBuilderException("unable to create group: "+name, e1);
		}
		
	}
	
}
