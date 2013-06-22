package com.jbp.randommaster.datasource.historical;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRFileSourceTests extends TestCase {
	
	
	@Test
	public void testReadLinesNoFiltering() {

		StringBuilder buf=new StringBuilder(1000);
		buf.append("HHI,F,1211,0,,20121031,161458,10607,1,001\n");
		buf.append("HHI,O,1211,8400,P,20121031,161459,2,5,001\n");
		buf.append("MHI,O,1211,22400,C,20121031,161459,110,1,001");
		
		File tempFile=null;
		try {
			tempFile=File.createTempFile("HkexTRFileSourceTests", null);
			FileWriter fw=new FileWriter(tempFile);
			fw.write(buf.toString());
			fw.close();
			
			String tempFilename=tempFile.getAbsolutePath();
			
			List<HkDerivativesTR> r = new LinkedList<HkDerivativesTR>();

			try (HkDerivativesTRFileSource src=new HkDerivativesTRFileSource(tempFilename)) {
				Iterable<HkDerivativesTR> data=src.getData();
				for (HkDerivativesTR d : data)
					r.add(d);
			}
			
			Assert.assertEquals("Not exactly 3 items", 3, r.size());

			HkDerivativesTR i1=r.get(0);
			HkDerivativesTR i2=r.get(1);
			HkDerivativesTR i3=r.get(2);
			
			Assert.assertEquals("item 1 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,58), i1.getTimestamp());
			Assert.assertEquals("item 1 class code mismatched", "HHI", i1.getUnderlying());
			Assert.assertEquals("item 1 futures/options mismatched", "F", i1.getFuturesOrOptions());
			Assert.assertEquals("item 1 expiry month mismatched", new YearMonth(2012, 11), i1.getExpiryMonth());
			Assert.assertEquals("item 1 strike mismatched", 0.0, i1.getStrikePrice(), 0.000001);
			Assert.assertEquals("item 1 call/put mismatched", "", i1.getCallPut());
			Assert.assertEquals("item 1 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,58), i1.getTradeTimestamp());
			Assert.assertEquals("item 1 price mismatched", 10607.0, i1.getPrice(), 0.000001);
			Assert.assertEquals("item 1 quantity mismatched", 1.0, i1.getQuantity(), 0.000001);
			Assert.assertEquals("item 1 trade type mismatched", "001", i1.getTradeType());
			

			Assert.assertEquals("item 2 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i2.getTimestamp());
			Assert.assertEquals("item 2 class code mismatched", "HHI", i2.getUnderlying());
			Assert.assertEquals("item 2 futures/options mismatched", "O", i2.getFuturesOrOptions());
			Assert.assertEquals("item 2 expiry month mismatched", new YearMonth(2012, 11), i2.getExpiryMonth());
			Assert.assertEquals("item 2 strike mismatched", 8400.0, i2.getStrikePrice(), 0.000001);
			Assert.assertEquals("item 2 call/put mismatched", "P", i2.getCallPut());
			Assert.assertEquals("item 2 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i2.getTradeTimestamp());
			Assert.assertEquals("item 2 price mismatched", 2.0, i2.getPrice(), 0.000001);
			Assert.assertEquals("item 2 quantity mismatched", 5.0, i2.getQuantity(), 0.000001);
			Assert.assertEquals("item 2 trade type mismatched", "001", i2.getTradeType());
			
			Assert.assertEquals("item 3 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i3.getTimestamp());
			Assert.assertEquals("item 3 class code mismatched", "MHI", i3.getUnderlying());
			Assert.assertEquals("item 3 futures/options mismatched", "O", i3.getFuturesOrOptions());
			Assert.assertEquals("item 3 expiry month mismatched", new YearMonth(2012, 11), i3.getExpiryMonth());
			Assert.assertEquals("item 3 strike mismatched", 22400.0, i3.getStrikePrice(), 0.000001);
			Assert.assertEquals("item 3 call/put mismatched", "C", i3.getCallPut());
			Assert.assertEquals("item 3 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i3.getTradeTimestamp());
			Assert.assertEquals("item 3 price mismatched", 110.0, i3.getPrice(), 0.000001);
			Assert.assertEquals("item 3 quantity mismatched", 1.0, i3.getQuantity(), 0.000001);
			Assert.assertEquals("item 3 trade type mismatched", "001", i3.getTradeType());
			
			
		} catch (Exception e1) {
			Assert.fail("unable to getData(), exception msg = "+e1.getMessage());
		} finally {
			if (tempFile!=null) {
				try {
					tempFile.delete();
				} catch (Exception e1) {
					Assert.fail("unable to delete temp file during the unit test: "+e1.getMessage());
				}
			}
		}
		
	}
	

}
