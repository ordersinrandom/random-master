package com.jbp.randommaster.datasource.historical.filters;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.jbp.randommaster.datasource.historical.DerivativesData;
import com.jbp.randommaster.utils.HolidaysList;

/**
 * 
 * HolidaysFilter filters given historical data record by given timestamp. If the data is not on a business day, or it is not within market open/close, it will be filtered.
 *
 * @param <T>
 */
public class HolidaysFilter<T extends DerivativesData> implements HistoricalDataFilter<T> {

	
	private HolidaysList holidaysList;
	private List<Pair<LocalTime, LocalTime>> openCloseList;
	private boolean closeExclusive;
	
	/**
	 * Create a new instance of HolidaysFilter.
	 * 
	 * @param list The list of holidays
	 * @param open Open time of each trading day
	 * @param close Close time of each trading day
	 */
	public HolidaysFilter(HolidaysList list, boolean closeExclusive, LocalTime open, LocalTime close) {
		this(list, closeExclusive);
		addOpenCloseInterval(open, close);
	}
	
	public HolidaysFilter(HolidaysList list, boolean closeExclusive) {
		this.holidaysList = list;
		this.closeExclusive = closeExclusive;
		this.openCloseList = new ArrayList<>();
	}
	
	public HolidaysFilter(HolidaysList list) {
		this(list, true);
	}	

	/**
	 * Add open/close time pair
	 * 
	 * @param open Open time of each trading day
	 * @param close Close time of each trading day
	 */
	public void addOpenCloseInterval(LocalTime open, LocalTime close) {
		openCloseList.add(new Pair<>(open, close));
	}
	
	
	/**
	 * Accept the data if its timestamp is not on a holiday, and not outside trading hours.
	 */
	@Override
	public boolean accept(T data) {

		LocalDateTime dt = data.getTimestamp();
		// business days checking
		if (holidaysList != null && !holidaysList.isBusinessDay(dt.toLocalDate())) {
			return false;
		}
		
		// open close checking
		LocalTime t = dt.toLocalTime();
		boolean passedTimeCheck = false;
		for (Pair<LocalTime,LocalTime> p : openCloseList) {
			LocalTime open = p.getValue0();
			LocalTime close = p.getValue1();

			if ((open.isEqual(t) || open.isBefore(t)) && ((!closeExclusive && close.isEqual(t)) || close.isAfter(t))) {
				passedTimeCheck = true;
				break;
			}
		}
		
		return passedTimeCheck;

	}
	
}
