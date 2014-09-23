package com.jbp.randommaster.quant.sde.univariate.simulations;

import java.util.Iterator;

import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;

/**
 * 
 * PathsFactory holds a PathGenerator object and will generate paths based on the same filtration starting point.
 *
 */
public class PathsFactory<T extends UnivariateStochasticProcess> {
	
	private PathGenerator<T> generator;
	
	public PathsFactory(PathGenerator<T> generator) {
		this.generator = generator;
	}
	
	/**
	 * Get next many values by the given count and time step size.
	 * 
	 * @param dt The step size
	 * @param count How many more items to be generated.
	 * @param includeCurrentValue Determines whether include the current value in the result object.
	 * @return An Iterable that contains the generated items.
	 */
	public Iterable<Double> getNextSeries(double dt, int count, boolean includeCurrentValue) {
		
		return () -> new SingleSeriesIterator<T>(generator, dt, count, includeCurrentValue);
	}
	
	
	private static class SingleSeriesIterator<T extends UnivariateStochasticProcess> implements Iterator<Double> {
		
		private PathGenerator<T> generator;
		private double dt;
		private int steps;
		private int currentIndex;
		private boolean includeCurrentValue;
		private boolean iterationStarted;
		
		public SingleSeriesIterator(PathGenerator<T> generator, double dt, int steps, boolean includeCurrentValue) {
			if (dt<=0.0)
				throw new IllegalArgumentException("SingleSeriesIterator: dt <= 0");
			if (steps<=0)
				throw new IllegalArgumentException("SingleSeriesIterator: steps <= 0");
			this.generator=generator.deepClone();
			this.dt=dt;
			this.steps=steps;
			this.includeCurrentValue=includeCurrentValue;
			iterationStarted=false;
			currentIndex = 0;
		}
		

		@Override
		public boolean hasNext() {
			if (!iterationStarted)
				return true;
			else return currentIndex < steps;
		}

		@Override
		public Double next() {
			
			if (!iterationStarted) {
				iterationStarted=true;
				if (includeCurrentValue) {
					currentIndex++;
					return generator.getFiltration().getProcessValue();
				}
			}
			
			double nextVal = generator.getNext(dt);
			currentIndex++;
			return nextVal;
			
		}
		
	}

}
