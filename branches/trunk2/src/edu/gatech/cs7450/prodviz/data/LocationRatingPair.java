package edu.gatech.cs7450.prodviz.data;

public class LocationRatingPair {

	private String location;
	private double rating;
	
	public LocationRatingPair(String location, double rating) {
		this.location = location;
		this.rating = rating;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}
