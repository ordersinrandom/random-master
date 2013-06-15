package com.jbp.randommaster.datasource.historical;

import java.util.LinkedList;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.CallPut;
import com.jbp.randommaster.datasource.historical.VanillaDerivativesData.FuturesOptions;

import junit.framework.TestCase;

public class HkDerivativesTRConsolidatorTests extends TestCase {

	@Test
	public void testEmptyIterableCase() {
		LinkedList<HkDerivativesTR> original=new LinkedList<HkDerivativesTR>();
		LocalDateTime tradeDateTime=new LocalDateTime(2012,11,30, 16,13, 1);
		//HkDerivativesConsolidatedData result=HkDerivativesConsolidatedData.consolidate(tradeDateTime, tuples);
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesConsolidatedData result = con.consolidate(tradeDateTime, original);
		
		//HkDerivativesConsolidatedData result
		
		Assert.assertNull("Result is not null even pass in tuples are empty", result);
	}

	@Test
	public void testSingleEntry() {
		
		LinkedList<HkDerivativesTR> tuples=new LinkedList<HkDerivativesTR>();
		tuples.add(HkDerivativesTR.parse("MHI,O,1211,20000,C,20121130,161301,1662,1,001"));
		
		LocalDateTime tradeDateTime=new LocalDateTime(2012,11,30, 16,13, 1);
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesConsolidatedData result = con.consolidate(tradeDateTime, tuples);
		
		//HkDerivativesConsolidatedData result=HkDerivativesConsolidatedData.consolidate(tradeDateTime, tuples);
		
		HkDerivativesConsolidatedData expected= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 20000.0, 
															FuturesOptions.OPTIONS, CallPut.CALL,
															1662.0, 1662.0, 1662.0, 
															1662.0, 1.0);
		
		Assert.assertEquals("Result object not matching expected", expected, result);
	}
	
	@Test
	public void testThreeEntriesConsolidations() {
		
		LocalDateTime tradeDateTime=new LocalDateTime(2012,10,31, 16,15, 1);
		
		LinkedList<HkDerivativesTR> tuples=new LinkedList<HkDerivativesTR>();
		tuples.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,161501,21662,1,001"));
		tuples.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,161501,21663,2,001"));
		tuples.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,161501,21664,1,001"));
		
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesConsolidatedData result = con.consolidate(tradeDateTime, tuples);

		//HkDerivativesConsolidatedData result=HkDerivativesConsolidatedData.consolidate(tradeDateTime, tuples);
		
		HkDerivativesConsolidatedData expected= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
															FuturesOptions.FUTURES, CallPut.NA,
															21664.0, 21664.0, 21662.0, 
															21663.0, 4.0);
		
		Assert.assertEquals("Result object not matching expected", expected, result);
		
	}
	
	
	@Test
	public void testUnderlyingMismatched() {
		
		LocalDateTime tradeDateTime=new LocalDateTime(2012,10,31, 16,15, 1);
		
		LinkedList<HkDerivativesTR> tuples=new LinkedList<HkDerivativesTR>();
		tuples.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,161501,21662,1,001"));
		tuples.add(HkDerivativesTR.parse("HSI,F,1211,0,,20121031,161501,21663,2,001"));
		tuples.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,161501,21664,1,001"));
		
		try {
			
			HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
			con.consolidate(tradeDateTime, tuples);
			
			//HkDerivativesConsolidatedData.consolidate(tradeDateTime, tuples);
			Assert.fail("No exception thrown even the input underlying is mismatched");
			
		} catch (Exception e1) {
			
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			Assert.assertEquals("Exception message mismatched", "input data doesn't have the same underlying: current=MHI, input=HSI", e1.getMessage());
			
		}
		
	}
	

		
}
