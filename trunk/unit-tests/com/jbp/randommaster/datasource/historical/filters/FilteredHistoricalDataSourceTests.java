package com.jbp.randommaster.datasource.historical.filters;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRHDF5Source;
import com.jbp.randommaster.datasource.historical.filters.ExpiryMonthFilter;
import com.jbp.randommaster.datasource.historical.filters.FilteredHistoricalDataSource;

import junit.framework.Assert;
import junit.framework.TestCase;

public class FilteredHistoricalDataSourceTests extends TestCase {

	@Test
	public void testLoad20121009() {
		String sp = System.getProperty("file.separator");
		String inputHDF5Filename = System.getProperty("user.dir")+sp+"testing-data"+sp+"201210_01_TR_Parsed.h5";
		
		HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(
				inputHDF5Filename,
				new LocalDate(2012,10,9), 
				"Futures", 
				"HSI");
		
		
		
		FilteredHistoricalDataSource<HkDerivativesTR> src = new FilteredHistoricalDataSource<HkDerivativesTR>(originalSrc, 
				new ExpiryMonthFilter<HkDerivativesTR>(new YearMonth(2012,10)));
		
		
		HkDerivativesTR expected20000 = new HkDerivativesTR("HSI", "F",
					new YearMonth(2012,10), 0.0, "",
					LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
					"001");
		
		
		HkDerivativesTR actual20000 = null;
		
		int rowCount = 0;
		for (HkDerivativesTR data : src.getData()) {
			if (rowCount==20000)
				actual20000=data;
			rowCount++;
		}
		
		int expectedRowCount = 52382;
		
		Assert.assertEquals("Number of result rows not matched", expectedRowCount , rowCount);
		
		Assert.assertEquals("Testing row mismatched",expected20000, actual20000);
		
	}	
	
}
