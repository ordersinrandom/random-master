package com.jbp.randommaster.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * 
 * MasterDatabase is a simple implementation that encapsulates the jdbc connection
 * to the master database.
 * 
 */
public class MasterDatabaseConnections {

	static Logger log = Logger.getLogger(MasterDatabaseConnections.class);

	private String connectionString;
	private String user;
	private String password;

	public MasterDatabaseConnections(String configFilePath)
			throws ConfigurationException, ClassNotFoundException {

		// e.g. conf/jdbc.xml
		XMLConfiguration config = new XMLConfiguration(configFilePath);

		String driverName = config.getString("driver");
		String connStr = config.getString("connection-string");
		String user = config.getString("user");
		String password = config.getString("password");

		log.info("Loading driver: " + driverName);
		Class.forName(driverName);

		log.info("Connection String: " + connStr);
		this.connectionString = connStr;
		this.user = user;
		this.password = password;
		
		log.info("user: "+user);

	}
	
	public Connection getConnection() throws SQLException {
		Connection conn=DriverManager.getConnection(connectionString, user, password);
		return conn;
	}

}
