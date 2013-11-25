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
	private LocalDateTime timestamp;
	private double firstTradedPrice;
	private double lastTradedPrice;
	private double maxTradedPrice;
	private double minTradedPrice;
	private double avgPrice;
	private double tradedVolume;

	public GapAdjustedTradeRecord(double gapAdjustment, TimeConsolidatedTradeRecord original) {
		if (original == null)
			throw new IllegalArgumentException("Original input trade record cannot be null");

		this.gapAdjustment = gapAdjustment;
		this.timestamp = original.getTimestamp();
		this.firstTradedPrice = original.getFirstTradedPrice() + gapAdjustment;
		this.lastTradedPrice = original.getLastTradedPrice() + gapAdjustment;
		this.maxTradedPrice = original.getMaxTradedPrice() + gapAdjustment;
		this.minTradedPrice = original.getMinTradedPrice() + gapAdjustment;
		this.avgPrice = original.getAveragedPrice() + gapAdjustment;
		this.tradedVolume = original.getTradedVolume();
	}

	public double getGapAdjustment() {
		return gapAdjustment;
	}

	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public int compareTo(HistoricalData o) {
		return this.getTimestamp().compareTo(o.getTimestamp());
	}

	@Override
	public double getFirstTradedPrice() {
		return firstTradedPrice;
	}

	@Override
	public double getLastTradedPrice() {
		return lastTradedPrice;
	}

	@Override
	public double getTradedVolume() {
		return tradedVolume;
	}

	@Override
	public double getMaxTradedPrice() {
		return maxTradedPrice;
	}

	@Override
	public double getMinTradedPrice() {
		return minTradedPrice;
	}

	@Override
	public double getAveragedPrice() {
		return avgPrice;
	}

}
