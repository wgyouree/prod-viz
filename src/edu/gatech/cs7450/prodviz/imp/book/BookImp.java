package edu.gatech.cs7450.prodviz.imp.book;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Classifier;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.IDatabaseConfig;

public class BookImp extends AbstractProduct {

	private String name = "Books";
	private Database database;
	
	public BookImp() {
		// Create database interface
		IDatabaseConfig config = new DatabaseConfig(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/",
				"books",
				"admin",
				"admin");
		this.database = new Database(config);
		this.firstLevelClassifier = new Classifier("Genre");
		this.secondLevelClassifier = new Classifier("Author");
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
