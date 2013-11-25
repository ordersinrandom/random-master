package com.jbp.randommaster.datasource.historical.filters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jbp.randommaster.datasource.historical.AutoCloseableHistoricalDataSource;
import com.jbp.randommaster.datasource.historical.AutoCloseableIterator;
import com.jbp.randommaster.datasource.historical.HistoricalData;
import com.jbp.randommaster.datasource.historical.HistoricalDataSource;

/**
 * 
 * A generic filtered data source that accept some filtering logic on an existing data source.
 *
 * @param <T> The HistoricalData type.
 */
public class FilteredHistoricalDataSource<T extends HistoricalData> extends AutoCloseableHistoricalDataSource<T>  {

	protected HistoricalDataSource<T> source;
	protected HistoricalDataFilter<T> filter;

	/**
	 * Create an instance of FilteredHistoricalDataSource
	 * @param src The original data source
	 * @param filter The filtering logic.
	 */
	public FilteredHistoricalDataSource(HistoricalDataSource<T> src, HistoricalDataFilter<T> filter) {
		// check arguments
		if (src == null)
			throw new IllegalArgumentException("Source cannot be null");
		if (filter == null)
			throw new IllegalArgumentException("Filter cannot be null");

		this.source = src;
		this.filter = filter;
	}

	public HistoricalDataSource<T> getSource() {
		return source;
	}

	public HistoricalDataFilter<T> getFilter() {
		return filter;
	}


	/**
	 * Implementation of AutoCloseableHistoricalDataSource.
	 */
	@Override
	protected AutoCloseableIterator<T> getDataIterator() {
		return new FilteredIterator();
	}	
	
	private class FilteredIterator implements AutoCloseableIterator<T> {

		private Iterator<T> nestedIt;
		private List<T> buffer; // stores only valid entry (i.e. passed the filter)

		public FilteredIterator() {
			nestedIt = getSource().getData().iterator();
			buffer = new LinkedList<T>();
		}

		@Override
		public boolean hasNext() {
			if (nestedIt==null)
				return false;
			
			// if there is still a buffered item, we have "next"
			if (!buffer.isEmpty())
				return true;

			while (buffer.isEmpty() && nestedIt.hasNext()) {
				T data = nestedIt.next();
				// if we found an item that's accepted, we add to the buffer and
				// leave this loop
				if (getFilter().accept(data))
					buffer.add(data);
			}

			boolean result = !buffer.isEmpty();
			if (!result) 
				close();
			
			return result;
		}

		@Override
		public T next() {
			if (nestedIt==null)
				return null;
			
			if (!buffer.isEmpty())
				return buffer.remove(0);
			else
				return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for FilteredHistoricalDataSource");
		}

		/**
		 * Implementation of AutoCloseableIterator.
		 */
		@Override
		public boolean isClosed() {
			return nestedIt==null;
		}

		/**
		 * Implementation of AutoCloseableIterator.
		 */
		@Override
		public void close() {
			if (nestedIt instanceof AutoCloseableIterator<?>) {
				AutoCloseableIterator<?> i = (AutoCloseableIterator<?>) nestedIt;
				if (!i.isClosed())
					i.close();
			}
			nestedIt=null;
		}

	}


}
