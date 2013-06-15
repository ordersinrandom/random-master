package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

/**
 * The data tuple that takes an list of HkDerivativesTRTuple and consolidate
 * them into useful data such as traded volume, last/max/min/avg traded prices
 * etc.
 * 
 * 
 */
public class HkDerivativesConsolidatedData implements VanillaDerivativesData, ConsolidatedTradeRecordsData {

	private static final long serialVersionUID = -8174697719234067519L;

	private YearMonth expiryMonth;
	private String underlying;
	private double strikePrice;
	private FuturesOptions futuresOrOptions;
	private CallPut callPut;
	private double lastTradedPrice;
	private double maxTradedPrice;
	private double minTradedPrice;
	private double averagedPrice;
	private int transactionsCount;
	private double tradedVolume;
	private LocalDateTime timestamp;

	/**
	 * Constructor used internally.
	 */
	protected HkDerivativesConsolidatedData(LocalDateTime timestamp) {
		expiryMonth = null;
		underlying = null;
		strikePrice = 0.0;
		futuresOrOptions = null;
		callPut = null;
		lastTradedPrice = 0.0;
		maxTradedPrice = Double.MIN_VALUE;
		minTradedPrice = Double.MAX_VALUE;
		averagedPrice = 0.0;
		tradedVolume = 0.0;
		transactionsCount = 0;
		this.timestamp=timestamp;

	}

	/**
	 * Create a new instance of HkDerivativesConsolidatedTuple with given data.
	 */
	public HkDerivativesConsolidatedData(LocalDateTime timestamp, YearMonth expiryMonth, String underlying, double strikePrice, FuturesOptions futOpt, CallPut cp,
			double lastTradedPrice, double maxTradedPrice, double minTradedPrice, double averagedPrice, double tradedVolume, int transactionsCount) {
		this.expiryMonth = expiryMonth;
		this.underlying = underlying;
		this.strikePrice = strikePrice;
		this.futuresOrOptions = futOpt;
		this.callPut = cp;
		this.lastTradedPrice = lastTradedPrice;
		this.maxTradedPrice = maxTradedPrice;
		this.minTradedPrice = minTradedPrice;
		this.averagedPrice = averagedPrice;
		this.tradedVolume = tradedVolume;
		this.transactionsCount = transactionsCount;
		this.timestamp=timestamp;
	}


	/**
	 * Consolidated field.
	 * 
	 * @return The last traded price
	 */
	public double getLastTradedPrice() {
		return lastTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The total traded volume.
	 */
	public double getTradedVolume() {
		return tradedVolume;
	}

	/**
	 * Consolidated field.
	 * 
	 * @return The maximum traded price within the given tuple collections.
	 */
	public double getMaxTradedPrice() {
		return maxTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The minimum traded price within the given tuple collections.
	 */
	public double getMinTradedPrice() {
		return minTradedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The average price of the given tuple collections.
	 */
	public double getAveragedPrice() {
		return averagedPrice;
	}

	/**
	 * Consolidated field
	 * 
	 * @return The number of transaction we have observed in the given tuple
	 *         collections.
	 */
	public int getTransactionsCount() {
		return transactionsCount;
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
	public CallPut getCallPut() {
		return callPut;
	}

	@Override
	public FuturesOptions getFuturesOrOptions() {
		return futuresOrOptions;
	}

	@Override
	public boolean isFutures() {
		return futuresOrOptions == FuturesOptions.FUTURES;
	}

	@Override
	public boolean isOptions() {
		return futuresOrOptions == FuturesOptions.OPTIONS;
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
		buf.append(", transactionsCount=");
		buf.append(transactionsCount);
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
					&& futuresOrOptions == t.futuresOrOptions && callPut == t.callPut && lastTradedPrice == t.lastTradedPrice
					&& maxTradedPrice == t.maxTradedPrice && minTradedPrice == t.minTradedPrice && averagedPrice == t.averagedPrice
					&& transactionsCount == t.transactionsCount && tradedVolume == t.tradedVolume
					&& timestamp == t.timestamp;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return expiryMonth.hashCode() ^ underlying.hashCode() ^ Double.valueOf(strikePrice).hashCode() ^ futuresOrOptions.hashCode()
				^ callPut.hashCode() ^ Double.valueOf(lastTradedPrice).hashCode() ^ Double.valueOf(maxTradedPrice).hashCode()
				^ Double.valueOf(minTradedPrice).hashCode() ^ Double.valueOf(averagedPrice).hashCode()
				^ Integer.valueOf(transactionsCount).hashCode() ^ Double.valueOf(tradedVolume).hashCode()
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
