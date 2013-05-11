package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.analysis.MultivariateFunction;

/**
 * 
 * General form of Black Scholes PDE <br/>
 * 
 * dV/dt + mu(x,t) dV/dx + sigma(x,t) d2V/dx2 + r(x,t) V = 0 
 *
 */
public class BlackScholesPde {

	// the term mu(x,t)
	private MultivariateFunction mu;
	
	// the term sigma(x,t)
	private MultivariateFunction sigma;
	
	// the term r(x,t)
	private MultivariateFunction r;
	
	
	public BlackScholesPde(MultivariateFunction mu, MultivariateFunction sigma, MultivariateFunction r) {

		this.mu=mu;
		this.sigma=sigma;
		this.r=r;
		
	}


	public MultivariateFunction getMu() {
		return mu;
	}


	public MultivariateFunction getSigma() {
		return sigma;
	}


	public MultivariateFunction getR() {
		return r;
	}
	
	
	
	
	
	
}
