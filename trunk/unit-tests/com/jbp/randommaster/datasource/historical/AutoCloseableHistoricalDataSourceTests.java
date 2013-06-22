package com.jbp.randommaster.datasource.historical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AutoCloseableHistoricalDataSourceTests extends TestCase {

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
		
		int breakAtIndex = 3;
		int currentIndex = 0;

		// try sudden break out from the loop
		try (AutoCloseableHistoricalDataSource<HkDerivativesTR> src=new HkDerivativesTRFileSource(tempFile.getAbsolutePath())) {
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
		
		// try no breaking from the loop
		try (HkDerivativesTRFileSource src=new HkDerivativesTRFileSource(tempFile.getAbsolutePath())) {
			Iterable<HkDerivativesTR> data=src.getData();

			for (@SuppressWarnings("unused") HkDerivativesTR d : data) {
				Assert.assertEquals("Inside loop not requires cleanup. it is in error state.", true, src.requiresCleanUp());
				
			}
			
			Assert.assertEquals("No loop breaking but requires cleanup. it is in error state.", false, src.requiresCleanUp());
		}		
		
	}	
	
}
