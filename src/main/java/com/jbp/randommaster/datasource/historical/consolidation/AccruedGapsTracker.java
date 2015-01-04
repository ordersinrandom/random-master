package com.jbp.randommaster.datasource.historical.consolidation;

/**
 * 
 * AccruedGapsTracker keeps track of the changes of gaps. A collection of GapAdjustedTradeRecord will be tied with a AccruedGapsTracker object
 * so that we don't need to iterate through all records to update their gaps one by one.
 *
 */
public class AccruedGapsTracker {

	private double accrued;
	
	public AccruedGapsTracker() {
		accrued=0;
	}
	
	public double getAccrued() {
		return accrued;
	}
	
	public void adjust(double changes) {
		accrued+=changes;
	}
	
}
