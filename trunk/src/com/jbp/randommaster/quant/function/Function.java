package com.jbp.randommaster.quant.function;

/**
 * 
 * Define the abstract interface of function of given Domain and Range
 *
 * @param <R> Range. (i.e. the y)
 * @param <D> Domain. (i.e. the x)
 */
public interface Function<R, D> {

	public int getDomainDimension();
	
	public int getRangeDimension();

	public R evaluate(D x);
	
}
