package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class YahooHistoricalData implements HistoricalData<YahooHistoricalDataTuple> {

	private static final long serialVersionUID = 1643322330608553665L;
	
	private LocalDateTime date;
	
	private YahooHistoricalDataTuple tuple;
	
	public YahooHistoricalData(LocalDateTime date, double open, double high, double low, double close, double volume, double adjustedClose) {
		this.date=date;
		
		tuple=new YahooHistoricalDataTuple(open, high, low, close, volume, adjustedClose);
		
	}
	
	public int hashCode() {
		LocalDateTime d=getTimestamp();
		return d.getYear() << 16 
				| d.getMonthOfYear() << 8
				| d.getDayOfMonth();
	}
	
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof YahooHistoricalData) {
			YahooHistoricalData d=(YahooHistoricalData) obj;
			return d.getTimestamp().equals(this.getTimestamp())
					&& d.getData().getOpen()==this.getData().getOpen()
					&& d.getData().getHigh()==this.getData().getHigh()
					&& d.getData().getLow()==this.getData().getLow()
					&& d.getData().getClose()==this.getData().getClose()
					&& d.getData().getVolume()==this.getData().getVolume()
					&& d.getData().getAdjustedClose()==this.getData().getAdjustedClose();
		}
		else return false;
	}
	
	
	@Override
	public int compareTo(HistoricalData<? extends HistoricalDataTuple> data) {
		int c=getTimestamp().compareTo(data.getTimestamp());
		if (c!=0)
			return c;
		else {
			
			YahooHistoricalDataTuple d=((YahooHistoricalData) data).getData();
			
			
			// if the volume is 0, we put it at the back, those are corp action items.
			if (tuple.getVolume()>d.getVolume())
				return -1;
			else if (tuple.getVolume()<d.getVolume())
				return 1;
			else {
				if (tuple.getAdjustedClose()<d.getAdjustedClose())
					return -1;
				else if (tuple.getAdjustedClose()>d.getAdjustedClose())
					return 1;
				else return 0;
			}
		}
	}
		
	
	public LocalDateTime getTimestamp() {
		return date;
	}
	
	public YahooHistoricalDataTuple getData() {
		return tuple;
	}
	

	public String toString() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		StringBuilder buf=new StringBuilder();
		buf.append("YahooHistoricalData { date=");
		buf.append(getTimestamp().toString(fmt));
		buf.append(", open=");
		buf.append(tuple.getOpen());
		buf.append(", high=");
		buf.append(tuple.getHigh());
		buf.append(", low=");
		buf.append(tuple.getLow());
		buf.append(", close=");
		buf.append(tuple.getClose());
		buf.append(", volume=");
		buf.append(tuple.getVolume());
		buf.append(", adjustedClose=");
		buf.append(tuple.getAdjustedClose());
		buf.append(" }");
		return buf.toString();
		
	}


}
