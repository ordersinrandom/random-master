package com.jbp.randommaster.datasource.historical;

public interface VanillaDerivativesData extends DerivativesData {

	public double getStrikePrice();
	
	public boolean isCall();
	
	public boolean isPut();
	
}
