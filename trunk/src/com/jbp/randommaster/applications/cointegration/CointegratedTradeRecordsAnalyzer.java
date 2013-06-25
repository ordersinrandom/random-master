package com.jbp.randommaster.applications.cointegration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import com.jbp.randommaster.gui.common.date.calendar.JDateChooser;
import com.jbp.randommaster.gui.common.table.block.BlockTableModel;

public class CointegratedTradeRecordsAnalyzer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 154266890136683222L;
	
	private JTextField chosenFileDisplayField;
	private JDateChooser tradingDateChooser;	
	
	private ChartPanel chartPanel;

	public CointegratedTradeRecordsAnalyzer() {
		super("Cointegrated Trade Records Analyzer");

		// set the L&F
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e1) {
			// ignore
		}		

		// top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		northPanel.add(new JLabel("Source File:"));
		
		chosenFileDisplayField = new JTextField("", 40);
		chosenFileDisplayField.setEnabled(false);
		northPanel.add(chosenFileDisplayField);
		
		JButton selectFileButton = new JButton("Select");
		selectFileButton.setActionCommand("SelectFile");
		selectFileButton.addActionListener(this);
		northPanel.add(selectFileButton);
		
		tradingDateChooser = new JDateChooser(false);
		northPanel.add(new JLabel("Trading Date:"));
		northPanel.add(tradingDateChooser);
		
		// create a dummy chart plotting area
		
		JFreeChart dummyChart = ChartFactory.createHighLowChart(
				"Intraday Plot", "Time", "Value", new OHLCSeriesCollection(), false);
		chartPanel = new ChartPanel(dummyChart);
		
		topPanel.add(northPanel, BorderLayout.NORTH);
		topPanel.add(chartPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		
		BlockTableModel tableModel = new BlockTableModel();
		tableModel.setColumnNames(new String[] { "Weight", "Instrument" } , true);
		
		JTable componentsTable = new JTable(tableModel);
		bottomPanel.add(new JScrollPane(componentsTable));
		
		// vertical split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		splitPane.setDividerLocation(450);
		
		getContentPane().add(splitPane);
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	/**
	 * Entry point
	 */
	public static void main(String[] args) {
		CointegratedTradeRecordsAnalyzer v = new CointegratedTradeRecordsAnalyzer();
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.pack();
		v.setSize(1000,600);
		v.setVisible(true);		
	}



}
