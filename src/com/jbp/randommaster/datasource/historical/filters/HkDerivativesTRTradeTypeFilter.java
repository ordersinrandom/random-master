package com.jbp.randommaster.datasource.historical.filters;

import java.util.HashSet;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;

/**
 * An implementation of HkDerivativesTR data filter that filters by the Trade Type field.
 *
 */
public class HkDerivativesTRTradeTypeFilter implements HistoricalDataFilter<HkDerivativesTR> {

	/**
	 * TradeType: Normal (000,001,002), DeltaHedge (020).
	 */
	public enum TradeType { Normal, DeltaHedge };
	
	private HashSet<String> acceptedTradeTypes;
	
	public HkDerivativesTRTradeTypeFilter(TradeType t) {
		acceptedTradeTypes=new HashSet<String>();
		if (t==TradeType.Normal) {
			acceptedTradeTypes.add("000");
			acceptedTradeTypes.add("001");
			acceptedTradeTypes.add("002");
		}
		else if (t==TradeType.DeltaHedge) {
			acceptedTradeTypes.add("020");
		}
		else throw new IllegalArgumentException("Unrecognized TradeType: "+t);

	}
	
	@Override
	public boolean accept(HkDerivativesTR data) {
		return acceptedTradeTypes.contains(data.getTradeType());
	}

}
