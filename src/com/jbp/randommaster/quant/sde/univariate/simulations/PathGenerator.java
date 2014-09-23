package com.jbp.randommaster.quant.sde.univariate.simulations;

import java.util.stream.Stream;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

public interface PathGenerator<T extends UnivariateStochasticProcess> {

	/**
	 * Move to the next step with size dt.
	 * @param dt 
	 * @return The next value of the stochastic process
	 */
	public double nextStep(double dt);
	
	/**
	 * Get filtration up to current time
	 * @return
	 */
	public Filtration<Double> getFiltration();
	
	/**
	 * Get the process that generates the values.
	 */
	public T getProcess();

	

	/**
	 * Stream indefinitely by the given step size dt. The stream iterator will never terminate.
	 * 
	 * @param dt Time step size.
	 */
	public Stream<Filtration<Double>> stream(double dt);

	/**
	 * Stream by given time step size dt, bound by maximum time maxT.
	 * 
	 * @param dt Time step size.
	 * @param maxT maximum time limit. The resulting filtration objects will not exceed this time limit.
	 * 
	 */
	public Stream<Filtration<Double>> streamUpToTime(double dt, double maxT);
	
	/**
	 * Stream by given time step size dt and number of steps.
	 * 
	 * @param dt The time step size.
	 * @param numberOfSteps The number of steps to generate. THe total number in the resulting stream will be equals to this value.
	 */
	public Stream<Filtration<Double>> streamSteps(double dt, long numberOfSteps);
	
}
