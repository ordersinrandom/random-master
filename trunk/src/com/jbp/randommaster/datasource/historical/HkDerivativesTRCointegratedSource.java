package com.jbp.randommaster.datasource.historical;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.javatuples.Pair;

import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesConsolidatedData;

/**
 * An implementation of CointegratedTRSource on HkDerivativesConsolidatedData
 *
 */
public class HkDerivativesTRCointegratedSource extends CointegratedTRSource<HkDerivativesConsolidatedData> {

	@Override
	protected HkDerivativesConsolidatedData cointegrate(Iterable<Pair<HkDerivativesConsolidatedData, Double>> allDataAndWeight) {
		
		LocalDateTime timestamp = null;
		YearMonth expiryMonth = null;
		String underlying = null;
		String futuresOrOptions = null;
		String callPut = null;
		double strikePrice = 0.0; // we don't verify this
		
		double firstTradedPrice = 0.0; 
		double lastTradedPrice = 0.0; 
		double maxTradedPrice = 0.0;
		double minTradedPrice = 0.0;
		double averagedPrice = 0.0;
		double tradedVolume = 0.0;
		
		for (Pair<HkDerivativesConsolidatedData, Double> p : allDataAndWeight) {

			HkDerivativesConsolidatedData d = p.getValue0();
			double weight = p.getValue1();
			
			if (timestamp==null)
				timestamp = d.getTimestamp();
			else if (!timestamp.equals(d.getTimestamp()))
				throw new IllegalArgumentException("timestamp mismatched: "+timestamp+", and "+d.getTimestamp()+", for input "+d);
			
			if (expiryMonth==null)
				expiryMonth = d.getExpiryMonth();
			
			if (underlying==null)
				underlying = weight+"*"+d.getUnderlying();
			else underlying = underlying + " " + weight + "*"+d.getUnderlying();
			
			if (futuresOrOptions==null)
				futuresOrOptions = d.getFuturesOrOptions();
			
			if (callPut==null)
				callPut = d.getCallPut();
			
			// strike is supposed to be the same
			strikePrice = d.getStrikePrice();
			
			firstTradedPrice += (weight*d.getFirstTradedPrice());
			lastTradedPrice += (weight*d.getLastTradedPrice());
			
			// if weight is positive, we use the original, or we use the opposite otherwise.
			maxTradedPrice += (weight>=0? weight*d.getMaxTradedPrice() : weight * d.getMinTradedPrice());
			minTradedPrice += (weight>=0? weight*d.getMinTradedPrice() : weight * d.getMaxTradedPrice());
			
			// these are just approximations.
			averagedPrice += (weight*d.getAveragedPrice());
			tradedVolume += (weight*d.getTradedVolume());
			
		}

		return new HkDerivativesConsolidatedData(timestamp, expiryMonth, underlying, strikePrice, futuresOrOptions, callPut,
				firstTradedPrice, lastTradedPrice, maxTradedPrice, minTradedPrice, averagedPrice, tradedVolume);
		
	}

}
