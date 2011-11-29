package edu.gatech.cs7450.prodviz.imp.book.data;

import edu.gatech.cs7450.prodviz.data.Product;

public class Book extends Product {

	private int authorID;
	private int genreID;
	private String imageUrlS;
	private String imageUrlM;
	private String imageUrlL;
	
	public Book(String ID, String title, int authorID, int genreID, String imageUrlS, String imageUrlM, String imageUrlL) {
		super(ID, title, "genre", "author");
		this.authorID = authorID;
		this.genreID = genreID;
		this.imageUrlS = imageUrlS;
		this.imageUrlM = imageUrlM;
		this.imageUrlL = imageUrlL;
	}

	public int getAuthorID() {
		return authorID;
	}
	
	public String getTitle() {
		return this.getName();
	}

	public int getGenreID() {
		return genreID;
	}

	public String getImageUrlS() {
		return imageUrlS;
	}

	public String getImageUrlM() {
		return imageUrlM;
	}

	public String getImageUrlL() {
		return imageUrlL;
	}
}
