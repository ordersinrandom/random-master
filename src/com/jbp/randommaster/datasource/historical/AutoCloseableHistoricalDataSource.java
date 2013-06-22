package com.jbp.randommaster.datasource.historical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * A HistoricalDataSource that supports AutoCloseable and proper handling on the looping-break cases.
 *
 * @param <T> Some generic HistoricalData
 */
public abstract class AutoCloseableHistoricalDataSource<T extends HistoricalData> implements HistoricalDataSource<T>, AutoCloseable {

	static Logger log=Logger.getLogger(AutoCloseableHistoricalDataSource.class);
	
	// holding list of iterators to ensure it will be cleaned up finally.
	private Collection<AutoCloseableIterator<T>> finalCleanUpList;	
	
	public AutoCloseableHistoricalDataSource() {
		finalCleanUpList=new ArrayList<AutoCloseableIterator<T>>();
	}

	/**
	 * Implementation of the AutoCloseable interface.
	 */
	@Override
	public void close() {
		for (AutoCloseableIterator<T> it : finalCleanUpList) {
			if (!it.isClosed()) {
				try {
					it.close();
				} catch (Exception e1) {
					// just log and ignore.
					log.warn("Unable to close one of the iterator: "+it, e1);
				}
			}
		}
		finalCleanUpList.clear();
	}

	/**
	 * Helper function to unit test to ensure the clean up is working fine.
	 */
	public boolean requiresCleanUp() {
		for (AutoCloseableIterator<T> it : finalCleanUpList) {
			if (!it.isClosed())
				return true;
		}
		return false;
	}
	

	/**
	 * Implementation of HistoricalDataSource interface.
	 */
	@Override
	public final Iterable<T> getData() {
		
		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				AutoCloseableIterator<T> it = getDataIterator();
				
				// save down the iterator to ensure it will be in the final clean up
				finalCleanUpList.add(it);
				
				return it;
			}
		};
		
	}	
	
	
	/**
	 * Subclass to implement this for help spawning iterators.
	 */
	protected abstract AutoCloseableIterator<T> getDataIterator(); 
}
