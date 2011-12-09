package edu.gatech.cs7450.prodviz.imp.book.data;

import java.util.Set;

import edu.gatech.cs7450.prodviz.data.PersistentObject;

public class Author extends PersistentObject {

	public Author(String ID, String name, Set<Book> books) {
		super(ID);
		
	}
}
