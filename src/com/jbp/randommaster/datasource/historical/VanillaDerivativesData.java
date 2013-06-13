package com.jbp.randommaster.datasource.historical;

public interface VanillaDerivativesData extends DerivativesData {

	public enum CallPut { CALL, PUT, NA };
	public enum FuturesOptions { FUTURES, OPTIONS };
	
	public double getStrikePrice();
	public CallPut getCallPut();
	public FuturesOptions getFuturesOrOptions();
	public boolean isFutures();
	public boolean isOptions();
	
}