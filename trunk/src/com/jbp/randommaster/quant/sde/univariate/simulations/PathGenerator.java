package com.jbp.randommaster.quant.sde.univariate.simulations;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

public interface PathGenerator<T extends UnivariateStochasticProcess> {

	/**
	 * Given the next step size dt, get a new value.
	 * @param dt 
	 * @return The next value of the stochastic process
	 */
	public double getNext(double dt);
	
	/**
	 * Get next many values by the given count and time step size.
	 * 
	 * @param dt The step size
	 * @param count How many more items to be generated.
	 * @param includeCurrentValue Determines whether include the current value in the result object.
	 * @return An Iterable that contains the generated items.
	 */
	public Iterable<Double> getNextSeries(double dt, int count, boolean includeCurrentValue);
	
	/**
	 * Get filtration up to current time
	 * @return
	 */
	public Filtration<Double> getFiltration();
	
	
	/**
	 * Get the process that generates the values.
	 */
	public T getProcess();
	
	
	
}
