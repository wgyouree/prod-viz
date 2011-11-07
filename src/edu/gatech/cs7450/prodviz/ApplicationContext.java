package edu.gatech.cs7450.prodviz;

import java.awt.event.ActionListener;

import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.gui.AppFrame;
import edu.gatech.cs7450.prodviz.listeners.ApplicationListener;

public class ApplicationContext {

	private ProductImp[] products;
	private AppFrame mainFrame;
	private ApplicationListener listener;
	
	private ApplicationContext(ProductImp[] products, AppFrame mainFrame) {
		this.products = products;
		this.mainFrame = mainFrame;
		this.listener = new ApplicationListener(this);
	}
	
	private static ApplicationContext INSTANCE;
	
	public static void initialize(ProductImp[] products, AppFrame mainFrame) {
		INSTANCE = new ApplicationContext(products, mainFrame);
	}
	
	public static ApplicationContext getInstance() {
		return INSTANCE;
	}
	
	public ProductImp[] getProducts() {
		return this.products;
	}
	
	public AppFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public ActionListener getApplicationListener() {
		return this.listener;
	}
	
	public void startApplication() {
		// initialize GUI
		mainFrame.initializeGUI();
		
		// show the GUI
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
