package com.jbp.randommaster.datasource.historical;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YahooCsvSourceTests extends TestCase {

	@Test
	public void testWrongConstructorParam() {
		
		boolean gotException=false;
		try {
			new YahooCsvSource("0005.HK",
					new LocalDate(2011, 1, 1), new LocalDate(2010, 2, 1));
		} catch (Exception e1) {
			gotException=true;

			Assert.assertEquals("Exception type is not IllegalArgumentException", true, e1 instanceof IllegalArgumentException);
			
			String expected="endDate 2010-02-01 is before the startDate 2011-01-01 for symbol 0005.HK";
			String msg=e1.getMessage();
			
			Assert.assertEquals("Exception message not matched ("+expected+")", expected, msg);
		}
		
		Assert.assertEquals("No exception thrown for wrong parameter input", true, gotException);
	}
	
	@Test
	public void testWithinOneYear() {
		YahooCsvSource t = new YahooCsvSource("0005.HK",
				new LocalDate(2010, 1, 1), new LocalDate(2010, 2, 1));

		List<String> urls = t.getUrls();

		Assert.assertNotNull("Result URLs is null", urls);
		Assert.assertEquals("Not exactly 1 URL produced", 1, urls.size());
		
		Assert.assertEquals(
				"Result URL doesn't match",
				"http://ichart.finance.yahoo.com/table.csv?s=0005.HK&d=1&e=1&f=2010&g=d&a=0&b=1&c=2010&ignore=.csv",
				urls.get(0));


	}
	
	@Test
	public void testStartEqualsEnd() {
		
		YahooCsvSource t = new YahooCsvSource("0005.HK",
				new LocalDate(2010, 1, 11), new LocalDate(2010, 1, 11));

		List<String> urls = t.getUrls();

		Assert.assertNotNull("Result URLs is null", urls);
		Assert.assertEquals("Not exactly 1 URL produced", 1, urls.size());
		
		Assert.assertEquals(
				"Result URL doesn't match",
				"http://ichart.finance.yahoo.com/table.csv?s=0005.HK&d=0&e=11&f=2010&g=d&a=0&b=11&c=2010&ignore=.csv",
				urls.get(0));
		
	}
	
	
	@Test 
	public void testDownloadAcrossYears() {
		
		YahooCsvSource t = new YahooCsvSource("^HSI",
				new LocalDate(2012, 1, 1), new LocalDate(2013, 3, 5));

		Collection<YahooHistoricalData> data=new LinkedList<YahooHistoricalData>();
		for (YahooHistoricalData d : t.getData()) 
			data.add(d);
		
		Assert.assertEquals("Not exactly 296 items downloaded", 296, data.size());
	}
	
	
	@Test
	public void testAcrossYears() {
		YahooCsvSource t = new YahooCsvSource("^HSI",
				new LocalDate(2008, 1, 1), 
				new LocalDate(2013, 3, 5));

		List<String> urls = t.getUrls();
		
		Assert.assertNotNull("Result URLs is null", urls);
		Assert.assertEquals("Not exactly 6 URL produced", 6, urls.size());
		
		String[] expected = new String[] {
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=0&e=1&f=2009&g=d&a=0&b=1&c=2008&ignore=.csv",
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=0&e=2&f=2010&g=d&a=0&b=2&c=2009&ignore=.csv",
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=0&e=3&f=2011&g=d&a=0&b=3&c=2010&ignore=.csv",
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=0&e=4&f=2012&g=d&a=0&b=4&c=2011&ignore=.csv",
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=0&e=5&f=2013&g=d&a=0&b=5&c=2012&ignore=.csv",
				"http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=2&e=5&f=2013&g=d&a=0&b=6&c=2013&ignore=.csv" };
		

		for (int i=0;i<6;i++) {
			Assert.assertEquals(
					"Result URL "+i+" doesn't match",
					expected[i],
					urls.get(i));
		}
		
		Collection<YahooHistoricalData> data=new LinkedList<YahooHistoricalData>();
		for (YahooHistoricalData d : t.getData()) 
			data.add(d);		
		
		int count=0;
		YahooHistoricalData first=null;
		YahooHistoricalData last=null;
		for (YahooHistoricalData d : data) {
			if (count==0)
				first=d;
			last=d;
			count++;
		}
		
		Assert.assertEquals("Not exactly 1304 items", 1304, count);
		Assert.assertEquals("First item date mismatched", new LocalDateTime(2008,1,2,0,0,0), first.getTimestamp());
		Assert.assertEquals("First item adj Close mismatched", 27560.52, first.getAdjustedClose(), 0.0000001);
		Assert.assertEquals("Last item date mismatched", new LocalDateTime(2013,3,5,0,0,0), last.getTimestamp());
		Assert.assertEquals("Last item adj Close mismatched", 22560.5, last.getAdjustedClose(), 0.00000001);
		

	}

}
