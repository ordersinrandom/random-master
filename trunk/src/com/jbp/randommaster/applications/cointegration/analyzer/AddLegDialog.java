package com.jbp.randommaster.applications.cointegration.analyzer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;


public class AddLegDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5640609351409344846L;
	
	private JTextField weightField;
	private JComboBox<String> underlyingBox;
	private JComboBox<String> yearBox, monthBox;
	private JComboBox<String> futuresOptionsBox;
	
	private boolean validInput;
	private double weight;
	private String underlying;
	private YearMonth expiry;
	private String futuresOrOptions;
	
	public AddLegDialog(Frame owner) {
		super(owner, "Add Leg", true);
		
		getContentPane().setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		
		// weight.
		centerPanel.add(new JLabel("Weight:"), 
				new GridBagConstraints(
						0, 0, 1, 1, 0, 0, 
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(1,1,1,10), 1, 1));

		
		weightField=new JTextField("1", 20);
		weightField.setMaximumSize(weightField.getPreferredSize());
		centerPanel.add(weightField, 
				new GridBagConstraints(
						1, 0, 2, 1, 1, 0, 
						GridBagConstraints.EAST, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(1,1,1,1), 1, 1));

		// underlying
		centerPanel.add(new JLabel("Underlying:"), 
				new GridBagConstraints(
						0, 1, 1, 1, 0, 0, 
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(1,1,1,10), 1, 1));
		
		underlyingBox=new JComboBox<String>(new String[] { "HSI", "HHI", "MHI", "MCH"});
		underlyingBox.setMaximumSize(underlyingBox.getPreferredSize());
		centerPanel.add(underlyingBox,
				new GridBagConstraints(
						1, 1, 2, 1, 1, 0, 
						GridBagConstraints.EAST, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(1,1,1,1), 1, 1));
				
		// expiry year and month
		centerPanel.add(new JLabel("Expiry:"), 
				new GridBagConstraints(
						0, 2, 1, 1, 0, 0, 
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(1,1,1,10), 1, 1));
		
		LocalDate d=new LocalDate(new java.util.Date());
		int currentYear = d.getYear();
		yearBox = new JComboBox<String>(new String[] { 
				Integer.valueOf(currentYear-2).toString(),
				Integer.valueOf(currentYear-1).toString(),
				Integer.valueOf(currentYear).toString(),
				Integer.valueOf(currentYear+1).toString(),
				Integer.valueOf(currentYear+2).toString() 
		});
		yearBox.setSelectedIndex(2); // select current year
		centerPanel.add(yearBox,
				new GridBagConstraints(
						1, 2, 1, 1, 1, 0, 
						GridBagConstraints.EAST, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(1,1,1,1), 1, 1));
		
		monthBox = new JComboBox<String>(new String[] { "1","2","3","4","5","6","7","8","9","10","11","12" });
		centerPanel.add(monthBox,
				new GridBagConstraints(
						2, 2, 1, 1, 1, 0, 
						GridBagConstraints.EAST, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(1,1,1,1), 1, 1));
		

		// futures or options.
		centerPanel.add(new JLabel("Futures/Options:"), 
				new GridBagConstraints(
						0, 3, 1, 1, 0, 0, 
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(1,1,1,10), 1, 1));
		
		futuresOptionsBox = new JComboBox<String>(new String[] { "Futures", "Options" });
		futuresOptionsBox.setMaximumSize(futuresOptionsBox.getPreferredSize());
		centerPanel.add(futuresOptionsBox,
				new GridBagConstraints(
						1, 3, 2, 1, 1, 0, 
						GridBagConstraints.EAST, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(1,1,1,1), 1, 1));
				
		// dummy panel to eat up the space
		JPanel dummy = new JPanel();
		centerPanel.add(dummy,
				new GridBagConstraints(
						0, 4, 3, 1, 1, 1, 
						GridBagConstraints.CENTER, 
						GridBagConstraints.BOTH, 
						new Insets(0,0,0,0), 0, 0));
				
		
		centerPanel.setBorder(
				BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Specify Leg Details"));
		
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
	
		
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setSize(350,300);
		
		setLocationRelativeTo(owner);
		
		validInput = false;
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			validInput = readInputDetails();
			
			if (validInput)
				setVisible(false);
		}
		else if (e.getActionCommand().equals("Cancel")) {
			validInput = false;
			setVisible(false);
		}
	}
	
	
	private boolean readInputDetails() {
		String weightStr = weightField.getText();
		try {
			weight = Double.parseDouble(weightStr);
		} catch (Exception e1) {
			
			JOptionPane.showMessageDialog(this, "Invalid Weight Input: "+weightStr, "Invalid Input", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		underlying = underlyingBox.getItemAt(underlyingBox.getSelectedIndex());
		
		String yearStr = yearBox.getItemAt(yearBox.getSelectedIndex());
		String monthStr = monthBox.getItemAt(monthBox.getSelectedIndex());
		expiry = new YearMonth(Integer.valueOf(yearStr).intValue(), Integer.valueOf(monthStr).intValue());
		
		futuresOrOptions = futuresOptionsBox.getItemAt(futuresOptionsBox.getSelectedIndex());
		
		return true;
	}

	public boolean isValidInput() {
		return validInput;
	}

	public double getWeight() {
		return weight;
	}

	public String getUnderlying() {
		return underlying;
	}

	public YearMonth getExpiry() {
		return expiry;
	}

	public String getFuturesOrOptions() {
		return futuresOrOptions;
	}
	
}
