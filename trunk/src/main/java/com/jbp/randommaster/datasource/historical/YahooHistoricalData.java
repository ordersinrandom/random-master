package com.jbp.randommaster.datasource.historical;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jbp.randommaster.datasource.historical.consolidation.TimeConsolidatedTradeRecord;

/**
 * Encapsulate the Yahoo historical data (open, high, low, close, volume, adjusted close).
 *
 */
public class YahooHistoricalData implements TimeConsolidatedTradeRecord {

	private static final long serialVersionUID = 1643322330608553665L;
	
	private LocalDateTime date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	private double adjustedClose;	
	
	
	public YahooHistoricalData(LocalDateTime date, double open, double high, double low, double close, double volume, double adjustedClose) {
		this.date=date;
		this.open=open;
		this.high=high;
		this.low=low;
		this.close=close;
		this.volume=volume;
		this.adjustedClose=adjustedClose;
		
	}

	
	public int hashCode() {
		return getTimestamp().hashCode()^Double.valueOf(open).hashCode()
				^Double.valueOf(high).hashCode()
				^Double.valueOf(low).hashCode()
				^Double.valueOf(close).hashCode()
				^Double.valueOf(volume).hashCode()
				^Double.valueOf(adjustedClose).hashCode();
	}	
	
	
	
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof YahooHistoricalData) {
			YahooHistoricalData d=(YahooHistoricalData) obj;
			return d.getTimestamp().equals(this.getTimestamp())
					&& d.open==this.open
					&& d.high==this.high
					&& d.low==this.low
					&& d.close==this.close
					&& d.volume==this.volume
					&& d.adjustedClose==this.adjustedClose;
		}
		else return false;
	}
	
	
	@Override
	public int compareTo(HistoricalData data) {
		int c=getTimestamp().compareTo(data.getTimestamp());
		if (c!=0)
			return c;
		else {
			
			YahooHistoricalData d=(YahooHistoricalData) data;
			
			// if the volume is 0, we put it at the back, those are corp action items.
			if (getVolume()>d.getVolume())
				return -1;
			else if (getVolume()<d.getVolume())
				return 1;
			else {
				if (getAdjustedClose()<d.getAdjustedClose())
					return -1;
				else if (getAdjustedClose()>d.getAdjustedClose())
					return 1;
				else return 0;
			}
		}
	}
		
	
	public LocalDateTime getTimestamp() {
		return date;
	}
	
	public String toString() {
		StringBuilder buf=new StringBuilder();
		buf.append("YahooHistoricalData { date=");
		buf.append(getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		buf.append(", open=");
		buf.append(open);
		buf.append(", high=");
		buf.append(high);
		buf.append(", low=");
		buf.append(low);
		buf.append(", close=");
		buf.append(close);
		buf.append(", volume=");
		buf.append(volume);
		buf.append(", adjustedClose=");
		buf.append(adjustedClose);		

		buf.append(" }");
		return buf.toString();
		
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public double getVolume() {
		return volume;
	}

	public double getAdjustedClose() {
		return adjustedClose;
	}

	
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getFirstTradedPrice() {
		return getOpen();
	}


	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getLastTradedPrice() {
		return getClose();
	}


	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getTradedVolume() {
		return getVolume();
	}


	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getMaxTradedPrice() {
		return getHigh();
	}


	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getMinTradedPrice() {
		return getLow();
	}


	/**
	 * Implementation of the ConsolidatedTradeRecordsData source.
	 */
	@Override
	public double getAveragedPrice() {
		// approximation
		return (getHigh()+getLow())/2.0;
	}
	
	
	
}