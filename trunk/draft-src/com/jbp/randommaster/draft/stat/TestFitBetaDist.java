package com.jbp.randommaster.draft.stat;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;

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

		MultivariateFunction unboundedFunc = new MultivariateFunctionPenaltyAdapter(likelihoodFunc, new double[] { 0.00000001, 0.00000001 },
				new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }, -100000000000.0, new double[] { 1.0, 1.0 });

		ObjectiveFunction objectiveFunc = new ObjectiveFunction(unboundedFunc);

		// simplex
		NelderMeadSimplex simplex = new NelderMeadSimplex(startPt.getInitialGuess().length);

		SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(-1.0, Double.MIN_VALUE));

		PointValuePair result = optimizer.optimize(new MaxEval(20000), GoalType.MAXIMIZE, startPt, objectiveFunc, simplex);

		double[] pt = result.getPoint();
		double alpha = pt[0];
		double beta = pt[1];

		System.out.println("estimated alpha = " + alpha + ", beta = " + beta);

		return new BetaDistribution(alpha, beta);
	}

	private static double[] getSamples() {
		// prepare samples
		int sampleSize = 50;
		RandomGenerator rand = new JDKRandomGenerator();
		//rand.setSeed(28312984214531L);
		double[] s = new double[sampleSize];
		BetaDistribution bdist = new BetaDistribution(rand, 3, 10, BetaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		for (int i = 0; i < s.length; i++) {
			s[i] = bdist.sample();
		}

		return s;
	}

	private static JFreeChart prepareChart(double[] samples, BetaDistribution fittedDist) {
		HistogramDataset ds = new HistogramDataset();
		int binsCount = 50;
		ds.addSeries("Samples", samples, binsCount, 0.0, 1.0);

		DecimalFormat fmt = new DecimalFormat("0.##");
		String title = "Fitted Beta("+fmt.format(fittedDist.getAlpha())+", "+fmt.format(fittedDist.getBeta())+")";
		
		JFreeChart chart = ChartFactory.createHistogram(title, "Bins", "Frequency", ds, PlotOrientation.VERTICAL, true, true, false);

		XYPlot localXYPlot = (XYPlot) chart.getPlot();
		localXYPlot.setDomainPannable(true);
		localXYPlot.setRangePannable(true);
		localXYPlot.setForegroundAlpha(0.55f);
		NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		XYBarRenderer localXYBarRenderer = (XYBarRenderer) localXYPlot.getRenderer();
		localXYBarRenderer.setDrawBarOutline(false);
		localXYBarRenderer.setBarPainter(new StandardXYBarPainter());
		localXYBarRenderer.setShadowVisible(false);
		
		XYDataset distDS = DatasetUtilities.sampleFunction2D(x -> fittedDist.density(x) , 0.0, 1.0, 500, "BetaDist PDF");

		localXYPlot.setDataset(1, distDS);
		localXYPlot.setRangeAxis(1, new NumberAxis("Beta Dist PDF(x)"));
		localXYPlot.mapDatasetToRangeAxis(1,1);
		XYLineAndShapeRenderer funcRenderer = new XYLineAndShapeRenderer();
		localXYPlot.setRenderer(1, funcRenderer);
		funcRenderer.setSeriesShapesVisible(0, false);
		funcRenderer.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] {10.0f, 6.0f}, 0.0f));
		
		return chart;
	}
	
	public static void main(String[] args) {

		double[] s = getSamples();

		System.out.println(s.length + " samples prepared.");

		// try fitting a distribution from samples.
		TestFitBetaDist t = new TestFitBetaDist(s);
		BetaDistribution dist = t.fit();

		JFreeChart chart = prepareChart(s, dist);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMouseWheelEnabled(true);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(chartPanel, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton rerunBut = new JButton("Regenerate and Fit");
		bottom.add(rerunBut);
		mainPanel.add(bottom, BorderLayout.SOUTH);
		rerunBut.addActionListener(
				ev -> {
					double[] s2 = getSamples();

					System.out.println(s.length + " samples prepared.");

					// try fitting a distribution from samples.
					TestFitBetaDist t2 = new TestFitBetaDist(s2);
					BetaDistribution dist2 = t2.fit();

					JFreeChart chart2 = prepareChart(s2, dist2);
					chartPanel.setChart(chart2);
				});
		
		
		JFrame frame = new JFrame("Test Fit");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.setSize(1000,600);
		frame.setVisible(true);
	}

}
