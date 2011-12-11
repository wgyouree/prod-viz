package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXMapKit;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.AbstractBottomAppPanel;
import edu.gatech.cs7450.prodviz.gui.chart.AgeRatingsGenerator;

public class BarPanel extends AbstractBottomAppPanel {

	private static final long serialVersionUID = 1L;
	
	public BarPanel() {
		this.setBackground(Color.GREEN);
	}
	
	public void initComponents() {
		this.setLayout(new BorderLayout());
	}
	
	@Override
	public void updateAgePlot(Product product) {
		this.removeAll();
		
		ApplicationContext appContext = ApplicationContext.getInstance();
		JComponent comp = AgeRatingsGenerator.createAgePlot(appContext.getActiveProduct(), product);
		this.add(comp, BorderLayout.CENTER);
		this.validate();
	}
	
	@Override
	public void resize(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}
}
