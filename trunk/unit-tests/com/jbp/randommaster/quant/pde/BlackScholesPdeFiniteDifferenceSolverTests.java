package com.jbp.randommaster.quant.pde;

import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import com.jbp.randommaster.quant.pde.BlackScholesPdeFiniteDifferenceSolver.Solution;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;
import com.jbp.randommaster.quant.sde.univariate.simulations.OUProcessPathGenerator;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BlackScholesPdeFiniteDifferenceSolverTests extends TestCase {


	@Test
	public void testBlackScholesEuropeanCall() {
		
		
		final double riskFree = 0.05;
		final double vol = 0.35;
		final double strike = 10.0;
		
		MultivariateFunction mu = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				return riskFree * x;
			} 
		};
		
		MultivariateFunction sigma = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
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
				double x = input[1];
				if (x>strike)
					return x-strike; // vanilla call payoff
				else return 0.0;
			}
		};
		
		
		MultivariateFunction boundaryConditionAtMaxX = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				double t = input[0];
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
			double closeForm = getCloseFormEuropeanCallPrice(spot, strike, vol, tmat, riskFree);
			Assert.assertEquals("PDE solution ("+pdeSolution+") significantly different from close form ("+closeForm+")", 
					closeForm, pdeSolution, 0.001);
		}
		
				
		
	}
	
	
	private double getCloseFormEuropeanCallPrice(double spot, double strike, double vol, double tmat, double riskFree) {
		
		// close form
		double d1 = (Math.log(spot/strike) + (riskFree + 0.5 * vol * vol) * tmat)/(vol * Math.sqrt(tmat));
		double d2 = (Math.log(spot/strike) + (riskFree - 0.5 * vol * vol) * tmat)/(vol * Math.sqrt(tmat));
		NormalDistribution normDist=new NormalDistribution(0,1);
		double callPrice = spot*normDist.cumulativeProbability(d1) - strike * Math.exp(-riskFree*tmat) * normDist.cumulativeProbability(d2);
		return callPrice;
	}
	
	
	
	@Test
	public void testOUProcessEuropeanPut() { 
		final double theta = 50;
		final double mu = 30;
		final double sigma = 80;
		
		final double strike = 30;
		
		final double x0 = 30;
		
		final double tmat = 1.0;
		
		OUProcess ou=new OUProcess(theta, mu, sigma);
		
		FeynmanKacFormula pde=new FeynmanKacFormula(ou);
		
		
		
		MultivariateFunction boundaryConditionAtT = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				if (x<strike)
					return strike-x; // vanilla put payoff
				else return 0.0;
			}
		};
		
		
		MultivariateFunction boundaryConditionAtMinX = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				return (strike -x);
			}
		};
		
		
		
		MultivariateFunction boundaryConditionAtMaxX = new MultivariateFunction() {
			public double value(double[] input) {
				return 0.0;
			}
		};		
		
		
		BlackScholesPdeFiniteDifferenceSolver solver= new BlackScholesPdeFiniteDifferenceSolver(
				pde, boundaryConditionAtT, boundaryConditionAtMaxX, boundaryConditionAtMinX, 800, 600);

		Solution sol=solver.computePde(mu + 5 * sigma, mu - 5 * sigma, tmat);
		
		// solve the PDE to get the put price.
		double pdePutPrice=sol.getSolutionAtTime0(x0);
		
		
		// solve the square of the put price in order to get the Standard Deviation of the OU Process payoffs 
		MultivariateFunction squareBoundaryConditionAtT = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				if (x<strike)
					return Math.pow(strike-x, 2.0); // square of the put payoff
				else return 0.0;
			}
		};
		
		
		MultivariateFunction squareBoundaryConditionAtMinX = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				return Math.pow(strike -x, 2.0);
			}
		};		

		BlackScholesPdeFiniteDifferenceSolver squareSolver= new BlackScholesPdeFiniteDifferenceSolver(
				pde, squareBoundaryConditionAtT, boundaryConditionAtMaxX, squareBoundaryConditionAtMinX, 800, 600);
		
		Solution squareSol=squareSolver.computePde(mu + 5 * sigma, mu - 5 * sigma, tmat);
		double pdeSquarePutPrice=squareSol.getSolutionAtTime0(x0);
		double pdePutPriceSD = Math.sqrt(pdeSquarePutPrice - pdePutPrice*pdePutPrice);
		

		// run simulation to get the put price and SD
		Pair<Double, Double> simResult=getSimulatedOUPut(ou, x0, strike, tmat);
		double simPutPrice = simResult.getKey();
		double simPutPriceSD = simResult.getValue();
		
//System.out.println("PDE Solution: "+pdePutPrice);		
		
		Assert.assertEquals("PDE solution ("+pdePutPrice+") significantly different from simulated result ("+simPutPrice+")", 
				simPutPrice, pdePutPrice, 0.01);
		
		Assert.assertEquals("PDE estimated SD ("+pdePutPriceSD+") significantly different from simulated result ("+simPutPriceSD+")", 
				simPutPriceSD, pdePutPriceSD, 0.01);
		
	}
	
	
	private Pair<Double, Double> getSimulatedOUPut(OUProcess ou, double x0, double strike, double tmat) {
		// run simulation to get the put price
		int pathCount=500000;
		double[] finalPutPrice = new double [pathCount];
		
		NormalDistribution standardNormal = new NormalDistribution(0,1);
		standardNormal.reseedRandomGenerator(123215464334L);
		
		for (int i=0;i<pathCount;i++) {
			OUProcessPathGenerator gen = new OUProcessPathGenerator(ou, x0, standardNormal);
			double finalX = gen.getNext(tmat);
			finalPutPrice[i] = Math.max(0.0, strike - finalX); // just put, no discounting.
		}
		
		double simPutPrice = StatUtils.mean(finalPutPrice);
	
		double sd = Math.sqrt(StatUtils.variance(finalPutPrice)); // also save down the standard deviation of the sim.
		
		
		return new Pair<Double, Double>(simPutPrice, sd);
	}
	
}
