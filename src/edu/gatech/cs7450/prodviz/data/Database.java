package edu.gatech.cs7450.prodviz.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private IDatabaseConfig config;
	private boolean initialized = false;
	
	public Database(IDatabaseConfig config) {
		this.config = config;
		this.initialize();
	}
	
	public boolean initialize() {
		try {
			Class.forName(this.config.getDriver());
			this.initialized = true;
			return true;
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				config.getUrl() + config.getDatabaseName(),
				config.getUsername(),
				config.getPassword());
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
}
