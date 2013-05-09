package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.Filtration;

public class OUProcess extends DriftDiffusionProcess<OUProcess.MeanRevertingTerm, ConstantDiffusion> {

	public OUProcess(double theta, double mu, double sigma) {
		super();
		
		setDrift(new MeanRevertingTerm(theta, mu));
		setDiffusion(new ConstantDiffusion(sigma));
	}
	
	
	public class MeanRevertingTerm implements DriftTerm {
		
		private double theta;
		private double mu;
		
		public MeanRevertingTerm(double theta, double mu) {
			this.theta=theta;
			this.mu=mu;
		}

		public double getTheta() {
			return theta;
		}

		public double getMu() {
			return mu;
		}

		@Override
		public double evaluate(Filtration<Double> f) {
			double xt = f.getProcessValue();
			return theta * (mu-xt);
		}
		
		@Override
		public String toString() {
			return "OUDrift { theta = "+theta+", mu = "+mu+" }";
		}
		
	}
	
}
