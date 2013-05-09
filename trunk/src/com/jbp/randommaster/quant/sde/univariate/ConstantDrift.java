package com.jbp.randommaster.quant.sde.univariate;

import java.io.Serializable;

import com.jbp.randommaster.quant.sde.Filtration;

/**
 * 
 * A simple implementation of a constant drift.
 *
 */
public class ConstantDrift implements DriftTerm, Serializable, Cloneable {

	private static final long serialVersionUID = -8294861739839040313L;

	protected double mu;
	
	public ConstantDrift(double mu) {
		this.mu=mu;
	}
	
	
	@Override
	public double evaluate(Filtration<Double> f) {
		// depends on nothing at the current time 
		return mu;
	}
	
	@Override
	public Object clone() {
		try {
			Object r=super.clone();
			return r;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public String toString() {
		return "Drift { mu="+mu+" }";
	}

}
