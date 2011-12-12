package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jdesktop.swingx.JXMapKit;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.LocationRatingPair;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.AbstractBottomAppPanel;
import edu.gatech.cs7450.prodviz.gui.chart.AgeRatingsGenerator;
import edu.gatech.cs7450.prodviz.gui.viz.USMap;

public class BarPanel extends AbstractBottomAppPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	public void initComponents() {
		this.leftPanel.setLayout(new BorderLayout());
		this.rightPanel.setLayout(new BorderLayout());
		
		this.setupBarPanel();
	}
	
	private void setupBarPanel() {
		this.add(leftPanel);
		this.add(rightPanel);
	}
	
	@Override
	public void updateAgePlot(Product product) {
		this.leftPanel.removeAll();
		
		ApplicationContext appContext = ApplicationContext.getInstance();
		JComponent comp = AgeRatingsGenerator.createAgePlot(appContext.getActiveProduct(), product);
		this.leftPanel.add(comp, BorderLayout.CENTER);
		this.validate();
	}
	
	@Override
	public void updateMap(LocationRatingPair[] pair) {
		this.rightPanel.removeAll();
		
		JComponent comp = USMap.createMap(pair);
		this.rightPanel.add(comp,BorderLayout.CENTER);
		this.validate();
	}
	
	@Override
	public void hideProgressIndicator() {
		this.removeAll();
		this.setupBarPanel();
		this.validate();
	}
	
	@Override
	public void showProgressIndicator() {
		this.removeAll();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setPreferredSize(new Dimension(300, 50));
		this.add(progressBar);
	}
	
	@Override
	public void resize(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}
}
