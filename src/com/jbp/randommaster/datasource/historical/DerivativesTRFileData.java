package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;


public class DerivativesTRFileData implements HistoricalData<DerivativesTRFileTuple> {

	private static final long serialVersionUID = -8221738364963370500L;

	private DerivativesTRFileTuple tuple;
	
	
	public DerivativesTRFileData(String inputLine) {
		
		tuple=DerivativesTRFileTuple.parse(inputLine);
	}

	public int hashCode() {
		return tuple.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof DerivativesTRFileData) {
			DerivativesTRFileData d=(DerivativesTRFileData) obj;
			return this.tuple.equals(d.tuple);
		}
		else return false;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e1) {
			return null;
		}
	}
	
	@Override
	public int compareTo(HistoricalData<? extends HistoricalDataTuple> o) {
		
		int c1=this.getTimestamp().compareTo(o.getTimestamp());
		if (c1!=0)
			return c1;

		// this is bad. 
		// it doesn't distinguish trade futures / options etc etc...
		return this.getData().getClassCode().compareTo(((DerivativesTRFileTuple) o.getData()).getClassCode());
		
	}

	@Override
	public LocalDateTime getTimestamp() {
		return tuple.getTradeTimestamp();
	}

	@Override
	public DerivativesTRFileTuple getData() {
		return tuple;
	}

	
	public String toString() {
		StringBuilder buf=new StringBuilder(300);
		buf.append("HkexTRFileData { tuple=");
		buf.append(tuple);
		buf.append(" }");
		return buf.toString();
		
	}
}
