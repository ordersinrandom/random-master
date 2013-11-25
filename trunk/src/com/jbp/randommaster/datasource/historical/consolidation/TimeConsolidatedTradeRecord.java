package com.jbp.randommaster.datasource.historical.consolidation;

import com.jbp.randommaster.datasource.historical.HistoricalData;


/**
 * 
 * ConsolidatedTradeRecord represents a period of trade records that are grouped together by a given time period.
 *
 */
public interface TimeConsolidatedTradeRecord extends HistoricalData {

	/**
	 * Consolidated field.
	 * @return The first traded price.
	 */
	public double getFirstTradedPrice();
	
	/**
	 * Consolidated field.
	 * 
	 * @return The last traded price
	 */
	public double getLastTradedPrice();

	/**
	 * Consolidated field
	 * 
	 * @return The total traded volume.
	 */
	public double getTradedVolume();

	/**
	 * Consolidated field.
	 * 
	 * @return The maximum traded price within the given tuple collections.
	 */
	public double getMaxTradedPrice();

	/**
	 * Consolidated field
	 * 
	 * @return The minimum traded price within the given tuple collections.
	 */
	public double getMinTradedPrice();

	/**
	 * Consolidated field
	 * 
	 * @return The average price of the given tuple collections.
	 */
	public double getAveragedPrice();


}
