package com.jbp.randommaster.draft.joda;


import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TestDateTime3 {

	public static void main(String[] args) {

		String input="1530";
		
		DateTimeFormatter f=DateTimeFormat.forPattern("HHmm");
		
		LocalTime t = LocalTime.parse(input, f);
		
		System.out.println(t);
		
		

	}

}
