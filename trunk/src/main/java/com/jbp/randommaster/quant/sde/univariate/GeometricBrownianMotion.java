package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.Filtration;

/**
 * 
 * Define the process
 * <br/>
 * 
 * dXt = mu * Xt * dt + sigma * Xt * dWt
 *
 */
public class GeometricBrownianMotion extends DriftDiffusionProcess<GeometricBrownianMotion.Drift, GeometricBrownianMotion.Diffusion> {

	public GeometricBrownianMotion(double mu, double sigma) {
		super();
		
		setDrift(new Drift(mu));
		setDiffusion(new Diffusion(sigma));
		
	}
	
	public class Drift implements DriftTerm {

		private double mu;
		
		public Drift(double mu) {
			this.mu=mu;
		}
		
		public double getMu() {
			return mu;
		}
		
		@Override
		public double evaluate(Filtration<Double> f) {
			double xt=f.getProcessValue();
			return mu*xt;
		}
		
		public String toString() {
			return "GBM Drift { "+mu+" }";
		}
		
		
		
	}
	
	public class Diffusion implements DiffusionTerm {
		
		private double sigma;
		
		public Diffusion(double sigma) {
			this.sigma=sigma;
		}

		public double getSigma() {
			return sigma;
		}

		@Override
		public double evaluate(Filtration<Double> f) {
			double xt=f.getProcessValue();
			return sigma*xt;
		}
		
		public String toString() {
			return "GBM Diffusion { "+sigma+" }";
		}
		
		
		
	}
	
}
