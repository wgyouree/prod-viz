package edu.gatech.cs7450.prodviz.data;

import java.util.HashMap;
import java.util.Map;

public class Review extends PersistentObject {

	public Review(String ID, int userID, int rating, Product product) {
		super(ID);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("userID", userID);
		attributes.put("rating", rating);
		attributes.put("product", product);
		this.setAttributes(attributes);
	}
	
	public int getUserID() {
		return (Integer)this.getAttributes().get("userID");
	}
	
	public int getRating() {
		return (Integer)this.getAttributes().get("rating");
	}
	
	public Product getProduct() {
		return (Product)this.getAttributes().get("product");
	}
}
