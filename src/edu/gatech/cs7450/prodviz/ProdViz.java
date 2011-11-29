package edu.gatech.cs7450.prodviz;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.IDatabaseConfig;
import edu.gatech.cs7450.prodviz.gui.MainFrame;

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
		
		// Create database interface
		IDatabaseConfig config = new DatabaseConfig(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/",
				"user",
				"admin",
				"admin");
		
		// Create application context
		ApplicationContext.initialize(initializeProductImps(), mainFrame);
		
		// Start the application
		ApplicationContext.getInstance().startApplication();
	}
	
	public static AbstractProduct[] initializeProductImps() {
		try {
			String className = ProdViz.class.getName();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String path = impPackageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
	        while (resources.hasMoreElements()) {
	            URL resource = resources.nextElement();
	            dirs.add(new File(resource.getFile()));
	        }
	        List<Class> classes = new ArrayList<Class>();
	        for (File directory : dirs) {
	            classes.addAll(findClasses(directory, impPackageName));
	        }
	        List<AbstractProduct> imps = new ArrayList<AbstractProduct>();
	        for (int i = 0; i < classes.size(); i++) {
	        	Object o = classes.get(i).newInstance();
	        	if (o instanceof AbstractProduct) {
	        		imps.add((AbstractProduct)o);
	        	}
	        }
	        return imps.toArray(new AbstractProduct[imps.size()]);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return new AbstractProduct[0];
	}
	
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    	List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith("Imp.class")) {
            	classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
