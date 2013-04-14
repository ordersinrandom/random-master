package com.janp.randommaster.datasource.historical;

public class YahooHistoricalDataSourceException extends RuntimeException {

	private static final long serialVersionUID = 7809627765177912813L;

	public YahooHistoricalDataSourceException(String msg) {
		super(msg);
	}
	
	public YahooHistoricalDataSourceException(String msg, Exception nested) {
		super(msg, nested);
	}
	
}
