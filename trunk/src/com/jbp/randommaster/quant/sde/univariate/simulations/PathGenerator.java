package com.jbp.randommaster.quant.sde.univariate.simulations;

import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

public interface PathGenerator<T extends UnivariateStochasticProcess> {

	/**
	 * Given the next step size dt, get a new value.
	 * 
	 * @param dt The time step size.
	 * @return The next value of the stochastic process
	 */
	public double getNext(double dt);
	
	
	/**
	 * Get the process that generates the values.
	 */
	public T getProcess();
	
}
