package com.jbp.randommaster.datasource.historical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.junit.Test;

import com.jbp.randommaster.datasource.historical.HkDerivativesTRTradeTypeFilter.TradeType;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CointegratedTRSourceTests extends TestCase {

	@Test
	public void testHkDerivativesTRCointegratedSourceCase1() throws IOException {
		
		
		StringBuilder buf=new StringBuilder(1000);
		buf.append("MHI,F,1211,0,,20121031,153405,21630,1,001\n");
		buf.append("MCH,F,1211,0,,20121031,153404,10211,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154104,21658,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154401,21660,1,001\n");
		buf.append("MCH,F,1211,0,,20121031,154401,10210,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154901,21662,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155201,21668,3,001\n");
		buf.append("MHI,F,1211,0,,20121031,155301,21656,1,001\n");
		buf.append("MCH,F,1211,0,,20121031,155400,10200,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155500,21670,3,001\n");
		buf.append("MHI,F,1211,0,,20121031,155701,21640,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155901,21672,1,001\n");
		buf.append("MCH,F,1211,0,,20121031,155902,10204,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,160000,21699,5,001\n");
		
		
		File tempFile=File.createTempFile("TestHkDerivativesTRCointegratedSourceCase1", null);
		tempFile.deleteOnExit();
		FileWriter fw=new FileWriter(tempFile);
		fw.write(buf.toString());
		fw.close();
		
		String tempFilename=tempFile.getAbsolutePath();		
		

		int frequencySeconds = 180;
		// we consolidated by number of seconds.
		Period interval = new Period(0, 0, frequencySeconds, 0);
		
		
		LocalDate tradeDate = new LocalDate(2012, 10, 31);
		LocalDateTime start = new LocalDateTime(
				tradeDate.getYear(),tradeDate.getMonthOfYear(),tradeDate.getDayOfMonth(), 15, 34, 0);
		LocalDateTime end = new LocalDateTime(
				tradeDate.getYear(),tradeDate.getMonthOfYear(),tradeDate.getDayOfMonth(), 16, 01, 0);
				

		CointegratedTRSource<HkDerivativesConsolidatedData> cointegratedSrc = new HkDerivativesTRCointegratedSource();

		double[] weight = new double[] { 1.0, -2.0 };
		String[] underlying = new String[] { "MHI", "MCH" };
		
		
		for (int i=0;i<weight.length;i++) {
			
			YearMonth expiryMonth = new YearMonth(2012,11);
			
			try (
					HkDerivativesTRFileSource originalSrc = new HkDerivativesTRFileSource(tempFilename);
					
					// filtered by underlying
					FilteredHistoricalDataSource<HkDerivativesTR> underlyingFilteredSource = 
							new FilteredHistoricalDataSource<HkDerivativesTR>(
									originalSrc, new UnderlyingFilter<HkDerivativesTR>(underlying[i]));
					
					// filtered by Futures contract
					FilteredHistoricalDataSource<HkDerivativesTR> futuresFilteredSource = 
							new FilteredHistoricalDataSource<HkDerivativesTR>(
									underlyingFilteredSource, new FuturesFilter<HkDerivativesTR>());
					
					// filtered by expiry month
					FilteredHistoricalDataSource<HkDerivativesTR> expMonthFilteredSource = 
							new FilteredHistoricalDataSource<HkDerivativesTR>(
									futuresFilteredSource, new ExpiryMonthFilter<HkDerivativesTR>(expiryMonth));
					
					// filtered by trade type (Normal)
					FilteredHistoricalDataSource<HkDerivativesTR> filteredSource =
							new FilteredHistoricalDataSource<HkDerivativesTR>(
							expMonthFilteredSource, new HkDerivativesTRTradeTypeFilter(TradeType.Normal)); 
					) {
				
		
				// consolidated source.
				TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR> consolidatedSrc 
					= new TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR>(
							new HkDerivativesTRConsolidator(), filteredSource, start, end, interval);		
				

				cointegratedSrc.addSource(consolidatedSrc, weight[i]);
			
			} 
		}
		
		
		String expectedUnderlying = "1.0*MHI -2.0*MCH";
		
		HkDerivativesConsolidatedData[] expected = new HkDerivativesConsolidatedData[] {
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,37,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1208.0, 1208.0, 1208.0, 1208.0, 
						1208.0, -1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,40,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1208.0, 1208.0, 1208.0, 1208.0, 
						1208.0, 0.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,43,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1236.0, 1236.0, 1236.0, 1236.0, 
						1236.0, 1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,46,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1240.0, 1240.0, 1240.0, 1240.0, 
						1240.0, -1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,49,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1240.0, 1240.0, 1240.0, 1240.0, 
						1240.0, 0.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,52,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1242.0, 1242.0, 1242.0, 1242.0, 
						1242.0, 1.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,55,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1268.0, 1256.0, 1268.0, 1256.0, 
						1265.0, 2.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,58,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1270.0, 1240.0, 1270.0, 1240.0, 
						1262.5, 4.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,16,01,0), 
						new YearMonth(2012,11), expectedUnderlying, 0.0, "F", "",
						1264.0, 1291.0, 1291.0, 1264.0, 
						1286.5, 4.0)						
				
		};		
		
		

		// first count the items.
		int count = 0;
		for (Iterator<? extends ConsolidatedTradeRecordsData> it = cointegratedSrc.getData().iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		
		Assert.assertEquals("Not expected number of items output", expected.length, count);		
		
		if (count==expected.length) {
			int index=0;		
			// actual matching
			for (HkDerivativesConsolidatedData d: cointegratedSrc.getData()) {

				Assert.assertEquals("Item "+index+" mismatched", expected[index], d);
				index++;				
				
			}		
		}
		
	}
	
}
