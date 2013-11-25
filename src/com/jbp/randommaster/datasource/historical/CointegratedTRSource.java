package com.jbp.randommaster.datasource.historical;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.javatuples.Pair;

/**
 * 
 * A data source implementation that takes a number of <code>TimeIntervalConsolidatedTRSource</code>
 * and weights, and output a cointegrated time series data source.
 * 
 * Subclass is expected to implement the cointegration operation of the given data and weight.
 *
 * @param <T> The consolidated trade records data type. It has to be the same for input and output types.
 */
public abstract class CointegratedTRSource<T extends TimeConsolidatedTradeRecord> implements HistoricalDataSource<T> {

	/**
	 * All sources. List of sources to weight pairs.
	 */
	private List<Pair<TimeIntervalConsolidatedTRSource<T, ? extends TradeRecordData>, Double>> sources;
	
	/**
	 * Create a new instance of cointegrated TR source.
	 */
	public CointegratedTRSource() {
		sources = new LinkedList<Pair<TimeIntervalConsolidatedTRSource<T, ? extends TradeRecordData>, Double>>();
	}
	
	/**
	 * Add a new consolidated trade record source, with a given weight.
	 * 
	 * @param src A TimeIntervalConsolidatedTRSource instance.
	 * @param weight The weight to cointegrate with others.
	 */
	public void addSource(TimeIntervalConsolidatedTRSource<T, ? extends TradeRecordData> src, double weight) {
		sources.add(new Pair<TimeIntervalConsolidatedTRSource<T, ? extends TradeRecordData>, Double>(src, weight));
	}
	
	/**
	 * Subclass to implement the cointegrate and create a data object based on the given existing data and weights.
	 * 
	 * @param allDataAndWeight All pairs of data and weight items. 
	 * @return A cointegrated item T.
	 */
	protected abstract T cointegrate(Iterable<Pair<T, Double>> allDataAndWeight);
	
	/**
	 * Implementation of HistoricalDataSource interface.
	 */
	@Override
	public Iterable<T> getData() {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new MultipleSourcesIterator();
			}
		};
	}
	
	/**
	 * 
	 * Inner class that takes multiple TimeIntervalConsolidatedTRSource objects and output 
	 * a cointegrated data series.
	 *
	 */
	private class MultipleSourcesIterator implements Iterator<T> {

		// list of iterators to weight.
		private List<Pair<Iterator<T>, Double>> iterators;
		
		/**
		 * Create a new instance of MultipleSourcesIterator.
		 */
		public MultipleSourcesIterator() {
			iterators=new LinkedList<Pair<Iterator<T>, Double>>();
			// instantiate an iterator on each sources
			for (Pair<TimeIntervalConsolidatedTRSource<T, ? extends TradeRecordData>, Double> p : sources) {
				Iterator<T> it=p.getValue0().getData().iterator();
				double weight = p.getValue1();
				
				iterators.add(new Pair<Iterator<T>, Double>(it, weight));
			}
		}

		@Override
		public boolean hasNext() {
			if (iterators.isEmpty())
				return false;
			else {
				// return true only if all iterators hasNext() return true.
				boolean result = true;
				for (Pair<Iterator<T>, Double> p : iterators) {
					if (!p.getValue0().hasNext()) {
						result=false;
						break;
					}
				}
				return result;
			}
		}

		@Override
		public T next() {
			
			List<Pair<T, Double>> allDataAndWeight=new LinkedList<Pair<T, Double>>();
			
			for (Pair<Iterator<T>, Double> p : iterators) {
				// get a data from each source and weight.
				T data = p.getValue0().next();
				double weight = p.getValue1();
				
				allDataAndWeight.add(new Pair<T, Double>(data, weight));
			}
			
			// ask subclass to cointegrate and return the result.
			T result = cointegrate(allDataAndWeight);
			
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("MultipleSourcesIterator does not support remove function");
		}

		
	}

}
