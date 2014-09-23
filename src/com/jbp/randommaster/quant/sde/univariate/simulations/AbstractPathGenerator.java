package com.jbp.randommaster.quant.sde.univariate.simulations;

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
	public abstract double getNext(double dt);

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}


}
