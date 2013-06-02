package com.jbp.randommaster.datasource.historical;

/**
 * 
 * Encapsulation of Yahoo Historical data (open, high, low, close, volume, adjusted close).
 *
 */
public class YahooHistoricalDataTuple implements HistoricalDataTuple {
	
	private static final long serialVersionUID = 1848524988662119076L;
	
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	private double adjustedClose;
	
	public YahooHistoricalDataTuple(double open, double high, double low, double close, double volume, double adjustedClose) {
		this.open=open;
		this.high=high;
		this.low=low;
		this.close=close;
		this.volume=volume;
		this.adjustedClose=adjustedClose;
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
		StringBuilder buf=new StringBuilder(300);
		buf.append("YahooHistoricalDataTuple { open=");
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
		return buf.toString();
	}
	
	
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof YahooHistoricalDataTuple) {
			YahooHistoricalDataTuple t=(YahooHistoricalDataTuple) obj;
			return open==t.open && high==t.high && low==t.low && close==t.close && volume==t.volume && adjustedClose==t.adjustedClose;
		}
		else return false;
	}

	
	public int hashCode() {
		return Double.valueOf(open).hashCode()
				^Double.valueOf(high).hashCode()
				^Double.valueOf(low).hashCode()
				^Double.valueOf(close).hashCode()
				^Double.valueOf(volume).hashCode()
				^Double.valueOf(adjustedClose).hashCode();
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}
}
