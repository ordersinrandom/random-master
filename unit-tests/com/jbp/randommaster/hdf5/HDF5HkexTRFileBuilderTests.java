package com.jbp.randommaster.hdf5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HistoricalDataSourceException;
import com.jbp.randommaster.datasource.historical.HkexTRFileData;
import com.jbp.randommaster.datasource.historical.HkexTRFileSource;

import junit.framework.TestCase;

public class HDF5HkexTRFileBuilderTests extends TestCase {

	@Test
	public void testBuildingHDF5HkexTRFile() throws IOException, HistoricalDataSourceException {
		
		String zipFilename = "201210_01_TR_UnitTest.zip";
		//String csvFilename = "201210_01_TR.csv";
		String sp = System.getProperty("file.separator");
		
		String testingZipFile = System.getProperty("user.dir")+sp+"testing-data"+sp+zipFilename;
		
		String h5Filename = "201210_01_TR_UnitTest.h5";
		String testingOutputH5File = System.getProperty("user.dir")+sp+"testing-data"+sp+h5Filename;
		
		// delete it if the testing file already exists.
		File f=new File(testingOutputH5File);
		if (f.exists())
			f.delete();
		
		
		ZipFile zipFile = null;
		
		File tempFile = null;
		try {
			zipFile=new ZipFile(new File(testingZipFile));
			for (Enumeration<? extends ZipEntry> en=zipFile.entries();en.hasMoreElements();) {
				ZipEntry entry=en.nextElement();
				
				// get the input stream
				InputStream ins=zipFile.getInputStream(entry);
				
				// unzip it to the temp file first.
				tempFile=File.createTempFile("testBuildingHDF5HkexTRFile", null);
				String tempFilename = tempFile.getAbsolutePath();
				byte[] outBuf = new byte [1024*100]; // 100k buffer
				FileOutputStream outs=new FileOutputStream(tempFilename);
				int count=-1;
				while ((count=ins.read(outBuf))!=-1) {
					outs.write(outBuf, 0, count);
				}
				ins.close();
				outs.close();
				// finished unzip
				
				// now read the source file
				HkexTRFileSource src = new HkexTRFileSource(tempFilename);
				
				Iterable<HkexTRFileData> loadedData=src.getData();
				
				HDF5HkexTRFileBuilder builder = new HDF5HkexTRFileBuilder(testingOutputH5File);
				builder.createOrOpen();
				builder.createCompoundDSForTRData(loadedData);
				builder.closeFile();
				
				
			}
		} finally {
			if (zipFile!=null)
				zipFile.close();
		}
		
		

		
		
		// read the result and check it
		H5File h5ReadOnlyFile=null;
		try {
			FileFormat format=FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
			h5ReadOnlyFile=(H5File) format.createInstance(testingOutputH5File, FileFormat.READ);
			
			String loadPath = "/HHI/2012/10/03/TRData";
			HObject dataset=h5ReadOnlyFile.get(loadPath);
			Assert.assertNotNull("Cannot load from "+loadPath+", result object is null", dataset);
			if (dataset!=null) {
				Assert.assertTrue("Result object from "+loadPath+" is not H5CompoundDS", dataset instanceof H5CompoundDS);
				if (dataset instanceof H5CompoundDS) {
					H5CompoundDS ds = (H5CompoundDS) dataset;
					Object dsData=ds.getData();
					if (dsData!=null && dsData instanceof Vector) {
						
						// TODO: more checking on those result data later.
						@SuppressWarnings("rawtypes")
						Vector v = (Vector) dsData;
						Assert.assertEquals("Not exactly 9 columns", 9, v.size());
						
						Object item0=v.get(0);
						if (item0!=null && item0 instanceof String[]) {
							String[] classCode = (String[]) item0;
							Assert.assertEquals("Not exactly 7 rows", 7, classCode.length);
						}
						else Assert.fail("Unexpected first column data type: "+item0);
						
					}
					else Assert.fail("Unexpected dataset data: "+dsData);
				}
			}
			
		} catch (Exception e1) {
			Assert.fail("Caught exception when loading the result file: "+testingOutputH5File+": "+e1.getMessage());
		} finally {
			if (h5ReadOnlyFile!=null) {
				try {
					h5ReadOnlyFile.close();
				} catch (Exception e2) {
					// ignore.
				}
			}
		}
		
		// delete the temp h5 file.
		File f2=new File(testingOutputH5File);
		if (f2.exists())
			f2.delete();
		
	}
	
}
