package edu.gatech.cs7450.prodviz.gui.viz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.JComponent;

import prefuse.data.Table;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;

public class MapGenerator {

	public static final String STATES = "data/stateData.txt";
	
	private static java.util.Map<String, String> latitude;
	private static java.util.Map<String, String> longitude;
    
	public static JComponent generateMap(LocationRatingPair[] ratings) {
		
		if (latitude == null || longitude == null) {
			populateTables();
		}
		
		// create table
		Table t = new Table();
		t.addColumn("state", String.class);
		t.addColumn("rating", String.class);
		t.addColumn("latitude", String.class);
		t.addColumn("longitude", String.class);
		
		for (int i = 0; i < ratings.length; i++) {
			int rowNum = t.addRow();
			String state = ratings[i].getLocation();
			t.setString(rowNum, 0, state);
			t.setString(rowNum, 1, "" + (new Double(ratings[i].getRating()).intValue()));
			t.setString(rowNum, 2, latitude.get(state));
			t.setString(rowNum, 3, longitude.get(state));
		}
		
		// create Map
        Map zd = new Map(t);
        
        return zd;
	}
	
	private static void populateTables() {
		
		try {
			latitude = new HashMap<String, String>();
			longitude = new HashMap<String, String>();

	        BufferedReader reader = new BufferedReader(new FileReader(STATES));
	        String line = reader.readLine();
	        while(line != null) {
	        	String[] parts = line.split("[,]");
	        	latitude.put(parts[0], parts[1]);
	        	longitude.put(parts[0], parts[2]);
	        	line = reader.readLine();
	        }
	        reader.close();
			System.err.println("READ THE FILE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
