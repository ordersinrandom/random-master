package com.jbp.randommaster.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MarketHours {

	public static final MarketHours HongKongDerivatives = new MarketHours(
			new Session[] {
				new Session(LocalTime.of(9,15), LocalTime.of(12,0)),
				new Session(LocalTime.of(13,0), LocalTime.of(16,15)),
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

			if (((!openExclusive && open.equals(t)) || open.isBefore(t)) && ((!closeExclusive && close.equals(t)) || close.isAfter(t))) {
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
