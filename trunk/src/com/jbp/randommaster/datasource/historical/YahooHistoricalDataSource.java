package com.jbp.randommaster.datasource.historical;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class YahooHistoricalDataSource implements HistoricalDataSource<YahooHistoricalData> {

	private static final int THREADS_COUNT=10;
	
	private String yqlPattern = "http://query.yahooapis.com/v1/public/yql?q={YQL}&env=http%3A%2F%2Fdatatables.org%2Falltables.env";

	private String yahooSymbol;
	private LocalDateTime startDate, endDate;
	private List<String> yqls;

	public YahooHistoricalDataSource(String yahooSymbol, LocalDateTime startDate,
			LocalDateTime endDate) {

		// throw exception for error case
		if (endDate.compareTo(startDate) < 0)
			throw new YahooHistoricalDataSourceException("endDate " + endDate
					+ " is before the startDate " + startDate + " for symbol "
					+ yahooSymbol);

		this.yahooSymbol = yahooSymbol;
		this.startDate = startDate;
		this.endDate = endDate;
		yqls = new LinkedList<String>();

		String queryPattern = "select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22{SYMBOL}%22%20and%20startDate%20%3D%20%22{STARTDATE}%22%20and%20endDate%20%3D%20%22{ENDDATE}%22";

		LocalDateTime nextStartDate = new LocalDateTime(startDate.getYear(),
				startDate.getMonthOfYear(), startDate.getDayOfMonth(), 0, 0, 0);
		while (nextStartDate.compareTo(endDate) <= 0) {

			// hard coded one year per query.
			LocalDateTime nextEndDate = nextStartDate.plusYears(1);
			if (nextEndDate.compareTo(endDate) > 0)
				nextEndDate = endDate;

			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
			
			String escapedSymbol = yahooSymbol;
			try {
				escapedSymbol=URLEncoder.encode(yahooSymbol, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new YahooHistoricalDataSourceException("Unable to escape input symbol: "+yahooSymbol, e);
			}

			String query = queryPattern.replace("{SYMBOL}", escapedSymbol)
					.replace("{STARTDATE}", nextStartDate.toString(fmt))
					.replace("{ENDDATE}", nextEndDate.toString(fmt));

			yqls.add(yqlPattern.replace("{YQL}", query));

			nextStartDate = nextEndDate.plusDays(1);
		}

	}

	public String getYahooSymbol() {
		return yahooSymbol;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public List<String> getYqls() {
		return yqls;
	}

	public String toString() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return "YahooHistoricalDataSource { " + yahooSymbol + ", "
				+ startDate.toString(fmt) + ", " + endDate.toString(fmt) + " }";
	}
	
	
	public Collection<YahooHistoricalData> getData() {
		TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
		
		
		ExecutorService pool = Executors.newFixedThreadPool(THREADS_COUNT);
		
		
		HashMap<Future<Collection<YahooHistoricalData>>, String> taskToYqlMap
			=new HashMap<Future<Collection<YahooHistoricalData>>, String>();
		
		
		for (String yql: getYqls()) {
			YqlHistoricalDownloadTask t=new YqlHistoricalDownloadTask(yql);
			Future<Collection<YahooHistoricalData>> handle=pool.submit(t);
			taskToYqlMap.put(handle, yql);
		}
		
		try {
			// get all the result
			for (Future<Collection<YahooHistoricalData>> handle : taskToYqlMap.keySet()) {
				try {
					Collection<YahooHistoricalData> batchResult=handle.get();
					result.addAll(batchResult);
				} catch (Exception e1) {
					throw new YahooHistoricalDataSourceException("Unable to download the data from "+taskToYqlMap.get(handle), e1);
				}
			}
		} finally {
		
			// shutdown all the threads in any case
			pool.shutdownNow();
		}
		
		return result;
	}
	

}
