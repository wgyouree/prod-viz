package edu.gatech.cs7450.prodviz.data;


public abstract class AbstractProduct {

	private String name;
	private Database database;
	private Classifier firstLevelClassifier;
	private Classifier secondLevelClassifier;
	private IProductTableSchema productTableSchema;
	private IReviewTableSchema reviewTableSchema;
	private IUserTableSchema userTableSchema;
	
	protected AbstractProduct(
			String name, Classifier firstLevelClassifier, Classifier secondLevelClassifier,
			IProductTableSchema productTableSchema, IReviewTableSchema reviewTableSchema, IUserTableSchema userTableSchema) {
		this.name = name;
		this.firstLevelClassifier = firstLevelClassifier;
		this.secondLevelClassifier = secondLevelClassifier;
		this.productTableSchema = productTableSchema;
		this.reviewTableSchema = reviewTableSchema;
		this.userTableSchema = userTableSchema;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public Classifier getFirstLevelClassifier() {
		return firstLevelClassifier;
	}

	public Classifier getSecondLevelClassifier() {
		return secondLevelClassifier;
	}

	public IProductTableSchema getProductTableSchema() {
		return productTableSchema;
	}

	public IReviewTableSchema getReviewTableSchema() {
		return reviewTableSchema;
	}

	public IUserTableSchema getUserTableSchema() {
		return userTableSchema;
	}

	public String toString() {
		return this.getName();
	}
	
	public String getName() {
		return name;
	}
	
	protected void setDatabase(Database database) {
		this.database = database;
	}
}
