package com.jbp.randommaster.quant.sde;


/**
 * 
 * This class represents filtration. All the information up to the current time t.
 *
 */
public class Filtration<T> implements Cloneable {
	
	protected double time;
	// this could be vector or a single Xt
	protected T processValue; 
	
	public Filtration() {
		
	}
	
	public Filtration(T initValue, double initTime) {
		processValue=initValue;
		time=initTime;
	}
	
	public Filtration(T initValue) {
		this(initValue, 0.0);
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
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e1) {
			return null;
		}
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
