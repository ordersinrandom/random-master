package com.jbp.randommaster.utils;

import org.joda.time.LocalDate;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HolidaysListTests extends TestCase {

	@Test
	public void testAddBusinessDays() {
		
		HolidaysList list = HolidaysList.HongKong;
		
		LocalDate start = new LocalDate(2014, 1, 20);
		
		Assert.assertEquals("2014-1-20 plus 0 should be 2014-1-20", new LocalDate(2014, 1, 20), list.addBusinessDays(start, 0));
		Assert.assertEquals("2014-1-20 plus 1 should be 2014-1-21", new LocalDate(2014, 1, 21), list.addBusinessDays(start, 1));
		Assert.assertEquals("2014-1-20 plus 4 should be 2014-1-24", new LocalDate(2014, 1, 24), list.addBusinessDays(start, 4));
		Assert.assertEquals("2014-1-20 plus 5 should be 2014-1-27", new LocalDate(2014, 1, 27), list.addBusinessDays(start, 5));
		Assert.assertEquals("2014-1-20 plus 8 should be 2014-1-30", new LocalDate(2014, 1, 30), list.addBusinessDays(start, 8));
		Assert.assertEquals("2014-1-20 plus 9 should be 2014-2-4", new LocalDate(2014, 2, 4), list.addBusinessDays(start, 9));
		Assert.assertEquals("2014-2-2 plus 1 should be 2014-2-4", new LocalDate(2014, 2, 4), list.addBusinessDays(new LocalDate(2014, 2, 2), 1));
		
		LocalDate start2 = new LocalDate(2014, 2, 5);
		Assert.assertEquals("2014-2-5 minus 1 should be 2014-2-4", new LocalDate(2014, 2, 4), list.addBusinessDays(start2, -1));
		Assert.assertEquals("2014-2-5 minus 2 should be 2014-1-30", new LocalDate(2014, 1, 30), list.addBusinessDays(start2, -2));
		Assert.assertEquals("2014-2-5 minus 5 should be 2014-1-27", new LocalDate(2014, 1, 27), list.addBusinessDays(start2, -5));
		Assert.assertEquals("2014-2-5 minus 6 should be 2014-1-24", new LocalDate(2014, 1, 24), list.addBusinessDays(start2, -6));
		Assert.assertEquals("2014-2-5 minus 22 should be 2014-1-2", new LocalDate(2014, 1, 2), list.addBusinessDays(start2, -22));
		Assert.assertEquals("2014-2-5 minus 23 should be 2013-12-31", new LocalDate(2013, 12, 31), list.addBusinessDays(start2, -23));
	}
	

	@Test
	public void testIsBusinessDay() {
		
		HolidaysList list = HolidaysList.HongKong;
		
		Assert.assertEquals("2014-1-20 should be a business day", true, list.isBusinessDay(new LocalDate(2014, 1, 20)));
		Assert.assertEquals("2014-1-25 should not be a business day", false, list.isBusinessDay(new LocalDate(2014, 1, 25)));
		Assert.assertEquals("2014-1-30 should be a business day", true, list.isBusinessDay(new LocalDate(2014, 1, 30)));
		Assert.assertEquals("2014-1-31 should not be a business day", false, list.isBusinessDay(new LocalDate(2014, 1, 31)));
		
	}
	
	
}
