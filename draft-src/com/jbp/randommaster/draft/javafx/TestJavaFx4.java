package com.jbp.randommaster.draft.javafx;


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
import com.jbp.randommaster.quant.sde.univariate.simulations.EulerDriftDiffusionPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.GBMPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.OUProcessPathGenerator;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestJavaFx4 extends Application {

	
	
	public static void main(String[] args) {

		launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		SwingNode swingContent=new SwingNode();
		swingContent.setContent(new ChartPanel(prepareChart(new java.util.Date().getTime())));

		Button regenerateBut = new Button("Regenerate Paths");
		//regenerateBut.setEffect(getEffect());
		
		regenerateBut.setOnAction(ev -> {
			swingContent.setContent(new ChartPanel(prepareChart(new java.util.Date().getTime())));
		});
		
		
		HBox bottomBox=new HBox();
		bottomBox.setPadding(new Insets(4,4,4,4));
		bottomBox.setSpacing(4);
		bottomBox.getChildren().add(regenerateBut);
		
	
		BorderPane panel=new BorderPane();
		panel.setCenter(swingContent);
		panel.setBottom(bottomBox);
		
		Scene scene=new Scene(panel);
		primaryStage.setScene(scene);
		primaryStage.setWidth(800);
		primaryStage.setHeight(700);
		primaryStage.show();
	}

	
	private JFreeChart prepareChart(long seed) {
		NormalDistribution standardNormal = new NormalDistribution(0,1);
		standardNormal.reseedRandomGenerator(seed);
		
		GeometricBrownianMotion gbm=new GeometricBrownianMotion(0.7, 0.55);
		
		GBMPathGenerator gen1=new GBMPathGenerator(gbm, 100, standardNormal);
		
		
		Filtration<Double> initGbmFt=new Filtration<Double>();
		initGbmFt.setProcessValue(100.0);
		initGbmFt.setTime(0);
		EulerDriftDiffusionPathGenerator<GeometricBrownianMotion> gen2=new EulerDriftDiffusionPathGenerator<GeometricBrownianMotion>(gbm, initGbmFt, standardNormal);

		
		OUProcess ou=new OUProcess(30, 100.0, 20.0);
		OUProcessPathGenerator gen3 = new OUProcessPathGenerator(ou, 110.0, standardNormal);
		
		Filtration<Double> initOuFt=new Filtration<Double>();
		initOuFt.setProcessValue(110.0);
		initOuFt.setTime(0);
		EulerDriftDiffusionPathGenerator<OUProcess> gen4 = new EulerDriftDiffusionPathGenerator<OUProcess>(ou, initOuFt, standardNormal);
		

			
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
		
		chart.getPlot().setBackgroundPaint(new java.awt.GradientPaint(1, 1, java.awt.Color.yellow.darker().darker(), 
				1500, 1500, java.awt.Color.darkGray));
		
		return chart;
	}
}

