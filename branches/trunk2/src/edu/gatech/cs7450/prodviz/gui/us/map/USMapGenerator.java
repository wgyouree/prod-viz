package edu.gatech.cs7450.prodviz.gui.us.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;


import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;
import edu.gatech.cs7450.prodviz.data.Review;

public class USMapGenerator {

	private static ArrayList<USState> regions;
	private static final String[] stateNames  = {"alabama", "alaska", "arizona", "arkansas", "california", "colorado", "connecticut",
		"delaware", "florida", "georgia", "hawaii", "idaho", "illinois", "indiana", "iowa", "kansas", 
		"kentucky", "louisiana", "maine", "maryland", "massachusetts", "michigan", "minnesota", 
		"mississippi", "missouri", "montana", "nebraska","nevada", "new hampshire", "new jersey", 
		"new mexico", "new york", "north carolina", "north dakota", "ohio", "oklahoma", "oregon", 
		"pennsylvania", "rhode island", "south carolina", "south dakota", "tennessee", "texas", 
		"utah", "vermont", "virginia", "washington", "west virginia", "wisconsin", "wyoming"};

	public static JComponent createMap(final LocationRatingPair[] ratings) {
		USMapGenerator.createStates();
		
		/*Painter<JXMapViewer> polygonOverlay = new Painter<JXMapViewer>(){
			public void paint (Graphics2D g, JXMapViewer map, int w, int h)
			{
				g = (Graphics2D) g.create();
				Rectangle rect = map.getViewportBounds();
				g.translate(-rect.x, -rect.y);
				
				
				for(USState state : regions){
					Polygon poly = new Polygon();
					for(GeoPosition gp : state.getStatePoints())
					{
						Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
						poly.addPoint((int) pt.getX(), (int) pt.getY()); 
					}
					int i =0;
					for(i=0; i<50; i++)
					{
						if(state.getStateName().equalsIgnoreCase(stateNames[i])) break;
					}
					
					Color fillColor = new Color(0,((255)/10)*(new Double(ratings[i].getRating())).intValue(),0);
					g.setColor(fillColor);
					g.fill(poly);
					g.setColor(fillColor);
					g.draw(poly);
				}
				
				g.dispose();
			}
		};*/
		
		JXMapViewer usMap = new JXMapViewer();
		
		//usMap.getMainMap().setOverlayPainter(polygonOverlay);
		return usMap;
		// TODO: populate panel with map
		//return new JPanel();
	}
	
	private static ArrayList<USState> createStates()
	{
		//Read in KML file 
		
		regions = new ArrayList<USState>();
		
		
		
		//For each Placemark in KML file
			//For each Polygon in the placemark
				//ArrayList <GeoPosition> points = new ArrayList<GeoPosition>(); 
				//For each point in Polygon poly
					//points.add(new GeoPosition(poly.lat, poly.long))
				//USState state = new USState(KML.stateName, points)
				//regions.add(state)
			
				
		
		return regions;
	}
	
	
}
