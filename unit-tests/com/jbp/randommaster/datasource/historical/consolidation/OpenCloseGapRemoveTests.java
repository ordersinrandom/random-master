package com.jbp.randommaster.datasource.historical.consolidation;

import java.util.LinkedList;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class OpenCloseGapRemoveTests extends TestCase {

	@Test
	public void testInterDayCase() {
		
		HkDerivativesConsolidatedData r1= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 11,58, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21664.0, 21664.0, 21662.0, 
				21663.0, 4.0);
		// intraday dropped 4 point
		HkDerivativesConsolidatedData r2= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 16,00, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21660.0,
				21660.0, 21664.0, 21662.0, 
				21663.0, 4.0);

		// across day +110
		
		HkDerivativesConsolidatedData r3= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 9,30, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21760.0,
				21770.0, 21770.0, 21760.0, 
				21763.0, 5.0);		

		// intraday droped 200
		HkDerivativesConsolidatedData r4= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 15,59, 58), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21560.0,
				21570.0, 21570.0, 21560.0, 
				21563.0, 5.0);		

		// across day -300
		HkDerivativesConsolidatedData r5= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,02, 9,31, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21260.0,
				21270.0, 21270.0, 21260.0, 
				21263.0, 5.0);		

		
		
		
		LinkedList<HkDerivativesConsolidatedData> timeSeries = new LinkedList<>();
		timeSeries.add(r1);
		timeSeries.add(r2);
		timeSeries.add(r3);
		timeSeries.add(r4);
		timeSeries.add(r5);
		
		LocalTime dayOpen = new LocalTime(9,30);
		LocalTime dayClose = new LocalTime(16,30);
		OpenCloseGapRemover<HkDerivativesConsolidatedData> remover = new OpenCloseGapRemover<>(dayOpen, dayClose);
		
		Iterable<GapAdjustedTradeRecord<HkDerivativesConsolidatedData>> resultList = remover.removeGaps(timeSeries);
		
		double[] correctLast = new double [] { 21474, 21470, 21470, 21270, 21270 };
		double[] correctGap = new double[] { -190, -190, -300, -300, 0 };
		
		double delta = 0.000001;
		int i = 0;
		for (GapAdjustedTradeRecord<HkDerivativesConsolidatedData> result : resultList) {
			
			//System.out.println(result.getLastTradedPrice()+", gap = "+result.getGapAdjustment());
			Assert.assertEquals("r"+i+" last traded price incorrect", correctLast[i], result.getLastTradedPrice(), delta);
			Assert.assertEquals("r"+i+" gap is incorrect", correctGap[i], result.getGapAdjustment(), delta);
			
			i++;
		}
		
	}

	
	
	@Test
	public void testIntraDayCase() {
		
		HkDerivativesConsolidatedData r1= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 9,30, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21664.0,
				21664.0, 21664.0, 21664.0, 
				21664.0, 4.0);
		// dropped 10 points in the morning
		HkDerivativesConsolidatedData r2= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 11,58, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21654.0,
				21654.0, 21654.0, 21654.0, 
				21654.0, 4.0);
		// mid day raised 100
		HkDerivativesConsolidatedData r3= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 13,30, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21754.0,
				21754.0, 21754.0, 21754.0, 
				21754.0, 4.0);
		// dropped 50 in the afternoon
		HkDerivativesConsolidatedData r4= new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31, 15,59, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21704.0,
				21704.0, 21704.0, 21704.0, 
				21704.0, 4.0);
		
		// across day +110
		
		HkDerivativesConsolidatedData r5= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 9,30, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21814.0,
				21814.0, 21814.0, 21814.0, 
				21814.0, 5.0);		

		// morning dropped 10
		HkDerivativesConsolidatedData r6= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 12,00, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21804.0,
				21804.0, 21804.0, 21804.0, 
				21804.0, 5.0);		

		// mid day raised 40
		HkDerivativesConsolidatedData r7= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 13,30, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21844.0,
				21844.0, 21844.0, 21844.0, 
				21844.0, 5.0);		

		// afternoon raised 60
		HkDerivativesConsolidatedData r8= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,01, 16,0, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21904.0,
				21904.0, 21904.0, 21904.0, 
				21904.0, 5.0);		
		

		// across day -300
		HkDerivativesConsolidatedData r9= new HkDerivativesConsolidatedData(new LocalDateTime(2012,11,02, 9,31, 0), new YearMonth(2012,11), "MHI", 0.0, 
				"F", "",
				21604.0,
				21604.0, 21604.0, 21604.0, 
				21604.0, 5.0);		

		
		
		
		LinkedList<HkDerivativesConsolidatedData> timeSeries = new LinkedList<>();
		timeSeries.add(r1);
		timeSeries.add(r2);
		timeSeries.add(r3);
		timeSeries.add(r4);
		timeSeries.add(r5);
		timeSeries.add(r6);
		timeSeries.add(r7);
		timeSeries.add(r8);
		timeSeries.add(r9);
		
		LocalTime dayOpen = new LocalTime(9,30);
		LocalTime dayClose = new LocalTime(16,30);
		OpenCloseGapRemover<HkDerivativesConsolidatedData> remover = new OpenCloseGapRemover<>(dayOpen, dayClose);
		remover.addIntradayClosePeriod(new LocalTime(12, 0), new LocalTime(13, 30));
		
		Iterable<GapAdjustedTradeRecord<HkDerivativesConsolidatedData>> resultList = remover.removeGaps(timeSeries);
		
		double[] correctLast = new double [] { 21614, 21604, 21604, 21554, 21554, 21544, 21544, 21604, 21604 };
		double[] correctGap = new double[] { -50, -50, -150, -150, -260, -260, -300, -300, 0 };
		
		double delta = 0.000001;
		int i = 0;
		for (GapAdjustedTradeRecord<HkDerivativesConsolidatedData> result : resultList) {
			
			//System.out.println(result.getLastTradedPrice()+", gap = "+result.getGapAdjustment());
			
			Assert.assertEquals("r"+i+" last traded price incorrect", correctLast[i], result.getLastTradedPrice(), delta);
			Assert.assertEquals("r"+i+" gap is incorrect", correctGap[i], result.getGapAdjustment(), delta);
			
			i++;
		}
		
	}
	
}
