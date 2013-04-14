package tests.randommaster.datasource.historical;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.janp.randommaster.datasource.historical.HistoricalData;
import com.janp.randommaster.datasource.historical.YahooHistoricalDataSource;
import com.janp.randommaster.datasource.historical.YahooHistoricalDataSourceException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class YahooHistoricalDataSourceTests extends TestCase {

	@Test
	public void testWrongConstructorParam() {
		
		boolean gotException=false;
		try {
			new YahooHistoricalDataSource("0005.HK",
					new LocalDate(2011, 1, 1), new LocalDate(2010, 2, 1));
		} catch (Exception e1) {
			gotException=true;

			Assert.assertEquals("Exception type is not YahooHistoricalDataSourceException", true, e1 instanceof YahooHistoricalDataSourceException);
			
			String expected="endDate 2010-02-01 is before the startDate 2011-01-01 for symbol 0005.HK";
			String msg=e1.getMessage();
			
			Assert.assertEquals("Exception message not matched ("+expected+")", expected, msg);
		}
		
		Assert.assertEquals("No exception thrown for wrong parameter input", true, gotException);
		
		
	}
	
	@Test
	public void testWithinOneYear() {
		YahooHistoricalDataSource t = new YahooHistoricalDataSource("0005.HK",
				new LocalDate(2010, 1, 1), new LocalDate(2010, 2, 1));

		List<String> yqls = t.getYqls();

		Assert.assertNotNull("Result YQLs is null", yqls);
		Assert.assertEquals("Not exactly 1 YQL produced", 1, yqls.size());
		Assert.assertEquals(
				"Result YQL doesn't match",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%220005.HK%22%20and%20startDate%20%3D%20%222010-01-01%22%20and%20endDate%20%3D%20%222010-02-01%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				yqls.get(0));

	}
	
	@Test
	public void testStartEqualsEnd() {
		YahooHistoricalDataSource t = new YahooHistoricalDataSource("0005.HK",
				new LocalDate(2010, 1, 11), new LocalDate(2010, 1, 11));

		List<String> yqls = t.getYqls();

		Assert.assertNotNull("Result YQLs is null", yqls);
		Assert.assertEquals("Not exactly 1 YQL produced", 1, yqls.size());
		Assert.assertEquals(
				"Result YQL doesn't match",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%220005.HK%22%20and%20startDate%20%3D%20%222010-01-11%22%20and%20endDate%20%3D%20%222010-01-11%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				yqls.get(0));
		
	}
	
	@Test 
	public void testDownloadAcrossYears() {
		
		YahooHistoricalDataSource t = new YahooHistoricalDataSource("^HSI",
				new LocalDate(2012, 1, 1), new LocalDate(2013, 3, 5));

		Collection<HistoricalData> data=t.getData();
		
		Assert.assertEquals("Not exactly 296 items downloaded", 296, data.size());
	}
	
	
	@Test
	public void testAcrossYears() {
		YahooHistoricalDataSource t = new YahooHistoricalDataSource("^HSI",
				new LocalDate(2008, 1, 1), new LocalDate(2013, 3, 5));

		List<String> yqls = t.getYqls();
		
		Assert.assertNotNull("Result YQLs is null", yqls);
		Assert.assertEquals("Not exactly 6 YQL produced", 6, yqls.size());
		
		String[] expected = new String[] {
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222008-01-01%22%20and%20endDate%20%3D%20%222009-01-01%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222009-01-02%22%20and%20endDate%20%3D%20%222010-01-02%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222010-01-03%22%20and%20endDate%20%3D%20%222011-01-03%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222011-01-04%22%20and%20endDate%20%3D%20%222012-01-04%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222012-01-05%22%20and%20endDate%20%3D%20%222013-01-05%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env",
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22%5EHSI%22%20and%20startDate%20%3D%20%222013-01-06%22%20and%20endDate%20%3D%20%222013-03-05%22&env=http%3A%2F%2Fdatatables.org%2Falltables.env" };
		
		for (int i=0;i<6;i++) {
			Assert.assertEquals(
					"Result YQL "+i+" doesn't match",
					expected[i],
					yqls.get(i));
		}
	}

}
