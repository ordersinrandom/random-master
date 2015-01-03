package com.jbp.randommaster.datasource.historical.filters;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.utils.HolidaysList;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HolidaysFilterTests extends TestCase {

	@Test
	public void testHolidaysFilter() {

		HkDerivativesTR data0 = new HkDerivativesTR("HSI", "F",
				YearMonth.of(2013,10), 0.0, "",
				LocalDateTime.parse("2013-10-01T09:14:59.050"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				YearMonth.of(2013,10), 0.0, "",
				LocalDateTime.parse("2013-10-02T09:15:00.000"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data2 = new HkDerivativesTR("HSI", "F",
				YearMonth.of(2013,10), 0.0, "",
				LocalDateTime.parse("2013-10-05T09:15:00.000"), 21081.0, 1.0,
				"001");

		
		HolidaysFilter<HkDerivativesTR> f = new HolidaysFilter<>(HolidaysList.HongKong);
		
		boolean accept0 = f.accept(data0);
		Assert.assertEquals("Data0 is accepted", false, accept0);
		boolean accept1 = f.accept(data1);
		Assert.assertEquals("Data1 is not accepted", true, accept1);
		boolean accept2 = f.accept(data2);
		Assert.assertEquals("Data2 is accepted", false, accept2);
		
	}
	
	

	
}
