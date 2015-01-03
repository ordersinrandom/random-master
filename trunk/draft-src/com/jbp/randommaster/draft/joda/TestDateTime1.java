package com.jbp.randommaster.draft.joda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;



public class TestDateTime1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LocalDate d = LocalDate.now();
		
		Date d2 = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		LocalDateTime d3= LocalDateTime.now();
		Date d4 = Date.from(d3.atZone(ZoneId.systemDefault()).toInstant());
		
		LocalDateTime d5 = LocalDateTime.ofInstant(d4.toInstant(), ZoneId.systemDefault());
		
		
		LocalDate d6 = LocalDate.parse("2014-05-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDateTime d7 = d6.atTime(LocalTime.MIDNIGHT);
		
		
		System.out.println(d);
		System.out.println(d.atTime(LocalTime.MIDNIGHT));
		System.out.println(d2);
		System.out.println(d3);
		System.out.println(d4);
		System.out.println(d5);
		System.out.println(d5.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		System.out.println(d6);
		System.out.println(d7);		
	}

}
