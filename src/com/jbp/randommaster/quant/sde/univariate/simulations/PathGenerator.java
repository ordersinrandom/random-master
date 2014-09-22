package com.jbp.randommaster.quant.sde.univariate.simulations;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

public interface PathGenerator<T extends UnivariateStochasticProcess> extends Cloneable {

	/**
	 * Given the next step size dt, get a new value.
	 * @param dt 
	 * @return The next value of the stochastic process
	 */
	public double getNext(double dt);
	
	/**
	 * Get filtration up to current time
	 * @return
	 */
	public Filtration<Double> getFiltration();
	
	/**
	 * Set the filtration at current time.
	 */
	public void setFiltration(Filtration<Double> ft);
	
	/**
	 * Get the process that generates the values.
	 */
	public T getProcess();
	
	/**
	 * Perform shallow clone clone on the PathGenerator.
	 */
	public Object clone();

	/**
	 * Implementation of deep cloning.
	 */
	@SuppressWarnings("unchecked")
	public default PathGenerator<T> deepClone() {
		PathGenerator<T> p = (PathGenerator<T>) clone();
		Filtration<Double> ft = getFiltration();
		p.setFiltration((Filtration<Double>) ft.clone());
		return p;
		
	}
}
