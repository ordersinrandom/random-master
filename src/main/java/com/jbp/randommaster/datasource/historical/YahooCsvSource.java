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

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * Implementation of historical data source that features Yahoo finance webpage CSV download.
 * Maximum it spawns 10 threads to download data in parallel.
 *
 */
public class YahooCsvSource implements HistoricalDataSource<YahooHistoricalData> {

	private static final int THREADS_COUNT=10;
	
	private static String csvPattern = "http://ichart.finance.yahoo.com/table.csv?s={SYMBOL}&d={ENDMONTH}&e={ENDDAY}&f={ENDYEAR}&g=d&a={STARTMONTH}&b={STARTDAY}&c={STARTYEAR}&ignore=.csv";
	
	private String yahooSymbol;
	private LocalDate startDate, endDate;
	private List<String> allUrls;

	/**
	 * Create a new instance of YahooCsvSource.
	 * 
	 * @param yahooSymbol The target symbol to be loaded
	 * @param startDate The start date of the loading task.
	 * @param endDate The end date of the loading task.
	 */
	public YahooCsvSource(String yahooSymbol, LocalDate startDate, LocalDate endDate) {
		
		if (endDate.compareTo(startDate) < 0)
			throw new IllegalArgumentException("endDate " + endDate
					+ " is before the startDate " + startDate + " for symbol "
					+ yahooSymbol);

		this.yahooSymbol = yahooSymbol;
		this.startDate = startDate;
		this.endDate = endDate;
		this.allUrls=new LinkedList<>();
		
		
		LocalDateTime nextStartDate = startDate.atTime(LocalTime.MIDNIGHT);
		
		while (nextStartDate.compareTo(endDate.atTime(LocalTime.MIDNIGHT)) <= 0) {

			// hard coded one year per query.
			LocalDateTime nextEndDate = nextStartDate.plusYears(1);
			if (nextEndDate.compareTo(endDate.atTime(LocalTime.MIDNIGHT)) > 0)
				nextEndDate = endDate.atTime(LocalTime.MIDNIGHT);

			String escapedSymbol = yahooSymbol;
			try {
				escapedSymbol=URLEncoder.encode(yahooSymbol, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Unable to escape input symbol: "+yahooSymbol, e);
			}
			
			String url = csvPattern.replace("{SYMBOL}", escapedSymbol)
				.replace("{ENDYEAR}", Integer.valueOf(nextEndDate.getYear()).toString())
				.replace("{ENDMONTH}", Integer.valueOf(nextEndDate.getMonthValue()-1).toString())
				.replace("{ENDDAY}", Integer.valueOf(nextEndDate.getDayOfMonth()).toString())
				.replace("{STARTYEAR}", Integer.valueOf(nextStartDate.getYear()).toString())
				.replace("{STARTMONTH}", Integer.valueOf(nextStartDate.getMonthValue()-1).toString())
				.replace("{STARTDAY}", Integer.valueOf(nextStartDate.getDayOfMonth()).toString());
			
			allUrls.add(url);

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

	public List<String> getUrls() {
		return allUrls;
	}	
	
	public String toString() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return "YahooCsvSource { " + yahooSymbol + ", "
				+ startDate.format(fmt) + ", " + endDate.format(fmt) + " }";
	}
		

	@Override
	public Iterable<YahooHistoricalData> getData() {
		
		TreeSet<YahooHistoricalData> result=new TreeSet<YahooHistoricalData>();
		
		ExecutorService pool = Executors.newFixedThreadPool(THREADS_COUNT);
		
		
		HashMap<Future<Iterable<YahooHistoricalData>>, String> taskToUrlMap
			=new HashMap<Future<Iterable<YahooHistoricalData>>, String>();
		
		
		for (String url: getUrls()) {
			YahooCsvDownloadTask t=new YahooCsvDownloadTask(url);
			Future<Iterable<YahooHistoricalData>> handle=pool.submit(t);
			taskToUrlMap.put(handle, url);
		}
		
		try {
			// get all the result
			for (Future<Iterable<YahooHistoricalData>> handle : taskToUrlMap.keySet()) {
				try {
					Iterable<YahooHistoricalData> batchResult=handle.get();
					CollectionUtils.addAll(result, batchResult.iterator());
					
				} catch (Exception e1) {
					throw new YahooHistoricalDataSourceException("Unable to download the data from "+taskToUrlMap.get(handle), e1);
				}
			}
		} finally {
		
			// shutdown all the threads in any case
			pool.shutdownNow();
		}
		
		return result;		
		
	}

	
	
}
