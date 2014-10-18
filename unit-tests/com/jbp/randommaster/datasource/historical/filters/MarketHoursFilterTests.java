package com.jbp.randommaster.datasource.historical.filters;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.utils.MarketHours;

import junit.framework.TestCase;
import junit.framework.Assert;

public class MarketHoursFilterTests extends TestCase {

	@Test
	public void testHolidaysFilterCase1() {

		HkDerivativesTR data0 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T09:14:59.050"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T09:15:00.000"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data2 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data3 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"020");

		HkDerivativesTR data4 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T16:15:00.000"), 21081.0, 1.0,
				"020");
		
		HkDerivativesTR data5 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T16:15:00.010"), 21081.0, 1.0,
				"020");		
		

		MarketHoursFilter<HkDerivativesTR> f = new MarketHoursFilter<>(MarketHours.HongKongDerivatives, true, false);
		
		boolean accept0 = f.accept(data0);
		Assert.assertEquals("Data0 is accepted", false, accept0);
		boolean accept1 = f.accept(data1);
		Assert.assertEquals("Data1 is accepted", false, accept1);
		boolean accept2 = f.accept(data2);
		Assert.assertEquals("Data2 is not accepted", true, accept2);
		boolean accept3 = f.accept(data3);
		Assert.assertEquals("Data3 is accepted", false, accept3);
		boolean accept4 = f.accept(data4);
		Assert.assertEquals("Data4 is not accepted", true, accept4);
		boolean accept5 = f.accept(data5);
		Assert.assertEquals("Data5 is accepted", false, accept5);
		
		
	}	

	
	@Test
	public void testHolidaysFilterCase2() {

		HkDerivativesTR data0 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T09:14:59.050"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T09:15:00.000"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data2 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		
		HkDerivativesTR data3 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"020");

		HkDerivativesTR data4 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T16:15:00.000"), 21081.0, 1.0,
				"020");
		
		HkDerivativesTR data5 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T16:15:00.010"), 21081.0, 1.0,
				"020");		
		

		MarketHoursFilter<HkDerivativesTR> f = new MarketHoursFilter<>(MarketHours.HongKongDerivatives, false, true);
		
		boolean accept0 = f.accept(data0);
		Assert.assertEquals("Data0 is accepted", false, accept0);
		boolean accept1 = f.accept(data1);
		Assert.assertEquals("Data1 is not accepted", true, accept1);
		boolean accept2 = f.accept(data2);
		Assert.assertEquals("Data2 is not accepted", true, accept2);
		boolean accept3 = f.accept(data3);
		Assert.assertEquals("Data3 is accepted", false, accept3);
		boolean accept4 = f.accept(data4);
		Assert.assertEquals("Data4 is accepted", false, accept4);
		boolean accept5 = f.accept(data5);
		Assert.assertEquals("Data5 is accepted", false, accept5);
		
		
	}	
	
}