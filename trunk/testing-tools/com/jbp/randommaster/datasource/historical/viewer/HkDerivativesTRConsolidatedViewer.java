package com.jbp.randommaster.datasource.historical.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;

import com.jbp.randommaster.datasource.historical.HistoricalDataSource;
import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRHDF5Source;
import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesConsolidatedData;
import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesTRConsolidator;
import com.jbp.randommaster.datasource.historical.consolidation.TimeConsolidatedTradeRecord;
import com.jbp.randommaster.datasource.historical.consolidation.TimeIntervalConsolidatedTRSource;
import com.jbp.randommaster.datasource.historical.filters.ExpiryMonthFilter;
import com.jbp.randommaster.datasource.historical.filters.FilteredHistoricalDataSource;
import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter;
import com.jbp.randommaster.datasource.historical.filters.HkDerivativesTRTradeTypeFilter.TradeType;
import com.jbp.randommaster.gui.common.date.calendar.JDateChooser;

public class HkDerivativesTRConsolidatedViewer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2211249807333564808L;
	
	
	private JDateChooser tradingDateChooser;
	private JComboBox<String> underlyingComboBox;
	private JComboBox<String> futuresOrOptionsComboBox;
	private JComboBox<String> expiryYearComboBox;
	private JComboBox<String> expiryMonthComboBox;
	private JTextField observationFrequencyField;

	private JTextField chosenFilePathField;
	
	private JButton loadAndPlotBut;
	
	private ChartPanel chartPanel;
	
	public HkDerivativesTRConsolidatedViewer() {
		super("HkDerivativesTR Consolidated Viewer");
		
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e1) {
			// ignore
		}
		
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		tradingDateChooser=new JDateChooser(false);
		bottomPanel.add(new JLabel("Trading Date:"));
		bottomPanel.add(tradingDateChooser);
		
		underlyingComboBox=new JComboBox<String>(new String[] { "HSI", "HHI", "MHI", "MCH" });
		bottomPanel.add(underlyingComboBox);
		
		futuresOrOptionsComboBox =new JComboBox<String>(new String[] { "Futures", "Options" });
		bottomPanel.add(futuresOrOptionsComboBox);

		expiryYearComboBox = new JComboBox<String>(new String[] { "2012", "2013", "2014" });
		expiryMonthComboBox = new JComboBox<String>(new String[] { "1","2","3","4","5","6","7","8","9","10","11","12" });
		bottomPanel.add(new JLabel("Expiry:"));
		bottomPanel.add(expiryYearComboBox);
		bottomPanel.add(expiryMonthComboBox);
		
		observationFrequencyField=new JTextField("180", 5); // 180 Seconds by default
		bottomPanel.add(new JLabel("Freq (Seconds):"));
		bottomPanel.add(observationFrequencyField);

		loadAndPlotBut=new JButton("Load & Plot");
		loadAndPlotBut.setActionCommand("LoadAndPlot");
		loadAndPlotBut.addActionListener(this);
		bottomPanel.add(loadAndPlotBut);
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton chooseFileBut=new JButton("Choose HDF5 File");
		chooseFileBut.setActionCommand("ChooseFile");
		chooseFileBut.addActionListener(this);
		topPanel.add(chooseFileBut);
		
		chosenFilePathField=new JTextField("", 50);
		chosenFilePathField.setEditable(false);
		topPanel.add(new JLabel("Target File:"));
		topPanel.add(chosenFilePathField);
		
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		if ("ChooseFile".equals(cmd)) {
			chooseHDF5File();
		}
		else if ("LoadAndPlot".equals(cmd)) {
			
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
									JOptionPane.showMessageDialog(HkDerivativesTRConsolidatedViewer.this, 
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
	
	
	private void chooseHDF5File() {

		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("HDF5 Files", "hdf5", "h5");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String chosenFile = chooser.getSelectedFile().getAbsolutePath();
			chosenFilePathField.setText(chosenFile);
		}

	}	

	private JFreeChart loadAndPlot() {
		
		String inputHDF5Filename = chosenFilePathField.getText();
		LocalDate tradingDate=LocalDate.fromDateFields(tradingDateChooser.getDate());
		String futuresOrOptions = futuresOrOptionsComboBox.getItemAt(futuresOrOptionsComboBox.getSelectedIndex());
		String underlying = underlyingComboBox.getItemAt(underlyingComboBox.getSelectedIndex());
		
		YearMonth expiryMonth = new YearMonth(
					Integer.valueOf(expiryYearComboBox.getItemAt(expiryYearComboBox.getSelectedIndex())).intValue(),
					Integer.valueOf(expiryMonthComboBox.getItemAt(expiryMonthComboBox.getSelectedIndex())).intValue()
				);
		
		int frequencySeconds = Integer.valueOf(observationFrequencyField.getText()).intValue();
		
		HistoricalDataSource<? extends TimeConsolidatedTradeRecord> consolidatedSrc = getDataSource(
				inputHDF5Filename, tradingDate, futuresOrOptions, underlying, expiryMonth, frequencySeconds);		
		
		String chartTitle = frequencySeconds+" Seconds consolidated "+underlying+" "+futuresOrOptions+" Ticks";
		
		// create the plot series
		//XYSeries plotSeries=new XYSeries(chartTitle);
		OHLCSeries plotSeries = new OHLCSeries(chartTitle);
		
		
		// iterate through the data and add to the plot.
		for (TimeConsolidatedTradeRecord data : consolidatedSrc.getData()) {
			//double t = data.getTimestamp().getHourOfDay()*100.0 + data.getTimestamp().getMinuteOfHour();
			//double val = data.getLastTradedPrice();
			//plotSeries.add(t, val);
			plotSeries.add(
					RegularTimePeriod.createInstance(Second.class, data.getTimestamp().toDate(), TimeZone.getDefault()),
					data.getFirstTradedPrice(),
					data.getMaxTradedPrice(),
					data.getMinTradedPrice(),
					data.getLastTradedPrice());

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
	private HistoricalDataSource<? extends TimeConsolidatedTradeRecord> getDataSource(
			String inputHDF5Filename, LocalDate tradingDate, String futuresOrOptions, String underlying, YearMonth expiryMonth, int frequencySeconds) {
		
		TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR> consolidatedSrc = null;
		
		try (
			// raw data source
			HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(
					inputHDF5Filename,tradingDate, futuresOrOptions, underlying);
			
			// filtered by expiry month
			FilteredHistoricalDataSource<HkDerivativesTR> expMonthFilteredSource = 
					new FilteredHistoricalDataSource<HkDerivativesTR>(
					originalSrc, new ExpiryMonthFilter<HkDerivativesTR>(expiryMonth));
			// filtered by trade type (Normal)
			FilteredHistoricalDataSource<HkDerivativesTR> filteredSource =
					new FilteredHistoricalDataSource<HkDerivativesTR>(
					expMonthFilteredSource, new HkDerivativesTRTradeTypeFilter(TradeType.Normal));
		) {
		
			HkDerivativesTRConsolidator consolidator = new HkDerivativesTRConsolidator();
			LocalDateTime start = new LocalDateTime(
					tradingDate.getYear(),tradingDate.getMonthOfYear(),tradingDate.getDayOfMonth(), 9, 30, 0);
			LocalDateTime end = new LocalDateTime(
					tradingDate.getYear(),tradingDate.getMonthOfYear(),tradingDate.getDayOfMonth(), 16, 15, 0);
	
			// we consolidated by number of seconds.
			Period interval = new Period(0, 0, frequencySeconds, 0);
			
			consolidatedSrc = new TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR>(
								consolidator, filteredSource, start, end, interval);
		}
		
		return consolidatedSrc;
	}
	
	

	/**
	 * Entry point.
	 */
	public static void main(String[] args) {
		HkDerivativesTRConsolidatedViewer v=new HkDerivativesTRConsolidatedViewer();
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.pack();
		v.setSize(1000,600);
		v.setVisible(true);

	}

}
