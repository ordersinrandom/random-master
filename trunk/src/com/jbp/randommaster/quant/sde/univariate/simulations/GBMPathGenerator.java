package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;

public class GBMPathGenerator implements PathGenerator<GeometricBrownianMotion> {
	
	private GeometricBrownianMotion gbm;
	
	private boolean useExactSimulation;
	
	// this is non null only if it is not using exact simulation.
	private EulerDriftDiffusionPathGenerator<GeometricBrownianMotion> eulerPathSim;

	// these are used when it is exact simulation
	// representing the current state of the generator
	private Filtration<Double> ft;
	private NormalDistribution normDist;

	/**
	 * Create a GBMPathGenerator, using exact simulation.
	 * 
	 */
	public GBMPathGenerator(GeometricBrownianMotion gbm, double initValue, long seed) {
		this(gbm, initValue, true, seed);
	}
	
	
	public GBMPathGenerator(GeometricBrownianMotion gbm, double initValue, boolean useExactSimulation, long seed) {
		if (gbm==null)
			throw new IllegalArgumentException("Input motion cannot be null");
		
		this.gbm=gbm;
		
		ft=new Filtration<Double>();
		ft.setTime(0.0);
		ft.setProcessValue(initValue);
		
		this.useExactSimulation=useExactSimulation;
		
		if (!useExactSimulation) {
			eulerPathSim=new EulerDriftDiffusionPathGenerator<GeometricBrownianMotion>(gbm, ft, seed);
		}
		else {
			normDist=new NormalDistribution(0.0, 1.0);
			normDist.reseedRandomGenerator(seed);
		}
	}

	@Override
	public double getNext(double dt) {

		double result=Double.NaN;
		
		if (useExactSimulation) {
			double mu = gbm.getDrift().getMu();
			double sigma = gbm.getDiffusion().getSigma();
			double dWt = normDist.sample() * Math.sqrt(dt);
			double xt = ft.getProcessValue();
			result = xt * Math.exp( (mu - Math.pow(sigma, 2.0) / 2.0) * dt + sigma * dWt);
			
			// adjust the filtration.
			ft.incrementTime(dt);
			ft.setProcessValue(result);
		}
		else {
			// we use euler path generator to do the job
			result=eulerPathSim.getNext(dt);
		}

		return result;
	}

	@Override
	public GeometricBrownianMotion getProcess() {
		return gbm;
	}

	
	
}
