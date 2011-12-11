package edu.gatech.cs7450.prodviz.gui.us.map;

import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

public class USState {

	private String name;
	private ArrayList<GeoPosition> gp;
	public USState(String name, ArrayList<GeoPosition> gp)
	{
		this.name = name;
		this.gp = gp;
	}
	
	public String getStateName(){
		return name;
	}
	public ArrayList<GeoPosition> getStatePoints(){
		return gp;
	}
}
