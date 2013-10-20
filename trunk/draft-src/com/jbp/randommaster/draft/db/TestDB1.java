package com.jbp.randommaster.draft.db;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;

public class TestDB1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		/*
		if (args.length!=2) {
			System.out.println("Please input username and password as command line arguments");
			return;
		}
		*/
		
		Class.forName("org.postgresql.Driver");
		
		Connection conn=DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/randommaster","randommaster","randommaster");

		conn.setAutoCommit(true);
		
		//Statement st=conn.createStatement();
		
/*
 * CREATE TABLE hkextrsource
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
		
		YearMonth ex=new YearMonth(2012,5);
		//String expiryStr = ex.toString("yyyyMM");
		//System.out.println(expiryStr);
		
		
		LocalDateTime tradeTime = new LocalDateTime();
		//String tradeTimeStr=tradeTime.toString("yyyyMMddHHmmss");
		//System.out.println(tradeTimeStr);
		
		
		PreparedStatement st=conn.prepareStatement("Insert into hkextrsource values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		
		for (int i = 0; i < 2000000; i++) {
			
			byte[] hash=MessageDigest.getInstance("SHA1").digest(UUID.randomUUID().toString().getBytes("ISO-8859-1"));
			String key=Base64.encodeBase64String(hash);
			
			/*
			String sql = "insert into hkextrsource values('"
					+ key
					+ "','hsi','F',to_timestamp('" + expiryStr
					+ "', 'YYYYMM'),123.4,'C', to_timestamp('" + tradeTimeStr
					+ "','YYYYMMDDHH24MISS'), 333.88,20,'def')";
					*/
			
			
			st.setString(1, key);
			st.setString(2, "HSI");
			st.setString(3,  "F");
			st.setTimestamp(4, new Timestamp(ex.toLocalDate(1).toDate().getTime()));
			//st.setDouble(5, 123.4);
			st.setBigDecimal(5, new BigDecimal("123.4"));
			st.setString(6, "C");
			st.setTimestamp(7, new Timestamp(tradeTime.toDate().getTime()));
			//st.setDouble(8, 333.88);
			st.setBigDecimal(8, new BigDecimal("334.81"));
			//st.setDouble(9, 20);
			st.setBigDecimal(9, new BigDecimal("20"));
			st.setString(10, "fff");
			

			// System.out.println("Executing sql: "+sql);
			//int row = st.executeUpdate(sql);
			st.executeUpdate();

			// System.out.println("return value: "+row);
			
			int rowCount = i+1;
			if (rowCount%10000==0)
				System.out.println("processed "+rowCount);
			
		}
		st.close();
		
		conn.close();
		

	}

}
