package com.jbp.randommaster.quant.sde.univariate;

import java.io.Serializable;

import com.jbp.randommaster.quant.sde.Filtration;

/**
 * 
 * A simple implementation of a constant diffusion.
 *
 */
public class ConstantDiffusion implements DriftTerm, Serializable, Cloneable {

	
	private static final long serialVersionUID = -6186046043165867589L;
	
	
	protected double sigma;
	
	public ConstantDiffusion(double sigma) {
		this.sigma=sigma;
	}
	
	
	@Override
	public double evaluate(Filtration<Double> f) {
		// depends on nothing at the current time 
		return sigma;
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
		return "Diffusion { sigma="+sigma+" }";
	}

}
