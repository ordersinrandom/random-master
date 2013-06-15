package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.CallPut;
import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.FuturesOptions;

import junit.framework.TestCase;

public class HkDerivativesConsolidatedDataTests extends TestCase {

	@Test
	public void testToString() {
		
		LocalDateTime tradeDateTime=new LocalDateTime(2012,10,31, 16,15, 1);
		
		HkDerivativesConsolidatedData r= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
				FuturesOptions.FUTURES, CallPut.NA,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);
		
		String expectedStr="HkDerivativesConsolidatedData { timestamp=2012-10-31T16:15:01.000, expiryMonth=2012-11, underlying=MHI, strikePrice=0.0, futuresOrOptions=FUTURES, callPut=NA, lastTradedPrice=21664.0, maxTradedPrice=21664.0, minTradedprice=21662.0, averagedPrice=21663.0, tradedVolume=4.0 }";
		Assert.assertEquals("toString() output mismatched", expectedStr, r.toString());
	}
	
	@Test
	public void testEqualsAndHashcode() {

		LocalDateTime tradeDateTime=new LocalDateTime(2012,10,31, 16,15, 1);
		
		HkDerivativesConsolidatedData r1= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
				FuturesOptions.FUTURES, CallPut.NA,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);
		
		HkDerivativesConsolidatedData r2= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
				FuturesOptions.FUTURES, CallPut.NA,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);

		HkDerivativesConsolidatedData r3= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
				FuturesOptions.FUTURES, CallPut.NA,
				21664.0, 21664.0, 21662.0, 
				21663.0, 5.0);
		
		Assert.assertEquals("same object equals() is not true", true, r1.equals(r2));
		Assert.assertEquals("object vs null equals() is true", false, r1.equals(null));
		Assert.assertEquals("different object equals() is true", false, r1.equals(r3));
		Assert.assertEquals("same object produces different hash code", true, r1.hashCode()==r2.hashCode());
		
	}
			
}
