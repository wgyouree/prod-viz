package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.Color;
import java.awt.Dimension;

import org.jdesktop.swingx.JXMapKit;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.chart.AgeRatingsGenerator;

public class BarPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;
	
	public BarPanel() {
		this.setBackground(Color.GREEN);
	}
	
	public void initComponents() {
		
		//ApplicationContext appContext = ApplicationContext.getInstance();
		
		//AgeRatingsGenerator.createAgePlot(appContext.getActiveProduct(), );
		
		
	}
	
	@Override
	public void resize(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}
}
