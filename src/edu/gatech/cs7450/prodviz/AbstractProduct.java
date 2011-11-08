package edu.gatech.cs7450.prodviz;

import edu.gatech.cs7450.prodviz.data.Database;

public abstract class AbstractProduct {

	public abstract String getName();
	public abstract Database getDatabase();
	
	public String toString() {
		return this.getName();
	}
}
