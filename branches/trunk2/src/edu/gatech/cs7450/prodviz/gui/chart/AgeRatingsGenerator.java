package edu.gatech.cs7450.prodviz.gui.chart;

import org.jfree.chart.ChartPanel;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.AgeRatingPair;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.Review;

public class AgeRatingsGenerator {

	public static ChartPanel createAgePlot(AbstractProduct abstractProduct, Product product)
	{
		Database db = abstractProduct.getDatabase();
		AgeRatingPair[] pairs = db.getRatingsByAgeOfUser(product);
		
		AgeRatings ratings = new AgeRatings(pairs);
		
		return ratings.getChartPanel();
	}
}
