package com.jbp.randommaster.datasource.historical;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;

/**
 * 
 * The time interval consolidated TR source.
 *
 * @param <T1> A ConsolidatedTradeRecordsData instance.
 * @param <T2> A TradeRecordData instance.
 */
public class TimeIntervalConsolidatedTRSource<T1 extends ConsolidatedTradeRecordsData, T2 extends TradeRecordData> implements HistoricalDataSource<T1> {

	private TradeRecordsConsolidator<T1, T2> consolidator;
	private HistoricalDataSource<T2> dataSource;
	private LocalDateTime start;
	private LocalDateTime end;
	private Period interval;
	
	/**
	 * Create an instance of <code>TimeIntervalConsolidatedTRSource</code>
	 * 
	 */
	public TimeIntervalConsolidatedTRSource(
			TradeRecordsConsolidator<T1, T2> consolidator, 
			HistoricalDataSource<T2> dataSource,
			LocalDateTime start, LocalDateTime end, Period interval) {
		
		this.consolidator=consolidator;
		this.dataSource=dataSource;
		this.start=start;
		this.end=end;
		this.interval=interval;
	}
	
	@Override
	public Iterable<T1> getData() {
		
		Iterable<T1> consolidatedResult=consolidator.consolidateByTimeIntervals(
				start, end, interval, dataSource.getData());			

		return consolidatedResult;
	}

	
	
}
