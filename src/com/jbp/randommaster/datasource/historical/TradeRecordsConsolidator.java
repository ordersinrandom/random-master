package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;


/**
 * 
 * Abstract class implementation of the function to consolidated an iterable of TradeRecordData
 * to ConsolidatedTradeRecordsData.
 *
 * @param <T1> The result object type.
 * @param <T2> The input object type.
 */
public abstract class TradeRecordsConsolidator<T1 extends ConsolidatedTradeRecordsData, T2 extends TradeRecordData> {

	/**
	 * Consolidate the given iterable of TradeRecordData, and return a ConsolidatedTradeRecordsData.
	 * 
	 * @param refTimestamp The timestamp to be used.
	 * @param original The original input of trade records.
	 * @return null if original is empty or null. Otherwise it consolidated the basic details and the invokes createConsolidatedData()
	 */
	public T1 consolidate(LocalDateTime refTimestamp, Iterable<T2> original) {
		
		if (original==null)
			return null;
		
		
		double tradedVolume = 0.0;
		double averagedPrice = 0.0;
		double lastTradedPrice = 0.0;
		double maxTradedPrice = Double.MIN_VALUE;
		double minTradedPrice = Double.MAX_VALUE;
		
		int itemsCount=0;
		
		for (T2 t : original) {

			lastTradedPrice = t.getPrice();
			if (maxTradedPrice <= t.getPrice())
				maxTradedPrice = t.getPrice();
			if (minTradedPrice >= t.getPrice())
				minTradedPrice = t.getPrice();
			averagedPrice += (t.getPrice() * t.getQuantity());
			tradedVolume += t.getQuantity();

			itemsCount++;
		}

		averagedPrice /= tradedVolume;

		// case for empty input iterable
		if (itemsCount == 0)
			return null;		
		
		return createConsolidatedData(refTimestamp, original,
				lastTradedPrice, maxTradedPrice,
				minTradedPrice, averagedPrice,
				tradedVolume);
		
	}
	
	
	/**
	 * Subclass to implement this function to "instantiate" the new ConsolidatedTradeRecordsData object.
	 * This function requires to subclass to look at the original iterable items and extract their
	 * common properties and use our calculated results 
	 * (lastTradedPrice/maxTradedPrice/minTradedPrice/averagedPrice/tradedVolume) 
	 * to instantiate the new ConsolidatedTradeRecordsData object.
	 *  
	 * @param refTimestamp The timestamp to be used.
	 * @param original The original iterable just in case the new object needs to copy some values from it. Must not empty or null.
	 * @param lastTradedPrice The computed last traded price based on the iterable.
	 * @param maxTradedPrice The max traded price computed based on the iterable.
	 * @param minTradedPrice The min traded price computed based on the iterable.
	 * @param averagedPrice The averaged price computed based on the iterable.
	 * @param tradedVolume The traded volume computed based on the iterable.
	 * 
	 * @return A new instance of ConsolidatedTradeRecordsData
	 */
	protected abstract T1 createConsolidatedData(
			LocalDateTime refTimestamp, Iterable<T2> original,
			double lastTradedPrice, double maxTradedPrice, 
			double minTradedPrice, double averagedPrice,
			double tradedVolume);
	
}
