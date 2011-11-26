package edu.gatech.cs7450.prodviz.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User extends PersistentObject {

	public User(int ID, String name) {
		this.setID(ID);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", name);
		attributes.put("reviews", new HashSet<Review>());
		this.setAttributes(attributes);
	}
	
	public String getName() {
		return (String)this.getAttributes().get("name");
	}
	
	public void addReview(Review review) {
		((Set<Review>)this.getAttributes().get("reviews")).add(review);
	}
	
	public Set<Review> getReviews() {
		return (Set<Review>)this.getAttributes().get("reviews");
	}
	
	public void setReviews(Set<Review> reviews) {
		this.getAttributes().remove("reviews");
		this.getAttributes().put("reviews", reviews);
	}
}
