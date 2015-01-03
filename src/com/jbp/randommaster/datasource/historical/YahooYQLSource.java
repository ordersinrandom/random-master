package com.jbp.randommaster.datasource.historical;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * Implementation of HistoricalDataSource that downloads daily data of any instruments
 * from yahoo using YQL.
 *
 */
public class YahooYQLSource implements HistoricalDataSource<YahooHistoricalData> {

	private static final int THREADS_COUNT=10;
	
	private String yqlPattern = "http://query.yahooapis.com/v1/public/yql?q={YQL}&env=http%3A%2F%2Fdatatables.org%2Falltables.env";

	private String yahooSymbol;
	private LocalDate startDate, endDate;
	private List<String> yqls;

	/**
	 * Create a new YahooHistoricalDataSource instance.
	 * 
	 * @param yahooSymbol The symbol to be loaded
	 * @param startDate The start date range inclusive.
	 * @param endDate The end date range inclusive.
	 */
	public YahooYQLSource(String yahooSymbol, LocalDate startDate, LocalDate endDate) {

		// throw exception for error case
		if (endDate.compareTo(startDate) < 0)
			throw new IllegalArgumentException("endDate " + endDate
					+ " is before the startDate " + startDate + " for symbol "
					+ yahooSymbol);

		this.yahooSymbol = yahooSymbol;
		this.startDate = startDate;
		this.endDate = endDate;
		yqls = new LinkedList<String>();

		String queryPattern = "select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22{SYMBOL}%22%20and%20startDate%20%3D%20%22{STARTDATE}%22%20and%20endDate%20%3D%20%22{ENDDATE}%22";

		LocalDateTime nextStartDate = startDate.atTime(LocalTime.MIDNIGHT);
		while (nextStartDate.compareTo(endDate.atTime(LocalTime.MIDNIGHT)) <= 0) {

			// hard coded one year per query.
			LocalDateTime nextEndDate = nextStartDate.plusYears(1);
			if (nextEndDate.compareTo(endDate.atTime(LocalTime.MIDNIGHT)) > 0)
				nextEndDate = endDate.atTime(LocalTime.MIDNIGHT);

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			String escapedSymbol = yahooSymbol;
			try {
				escapedSymbol=URLEncoder.encode(yahooSymbol, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Unable to escape input symbol: "+yahooSymbol, e);
			}

			String query = queryPattern.replace("{SYMBOL}", escapedSymbol)
					.replace("{STARTDATE}", nextStartDate.format(fmt))
					.replace("{ENDDATE}", nextEndDate.format(fmt));

			yqls.add(yqlPattern.replace("{YQL}", query));

			nextStartDate = nextEndDate.plusDays(1);
		}

	}

	public String getYahooSymbol() {
		return yahooSymbol;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public List<String> getYqls() {
		return yqls;
	}

	public String toString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return "YahooHistoricalDataSource { " + yahooSymbol + ", "
				+ startDate.format(fmt) + ", " + endDate.format(fmt) + " }";
	}
	
	/**
	 * Get the data downloaded from yahoo.
	 * 
	 * Note that it spawns threads to load the data and wait until all threads finished loading.
	 * 
	 * @throws YahooHistoricalDataSourceException if download error.
	 * 
	 */
	public Iterable<YahooHistoricalData> getData() {
		
		TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
		
		ExecutorService pool = Executors.newFixedThreadPool(THREADS_COUNT);
		
		
		HashMap<Future<Iterable<YahooHistoricalData>>, String> taskToYqlMap
			=new HashMap<Future<Iterable<YahooHistoricalData>>, String>();
		
		
		for (String yql: getYqls()) {
			YqlHistoricalDownloadTask t=new YqlHistoricalDownloadTask(yql);
			Future<Iterable<YahooHistoricalData>> handle=pool.submit(t);
			taskToYqlMap.put(handle, yql);
		}
		
		try {
			// get all the result
			for (Future<Iterable<YahooHistoricalData>> handle : taskToYqlMap.keySet()) {
				try {
					Iterable<YahooHistoricalData> batchResult=handle.get();
					
					for (YahooHistoricalData d : batchResult) {
						result.add(d);
					}
					
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
