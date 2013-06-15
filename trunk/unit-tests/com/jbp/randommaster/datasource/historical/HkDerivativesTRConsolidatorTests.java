package com.jbp.randommaster.datasource.historical;

import java.util.LinkedList;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class HkDerivativesTRConsolidatorTests extends TestCase {

	@Test
	public void testEmptyIterableCase() {
		LinkedList<HkDerivativesTR> original=new LinkedList<HkDerivativesTR>();
		LocalDateTime tradeDateTime=new LocalDateTime(2012,11,30, 16,13, 1);
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesConsolidatedData result = con.consolidate(tradeDateTime, original);
		
		
		Assert.assertNull("Result is not null even pass in tuples are empty", result);
	}

	@Test
	public void testSingleEntry() {
		
		LinkedList<HkDerivativesTR> tuples=new LinkedList<HkDerivativesTR>();
		tuples.add(HkDerivativesTR.parse("MHI,O,1211,20000,C,20121130,161301,1662,1,001"));
		
		LocalDateTime tradeDateTime=new LocalDateTime(2012,11,30, 16,13, 1);
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesConsolidatedData result = con.consolidate(tradeDateTime, tuples);
		
		
		HkDerivativesConsolidatedData expected= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 20000.0, 
															"O", "C",
															1662.0,
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

		HkDerivativesConsolidatedData expected= new HkDerivativesConsolidatedData(tradeDateTime, new YearMonth(2012,11), "MHI", 0.0, 
															"F", "",
															21662.0,
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
			
			Assert.fail("No exception thrown even the input underlying is mismatched");
			
		} catch (Exception e1) {
			
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			Assert.assertEquals("Exception message mismatched", "input data series doesn't have the same underlying: current=MHI, input=HSI", e1.getMessage());
			
		}
		
	}
	


	@SuppressWarnings("unused")
	@Test
	public void testConsolidations1() {
		
		
		LinkedList<HkDerivativesTR> inputData=new LinkedList<HkDerivativesTR>();
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,153405,21630,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,154104,21658,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,154401,21660,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,154901,21662,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,155201,21668,3,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,155301,21656,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,155500,21670,3,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,155701,21640,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,155901,21672,1,001"));
		inputData.add(HkDerivativesTR.parse("MHI,F,1211,0,,20121031,160000,21699,5,001"));
		
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		
		LocalDateTime start = new LocalDateTime(2012,10,31,15, 30, 0);
		LocalDateTime end = new LocalDateTime(2012,10,31,16, 00, 0);
		Period interval = new Period(0, 5, 0, 0);
		Iterable<HkDerivativesConsolidatedData> result=con.consolidateByTimeIntervals(start, end, interval, inputData);
		
		
		HkDerivativesConsolidatedData[] expected = new HkDerivativesConsolidatedData[] {
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,35,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21630.0, 21630.0, 21630.0, 21630.0, 
						21630.0, 1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,40,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21630.0, 21630.0, 21630.0, 21630.0, 
						21630.0, 0.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,45,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21658.0, 21660.0, 21660.0, 21658.0, 
						21659.0, 2.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,50,0), 
								new YearMonth(2012,11), "MHI", 0.0, "F", "",
								21662.0, 21662.0, 21662.0, 21662.0, 
								21662.0, 1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,55,0), 
								new YearMonth(2012,11), "MHI", 0.0, "F", "",
								21668.0, 21656.0, 21668.0, 21656.0, 
								21665.0, 4.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,16,0,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21670.0, 21699.0, 21699.0, 21640.0, 
						21681.7, 10.0)						
		};
		
		int count=0;
		for (HkDerivativesConsolidatedData d : result) {
			//System.out.println(d);
			count++;
		}
		
		Assert.assertEquals("Number of output mismatched", expected.length, count);
		if (count==expected.length) {
			int i=0;
			for (HkDerivativesConsolidatedData d : result) {
				Assert.assertEquals("Output item "+i+" mismatched", expected[i], d);
				i++;
			}
		}
		
	}	
	
}
