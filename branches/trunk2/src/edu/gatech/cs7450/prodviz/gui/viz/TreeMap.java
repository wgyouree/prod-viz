package edu.gatech.cs7450.prodviz.gui.viz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.panels.GraphPanel;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.SquarifiedTreeMapLayout;
import prefuse.controls.ControlAdapter;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Schema;
import prefuse.data.Tree;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.query.SearchQueryBinding;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.util.UpdateListener;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.UILib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTree;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Displays a Tree map. Code from ...
 * http://prefuse.org/gallery/treemap/TreeMap.java
 * 
 * Modified slightly (ie, removed demo stuff) by ... 
 * 
 * @author W. Greg Youree
 *
 */
public class TreeMap extends Display {

	private static final long serialVersionUID = 1L;

    
    // create data description of labels, setting colors, fonts ahead of time
    private static final Schema LABEL_SCHEMA = PrefuseLib.getVisualItemSchema();
    static {
        LABEL_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        LABEL_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(200));
        LABEL_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma",16));
    }
    
    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    private static final String labels = "labels";
	
	private SearchQueryBinding searchQ;
	
	/**
	 * Use renderTreeMap() method to get an instance
	 * 
	 * @param t
	 * @param label
	 */
	private TreeMap(Tree t, String label, Dimension dimension) {
		super(new Visualization());
		
		// add the tree to the visualization
		VisualTree vt = m_vis.addTree(tree, t);
		m_vis.setVisible(treeEdges, null, false);
		
		// ensure that only leaf nodes are interactive
        Predicate noLeaf = (Predicate)ExpressionParser.parse("childcount()>0");
        m_vis.setInteractive(treeNodes, noLeaf, false);
		
		// add labels to the visualization
        // first create a filter to show labels only at top-level nodes
        Predicate labelP = (Predicate)ExpressionParser.parse("treedepth()=1");
        // now create the labels as decorators of the nodes
        m_vis.addDecorators(labels, treeNodes, labelP, LABEL_SCHEMA);

        // set up the renderers - one for nodes and one for labels
        DefaultRendererFactory rf = new DefaultRendererFactory();
        rf.add(new InGroupPredicate(treeNodes), new NodeRenderer());
        rf.add(new InGroupPredicate(labels), new LabelRenderer(label));
		m_vis.setRendererFactory(rf);
		
		// border colors
        final ColorAction borderColor = new BorderColorAction(treeNodes);
        final ColorAction fillColor = new FillColorAction(treeNodes);
        
        // color settings
        ActionList colors = new ActionList();
        colors.add(fillColor);
        colors.add(borderColor);
        m_vis.putAction("colors", colors);
        
        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator());
        animatePaint.add(new RepaintAction());
        m_vis.putAction("animatePaint", animatePaint);
        
        // create the single filtering and layout action list
        ActionList layout = new ActionList();
        layout.add(new SquarifiedTreeMapLayout(tree));
        layout.add(new LabelLayout(labels));
        layout.add(colors);
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);
        
        // initialize our display
        if (dimension == null) {
        	setSize(700,600);
        } else {
        	setSize(dimension);
        }
        //setPreferredSize(new Dimension(700, 600));
        setItemSorter(new TreeDepthItemSorter());
        addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                item.setStrokeColor(borderColor.getColor(item));
                item.getVisualization().repaint();
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                item.setStrokeColor(item.getEndStrokeColor());
                item.getVisualization().repaint();
            }           
        });
        
        searchQ = new SearchQueryBinding(vt.getNodeTable(), label);
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, searchQ.getSearchSet());
        searchQ.getPredicate().addExpressionListener(new UpdateListener() {
        	
            public void update(Object src) {
                m_vis.cancel("animatePaint");
                m_vis.run("colors");
                m_vis.run("animatePaint");
            }
        });
        
        // perform layout
        m_vis.run("layout");
    }
    
    public SearchQueryBinding getSearchQuery() {
        return searchQ;
    }
	
    /**
     * Get a rendered instance of the TreeMap.
     * 
     * @param t the tree to render
     * @param label a label for the tree
     * 
     * @return a JComponent with the rendered tree inside, including a search panel
     */
    public static JComponent renderTreeMap(GraphPanel rootPanel, Dimension dimension, Tree t, final String label, boolean isTop) {
    	
    	final GraphPanel ROOT_PANEL = rootPanel;
    	
    	final Dimension currentSize = dimension;
    	
    	final TreeMap treemap = new TreeMap(t, label, dimension);
    	
    	// create a search panel for the tree map
        JSearchPanel search = treemap.getSearchQuery().createSearchPanel();
        search.setShowResultCount(true);
        search.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
        search.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
        
        final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
        
        final JButton backToTop = new JButton("Back to Top");
        backToTop.setVerticalAlignment(SwingConstants.BOTTOM);
        backToTop.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
        //backToTop.setMargin(new Insets(5, 5, 5, 5));
        backToTop.setEnabled(!isTop);
        
        backToTop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ROOT_PANEL.swapPanel(currentSize, null);
			}
		});
        
        treemap.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
            	String name = item.getString("name");
            	String[] peices = name.split(TreeMapGenerator.DELIMETER_REGEXP);
                title.setText(peices[3] + " - " + peices[2]);
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
        treemap.addControlListener(new ZoomToFitControl() {
        	public void itemClicked(VisualItem item, MouseEvent e) {

            	String name = item.getString("name");
            	String[] peices = name.split(TreeMapGenerator.DELIMETER_REGEXP);
            	
        		if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
        			ROOT_PANEL.showInfo(ApplicationContext.getInstance().getActiveProduct().getDatabase().getProductById(peices[4]));
        			ROOT_PANEL.productSelected(new Product(peices[4], peices[3], peices[1], peices[2]));
        		} else if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
	            	ROOT_PANEL.swapPanel(currentSize, peices[1]);
        		}
        	}
        });
        
        Box box = UILib.getBox(new Component[]{title,search,backToTop}, true, 10, 3, 0);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(treemap, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        UILib.setColor(panel, Color.BLACK, Color.GRAY);
        return panel;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Set the stroke color for drawing treemap node outlines. A graded
     * grayscale ramp is used, with higer nodes in the tree drawn in
     * lighter shades of gray.
     */
    public static class BorderColorAction extends ColorAction {
        
        public BorderColorAction(String group) {
            super(group, VisualItem.STROKECOLOR);
        }
        
        public int getColor(VisualItem item) {
            NodeItem nitem = (NodeItem)item;
            if ( nitem.isHover() )
                return ColorLib.rgb(99,130,191);
            
            int depth = nitem.getDepth();
            if ( depth < 2 ) {
                return ColorLib.gray(100);
            } else if ( depth < 4 ) {
                return ColorLib.gray(75);
            } else {
                return ColorLib.gray(50);
            }
        }
    }
    
    /**
     * Set fill colors for treemap nodes. Search items are colored
     * in pink, while normal nodes are shaded according to their
     * depth in the tree.
     */
    public static class FillColorAction extends ColorAction {
        private ColorMap cmap = new ColorMap(
            ColorLib.getInterpolatedPalette(10,
                ColorLib.rgb(85,85,85), ColorLib.rgb(0,0,0)), 0, 9);

        public FillColorAction(String group) {
            super(group, VisualItem.FILLCOLOR);
        }
        
        public int getColor(VisualItem item) {
            if ( item instanceof NodeItem ) {
                NodeItem nitem = (NodeItem)item;
                if ( nitem.getChildCount() > 0 ) {
                    return 0; // no fill for parent nodes
                } else {
                    if ( m_vis.isInGroup(item, Visualization.SEARCH_ITEMS) )
                        return ColorLib.rgb(191,99,130);
                    else {
                    	String name = nitem.getString("name");
                    	String[] peices = name.split(TreeMapGenerator.DELIMETER_REGEXP);
                        return getWeightColor(Integer.parseInt(peices[0]));
                    }
                }
            } else {
                return cmap.getColor(0);
            }
        }
        
    } // end of inner class TreeMapColorAction
    
    private static int getWeightColor(int weight) {
    	int r = 0;
    	int g = 0;
    	int b = 0;
    	
    	if (weight <= 5) {
    		int diff = 5 - weight;
    		r = Math.max(0, (new Long(Math.round(0.1 * diff * 255)).intValue()));
    	} else if (weight > 5) {
    		int diff = 10 - weight;
    		g = Math.min(255, (new Long(Math.round(0.1 * diff * 255)).intValue()));
    	}
    	
    	return ColorLib.rgb(r, g, b);
    }
    
    /**
     * Set label positions. Labels are assumed to be DecoratorItem instances,
     * decorating their respective nodes. The layout simply gets the bounds
     * of the decorated node and assigns the label coordinates to the center
     * of those bounds.
     */
    public static class LabelLayout extends Layout {
        public LabelLayout(String group) {
            super(group);
        }
        public void run(double frac) {
            Iterator iter = m_vis.items(m_group);
            while ( iter.hasNext() ) {
                DecoratorItem item = (DecoratorItem)iter.next();
                VisualItem node = item.getDecoratedItem();
                Rectangle2D bounds = node.getBounds();
                setX(item, null, bounds.getCenterX());
                setY(item, null, bounds.getCenterY());
            }
        }
    } // end of inner class LabelLayout
    
    /**
     * A renderer for treemap nodes. Draws simple rectangles, but defers
     * the bounds management to the layout.
     */
    public static class NodeRenderer extends AbstractShapeRenderer {
        private Rectangle2D m_bounds = new Rectangle2D.Double();
        
        public NodeRenderer() {
            m_manageBounds = false;
        }
        protected Shape getRawShape(VisualItem item) {
            m_bounds.setRect(item.getBounds());
            return m_bounds;
        }
    } // end of inner class NodeRenderer
    
} // end of class TreeMap
