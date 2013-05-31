package com.jbp.randommaster.datasource.historical;

import com.jbp.randommaster.datasource.FinancialDataSource;


/**
 * 
 * Responsible for getting historical price data from any arbitrary source.
 *
 * @param <T> The actual collection of data object getting loaded.
 */
public interface HistoricalDataSource<T extends HistoricalData<? extends HistoricalDataTuple>> extends FinancialDataSource {

	public Iterable<T> getData() throws HistoricalDataSourceException;
}
