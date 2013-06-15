package com.jbp.randommaster.datasource.historical;

import org.joda.time.YearMonth;

public interface DerivativesData extends HistoricalData {

	public YearMonth getExpiryMonth();
	
	public String getUnderlying();

	public boolean isFutures();
	
	public boolean isOptions();	
}
