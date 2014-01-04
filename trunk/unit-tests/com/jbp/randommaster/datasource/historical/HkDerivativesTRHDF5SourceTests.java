package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRHDF5SourceTests extends TestCase {

	@Test
	public void testLoad2Files() {
		String sp = System.getProperty("file.separator");
		
		String srcPath = System.getProperty("user.dir") + sp + "testing-data";
		
		String[] inputHDF5Filenames = new String[] { srcPath + sp + "201303_01_TR_Parsed.h5", srcPath + sp + "201304_01_TR_Parsed.h5" };

		HkDerivativesTR expectedRow1 = new HkDerivativesTR("HSI", "F", new YearMonth(2013, 3), 0.0, "",
				LocalDateTime.parse("2013-03-01T09:14:02.000"), 22909.0, 1.0, "020");
		HkDerivativesTR expectedRow1108244 = new HkDerivativesTR("HSI", "F", new YearMonth(2013, 4), 0.0, "",
				LocalDateTime.parse("2013-03-28T16:14:59.000"), 22316.0, 1.0, "001");
		HkDerivativesTR expectedRow1108245 = new HkDerivativesTR("HSI", "F", new YearMonth(2013, 4), 0.0, "",
				LocalDateTime.parse("2013-04-02T09:14:00.000"), 22100.0, 4.0, "020");
		HkDerivativesTR expectedRow2243750 = new HkDerivativesTR("HSI", "F", new YearMonth(2013, 5), 0.0, "",
				LocalDateTime.parse("2013-04-30T16:14:59.000"), 22604.0, 1.0, "001");		
		
		HkDerivativesTR actualRow1 = null;
		HkDerivativesTR actualRow1108244 = null;
		HkDerivativesTR actualRow1108245 = null;
		HkDerivativesTR actualRow2243750 = null;

		int row = 1;
		try (HkDerivativesTRHDF5Source src = new HkDerivativesTRHDF5Source(inputHDF5Filenames, "Futures", "HSI")) {
			for (HkDerivativesTR data : src.getData()) {
				switch (row) {
				case 1:
					actualRow1 = data;
					break;
				case 1108244:
					actualRow1108244 = data;
					break;
				case 1108245:
					actualRow1108245 = data;
					break;
				case 2243750:
					actualRow2243750 = data;
					break;
				default:
					break;
				}
				row++;
			}
		}
		
		row--;
		
		int expectedRowCount = 2243750;
		Assert.assertEquals("Number of result rows not matched", expectedRowCount, row);
		
		Assert.assertEquals("Testing row 1 mismatched", expectedRow1, actualRow1);
		Assert.assertEquals("Testing row 1108244 mismatched", expectedRow1108244, actualRow1108244);
		Assert.assertEquals("Testing row 1108245 mismatched", expectedRow1108245, actualRow1108245);
		Assert.assertEquals("Testing row 2243750 mismatched", expectedRow2243750, actualRow2243750);
		
	}

	@Test
	public void testLoad20121009() {
		String sp = System.getProperty("file.separator");
		String inputHDF5Filename = System.getProperty("user.dir") + sp + "testing-data" + sp + "201210_01_TR_Parsed.h5";

		try (HkDerivativesTRHDF5Source src = new HkDerivativesTRHDF5Source(inputHDF5Filename, new LocalDate(2012, 10, 9), "Futures", "HSI")) {
			HkDerivativesTR expected20000 = new HkDerivativesTR("HSI", "F", new YearMonth(2012, 10), 0.0, "",
					LocalDateTime.parse("2012-10-09T10:21:57.000"), 21078.0, 1.0, "001");

			HkDerivativesTR actual20000 = null;

			int rowCount = 0;
			for (HkDerivativesTR data : src.getData()) {
				if (rowCount == 20000)
					actual20000 = data;
				rowCount++;
			}

			int expectedRowCount = 52921;

			Assert.assertEquals("Number of result rows not matched", expectedRowCount, rowCount);

			Assert.assertEquals("Testing row mismatched", expected20000, actual20000);
		}
	}

}
