package com.janp.randommaster.datasource.historical;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class YahooHistoricalData implements HistoricalData {

	private static final long serialVersionUID = 1643322330608553665L;
	
	private LocalDate date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	private double adjustedClose;
	
	public YahooHistoricalData(LocalDate date, double open, double high, double low, double close, double volume, double adjustedClose) {
		this.date=date;
		this.open=open;
		this.high=high;
		this.low=low;
		this.close=close;
		this.volume=volume;
		this.adjustedClose=adjustedClose;
	}
	
	public LocalDate getDate() {
		return date;
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

	public String toString() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		StringBuilder buf=new StringBuilder();
		buf.append("YahooHistoricalData { date=");
		buf.append(date.toString(fmt));
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
	
}
