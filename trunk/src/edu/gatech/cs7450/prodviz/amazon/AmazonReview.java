package edu.gatech.cs7450.prodviz.amazon;

public class AmazonReview {

	private String name = "";
	private String contents = "";
	
	public AmazonReview() { }
	
	public String getName() {
		return this.name;
	}
	
	public String getContents() {
		return this.contents;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
}
