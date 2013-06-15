package com.jbp.randommaster.datasource.historical;

public class TradeRecordsConsolidatorException extends RuntimeException {

	private static final long serialVersionUID = 263015334340690600L;
	
	public TradeRecordsConsolidatorException(String msg) {
		super(msg);
	}

	public TradeRecordsConsolidatorException(String msg, Throwable nested) {
		super(msg, nested);
	}
	
}
