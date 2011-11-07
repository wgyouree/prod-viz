package edu.gatech.cs7450.prodviz.data;

public interface DatabaseConfig {

	public String getDriver();
	public String getUrl();
	public String getDatabaseName();
	public String getUsername();
	public String getPassword();
	
	public void bootstrapDatabase();
}
