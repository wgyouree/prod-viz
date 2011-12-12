package edu.gatech.cs7450.prodviz.gui.viz;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.AxisLayout;
import prefuse.activity.Activity;
import prefuse.activity.ActivityAdapter;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.FunctionExpression;
import prefuse.data.expression.FunctionTable;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
//import prefuse.render.ShapeItemRenderer;
//import prefuse.render.TextItemRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.util.ui.JSearchPanel;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;

/**
 * Re-implementation of Ben Fry's Zipdecode. Check out the
 * original at <a href="http://acg.media.mit.edu/people/fry/zipdecode/">
 * http://acg.media.mit.edu/people/fry/zipdecode/</a>.
 * 
 * This demo showcases creating new functions in the prefuse expression
 * language, creating derived columns, and provides an example of using
 * a dedicated focus set of items to support more efficient data handling.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class USMap extends Display implements Constants {

    public static final String ZIPCODES = "data/zipcode.txt";
    public static final String STATES = "data/state.txt";
    
    // data groups
    private static final String DATA = "data";
    private static final String LABELS = "labels";
    private static final String FOCUS = Visualization.FOCUS_ITEMS;
    
    private static LocationRatingPair[] PAIRS;
    private static java.util.Map<String, String> STATE_MAPPING;
    
    public static class StateLookupFunction extends FunctionExpression {
        private static Table s_states;
        static {
            try {
                s_states = new DelimitedTextTableReader().readTable(STATES);
            } catch ( Exception e ) { e.printStackTrace(); }
        }
        
        public StateLookupFunction() { super(1); }
        public String getName() { return "STATE"; }
        public Class getType(Schema s) { return String.class; }
        public Object get(Tuple t) {
            int code = s_states.index("code").get(param(0).getInt(t));
            return s_states.getString(code, "alpha");
        }
    }
    // add state function to the FunctionTable
    static { FunctionTable.addFunction("STATE", StateLookupFunction.class); }
    
    private java.util.Map<String, String> stateMapping;
    
    public USMap(final Table t, final LocationRatingPair[] pairs) {
        super(new Visualization());
        
        if (stateMapping == null) {
        	populateStateMapping();
        }
        
        STATE_MAPPING = stateMapping;
        PAIRS = pairs;
        
        // this predicate makes sure only the continental states are included
        Predicate filter = (Predicate)ExpressionParser.parse(
                "state >= 1 && state <= 56 && state != 2 && state != 15");
        VisualTable vt = m_vis.addTable(DATA, t, filter, getDataSchema());
        // zip codes are loaded in as integers, so lets create a derived
        // column that has correctly-formatted 5 digit strings
        vt.addColumn("zipstr", "LPAD(zip,5,'0')");
        // now add a formatted label to show within the visualization
        vt.addColumn("label", "CONCAT(CAP(city),', ',STATE(state),' ',zipstr)");
        
        // create a filter controlling label appearance
        Predicate loneResult = (Predicate)ExpressionParser.parse(
                "INGROUP('_search_') AND GROUPSIZE('_search_')=1 AND " +
                "LENGTH(QUERY('_search_'))=5");
        
        // add a table of visible city,state,zip labels
        // this is a derived table, overriding only the fields that need to
        // have unique values and inheriting all other data values from the
        // data table. in particular, we want to inherit the x,y coordinates.
        m_vis.addDerivedTable(LABELS, DATA, loneResult, getLabelSchema());
        
        // -- renderers -------------------------------------------------------
        
        DefaultRendererFactory rf = new DefaultRendererFactory();
        rf.setDefaultRenderer(new ShapeRenderer(1)); // 1 pixel rectangles
        m_vis.setRendererFactory(rf);
        
        // -- actions ---------------------------------------------------------
        
        ActionList layout = new ActionList();
        layout.add(new AxisLayout(DATA, "lat", Y_AXIS));
        layout.add(new AxisLayout(DATA, "lon", X_AXIS));
        m_vis.putAction("layout", layout);
        
        // the update list updates the colors of data points and sets the visual
        // properties for any labels. Color updating is limited only to the
        // current focus items, ensuring faster performance.
        ActionList colors = new ActionList();
        colors.add(new ZipColorAction(DATA));
        m_vis.putAction("colors", colors);
        
        // animate a change in color in the interface. this animation is quite
        // short, only 200ms, so that it does not impede with interaction.
        // color animation of data points looks only at the focus items,
        // ensuring faster performance.
        ActionList animate = new ActionList(200);
        animate.add(new ColorAnimator(FOCUS, VisualItem.FILLCOLOR));
        animate.add(new ColorAnimator(LABELS, VisualItem.TEXTCOLOR));
        animate.add(new RepaintAction());
//        animate.addActivityListener(new ActivityAdapter() {
//            public void activityCancelled(Activity a) {
//                // if animation is canceled, set colors to final state
//                update.run(1.0);
//            }
//        });
        m_vis.putAction("animate", animate);
        
        // update items after a resize of the display, animating them to their
        // new locations. this animates all data points, so is noticeably slow.
        ActionList resize = new ActionList(1500);
        resize.setPacingFunction(new SlowInSlowOutPacer());
        resize.add(new LocationAnimator(DATA));
        resize.add(new LocationAnimator(LABELS));
        resize.add(new RepaintAction());
        m_vis.putAction("resize", resize);
        
        // -- display ---------------------------------------------------------
        
        setSize(300, 200);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBackground(ColorLib.getGrayscale(50));
        setFocusable(false);
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                m_vis.run("layout");
                m_vis.run("update");
                m_vis.run("resize");
                m_vis.run("colors");
                invalidate();
            }
        });
        
        // -- launch ----------------------------------------------------------
        
        m_vis.run("layout");
        m_vis.run("colors");
        m_vis.run("animate");
    }
    
    private static Schema getDataSchema() {
        Schema s = PrefuseLib.getVisualItemSchema();
        s.setDefault(VisualItem.INTERACTIVE, false);
        s.setDefault(VisualItem.FILLCOLOR, ColorLib.gray(50));
        return s;
    }
    
    private static Schema getLabelSchema() {
        Schema s = PrefuseLib.getMinimalVisualSchema();
        s.setDefault(VisualItem.INTERACTIVE, false);
        
        // default font is 16 point Georgia
        s.addInterpolatedColumn(
                VisualItem.FONT, Font.class, FontLib.getFont("Georgia",16));
        
        // default fill color should be invisible
        s.addInterpolatedColumn(VisualItem.FILLCOLOR, int.class);
        s.setInterpolatedDefault(VisualItem.FILLCOLOR, ColorLib.gray(50));
        
        s.addInterpolatedColumn(VisualItem.TEXTCOLOR, int.class);
        // default text color is white
        s.setInterpolatedDefault(VisualItem.TEXTCOLOR, ColorLib.gray(255));
        // default start text color is fully transparent
        s.setDefault(VisualItem.STARTTEXTCOLOR, ColorLib.gray(255,0));
        return s;
    }
    
    private void populateStateMapping() {
    	try {
    		File stateDataFile = new File("data/stateData.txt");
    		BufferedReader stateDataReader = new BufferedReader(new FileReader(stateDataFile));
    		
    		File stateFile = new File("data/state.txt");
    		BufferedReader stateReader = new BufferedReader(new FileReader(stateFile));
    		
    		stateMapping = new HashMap<String, String>();
    		
    		java.util.Map<String, String> stateToCode = new HashMap<String, String>();
    		
    		String line = stateReader.readLine();
    		while (line != null) {
    			String[] parts = line.split("[\\t]");
    			stateToCode.put(parts[1].toLowerCase(), parts[0]);
    			line = stateReader.readLine();
    		}
    		
    		line = stateDataReader.readLine();
    		while (line != null) {
    			String[] parts = line.split("[,]");
    			stateMapping.put(parts[0].toLowerCase(), stateToCode.get(parts[0]));
    			line = stateDataReader.readLine();
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    // ------------------------------------------------------------------------
    
    public static JComponent createMap(LocationRatingPair[] pairs) {
    	try {
	    	DelimitedTextTableReader tr = new DelimitedTextTableReader();
	        Table t = tr.readTable(ZIPCODES);
	        USMap zd = new USMap(t, pairs);
	        return zd;
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public static class ZipColorAction extends ColorAction {
        public ZipColorAction(String group) {
            super(group, VisualItem.FILLCOLOR);
        }
        
        public int getColor(VisualItem item) {

			String stateCode = item.getString("state");
			for (int i = 0; i < PAIRS.length; i++) {
				String locationStateCode = STATE_MAPPING.get(PAIRS[i].getLocation().toLowerCase());
				if (locationStateCode == null) {
					System.out.println(PAIRS[i].getLocation());
				} else if (locationStateCode.equalsIgnoreCase(stateCode)) {
					double rating = PAIRS[i].getRating();
					int minimum = 40;
					int diff = 255 - minimum;
	                return ColorLib.rgb(0,(new Double((rating / 10) * diff + minimum).intValue()),0);
				}
			}
            return ColorLib.gray(50);
        }
    }
    
} // end of class ZipDecode