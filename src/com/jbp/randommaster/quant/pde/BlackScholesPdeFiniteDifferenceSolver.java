package com.jbp.randommaster.quant.pde;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

/**
 * 
 * Implement a Crank Nicolson scheme PDE solver for the PDE defined in <code>BlackScholesPde</code>.
 *
 */
public class BlackScholesPdeFiniteDifferenceSolver {

	// multivariate function (x,t)
	private MultivariateFunction boundaryConditionAtT;
	private MultivariateFunction boundaryConditionAtMaxX;
	private MultivariateFunction boundaryConditionAtMinX;
	
	private int spaceSteps;
	private int timeSteps;
	
	private BlackScholesPde pde;

	public BlackScholesPdeFiniteDifferenceSolver(
				BlackScholesPde pde,
				MultivariateFunction boundaryConditionAtT,
				MultivariateFunction boundaryConditionAtMaxX,
				MultivariateFunction boundaryConditionAtMinX,
				int spaceSteps,
				int timeSteps) {
		this.pde=pde;
		this.boundaryConditionAtT=boundaryConditionAtT;
		this.boundaryConditionAtMaxX=boundaryConditionAtMaxX;
		this.boundaryConditionAtMinX=boundaryConditionAtMinX;
		this.spaceSteps=spaceSteps;
		this.timeSteps=timeSteps;
	}
	
	
	/**
	 * Compute the solution of the PDE.
	 */
	public Solution computePde(double xMax, double xMin, double tMax) {
		
		double dt = tMax/timeSteps;
		double dx = (xMax - xMin)/(spaceSteps+2); // include the upper and lower boundary
		
		int n=0; // time index, 0 means at maturity
		int j=0; // space index, 0 means at lowest grid point
		
		int maxN = timeSteps;
		int maxJ = spaceSteps-1;
		
		int layerSize = maxJ+1;
		
		
		ArrayList<double[]> pdeGrid=new ArrayList<double[]>(maxN+1);
		
		// scan across time axis
		for (n=0;n<=maxN;n++) {

			double[] valuationLayer=new double[layerSize];
			double[] a=new double[layerSize];
			double[] b=new double[layerSize];
			double[] c=new double[layerSize];
			double[] d=new double[layerSize];
			
			// scan across space axis
			for (j=0;j<=maxJ;j++) {

				double currentX = (xMin+dx) + j*dx;
				double currentT = (maxN-n) * dt;
				
				double prevT = (maxN-(n-1)) * dt;
				
				
				// if it is at maturity
				if (n==0) {
					valuationLayer[j] = boundaryConditionAtT.value(new double[] { currentX, currentT });
				}
				else {

					double[] xAndT=new double[] { currentX, currentT };
					double mu = pde.getMu().value(xAndT);
					double sigma = pde.getSigma().value(xAndT);
					double r = pde.getR().value(xAndT);
					
					// fill in a, b, c arrays
					a[j] = -mu / (4.0 * dx) + sigma / (2.0 * dx * dx);
					b[j] = -1.0 / dt - sigma / (dx * dx) + r / 2.0;
					c[j] = mu / (4.0 * dx) + sigma / (2.0 * dx * dx);

					double[] prevLayer = pdeGrid.get(pdeGrid.size()-1); 

					// fill in d[] for different state steps
					if (j>0 && j<maxJ) {
						
						d[j] = -prevLayer[j] * (1.0 / dt - sigma / (dx*dx) + r/2.0)
								-prevLayer[j+1] * (mu/(4.0*dx) + sigma / (2.0 * dx*dx))
								-prevLayer[j-1] * (-mu/(4.0*dx) + sigma / (2.0 * dx * dx));
						
					}
					else if (j==0) {
						
						double prevVJMinus1 = boundaryConditionAtMinX.value(new double[] { xMin, prevT });
						
						double originalD = -prevLayer[j] * (1.0 / dt - sigma / (dx*dx) + r/2.0)
								-prevLayer[j+1] * (mu/(4.0*dx) + sigma / (2.0 * dx*dx))
								-prevVJMinus1 * (-mu/(4.0*dx) + sigma / (2.0 * dx * dx));
						
						double currentVJMinus1 = boundaryConditionAtMinX.value(new double[] { xMin, currentT });
						
						d[j] = originalD - a[j] * currentVJMinus1;
						
					}
					else if (j==maxJ) {

						double prevVJPlus1 = boundaryConditionAtMaxX.value(new double[] { xMax, prevT });
						
						double originalD = -prevLayer[j] * (1.0 / dt - sigma / (dx*dx) + r/2.0)
								-prevVJPlus1 * (mu/(4.0*dx) + sigma / (2.0 * dx*dx))
								-prevLayer[j-1] * (-mu/(4.0*dx) + sigma / (2.0 * dx * dx));
						
						double currentVJPlus1 = boundaryConditionAtMaxX.value(new double[] { xMax, currentT });
						
						d[j] = originalD - c[j] * currentVJPlus1;
					}
					
					
				}
			}

			// if it is not at maturity time step
			if (n>0) {
				// here a,b,c,d are all ready
				valuationLayer=ThomasAlgorithm.solve(a, b, c, d);
			}
			// save down the valuation layer and next.
			pdeGrid.add(valuationLayer);
		}
		
		
		return new Solution(pdeGrid, xMax, xMin, tMax, dt, dx);
		
	}

