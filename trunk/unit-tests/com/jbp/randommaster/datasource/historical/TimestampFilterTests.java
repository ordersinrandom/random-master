package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.filters.TimestampFilter;

import junit.framework.TestCase;
import junit.framework.Assert;

public class TimestampFilterTests extends TestCase {

	@Test
	public void testTimestampFilter() {
		
		HkDerivativesTR data1 = new HkDerivativesTR("HSI", "F",
				new YearMonth(2012,10), 0.0, "",
				LocalDateTime.parse("2012-10-09T10:23:24.000"), 21081.0, 1.0,
				"001");


		HkDerivativesTR data2 = new HkDerivativesTR("HHI", "F",
				new YearMonth(2012,11), 0.0, "",
				LocalDateTime.parse("2012-10-09T12:23:24.000"), 21081.0, 1.0,
				"001");
		
		
		
		LocalDateTime start=new LocalDateTime(2012, 10, 9, 10, 30, 0);
		LocalDateTime end = new LocalDateTime(2012, 10, 9, 11, 0, 0);
		
		TimestampFilter<HkDerivativesTR> f1=new TimestampFilter<HkDerivativesTR>(null, null);
		Assert.assertEquals("f1 incorrectly rejected data1", true, f1.accept(data1));
		Assert.assertEquals("f1 incorrectly rejected data2", true, f1.accept(data2));
		
		TimestampFilter<HkDerivativesTR> f2=new TimestampFilter<HkDerivativesTR>(start, null);
		Assert.assertEquals("f2 incorrectly accepted data1", false, f2.accept(data1));
		Assert.assertEquals("f2 incorrectly rejected data2", true, f2.accept(data2));
		
		TimestampFilter<HkDerivativesTR> f3=new TimestampFilter<HkDerivativesTR>(null, end);
		Assert.assertEquals("f3 incorrectly rejected data1", true, f3.accept(data1));
		Assert.assertEquals("f3 incorrectly accepted data2", false, f3.accept(data2));
		
		TimestampFilter<HkDerivativesTR> f4=new TimestampFilter<HkDerivativesTR>(start, end);
		Assert.assertEquals("f4 incorrectly accepted data1", false, f4.accept(data1));
		Assert.assertEquals("f4 incorrectly accepted data2", false, f4.accept(data2));
		
		
		LocalDateTime start2=new LocalDateTime(2012, 10, 9, 8, 30, 0);
		LocalDateTime end2 = new LocalDateTime(2012, 10, 9, 17, 0, 0);		
		TimestampFilter<HkDerivativesTR> f5=new TimestampFilter<HkDerivativesTR>(start2, end2);
		Assert.assertEquals("f5 incorrectly rejected data1", true, f5.accept(data1));
		Assert.assertEquals("f5 incorrectly rejected data2", true, f5.accept(data2));
		
	}
	
}
