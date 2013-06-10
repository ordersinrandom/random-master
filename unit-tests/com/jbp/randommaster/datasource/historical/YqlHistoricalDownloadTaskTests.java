package com.jbp.randommaster.datasource.historical;


import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.YahooHistoricalData;
import com.jbp.randommaster.datasource.historical.YahooHistoricalDataSourceException;
import com.jbp.randommaster.datasource.historical.YqlHistoricalDownloadTask;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YqlHistoricalDownloadTaskTests extends TestCase {

	@Test
	public void testDownload() {
		
		String yql="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%220005.HK%22%20and%20startDate%20%3D%20%222010-01-01%22%20and%20endDate%20%3D%20%222010-01-05%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
		
		YqlHistoricalDownloadTask t=new YqlHistoricalDownloadTask(yql);
		
		try {
			Iterable<YahooHistoricalData> result=t.call();
			Assert.assertNotNull("Result Collection is null",result);
			
		} catch (Exception e1) {
			Assert.assertTrue("Exception type does not match", e1 instanceof YahooHistoricalDataSourceException);
		}
		
	}
	
	@Test
	public void testInterpretXml() {
		
		StringBuilder inputXml=new StringBuilder();
		inputXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		inputXml.append("<query xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\" yahoo:count=\"3\" yahoo:created=\"2013-04-14T15:15:15Z\" yahoo:lang=\"en-US\"><results><quote date=\"2010-01-05\"><Date>2010-01-05</Date><Open>90.20</Open><High>90.65</High><Low>90.10</Low><Close>90.45</Close><Volume>16914100</Volume><Adj_Close>88.76</Adj_Close></quote><quote date=\"2010-01-04\"><Date>2010-01-04</Date><Open>89.40</Open><High>89.90</High><Low>88.80</Low><Close>89.25</Close><Volume>10381700</Volume><Adj_Close>87.58</Adj_Close></quote><quote date=\"2010-01-01\"><Date>2010-01-01</Date><Open>89.40</Open><High>89.40</High><Low>89.40</Low><Close>89.40</Close><Volume>000</Volume><Adj_Close>87.73</Adj_Close></quote></results></query><!-- total: 48 -->\n");
		inputXml.append("<!-- engine5.yql.sg3.yahoo.com -->\n");
		
		try {
			Iterable<YahooHistoricalData> resultIt=YqlHistoricalDownloadTask.parseYqlResponse(inputXml.toString());
			
			List<YahooHistoricalData> result=new LinkedList<YahooHistoricalData>();
			CollectionUtils.addAll(result, resultIt.iterator());
			
			Assert.assertEquals("Not getting exactly 3 items", 3, result.size());
			
			
			YahooHistoricalData[] expected = new YahooHistoricalData[] {
				new YahooHistoricalData(new LocalDate(2010,1,1).toLocalDateTime(LocalTime.MIDNIGHT), 89.4, 89.4, 89.4, 89.4, 0.0, 87.73),
				new YahooHistoricalData(new LocalDate(2010,1,4).toLocalDateTime(LocalTime.MIDNIGHT), 89.4, 89.9, 88.8, 89.25, 1.03817E7, 87.58),
				new YahooHistoricalData(new LocalDate(2010,1,5).toLocalDateTime(LocalTime.MIDNIGHT), 90.2, 90.65, 90.1, 90.45, 1.69141E7, 88.76)
			};
			
			int i=0;
			for (YahooHistoricalData d : result) {
				
				YahooHistoricalData dtu = d;
				YahooHistoricalData tu = expected[i];
				
				Assert.assertEquals("Date not matched: "+expected[i].getTimestamp(), expected[i].getTimestamp(), d.getTimestamp());
				Assert.assertEquals("Open not matched: "+tu.getOpen(), tu.getOpen(), dtu.getOpen(), 0.00001);
				Assert.assertEquals("High not matched: "+tu.getHigh(), tu.getHigh(), dtu.getHigh(), 0.00001);
				Assert.assertEquals("Low not matched: "+tu.getLow(), tu.getLow(), dtu.getLow(), 0.00001);
				Assert.assertEquals("Close not matched: "+tu.getClose(), tu.getClose(), dtu.getClose(), 0.00001);
				Assert.assertEquals("Volume not matched: "+tu.getVolume(), tu.getVolume(), dtu.getVolume(), 0.00001);
				Assert.assertEquals("AdjClose not matched: "+tu.getAdjustedClose(),tu.getAdjustedClose(), dtu.getAdjustedClose(), 0.00001);
				
				i++;
			}
			
		} catch (Exception e1) {
			Assert.fail("Caught unexpected exception "+e1.getMessage());
		}
	}
	
}
