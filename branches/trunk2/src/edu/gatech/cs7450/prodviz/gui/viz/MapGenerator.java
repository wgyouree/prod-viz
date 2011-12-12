package edu.gatech.cs7450.prodviz.gui.viz;

import javax.swing.JComponent;

import prefuse.data.Table;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;

public class MapGenerator {

	public static JComponent generateMap(LocationRatingPair[] ratings) {
		
		// create table
		Table t = new Table();
		t.addColumn("State", String.class);
		t.addColumn("Rating", Double.class);
		
		for (int i = 0; i < ratings.length; i++) {
			int rowNum = t.addRow();
			t.setString(rowNum, 1, ratings[i].getLocation());
			t.setDouble(rowNum, 2, ratings[i].getRating());
		}
		
		// create Map
        Map zd = new Map(t);
        
        return zd;
	}
	
	
}
