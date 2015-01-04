package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.TimeDomain;

/**
 * 
 * A simple marker interface to categorize all Univariate stochastic processes.
 *
 */
public interface UnivariateStochasticProcess {

	public TimeDomain getTimeDomain();
	
}
