package com.jbp.randommaster.draft.javafx;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jbp.randommaster.quant.sde.Filtration;
import com.jbp.randommaster.quant.sde.univariate.GeometricBrownianMotion;
import com.jbp.randommaster.quant.sde.univariate.simulations.GBMPathGenerator;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestJavaFx4 extends Application {

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		TabPane tabPane = new TabPane();
		
		Tab jfreeChartTab = new Tab("JFreeChart");
		jfreeChartTab.setContent(getJFreeChartContent());
		jfreeChartTab.setClosable(false);
		
		Tab javaFXChartTab = new Tab("JavaFXChart");
		javaFXChartTab.setContent(getJavaFXChartContent());
		javaFXChartTab.setClosable(false);
		
		tabPane.getTabs().add(jfreeChartTab);
		tabPane.getTabs().add(javaFXChartTab);

		Scene scene = new Scene(tabPane);
		primaryStage.setScene(scene);
		primaryStage.setWidth(800);
		primaryStage.setHeight(700);
		primaryStage.show();
	}
	
	private Node getJavaFXChartContent() {
		
        
		BorderPane javaFXChartPanel = new BorderPane();
		javaFXChartPanel.setCenter(prepareJavaFXChart());
		
		
		Button regeneratePathsBut = new Button("Regenerate Paths");

		regeneratePathsBut.setOnAction(ev -> {
			javaFXChartPanel.setCenter(prepareJavaFXChart());
		});
		
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(4, 4, 4, 4));
		bottomBox.setSpacing(4);
		bottomBox.getChildren().addAll(regeneratePathsBut);
		
		javaFXChartPanel.setTop(bottomBox);
		
        
        return javaFXChartPanel;
	}
	

	private LineChart<Number,Number> prepareJavaFXChart() {
		long seed = new java.util.Date().getTime();
		NormalDistribution standardNormal = new NormalDistribution(0, 1);
		standardNormal.reseedRandomGenerator(seed);
		double dt = 1.0 / 252.0;
		double maxT = 1.5; // run for 1.5 years
		
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Value");
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        //lineChart.setStyle(".chart-series-line { -fx-stroke-width: 1px; -fx-effect: null; }");

        lineChart.setTitle("Simulated Paths");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        
        
        int seriesCount = 10;
        for (int i=0;i<seriesCount;i++) {

            //defining a series
            XYChart.Series<Number,Number> series = new XYChart.Series<>();
            
            
            series.setName("GBM "+(i+1));
            
			Stream<Filtration<Double>> stream = generateSeries(standardNormal, dt, maxT);

			for (Iterator<Filtration<Double>> it = stream.iterator(); it.hasNext();) {
				Filtration<Double> ft = it.next();
				series.getData().add(new XYChart.Data<Number,Number>(ft.getTime(), ft.getProcessValue()));
			}            
            
            lineChart.getData().add(series);	        	

        }		
        
        //lineChart.setStyle("-fx-font-family: sample; -fx-font-size: 80;");
        
        return lineChart;
	}
	
	
	
	private Node getJFreeChartContent() {

		BorderPane jfreeChartPanel = new BorderPane();

		Button regeneratePathsBut = new Button("Regenerate Paths");
		
		HBox bottomBox = new HBox();
		
		bottomBox.setPadding(new Insets(4, 4, 4, 4));
		bottomBox.setSpacing(4);
		bottomBox.getChildren().addAll(regeneratePathsBut);
		
		jfreeChartPanel.setBottom(bottomBox);		
		
		
		SwingNode swingContent = new SwingNode();
		ChartPanel cp = new ChartPanel(prepareJFreeChart());
		swingContent.setContent(cp);
		jfreeChartPanel.setCenter(swingContent);
		
		
		
		//ChartViewer cv = new ChartViewer(prepareJFreeChart());
		//jfreeChartPanel.setCenter(cv);
		

		regeneratePathsBut.setOnAction(ev -> {
			
			ChartPanel cp2 = new ChartPanel(prepareJFreeChart());
			swingContent.setContent(cp2);
			
			//cv.setChart(prepareJFreeChart());
			
		});
		
		
		return jfreeChartPanel;
	}
	
	private JFreeChart prepareJFreeChart() {

		long seed = new java.util.Date().getTime();
		NormalDistribution standardNormal = new NormalDistribution(0, 1);
		standardNormal.reseedRandomGenerator(seed);

		int seriesCount = 10;
		List<XYSeries> series = new LinkedList<>();
		for (int i = 0; i < seriesCount; i++) {
			series.add(new XYSeries("GBM" + i));
		}

		double dt = 1.0 / 252.0;
		double maxT = 1.5; // run for 1.5 years
		for (int i = 0; i < series.size(); i++) {
			XYSeries currentSeries = series.get(i);

			Stream<Filtration<Double>> stream = generateSeries(standardNormal, dt, maxT);

			for (Iterator<Filtration<Double>> it = stream.iterator(); it.hasNext();) {
				Filtration<Double> ft = it.next();
				currentSeries.add(ft.getTime(), ft.getProcessValue());
			}
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries s : series)
			dataset.addSeries(s);

		JFreeChart chart = ChartFactory.createXYLineChart("Simulated Paths", "time", "value", dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.getPlot().setBackgroundPaint(
				new java.awt.GradientPaint(1, 1, java.awt.Color.yellow.darker().darker(), 1500, 1500, java.awt.Color.darkGray));

		return chart;
	}

	private Stream<Filtration<Double>> generateSeries(NormalDistribution standardNormal, double dt, double maxT) {

		GeometricBrownianMotion gbm = new GeometricBrownianMotion(0.1, 0.25);
		return new GBMPathGenerator(gbm, 100, standardNormal).streamUpToTime(dt, maxT);
	}

}
