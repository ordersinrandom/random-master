package com.jbp.randommaster.datasource.historical.filters;

import java.util.HashSet;

import com.jbp.randommaster.datasource.historical.HkDerivativesTR;

/**
 * An implementation of HkDerivativesTR data filter that filters by the Trade Type field.
 *
 */
public class HkDerivativesTRTradeTypeFilter implements HistoricalDataFilter<HkDerivativesTR> {

	/**
	 * TradeType: Normal (000,001,002), Block trade (003,004,005,006), Standard combination (007),
	 * DeltaHedge (020), Bulletin board trade (032, 033, 034, 035), Non-standard combination (037),
	 * Standard Combo Trade (134)
	 */
	public enum TradeType { Normal, BlockTrade, StandardCombination, DeltaHedge, BulletinBoardTrade, NonStandardCombination, StandardComboTrade };
	
	private HashSet<String> acceptedTradeTypes;
	
	public HkDerivativesTRTradeTypeFilter(TradeType t) {
		acceptedTradeTypes = new HashSet<String>();
		if (t == TradeType.Normal) {
			acceptedTradeTypes.add("000");
			acceptedTradeTypes.add("001");
			acceptedTradeTypes.add("002");
		} else if (t == TradeType.BlockTrade) {
			acceptedTradeTypes.add("003");
			acceptedTradeTypes.add("004");
			acceptedTradeTypes.add("005");
			acceptedTradeTypes.add("006");
		} else if (t == TradeType.StandardCombination) {
			acceptedTradeTypes.add("007");
		} else if (t == TradeType.DeltaHedge) {
			acceptedTradeTypes.add("020");
		} else if (t == TradeType.BulletinBoardTrade) {
			acceptedTradeTypes.add("032");
			acceptedTradeTypes.add("033");
			acceptedTradeTypes.add("034");
			acceptedTradeTypes.add("035");
		} else if (t == TradeType.NonStandardCombination) {
			acceptedTradeTypes.add("037");
		} else if (t == TradeType.StandardComboTrade) {
			acceptedTradeTypes.add("134");
		} else
			throw new IllegalArgumentException("Unrecognized TradeType: " + t);

	}
	
	@Override
	public boolean accept(HkDerivativesTR data) {
		return acceptedTradeTypes.contains(data.getTradeType());
	}

}
