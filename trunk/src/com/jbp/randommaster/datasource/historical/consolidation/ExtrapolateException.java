package com.jbp.randommaster.datasource.historical.consolidation;

/**
 * 
 * ExtrapolateException indicates an error when there is no data to extrapolate (either backward or forward).
 *
 */
public class ExtrapolateException extends TradeRecordsConsolidatorException {
	
	private static final long serialVersionUID = -391164996445926687L;

	public ExtrapolateException(String msg) {
		super(msg);
	}

	public ExtrapolateException(String msg, Throwable nested) {
		super(msg, nested);
	}	
	
}
