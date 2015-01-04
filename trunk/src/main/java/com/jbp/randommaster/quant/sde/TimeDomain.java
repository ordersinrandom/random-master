package com.jbp.randommaster.quant.sde;

/**
 * Define the valid time domain of a SDE.
 * 
 * i.e. the value of the SDE outside the range defined in this object would be invalid
 *
 */
public class TimeDomain {

	private double start;
	private double end;
	
	public TimeDomain() {
		this(0.0, Double.POSITIVE_INFINITY);
	}
	
	public TimeDomain(double start, double end) {
		this.start=start;
		this.end=end;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}
	
	
	
}
