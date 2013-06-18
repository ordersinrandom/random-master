package com.jbp.randommaster.datasource.historical;

import org.joda.time.YearMonth;

/**
 * A data filter that would accept the historical data object depending on its expiry month.
 *
 * @param <T> The HistoricalData class that contains a DerivativesDataTuple.
 */
public class ExpiryMonthFilter<T extends DerivativesData> implements HistoricalDataFilter<T> {

	private YearMonth expiryMonth;
	
	public ExpiryMonthFilter(YearMonth expiryMonth) {
		this.expiryMonth=expiryMonth;
	}
	
	public YearMonth getExpiryMonth() {
		return expiryMonth;
	}
	
	@Override
	public boolean accept(T data) {
		if (data==null)
			throw new IllegalArgumentException("data cannot be null for ExpiryMonthFilter");
		
		YearMonth m=data.getExpiryMonth();
		
		return expiryMonth.equals(m);
		
	}
	
}


