package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

/**
 * This class summarizes a list of HkDerivativesTR objects into useful data values such as
 * lastTradedPrice / maxTradedPrice / minTradedPrice / averagedPrice / tradedVolume.
 * 
 */
public class HkDerivativesConsolidatedData implements VanillaDerivativesData, FuturesData, ConsolidatedTradeRecordsData {

	private static final long serialVersionUID = -8174697719234067519L;

	private YearMonth expiryMonth;
	private String underlying;
	private double strikePrice;
	private String futuresOrOptions; // F or O
	private String callPut; // C or P or empty string
	private double firstTradedPrice;
	private double lastTradedPrice;
	private double maxTradedPrice;
	private double minTradedPrice;
	private double averagedPrice;
	private double tradedVolume;
	private LocalDateTime timestamp;

	/**
	 * Create a new instance of HkDerivativesConsolidatedTuple with given data.
	 */
	public HkDerivativesConsolidatedData(LocalDateTime timestamp, YearMonth expiryMonth, String underlying, double strikePrice, String futuresOrOptions, String callPut,
			double firstTradedPrice, double lastTradedPrice, double maxTradedPrice, double minTradedPrice, double averagedPrice, double tradedVolume) {
		this.expiryMonth = expiryMonth;
		this.underlying = underlying;
		this.strikePrice = strikePrice;
		this.futuresOrOptions = futuresOrOptions;
		this.callPut = callPut;
		this.firstTradedPrice = firstTradedPrice;
		this.lastTradedPrice = lastTradedPrice;
		this.maxTradedPrice = maxTradedPrice;
		this.minTradedPrice = minTradedPrice;
		this.averagedPrice = averagedPrice;
		this.tradedVolume = tradedVolume;
		this.timestamp=timestamp;
	}
	
	
	public String getFuturesOrOptions() { 
		return futuresOrOptions;
	}
	
	public String getCallPut() {
		return callPut;
	}
		
	/**
	 * Consolidated field.
	 * 
	 * @return The first traded price
	 */
	@Override
	public double getFirstTradedPrice() {
		return firstTradedPrice;
	}
	
	/**
	 * Consolidated field.
	 * 
	 * @return The last traded price
	 */
	@Override
	public double getLastTradedPrice() {
		return lastTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The total traded volume.
	 */
	@Override
	public double getTradedVolume() {
		return tradedVolume;
	}

	/**
	 * Consolidated field.
	 * 
	 * @return The maximum traded price within the given tuple collections.
	 */
	@Override
	public double getMaxTradedPrice() {
		return maxTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The minimum traded price within the given tuple collections.
	 */
	@Override
	public double getMinTradedPrice() {
		return minTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The average price of the given tuple collections.
	 */
	@Override
	public double getAveragedPrice() {
		return averagedPrice;
	}

	@Override
	public YearMonth getExpiryMonth() {
		return expiryMonth;
	}

	@Override
	public String getUnderlying() {
		return underlying;
	}

	@Override
	public double getStrikePrice() {
		return strikePrice;
	}
	


	@Override
	public boolean isFutures() {
		return "F".equals(futuresOrOptions);
	}

	@Override
	public boolean isOptions() {
		return "O".equals(futuresOrOptions);
	}
	
	@Override
	public boolean isCall() {
		return !isFutures() && "C".equals(callPut);
	}
	
	@Override
	public boolean isPut() {
		return !isFutures() && "P".equals(callPut);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(300);
		buf.append("HkDerivativesConsolidatedData { timestamp=");
		buf.append(timestamp);
		buf.append(", expiryMonth=");
		buf.append(expiryMonth);
		buf.append(", underlying=");
		buf.append(underlying);
		buf.append(", strikePrice=");
		buf.append(strikePrice);
		buf.append(", futuresOrOptions=");
		buf.append(futuresOrOptions);
		buf.append(", callPut=");
		buf.append(callPut);
		buf.append(", firstTradedPrice=");
		buf.append(firstTradedPrice);
		buf.append(", lastTradedPrice=");
		buf.append(lastTradedPrice);
		buf.append(", maxTradedPrice=");
		buf.append(maxTradedPrice);
		buf.append(", minTradedprice=");
		buf.append(minTradedPrice);
		buf.append(", averagedPrice=");
		buf.append(averagedPrice);
		buf.append(", tradedVolume=");
		buf.append(tradedVolume);
		buf.append(" }");
		return buf.toString();

	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e1) {
			// ignore
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (obj instanceof HkDerivativesConsolidatedData) {
			HkDerivativesConsolidatedData t = (HkDerivativesConsolidatedData) obj;
			return expiryMonth.equals(t.expiryMonth) && underlying.equals(t.underlying) && strikePrice == t.strikePrice
					&& futuresOrOptions.equals(t.futuresOrOptions) && callPut.equals(t.callPut)
					&& firstTradedPrice == t.firstTradedPrice
					&& lastTradedPrice == t.lastTradedPrice
					&& maxTradedPrice == t.maxTradedPrice && minTradedPrice == t.minTradedPrice && averagedPrice == t.averagedPrice
					&& tradedVolume == t.tradedVolume
					&& timestamp.equals(t.timestamp);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return expiryMonth.hashCode() ^ underlying.hashCode() ^ Double.valueOf(strikePrice).hashCode() ^ futuresOrOptions.hashCode()
				^ callPut.hashCode()
				^ Double.valueOf(firstTradedPrice).hashCode()
				^ Double.valueOf(lastTradedPrice).hashCode() ^ Double.valueOf(maxTradedPrice).hashCode()
				^ Double.valueOf(minTradedPrice).hashCode() ^ Double.valueOf(averagedPrice).hashCode()
				^ Double.valueOf(tradedVolume).hashCode()
				^ timestamp.hashCode();
	}

	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	@Override
	public int compareTo(HistoricalData o) {
		return this.timestamp.compareTo(o.getTimestamp());
	}





}
