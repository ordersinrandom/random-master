package com.jbp.randommaster.datasource.historical;


/**
 * A data filter that would accept the historical data object if the instrument is a futures contract.
 *
 * @param <T> The HistoricalData class that contains a DerivativesData.
 */
public class FuturesFilter<T extends DerivativesData> implements HistoricalDataFilter<T> {

	
	public FuturesFilter() {
	}
	
	@Override
	public boolean accept(T data) {
		if (data==null)
			throw new IllegalArgumentException("data cannot be null for FuturesFilter");
		
		return data.isFutures();
	}
	
}