	public BlackScholesPde getPde() {
		return pde;
	}

	public int getSpaceSteps() {
		return spaceSteps;
	}

	public int getTimeSteps() {
		return timeSteps;
	}

	public MultivariateFunction getBoundaryConditionAtT() {
		return boundaryConditionAtT;
	}
	
	public MultivariateFunction getBoundaryConditionAtMaxX() {
		return boundaryConditionAtMaxX;
	}
	
	public MultivariateFunction getBoundaryConditionAtMinX() {
		return boundaryConditionAtMinX;
	}

	
	/**
	 * 
	 * Encapsulate the solution of BlackScholesPdeFiniteDifferenceSolver
	 *
	 */
	public class Solution  {
		
		private List<double[]> pdeGrid;
		private double xMax;
		private double xMin;
		private double tMax;
		private double dt;
		private double dx;
		
		public Solution(List<double[]> pdeGrid, double xMax, double xMin, double tMax, double dt, double dx) {
			this.pdeGrid=pdeGrid;
			this.xMax=xMax;
			this.xMin=xMin;
			this.tMax=tMax;
			this.dt=dt;
			this.dx=dx;
		}
		
		public double getSolutionAtTime0(double x0) {
			
			
			if (x0<=xMin)
				return boundaryConditionAtMinX.value(new double[] { x0, 0.0 });
			else if (x0>=xMax) 
				return boundaryConditionAtMaxX.value(new double[] { x0, 0.0 });

			
			double vMin = boundaryConditionAtMinX.value(new double[] { xMin, 0.0 });
			double vMax = boundaryConditionAtMaxX.value(new double[] { xMax, 0.0 });
			double[] gridAtTime0 = pdeGrid.get(pdeGrid.size()-1);
			
			double[] v = new double [ gridAtTime0.length +2];
			double[] x = new double [ gridAtTime0.length +2];
			x[0] = xMin;
			v[0] = vMin;
			x[v.length-1]=xMax;
			v[v.length-1]=vMax;
			
			for (int i=1;i<v.length-1;i++) {
				x[i] = xMin + dx * ((double)i);
				v[i] = gridAtTime0[i-1];
			}

			UnivariateInterpolator interpolator = new LinearInterpolator();
			UnivariateFunction solutionCurve = interpolator.interpolate(x, v);
			double interpolatedV = solutionCurve.value(x0);
			return interpolatedV;				
		}
		
		public List<double[]> getPdeGrid() {
			return pdeGrid;
		}
		public double getxMax() {
			return xMax;
		}
		public double getxMin() {
			return xMin;
		}
		public double gettMax() {
			return tMax;
		}
		public double getDt() {
			return dt;
		}
		public double getDx() {
			return dx;
		}
		
	}
	
}
