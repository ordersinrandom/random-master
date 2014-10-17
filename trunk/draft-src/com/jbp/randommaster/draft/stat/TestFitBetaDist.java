package com.jbp.randommaster.draft.stat;

import java.util.Arrays;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionPenaltyAdapter;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public class TestFitBetaDist {

	private double[] samples;

	public TestFitBetaDist(double[] samples) {
		this.samples = samples;
	}

	public BetaDistribution fit() {
		// initial guess
		InitialGuess startPt = new InitialGuess(new double[] { 15, 10 });
		
		MultivariateFunction likelihoodFunc = new MultivariateFunction() {
			@Override
			public double value(double[] param) {
				RealDistribution dist = new BetaDistribution(param[0], param[1]);
				return Arrays.stream(samples).parallel().map(x -> Math.log(dist.density(x))).sum();
			}
		};
		
		MultivariateFunction unboundedFunc = new MultivariateFunctionPenaltyAdapter(likelihoodFunc, 
				new double[] { 0.00000001, 0.00000001 }, new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },  -100000000000.0, new double[] { 1.0, 1.0 } );
		
		ObjectiveFunction objectiveFunc = new ObjectiveFunction(unboundedFunc);

		// simplex
		NelderMeadSimplex simplex = new NelderMeadSimplex(startPt.getInitialGuess().length);
		
		SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(-1.0, Double.MIN_VALUE));
		
		PointValuePair result = optimizer.optimize(new MaxEval(20000), GoalType.MAXIMIZE, startPt, objectiveFunc, simplex);
		
		double[] pt = result.getPoint();
		double alpha = pt[0];
		double beta = pt[1];
		
		System.out.println("estimated alpha = "+alpha+", beta = "+beta);
		
		return new BetaDistribution(alpha, beta);
	}
	
	private static double[] getSamples() {
		// prepare samples
		int sampleSize = 90;
		RandomGenerator rand = new JDKRandomGenerator();
		rand.setSeed(28312984214531L);
		double[] s = new double[sampleSize];
		BetaDistribution bdist = new BetaDistribution(rand, 3, 10, BetaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		for (int i = 0; i < s.length; i++) {
			s[i] = bdist.sample();
		}

		return s;
	}

	public static void main(String[] args) {

		double[] s = getSamples();

		System.out.println(s.length+" samples prepared.");
		
		// try fitting a distribution from samples.
		TestFitBetaDist t = new TestFitBetaDist(s);
		t.fit();
		
	}

}
