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
public class EulerDriftDiffusionPathGenerator<T extends DriftDiffusionProcess<? extends DriftTerm, ? extends DiffusionTerm>> implements PathGenerator<T>{

	private T process;
	private Filtration<Double> currentFt;
	private NormalDistribution normDist;
	
	public EulerDriftDiffusionPathGenerator(T process, Filtration<Double> initFt, long seed) {
		this.process=process;
		this.currentFt=initFt;
		
		normDist=new NormalDistribution(0.0, 1.0);
		normDist.reseedRandomGenerator(seed);		
	}
	
	@Override
	public double getNext(double dt) {
		
		double dWt = normDist.sample() * Math.sqrt(dt);
		
		double dXt = process.getDrift().evaluate(currentFt) * dt + process.getDiffusion().evaluate(currentFt) * dWt;
		
		double result = currentFt.getProcessValue()+dXt;
		
		currentFt.setProcessValue(result);
		currentFt.incrementTime(dt);
		
		return result;
	}

	@Override
	public T getProcess() {
		return process;
	}


}
