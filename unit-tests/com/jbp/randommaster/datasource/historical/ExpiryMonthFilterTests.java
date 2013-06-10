package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ExpiryMonthFilterTests extends TestCase {

	@Test
	public void testExpiryMonthFilter() {
		
		HkDerivativesTRTuple tuple1 = new HkDerivativesTRTuple("HSI", VanillaDerivativesDataTuple.FuturesOptions.FUTURES,
				new YearMonth(2012,10), 0.0, VanillaDerivativesDataTuple.CallPut.NA,
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");
		HkDerivativesTRData data1 = new HkDerivativesTRData(tuple1);


		HkDerivativesTRTuple tuple2 = new HkDerivativesTRTuple("HHI", VanillaDerivativesDataTuple.FuturesOptions.FUTURES,
				new YearMonth(2012,11), 0.0, VanillaDerivativesDataTuple.CallPut.NA,
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"001");
		HkDerivativesTRData data2 = new HkDerivativesTRData(tuple2);
		
		
		ExpiryMonthFilter<HkDerivativesTRData> f=new ExpiryMonthFilter<HkDerivativesTRData>(new YearMonth(2012,11));
		
		boolean result1=f.accept(data1);
		Assert.assertEquals("data1 is incorrectly accepted", false, result1);
		boolean result2=f.accept(data2);
		Assert.assertEquals("data2 is incorrectly rejected", true, result2);
		
	}
	
}
