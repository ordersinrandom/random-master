package com.jbp.randommaster.datasource.historical;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkexTRFileSourceTests extends TestCase {
	
	
	@Test
	public void testReadLinesNoFiltering() {

		StringBuilder buf=new StringBuilder(1000);
		buf.append("HHI,F,1211,0,,20121031,161458,10607,1,001\n");
		buf.append("HHI,O,1211,8400,P,20121031,161459,2,5,001\n");
		buf.append("MHI,O,1211,22400,C,20121031,161459,110,1,001");
		
		StringReader sr=new StringReader(buf.toString());
		
		HkexTRFileSource src=new HkexTRFileSource(sr);
		try {
			Collection<HkexTRFileData> data=src.getData();
			Assert.assertEquals("Not exactly 3 items", 3, data.size());

			Iterator<HkexTRFileData> it=data.iterator();
			HkexTRFileData i1=it.next();
			HkexTRFileData i2=it.next();
			HkexTRFileData i3=it.next();
			
			Assert.assertEquals("item 1 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,58), i1.getTimestamp());
			Assert.assertEquals("item 1 class code mismatched", "HHI", i1.getData().getClassCode());
			Assert.assertEquals("item 1 futures/options mismatched", "F", i1.getData().getFuturesOrOptions());
			Assert.assertEquals("item 1 expiry month mismatched", new YearMonth(2012, 11), i1.getData().getExpiryMonth());
			Assert.assertEquals("item 1 strike mismatched", 0.0, i1.getData().getStrikePrice(), 0.000001);
			Assert.assertEquals("item 1 call/put mismatched", "", i1.getData().getCallPut());
			Assert.assertEquals("item 1 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,58), i1.getData().getTradeTimestamp());
			Assert.assertEquals("item 1 price mismatched", 10607.0, i1.getData().getPrice(), 0.000001);
			Assert.assertEquals("item 1 quantity mismatched", 1.0, i1.getData().getQuantity(), 0.000001);
			Assert.assertEquals("item 1 trade type mismatched", "001", i1.getData().getTradeType());
			

			Assert.assertEquals("item 2 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i2.getTimestamp());
			Assert.assertEquals("item 2 class code mismatched", "HHI", i2.getData().getClassCode());
			Assert.assertEquals("item 2 futures/options mismatched", "O", i2.getData().getFuturesOrOptions());
			Assert.assertEquals("item 2 expiry month mismatched", new YearMonth(2012, 11), i2.getData().getExpiryMonth());
			Assert.assertEquals("item 2 strike mismatched", 8400.0, i2.getData().getStrikePrice(), 0.000001);
			Assert.assertEquals("item 2 call/put mismatched", "P", i2.getData().getCallPut());
			Assert.assertEquals("item 2 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i2.getData().getTradeTimestamp());
			Assert.assertEquals("item 2 price mismatched", 2.0, i2.getData().getPrice(), 0.000001);
			Assert.assertEquals("item 2 quantity mismatched", 5.0, i2.getData().getQuantity(), 0.000001);
			Assert.assertEquals("item 2 trade type mismatched", "001", i2.getData().getTradeType());
			
			Assert.assertEquals("item 3 timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i3.getTimestamp());
			Assert.assertEquals("item 3 class code mismatched", "MHI", i3.getData().getClassCode());
			Assert.assertEquals("item 3 futures/options mismatched", "O", i3.getData().getFuturesOrOptions());
			Assert.assertEquals("item 3 expiry month mismatched", new YearMonth(2012, 11), i3.getData().getExpiryMonth());
			Assert.assertEquals("item 3 strike mismatched", 22400.0, i3.getData().getStrikePrice(), 0.000001);
			Assert.assertEquals("item 3 call/put mismatched", "C", i3.getData().getCallPut());
			Assert.assertEquals("item 3 trade timestamp mismatched", new LocalDateTime(2012, 10, 31, 16,14,59), i3.getData().getTradeTimestamp());
			Assert.assertEquals("item 3 price mismatched", 110.0, i3.getData().getPrice(), 0.000001);
			Assert.assertEquals("item 3 quantity mismatched", 1.0, i3.getData().getQuantity(), 0.000001);
			Assert.assertEquals("item 3 trade type mismatched", "001", i3.getData().getTradeType());
			
			
		} catch (Exception e1) {
			Assert.fail("unable to getData(), exception msg = "+e1.getMessage());
		}
		
	}
	

}