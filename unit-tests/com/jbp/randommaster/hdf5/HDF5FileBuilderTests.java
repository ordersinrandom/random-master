package com.jbp.randommaster.hdf5;

import java.io.File;

import ncsa.hdf.object.h5.H5File;

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
			
			result.close();
			
		} catch (Exception e2) {
			Assert.fail("Unable to execute createOrOpen() the 2nd time, target file = "+filename);
		}
		
		
		File f=new File(filename);
		if (f.exists()) {
			boolean ok=f.delete();
			if (!ok) 
				Assert.fail("unable to delete the file: "+filename);
		}
		
	}
	
}
