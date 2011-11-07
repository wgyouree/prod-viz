package edu.gatech.cs7450.prodviz.imp.book;

import edu.gatech.cs7450.prodviz.ProductImp;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;

public class BookImp implements ProductImp {

	private String name = "Book";
	private Database database;
	
	public BookImp() {
		// Create database interface
		DatabaseConfig config = new BookDatabaseConfig(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/",
				"book",
				"admin",
				"admin");
		this.database = new Database(config);
	}
	
	@Override
	public Database getDatabase() {
		return this.database;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}
