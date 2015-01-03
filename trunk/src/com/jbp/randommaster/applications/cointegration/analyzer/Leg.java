package com.jbp.randommaster.applications.cointegration.analyzer;

import java.time.YearMonth;


public class Leg {

	private double weight;
	private String underlying;
	private YearMonth expiry;
	private String futuresOrOptions;	
	
	public Leg() {
		
	}
	
	public Leg(double weight, String underlying, YearMonth expiry, String futuresOrOptions) {
		this.weight=weight;
		this.underlying=underlying;
		this.expiry=expiry;
		this.futuresOrOptions=futuresOrOptions;
	}
	

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getUnderlying() {
		return underlying;
	}

	public void setUnderlying(String underlying) {
		this.underlying = underlying;
	}

	public YearMonth getExpiry() {
		return expiry;
	}

	public void setExpiry(YearMonth expiry) {
		this.expiry = expiry;
	}

	public String getFuturesOrOptions() {
		return futuresOrOptions;
	}

	public void setFuturesOrOptions(String futuresOrOptions) {
		this.futuresOrOptions = futuresOrOptions;
	}

	@Override
	public String toString() {
		return "Leg [weight=" + weight + ", underlying=" + underlying + ", expiry=" + expiry + ", futuresOrOptions=" + futuresOrOptions + "]";
	}
	
	
	
}
