package edu.gatech.cs7450.prodviz.data;

public class Product extends PersistentObject {

	private String name;
	private String firstLevelClassifier;
	private String secondLevelClassifier;
	
	public Product(String ID, String name, String firstLevelClassifier, String secondLevelClassifier) {
		super(ID);
		this.name = name;
		this.firstLevelClassifier = firstLevelClassifier;
		this.secondLevelClassifier = secondLevelClassifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLevelClassifier() {
		return firstLevelClassifier;
	}

	public String getSecondLevelClassifier() {
		return secondLevelClassifier;
	}
}
