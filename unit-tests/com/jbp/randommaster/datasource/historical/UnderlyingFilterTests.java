package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UnderlyingFilterTests extends TestCase {
	
	@Test
	public void testUnderlyingFilter() {
		
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
		
		UnderlyingFilter<HkDerivativesTRData> f1=new UnderlyingFilter<HkDerivativesTRData>("HSI");
		Assert.assertEquals("f1 incorrectly rejected data1", true, f1.accept(data1));
		Assert.assertEquals("f1 incorrectly accepted data2", false, f1.accept(data2));
		
	}

}
