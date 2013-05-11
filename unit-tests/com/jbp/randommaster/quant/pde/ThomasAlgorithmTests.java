package com.jbp.randommaster.quant.pde;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ThomasAlgorithmTests extends TestCase {

	
	
	@Test
	public void testTwoByTwoCase() {
		
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
	
}
