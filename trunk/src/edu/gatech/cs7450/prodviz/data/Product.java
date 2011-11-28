package edu.gatech.cs7450.prodviz.data;

public class Product extends PersistentObject {

	private String name;
	
	public Product(String ID, String name) {
		super(ID);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
