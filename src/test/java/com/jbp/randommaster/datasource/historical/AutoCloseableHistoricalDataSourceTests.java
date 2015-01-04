package com.jbp.randommaster.datasource.historical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Test;

import com.jbp.randommaster.datasource.historical.filters.ExpiryMonthFilter;
import com.jbp.randommaster.datasource.historical.filters.FilteredHistoricalDataSource;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AutoCloseableHistoricalDataSourceTests extends TestCase {

	@Test
	public void testFilteredHistoricalDataSourceCleanUp() {
		
		String sp = System.getProperty("file.separator");
		String inputHDF5Filename = System.getProperty("user.dir")+sp+"testing-data"+sp+"201210_01_TR_Parsed.h5";
		
		
		// try full looping
		try (HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(inputHDF5Filename, LocalDate.of(2012,10,9), "Futures", "HSI");
				FilteredHistoricalDataSource<HkDerivativesTR> src = new FilteredHistoricalDataSource<HkDerivativesTR>(originalSrc, new ExpiryMonthFilter<HkDerivativesTR>(YearMonth.of(2012,10)));
				) {
			int rowCount = 0;
			for (@SuppressWarnings("unused") HkDerivativesTR data : src.getData()) {
				if (rowCount>0 && rowCount%10000==0) {
					Assert.assertEquals("Inside loop not requires cleanup. FilteredHistoricalDataSource is in error state.", true, src.requiresCleanUp());
					Assert.assertEquals("Inside loop not requires cleanup. HkDerivativesTRHDF5Source is in error state.", true, originalSrc.requiresCleanUp());
				}			
				rowCount++;
			}
			
			Assert.assertEquals("Outside loop requires cleanup. FilteredHistoricalDataSource is in error state.", false, src.requiresCleanUp());
			Assert.assertEquals("Outside loop requires cleanup. HkDerivativesTRHDF5Source is in error state.", false, originalSrc.requiresCleanUp());
		}
		
		
		// try breaking loop
		HkDerivativesTRHDF5Source s1=null;
		FilteredHistoricalDataSource<HkDerivativesTR> s2=null;
		try (HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(inputHDF5Filename, LocalDate.of(2012,10,9), "Futures", "HSI");
				FilteredHistoricalDataSource<HkDerivativesTR> src = new FilteredHistoricalDataSource<HkDerivativesTR>(originalSrc, new ExpiryMonthFilter<HkDerivativesTR>(YearMonth.of(2012,10)));
				) {
			// save down the final testing pointer.
			s1=originalSrc;
			s2=src;
			
			int rowCount = 0;
			for (@SuppressWarnings("unused") HkDerivativesTR data : src.getData()) {
				if (rowCount>0 && rowCount%10000==0) {
					Assert.assertEquals("Inside loop not requires cleanup. FilteredHistoricalDataSource is in error state.", true, src.requiresCleanUp());
					Assert.assertEquals("Inside loop not requires cleanup. HkDerivativesTRHDF5Source is in error state.", true, originalSrc.requiresCleanUp());
				}			
				
				if (rowCount>15000)
					break;
				
				rowCount++;
			}
			
			Assert.assertEquals("Outside loop not requires cleanup. FilteredHistoricalDataSource is in error state.", true, src.requiresCleanUp());
			Assert.assertEquals("Outside loop not requires cleanup. HkDerivativesTRHDF5Source is in error state.", true, originalSrc.requiresCleanUp());
		}

		Assert.assertEquals("Outside try requires cleanup. FilteredHistoricalDataSource is in error state.", false, s2.requiresCleanUp());
		Assert.assertEquals("Outside try requires cleanup. HkDerivativesTRHDF5Source is in error state.", false, s1.requiresCleanUp());
		
		
	}
	
	@Test
	public void testHkDerivativesTRHDF5SourceCleanUp() {
		
		String sp = System.getProperty("file.separator");
		String inputHDF5Filename = System.getProperty("user.dir")+sp+"testing-data"+sp+"201210_01_TR_Parsed.h5";

		
		// try normal looping
		try (HkDerivativesTRHDF5Source src=new HkDerivativesTRHDF5Source(inputHDF5Filename,LocalDate.of(2012,10,9), "Futures", "HSI")) {
			int rowCount = 0;
			for (@SuppressWarnings("unused") HkDerivativesTR data : src.getData()) {
				if (rowCount>0 && rowCount%10000==0) {
					Assert.assertEquals("Inside loop not requires cleanup. it is in error state.", true, src.requiresCleanUp());
				}
				rowCount++;
			}
			Assert.assertEquals("Outside for loop requires cleanup. It is in error state.", false, src.requiresCleanUp());
		}

		// try breaking 
		HkDerivativesTRHDF5Source s1=null;
		try (HkDerivativesTRHDF5Source src=new HkDerivativesTRHDF5Source(inputHDF5Filename,LocalDate.of(2012,10,9), "Futures", "HSI")) {
			int rowCount = 0;
			s1=src;
			
			for (@SuppressWarnings("unused") HkDerivativesTR data : src.getData()) {
				if (rowCount>0 && rowCount%10000==0) {
					Assert.assertEquals("Inside loop not requires cleanup. it is in error state.", true, src.requiresCleanUp());
				}
				
				// break out from looping
				if (rowCount>15000)
					break;

				rowCount++;
			}
			Assert.assertEquals("Outside for loop requires cleanup. It is in error state.", true, src.requiresCleanUp());
		}
		Assert.assertEquals("Outside try block requires cleanup. It is in error state.", false, s1.requiresCleanUp());
		
	}
	
	
	@Test
	public void testHkDerivativesTRFileSourceCleanUp() throws IOException {
		StringBuilder buf=new StringBuilder(1000);
		buf.append("HHI,F,1211,0,,20121031,161458,10607,1,001\n");
		buf.append("HHI,F,1211,0,,20121031,161458,10608,1,001\n");
		buf.append("HHI,F,1211,0,,20121031,161458,10609,1,001\n");
		buf.append("HHI,O,1211,8400,P,20121031,161459,2,5,001\n");
		buf.append("MHI,O,1211,22400,C,20121031,161459,110,1,001");
		
		File tempFile=File.createTempFile("HkexTRFileSourceTests2", null);
		tempFile.deleteOnExit();
		FileWriter fw=new FileWriter(tempFile);
		fw.write(buf.toString());
		fw.close();
		
		
		// try no breaking from the loop
		try (HkDerivativesTRFileSource src=new HkDerivativesTRFileSource(tempFile.getAbsolutePath())) {
			Iterable<HkDerivativesTR> data=src.getData();

			for (@SuppressWarnings("unused") HkDerivativesTR d : data) {
				Assert.assertEquals("Inside loop not requires cleanup. it is in error state.", true, src.requiresCleanUp());
				
			}
			
			Assert.assertEquals("No loop breaking but requires cleanup. it is in error state.", false, src.requiresCleanUp());
		}				
		

		// try sudden break out from the loop
		int breakAtIndex = 3;
		int currentIndex = 0;
		AutoCloseableHistoricalDataSource<HkDerivativesTR> s1=null;
		try (AutoCloseableHistoricalDataSource<HkDerivativesTR> src=new HkDerivativesTRFileSource(tempFile.getAbsolutePath())) {
			s1=src;
			Iterable<HkDerivativesTR> data=src.getData();

			for (@SuppressWarnings("unused") HkDerivativesTR d : data) {
				Assert.assertEquals("Inside loop not requires cleanup. it is in error state.", true, src.requiresCleanUp());
				
				if (breakAtIndex==currentIndex) {
					break;
				}
				
				currentIndex++;
			}
			
			Assert.assertEquals("Loop breaking not requires cleanup. it is in error state.", true, src.requiresCleanUp());
		}
		Assert.assertEquals("Outside try block requires cleanup. it is in error state.", false, s1.requiresCleanUp());
		
	}	
	
}
