package com.jbp.randommaster.datasource.historical;


/**
 * A data filter that would accept the historical data object if the instrument is an options contract.
 *
 * @param <T> The HistoricalData class that contains a DerivativesData.
 */
public class OptionsFilter<T extends DerivativesData> implements HistoricalDataFilter<T> {

	
	public OptionsFilter() {
	}
	
	@Override
	public boolean accept(T data) {
		if (data==null)
			throw new IllegalArgumentException("data cannot be null for OptionsFilter");
		
		return data.isOptions();
	}
	
}



