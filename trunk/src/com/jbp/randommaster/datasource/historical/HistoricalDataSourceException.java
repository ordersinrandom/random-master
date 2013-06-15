package com.jbp.randommaster.datasource.historical;

public class HistoricalDataSourceException extends RuntimeException {

	private static final long serialVersionUID = -5628359859119514864L;
	
	
	public HistoricalDataSourceException(String msg) {
		super(msg);
	}
	
	public HistoricalDataSourceException(String msg, Throwable nested) {
		super(msg, nested);
	}	

}
