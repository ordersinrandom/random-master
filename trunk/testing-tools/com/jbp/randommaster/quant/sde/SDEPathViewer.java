package com.jbp.randommaster.quant.sde;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jbp.randommaster.quant.sde.univariate.BrownianBridge;
import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;
import com.jbp.randommaster.quant.sde.univariate.simulations.EulerDriftDiffusionPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.GBMPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.OUProcessPathGenerator;

public class SDEPathViewer {

	public static void main(String[] args) throws Exception {
		
		
		
		GeometricBrownianMotion gbm=new GeometricBrownianMotion(0.7, 0.55);
		
		long seed=19230123213L;
		GBMPathGenerator gen1=new GBMPathGenerator(gbm, 100, seed);
		
		Filtration<Double> initGbmFt=new Filtration<Double>();
		initGbmFt.setProcessValue(100.0);
		initGbmFt.setTime(0);
		EulerDriftDiffusionPathGenerator<GeometricBrownianMotion> gen2=new EulerDriftDiffusionPathGenerator<GeometricBrownianMotion>(gbm, initGbmFt, seed);

		
		OUProcess ou=new OUProcess(30, 100.0, 20.0);
		OUProcessPathGenerator gen3 = new OUProcessPathGenerator(ou, 110.0, seed);
		
		Filtration<Double> initOuFt=new Filtration<Double>();
		initOuFt.setProcessValue(110.0);
		initOuFt.setTime(0);
		EulerDriftDiffusionPathGenerator<OUProcess> gen4 = new EulerDriftDiffusionPathGenerator<OUProcess>(ou, initOuFt, seed);
		

			
		XYSeries series1=new XYSeries("GBM Exact");
		XYSeries series2=new XYSeries("GBM Euler");
		XYSeries series3=new XYSeries("OU Exact");
		XYSeries series4=new XYSeries("OU Euler");

		int simCount=252;
		double dt = 1.0/252.0;
		
		int step=0;
		for (double x : gen1.getNextSeries(dt, simCount, true)) {
			series1.add((double) dt*(step++), x);
		}
		
		step=0;
		for (double x : gen2.getNextSeries(dt, simCount, true)) {
			series2.add((double) dt*(step++), x);
		}
		
		step=0;
		for (double x : gen3.getNextSeries(dt, simCount, true)) {
			series3.add((double) dt*(step++), x);
		}
		
		step=0;
		for (double x : gen4.getNextSeries(dt, simCount, true)) {
			series4.add((double) dt*(step++), x);
		}
		
		XYSeriesCollection dataset=new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);
		
		JFreeChart chart=ChartFactory.createXYLineChart("Simulated Paths", "time", "value", dataset, PlotOrientation.VERTICAL, 
				true, true, false);
		
		chart.getPlot().setBackgroundPaint(new GradientPaint(1, 1, Color.yellow.darker().darker(), 
				1500, 1500, Color.darkGray));
		
		JFrame f=new JFrame("SDE Path Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new ChartPanel(chart));
		f.pack();
		f.setVisible(true);
		
	}
	
}
