package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

/**
 * Trade Records consolidator implementation that consolidates HkDerivativesTR objects into a single HkDerivativesConsolidatedData.
 *
 */
public class HkDerivativesTRConsolidator extends TradeRecordsConsolidator<HkDerivativesConsolidatedData, HkDerivativesTR> {

	/**
	 * Given a series of HkDerivativesTR data, and superclass computed fields such as first/last/max/min/average/tradedVolume,
	 * build an HkDerivativesConsolidatedData object.
	 */
	@Override
	protected HkDerivativesConsolidatedData createConsolidatedData(LocalDateTime refTimestamp, Iterable<HkDerivativesTR> original,
			double firstTradedPrice, double lastTradedPrice, double maxTradedPrice, double minTradedPrice, double averagedPrice, double tradedVolume) {

		YearMonth expiryMonth = null;
		String underlying = null;
		double strikePrice = 0.0;
		String futuresOrOptions = null;
		String callPut = null;

		for (HkDerivativesTR t : original) {
			// expiry
			if (expiryMonth == null)
				expiryMonth = t.getExpiryMonth();
			else if (!expiryMonth.equals(t.getExpiryMonth()))
				throw new IllegalArgumentException("input data series doesn't have the same expiry month: current=" + expiryMonth + ", input="
						+ t.getExpiryMonth());
			// underlying
			if (underlying == null)
				underlying = t.getUnderlying();
			else if (!underlying.equals(t.getUnderlying()))
				throw new IllegalArgumentException("input data series doesn't have the same underlying: current=" + underlying + ", input="
						+ t.getUnderlying());
			// strike
			if (strikePrice == 0.0)
				strikePrice = t.getStrikePrice();
			else if (strikePrice != t.getStrikePrice())
				throw new IllegalArgumentException("input data series doesn't have the same strike: current=" + strikePrice + ", input="
						+ t.getStrikePrice());
			// futures or options
			if (futuresOrOptions == null)
				futuresOrOptions = t.getFuturesOrOptions();
			else if (!futuresOrOptions.equals(t.getFuturesOrOptions()))
				throw new IllegalArgumentException("input data series doesn't have the same futuresOrOptions: current=" + futuresOrOptions + ", input="
						+ t.getFuturesOrOptions());

			// call or put
			if (callPut == null)
				callPut = t.getCallPut();
			else if (!callPut.equals(t.getCallPut()))
				throw new IllegalArgumentException("input data series doesn't have the same callPut: current=" + callPut + ", input=" + t.getCallPut());
		}

		HkDerivativesConsolidatedData result = new HkDerivativesConsolidatedData(refTimestamp, expiryMonth, underlying, strikePrice,
				futuresOrOptions, callPut, firstTradedPrice, lastTradedPrice, maxTradedPrice, minTradedPrice, averagedPrice, tradedVolume);

		return result;
	}

	/**
	 * Extrapolate forward in time by given previous intervals data, and the new reference timestamp.
	 */
	@Override
	protected HkDerivativesConsolidatedData extrapolate(LocalDateTime refTimestamp, Iterable<HkDerivativesConsolidatedData> previousIntervalResults) {
		// get the last item
		HkDerivativesConsolidatedData lastItem = null;
		for (HkDerivativesConsolidatedData d :  previousIntervalResults ) {
			lastItem=d;
		}
		

		if (lastItem==null) {
			throw new TradeRecordsConsolidatorException("there is no previous result to extrapolate for the given timestamp: "+refTimestamp);
		}
		else {
			return new HkDerivativesConsolidatedData(refTimestamp, lastItem.getExpiryMonth(), 
					lastItem.getUnderlying(), lastItem.getStrikePrice(),
					lastItem.getFuturesOrOptions(), 
					lastItem.getCallPut(), 
					lastItem.getLastTradedPrice(), // first traded price as last traded price if we are extrapolating.
					lastItem.getLastTradedPrice(), 
					lastItem.getLastTradedPrice(), 
					lastItem.getLastTradedPrice(), 
					lastItem.getLastTradedPrice(), 
					0.0);			
		}
		
	}

	/**
	 * Extrapolate backward in time given a series of "later interval" output from the super class and an earlier reference timestamp.
	 */
	@Override
	protected HkDerivativesConsolidatedData backwardExtrapolate(LocalDateTime refTimestamp, Iterable<HkDerivativesConsolidatedData> laterIntervalResults) {

		HkDerivativesConsolidatedData nextItem = null;
		for (HkDerivativesConsolidatedData d : laterIntervalResults) {
			nextItem=d;
			break;
		}
		
		if (nextItem==null)
			throw new TradeRecordsConsolidatorException("there is no later result to backward extrapolate for the given timestamp: "+refTimestamp);
		else {
			return new HkDerivativesConsolidatedData(refTimestamp, nextItem.getExpiryMonth(), 
					nextItem.getUnderlying(), nextItem.getStrikePrice(),
					nextItem.getFuturesOrOptions(), 
					nextItem.getCallPut(), 
					nextItem.getFirstTradedPrice(),
					nextItem.getFirstTradedPrice(), // last traded price as first traded price if we are extrapolating backward 
					nextItem.getFirstTradedPrice(), 
					nextItem.getFirstTradedPrice(), 
					nextItem.getFirstTradedPrice(), 
					0.0);						
		}
		
	}
}
