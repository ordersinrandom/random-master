package com.jbp.randommaster.utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

public class MarketHours {

	public static final MarketHours HongKongDerivatives = new MarketHours(
			new Session[] {
				new Session(new LocalTime(9,15), new LocalTime(12,0)),
				new Session(new LocalTime(13,0), new LocalTime(16,15)),
			});
	
	
	private List<Session> sessionsList;
	
	public MarketHours() {
		sessionsList = new ArrayList<>();
	}
	
	public MarketHours(LocalTime open, LocalTime close) {
		this(new Session[] { new Session(open, close) });
	}
	
	public MarketHours(Session[] sessions) {
		this();
		for (Session p : sessions)
			sessionsList.add(p);
	}
	
	
	public void addSession(LocalTime open, LocalTime close) {
		sessionsList.add(new Session(open, close));		
	}
	
	public boolean isMarketHour(LocalTime t, boolean openExclusive, boolean closeExclusive) {
		boolean passedTimeCheck = false;

		for (Session p : sessionsList) {
			LocalTime open = p.getOpen();
			LocalTime close = p.getClose();

			if (((!openExclusive && open.isEqual(t)) || open.isBefore(t)) && ((!closeExclusive && close.isEqual(t)) || close.isAfter(t))) {
				passedTimeCheck = true;
				break;
			}
		}
		
		return passedTimeCheck;
	}
	
	
	public static class Session {
		private LocalTime open;
		private LocalTime close;
		
		public Session(LocalTime open, LocalTime close) {
			this.open = open;
			this.close = close;
		}

		public LocalTime getOpen() {
			return open;
		}

		public LocalTime getClose() {
			return close;
		}
	}
}
