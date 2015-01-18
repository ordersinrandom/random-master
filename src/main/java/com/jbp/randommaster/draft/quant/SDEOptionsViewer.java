package com.jbp.randommaster.draft.quant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jbp.randommaster.quant.pde.BlackScholesPdeFiniteDifferenceSolver;
import com.jbp.randommaster.quant.pde.FeynmanKacFormula;
import com.jbp.randommaster.quant.pde.BlackScholesPdeFiniteDifferenceSolver.Solution;
import com.jbp.randommaster.quant.sde.univariate.OUProcess;

public class SDEOptionsViewer extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 4848153128469539706L;
	
	private JTextField ouThetaField, ouMuField, ouSigmaField, ouStrikeField, ouTmatField;
	
	public SDEOptionsViewer() {
		super("Put options on SDE");
		
		ouThetaField=new JTextField(8);
		ouMuField=new JTextField(8);
		ouSigmaField=new JTextField(8);
		ouStrikeField=new JTextField(8);
		ouTmatField=new JTextField(8);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel ouPanel=new JPanel();
		ouPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		ouPanel.add(new JLabel("Theta:"));
		ouPanel.add(ouThetaField);
		ouPanel.add(new JLabel("Mu:"));
		ouPanel.add(ouMuField);
		ouPanel.add(new JLabel("Sigma:"));
		ouPanel.add(ouSigmaField);
		ouPanel.add(new JLabel("Strike:"));
		ouPanel.add(ouStrikeField);
		ouPanel.add(new JLabel("Tmat:"));
		ouPanel.add(ouTmatField);

		
		JButton computeBut=new JButton("Compute");
		computeBut.setActionCommand("Compute OU");
		ouPanel.add(computeBut);
		computeBut.addActionListener(this);
		tabbedPane.addTab("OU Process", ouPanel);
		
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Compute OU")) {
			double theta = Double.valueOf(ouThetaField.getText()).doubleValue();
			double mu = Double.valueOf(ouMuField.getText()).doubleValue();
			double sigma = Double.valueOf(ouSigmaField.getText()).doubleValue();
			double strike = Double.valueOf(ouStrikeField.getText())
					.doubleValue();
			double tmat = Double.valueOf(ouTmatField.getText()).doubleValue();
			// double x0 = Double.valueOf(x0Field.getText()).doubleValue();

			Solution sol = computeOUProcessEuropeanPut(theta, mu, sigma,
					strike, tmat);

			double[] solutionValue = sol.getPdeGrid().get(
					sol.getPdeGrid().size() - 1);

			XYSeries putPriceSeries = new XYSeries("OU Process European Put");

			double x = sol.getxMin();
			int i = 0;
			while (i < solutionValue.length) {
				putPriceSeries.add(x, solutionValue[i]);

				// System.out.println("X: "+x+", sol="+solutionValue[i]);
				x += sol.getDx();
				i++;
			}

			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(putPriceSeries);

			JFreeChart chart = ChartFactory.createXYLineChart(
					"OU Process Put Price", "X", "Price", dataset,
					PlotOrientation.VERTICAL, true, true, false);

			chart.getPlot().setBackgroundPaint(
					new GradientPaint(1, 1, Color.yellow.darker().darker(),
							1500, 1500, Color.darkGray));

			getContentPane().add(new ChartPanel(chart), BorderLayout.CENTER);

			super.invalidate();
			super.validate();
		}

	}
	
	
	
	public Solution computeOUProcessEuropeanPut(final double theta, final double mu, final double sigma, final double strike, final double tmat) {
		
		OUProcess ou=new OUProcess(theta, mu, sigma);
		FeynmanKacFormula pde=new FeynmanKacFormula(ou);
		
		MultivariateFunction boundaryConditionAtT = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				if (x<strike)
					return strike-x; // vanilla put payoff
				else return 0.0;
			}
		};
		
		MultivariateFunction boundaryConditionAtMinX = new MultivariateFunction() {
			@Override
			public double value(double[] input) {
				double x = input[1];
				return (strike -x);
			}
		};
		
		MultivariateFunction boundaryConditionAtMaxX = new MultivariateFunction() {
			public double value(double[] input) {
				return 0.0;
			}
		};		
		
		BlackScholesPdeFiniteDifferenceSolver solver= new BlackScholesPdeFiniteDifferenceSolver(
				pde, boundaryConditionAtT, boundaryConditionAtMaxX, boundaryConditionAtMinX, 800, 600);

		Solution sol=solver.computePde(mu + 5 * sigma, mu - 5 * sigma, tmat);
		
		return sol;
	}



	public static void main(String[] args) throws UnsupportedLookAndFeelException, 
											IllegalAccessException, InstantiationException,
											ClassNotFoundException {
		
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		SDEOptionsViewer viewer=new SDEOptionsViewer();
		viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		viewer.setSize(1000, 600);
		viewer.setVisible(true);
	}
	
	

}
