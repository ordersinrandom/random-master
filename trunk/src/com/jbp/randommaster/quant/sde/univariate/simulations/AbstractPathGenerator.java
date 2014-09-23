package com.jbp.randommaster.quant.sde.univariate.simulations;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

/**
 * 
 * An implementation of <code>PathGenerator</code> that provides common features
 * that subclasses would require.
 *
 * @param <T>
 *            The stochastic process that we want to generate random paths.
 */
public abstract class AbstractPathGenerator<T extends UnivariateStochasticProcess> implements PathGenerator<T> {

	private T process;
	private Filtration<Double> ft;

	public AbstractPathGenerator(T process, Filtration<Double> initFt) {
		this.process = process;
		this.ft = initFt;
	}

	public AbstractPathGenerator(T process, double initValue) {
		this(process, new Filtration<Double>(initValue));
	}

	@Override
	public T getProcess() {
		return process;
	}

	@Override
	public Filtration<Double> getFiltration() {
		return ft;
	}
	
	@Override
	public abstract double nextStep(double dt);


	/**
	 * Stream indefinitely by the given step size dt. The stream iterator will never terminate.
	 * 
	 * @param dt Time step size.
	 */
	@Override
	public Stream<Filtration<Double>> stream(double dt) {
		
		Spliterator<Filtration<Double>> spliterator = Spliterators.<Filtration<Double>>spliterator(new IteratorHelper(dt, -1.0, -1L), Long.MAX_VALUE, Spliterator.IMMUTABLE);
		
		return StreamSupport.stream(spliterator, false);
	}

	
	/**
	 * Stream by given time step size dt, bound by maximum time maxT.
	 * 
	 * @param dt Time step size.
	 * @param maxT maximum time limit. The resulting filtration objects will not exceed this time limit.
	 * 
	 */
	@Override
	public Stream<Filtration<Double>> streamUpToTime(double dt, double maxT) {
		
		Spliterator<Filtration<Double>> spliterator = Spliterators.<Filtration<Double>>spliterator(new IteratorHelper(dt, maxT, -1L), Long.MAX_VALUE, Spliterator.IMMUTABLE);

		return StreamSupport.stream(spliterator, false);		
	}
	
	/**
	 * Stream by given time step size dt and number of steps.
	 * 
	 * @param dt The time step size.
	 * @param numberOfSteps The number of steps to generate. THe total number in the resulting stream will be equals to this value.
	 */
	@Override
	public Stream<Filtration<Double>> streamSteps(double dt, long numberOfSteps) {	
		Spliterator<Filtration<Double>> spliterator = Spliterators.<Filtration<Double>>spliterator(new IteratorHelper(dt, -1.0, numberOfSteps), Long.MAX_VALUE, Spliterator.IMMUTABLE);

		return StreamSupport.stream(spliterator, false);		
		
	}
	

	private class IteratorHelper implements Iterator<Filtration<Double>> {

		private double dt;
		private double maxT;
		private long numberOfSteps;
		
		private long currentStep;
		private boolean started;
		
		public IteratorHelper(double dt, double maxT, long numberOfSteps) {
			this.dt=dt;
			this.maxT=maxT;
			this.numberOfSteps=numberOfSteps;
			currentStep=0;
			started=false;
		}
		
		@Override
		public boolean hasNext() {
			if (maxT>0.0) {
				return getFiltration().getTime()+dt<=maxT;
			}
			else if (numberOfSteps>0) {
				return currentStep<numberOfSteps;
			}
			else return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Filtration<Double> next() {
			currentStep++;
			
			if (!started) {
				started=true;
				Filtration<Double> initVal = getFiltration();
				return (Filtration<Double>) initVal.clone(); // make a copy, filtration is stateful
			}
			else {
				nextStep(dt);
				Filtration<Double> currentFt = getFiltration();
				return (Filtration<Double>) currentFt.clone(); // make a copy, filtration is stateful
			}
		}
	}
}
