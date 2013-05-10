package com.jbp.randommaster.quant.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * 
 * Polynomial of this form. <br/>
 * 
 * y = 
 * 		coef_0_0 + coef_0_1 * x0 + coef_0_2 * x0^2 +...
 * 		+coef_1_0 + coef_1_1 * x1 + coef_1_2 * x1^2 +...
 * 		+...
 *
 */
public class Polynomial implements RealScalarFunction {

	private List<RealVector> coefficients;
	
	/**
	 * Create a polynomial of this form. <br/>
	 * 
	 * y = 
	 * 		coef_0_0 + coef_0_1 * x0 + coef_0_2 * x0^2 +...
	 * 		+coef_1_0 + coef_1_1 * x1 + coef_1_2 * x1^2 +...
	 * 		+...
	 * 
	 * <br/>
	 * 
	 * The input coefficients list should be
	 * [ <coef_0_0, coef_0_1, ...>, <coef_1_0, coef_1_1 .... >, ... ]
	 * 
	 * @param coefficients The vector of coefficients for each dimension.
	 */
	public Polynomial(List<RealVector> coefficients) {
		if (coefficients==null || coefficients.isEmpty())
			throw new IllegalArgumentException("invalid coefficients input");
		this.coefficients=coefficients;
	}
	
	/**
	 * Create a polynomial of one dimension.
	 */
	public Polynomial(RealVector coeff) {
		List<RealVector> l = new ArrayList<RealVector>();
		l.add(coeff);
		this.coefficients=l;
	}
	
	
	@Override
	public int getDomainDimension() {
		return 1;
	}

	@Override
	public int getRangeDimension() {
		return coefficients.size();
	}

	@Override
	public Double evaluate(RealVector x) {
		if (x.getDimension()!=coefficients.size())
			throw new IllegalArgumentException("input vector x dimension mismatched with coefficients list");
		
		double y=0.0;
		
		for (int i=0;i<x.getDimension();i++) {
			RealVector coeff=coefficients.get(i);
			
			double xValue = x.getEntry(i);
			// create the polynomial xVector
			ArrayRealVector xPolynomialVect = new ArrayRealVector(coeff.getDimension());
			for (int k=0;k<coeff.getDimension();k++) {
				if (k==0) 
					xPolynomialVect.setEntry(k, 1.0); // 0 power 0 is NaN, we need to avoid.
				else xPolynomialVect.setEntry(k, Math.pow(xValue, (double) k)); 
			}
			
			y+= coeff.dotProduct(xPolynomialVect);
		}
		
		return y;
	}
	
	@Override
	public String toString() {
		
		StringBuilder buf=new StringBuilder();
		buf.append("Polynomial: coefficients= { ");
		for (int i=0;i<coefficients.size();i++) {
			buf.append("Dimension(");
			buf.append(i);
			buf.append("): ");
			buf.append(coefficients.get(i));
			buf.append('\n');
		}
		buf.append(" }");
		return buf.toString();
	}

}
