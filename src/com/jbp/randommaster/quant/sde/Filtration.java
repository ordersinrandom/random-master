package com.jbp.randommaster.quant.sde;

/**
 * 
 * This class represents filtration. All the information up to the current time t.
 *
 */
public class Filtration<T> {

	protected double time;
	// this could be vector or a single Xt
	protected T processValue; 
	
	public Filtration() {
		
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	public void incrementTime(double dt) {
		this.time+=dt;
	}

	public T getProcessValue() {
		return processValue;
	}

	public void setProcessValue(T processValue) {
		this.processValue = processValue;
	}
	
	@Override
	public String toString() {
		StringBuilder buf=new StringBuilder();
		buf.append("F(");
		buf.append(getTime());
		buf.append(") = ");
		buf.append(getProcessValue());
		return buf.toString();
	}
	
}
