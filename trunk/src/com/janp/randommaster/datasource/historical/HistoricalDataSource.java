package com.janp.randommaster.datasource.historical;

import java.util.Collection;

import com.janp.randommaster.datasource.FinancialDataSource;

public interface HistoricalDataSource extends FinancialDataSource {

	public Collection<HistoricalData> getData();
}
