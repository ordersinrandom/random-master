package com.jbp.randommaster.datasource.historical;

public class HistoricalDataSourceException extends Exception {

	private static final long serialVersionUID = -5628359859119514864L;
	
	
	public HistoricalDataSourceException(String msg) {
		super(msg);
	}
	
	public HistoricalDataSourceException(String msg, Exception nested) {
		super(msg, nested);
	}	

}
