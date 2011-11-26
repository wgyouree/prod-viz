package edu.gatech.cs7450.prodviz.imp.book.data;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs7450.prodviz.data.PersistentObject;

public class Genre extends PersistentObject {

	public Genre(int ID, String name) {
		this.setID(ID);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", name);
		this.setAttributes(attributes);
	}
	
	public String getName() {
		return (String)this.getAttributes().get("name");
	}
}
