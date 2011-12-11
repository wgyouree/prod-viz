package edu.gatech.cs7450.prodviz.gui.chart;

import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.gatech.cs7450.prodviz.data.AgeRatingPair;
import edu.gatech.cs7450.prodviz.data.Review;

public class AgeRatings {

	private ChartPanel chartPanel;
	
	public AgeRatings(AgeRatingPair[] pairs)
	{
		XYDataset dataset = createDataset(pairs);
		JFreeChart chart = createChart(dataset);
		
		chartPanel = new ChartPanel(chart);
		
	}
	
	private XYDataset createDataset(AgeRatingPair[] reviews)
	{
		XYSeries series = new XYSeries("Ratings");
		
		for(int i=0; i<reviews.length;i++)
		{
			series.add(new XYDataItem(reviews[i].getAge(), reviews[i].getRating()));
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}
	
	private static JFreeChart createChart(XYDataset dataset)
	{
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Ratings by Age", //Title
				"Age",			  //X label
				"Average Rating", //Y label
				dataset,		  //data
				PlotOrientation.VERTICAL, //Orientation
				false,			 //legend
				false,			 //tooltips
				false);			 //urls
		chart.setBorderPaint(Color.white);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);
        
        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                
        return chart;
	}
	
	public ChartPanel getChartPanel()
	{
		return chartPanel;
	}
	
}
