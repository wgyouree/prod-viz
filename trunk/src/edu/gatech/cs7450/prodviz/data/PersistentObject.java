package edu.gatech.cs7450.prodviz.data;

import java.util.Map;

public class PersistentObject {

	private int ID;
	private Map<String, Object> attributes;
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return this.ID;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
