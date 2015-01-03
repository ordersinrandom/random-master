package com.jbp.randommaster.datasource.historical.filters;


import java.time.LocalDateTime;

import com.jbp.randommaster.datasource.historical.HistoricalData;

/**
 * 
 * Filtering on HistoricalData timestamp.
 *
 * @param <T> A HistoricalData class that has a timestamp.
 */
public class TimestampFilter<T extends HistoricalData> implements HistoricalDataFilter<T> {

	private LocalDateTime start, end;
	
	/**
	 * Create a timestamp filter.
	 * 
	 * Only data that's on or after start, and on or before end will be accepted.
	 * 
	 * @param start Start datetime range. null means no start limit.
	 * @param end End datetime range. null means no end limit.
	 */
	public TimestampFilter(LocalDateTime start, LocalDateTime end) {
		this.start=start;
		this.end=end;
	}
	
	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	@Override
	public boolean accept(T data) {
		if (data==null)
			throw new IllegalArgumentException("data cannot be null for TimestampFilter");
		
		LocalDateTime t = data.getTimestamp();
		boolean startOk = start==null || t.isEqual(start) || t.isAfter(start);
		boolean endOk = end==null || t.isEqual(end) || t.isBefore(end);
		
		return startOk && endOk;
	}
}
