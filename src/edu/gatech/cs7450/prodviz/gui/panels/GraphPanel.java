package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.LocationRatingPair;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.BottomPanel;
import edu.gatech.cs7450.prodviz.gui.MainFrame;
import edu.gatech.cs7450.prodviz.gui.viz.TreeMap;
import edu.gatech.cs7450.prodviz.gui.viz.TreeMapGenerator;

public class GraphPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;

	private MainFrame parent;
	private boolean infoPanelShowing = false;
	
	public GraphPanel(MainFrame parent) {
		this.parent = parent;
		this.setBackground(Color.RED);
		this.setLayout(new BorderLayout());
	}
	
	public void resize(Dimension dimension) {
		if (!this.infoPanelShowing) {
			this.swapPanel(new Dimension(
					(new Double(dimension.getWidth())).intValue(),
					(new Double(dimension.getHeight())).intValue() - BottomPanel.DEFAULT_HEIGHT - 100), null);
//			System.out.println("resized to [" + dimension.getWidth() + " " + dimension.getHeight() + "]");
		}
	}
	
	public void initComponents() {
		this.swapPanel(null, null);
	}
	
	public void swapPanel(Dimension dimension, String onlyThisFirstLevelClassifier) {
		this.infoPanelShowing = false;
		this.removeAll();
		ApplicationContext appContext = ApplicationContext.getInstance();
		JComponent treeMap = TreeMap.renderTreeMap(
				this,
				dimension,
				TreeMapGenerator.createTreeMap(
						appContext.getActiveProduct(),
						appContext.getActiveUser(),
						appContext.getActiveRecommender(),
						onlyThisFirstLevelClassifier),
						"name",
						false);
		JPanel container = new JPanel();
		container.add(treeMap);
		this.add(container, BorderLayout.CENTER);
		this.validate();
	}
	
	public void showInfo(Product product) {
		this.infoPanelShowing = true;
		this.removeAll();
		InfoPanel panel = new InfoPanel(this, product);
		JScrollPane scrollPane = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		System.out.println(this.getSize().getWidth() + " " + this.getSize().getHeight());
		scrollPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		scrollPane.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));
		this.add(scrollPane, BorderLayout.CENTER);
		this.validate();
	}
	
	public void productSelected(Product product) {
		this.parent.updateAgePlot(product);

		ApplicationContext appContext = ApplicationContext.getInstance();
		
		Database db = appContext.getActiveProduct().getDatabase();
		
		LocationRatingPair[] pair = db.getRatingsByLocationOfUser(product);
		this.parent.updateUSMap(pair);

	}
	
	public void resizeAll() {
		this.parent.resizeAll();
	}
}
