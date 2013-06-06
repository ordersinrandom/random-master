package com.jbp.randommaster.hdf5;


import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class HDF5FileBuilderTests extends TestCase {

	@Test
	public void testCreateOrOpen() {
		
		
		String tempFolder = System.getProperty("java.io.tmpdir");
		
		String filename = tempFolder+"HDF5FileBuilderUnitTest-TempFile.h5";
		
		HDF5FileBuilder b=new HDF5FileBuilder(filename);

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
		
		
		H5Group g1=b.createOrGetInstrumentAndDateGroup("Futures", "MyBday", new LocalDate(2013, 05, 04));
		
		//System.out.println("fullname: "+g1.getFullName());
		
		Assert.assertEquals("The group name doesn't match", "/Futures/MyBday/2013/05/04", g1.getFullName());

		try {
			b.getHDF5File().close();
		} catch (Exception e1) {
			Assert.fail("Unable to close the file after use, target file = "+filename);
		}
		
		/*
		File f=new File(filename);
		if (f.exists()) {
			boolean ok=f.delete();
			if (!ok) 
				Assert.fail("unable to delete the file: "+filename);
		}*/
		
	}
	
	
	
}
