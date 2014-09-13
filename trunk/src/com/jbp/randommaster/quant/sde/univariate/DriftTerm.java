package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.Filtration;


/**
 * 
 * Implementation of  mu(t, Xt) concept, in a generic SDE of the form: dXt = mu(t,Xt) dt + sigma(t, Xt) dWt. 
 *
 */
@FunctionalInterface
public interface DriftTerm  {

	/**
	 * Evaluate the drift term by given filtration which includes time and the current value of the process.
	 * @param f The information up to current time.
	 * @return The value of the drift term.
	 */
	public double evaluate(Filtration<Double> f);		
}
