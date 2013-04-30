package com.jbp.randommaster.datasource.historical;

import java.util.Collection;

import com.jbp.randommaster.datasource.FinancialDataSource;

public interface HistoricalDataSource extends FinancialDataSource {

	public Collection<HistoricalData> getData();
}
