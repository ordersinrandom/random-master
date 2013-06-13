package com.jbp.randommaster.datasource.historical;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.Callable;


import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.joda.time.LocalDateTime;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * 
 * The callable object that implements the asynchronous operation of getData() in <code>YahooHistoricalDataSource</code>.
 *
 */
public class YahooCsvDownloadTask implements Callable<Iterable<YahooHistoricalData>> {

	private String url;
	
	public YahooCsvDownloadTask(String url) {
		this.url=url;
	}

	/**
	 * Invoke the http request of the given yql and parse the response XML.
	 * 
	 * @return A collection of <code>YahooHistoricalData</code>
	 */
	@Override
	public Iterable<YahooHistoricalData> call() throws Exception {
		
		//System.out.println("invoking....");
		Content content=Request.Get(url).execute().returnContent();
		
		String resultCsv=content.asString();
		
		//System.out.println("resultXml=\n"+resultXml);
		
		return parseCsvResponse(resultCsv);
		
	}
	
	
	public static Iterable<YahooHistoricalData> parseCsvResponse(String resultCsv) throws IOException {
		
		TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
		
		CsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new StringReader(resultCsv), CsvPreference.EXCEL_PREFERENCE);
			beanReader.getHeader(true);
			CellProcessor[] processors = new CellProcessor[] { 
					new ParseDate("yyyy-MM-dd"), new ParseDouble(), new ParseDouble(), new ParseDouble(),
					new ParseDouble(), new ParseDouble(), new ParseDouble() };

			String[] propertyNames = new String[] { "Date", "Open", "High", "Low", "Close", "Volume", "AdjClose" };

			YahooCsvRow row = null;
			while ((row = beanReader.read(YahooCsvRow.class, propertyNames, processors)) != null) {

				YahooHistoricalData d=new YahooHistoricalData(
						new LocalDateTime(row.getDate()), 
						row.getOpen(), 
						row.getHigh(), 
						row.getLow(), 
						row.getClose(), 
						row.getVolume(), 
						row.getAdjClose());
				
				result.add(d);
				
				//System.out.println(row);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		
		//System.out.println(resultCsv);
	    
	    return result;
	}
	
	
	public static class YahooCsvRow {

		private Date date;
		private double open, high, low, close, volume, adjClose;

		public YahooCsvRow() {
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public void setOpen(double open) {
			this.open = open;
		}

		public void setHigh(double high) {
			this.high = high;
		}

		public void setLow(double low) {
			this.low = low;
		}

		public void setClose(double close) {
			this.close = close;
		}

		public void setVolume(double volume) {
			this.volume = volume;
		}

		public void setAdjClose(double adjClose) {
			this.adjClose = adjClose;
		}

		public Date getDate() {
			return date;
		}

		public double getOpen() {
			return open;
		}

		public double getHigh() {
			return high;
		}

		public double getLow() {
			return low;
		}

		public double getClose() {
			return close;
		}

		public double getVolume() {
			return volume;
		}

		public double getAdjClose() {
			return adjClose;
		}
	}	
	
}
