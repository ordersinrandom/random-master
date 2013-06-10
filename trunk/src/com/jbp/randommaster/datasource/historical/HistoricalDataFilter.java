package com.jbp.randommaster.datasource.historical;

public interface HistoricalDataFilter<T extends HistoricalData> {

	/**
	 * Accept this Historical Data.
	 * @param data The historical data to be checked.
	 * @return true if accept
	 */
	public boolean accept(T data);	
}
