package com.jbp.randommaster.quant.sde.univariate.simulations;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

import com.jbp.randommaster.quant.sde.univariate.OUProcess;

import junit.framework.Assert;
import junit.framework.TestCase;

public class OUProcessPathGeneratorTests extends TestCase {

	
	@Test
	public void testOUSimulation() {
		
		NormalDistribution standardNormal = new NormalDistribution(0,1);
		standardNormal.reseedRandomGenerator(123215464334L);
		
		double theta = 30.0;
		double mu = 100.0;
		double sigma = 20.0;

		OUProcess ou=new OUProcess(theta, mu, sigma);

		int pathCount=1000000;
		double x0=95.0;

		double[] finalX = new double [pathCount];
		double dt=1.0/252.0;
		
		for (int i=0;i<pathCount;i++) {
			OUProcessPathGenerator gen = new OUProcessPathGenerator(ou, x0, standardNormal);
			finalX[i] = gen.getNext(dt);
		}
		
		double estimatedMean = StatUtils.mean(finalX);
		double estimatedVariance = StatUtils.variance(finalX);
		
		double analyticMean = Math.exp(-theta*dt) * x0 + mu * (1.0 - Math.exp(-theta*dt));
		double analyticVariance = sigma*sigma/ (2.0 * theta) * (1.0 - Math.exp(-2.0 * theta * dt));
		
		/*
		System.out.println(estimatedMean);
		System.out.println(estimatedVariance);
		System.out.println(analyticMean);
		System.out.println(analyticVariance);*/
		
		Assert.assertEquals("Estimated Mean("+estimatedMean+") is significantly off from analytical value "+analyticMean, 
				analyticMean, estimatedMean, 0.01);
		Assert.assertEquals("Estimated Variance("+estimatedVariance+") is significantly off from analytical value "+analyticVariance, 
				analyticVariance, estimatedVariance, 0.01);
		
	}

}
