package com.jbp.randommaster.datasource.historical.consolidation;

import org.joda.time.LocalDateTime;

import com.jbp.randommaster.datasource.historical.HistoricalData;

/**
 * 
 * GapAdjustedTradeRecord is a wrapper class that allows putting an adjustment
 * on the gap open / gap close for analysis purpose.
 * 
 */
public class GapAdjustedTradeRecord implements TimeConsolidatedTradeRecord {

	private static final long serialVersionUID = 6248095149428052529L;

	private double gapAdjustment;
	private TimeConsolidatedTradeRecord originalRecord;

	public GapAdjustedTradeRecord(double gapAdjustment, TimeConsolidatedTradeRecord original) {
		if (original == null)
			throw new IllegalArgumentException("Original input trade record cannot be null");

		this.gapAdjustment = gapAdjustment;
		this.originalRecord = original;
	}

	public double getGapAdjustment() {
		return gapAdjustment;
	}

	public TimeConsolidatedTradeRecord getOriginalRecord() {
		return originalRecord;
	}

	@Override
	public LocalDateTime getTimestamp() {
		return originalRecord.getTimestamp();
	}

	@Override
	public int compareTo(HistoricalData o) {
		return this.getTimestamp().compareTo(o.getTimestamp());
	}

	@Override
	public double getFirstTradedPrice() {
		return originalRecord.getFirstTradedPrice() + gapAdjustment;
	}

	@Override
	public double getLastTradedPrice() {
		return originalRecord.getLastTradedPrice() + gapAdjustment;
	}

	@Override
	public double getTradedVolume() {
		return originalRecord.getTradedVolume();
	}

	@Override
	public double getMaxTradedPrice() {
		return originalRecord.getMaxTradedPrice() + gapAdjustment;
	}

	@Override
	public double getMinTradedPrice() {
		return originalRecord.getMinTradedPrice() + gapAdjustment;
	}

	@Override
	public double getAveragedPrice() {
		return originalRecord.getAveragedPrice() + gapAdjustment;
	}

}
