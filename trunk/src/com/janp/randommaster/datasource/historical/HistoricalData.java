package com.janp.randommaster.datasource.historical;

import java.io.Serializable;

import org.joda.time.LocalDate;

public interface HistoricalData extends Serializable {

	public LocalDate getDate();
	public double getOpen();
	public double getHigh();
	public double getLow();
	public double getClose();
	public double getVolume();
	public double getAdjustedClose();	
	
}
