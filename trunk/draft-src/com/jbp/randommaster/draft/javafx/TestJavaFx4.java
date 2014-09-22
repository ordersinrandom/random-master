package com.jbp.randommaster.draft.javafx;


import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;
import com.jbp.randommaster.quant.sde.univariate.UnivariateStochasticProcess;
import com.jbp.randommaster.quant.sde.univariate.simulations.GBMPathGenerator;
import com.jbp.randommaster.quant.sde.univariate.simulations.PathsFactory;

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
		
		GBMPathGenerator gen=new GBMPathGenerator(gbm, 100, standardNormal);
		
		int seriesCount = 5;
		List<XYSeries> series=new LinkedList<>();
		for (int i=0;i<seriesCount;i++)
			series.add(new XYSeries("GBM"+i));

		PathsFactory<? extends UnivariateStochasticProcess> factory1 = new PathsFactory<>(gen);
		
		int simCount=252;
		double dt = 1.0/252.0;

		int step=0;
		for (int i=0;i<series.size();i++) {
			step=0;
			XYSeries currentSeries = series.get(i);
			for (double x : factory1.getNextSeries(dt, simCount, true)) {
				currentSeries.add((double) dt*(step++), x);
			}
		}
		

		XYSeriesCollection dataset=new XYSeriesCollection();
		for (XYSeries s : series)
			dataset.addSeries(s);
		
		JFreeChart chart=ChartFactory.createXYLineChart("Simulated Paths", "time", "value", dataset, PlotOrientation.VERTICAL, 
				true, true, false);
		
		chart.getPlot().setBackgroundPaint(new java.awt.GradientPaint(1, 1, java.awt.Color.yellow.darker().darker(), 
				1500, 1500, java.awt.Color.darkGray));
		
		return chart;
	}
}

