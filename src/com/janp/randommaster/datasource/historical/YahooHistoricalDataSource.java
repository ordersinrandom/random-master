package com.janp.randommaster.datasource.historical;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;

public class YahooHistoricalDataSource {

	private String yqlPattern=
			"http://query.yahooapis.com/v1/public/yql?q={YQL}&env=http%3A%2F%2Fdatatables.org%2Falltables.env";
	
	// select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%220005.HK%22%20and%20startDate%20%3D%20%222009-09-11%22%20and%20endDate%20%3D%20%222010-03-10%22
	
	private String yahooSymbol;
	private List<String> yqls;
	
	public YahooHistoricalDataSource(String yahooSymbol, LocalDate startDate, LocalDate endDate) {
		this.yahooSymbol=yahooSymbol;
		yqls=new LinkedList<String>();
		
		String queryPattern="select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22{SYMBOL}%22%20and%20startDate%20%3D%20%22{STARTDATE}%22%20and%20endDate%20%3D%20%22{ENDDATE}%22";
		
		
		// TODO: split the start date and end date into years
		// and create multiple queries which in turns generates multiple yqls
		
	}
	
}
