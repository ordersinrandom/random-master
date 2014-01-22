package com.jbp.randommaster.datasource.historical.filters;

import org.joda.time.LocalDateTime;

import com.jbp.randommaster.datasource.historical.HistoricalData;
import com.jbp.randommaster.utils.HolidaysList;

/**
 * 
 * HolidaysFilter filters given historical data record by given timestamp. If the data is not on a business day it will be filtered.
 *
 * @param <T> Any HistoricalData subclasses.
 */
public class HolidaysFilter<T extends HistoricalData> implements HistoricalDataFilter<T> {

	
	private HolidaysList holidaysList;
	
	/**
	 * Create a new instance of HolidaysFilter.
	 * 
	 * @param list The list of holidays
	 */
	public HolidaysFilter(HolidaysList list) {
		this.holidaysList = list;
	}
	
	/**
	 * Accept the data if its timestamp is not on a holiday / weekend
	 */
	@Override
	public boolean accept(T data) {

		LocalDateTime dt = data.getTimestamp();
		// business days checking
		if (holidaysList != null && !holidaysList.isBusinessDay(dt.toLocalDate())) {
			return false;
		}
		else return true;

	}
	
}
