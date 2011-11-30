package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.Color;

import javax.swing.JComponent;

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.viz.TreeMap;
import edu.gatech.cs7450.prodviz.gui.viz.TreeMapGenerator;

public class GraphPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;
	
	public GraphPanel() {
		this.setBackground(Color.RED);
	}
	
	public void initComponents() {
		ApplicationContext appContext = ApplicationContext.getInstance();
		JComponent treeMap = TreeMap.renderTreeMap(
				TreeMapGenerator.createTreeMap(
						appContext.getActiveProduct(),
						appContext.getActiveUser(),
						appContext.getActiveRecommender()),
						"name");
		this.add(treeMap);
	}
}
