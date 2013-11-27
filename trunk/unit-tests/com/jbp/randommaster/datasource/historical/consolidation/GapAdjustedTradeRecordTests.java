package com.jbp.randommaster.datasource.historical.consolidation;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

public class GapAdjustedTradeRecordTests extends TestCase {

	@Test
	public void testAdjustment() {

		LocalDateTime timestamp = new LocalDateTime(2012, 10, 31, 16, 01, 0);
		YearMonth expiry = new YearMonth(2012, 11);
		String underlying = "HSI";
		double strike = 0;
		String futuresOptions = "F";
		String callPut="";
		double firstTradedPrice = 1264.0; 
		double lastTradedPrice = 1291.0;
		double maxTradedPrice = 1291.0;
		double minTradedPrice = 1264.0;
		double averagedPrice = 1286.5;
		double tradedVolume = 4.0;
		
		
		
		HkDerivativesConsolidatedData data = new HkDerivativesConsolidatedData(timestamp, expiry,
				underlying, strike, futuresOptions, callPut, firstTradedPrice, lastTradedPrice, maxTradedPrice, minTradedPrice, 
				averagedPrice, tradedVolume);

		double gap = 200;
		
		AccruedGapsTracker tracker = new AccruedGapsTracker();
		tracker.adjust(gap);
		
		GapAdjustedTradeRecord<HkDerivativesConsolidatedData> ga = new GapAdjustedTradeRecord<>(data, tracker);
		
		double delta = 0.00000001;
		Assert.assertEquals("Traded Volume mismatched", tradedVolume, ga.getTradedVolume(), delta);
		Assert.assertEquals("First Traded Price mismatced", firstTradedPrice + gap, ga.getFirstTradedPrice(), delta);
		Assert.assertEquals("Last traded price mismatched", lastTradedPrice + gap, ga.getLastTradedPrice(), delta);
		Assert.assertEquals("Max traded price mismatched", maxTradedPrice + gap, ga.getMaxTradedPrice(), delta);
		Assert.assertEquals("Min traded price mismatched", minTradedPrice + gap, ga.getMinTradedPrice(), delta);
		Assert.assertEquals("Averaged Price mismatched", averagedPrice + gap, ga.getAveragedPrice(), delta);
		
		double gap2 = -150;
		
		tracker.adjust(gap2);
		
		Assert.assertEquals("Traded Volume mismatched", tradedVolume, ga.getTradedVolume(), delta);
		Assert.assertEquals("First Traded Price mismatced", firstTradedPrice + gap + gap2, ga.getFirstTradedPrice(), delta);
		Assert.assertEquals("Last traded price mismatched", lastTradedPrice + gap + gap2, ga.getLastTradedPrice(), delta);
		Assert.assertEquals("Max traded price mismatched", maxTradedPrice + gap + gap2, ga.getMaxTradedPrice(), delta);
		Assert.assertEquals("Min traded price mismatched", minTradedPrice + gap + gap2, ga.getMinTradedPrice(), delta);
		Assert.assertEquals("Averaged Price mismatched", averagedPrice + gap + gap2, ga.getAveragedPrice(), delta);
		
	}

}
