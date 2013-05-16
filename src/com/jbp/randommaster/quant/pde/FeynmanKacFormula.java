package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.analysis.MultivariateFunction;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.DiffusionTerm;
import com.jbp.randommaster.quant.sde.univariate.DriftDiffusionProcess;
import com.jbp.randommaster.quant.sde.univariate.DriftTerm;

/**
 * 
 * An implementation of the Feynman Kac theorem converting a drift diffusion process to a Black Scholes PDE.
 *
 */
public class FeynmanKacFormula extends BlackScholesPde {
	
	private DriftTerm muTerm;
	private DiffusionTerm sigmaTerm;

	public FeynmanKacFormula(DriftDiffusionProcess<? extends DriftTerm, ? extends DiffusionTerm> sde) {
		super();
		
		this.muTerm=sde.getDrift();
		this.sigmaTerm=sde.getDiffusion();
		
		super.setMu(new MuFunction());
		super.setSigma(new SigmaFunction());
		super.setR(new RFunction());
	}
	
	
	private class MuFunction implements MultivariateFunction {

		@Override
		public double value(double[] input) {
			double t = input[0];
			double x = input[1];
			
			Filtration<Double> f = new Filtration<Double>();
			f.setTime(t);
			f.setProcessValue(x);
			
			return muTerm.evaluate(f);
		}
		
	}
	
	private class SigmaFunction implements MultivariateFunction {

		@Override
		public double value(double[] input) {
			double t = input[0];
			double x = input[1];
			
			Filtration<Double> f = new Filtration<Double>();
			f.setTime(t);
			f.setProcessValue(x);
			
			double sigmaValue = sigmaTerm.evaluate(f);
			
			return 1.0 / 2.0 * sigmaValue * sigmaValue;
			
		}
		
	}
	
	
	private class RFunction implements MultivariateFunction {

		@Override
		public double value(double[] input) {
			// nothing.. always return 0
			return 0;
		}
		
	}
	
	
	
	
}
