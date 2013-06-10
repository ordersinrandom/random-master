package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.YahooHistoricalData;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YahooHistoricalDataTests extends TestCase {

	@Test
	public void testParameters() {
		
		LocalDateTime inputDateTime=new LocalDate(2011,1,1).toLocalDateTime(LocalTime.MIDNIGHT);
		double open=20000.0;
		double high=20100.0;
		double low=19900.0;
		double close=20050.0;
		double vol = 12345678.12;
		double adjustedClose=20050.02;
		YahooHistoricalData d=new YahooHistoricalData(inputDateTime, 
				open, high, low, close, vol, adjustedClose);
		
		Assert.assertEquals("Date mismatched input = "+inputDateTime+", result = "+d.getTimestamp(), inputDateTime, d.getTimestamp());
		Assert.assertEquals("Open mismatched", open, d.getOpen(), 0.000000001);
		Assert.assertEquals("High mismatched", high, d.getHigh(), 0.000000001);
		Assert.assertEquals("Low mismatched", low, d.getLow(), 0.000000001);
		Assert.assertEquals("Close mismatched", close, d.getClose(), 0.000000001);
		Assert.assertEquals("Volume mismatched", vol, d.getVolume(), 0.000000001);
		Assert.assertEquals("Adjusted Close mismatched", adjustedClose, d.getAdjustedClose(), 0.000000001);
		
	}
	
	@Test
	public void testToString() {

		LocalDate inputDate=new LocalDate(2011,1,1);
		double open=20000.0;
		double high=20100.0;
		double low=19900.0;
		double close=20050.0;
		double vol = 12345678.12;
		double adjustedClose=20050.02;
		YahooHistoricalData d=new YahooHistoricalData(inputDate.toLocalDateTime(LocalTime.MIDNIGHT), 
				open, high, low, close, vol, adjustedClose);
		
		Assert.assertEquals("toString() output mismatched", 
				"YahooHistoricalData { date=2011-01-0120000.0, high=20100.0, low=19900.0, close=20050.0, volume=1.234567812E7, adjustedClose=20050.02 }",
				d.toString());
		
	}
	
}
