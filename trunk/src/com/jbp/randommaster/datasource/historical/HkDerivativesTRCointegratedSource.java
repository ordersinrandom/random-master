package com.jbp.randommaster.datasource.historical;

import org.javatuples.Pair;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

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
			else if (!expiryMonth.equals(d.getExpiryMonth()))
				throw new IllegalArgumentException("expiryMonth mismatched: "+expiryMonth+", and "+d.getExpiryMonth()+", for input "+d);
			
			if (underlying==null)
				underlying = d.getUnderlying();
			else if (!underlying.equals(d.getUnderlying()))
				throw new IllegalArgumentException("underlying mismatched: "+underlying+", and "+d.getUnderlying()+", for input "+d);
			
			if (futuresOrOptions==null)
				futuresOrOptions = d.getFuturesOrOptions();
			else if (!futuresOrOptions.equals(d.getFuturesOrOptions()))
				throw new IllegalArgumentException("futuresOrOptions mismatched: "+futuresOrOptions+", and "+d.getFuturesOrOptions()+", for input "+d);
			
			if (callPut==null)
				callPut = d.getCallPut();
			else if (!callPut.equals(d.getCallPut()))
				throw new IllegalArgumentException("callPut mismatched: "+callPut+", and "+d.getCallPut()+", for input "+d);
			
			// strike is supposed to be the same
			strikePrice = d.getStrikePrice();
			
			
			firstTradedPrice += (weight*d.getFirstTradedPrice());
			lastTradedPrice += (weight*d.getLastTradedPrice());
			maxTradedPrice += (weight*d.getMaxTradedPrice());
			minTradedPrice += (weight*d.getMinTradedPrice());
			averagedPrice += (weight*d.getAveragedPrice());
			tradedVolume += (weight*d.getTradedVolume());
			
		}

		return new HkDerivativesConsolidatedData(timestamp, expiryMonth, underlying, strikePrice, futuresOrOptions, callPut,
				firstTradedPrice, lastTradedPrice, maxTradedPrice, minTradedPrice, averagedPrice, tradedVolume);
		
	}

}
