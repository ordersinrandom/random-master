package com.jbp.randommaster.quant.sde.univariate.simulations;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	 * Get filtration up to current time
	 * @return
	 */
	public Filtration<Double> getFiltration();
	
	/**
	 * Get the process that generates the values.
	 */
	public T getProcess();

	
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
