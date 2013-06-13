package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YahooCsvDownloadTaskTests extends TestCase {

	@Test
	public void testDownload() {
		
		String url="http://ichart.finance.yahoo.com/table.csv?s=%5EHSI&d=5&e=7&f=2013&g=d&a=11&b=31&c=2012&ignore=.csv";
		YahooCsvDownloadTask t=new YahooCsvDownloadTask(url);
		
		try {
			Iterable<YahooHistoricalData> result=t.call();
			Assert.assertNotNull("Result Iterable is null",result);
			
			int count=0;
			YahooHistoricalData first=null;
			YahooHistoricalData last=null;
			for (YahooHistoricalData d : result) {
				if (count==0)
					first=d;
				last=d;
				count++;
			}
			

			Assert.assertEquals("Not exactly 113 items", 113, count);
			Assert.assertEquals("First item date mismatched", new LocalDateTime(2012,12,31,0,0,0), first.getTimestamp());
			Assert.assertEquals("First item adjClose mismatched", 22656.92, first.getAdjustedClose(), 0.0000001);
			Assert.assertEquals("Last item date mismatched", new LocalDateTime(2013,6,7,0,0,0), last.getTimestamp());
			Assert.assertEquals("Last item adjClose  mismatched", 21575.26, last.getAdjustedClose(), 0.0000001);
			
			
		} catch (Exception e1) {
			
			Assert.fail("caught exception when invoking YahooCsvDownloadTask: "+e1.getMessage());
			
		}
		
		
	}
	
}
