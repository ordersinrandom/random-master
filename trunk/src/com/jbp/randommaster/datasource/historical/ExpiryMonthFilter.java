package com.jbp.randommaster.datasource.historical;

import org.joda.time.YearMonth;

public class ExpiryMonthFilter<T extends HistoricalData<? extends DerivativesDataTuple>> implements HistoricalDataFilter<T> {

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
		
		YearMonth m=data.getData().getExpiryMonth();
		
		return expiryMonth.equals(m);
		
	}
	
}



