package com.jbp.randommaster.quant.pde;

import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import com.jbp.randommaster.quant.pde.BlackScholesPdeFiniteDifferenceSolver.Solution;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BlackScholesPdeFiniteDifferenceSolverTest extends TestCase {


	@Test
	public void testBasicBlackScholes() {
		
		
		final double riskFree = 0.05;
		final double vol = 0.35;
		final double strike = 10.0;
		
		MultivariateFunction mu = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[0];
				return riskFree * x;
			} 
		};
		
		MultivariateFunction sigma = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[0];
				return 0.5 * vol * vol * x * x;
			}
			
		};
		
		MultivariateFunction r = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				return -riskFree;
			}
		};
		
		BlackScholesPde pde = new BlackScholesPde(mu, sigma, r);
		
		

		MultivariateFunction boundaryConditionAtT = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[0];
				if (x>strike)
					return x-strike; // vanilla call payoff
				else return 0.0;
			}
		};
		
		
		MultivariateFunction boundaryConditionAtMaxX = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[0];
				double t = input[1];
				double discountedMax= Math.exp( -riskFree * t) * (x-strike); // approximation
				return discountedMax;
			}
		};
		
		MultivariateFunction boundaryConditionAtMinX = new MultivariateFunction() {
			public double value(double[] input) {
				return 0.0;
			}
		};
		
		
		BlackScholesPdeFiniteDifferenceSolver solver= new BlackScholesPdeFiniteDifferenceSolver(
				pde, boundaryConditionAtT, boundaryConditionAtMaxX, boundaryConditionAtMinX, 800, 600);
		
		
		double tmat = 0.5;
		Solution sol=solver.computePde(100.0, 0.0, tmat);

		List<double[]> grid=sol.getPdeGrid();
		Assert.assertNotNull("The result grid is null", grid);
		

		for (double spot=0.1;spot<=20.0;spot+=0.1) {
			double pdeSolution = sol.getSolutionAtTime0(spot);
			double closeForm = getCloseFormCallPrice(spot, strike, vol, tmat, riskFree);
			Assert.assertEquals("PDE solution ("+pdeSolution+") significantly different from close form ("+closeForm+")", 
					closeForm, pdeSolution, 0.001);
		}
		
				
		
	}
	
	
	private double getCloseFormCallPrice(double spot, double strike, double vol, double tmat, double riskFree) {
		
		// close form
		double d1 = (Math.log(spot/strike) + (riskFree + 0.5 * vol * vol) * tmat)/(vol * Math.sqrt(tmat));
		double d2 = (Math.log(spot/strike) + (riskFree - 0.5 * vol * vol) * tmat)/(vol * Math.sqrt(tmat));
		NormalDistribution normDist=new NormalDistribution(0,1);
		double callPrice = spot*normDist.cumulativeProbability(d1) - strike * Math.exp(-riskFree*tmat) * normDist.cumulativeProbability(d2);
		return callPrice;
	}
	
}
