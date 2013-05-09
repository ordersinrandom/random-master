package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;

/**
 * Implement the exact simulation algorithm on OUProcess.
 *
 */
public class OUProcessPathGenerator extends AbstractPathGenerator<OUProcess> {

	private NormalDistribution normDist;
	
	public OUProcessPathGenerator(OUProcess ouProcess, double initValue, long seed) {
		
		super(ouProcess, initValue);
		
		normDist=new NormalDistribution(0.0, 1.0);
		normDist.reseedRandomGenerator(seed);		
	}
	
	@Override
	public double getNext(double dt) {
		
		OUProcess ouProcess=super.getProcess();
		Filtration<Double> ft=super.getFiltration();
		
		double theta = ouProcess.getDrift().getTheta();
		double mu = ouProcess.getDrift().getMu();
		double sigma = ouProcess.getDiffusion().getSigma();

		double lastX = ft.getProcessValue();
		
		// exact simulation
		double stepMean = Math.exp(-theta*dt)*lastX + mu*(1.0 - Math.exp(-theta*dt));
		double stepVol = Math.sqrt(sigma * sigma / (2.0 * theta) * (1.0 - Math.exp(-2.0 * theta * dt)));
		double newX = stepMean + stepVol * normDist.sample();
		
		ft.setProcessValue(newX);
		ft.incrementTime(dt);
		
		return newX;
	}

}
