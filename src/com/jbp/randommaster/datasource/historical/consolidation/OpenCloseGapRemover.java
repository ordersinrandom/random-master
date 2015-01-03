package com.jbp.randommaster.datasource.historical.consolidation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;

/**
 * 
 * OpenCloseGapRemover removes the gaps between two market open sessions using back adjust method by adding the gap difference to past time series.
 * 
 */
public class OpenCloseGapRemover<T extends TimeConsolidatedTradeRecord> {

	private List<Pair<LocalTime, LocalTime>> intradayClosePeriods;
	private LocalTime dayOpen, dayClose;
	private Set<LocalDate> holidays;

	public OpenCloseGapRemover(LocalTime dayOpen, LocalTime dayClose) {
		intradayClosePeriods = new LinkedList<>();
		holidays = new HashSet<>();
		
		this.dayClose=dayClose;
		this.dayOpen=dayOpen;
	}


	public void addHoliday(LocalDate d) {
		holidays.add(d);
	}

	public void addIntradayClosePeriod(LocalTime start, LocalTime end) {
		intradayClosePeriods.add(new Pair<>(start,end));
	}

	public Iterable<GapAdjustedTradeRecord<T>> removeGaps(Iterable<T> src) {

		LinkedList<GapAdjustedTradeRecord<T>> resultList = new LinkedList<>();

		LinkedList<AccruedGapsTracker> trackers = new LinkedList<>();

		AccruedGapsTracker currentTracker = new AccruedGapsTracker();
		T lastRecord = null;
		for (T rec : src) {

			if (lastRecord != null) {
				// last record is always market open
				
				// if we are across from market open to close
				if (!isMarketOpen(rec.getTimestamp())) {
					// ignore the new record
					continue;
				}
				else {
					// new record is market open
					
					// if across gap
					if (isAcrossDay(lastRecord.getTimestamp(), rec.getTimestamp())
							|| isAcrossIntradayGaps(lastRecord.getTimestamp(), rec.getTimestamp())) {
						
						// apply the adjustment to previous records
						double adjustmentAmount = rec.getLastTradedPrice() - lastRecord.getLastTradedPrice();
						trackers.add(currentTracker);
						applyAdjustment(adjustmentAmount, trackers);
						
						// create a new tracker
						currentTracker = new AccruedGapsTracker();
					}
					// add new record
					GapAdjustedTradeRecord<T> item=new GapAdjustedTradeRecord<>(rec, currentTracker);
					resultList.add(item);
					lastRecord=rec;
				}
				
				

			} else if (isMarketOpen(rec.getTimestamp())) {
				GapAdjustedTradeRecord<T> item = new GapAdjustedTradeRecord<>(rec, currentTracker);
				resultList.add(item);
				lastRecord = rec;
			}

		}

		return resultList;
	}
	
	private void applyAdjustment(double adjustment, Iterable<AccruedGapsTracker> trackers) {
		for (AccruedGapsTracker t : trackers) {
			t.adjust(adjustment);
		}
	}

	private boolean isAcrossDay(LocalDateTime t0, LocalDateTime t1) {
		LocalDate d0 = t0.toLocalDate();
		LocalDate d1 = t1.toLocalDate();
		int c = d0.compareTo(d1);
		if (c > 0)
			throw new IllegalArgumentException("d1 is before d0");
		return c != 0;
	}

	private boolean isAcrossIntradayGaps(LocalDateTime ts0, LocalDateTime ts1) {
		if (isAcrossDay(ts0, ts1))
			throw new IllegalArgumentException("ts0 and ts1 are not on the same day");

		LocalTime t0 = ts0.toLocalTime();
		LocalTime t1 = ts1.toLocalTime();

		// assume not fall into gaps
		boolean acrossGap = false;
		for (Pair<LocalTime, LocalTime> en : intradayClosePeriods) {
			LocalTime start = en.getValue0();
			LocalTime end = en.getValue1();
			if (t0.compareTo(start) <= 0 && t1.compareTo(end) >= 0) {
				acrossGap = true;
				break;
			}
		}
		return acrossGap;
	}

	private boolean isMarketOpen(LocalDateTime ts) {
		if (!isBusinessDay(ts))
			return false;

		LocalTime t = ts.toLocalTime();
		if (t.compareTo(dayClose) <= 0 && t.compareTo(dayOpen) >= 0) {
			boolean fallInGap = false;
			for (Pair<LocalTime, LocalTime>  en : intradayClosePeriods) {
				LocalTime start = en.getValue0();
				LocalTime end = en.getValue1();
				if (start.compareTo(t) < 0 && end.compareTo(t) > 0) {
					fallInGap = true;
					break;
				}
			}
			return !fallInGap;
		} else
			return false;
	}

	private boolean isWeekend(LocalDateTime t) {
		DayOfWeek dayOfWeek = t.getDayOfWeek();
		return dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY;
	}

	private boolean isHoliday(LocalDateTime t) {
		LocalDate d = t.toLocalDate();
		return holidays.contains(d);
	}

	private boolean isBusinessDay(LocalDateTime t) {
		return !isWeekend(t) && !isHoliday(t);
	}

}
