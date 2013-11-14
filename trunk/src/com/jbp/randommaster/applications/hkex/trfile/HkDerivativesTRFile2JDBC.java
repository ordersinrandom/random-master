package com.jbp.randommaster.applications.hkex.trfile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.jbp.randommaster.database.MasterDatabaseConnections;
import com.jbp.randommaster.datasource.historical.HkDerivativesTR;

/**
 * 
 * HkDerivativesTRFile2JDBC takes a folder of ZIPPED HKEX TR CSV Files and
 * insert them into JDBC database.
 * 
 */
public class HkDerivativesTRFile2JDBC extends HkDerivativesTRZipFilesProcessor {

	static Logger log = Logger.getLogger(HkDerivativesTRFile2JDBC.class);

	private MasterDatabaseConnections connectionSrc;
	
	private int numberOfRowsUpdated;

	public HkDerivativesTRFile2JDBC(File inputFolder, MasterDatabaseConnections connectionSrc) {
		super(inputFolder);
		this.connectionSrc = connectionSrc;
		numberOfRowsUpdated=0;
	}

	@Override
	protected void processHkDerivativesTRInput(File srcZipFile, String zipEntryKey, Iterable<HkDerivativesTR> loadedData) {
		
		String sql="Insert into hkextrsource(entryhash,underlying,futuresoptions,expirymonth,strike,callput,tradetimestamp,price,quantity,tradetype) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
/*
CREATE TABLE hkextrsource
(
  entryhash character varying(30) NOT NULL,
  underlying character varying(10) NOT NULL,
  futuresoptions character varying(1) NOT NULL,
  expirymonth timestamp without time zone NOT NULL,
  strike numeric,
  callput character varying(1),
  tradetimestamp timestamp without time zone NOT NULL,
  price numeric,
  quantity numeric,
  tradetype character varying(4),
		
 */
		
		
		try (Connection conn=connectionSrc.getConnection();
				PreparedStatement st=conn.prepareStatement(sql);) {
			
			for (HkDerivativesTR tr : loadedData) {
			
				byte[] hash=MessageDigest.getInstance("SHA1").digest(UUID.randomUUID().toString().getBytes("ISO-8859-1"));
				String key=Base64.encodeBase64String(hash);
				
				st.setString(1, key);
				st.setString(2, tr.getUnderlying());
				st.setString(3,  tr.getFuturesOrOptions());
				st.setTimestamp(4, new Timestamp(tr.getExpiryMonth().toLocalDate(1).toDate().getTime()));
				st.setDouble(5, tr.getStrikePrice());
				st.setString(6, tr.getCallPut());
				st.setTimestamp(7, new Timestamp(tr.getTradeTimestamp().toDate().getTime()));
				st.setDouble(8, tr.getPrice());
				st.setDouble(9, tr.getQuantity());
				st.setString(10, tr.getTradeType());

				int row = st.executeUpdate();
				numberOfRowsUpdated += row;
			}
			
		} catch (SQLException sqle) {
			log.fatal("SQL Exception. Unable to update database", sqle);
		} catch (NoSuchAlgorithmException e) {
			log.fatal("Unable to get hashing algorithm.", e);
		} catch (UnsupportedEncodingException e) {
			log.fatal("Unable to convert UUID bytes to ISO-8859-1 string format", e);
		}

	}
	
	
	public int getRowsUpdated() {
		return numberOfRowsUpdated;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
