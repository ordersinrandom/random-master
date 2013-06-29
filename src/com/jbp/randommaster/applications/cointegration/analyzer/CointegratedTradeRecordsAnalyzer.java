package com.jbp.randommaster.applications.cointegration.analyzer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.joda.time.format.DateTimeFormat;

import com.jbp.randommaster.gui.common.date.calendar.JDateChooser;
import com.jbp.randommaster.gui.common.table.block.AbstractBlock;
import com.jbp.randommaster.gui.common.table.block.BlockSorter;
import com.jbp.randommaster.gui.common.table.block.BlockTableModel;
import com.jbp.randommaster.gui.common.table.block.DoubleSorter;
import com.jbp.randommaster.gui.common.table.block.StringSorter;
import com.jbp.randommaster.gui.common.table.util.SortHeaderUtil;

public class CointegratedTradeRecordsAnalyzer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 154266890136683222L;
	
	private JTextField chosenFileDisplayField;
	private JDateChooser tradingDateChooser;	
	
	private ChartPanel chartPanel;
	
	
	private JTable legsTable;
	private JPopupMenu popupMenu;	

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
		
		
		// create the table showing basket components
		BlockTableModel tableModel = new BlockTableModel();
		tableModel.setColumnNames(new String[] { "Weight", "Underlying", "Expiry", "Futures/Options" } , true);
		legsTable = new JTable(tableModel);
		
		// setup the table sorting
		Map<BlockSorter, int[]> sorterColumnsMap = new HashMap<BlockSorter, int[]>();
		sorterColumnsMap.put(new DoubleSorter(), new int[] { 0 }); // sorting weight
		sorterColumnsMap.put(new StringSorter(), new int[] { 1 }); // sorting string
		SortHeaderUtil.setupHeader(legsTable, tableModel, sorterColumnsMap, null);
		
		
		// prepare the popup menu on the legs table
		popupMenu = new JPopupMenu();
		JMenuItem addLegMenuItem = popupMenu.add(new JMenuItem("Add Leg"));
		addLegMenuItem.addActionListener(this);
		addLegMenuItem.setActionCommand("AddLeg");
		JMenuItem removeLegMenuItem = popupMenu.add(new JMenuItem("Remove Leg"));
		removeLegMenuItem.addActionListener(this);
		removeLegMenuItem.setActionCommand("RemoveLeg");
		
		PopupMenuInvoker popupInvoker = new PopupMenuInvoker();
		
		Component c=bottomPanel.add(new JScrollPane(legsTable));
		// register the mouse listener of the scroll pane and the table!
		c.addMouseListener(popupInvoker);
		legsTable.addMouseListener(popupInvoker);
		
		
		
		// vertical split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		splitPane.setDividerLocation(450);
		splitPane.setOneTouchExpandable(true);
		
		getContentPane().add(splitPane);

	}
	
	/**
	 * Helper class to bring up the popup menu of the legs table.
	 *
	 */
	private class PopupMenuInvoker extends MouseAdapter {
		public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
		public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) 
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("SelectFile".equals(e.getActionCommand())) {
			selectFile();
		}
		else if ("AddLeg".equals(e.getActionCommand())) {
			AddLegDialog d = new AddLegDialog(this);
			if (d.isValidInput()) {
				Leg newLeg = d.getNewLeg();
				LegTableBlock b=new LegTableBlock(newLeg);
				((BlockTableModel) legsTable.getModel()).addBlock(b);
			}
			
		}
		else if ("RemoveLeg".equals(e.getActionCommand())) {
			int r = legsTable.getSelectedRow();
			LegTableBlock b=(LegTableBlock) ((BlockTableModel) legsTable.getModel()).findBlock(r);
			if (b!=null) 
				((BlockTableModel) legsTable.getModel()).removeBlock(b);
		}
		else if ("LoadAndPlot".equals(e.getActionCommand())) {
			
		}
	}
	
	/**
	 * Get all legs in the table.
	 */
	private Iterable<Leg> getAllLegs() {
		List<Leg> result=new LinkedList<Leg>();
		BlockTableModel tableModel = (BlockTableModel) legsTable.getModel();
		for (@SuppressWarnings("rawtypes") Iterator it=tableModel.getBlocks();it.hasNext();) { 
			LegTableBlock b = (LegTableBlock) it.next();
			result.add(b.getLeg());
		}
		return result;
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
	 * The table item representing one leg.
	 */
	private static class LegTableBlock extends AbstractBlock {
		
		private Leg leg;
		
		public LegTableBlock(Leg l) {
			leg=l;
		}
		
		public Leg getLeg() {
			return leg;
		}

		@Override
		public int getWidth() {
			return 4;
		}

		@Override
		public int getHeight() {
			return 1;
		}

		@Override
		public Object getValueAt(int x, int y) {
			if (y==0) {
				switch (x) {
				case 0:
					return leg.getWeight();
				case 1:
					return leg.getUnderlying();
				case 2:
					return leg.getExpiry().toString(DateTimeFormat.forPattern("yyyy-MM"));
				case 3:
					return leg.getFuturesOrOptions();
				default:
					return "";
				}
			}
			else return "";
		}
		
		/**
		 * Set the value in this block. Empty default implementation.
		 */
		public void setValueAt(Object v, int x, int y) {
			// empty implementation
		}

		/**
		 * Check if the block data is editable at (x,y) relative to block upper
		 * left corner.
		 * 
		 * @return false by default, subclass to override.
		 */
		public boolean isEditableAt(int x, int y) {
			return false;
		}

		/**
		 * Get the cell editor at (x,y) relative to block upper left corner.
		 * 
		 * @return Default implementation returns null.
		 */
		public TableCellEditor getCellEditorAt(int x, int y) {
			return null;
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
