package com.jbp.randommaster.draft.joda;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class TestDateTime1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LocalDateTime d1=LocalDateTime.now();
		
		
		long instant = d1.toDate().getTime();
		
		DateTimeFormatter df=ISODateTimeFormat.dateTime();
		
		System.out.println(d1.toString(df));
		
		System.out.println(d1.toLocalDate().toString(df));
		
		System.out.println(LocalDateTime.fromDateFields(d1.toDate()).toString(df));
		
		System.out.println((new LocalDateTime(instant)).toString(df));

	}

}
