package com.jbp.randommaster.datasource.historical.consolidation;

import java.util.Iterator;
import java.util.LinkedList;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import com.jbp.randommaster.datasource.historical.TradeRecordData;

/**
 * 
 * Abstract class implementation of the function to consolidated an iterable of
 * TradeRecordData to TimeConsolidatedTradeRecord.
 * 
 * @param <T1>
 *            The result object type.
 * @param <T2>
 *            The input object type.
 */
public abstract class TimeBasedTRConsolidator<T1 extends TimeConsolidatedTradeRecord, T2 extends TradeRecordData> {

	/**
	 * Consolidate the given input trade records by splitting the start to end
	 * time by a given interval.
	 * 
	 * @param start
	 *            The start time of the consolidation.
	 * @param end
	 *            The end time of the consolidation.
	 * @param interval
	 *            The interval of each consolidation.
	 * @param inputData
	 *            The original input data.
	 * 
	 * @return An iterable of ConsolidatedTradeRecordsData splitted by time
	 *         interval.
	 */
	public Iterable<T1> consolidateByTimeIntervals(LocalDateTime start, LocalDateTime end, Period interval, Iterable<T2> inputData) {

		LinkedList<T1> result = new LinkedList<T1>();

		LinkedList<T2> currentBuffer = new LinkedList<T2>();
		LinkedList<T2> nextBuffer = new LinkedList<T2>();

		Iterator<T2> inputIt = inputData.iterator();

		LinkedList<LocalDateTime> backwardExtrapolateList = new LinkedList<LocalDateTime>();

		LocalDateTime nextStart = start;
		while (nextStart.compareTo(end) < 0) {
			LocalDateTime nextEnd = nextStart.plus(interval);

			if (nextEnd.compareTo(end) > 0) {
				nextEnd = end;
			}

			// add the data in nextBuffer to currentBuffer and remove it
			// whenever it is applicable to current period.
			if (!nextBuffer.isEmpty()) {
				for (Iterator<T2> it = nextBuffer.iterator(); it.hasNext();) {
					T2 n = it.next();
					// must be within the current timestamp.
					if (n.getTradeTimestamp().compareTo(nextStart) >= 0
							&& ((n.getTradeTimestamp().compareTo(nextEnd) == 0 && nextEnd.compareTo(end) == 0) || n.getTradeTimestamp().compareTo(
									nextEnd) < 0)) {
						currentBuffer.add(n);
						it.remove();
					}
				}
			}

			while (inputIt.hasNext()) {
				T2 n = inputIt.next();
				if (n.getTradeTimestamp().compareTo(nextStart) < 0)
					continue; // drop it if the data is before next start (this
								// should happen only on first round)
				else if (n.getTradeTimestamp().compareTo(nextEnd) < 0
						|| (n.getTradeTimestamp().compareTo(nextEnd) == 0 && nextEnd.compareTo(end) == 0)) {
					// either it is within current period
					// OR special handling for closing.
					// if it is exactly stepped on current end time we will
					// include them both in the current and next buffer
					currentBuffer.add(n);
				} else {
					nextBuffer.add(n);
					break;
				}
			}

			T1 currentIntervalResult = null;
			if (currentBuffer.isEmpty()) {
				try {
					// extrapolate a result if current interval has no data.
					currentIntervalResult = extrapolate(nextEnd, result);
				} catch (Exception e1) {
					// if that's an exception (i.e. unable to extrapolate)
					// we just ignore so that the currentIntervalResult is left
					// as null
				}
			} else {
				// now current buffer stores the current interval data
				// and next buffer potentially stores the next interval data.
				currentIntervalResult = consolidate(nextEnd, currentBuffer);
				// clear the buffer
				currentBuffer.clear();
			}

			// if there is a result from this interval
			if (currentIntervalResult != null)
				result.add(currentIntervalResult);
			else {
				// potentially currentIntervalResult can be null if at start
				// early intervals and there are no previous data.
				// we would need to backward extrapolate the data if we want a
				// proper "plot"
				backwardExtrapolateList.add(nextEnd);
			}

			nextStart = nextEnd;
		}

		// TODO: handle the remaining data in the nextBuffer ???

		// handle backward extrapolating the early intervals data.
		for (Iterator<LocalDateTime> it = backwardExtrapolateList.descendingIterator(); it.hasNext();) {
			LocalDateTime t = it.next();
			T1 exResult = backwardExtrapolate(t, result);
			result.add(0, exResult);
		}

		return result;
	}

