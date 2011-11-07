package edu.gatech.cs7450.prodviz;

import java.awt.event.ActionListener;

import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.gui.AppFrame;
import edu.gatech.cs7450.prodviz.listeners.ApplicationListener;

public class ApplicationContext {

	private Database database;
	private AppFrame mainFrame;
	private ApplicationListener listener;
	
	private ApplicationContext(Database database, AppFrame mainFrame) {
		this.database = database;
		this.mainFrame = mainFrame;
		this.listener = new ApplicationListener(this);
	}
	
	private static ApplicationContext INSTANCE;
	
	public static void initialize(Database database, AppFrame mainFrame) {
		INSTANCE = new ApplicationContext(database, mainFrame);
	}
	
	public static ApplicationContext getInstance() {
		return INSTANCE;
	}
	
	public Database getDatabase() {
		return this.database;
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
