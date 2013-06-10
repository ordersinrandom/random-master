package com.jbp.randommaster.datasource.historical;

public class UnderlyingFilter<T extends DerivativesData> implements HistoricalDataFilter<T> {

	private String underlying;
	
	public UnderlyingFilter(String underlying) {
		this.underlying=underlying;
	}
	
	public String getUnderlying() {
		return underlying;
	}
	
	
	@Override
	public boolean accept(T data) {
		if (data==null)
			throw new IllegalArgumentException("data cannot be null for UnderlyingFilter");
	
		return underlying.equals(data.getUnderlying());
	}

}
