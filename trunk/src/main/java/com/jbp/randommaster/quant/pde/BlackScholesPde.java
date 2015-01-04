package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.analysis.MultivariateFunction;

/**
 * 
 * General form of Black Scholes PDE <br/>
 * 
 * dV/dt + mu(t,x) dV/dx + sigma(t,x) d2V/dx2 + r(t,x) V = 0 
 *
 */
public class BlackScholesPde {

	// the term mu(t,x)
	private MultivariateFunction mu;
	
	// the term sigma(t,x)
	private MultivariateFunction sigma;
	
	// the term r(t,x)
	private MultivariateFunction r;
	
	public BlackScholesPde() {
		// nothing, use setters to set the coefficients.
	}
	
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


	public void setMu(MultivariateFunction mu) {
		this.mu = mu;
	}


	public void setSigma(MultivariateFunction sigma) {
		this.sigma = sigma;
	}


	public void setR(MultivariateFunction r) {
		this.r = r;
	}
	
	
	
	
	
	
	
}
