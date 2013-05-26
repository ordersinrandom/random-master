package com.jbp.randommaster.datasource.historical;


import java.io.Serializable;

import org.joda.time.LocalDateTime;

public interface HistoricalData<T extends HistoricalDataTuple> extends
													Comparable<HistoricalData<? extends HistoricalDataTuple>>,
													Serializable, Cloneable {

	public LocalDateTime getTimestamp();
	
	public T getData();
	
}
