package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.CallPut;
import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.FuturesOptions;

/**
 * Trade Records consolidator implementation that consolidates HkDerivativesTR objects into a single HkDerivativesConsolidatedData.
 *
 */
public class HkDerivativesTRConsolidator extends TradeRecordsConsolidator<HkDerivativesConsolidatedData, HkDerivativesTR> {

	@Override
	protected HkDerivativesConsolidatedData createConsolidatedData(LocalDateTime refTimestamp, Iterable<HkDerivativesTR> original,
			double lastTradedPrice, double maxTradedPrice, double minTradedPrice, double averagedPrice, double tradedVolume, int transactionsCount) {

		YearMonth expiryMonth = null;
		String underlying = null;
		double strikePrice = 0.0;
		FuturesOptions futuresOrOptions = null;
		CallPut callPut = null;

		for (HkDerivativesTR t : original) {
			// expiry
			if (expiryMonth == null)
				expiryMonth = t.getExpiryMonth();
			else if (!expiryMonth.equals(t.getExpiryMonth()))
				throw new IllegalArgumentException("input data doesn't have the same expiry month: current=" + expiryMonth + ", input="
						+ t.getExpiryMonth());
			// underlying
			if (underlying == null)
				underlying = t.getUnderlying();
			else if (!underlying.equals(t.getUnderlying()))
				throw new IllegalArgumentException("input data doesn't have the same underlying: current=" + underlying + ", input="
						+ t.getUnderlying());
			// strike
			if (strikePrice == 0.0)
				strikePrice = t.getStrikePrice();
			else if (strikePrice != t.getStrikePrice())
				throw new IllegalArgumentException("input data doesn't have the same strike: current=" + strikePrice + ", input="
						+ t.getStrikePrice());
			// futures or options
			if (futuresOrOptions == null)
				futuresOrOptions = t.getFuturesOrOptions();
			else if (futuresOrOptions != t.getFuturesOrOptions())
				throw new IllegalArgumentException("input data doesn't have the same futuresOrOptions: current=" + futuresOrOptions + ", input="
						+ t.getFuturesOrOptions());

			// call or put
			if (callPut == null)
				callPut = t.getCallPut();
			else if (callPut != t.getCallPut())
				throw new IllegalArgumentException("input data doesn't have the same callPut: current=" + callPut + ", input=" + t.getCallPut());
		}

		HkDerivativesConsolidatedData result = new HkDerivativesConsolidatedData(refTimestamp, expiryMonth, underlying, strikePrice,
				futuresOrOptions, callPut, lastTradedPrice, maxTradedPrice, minTradedPrice, averagedPrice, tradedVolume, transactionsCount);

		return result;
	}

}
