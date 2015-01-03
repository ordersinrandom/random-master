package com.jbp.randommaster.datasource.historical.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.configuration.ConfigurationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import com.jbp.randommaster.database.MasterDatabaseConnections;
import com.jbp.randommaster.datasource.historical.MasterDatabasePriceSource;
import com.jbp.randommaster.datasource.historical.consolidation.TimeConsolidatedTradeRecord;
import com.jbp.randommaster.gui.common.date.calendar.JDateChooser;

public class HkDerivativesMasterDBPriceViewer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2211242807133564808L;
	
	
	private JDateChooser startDateChooser;
	private JDateChooser endDateChooser;
	
	private JComboBox<String> instrumentCodeComboBox;

	private JButton loadAndPlotBut;
	
	private ChartPanel chartPanel;
	
	private MasterDatabaseConnections conn;
	
	public HkDerivativesMasterDBPriceViewer(MasterDatabaseConnections conn) {
		super("HkDerivativesMasterDBPriceViewer");
		
		this.conn=conn;
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e1) {
			// ignore
		}
		
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		startDateChooser=new JDateChooser(false);
		bottomPanel.add(new JLabel("Start:"));
		bottomPanel.add(startDateChooser);
		bottomPanel.add(new JLabel("End:"));
		endDateChooser = new JDateChooser(false);
		bottomPanel.add(endDateChooser);
		
		instrumentCodeComboBox=new JComboBox<String>(new String[] { "HSIc0", "HHIc0", "MHIc0", "MCHc0" });
		bottomPanel.add(instrumentCodeComboBox);
		
		loadAndPlotBut=new JButton("Load & Plot");
		loadAndPlotBut.setActionCommand("LoadAndPlot");
		loadAndPlotBut.addActionListener(this);
		bottomPanel.add(loadAndPlotBut);
		
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		if ("LoadAndPlot".equals(cmd)) {
			
			// disable the button
			loadAndPlotBut.setEnabled(false);
			
			Thread t=new Thread(new Runnable() {
				public void run() {

					try {
						// create the chart object
						final JFreeChart chart=loadAndPlot();
						
						// update the chart panel
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if (chartPanel==null) {
									chartPanel = new ChartPanel(chart);
									getContentPane().add(chartPanel, BorderLayout.CENTER);
									revalidate();
								}
								else {
									chartPanel.setChart(chart);
								}
								
							}
						});
					} catch (final Exception e1) {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									JOptionPane.showMessageDialog(HkDerivativesMasterDBPriceViewer.this, 
											"Unable to plot:\n"+e1.getMessage(), 
											"Error", JOptionPane.ERROR_MESSAGE);
								}
							});
						} catch (Exception e2) {
							// ignore
						}
					} finally {
						// enable the button.
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								loadAndPlotBut.setEnabled(true);
							}
						});
					}
					
				}
			});
			
			t.setDaemon(true);
			t.start();
			
		}
		
	}
	
	
	private JFreeChart loadAndPlot() {
		
		LocalDate startDate=LocalDateTime.ofInstant(startDateChooser.getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate=LocalDateTime.ofInstant(endDateChooser.getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
		String underlying = instrumentCodeComboBox.getItemAt(instrumentCodeComboBox.getSelectedIndex());
		
		String chartTitle = "Master DB "+underlying+" Current Month Price Records";
		
		// create the plot series
		//XYSeries plotSeries=new XYSeries(chartTitle);
		OHLCSeries plotSeries = new OHLCSeries(chartTitle);
		
		
		try (MasterDatabasePriceSource consolidatedSrc = getDataSource(underlying, startDate, endDate);) {

			// iterate through the data and add to the plot.
			for (TimeConsolidatedTradeRecord data : consolidatedSrc.getData()) {
				// double t = data.getTimestamp().getHourOfDay()*100.0 +
				// data.getTimestamp().getMinuteOfHour();
				// double val = data.getLastTradedPrice();
				// plotSeries.add(t, val);
				plotSeries.add(RegularTimePeriod.createInstance(Second.class, Date.from(data.getTimestamp().atZone(ZoneId.systemDefault()).toInstant()), 
						TimeZone.getDefault()),
						data.getFirstTradedPrice(), data.getMaxTradedPrice(), data.getMinTradedPrice(), data.getLastTradedPrice());

			}

		}
		
		//XYSeriesCollection dataset=new XYSeriesCollection();
		OHLCSeriesCollection dataset = new OHLCSeriesCollection();
		dataset.addSeries(plotSeries);
		
		/*
		JFreeChart chart=ChartFactory.createTimeSeriesChart(chartTitle, 
				"Time", "Last Traded Price", dataset,  
				true, true, false);
		*/
		
		JFreeChart chart = ChartFactory.createHighLowChart(chartTitle, "Time", "Traded Price", dataset, true);
		//JFreeChart chart = ChartFactory.createCandlestickChart(chartTitle, "Time", "Traded Price", dataset, true);
		
		chart.getPlot().setBackgroundPaint(new GradientPaint(1, 1, new Color(255,0,255).darker().darker().darker(), 
				1500, 1500, Color.darkGray));
		
		XYPlot plot = (XYPlot) chart.getPlot();
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		
		/*
		if (chartPanel==null) {
			chartPanel = new ChartPanel(chart);
			getContentPane().add(chartPanel, BorderLayout.CENTER);
			this.revalidate();
		}
		else {
			chartPanel.setChart(chart);
		}*/
		
		return chart;
	}
	
	/**
	 * Helper function to create the consolidated trade records data source object
	 */
	private MasterDatabasePriceSource getDataSource(String instrumentCode, LocalDate startDate, LocalDate endDate) {

		LocalDateTime startDateTime = startDate.atTime(0,0);
		LocalDateTime endDateTime = endDate.atTime(23,59);
		
		return new MasterDatabasePriceSource(conn, instrumentCode, startDateTime, endDateTime);
	}
	
	

	/**
	 * Entry point.
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, ConfigurationException {
		
		String dbConfPath = "./conf/jdbc.xml";
		if (args.length>=1)
			dbConfPath = args[0];
		MasterDatabaseConnections conn = new MasterDatabaseConnections(dbConfPath);
		
		HkDerivativesMasterDBPriceViewer v=new HkDerivativesMasterDBPriceViewer(conn);
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.pack();
		v.setSize(1000,600);
		v.setVisible(true);

	}

}
