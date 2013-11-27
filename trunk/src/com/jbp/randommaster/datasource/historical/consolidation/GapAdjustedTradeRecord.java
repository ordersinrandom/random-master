package com.jbp.randommaster.datasource.historical.consolidation;

import org.joda.time.LocalDateTime;

import com.jbp.randommaster.datasource.historical.HistoricalData;

/**
 * 
 * GapAdjustedTradeRecord is a wrapper class that allows putting an adjustment
 * on the gap open / gap close for analysis purpose.
 * 
 */
public class GapAdjustedTradeRecord<T extends TimeConsolidatedTradeRecord> implements TimeConsolidatedTradeRecord {

	private static final long serialVersionUID = 6248095149428052529L;

	private AccruedGapsTracker tracker;
	
	private T original;
	
	public GapAdjustedTradeRecord(T original, AccruedGapsTracker tracker) {
		this.tracker = tracker;
		this.original=original;
	}
	
	public T getOriginal() {
		return original;
	}
	
	
	@Override
	public LocalDateTime getTimestamp() {
		return original.getTimestamp();
	}

	@Override
	public int compareTo(HistoricalData o) {
		return original.compareTo(o);
	}

	@Override
	public double getFirstTradedPrice() {
		return original.getFirstTradedPrice()+tracker.getAccrued();
	}

	@Override
	public double getLastTradedPrice() {
		return original.getLastTradedPrice()+tracker.getAccrued();
	}

	@Override
	public double getTradedVolume() {
		return original.getTradedVolume();
	}

	@Override
	public double getMaxTradedPrice() {
		return original.getMaxTradedPrice()+tracker.getAccrued();
	}

	@Override
	public double getMinTradedPrice() {
		return original.getMinTradedPrice()+tracker.getAccrued();
	}

	@Override
	public double getAveragedPrice() {
		return original.getAveragedPrice()+tracker.getAccrued();
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(100);
		buf.append("GapAdjustedTradeRecord { gap = ");
		buf.append(tracker.getAccrued());
		buf.append(", original = ");
		buf.append(original);
		buf.append(" }");
		return buf.toString();
	}
	
	
}
