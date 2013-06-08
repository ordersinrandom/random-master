package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRHDF5SourceTests extends TestCase {

	
	@Test
	public void testLoad20121009() {
		String sp = System.getProperty("file.separator");
		String inputHDF5Filename = System.getProperty("user.dir")+sp+"testing-data"+sp+"201210_01_TR_Parsed.h5";
		
		HkDerivativesTRHDF5Source src=new HkDerivativesTRHDF5Source(
				inputHDF5Filename,
				new LocalDate(2012,10,9), 
				"Futures", 
				"HSI", 
				new YearMonth(2012,10));
		
		HkDerivativesTRTuple tuple20000 = new HkDerivativesTRTuple("HSI", "F",
					new YearMonth(2012,10), 0.0, "",
					LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
					"001");
		HkDerivativesTRData expected20000 = new HkDerivativesTRData(tuple20000);
		
		HkDerivativesTRData actual20000 = null;
		
		int rowCount = 0;
		for (HkDerivativesTRData data : src.getData()) {
			if (rowCount==20000)
				actual20000=data;
			rowCount++;
		}
		
		int expectedRowCount = 52382;
		
		Assert.assertEquals("Number of result rows not matched", expectedRowCount , rowCount);
		
		Assert.assertEquals("Testing row mismatched",expected20000, actual20000);
		
	}
	
}
