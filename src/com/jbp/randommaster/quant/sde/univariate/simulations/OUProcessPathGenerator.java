package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;

/**
 * Implement the exact simulation algorithm on OUProcess.
 *
 */
public class OUProcessPathGenerator implements PathGenerator<OUProcess> {

	private OUProcess process;
	private Filtration<Double> ft;
	
	private NormalDistribution normDist;
	
	public OUProcessPathGenerator(OUProcess process, double initValue, long seed) {
		this.process=process;
		ft=new Filtration<Double>();
		ft.setProcessValue(initValue);
		ft.setTime(0.0);
		
		normDist=new NormalDistribution(0.0, 1.0);
		normDist.reseedRandomGenerator(seed);		
	}
	
	@Override
	public double getNext(double dt) {
		
		double theta = process.getDrift().getTheta();
		double mu = process.getDrift().getMu();
		double sigma = process.getDiffusion().getSigma();

		double lastX = ft.getProcessValue();
		
		// exact simulation
		double stepMean = Math.exp(-theta*dt)*lastX + mu*(1.0 - Math.exp(-theta*dt));
		double stepVol = Math.sqrt(sigma * sigma / (2.0 * theta) * (1.0 - Math.exp(-2.0 * theta * dt)));
		double newX = stepMean + stepVol * normDist.sample();
		
		ft.setProcessValue(newX);
		ft.incrementTime(dt);
		
		return newX;
	}

	@Override
	public OUProcess getProcess() {
		return process;
	}

}
