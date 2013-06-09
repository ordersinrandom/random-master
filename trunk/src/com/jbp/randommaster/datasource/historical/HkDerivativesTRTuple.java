package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

/**
 * Encapsulate the data of a single row in the HKEX TR File.
 * 
 * For example,
 * 
 * HHI,F,1211,0,,20121031,161459,10607,1,001
 * HHI,O,1211,8400,P,20121031,161459,2,5,001
 * MHI,O,1211,22400,C,20121031,161459,110,1,001
 * HSI,O,1211,20400,P,20121031,161459,50,1,001
 * HSI,O,1301,19400,P,20121031,161459,117,1,001
 * 
 *
 */
public class HkDerivativesTRTuple implements DerivativesDataTuple {

	private static final long serialVersionUID = -5725594588284543984L;

	private String underlying; // e.g. HSI 
	private String futuresOrOptions; // e.g. F or O
	private YearMonth expiryMonth; 
	private double strikePrice; // 0 for futures
	private String callPut; // C for call, P for put, futures will have empty string
	private LocalDateTime tradeTimestamp; // including date and time
	private double price;
	private double quantity;
	/**
	 * 000 ¡V 002 : Normal trade
	 * 003 ¡V 006 : Block trade
	 * 007 : Standard combination - order matching
	 * 020 : Delta Hedge (Trade in pre-market opening)
	 * 032 ¡V 035 : Bulletin board trade
	 * 037 : Non-standard combination 
	 * 134 : Standard combo trade 
	 */
	private String tradeType;
	
	
	public HkDerivativesTRTuple(String underlying, String futuresOrOptions,
			YearMonth expiryMonth, double strikePrice, String callPut,
			LocalDateTime tradeTimestamp, double price, double quantity,
			String tradeType) {

		this.underlying=underlying;
		this.futuresOrOptions=futuresOrOptions;
		this.expiryMonth=expiryMonth;
		this.strikePrice=strikePrice;
		this.callPut=callPut;
		this.tradeTimestamp=tradeTimestamp;
		this.price=price;
		this.quantity=quantity;
		this.tradeType=tradeType;
	}
	
	public static HkDerivativesTRTuple parse(String line) {
		
		// examples
		// MHI,F,1210,0,,20121003,091524,20900,1,001
		// HSI,O,1212,25000,C,20121003,091524,7,20,001
		
		if (line==null)
			throw new IllegalArgumentException("Input line cannot be null");
		
		String[] terms=line.split(",");
		if (terms.length!=10)
			throw new IllegalArgumentException("Input line doesn't exactly having 10 csv delimited items");

		String underlying = terms[0].trim();
		String futuresOrOptions = terms[1].trim();
		String expiryMonthString = terms[2].trim();
		String strikeString = terms[3].trim();
		String callPut = terms[4].trim();
		String tradeDateString = terms[5].trim();
		String tradeTimeString = terms[6].trim();
		String priceString = terms[7].trim();
		String quantityString = terms[8].trim();
		String tradeType = terms[9].trim();

		YearMonth expiryMonth=null;
		try {
			String yearStr = expiryMonthString.substring(0, 2);
			String monthStr = expiryMonthString.substring(2, 4);
			
			// lol fixing Y2K problem!!!!
			if (Integer.valueOf(yearStr.substring(0,1)).intValue()>=7) 
				yearStr="19"+yearStr;
			else yearStr="20"+yearStr;
			
			expiryMonth=new YearMonth(Integer.valueOf(yearStr).intValue(), Integer.valueOf(monthStr).intValue());
		} catch (Exception e1) {
			throw new IllegalArgumentException("unable to interpret expiry month: "+expiryMonthString);
		}
		
		double strikePrice = Double.NaN;
		try {
			strikePrice = Double.valueOf(strikeString).doubleValue();
		} catch (Exception e2) {
			throw new IllegalArgumentException("unable to interpret the strike: "+strikeString);
		}
		
		LocalDateTime tradeTimestamp = null;
		try {
			// (int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) 
			int year = Integer.valueOf(tradeDateString.substring(0, 4)).intValue();
			int monthOfYear = Integer.valueOf(tradeDateString.substring(4, 6)).intValue();
			int dayOfMonth = Integer.valueOf(tradeDateString.substring(6, 8)).intValue();
			int hourOfDay = Integer.valueOf(tradeTimeString.substring(0, 2)).intValue();
			int minuteOfHour = Integer.valueOf(tradeTimeString.substring(2, 4)).intValue();
			int secondOfMinute = Integer.valueOf(tradeTimeString.substring(4, 6)).intValue();
			
			tradeTimestamp=new LocalDateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
			
		} catch (Exception e3) {
			throw new IllegalArgumentException("unable to interpret the trade timestamp: "+tradeDateString+", "+tradeTimeString);
		}
		
		double price = Double.NaN;
		try {
			price = Double.valueOf(priceString).doubleValue();
		} catch (Exception e4) {
			throw new IllegalArgumentException("unable to interpret price: "+priceString);
		}
		
		double quantity = Double.NaN;
		try {
			quantity = Double.valueOf(quantityString).doubleValue();
		} catch (Exception e5) {
			throw new IllegalArgumentException("unable to interpret quantity: "+quantityString);
		}
		
		return new HkDerivativesTRTuple(underlying, futuresOrOptions,
				expiryMonth, strikePrice, callPut,
				tradeTimestamp, price, quantity,
				tradeType);
		
	}
	
	
	public String getUnderlying() {
		return underlying;
	}
	public String getFuturesOrOptions() {
		return futuresOrOptions;
	}
	
