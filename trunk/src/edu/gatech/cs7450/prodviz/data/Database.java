package edu.gatech.cs7450.prodviz.data;

public class Database {

	private DatabaseConfig config;
	private boolean initialized = false;
	
	public Database(DatabaseConfig config) {
		this.config = config;
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
	
	public boolean isInitialized() {
		return this.initialized;
	}
}
