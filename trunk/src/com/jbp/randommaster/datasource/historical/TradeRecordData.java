package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;

public interface TradeRecordData extends HistoricalData {

	public LocalDateTime getTradeTimestamp();
	
	public double getPrice();
	
	public double getQuantity();
	
}
