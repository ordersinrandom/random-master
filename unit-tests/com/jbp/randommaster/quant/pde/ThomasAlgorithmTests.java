package com.jbp.randommaster.quant.pde;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import com.jbp.randommaster.quant.common.ThomasAlgorithm;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ThomasAlgorithmTests extends TestCase {

	@Test
	public void testTwoByTwo() {
		
		double[] a=new double [2];
		double[] b=new double [2];
		double[] c=new double [2];
		double[] r=new double [2];
		
		// 2 x0 + 6 x1 = 1
		// 3 x0 - 1 x1 = 2
		
		b[0] = 2.0;
		c[0] = 6.0;
		a[1] = 3.0;
		b[1] = -1.0;
		
		r[0] = 1.0;
		r[1] = 2.0;
		
		double[] x = ThomasAlgorithm.solve(a, b, c, r);
		
		Assert.assertNotNull("Result array is null", x);
		if (x!=null) {
			Assert.assertEquals("Result array is not exactly 2 items", 2, x.length);
			// the answer are 0.65 and -0.05
			Assert.assertEquals("x[0] is not correct", 0.65, x[0], 0.00000001);
			Assert.assertEquals("x[1] is not correct", -0.05, x[1], 0.00000001);
		}
		
	}
	
	@Test
	public void testThreeByTree() {
		
		// 1 x0 +4 x1 +0 x2 = 6
		// 2 x0 -1 x1 +1 x2 = 4
		// 0 x0 +1 x1 +1 x2 = 1
		
		double[] a = new double [3];
		double[] b = new double [3];
		double[] c = new double [3];
		
		double[] r = new double [3];
		
		b[0] = 1.0;
		c[0] = 4.0;
		r[0] = 6.0;
		
		a[1] = 2.0;
		b[1] = -1.0;
		c[1] = 1.0;
		r[1] = 4.0;
		
		a[2] = 1.0;
		b[2] = 1.0;
		r[2] = 1.0;
		
		double[] x  = ThomasAlgorithm.solve(a, b, c, r);
		
		Assert.assertNotNull("Result array is null", x);
		if (x!=null) {
			Assert.assertEquals("Result array is not exactly 3 items", 3, x.length);
			// the answer are 2.4, 0.9, 0.1
			Assert.assertEquals("x[0] is not correct", 2.4, x[0], 0.00000001);
			Assert.assertEquals("x[1] is not correct", 0.9, x[1], 0.00000001);
			Assert.assertEquals("x[2] is not correct", 0.1, x[2], 0.00000001);
		}
	}
	
	@Test
	public void testThreeByTreeWithSomeZeros() {
		
		// 1 x0 +4 x1 +0 x2 = 6
		// 2 x0 -1 x1 +1 x2 = 4
		// 0 x0 +1 x1 +0 x2 = 1
		
		double[] a = new double [3];
		double[] b = new double [3];
		double[] c = new double [3];
		
		double[] r = new double [3];
		
		b[0] = 1.0;
		c[0] = 4.0;
		r[0] = 6.0;
		
		a[1] = 2.0;
		b[1] = -1.0;
		c[1] = 1.0;
		r[1] = 4.0;
		
		a[2] = 1.0;
		b[2] = 0.0;
		r[2] = 1.0;
		
		double[] x  = ThomasAlgorithm.solve(a, b, c, r);
		
		Assert.assertNotNull("Result array is null", x);
		if (x!=null) {
			Assert.assertEquals("Result array is not exactly 3 items", 3, x.length);
			// the answer are 2.0, 1.0, 1.0
			Assert.assertEquals("x[0] is not correct", 2.0, x[0], 0.00000001);
			Assert.assertEquals("x[1] is not correct", 1.0, x[1], 0.00000001);
			Assert.assertEquals("x[2] is not correct", 1.0, x[2], 0.00000001);
		}
	}	
	
	
	
	@Test
	public void testTwoByTwoMatrix() {

		// 2 x0 + 6 x1 = 1
		// 3 x0 - 1 x1 = 2
		
		Array2DRowRealMatrix tri = new Array2DRowRealMatrix(
				new double[][] {
						{ 2.0, 6.0 },
						{ 3.0, -1.0 }
				});
		
		Array2DRowRealMatrix r = new Array2DRowRealMatrix(new double[][] {
				{ 1.0 },
				{ 2.0 }
		});
		
		RealMatrix x = ThomasAlgorithm.solve(tri, r);
		
		Assert.assertNotNull("Result matrix is null", x);
		if (x!=null) {
			Assert.assertEquals("Result matrix is not exactly 2 x 1", 2, x.getRowDimension());
			Assert.assertEquals("Result matrix is not exactly 2 x 1", 1, x.getColumnDimension());
			// the answer are 0.65 and -0.05
			Assert.assertEquals("x[0] is not correct", 0.65, x.getEntry(0, 0), 0.00000001);
			Assert.assertEquals("x[1] is not correct", -0.05, x.getEntry(1, 0), 0.00000001);
		}
		
	}	
	
	
	@Test
	public void testThreeByTreeMatrix() {
		
		// 1 x0 +4 x1 +0 x2 = 6
		// 2 x0 -1 x1 +1 x2 = 4
		// 0 x0 +1 x1 +1 x2 = 1
		
		Array2DRowRealMatrix tri = new Array2DRowRealMatrix(new double[][] {
			{ 1.0, 4.0, 0.0 },
			{ 2.0, -1.0, 1.0 },
			{ 0.0, 1.0, 1.0 }
		});
		
		Array2DRowRealMatrix r = new Array2DRowRealMatrix(new double[][] {
				{ 6.0 },
				{ 4.0 },
				{ 1.0 }
		});
		
		RealMatrix x  = ThomasAlgorithm.solve(tri, r);
		
		Assert.assertNotNull("Result array is null", x);
		if (x!=null) {
			Assert.assertEquals("Result array is not exactly 3 x 1", 3, x.getRowDimension());
			Assert.assertEquals("Result array is not exactly 3 x 1", 1, x.getColumnDimension());
			// the answer are 2.4, 0.9, 0.1
			Assert.assertEquals("x[0] is not correct", 2.4, x.getEntry(0, 0), 0.00000001);
			Assert.assertEquals("x[1] is not correct", 0.9, x.getEntry(1, 0), 0.00000001);
			Assert.assertEquals("x[2] is not correct", 0.1, x.getEntry(2, 0), 0.00000001);
		}
	}	
}
