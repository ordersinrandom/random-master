package com.jbp.randommaster.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 * 
 * HolidaysList is an immutable object that handles the business days
 * calculation.
 * 
 */
public class HolidaysList {

	public static final HolidaysList HongKong = new HolidaysList("Hong Kong", new DayOfWeek[] { DayOfWeek.SUNDAY, DayOfWeek.SATURDAY },
			new LocalDate[] { LocalDate.of(2012, 1, 2), LocalDate.of(2012, 1, 23), LocalDate.of(2012, 1, 24), LocalDate.of(2012, 1, 25),
					LocalDate.of(2012, 4, 4), LocalDate.of(2012, 4, 6), LocalDate.of(2012, 4, 7), LocalDate.of(2012, 4, 9),
					LocalDate.of(2012, 4, 28), LocalDate.of(2012, 5, 1), LocalDate.of(2012, 6, 23), LocalDate.of(2012, 7, 2),
					LocalDate.of(2012, 10, 1), LocalDate.of(2012, 10, 2), LocalDate.of(2012, 10, 23), LocalDate.of(2012, 12, 25),
					LocalDate.of(2012, 12, 26), LocalDate.of(2013, 1, 1), LocalDate.of(2013, 2, 10), LocalDate.of(2013, 2, 11),
					LocalDate.of(2013, 2, 12), LocalDate.of(2013, 2, 13), LocalDate.of(2013, 3, 29), LocalDate.of(2013, 3, 30),
					LocalDate.of(2013, 3, 31), LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 4), LocalDate.of(2013, 5, 1),
					LocalDate.of(2013, 5, 17), LocalDate.of(2013, 6, 12), LocalDate.of(2013, 7, 1), LocalDate.of(2013, 9, 20),
					LocalDate.of(2013, 10, 1), LocalDate.of(2013, 10, 13), LocalDate.of(2013, 10, 14), LocalDate.of(2013, 12, 25),
					LocalDate.of(2013, 12, 26), LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 31), LocalDate.of(2014, 2, 1),
					LocalDate.of(2014, 2, 2), LocalDate.of(2014, 2, 3), LocalDate.of(2014, 4, 5), LocalDate.of(2014, 4, 18),
					LocalDate.of(2014, 4, 19), LocalDate.of(2014, 4, 20), LocalDate.of(2014, 4, 21), LocalDate.of(2014, 5, 1),
					LocalDate.of(2014, 5, 6), LocalDate.of(2014, 6, 2), LocalDate.of(2014, 7, 1), LocalDate.of(2014, 9, 9),
					LocalDate.of(2014, 10, 1), LocalDate.of(2014, 10, 2), LocalDate.of(2014, 12, 25), LocalDate.of(2014, 12, 26) });

	// the delimiter to be used to union multiple holidays list together.
	private static final String namesDelimiter = "|";

	private String name;
	private Set<DayOfWeek> weekendDays;
	private Set<LocalDate> holidays;

	public HolidaysList(String name, DayOfWeek[] weekend, LocalDate[] list) {
		holidays = new HashSet<>(300, 0.9f);
		if (list != null) {
			for (LocalDate d : list)
				holidays.add(d);
		}

		this.weekendDays = new HashSet<>();
		if (weekend != null) {
			for (DayOfWeek c : weekend)
				weekendDays.add(c);
		}

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isBusinessDay(LocalDate d) {
		return !weekendDays.contains(d.getDayOfWeek()) && !holidays.contains(d);
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

	public HolidaysList union(HolidaysList another) {

		if (another == null)
			return this;

		// prepare the new holiday name
		TreeSet<String> names = new TreeSet<>();
		for (String n : this.name.split(namesDelimiter))
			names.add(n);
		for (String n : another.name.split(namesDelimiter))
			names.add(n);

		StringBuilder newName = new StringBuilder();
		int nc = names.size();
		int c = 0;
		for (String n : names) {
			newName.append(n);
			if (c < nc - 1)
				newName.append(namesDelimiter);
		}

		HolidaysList newList = new HolidaysList(newName.toString(), null, null);
		newList.holidays.addAll(this.holidays);
		newList.holidays.addAll(another.holidays);
		newList.weekendDays.addAll(this.weekendDays);
		newList.weekendDays.addAll(another.weekendDays);

		return newList;
	}

}
