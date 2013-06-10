package com.jbp.randommaster.draft.joda;


import org.joda.time.LocalDateTime;
import org.joda.time.Period;

public class TestDateTime2 {

	public static void main(String[] args) throws Exception {
		
		LocalDateTime d1=new LocalDateTime(2012,2,3, 10, 50, 0);
		
		Period p=new Period(0, 0, 5, 0);
		
		for (int i=0;i<30;i++) {
			System.out.println(d1);
			d1=d1.plus(p);
		}
		
	}
	
}
