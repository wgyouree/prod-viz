package edu.gatech.cs7450.prodviz;

import java.awt.event.ActionListener;

import edu.gatech.cs7450.prodviz.gui.AppFrame;
import edu.gatech.cs7450.prodviz.listeners.ApplicationListener;

public class ApplicationContext {

	private AbstractProduct[] products;
	private AppFrame mainFrame;
	private ApplicationListener listener;
	
	private ApplicationContext(AbstractProduct[] products, AppFrame mainFrame) {
		this.products = products;
		this.mainFrame = mainFrame;
		this.listener = new ApplicationListener(this);
	}
	
	private static ApplicationContext INSTANCE;
	
	public static void initialize(AbstractProduct[] products, AppFrame mainFrame) {
		INSTANCE = new ApplicationContext(products, mainFrame);
	}
	
	public static ApplicationContext getInstance() {
		return INSTANCE;
	}
	
	public AbstractProduct[] getProducts() {
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
