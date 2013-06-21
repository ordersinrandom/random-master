package com.jbp.randommaster.datasource.historical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TimeIntervalConsolidatedTRSourceTests extends TestCase {

	@Test
	public void testConsolidations1() throws IOException {

		
		StringBuilder buf=new StringBuilder(1000);
		buf.append("MHI,F,1211,0,,20121031,153405,21630,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154104,21658,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154401,21660,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,154901,21662,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155201,21668,3,001\n");
		buf.append("MHI,F,1211,0,,20121031,155301,21656,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155500,21670,3,001\n");
		buf.append("MHI,F,1211,0,,20121031,155701,21640,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,155901,21672,1,001\n");
		buf.append("MHI,F,1211,0,,20121031,160000,21699,5,001\n");
		
		
		File tempFile=File.createTempFile("TimeIntervalConsolidatedTRSourceTests", null);
		tempFile.deleteOnExit();
		FileWriter fw=new FileWriter(tempFile);
		fw.write(buf.toString());
		fw.close();
		
		String tempFilename=tempFile.getAbsolutePath();
		
		
		HkDerivativesTRConsolidator con = new HkDerivativesTRConsolidator();
		HkDerivativesTRFileSource src=new HkDerivativesTRFileSource(tempFilename);		
		
		LocalDateTime start = new LocalDateTime(2012,10,31,15, 20, 0);
		LocalDateTime end = new LocalDateTime(2012,10,31,16, 00, 0);
		Period interval = new Period(0, 5, 0, 0);
		HistoricalDataSource<? extends ConsolidatedTradeRecordsData> consolidatedSrc
			= new TimeIntervalConsolidatedTRSource<HkDerivativesConsolidatedData, HkDerivativesTR>(con, src, start, end, interval);

		
		HkDerivativesConsolidatedData[] expected = new HkDerivativesConsolidatedData[] {
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,25,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21630.0, 21630.0, 21630.0, 21630.0, 
						21630.0, 0.0),
				new HkDerivativesConsolidatedData(new LocalDateTime(2012,10,31,15,30,0), 
						new YearMonth(2012,11), "MHI", 0.0, "F", "",
						21630.0, 21630.0, 21630.0, 21630.0, 
						21630.0, 0.0),
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
		
		// first count the items.
		int count = 0;
		for (Iterator<? extends ConsolidatedTradeRecordsData> it = consolidatedSrc.getData().iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		
		Assert.assertEquals("Not expected number of items output", expected.length, count);
		
		if (count==expected.length) {
			int index=0;
			for (ConsolidatedTradeRecordsData d : consolidatedSrc.getData()) {
				Assert.assertEquals("Item "+index+" mismatched", expected[index], d);
				index++;
			}
		}

			
	}
	
}
