package edu.gatech.cs7450.prodviz;

import java.awt.event.ActionListener;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.IDatabaseConfig;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.Review;
import edu.gatech.cs7450.prodviz.data.User;
import edu.gatech.cs7450.prodviz.gui.AppFrame;
import edu.gatech.cs7450.prodviz.imp.book.BookImp;
import edu.gatech.cs7450.prodviz.listeners.ApplicationListener;
import edu.gatech.cs7450.prodviz.recommend.BFSRecommender;
import edu.gatech.cs7450.prodviz.recommend.IRecommender;

public class ApplicationContext {

	private AbstractProduct[] products;
	private AppFrame mainFrame;
	private ApplicationListener listener;
	private AbstractProduct activeProduct;
	private User activeUser;
	private IRecommender activeRecommender;
	
	private static final int POSITIVE_RATING_THRESHHOLD = 5;
	private static final int RECURSIVE_RECOMMENDER_DEPTH = 1;
	private static final int NUMBER_OF_RATINGS_CEILING = 1000;
	
	private ApplicationContext(AbstractProduct[] products, AppFrame mainFrame) {
		this.products = products;
		this.mainFrame = mainFrame;
		this.listener = new ApplicationListener(this);
		
		// defaults
		this.activeUser = new User("300000", "Your Name Here", "atlanta, georgia, usa", 30);
		this.activeUser.addReview(new Review("1", 1, 8, new Product("1570719586", "God-Shaped Hole", "Genre", "Author")));
		this.activeRecommender = new BFSRecommender();
		this.activeProduct = new BookImp();
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
	
	public void setActiveProduct(AbstractProduct activeProduct) {
		this.activeProduct = activeProduct;
	}
	
	public AbstractProduct getActiveProduct() {
		return this.activeProduct;
	}
	
	public int getPositiveRatingThreshhold() {
		return POSITIVE_RATING_THRESHHOLD;
	}
	
	public int getRecursiveRecommenderDepth() {
		return RECURSIVE_RECOMMENDER_DEPTH;
	}
	
	public User getActiveUser() {
		return this.activeUser;
	}
	
	public IRecommender getActiveRecommender() {
		return this.activeRecommender;
	}
	
	public int getNumberOfRatingsCeiling() {
		return NUMBER_OF_RATINGS_CEILING;
	}
	
	public void startApplication() {
		// initialize GUI
		mainFrame.initializeGUI();
		
		// show the GUI
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
