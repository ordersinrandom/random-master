package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;

/**
 * Implements the exact simulation algorithm on GeometricBrownianMotion
 *
 */
public class GBMPathGenerator extends AbstractPathGenerator<GeometricBrownianMotion> {
	
	private NormalDistribution normDist;

	public GBMPathGenerator(GeometricBrownianMotion gbm, double initValue, NormalDistribution normDist) {
		
		super(gbm, initValue);
		
		this.normDist=normDist;
		
	}
	
	@Override
	public double getNext(double dt) {
		
		GeometricBrownianMotion gbm=super.getProcess();
		Filtration<Double> ft=super.getFiltration();

		double mu = gbm.getDrift().getMu();
		double sigma = gbm.getDiffusion().getSigma();
		double dWt = normDist.sample() * Math.sqrt(dt);
		double xt = ft.getProcessValue();
		double result = xt * Math.exp( (mu - Math.pow(sigma, 2.0) / 2.0) * dt + sigma * dWt);
		
		// adjust the filtration.
		//ft.incrementTime(dt);
		//ft.setProcessValue(result);

		return result;
	}


}
