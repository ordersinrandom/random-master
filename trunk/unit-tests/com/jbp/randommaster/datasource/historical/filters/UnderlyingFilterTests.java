package com.jbp.randommaster.datasource.historical.filters;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.filters.UnderlyingFilter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UnderlyingFilterTests extends TestCase {
	
	@Test
	public void testUnderlyingFilter() {
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		


		HkDerivativesTR data2 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"001");

		
		UnderlyingFilter<HkDerivativesTR> f1=new UnderlyingFilter<HkDerivativesTR>("HSI");
		Assert.assertEquals("f1 incorrectly rejected data1", true, f1.accept(data1));
		Assert.assertEquals("f1 incorrectly accepted data2", false, f1.accept(data2));
		
	}

}
