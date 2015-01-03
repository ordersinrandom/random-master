package com.jbp.randommaster.datasource.historical;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.jbp.randommaster.database.MasterDatabaseConnections;

public class MasterDatabasePriceSource extends AutoCloseableHistoricalDataSource<MasterDBConsolidatedTradeRecord> {

	private static final String price5minSql = "select instrumentcode,recordtimestamp,open,high,low,close,average,tradedvolume from price5min where instrumentcode=? and recordtimestamp between ? and ?";
	
	private MasterDatabaseConnections connections;
	private String instrumentCode;
	private Timestamp startTime;
	private Timestamp endTime;
	
	public MasterDatabasePriceSource(MasterDatabaseConnections conn, String instrumentCode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.connections = conn;
		this.instrumentCode = instrumentCode;
		this.startTime = Timestamp.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
		this.endTime = Timestamp.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	protected AutoCloseableIterator<MasterDBConsolidatedTradeRecord> getDataIterator() {
		return new PriceTableIterator();
	}
	
	private class PriceTableIterator implements AutoCloseableIterator<MasterDBConsolidatedTradeRecord> {

		private Connection conn;
		private ResultSet resultSet;
		private PreparedStatement pstat;
		private boolean currentCursorRecordDone;
		private boolean noMoreResult;
		
		public PriceTableIterator() {
			try {
				conn = connections.getConnection();
				
				pstat = conn.prepareStatement(price5minSql);
				pstat.setString(1, instrumentCode);
				pstat.setTimestamp(2, startTime);
				pstat.setTimestamp(3, endTime);
				resultSet = pstat.executeQuery();
				
				
				currentCursorRecordDone=false;
				noMoreResult = false;
				
			} catch (SQLException sqle) {
				throw new RuntimeException("Unable to retrieve result set from master DB", sqle);
			} 
		}
		

		@Override
		public void remove() {
			throw new UnsupportedOperationException("MasterDatabasePriceSource.PriceTableIterator does not support remove operation");
		}


		@Override
		public boolean hasNext() {
			if (noMoreResult)
				return false;
			try {
				boolean result = resultSet.next();
				if (!result) 
					noMoreResult = true;
				
				currentCursorRecordDone = false;
				return result;
			} catch (SQLException sqle) {
				throw new RuntimeException("unable to seek resultSet cursor forward in hasNext()", sqle);
			}
		}


		@Override
		public MasterDBConsolidatedTradeRecord next() {
			
			if (noMoreResult)
				return null;
			
			//instrumentcode,recordtimestamp,open,high,low,close,average,tradedvolume
			try {
				if (currentCursorRecordDone) {
					boolean r = resultSet.next();
					if (r==false) {
						noMoreResult = true;
						return null;
					}
					
					currentCursorRecordDone=false;
				}
				
				// not used
				//String instrumentCode = resultSet.getString(1);
				Timestamp recordTimestamp = resultSet.getTimestamp(2);
				double open = resultSet.getDouble(3);
				double high = resultSet.getDouble(4);
				double low = resultSet.getDouble(5);
				double close = resultSet.getDouble(6);
				double average = resultSet.getDouble(7);
				double tradedVol = resultSet.getDouble(8);
				
				MasterDBConsolidatedTradeRecord record = new MasterDBConsolidatedTradeRecord(
						LocalDateTime.ofInstant(recordTimestamp.toInstant(), ZoneId.systemDefault()), open, close, tradedVol, high, low, average);
				
				currentCursorRecordDone=true;
				
				return record;
				
			} catch (SQLException sqle) {
				throw new RuntimeException("unable to read data from master database in next()", sqle);
			}
		}


		@Override
		public boolean isClosed() {
			return conn==null;
		}


		@Override
		public void close() {
			try {
				if (pstat!=null)
					pstat.close();
				
				if (resultSet!=null)
					resultSet.close();
				
				if (conn!=null)
					conn.close();
			} catch (SQLException sqle) {
				throw new RuntimeException("unable to close resources associated with master db", sqle);
			} finally {
				resultSet = null;
				conn = null;
			}
				
		}
		
	}



	
}