	/**
	 * Consolidate the given iterable of TradeRecordData, and return a
	 * ConsolidatedTradeRecordsData.
	 * 
	 * @param refTimestamp
	 *            The timestamp to be used.
	 * @param original
	 *            The original input of trade records.
	 * @return null if original is empty or null. Otherwise it consolidated the
	 *         basic details and the invokes createConsolidatedData()
	 */
	public T1 consolidate(LocalDateTime refTimestamp, Iterable<T2> original) {

		if (original == null)
			return null;

		double tradedVolume = 0.0;
		double averagedPrice = 0.0;
		double firstTradedPrice = 0.0;
		double lastTradedPrice = 0.0;
		double maxTradedPrice = Double.MIN_VALUE;
		double minTradedPrice = Double.MAX_VALUE;

		int itemsCount = 0;

		for (T2 t : original) {

			if (itemsCount == 0)
				firstTradedPrice = t.getPrice();

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

		return createConsolidatedData(refTimestamp, original, firstTradedPrice, lastTradedPrice, maxTradedPrice, minTradedPrice, averagedPrice,
				tradedVolume);

	}

	/**
	 * Subclass to implement this function to "instantiate" the new
	 * ConsolidatedTradeRecordsData object. This function requires to subclass
	 * to look at the original iterable items and extract their common
	 * properties and use our calculated results
	 * (lastTradedPrice/maxTradedPrice/
	 * minTradedPrice/averagedPrice/tradedVolume) to instantiate the new
	 * ConsolidatedTradeRecordsData object.
	 * 
	 * @param refTimestamp
	 *            The timestamp to be used.
	 * @param original
	 *            The original iterable just in case the new object needs to
	 *            copy some values from it. Must not empty or null.
	 * @param firstTradedPrice
	 *            The computed first traded price based on the iterable.
	 * @param lastTradedPrice
	 *            The computed last traded price based on the iterable.
	 * @param maxTradedPrice
	 *            The max traded price computed based on the iterable.
	 * @param minTradedPrice
	 *            The min traded price computed based on the iterable.
	 * @param averagedPrice
	 *            The averaged price computed based on the iterable.
	 * @param tradedVolume
	 *            The traded volume computed based on the iterable.
	 * 
	 * @return A new instance of ConsolidatedTradeRecordsData
	 */
	protected abstract T1 createConsolidatedData(LocalDateTime refTimestamp, Iterable<T2> original, double firstTradedPrice, double lastTradedPrice,
			double maxTradedPrice, double minTradedPrice, double averagedPrice, double tradedVolume);

	/**
	 * Given a new reference timestamp and previous interval results,
	 * extrapolate a new result.
	 * 
	 * @param refTimestamp
	 *            Reference timestamp to be used.
	 * @param previousIntervalResults
	 *            Previous results computed.
	 * @return A new entry of T1 type.
	 */
	protected abstract T1 extrapolate(LocalDateTime refTimestamp, Iterable<T1> previousIntervalResults);

	/**
	 * Given a new reference timestamp and later interval results, extrapolate a
	 * new result in backward direction.
	 * 
	 * @param refTimestamp
	 *            Reference timestamp to be used.
	 * @param laterIntervalResults
	 *            The results after this given timestamp.
	 * @return A new entry of T1 type.
	 */
	protected abstract T1 backwardExtrapolate(LocalDateTime refTimestamp, Iterable<T1> laterIntervalResults);

}