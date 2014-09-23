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
	
	
	/*
	public default Stream<Filtration<Double>> stream(double dt) {
		
		Spliterator<Filtration<Double>> spliterator = Spliterators.<Filtration<Double>>spliterator(
													new Iterator<Filtration<Double>>() {
														boolean started = false;
														@Override
														public boolean hasNext() {
															return true;
														}
								
														@SuppressWarnings("unchecked")
														@Override
														public Filtration<Double> next() {
															if (!started) {
																started=true;
																Filtration<Double> initVal = getFiltration();
																return (Filtration<Double>) initVal.clone(); // make a copy, filtration is stateful
															}
															else {
																double nextVal = getNext(dt);
																Filtration<Double> currentFt = getFiltration();
																currentFt.setProcessValue(nextVal);
																currentFt.incrementTime(dt);
																return (Filtration<Double>) currentFt.clone(); // make a copy, filtration is stateful
															}
														}
													}, Long.MAX_VALUE, Spliterator.IMMUTABLE);
		
		
		return StreamSupport.stream(spliterator, false);
	}
	*/
	
	/*
	public Object clone();

	@SuppressWarnings("unchecked")
	public default PathGenerator<T> deepClone() {
		PathGenerator<T> p = (PathGenerator<T>) clone();
		Filtration<Double> ft = getFiltration();
		p.setFiltration((Filtration<Double>) ft.clone());
		return p;
		
	}*/
}
