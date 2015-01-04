package com.jbp.randommaster.quant.sde.univariate;

import com.jbp.randommaster.quant.sde.Filtration;

/**
 * 
 * Implementation of Brownian Bridge where the SDE is <br/>
 * 
 * dXt = (X(T) - Xt) / (T - t) dt + dWt
 *
 */
public class BrownianBridge extends DriftDiffusionProcess<BrownianBridge.Drift, ConstantDiffusion> {

	public BrownianBridge(double endTime,  double endValue) {
		super();

		setDrift(new Drift(endTime, endValue));
		setDiffusion(new ConstantDiffusion(1.0));
		
		// brownian bridge cannot have any value after endTime
		getTimeDomain().setEnd(endTime);
	}
	

	public class Drift implements DriftTerm {
		
		private double endTime;
		private double endValue;
		
		public Drift(double endTime, double endValue) {
			this.endTime=endTime;
			this.endValue=endValue;
		}

		@Override
		public double evaluate(Filtration<Double> f) {
			
			double currentTime=f.getTime();
			
			if (endTime-currentTime>0.0) 
				return (endValue - f.getProcessValue()) / (endTime - currentTime);
			else return 0.0; // maybe incorrect for this setting
		}
		
		@Override
		public String toString() {
			
			return "Drift { endTime="+endTime+", endValue="+endValue+" }";
		}
		
	}
	
	
	
}
