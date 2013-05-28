package com.jbp.randommaster.hdf5;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HistoricalDataSourceException;
import com.jbp.randommaster.datasource.historical.HkexTRFileData;
import com.jbp.randommaster.datasource.historical.HkexTRFileSource;

import junit.framework.TestCase;

public class HDF5HkexTRFileBuilderTests extends TestCase {

	@Test
	public void testBuildingHDF5HkexTRFile() throws IOException, HistoricalDataSourceException {
		
		String zipFilename = "201210_01_TR_Test.zip";
		//String csvFilename = "201210_01_TR.csv";
		String sp = System.getProperty("file.separator");
		
		String testingZipFile = System.getProperty("user.dir")+sp+"testing-data"+sp+zipFilename;
		
		String h5Filename = "201210_01_TR_Test.h5";
		String testingOutputH5File = System.getProperty("user.dir")+sp+"testing-data"+sp+h5Filename;
		
		ZipFile zipFile = null;
		
		Collection<HkexTRFileData> loadedData = null;
		
		try {
			zipFile=new ZipFile(new File(testingZipFile));
			for (Enumeration<? extends ZipEntry> en=zipFile.entries();en.hasMoreElements();) {
				ZipEntry entry=en.nextElement();
				
				// get the input stream
				InputStream ins=zipFile.getInputStream(entry);
				InputStreamReader reader=new InputStreamReader(ins);
				HkexTRFileSource src = new HkexTRFileSource(reader);
				
				loadedData=src.getData();
				
				ins.close();
				
			}
		} finally {
			if (zipFile!=null)
				zipFile.close();
		}
		
		
		HDF5HkexTRFileBuilder builder = new HDF5HkexTRFileBuilder(testingOutputH5File);
		builder.createOrOpen();
		builder.createCompoundDSForTRData(loadedData);
		builder.closeFile();
	}
	
}
