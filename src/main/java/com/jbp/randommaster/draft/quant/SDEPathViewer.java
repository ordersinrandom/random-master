package com.jbp.randommaster.draft.quant;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;
import com.jbp.randommaster.quant.sde.univariate.simulations.EulerDriftDiffusionPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.GBMPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.OUProcessPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.PathGenerator;

public class SDEPathViewer {

	private static void fillSeries(XYSeries series, PathGenerator<? extends UnivariateStochasticProcess> generator) {
		double dt = 1.0 / 252.0;
		double maxT = 1.0;
		generator.streamUpToTime(dt, maxT).forEach(ft -> series.add(ft.getTime(), ft.getProcessValue()));
	}

	public static void main(String[] args) throws Exception {

		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		NormalDistribution standardNormal = new NormalDistribution(0, 1);
		standardNormal.reseedRandomGenerator(19230123213L);

		GeometricBrownianMotion gbm = new GeometricBrownianMotion(0.4, 0.25);

		GBMPathGenerator gen1 = new GBMPathGenerator(gbm, 100, standardNormal);

		Filtration<Double> initGbmFt = new Filtration<>(100.0, 0.0);
		EulerDriftDiffusionPathGenerator<GeometricBrownianMotion> gen2 = new EulerDriftDiffusionPathGenerator<>(gbm, initGbmFt, standardNormal);

		OUProcess ou = new OUProcess(30, 100.0, 28.0);
		OUProcessPathGenerator gen3 = new OUProcessPathGenerator(ou, 110.0, standardNormal);

		Filtration<Double> initOuFt = new Filtration<>(110.0, 0.0);
		EulerDriftDiffusionPathGenerator<OUProcess> gen4 = new EulerDriftDiffusionPathGenerator<>(ou, initOuFt, standardNormal);

		XYSeries series1 = new XYSeries("GBM Exact");
		XYSeries series2 = new XYSeries("GBM Euler");
		XYSeries series3 = new XYSeries("OU Exact");
		XYSeries series4 = new XYSeries("OU Euler");

		fillSeries(series1, gen1);
		fillSeries(series2, gen2);
		fillSeries(series3, gen3);
		fillSeries(series4, gen4);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);

		JFreeChart chart = ChartFactory.createXYLineChart("Simulated Paths", "time", "value", dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.getPlot().setBackgroundPaint(new GradientPaint(1, 1, Color.yellow.darker().darker(), 1500, 1500, Color.darkGray));

		JFrame f = new JFrame("SDE Path Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new ChartPanel(chart));
		f.pack();
		f.setVisible(true);

	}

}
