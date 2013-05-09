package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.Filtration;

/**
 * 
 * Implementation of sigma(t, Xt) concept, in a generic SDE of the form: dXt = mu(t,Xt) dt + sigma(t, Xt) dWt. 
 *
 */
public interface DiffusionTerm {

	
	/**
	 * Evaluate the diffusion term by given filtration which includes time and the current value of the process.
	 * @param f The information up to current time.
	 * @return The value of the diffusion term.
	 */
	public double evaluate(Filtration<Double> f);	
}
