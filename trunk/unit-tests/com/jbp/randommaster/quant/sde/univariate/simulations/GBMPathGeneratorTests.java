package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GBMPathGeneratorTests extends TestCase {

	@Test
	public void testGBMSimulation() {
		
		NormalDistribution standardNormal = new NormalDistribution(0,1);
		standardNormal.reseedRandomGenerator(123215464334L);
		
		double mu=0.25;
		double sigma = 0.3;
		
		GeometricBrownianMotion gbm=new GeometricBrownianMotion(mu, sigma);
		
		double x0=103.0;
		
		int pathCount=2000000;

		double[] finalX = new double [pathCount];
		double dt=1.0/252.0;		
		for (int i=0;i<pathCount;i++) {
			GBMPathGenerator gen=new GBMPathGenerator(gbm, x0, standardNormal);
			finalX[i] = gen.nextStep(dt);
		}
		
		
		double estimatedMean = StatUtils.mean(finalX);
		double estimatedVariance = StatUtils.variance(finalX);
		
		double analyticMean = x0 * Math.exp(mu*dt);
		double analyticVariance = x0 * x0 * Math.exp(2.0 * mu * dt) * (Math.exp(sigma*sigma*dt) - 1.0);
		
		Assert.assertEquals("Estimated Mean("+estimatedMean+") is significantly off from analytical value "+analyticMean, 
				analyticMean, estimatedMean, 0.01);
		Assert.assertEquals("Estimated Variance("+estimatedVariance+") is significantly off from analytical value "+analyticVariance, 
				analyticVariance, estimatedVariance, 0.01);		
		
	}
	
}
