package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class ThomasAlgorithm {

	/**
	 * Solve the tridiagonal matrix problem. <br/>
	 * 
	 * Refers to http://www3.ul.ie/wlee/ms6021_thomas.pdf
	 * 
	 * Note the first value of aDiagonal and last value of cDiagonal will be discarded.
	 * 
	 * All input arrays are expected to have the same size.
	 * 
	 */
	public static double[] solve(double[] a, double[] b, double[] c, double[] r) {
	
		if (!(a.length==b.length && b.length==c.length && c.length==r.length))
			throw new IllegalArgumentException("input arrarys size mismatched");
		
		double[] rho = new double [a.length];
		double[] gamma = new double [a.length-1];

		// stage 1: fill up the rho and gamma
		for (int i=0;i<a.length;i++) {
			if (i==0) {
				gamma[0]=c[0]/b[0];
				rho[0]=r[0]/b[0];
			}
			else {
				if (i<a.length-1)
					gamma[i] = c[i] / (b[i]-a[i]*gamma[i-1]);
				rho[i] = (r[i] - a[i] * rho[i-1]) / (b[i]-a[i]*gamma[i-1]);
			}
		}

		// stage 2: compute the result vector x.
		double[] x = new double [a.length];
		x[x.length-1]=rho[rho.length-1];
		for (int i=x.length-2;i>=0;i--) {
			x[i]=rho[i] - gamma[i] * x[i+1];
		}
		
		return x;
	}
	
	
	public static RealMatrix solve(RealMatrix tridiagonalMatrix, RealMatrix rValues) {
	
		if (!tridiagonalMatrix.isSquare())
			throw new IllegalArgumentException("tridiagonalMatrix is not square");
		
		if (rValues.getColumnDimension()!=1)
			throw new IllegalArgumentException("rValues is not a column matrix");
		
		int rows = tridiagonalMatrix.getRowDimension();
		
		double[] a = new double [rows];
		double[] b = new double [rows];
		double[] c = new double [rows];
		
		double[] r = new double [rValues.getRowDimension()];
		for (int i=0;i<r.length;i++) {
			r[i]=rValues.getEntry(i, 0);
		}
		
		for (int i=0;i<rows;i++) {
			if (i>0)
				a[i]=tridiagonalMatrix.getEntry(i, i-1);
			if (i<rows-1)
				c[i]=tridiagonalMatrix.getEntry(i, i+1);
			
			b[i]=tridiagonalMatrix.getEntry(i, i);
		}
		
		double[] x = solve(a,b,c,r);
		
		
		double[][] data=new double[x.length][1];
		for (int i=0;i<x.length;i++) {
			data[i][0] = x[i];
		}
		
		return new Array2DRowRealMatrix(data);
		
	}
	
}
