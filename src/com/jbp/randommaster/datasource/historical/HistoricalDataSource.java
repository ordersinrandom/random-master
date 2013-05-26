package com.jbp.randommaster.datasource.historical;

import java.util.Collection;

import com.jbp.randommaster.datasource.FinancialDataSource;

public interface HistoricalDataSource<T extends HistoricalData<? extends HistoricalDataTuple>> extends FinancialDataSource {

	public Collection<T> getData();
}
