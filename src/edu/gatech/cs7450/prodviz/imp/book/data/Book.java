package edu.gatech.cs7450.prodviz.imp.book.data;

import edu.gatech.cs7450.prodviz.data.Product;


public class Book extends Product {

	private String author;
	
	public Book(int ID, String title, String author) {
		super(ID, title);
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
