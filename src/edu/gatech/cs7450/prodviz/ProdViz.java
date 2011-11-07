package edu.gatech.cs7450.prodviz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.products.BookDatabase;
import edu.gatech.cs7450.prodviz.gui.MainFrame;
import edu.gatech.cs7450.prodviz.imp.book.BookDatabaseConfig;

public class ProdViz {

	private static String APP_NAME = "Visual Product Recommendation System";
	private static String impPackageName = "edu.gatech.cs7450.prodviz.imp";
	
	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Create GUI
		MainFrame mainFrame = new MainFrame(APP_NAME);

		// Create application context
		ApplicationContext.initialize(initializeProductImps(), mainFrame);
		
		// Start the application
		ApplicationContext.getInstance().startApplication();
	}
	
	public static ProductImp[] initializeProductImps() {
		try {
			String className = ProdViz.class.getName();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String path = impPackageName.replace('.', '/');
	        List<Class> classes = findClasses(new File(path), impPackageName);
	        List<ProductImp> imps = new ArrayList<ProductImp>();
	        for (int i = 0; i < classes.size(); i++) {
	        	Object o = classes.get(i).newInstance();
	        	if (o instanceof ProductImp) {
	        		imps.add((ProductImp)o);
	        	}
	        }
	        return imps.toArray(new ProductImp[imps.size()]);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return new ProductImp[0];
	}
	
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
    	File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
