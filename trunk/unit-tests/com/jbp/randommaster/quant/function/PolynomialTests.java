package com.jbp.randommaster.quant.function;

import java.util.ArrayList;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;


public class PolynomialTests extends TestCase {

	@Test
	public void testMXPlusC() {
		
		// y = 2 x + 1
		ArrayRealVector coef=new ArrayRealVector(new double[] { 1.0, 2.0 });
		
		Polynomial p=new Polynomial(coef);
		
		double y=p.evaluate(new ArrayRealVector(new double[] { 3.0 }));
		
		Assert.assertEquals("y is not 7.0", 7.0, y, 0.0000000001);
		
	}
	
	@Test
	public void testQuadratic() {
		
		// y = -1.0 x^2 + 5.0 x - 8.0
		
		ArrayRealVector coef=new ArrayRealVector(new double[] { -8.0, 5.0, -1.0 });
		
		Polynomial p=new Polynomial(coef);
		
		double y=p.evaluate(new ArrayRealVector(new double[] { 2.0 }));
		Assert.assertEquals("y is not -2.0", -2.0, y, 0.000000001);
		
	}
	
	@Test
	public void testTwoDimensionRange() {
		
		// y = 1.0 + 2.0 x0 -5.0 x0^2 + 3.0 x1 + 4.0 x1^2 -2.0 x1^3
		
		ArrayRealVector coef0=new ArrayRealVector(new double[] { 1.0, 2.0, -5.0 });
		ArrayRealVector coef1=new ArrayRealVector(new double[] { 0.0, 3.0, 4.0, -2.0 });
		ArrayList<RealVector> coefs=new ArrayList<RealVector>();
		coefs.add(coef0);
		coefs.add(coef1);
		
		Polynomial p=new Polynomial(coefs);
		
		double y=p.evaluate(new ArrayRealVector(new double[] { -3.0, 5.5 }));
		
		Assert.assertEquals("y is not -245.25", -245.25, y, 0.00000001);
	}

}
