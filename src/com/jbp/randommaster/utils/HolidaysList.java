package com.jbp.randommaster.utils;

import java.util.TreeSet;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class HolidaysList {

	public static final HolidaysList HongKong = new HolidaysList("Hong Kong", new LocalDate[] {
			new LocalDate(2013,1,1),
			new LocalDate(2013,2,10),new LocalDate(2013,2,11),new LocalDate(2013,2,12),new LocalDate(2013,2,13),
			new LocalDate(2013,3,29),new LocalDate(2013,3,30),new LocalDate(2013,3,31),
			new LocalDate(2013,4,1),new LocalDate(2013,4,4),
			new LocalDate(2013,5,1),new LocalDate(2013,5,17),
			new LocalDate(2013,6,12),
			new LocalDate(2013,7,1),
			new LocalDate(2013,9,20),
			new LocalDate(2013,10,1),new LocalDate(2013,10,13),new LocalDate(2013,10,14),
			new LocalDate(2013,12,25),new LocalDate(2013,12,26),
			new LocalDate(2014,1,1),new LocalDate(2014,1,31),
			new LocalDate(2014,2,1),new LocalDate(2014,2,2),new LocalDate(2014,2,3),
			new LocalDate(2014,4,5),new LocalDate(2014,4,18),new LocalDate(2014,4,19),new LocalDate(2014,4,20),new LocalDate(2014,4,21),
			new LocalDate(2014,5,1),new LocalDate(2014,5,6),
			new LocalDate(2014,6,2),
			new LocalDate(2014,7,1),
			new LocalDate(2014,9,9),
			new LocalDate(2014,10,1),new LocalDate(2014,10,2),
			new LocalDate(2014,12,25),new LocalDate(2014,12,26),
	});   
	
	
	
	private TreeSet<LocalDate> holidays;
	private String name;
	
	private HolidaysList(String name, LocalDate[] list) {
		holidays = new TreeSet<>();
		for (LocalDate d : list)
			holidays.add(d);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public boolean isBusinessDay(LocalDate d) {
		int dow = d.getDayOfWeek();
		return dow != DateTimeConstants.SUNDAY && dow != DateTimeConstants.SATURDAY && !holidays.contains(d);
	}
	
	
	public LocalDate addBusinessDays(LocalDate start, int daysCount) {
		int c = daysCount;
		LocalDate result = start;
		if (c > 0) {
			while (c > 0) {
				result = result.plusDays(1);
				if (isBusinessDay(result))
					c--;
			}
		} else if (c < 0) {
			while (c < 0) {
				result = result.minusDays(1);
				if (isBusinessDay(result))
					c++;
			}
		}
		return result;
	}	
	
}
