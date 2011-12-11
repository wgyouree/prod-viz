package edu.gatech.cs7450.prodviz.data;

public class AgeRatingPair {

	private int age;
	private double rating;
	
	public AgeRatingPair(int age, double rating) {
		this.age = age;
		this.rating = rating;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}
