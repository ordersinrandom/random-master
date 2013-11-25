package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.filters.ExpiryMonthFilter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ExpiryMonthFilterTests extends TestCase {

	@Test
	public void testExpiryMonthFilter() {
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		


		HkDerivativesTR data2 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"001");
		
		
		
		ExpiryMonthFilter<HkDerivativesTR> f=new ExpiryMonthFilter<HkDerivativesTR>(new YearMonth(2012,11));
		
		boolean result1=f.accept(data1);
		Assert.assertEquals("data1 is incorrectly accepted", false, result1);
		boolean result2=f.accept(data2);
		Assert.assertEquals("data2 is incorrectly rejected", true, result2);
		
	}
	
}
