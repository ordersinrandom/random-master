package com.jbp.randommaster.datasource.historical;


import org.joda.time.LocalDate;

public interface HistoricalDailyData extends HistoricalData, Comparable<HistoricalDailyData> {

	public LocalDate getDate();
	public double getOpen();
	public double getHigh();
	public double getLow();
	public double getClose();
	public double getVolume();
	public double getAdjustedClose();	
	
}
