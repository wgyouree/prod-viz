package edu.gatech.cs7450.prodviz.data;

import java.util.Map;

public class PersistentObject {

	private String ID;
	private Map<String, Object> attributes;
	
	public PersistentObject(String ID) {
		this.ID = ID;
	}
	
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public String getID() {
		return this.ID;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
