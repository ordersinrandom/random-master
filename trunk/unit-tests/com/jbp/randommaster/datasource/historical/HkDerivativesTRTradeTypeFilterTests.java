package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter;
import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter.TradeType;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRTradeTypeFilterTests extends TestCase {

	@Test
	public void testHkDerivativesTRTradeTypeFilterCase1() {
		
		HkDerivativesTR data1 = new HkDerivativesTR("HHI", "O",
				new YearMonth(2012,11), 0.0, "C",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"020");
		HkDerivativesTR data2 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		

		HkDerivativesTRTradeTypeFilter f = new HkDerivativesTRTradeTypeFilter(TradeType.Normal);
		
		boolean result1=f.accept(data1);
		Assert.assertEquals("data1 is incorrectly accepted", false, result1);
		boolean result2=f.accept(data2);
		Assert.assertEquals("data2 is incorrectly rejected", true, result2);
		
	}	

	
	@Test
	public void testHkDerivativesTRTradeTypeFilterCase2() {
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");

		HkDerivativesTR data2 = new HkDerivativesTR("HHI", "O",
				new YearMonth(2012,11), 0.0, "C",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"020");
		

		HkDerivativesTRTradeTypeFilter f = new HkDerivativesTRTradeTypeFilter(TradeType.DeltaHedge);
		
		boolean result1=f.accept(data1);
		Assert.assertEquals("data1 is incorrectly accepted", false, result1);
		boolean result2=f.accept(data2);
		Assert.assertEquals("data2 is incorrectly rejected", true, result2);
		
	}	
	
}
