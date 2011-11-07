package edu.gatech.cs7450.prodviz.data;

public class Database {

	private String driver;
	private String url;
	private String databaseName;
	private String username;
	private String password;
	private boolean initialized = false;
	
	public Database(String driver, String url, String databaseName, String username, String password) {
		this.driver = driver;
		this.url = url;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}
	
	public boolean initialize() {
		try {
			Class.forName(driver);
			this.initialized = true;
			return true;
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
}
