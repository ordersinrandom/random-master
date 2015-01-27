package com.jbp.randommaster.draft.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSqlite {

	public static void main(String[] args) throws SQLException {

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/temp/mydatabase.db");) {
			
			conn.setAutoCommit(false);

			try (Statement st = conn.createStatement();) {
				st.executeUpdate("Create table TestTable(x integer, y text)");
				for (int i=0;i<1000000;i++) {
					st.executeUpdate("insert into TestTable values("+i+",'text "+i+"')");
				}
				conn.commit();
			}
		}

	}

}
