package com.jbp.randommaster.datasource.historical;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FilteredHistoricalDataSource<T extends HistoricalData<? extends HistoricalDataTuple>> implements HistoricalDataSource<T> {

	protected HistoricalDataSource<T> source;
	protected HistoricalDataFilter<T> filter;

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

	@Override
	public Iterable<T> getData() {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new FilteredIterator();
			}
		};
	}

	private class FilteredIterator implements Iterator<T> {

		private Iterator<T> nestedIt;
		private List<T> buffer; // stores only valid entry (i.e. passed the filter)

		public FilteredIterator() {
			nestedIt = getSource().getData().iterator();
			buffer = new LinkedList<T>();
		}

		@Override
		public boolean hasNext() {
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

			return !buffer.isEmpty();
		}

		@Override
		public T next() {
			if (!buffer.isEmpty())
				return buffer.remove(0);
			else
				return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported for FilteredHistoricalDataSource");
		}

	}

}
