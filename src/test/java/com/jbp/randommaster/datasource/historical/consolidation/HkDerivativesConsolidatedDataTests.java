package com.jbp.randommaster.datasource.historical.consolidation;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.consolidation.HkDerivativesConsolidatedData;

import junit.framework.TestCase;

public class HkDerivativesConsolidatedDataTests extends TestCase {

	@Test
	public void testToString() {
		
		LocalDateTime tradeDateTime=LocalDateTime.of(2012,10,31, 16,15, 1);
		
		HkDerivativesConsolidatedData r= new HkDerivativesConsolidatedData(tradeDateTime, YearMonth.of(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);
		
		String expectedStr="HkDerivativesConsolidatedData { timestamp=2012-10-31T16:15:01, expiryMonth=2012-11, underlying=MHI, strikePrice=0.0, futuresOrOptions=F, callPut=, firstTradedPrice=21660.0, lastTradedPrice=21664.0, maxTradedPrice=21664.0, minTradedprice=21662.0, averagedPrice=21663.0, tradedVolume=4.0 }";
		Assert.assertEquals("toString() output mismatched", expectedStr, r.toString());
	}
	
	@Test
	public void testEqualsAndHashcode() {

		HkDerivativesConsolidatedData r1= new HkDerivativesConsolidatedData(LocalDateTime.of(2012,10,31, 16,15, 1), YearMonth.of(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);
		
		HkDerivativesConsolidatedData r2= new HkDerivativesConsolidatedData(LocalDateTime.of(2012,10,31, 16,15, 1), YearMonth.of(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);

		HkDerivativesConsolidatedData r3= new HkDerivativesConsolidatedData(LocalDateTime.of(2012,10,31, 16,15, 1), YearMonth.of(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21664.0, 21664.0, 21662.0, 
				21663.0, 5.0);
		
		Assert.assertEquals("same object equals() is not true", true, r1.equals(r2));
		Assert.assertEquals("object vs null equals() is true", false, r1.equals(null));
		Assert.assertEquals("different object equals() is true", false, r1.equals(r3));
		Assert.assertEquals("same object produces different hash code", true, r1.hashCode()==r2.hashCode());
		
	}
			
}
