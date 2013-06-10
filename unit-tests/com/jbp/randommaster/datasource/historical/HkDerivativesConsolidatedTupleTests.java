package com.jbp.randommaster.datasource.historical;

import java.util.LinkedList;

import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.VanillaDerivativesDataTuple.CallPut;
import com.jbp.randommaster.datasource.historical.VanillaDerivativesDataTuple.FuturesOptions;

import junit.framework.TestCase;

public class HkDerivativesConsolidatedTupleTests extends TestCase {

	@Test
	public void testEmptyIterableCase() {
		LinkedList<HkDerivativesTRTuple> tuples=new LinkedList<HkDerivativesTRTuple>();
		HkDerivativesConsolidatedTuple result=HkDerivativesConsolidatedTuple.consolidate(tuples);
		Assert.assertNull("Result is not null even pass in tuples are empty", result);
	}

	@Test
	public void testSingleEntry() {
		
		LinkedList<HkDerivativesTRTuple> tuples=new LinkedList<HkDerivativesTRTuple>();
		tuples.add(HkDerivativesTRTuple.parse("MHI,O,1211,20000,C,20121130,161301,1662,1,001"));
		
		HkDerivativesConsolidatedTuple result=HkDerivativesConsolidatedTuple.consolidate(tuples);
		
		HkDerivativesConsolidatedTuple expected= new HkDerivativesConsolidatedTuple(new YearMonth(2012,11), "MHI", 20000.0, 
															FuturesOptions.OPTIONS, CallPut.CALL,
															1662.0, 1662.0, 1662.0, 
															1662.0, 1.0, 1);
		
		Assert.assertEquals("Result object not matching expected", expected, result);
	}
	
	@Test
	public void testThreeEntriesConsolidations() {
		
		LinkedList<HkDerivativesTRTuple> tuples=new LinkedList<HkDerivativesTRTuple>();
		tuples.add(HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161501,21662,1,001"));
		tuples.add(HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161501,21663,2,001"));
		tuples.add(HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161501,21664,1,001"));
		
		HkDerivativesConsolidatedTuple result=HkDerivativesConsolidatedTuple.consolidate(tuples);
		
		HkDerivativesConsolidatedTuple expected= new HkDerivativesConsolidatedTuple(new YearMonth(2012,11), "MHI", 0.0, 
															FuturesOptions.FUTURES, CallPut.NA,
															21664.0, 21664.0, 21662.0, 
															21663.0, 4.0, 3);
		
		Assert.assertEquals("Result object not matching expected", expected, result);
		
	}
	
	
	@Test
	public void testUnderlyingMismatched() {
		
		LinkedList<HkDerivativesTRTuple> tuples=new LinkedList<HkDerivativesTRTuple>();
		tuples.add(HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161501,21662,1,001"));
		tuples.add(HkDerivativesTRTuple.parse("HSI,F,1211,0,,20121031,161501,21663,2,001"));
		tuples.add(HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161501,21664,1,001"));
		
		try {
			HkDerivativesConsolidatedTuple.consolidate(tuples);
			Assert.fail("No exception thrown even the input underlying is mismatched");
			
		} catch (Exception e1) {
			
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			Assert.assertEquals("Exception message mismatched", "input data doesn't have the same underlying: current=MHI, input=HSI", e1.getMessage());
			
		}
		
	}
	
	@Test
	public void testToString() {
		HkDerivativesConsolidatedTuple r= new HkDerivativesConsolidatedTuple(new YearMonth(2012,11), "MHI", 0.0, 
				FuturesOptions.FUTURES, CallPut.NA,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0, 3);
		
		String expectedStr="HkDerivativesConsolidatedTuple { expiryMonth=2012-11, underlying=MHI, strikePrice=0.0, futuresOrOptions=FUTURES, callPut=NA, lastTradedPrice=21664.0, maxTradedPrice=21664.0, minTradedprice=21662.0, averagedPrice=21663.0, tradedVolume=4.0, transactionsCount=3 }";
		Assert.assertEquals("toString() output mismatched", expectedStr, r.toString());
	}
		
		
}
