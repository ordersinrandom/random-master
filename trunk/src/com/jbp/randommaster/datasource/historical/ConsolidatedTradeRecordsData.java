package com.jbp.randommaster.datasource.historical;

public interface ConsolidatedTradeRecordsData extends HistoricalData {

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

	/**
	 * Consolidated field
	 * 
	 * @return The number of transaction we have observed in the given tuple
	 *         collections.
	 */
	public int getTransactionsCount();


}