	public boolean isFutures() {
		return "F".equals(futuresOrOptions);
	}
	
	public boolean isOptions() {
		return "O".equals(futuresOrOptions);
	}
	
	public YearMonth getExpiryMonth() {
		return expiryMonth;
	}
	public double getStrikePrice() {
		return strikePrice;
	}
	public String getCallPut() {
		return callPut;
	}
	public LocalDateTime getTradeTimestamp() {
		return tradeTimestamp;
	}
	public double getPrice() {
		return price;
	}
	public double getQuantity() {
		return quantity;
	}
	public String getTradeType() {
		return tradeType;
	}
	
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		else if (obj instanceof HkDerivativesTRTuple) {
			HkDerivativesTRTuple t = (HkDerivativesTRTuple) obj;
			return underlying.equals(t.underlying)
					&& futuresOrOptions.equals(t.futuresOrOptions)
					&& expiryMonth.equals(t.expiryMonth)
					&& strikePrice==t.strikePrice
					&& callPut.equals(t.callPut)
					&& tradeTimestamp.equals(t.tradeTimestamp)
					&& price==t.price
					&& quantity==t.quantity
					&& tradeType.equals(t.tradeType);
		}
		else return false;
	}
	

	public int hashCode() {

		return underlying.hashCode()
				^futuresOrOptions.hashCode()
				^expiryMonth.hashCode()
				^Double.valueOf(strikePrice).hashCode()
				^callPut.hashCode()
				^tradeTimestamp.hashCode()
				^Double.valueOf(price).hashCode()
				^Double.valueOf(quantity).hashCode()
				^tradeType.hashCode();
	}
	
	
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e1) {
			return null;
		}
	}
	
	public String toString() {
		StringBuilder buf=new StringBuilder(300);
		buf.append("HkexTRFileTuple { underlying=");
		buf.append(underlying);
		buf.append(", futuresOrOptions=");
		buf.append(futuresOrOptions);
		buf.append(", expiryMonth=");
		buf.append(expiryMonth);
		buf.append(", strikePrice=");
		buf.append(strikePrice);
		buf.append(", callPut=");
		buf.append(callPut);
		buf.append(", tradeTimestamp=");
		buf.append(tradeTimestamp);
		buf.append(", price=");
		buf.append(price);
		buf.append(", quantity=");
		buf.append(quantity);
		buf.append(", tradeType=");
		buf.append(tradeType);
		buf.append(" }");
		return buf.toString();
	}
	
	
	
}
