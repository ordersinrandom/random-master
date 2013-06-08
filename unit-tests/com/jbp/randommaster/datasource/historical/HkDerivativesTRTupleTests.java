package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRTupleTests extends TestCase {

	
	@Test
	public void testParsing1() {
		
		HkDerivativesTRTuple t=HkDerivativesTRTuple.parse("HSI,O,1212,25000,C,20121003,091524,7,20,001");

		Assert.assertEquals("Class code doesn't match", "HSI", t.getClassCode());
		Assert.assertEquals("Futures/Options flag doesn't match", "O", t.getFuturesOrOptions());
		Assert.assertEquals("Expiry Month doesn't match", new YearMonth(2012, 12), t.getExpiryMonth());
		Assert.assertEquals("Strike doesn't match", 25000.0, t.getStrikePrice(), 0.000001);
		Assert.assertEquals("Call/Put flag doesn't match", "C", t.getCallPut());
		Assert.assertEquals("trade timestamp doesn't match", new LocalDateTime(2012,10,3, 9, 15, 24), t.getTradeTimestamp());
		Assert.assertEquals("Price doesn't match", 7.0, t.getPrice(), 0.000001);
		Assert.assertEquals("Quantity doesn't match", 20.0, t.getQuantity(), 0.000001);
		Assert.assertEquals("Trade type doesn't match", "001", t.getTradeType());
		
	}
	
	@Test
	public void testParsing2() {
		HkDerivativesTRTuple t=HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161459,21662,1,001");

		Assert.assertEquals("Class code doesn't match", "MHI", t.getClassCode());
		Assert.assertEquals("Futures/Options flag doesn't match", "F", t.getFuturesOrOptions());
		Assert.assertEquals("Expiry Month doesn't match", new YearMonth(2012, 11), t.getExpiryMonth());
		Assert.assertEquals("Strike doesn't match", 0.0, t.getStrikePrice(), 0.000001);
		Assert.assertEquals("Call/Put flag doesn't match", "", t.getCallPut());
		Assert.assertEquals("trade timestamp doesn't match", new LocalDateTime(2012,10,31, 16, 14, 59), t.getTradeTimestamp());
		Assert.assertEquals("Price doesn't match", 21662.0, t.getPrice(), 0.000001);
		Assert.assertEquals("Quantity doesn't match", 1.0, t.getQuantity(), 0.000001);
		Assert.assertEquals("Trade type doesn't match", "001", t.getTradeType());
		
	}
	
	@Test
	public void testNullInputCheck() {
		
		try {
			HkDerivativesTRTuple.parse(null);
			
			Assert.fail("No exception thrown even input is null");
			
		} catch (Exception e1) {
			
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);

			String msg="Input line cannot be null";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}
		
	}
	
	@Test
	public void testMissingDataCheck() {
		try {
			HkDerivativesTRTuple.parse("MHI,F,1211,0,,20121031,161459,21662");
			
			Assert.fail("No exception thrown even input doesn't have enough items");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			String msg = "Input line doesn't exactly having 10 csv delimited items";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}
	}
	
	@Test
	public void testInvalidExpiryMonthCheck() {
		try {
			HkDerivativesTRTuple.parse("MHI,F,xxyyy,0,,20121031,161459,21662,1,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret expiry month: xxyyy";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}
	
	
	@Test
	public void testInvalidStrikeCheck() {
		try {
			HkDerivativesTRTuple.parse("HSI,O,1212,25000xx,C,20121003,091524,7,20,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret the strike: 25000xx";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}	
	
	@Test
	public void testInvalidTradeTimestampCheck1() {
		try {
			HkDerivativesTRTuple.parse("HSI,O,1212,25000,C,20121003,991524,7,20,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret the trade timestamp: 20121003, 991524";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}	
	
	@Test
	public void testInvalidTradeTimestampCheck2() {
		try {
			HkDerivativesTRTuple.parse("HSI,O,1212,25000,C,201210=3,091524,7,20,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret the trade timestamp: 201210=3, 091524";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}	
	
	@Test
	public void testInvalidPriceCheck() {
		try {
			HkDerivativesTRTuple.parse("HSI,O,1212,25000,C,20121003,091524,x7,20,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret price: x7";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}
	
	
	@Test
	public void testInvalidQuantityCheck() {
		try {
			HkDerivativesTRTuple.parse("HSI,O,1212,25000,C,20121003,091524,7,yy20,001");
			
			Assert.fail("No exception thrown even input is invalid");
		} catch (Exception e1) {
			Assert.assertEquals("Exception type mismatched", true, e1 instanceof IllegalArgumentException);
			
			String msg = "unable to interpret quantity: yy20";
			Assert.assertEquals("Exception message mismatched", msg, e1.getMessage());
		}		
	}		
}
