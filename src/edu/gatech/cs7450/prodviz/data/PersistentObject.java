package edu.gatech.cs7450.prodviz.data;

import java.util.HashSet;
import java.util.Set;

public class PersistentObject {

	private int ID;
	private Set<PersistentAttribute> attributes = new HashSet<PersistentAttribute>();
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return this.ID;
	}

	public Set<PersistentAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<PersistentAttribute> attributes) {
		this.attributes = attributes;
	}
}
