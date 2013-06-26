package com.jbp.randommaster.applications.cointegration.analyzer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

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

		// top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		northPanel.add(new JLabel("Source File:"));
		
		chosenFileDisplayField = new JTextField("", 40);
		chosenFileDisplayField.setEditable(false);
		northPanel.add(chosenFileDisplayField);
		
		JButton selectFileButton = new JButton("Select");
		selectFileButton.setActionCommand("SelectFile");
		selectFileButton.addActionListener(this);
		northPanel.add(selectFileButton);
		
		tradingDateChooser = new JDateChooser(false);
		tradingDateChooser.setDateFormatString("yyyy-MM-dd");
		tradingDateChooser.setDate(new java.util.Date());
		northPanel.add(new JLabel("Trading Date:"));
		northPanel.add(tradingDateChooser);
		
		JButton loadAndPlotButton = new JButton("Load & Plot");
		loadAndPlotButton.setActionCommand("LoadAndPlot");
		loadAndPlotButton.addActionListener(this);
		northPanel.add(loadAndPlotButton);
		
		// create a dummy chart plotting area
		
		JFreeChart dummyChart = ChartFactory.createHighLowChart(
				"Intraday Plot", "Time", "Value", new OHLCSeriesCollection(), false);
		chartPanel = new ChartPanel(dummyChart);
		
		topPanel.add(northPanel, BorderLayout.NORTH);
		topPanel.add(chartPanel, BorderLayout.CENTER);
		
		
		// panel below the split pane.
		JPanel bottomPanel = new JPanel(new BorderLayout());
		
		BlockTableModel tableModel = new BlockTableModel();
		tableModel.setColumnNames(new String[] { "Weight", "Underlying", "Expiry", "Futures/Options" } , true);
		
		// create the table showing basket components
		JTable componentsTable = new JTable(tableModel);
		
		// prepare the popup menu on the components table
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addComponentItem = popupMenu.add(new JMenuItem("Add New Component"));
		addComponentItem.addActionListener(this);
		addComponentItem.setActionCommand("AddNewComponent");
		JMenuItem removeComponentItem = popupMenu.add(new JMenuItem("Remove Component"));
		removeComponentItem.addActionListener(this);
		removeComponentItem.setActionCommand("RemoveComponent");
		
		Component c=bottomPanel.add(new JScrollPane(componentsTable));
		// register the mouse listener of the scroll pane instead of the table.
		c.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
			public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) 
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		
		
		// vertical split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		splitPane.setDividerLocation(450);
		splitPane.setOneTouchExpandable(true);
		
		getContentPane().add(splitPane);

	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("SelectFile".equals(e.getActionCommand())) {
			selectFile();
		}
		else if ("AddNewComponent".equals(e.getActionCommand())) {
			
		}
		else if ("RemoveComponent".equals(e.getActionCommand())) {
			
		}
		else if ("LoadAndPlot".equals(e.getActionCommand())) {
			
		}
	}
	
	
	/**
	 * Pop up a file dialog and select a HDF5 source file. Set the result filename to "chsenFileDisplayField".
	 */
	private void selectFile() {
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("HDF5 Files", "hdf5", "h5");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String chosenFile = chooser.getSelectedFile().getAbsolutePath();
			chosenFileDisplayField.setText(chosenFile);
		}		
	}
	
	
	
	
	/**
	 * Entry point
	 */
	public static void main(String[] args) {

		// set the L&F
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
		} catch (Exception e1) {
			// ignore
		}			
		
		CointegratedTradeRecordsAnalyzer v = new CointegratedTradeRecordsAnalyzer();
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.pack();
		v.setSize(1200,700);
		v.setVisible(true);		
	}



}
