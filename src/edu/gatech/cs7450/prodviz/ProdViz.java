package edu.gatech.cs7450.prodviz;

import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.book.BookDatabase;
import edu.gatech.cs7450.prodviz.gui.MainFrame;

public class ProdViz {

	private static String APP_NAME = "Visual Product Recommendation System";
	
	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create database interface
		Database database = new BookDatabase(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/",
				"book",
				"admin",
				"admin");
		
		// Create GUI
		MainFrame mainFrame = new MainFrame(APP_NAME);

		// Create application context
		ApplicationContext.initialize(database, mainFrame);
		
		// Start the application
		ApplicationContext.getInstance().startApplication();
	}
}
