package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HkDerivativesTRFileDataTests extends TestCase {

	@Test
	public void testTimestampMatching() {

		HkDerivativesTR d=HkDerivativesTR.parse("HSI,O,1212,25000,C,20121003,091524,7,20,001");
		Assert.assertEquals("Timestamp mismatched", new LocalDateTime(2012, 10, 3, 9, 15, 24), d.getTimestamp());
		
	}
	
	@Test
	public void testDataTupleMatching() {
		String input="HSI,O,1212,25000,C,20121003,091524,7,20,001";
		
		HkDerivativesTR d=HkDerivativesTR.parse(input);
		HkDerivativesTR t=HkDerivativesTR.parse(input);
		
		Assert.assertEquals("Tuple mismatched", t, d);
	}
	
}
