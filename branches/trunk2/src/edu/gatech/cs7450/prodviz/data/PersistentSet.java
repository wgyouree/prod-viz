package edu.gatech.cs7450.prodviz.data;

import java.util.HashSet;
import java.util.Set;

public class PersistentSet {

	private String name;
	private Set<PersistentObject> objects = new HashSet<PersistentObject>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<PersistentObject> getObjects() {
		return objects;
	}
	
	public void setObjects(Set<PersistentObject> objects) {
		this.objects = objects;
	}
}
