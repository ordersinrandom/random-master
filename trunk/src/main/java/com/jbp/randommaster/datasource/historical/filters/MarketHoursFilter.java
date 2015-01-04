package com.jbp.randommaster.datasource.historical.filters;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.jbp.randommaster.datasource.historical.HistoricalData;
import com.jbp.randommaster.utils.MarketHours;

public class MarketHoursFilter<T extends HistoricalData> implements HistoricalDataFilter<T> {

	private MarketHours marketHours;
	private boolean openExclusive, closeExclusive;
	
	public MarketHoursFilter(MarketHours hours, boolean openExclusive, boolean closeExclusive) {
		this.marketHours = hours;
		this.openExclusive = openExclusive;
		this.closeExclusive = closeExclusive;
	}

	/**
	 * Accept the input historical data if the given time stamp is within the market hours.
	 */
	@Override
	public boolean accept(T data) {
		
		LocalDateTime dt = data.getTimestamp();
		LocalTime t = dt.toLocalTime();
		
		return marketHours.isMarketHour(t, openExclusive, closeExclusive);
		
	}

}
