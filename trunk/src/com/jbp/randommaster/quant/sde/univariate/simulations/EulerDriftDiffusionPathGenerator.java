package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.DiffusionTerm;
import com.jbp.randommaster.quant.sde.univariate.DriftDiffusionProcess;
import com.jbp.randommaster.quant.sde.univariate.DriftTerm;

/**
 * 
 * A generic implementation of the Euler simulation scheme on drift diffusion processes.
 *
 * @param <T> Any DriftDiffusionProcess we want to simulate the paths.
 */
public class EulerDriftDiffusionPathGenerator<T extends DriftDiffusionProcess<? extends DriftTerm, ? extends DiffusionTerm>> extends AbstractPathGenerator<T> {

	private NormalDistribution normDist;
	
	public EulerDriftDiffusionPathGenerator(T driftDiffusionProcess, Filtration<Double> initFt, long seed) {
		
		super(driftDiffusionProcess, initFt);
		
		
		normDist=new NormalDistribution(0.0, 1.0);
		normDist.reseedRandomGenerator(seed);		
	}
	
	@Override
	public double getNext(double dt) {
		
		Filtration<Double> ft=super.getFiltration();
		
		DriftDiffusionProcess<? extends DriftTerm, ? extends DiffusionTerm> process=super.getProcess();
		
		double dWt = normDist.sample() * Math.sqrt(dt);
		
		double dXt = process.getDrift().evaluate(ft) * dt 
				+ process.getDiffusion().evaluate(ft) * dWt;
		
		double result = ft.getProcessValue()+dXt;
		
		ft.setProcessValue(result);
		ft.incrementTime(dt);
		
		return result;
	}
	
}
