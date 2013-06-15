package com.jbp.randommaster.datasource.historical;

import java.io.Serializable;

import org.joda.time.LocalDateTime;

public interface HistoricalData extends Comparable<HistoricalData>, Serializable, Cloneable {

	public LocalDateTime getTimestamp();
	
}
