package edu.gatech.cs7450.prodviz.gui;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;
import edu.gatech.cs7450.prodviz.data.Product;

public abstract class AbstractBottomAppPanel extends AbstractAppPanel {

	public abstract void updateAgePlot(Product product);
	
	public abstract void updateMap(LocationRatingPair[] pair);
}
