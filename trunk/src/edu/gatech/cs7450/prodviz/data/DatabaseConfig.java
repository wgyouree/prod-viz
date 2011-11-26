package edu.gatech.cs7450.prodviz.data;


public class DatabaseConfig implements IDatabaseConfig {

	private String driver;
	private String url;
	private String databaseName;
	private String username;
	private String password;
	
	public DatabaseConfig(String driver, String url, String databaseName, String username, String password) {
		this.driver = driver;
		this.url = url;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String getDriver() {
		return this.driver;
	}
	
	@Override
	public String getUrl() {
		return this.url;
	}
	
	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public void bootstrapDatabase() {
		// TODO Auto-generated method stub
		
	}
}
