package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jbp.randommaster.datasource.historical.consolidation.TimeConsolidatedTradeRecord;

/**
 * 
 * MasterDBConsolidatedTradeRecord is a generic java object that encapsulate the data row loaded from Master Database price tables.
 *
 */
public class MasterDBConsolidatedTradeRecord implements TimeConsolidatedTradeRecord {

	private static final long serialVersionUID = -213905302627600731L;

	private LocalDateTime timestamp;
	private double firstTradedPrice;
	private double lastTradedPrice;
	private double tradedVolume;
	private double maxTradedPrice;
	private double minTradedPrice;
	private double avgPrice;
	
	
	public MasterDBConsolidatedTradeRecord(LocalDateTime t, double first, double last, double volume, double max, double min, double avg) {
		timestamp=t;
		firstTradedPrice = first;
		lastTradedPrice = last;
		tradedVolume = volume;
		maxTradedPrice = max;
		minTradedPrice = min;
		avgPrice = avg;
	}
	
	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
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

	@Override
	public int compareTo(HistoricalData o) {
		return this.timestamp.compareTo(o.getTimestamp());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof MasterDBConsolidatedTradeRecord) {
			MasterDBConsolidatedTradeRecord r = (MasterDBConsolidatedTradeRecord) obj;
			return timestamp.equals(r.timestamp)
					&& firstTradedPrice == r.firstTradedPrice
					&& lastTradedPrice == r.lastTradedPrice
					&& tradedVolume == r.tradedVolume
					&& maxTradedPrice == r.maxTradedPrice
					&& minTradedPrice == r.minTradedPrice
					&& avgPrice == r.avgPrice;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return timestamp.hashCode() 
				^ (new Double(firstTradedPrice)).hashCode()
				^ (new Double(lastTradedPrice)).hashCode()
				^ (new Double(tradedVolume)).hashCode()
				^ (new Double(maxTradedPrice)).hashCode()
				^ (new Double(minTradedPrice)).hashCode()
				^ (new Double(avgPrice)).hashCode();
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e1) {
			return null;
		}
	}	
	
	public String toString() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		StringBuilder buf = new StringBuilder();
		buf.append("MasterDBConsolidatedTradeRecord { timestamp = ");
		buf.append(timestamp.toString(fmt));
		buf.append(", firstTradedPrice = ");
		buf.append(firstTradedPrice);
		buf.append(", lastTradedPrice = ");
		buf.append(lastTradedPrice);
		buf.append(", maxTradedPrice = ");
		buf.append(maxTradedPrice);
		buf.append(", minTradedPrice = ");
		buf.append(minTradedPrice);
		buf.append(", avgPrice = ");
		buf.append(avgPrice);
		buf.append(", tradedVolume = ");
		buf.append(tradedVolume);
		buf.append(" }");
		return buf.toString();
	}	
}
