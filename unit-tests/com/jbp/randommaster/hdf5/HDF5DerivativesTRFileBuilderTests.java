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

import com.jbp.randommaster.datasource.historical.DerivativesTRFileData;
import com.jbp.randommaster.datasource.historical.DerivativesTRFileSource;

import junit.framework.TestCase;

public class HDF5DerivativesTRFileBuilderTests extends TestCase {

	private File unzipTestingFile(String testingZipFile) throws IOException {
		ZipFile zipFile = null;
		
		File tempUnzippedFile = null;
		try {
			zipFile=new ZipFile(new File(testingZipFile));
			for (Enumeration<? extends ZipEntry> en=zipFile.entries();en.hasMoreElements();) {
				ZipEntry entry=en.nextElement();
				
				// get the input stream
				InputStream ins=zipFile.getInputStream(entry);
				
				// unzip it to the temp file first.
				tempUnzippedFile=File.createTempFile("testBuildingHDF5HkexTRFile", null);
				String tempFilename = tempUnzippedFile.getAbsolutePath();
				byte[] outBuf = new byte [1024*100]; // 100k buffer
				FileOutputStream outs=new FileOutputStream(tempFilename);
				int count=-1;
				while ((count=ins.read(outBuf))!=-1) {
					outs.write(outBuf, 0, count);
				}
				ins.close();
				outs.close();
				// finished unzip
			}
		} finally {
			if (zipFile!=null)
				zipFile.close();
		}
		
		// mark as auto delete.
		if (tempUnzippedFile!=null)
			tempUnzippedFile.deleteOnExit();
		
		return tempUnzippedFile;
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testBuildingHDF5HkexTRFile() throws IOException {
		
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
		
		
		
		File tempUnzippedFile = unzipTestingFile(testingZipFile);
		if (tempUnzippedFile==null)
			throw new IOException("unable to unzip file "+testingZipFile);
		
		String tempFilename = tempUnzippedFile.getAbsolutePath();
		
		// now read the source file
		DerivativesTRFileSource src = new DerivativesTRFileSource(tempFilename);
		
		Iterable<DerivativesTRFileData> loadedData=src.getData();
		
		HDF5DerivativesTRFileBuilder builder = new HDF5DerivativesTRFileBuilder(testingOutputH5File);
		builder.createOrOpen();
		builder.createCompoundDatasetsForTRData(loadedData);
		builder.closeFile();
			

		
		// read the result and check it
		H5File h5ReadOnlyFile=null;
		try {
			FileFormat format=FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
			h5ReadOnlyFile=(H5File) format.createInstance(testingOutputH5File, FileFormat.READ);
			
			String loadPath = "/Futures/HHI/2012/10/03/TRData";
			HObject dataset=h5ReadOnlyFile.get(loadPath);
			Assert.assertNotNull("Cannot load from "+loadPath+", result object is null", dataset);
			if (dataset!=null) {
				Assert.assertTrue("Result object from "+loadPath+" is not H5CompoundDS", dataset instanceof H5CompoundDS);
				if (dataset instanceof H5CompoundDS) {
					H5CompoundDS ds = (H5CompoundDS) dataset;
					Object dsData=ds.getData();
					if (dsData!=null && dsData instanceof Vector) {
						
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
					
					// setup the testing expected data
					String[] expectedClassCode=new String[7];
					String[] expectedFuturesOrOptions = new String[7];
					long[] expectedExpiryMonth = new long[7];
					double[] expectedStrike = new double[7];
					String[] expectedCallPut = new String[7];
					String[] expectedTradeType = new String[7];
					for (int i=0;i<expectedClassCode.length;i++) {
						expectedClassCode[i]="HHI";
						expectedFuturesOrOptions[i]="F";
						expectedExpiryMonth[i]=1349020800000L;
						expectedStrike[i]=0.0;
						expectedCallPut[i]="";
						expectedTradeType[i]="020";
					}
					long[] expectedTimestamp = new long[] {1349226841000L,1349226841000L,1349226841000L,1349226841000L,1349226841000L,1349226903000L,1349226961000L};
					double[] expectedPrice = new double[] {9827.0,9827.0,9827.0,9827.0,9827.0,9987.0,9820.0};
					double[] expectedQty = new double[] {1.0,2.0,2.0,2.0,2.0,3.0,5.0};
					// testing data setup done
					
					// actual loaded data
					String[] classCode = (String[]) ((Vector) dsData).get(0);
					String[] futuresOrOptions = (String[]) ((Vector) dsData).get(1);
					long[] expiryMonth = (long[]) ((Vector) dsData).get(2);
					double[] strike = (double[]) ((Vector) dsData).get(3);
					String[] callPut = (String[]) ((Vector) dsData).get(4);
					long[] timestamp = (long[]) ((Vector) dsData).get(5);
					double[] price = (double[]) ((Vector) dsData).get(6);
					double[] qty = (double[]) ((Vector) dsData).get(7);
					String[] tradeType = (String[]) ((Vector) dsData).get(8);
					
					// reconciliation of expected vs actual
					stringArrayRecon("ClassCode", expectedClassCode, classCode);
					stringArrayRecon("FuturesOrOptions", expectedFuturesOrOptions, futuresOrOptions);
					longArrayRecon("ExpiryMonth", expectedExpiryMonth, expiryMonth);
					doubleArrayRecon("Strike", expectedStrike, strike, 0.0000001);
					stringArrayRecon("CallPut", expectedCallPut, callPut);
					longArrayRecon("Timestamp", expectedTimestamp, timestamp);
					doubleArrayRecon("Price", expectedPrice, price, 0.0000001);
					doubleArrayRecon("Qty", expectedQty, qty, 0.000001);
					stringArrayRecon("TradeType", expectedTradeType, tradeType);
					
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
		
		//System.out.println("Temp out file: "+testingOutputH5File);

		
		// delete the temp h5 file.
		
		File f2=new File(testingOutputH5File);
		if (f2.exists())
			f2.delete();
			
		
	}
	
	
	private void stringArrayRecon(String colName, String[] expected, String[] actual) {
		Assert.assertNotNull(colName+" input rows are null", actual);
		if (actual!=null) {
			Assert.assertEquals(colName+" row count mismatched", expected.length, actual.length);
			if (expected.length==actual.length) {
				for (int i=0;i<expected.length;i++) {
					Assert.assertEquals(colName+" row "+i+" mismatched", expected[i], actual[i]);
				}
			}
		}
	}

	private void doubleArrayRecon(String colName, double[] expected, double[] actual, double threshold) {
		Assert.assertNotNull(colName+" input rows are null", actual);
		if (actual!=null) {
			Assert.assertEquals(colName+" row count mismatched", expected.length, actual.length);
			if (expected.length==actual.length) {
				for (int i=0;i<expected.length;i++) {
					Assert.assertEquals(colName+" row "+i+" mismatched", expected[i], actual[i], threshold);
				}
			}
		}
	}
	
	private void longArrayRecon(String colName, long[] expected, long[] actual) {
		Assert.assertNotNull(colName+" input rows are null", actual);
		if (actual!=null) {
			Assert.assertEquals(colName+" row count mismatched", expected.length, actual.length);
			if (expected.length==actual.length) {
				for (int i=0;i<expected.length;i++) {
					Assert.assertEquals(colName+" row "+i+" mismatched", expected[i], actual[i]);
				}
			}
		}
	}	
}
