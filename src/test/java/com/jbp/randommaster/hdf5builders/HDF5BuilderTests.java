package com.jbp.randommaster.hdf5builders;


import java.time.LocalDate;

import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.hdf5builders.HDF5Builder;

import junit.framework.TestCase;

public class HDF5BuilderTests extends TestCase {

	@Test
	public void testCreateOrOpen() {
		
		
		String tempFolder = System.getProperty("java.io.tmpdir");
		
		String filename = tempFolder+"HDF5FileBuilderUnitTest-TempFile.h5";
		
		HDF5Builder b=new HDF5Builder(filename);

		try {
			b.createOrOpen();
			H5File result=b.getHDF5File();
			Assert.assertNotNull("Result H5 file is null: "+filename, result);
		
			result.close();
			
		} catch (Exception e1) {
			Assert.fail("Unable to execute createOrOpen(), target file = "+filename);
		}
		
		try {
			
			b.createOrOpen(); // this time is open instead of create.
			H5File result=b.getHDF5File();
			Assert.assertNotNull("Result H5 file is null on second round: "+filename, result);
			
		} catch (Exception e2) {
			Assert.fail("Unable to execute createOrOpen() the 2nd time, target file = "+filename);
		}
		
		H5Group g1=b.createOrGetInstrumentAndDateGroup("Futures", "MyBday", LocalDate.of(2013, 05, 04));
		
		
		Assert.assertEquals("The group name doesn't match", "/Futures/MyBday/2013/05/04", g1.getFullName());

		try {
			b.getHDF5File().close();
		} catch (Exception e1) {
			Assert.fail("Unable to close the file after use, target file = "+filename);
		}

		
	}
	
	
	
}
