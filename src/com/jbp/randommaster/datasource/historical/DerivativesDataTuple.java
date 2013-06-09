package com.jbp.randommaster.datasource.historical;

import org.joda.time.YearMonth;

public interface DerivativesDataTuple extends HistoricalDataTuple {

	public YearMonth getExpiryMonth();
	
	public String getUnderlying();
	
}
