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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;

import com.jbp.randommaster.datasource.historical.ExpiryMonthFilter;
import com.jbp.randommaster.datasource.historical.FilteredHistoricalDataSource;
import com.jbp.randommaster.datasource.historical.HkDerivativesConsolidatedData;
import com.jbp.randommaster.datasource.historical.HkDerivativesTR;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRConsolidator;
import com.jbp.randommaster.datasource.historical.HkDerivativesTRHDF5Source;
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
	
	private ChartPanel chartPanel;
	
	public HkDerivativesTRConsolidatedViewer() {
		super("HkDerivativesTR Consolidated Viewer");
		
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e1) {
			// ignore
		}
		
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton chooseFileBut=new JButton("Choose HDF5 File");
		chooseFileBut.setActionCommand("ChooseFile");
		chooseFileBut.addActionListener(this);
		bottomPanel.add(chooseFileBut);
		
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
		
		observationFrequencyField=new JTextField("5", 3); // 5 min by default
		bottomPanel.add(new JLabel("Freq (Min):"));
		bottomPanel.add(observationFrequencyField);

		JButton loadAndPlotBut=new JButton("Load & Plot");
		loadAndPlotBut.setActionCommand("LoadAndPlot");
		loadAndPlotBut.addActionListener(this);
		bottomPanel.add(loadAndPlotBut);
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
			chooseHdf5File();
		}
		else if ("LoadAndPlot".equals(cmd)) {
			loadAndPlot();
		}
		
	}
	
	
	private void chooseHdf5File() {

		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("HDF5 Files", "hdf5", "h5");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String chosenFile = chooser.getSelectedFile().getAbsolutePath();
			chosenFilePathField.setText(chosenFile);
		}

	}	

	private void loadAndPlot() {
		
		/*
		 * 		HkDerivativesTRHDF5Source src=new HkDerivativesTRHDF5Source(
				inputHDF5Filename,
				new LocalDate(2012,10,9), 
				"Futures", 
				"HSI");
		 */
		
		String inputHDF5Filename = chosenFilePathField.getText();
		LocalDate tradingDate=LocalDate.fromDateFields(tradingDateChooser.getDate());
		String futuresOrOptions = futuresOrOptionsComboBox.getItemAt(futuresOrOptionsComboBox.getSelectedIndex());
		String underlying = underlyingComboBox.getItemAt(underlyingComboBox.getSelectedIndex());
		
		YearMonth expiryMonth = new YearMonth(
					Integer.valueOf(expiryYearComboBox.getItemAt(expiryYearComboBox.getSelectedIndex())).intValue(),
					Integer.valueOf(expiryMonthComboBox.getItemAt(expiryMonthComboBox.getSelectedIndex())).intValue()
				);
		
		int frequencyMin = Integer.valueOf(observationFrequencyField.getText()).intValue();
		
		
		ExpiryMonthFilter<HkDerivativesTR> filter = new ExpiryMonthFilter<HkDerivativesTR>(expiryMonth);
		
		HkDerivativesTRHDF5Source originalSrc=new HkDerivativesTRHDF5Source(
				inputHDF5Filename,tradingDate, futuresOrOptions, underlying);
		
		// filtered by expiry month
		FilteredHistoricalDataSource<HkDerivativesTR> filteredSource = new FilteredHistoricalDataSource<HkDerivativesTR>(
				originalSrc, filter);
		
		
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		LocalDateTime start = new LocalDateTime(
				tradingDate.getYear(),tradingDate.getMonthOfYear(),tradingDate.getDayOfMonth(), 9, 30, 0);
		LocalDateTime end = new LocalDateTime(
				tradingDate.getYear(),tradingDate.getMonthOfYear(),tradingDate.getDayOfMonth(), 16, 15, 0);

		Period interval = new Period(0, frequencyMin, 0, 0);
		Iterable<HkDerivativesConsolidatedData> consolidatedResult=con.consolidateByTimeIntervals(
				start, end, interval, filteredSource.getData());		

		
		String chartTitle = frequencyMin+" min consolidated "+underlying+" "+futuresOrOptions+" Ticks";
		//XYSeries plotSeries=new XYSeries(chartTitle);
		OHLCSeries plotSeries = new OHLCSeries(chartTitle);

		for (HkDerivativesConsolidatedData data : consolidatedResult) {
			//double t = data.getTimestamp().getHourOfDay()*100.0 + data.getTimestamp().getMinuteOfHour();
			//double val = data.getLastTradedPrice();
			//plotSeries.add(t, val);
			plotSeries.add(
					RegularTimePeriod.createInstance(Minute.class, data.getTimestamp().toDate(), TimeZone.getDefault()),
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
		
		//JFreeChart chart = ChartFactory.createHighLowChart(chartTitle, "Time", "Price", dataset, true);
		JFreeChart chart = ChartFactory.createCandlestickChart(chartTitle, "Time", "Traded Price", dataset, true);
		
		chart.getPlot().setBackgroundPaint(new GradientPaint(1, 1, Color.yellow.darker().darker(), 
				1500, 1500, Color.darkGray));
		
		XYPlot plot = (XYPlot) chart.getPlot();
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		
		
		if (chartPanel==null) {
			chartPanel = new ChartPanel(chart);
			getContentPane().add(chartPanel, BorderLayout.CENTER);
			this.revalidate();
		}
		else {
			chartPanel.setChart(chart);
		}
		
		
	}
	

	/**
	 * Entry point.
	 */
	public static void main(String[] args) {
		HkDerivativesTRConsolidatedViewer v=new HkDerivativesTRConsolidatedViewer();
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.pack();
		v.setVisible(true);

	}

}