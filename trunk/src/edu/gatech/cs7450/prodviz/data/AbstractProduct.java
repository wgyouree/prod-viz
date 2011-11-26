package edu.gatech.cs7450.prodviz.data;


public abstract class AbstractProduct {

	protected Classifier firstLevelClassifier;
	protected Classifier secondLevelClassifier;
	
	public abstract String getName();
	
	public abstract Database getDatabase();
		
	public String toString() {
		return this.getName();
	}
	
	public Classifier getFirstLevelClassifier() {
		return firstLevelClassifier;
	}

	public Classifier getSecondLevelClassifier() {
		return secondLevelClassifier;
	}
}
