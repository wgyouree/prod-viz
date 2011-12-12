package edu.gatech.cs7450.prodviz.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.sql.PreparedStatement;

import edu.gatech.cs7450.prodviz.ApplicationContext;

public class SQLDatabase extends Database {

	public SQLDatabase(AbstractProduct product, IDatabaseConfig config) {
		super(product);
		this.config = config;
		this.initialize();
	}
	
	public boolean initialize() {
		try {
			Class.forName(this.config.getDriver());
			this.initialized = true;
			return true;
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				config.getUrl() + config.getDatabaseName(),
				config.getUsername(),
				config.getPassword());
	}
	
	public List<User> getOtherUsersWithPositiveReviews(List<Product> products) {
		ensureInitialized();
		
		String reviewTableName = this.product.getReviewTableSchema().getTableName();
		String reviewRatingField = this.product.getReviewTableSchema().getRatingFieldName();
		String reviewProductIdField = this.product.getReviewTableSchema().getProductIdFieldName();
		
		List<User> result = new ArrayList<User>();
		
		try {
			Connection conn = getConnection();
			
			Iterator<Product> productIt = products.iterator();
			while (productIt.hasNext()) {
				Product nextProduct = productIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM " + reviewTableName + " WHERE " + reviewRatingField + ">=? AND " + reviewProductIdField + "=?");
				pr.setInt(1, ApplicationContext.getInstance().getPositiveRatingThreshhold());
				pr.setString(2, nextProduct.getID());
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					User user = new User(results.getInt(1) + "", "none", "none", 0);
					result.add(user);
				}
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public Collection<Product> getProductsRecommendedByUsers(Collection<User> otherUsers) {
		ensureInitialized();
		
		String reviewTableName = this.product.getReviewTableSchema().getTableName();
		String reviewProductIdField = this.product.getReviewTableSchema().getProductIdFieldName();
		String reviewUserIdField = this.product.getReviewTableSchema().getUserIdFieldName();
		String reviewRatingField = this.product.getReviewTableSchema().getRatingFieldName();
		
		String productTableName = this.product.getProductTableSchema().getTableName();
		String productIdField = this.product.getProductTableSchema().getIdFieldName();
		String productNameField = this.product.getProductTableSchema().getNameFieldName();
		String productFirstLevelClassiferField = this.product.getProductTableSchema().getFirstLevelClassifierFieldName();
		String productSecondLevelClassifierField = this.product.getProductTableSchema().getSecondLevelClassifierFieldName();

		List<Product> result = new ArrayList<Product>();
		
		try {
			Connection conn = getConnection();
			
			Map<String, Integer> productIds = new HashMap<String, Integer>();
			
			Iterator<User> userIt = otherUsers.iterator();
			while (userIt.hasNext()) {
				User nextUser = userIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM " + reviewTableName + " WHERE " + reviewUserIdField + "=? LIMIT " +
						ApplicationContext.getInstance().getNumberOfRatingsCeiling());
				pr.setInt(1, Integer.parseInt(nextUser.getID()));
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					int rating = results.getInt(reviewRatingField);
					if (rating >= ApplicationContext.getInstance().getPositiveRatingThreshhold()) {
						productIds.put(results.getString(reviewProductIdField), results.getInt(reviewRatingField));
					}
				}
			}
			
			Iterator<String> productIdsIt = productIds.keySet().iterator();
			while (productIdsIt.hasNext()) {
				String productId = productIdsIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM " + productTableName + " WHERE " + 
						productIdField + "=? AND " + productFirstLevelClassiferField + " IS NOT NULL AND " + 
						productSecondLevelClassifierField + " IS NOT NULL");
				
				pr.setString(1, productId);
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					Product product = new Product(
											results.getString(productIdField),
											results.getString(productNameField),
											results.getString(productFirstLevelClassiferField),
											results.getString(productSecondLevelClassifierField));
					product.setCummulativeRating(productIds.get(productId));
					result.add(product);
				}
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public AgeRatingPair[] getRatingsByAgeOfUser(Product searchProduct)
	{
		String reviewTableName = this.product.getReviewTableSchema().getTableName();
		String reviewProductIdField = this.product.getReviewTableSchema().getProductIdFieldName();
		String reviewUserIdField = this.product.getReviewTableSchema().getUserIdFieldName();
		String reviewRatingField = this.product.getReviewTableSchema().getRatingFieldName();
		
		String userTableName = this.product.getUserTableSchema().getTableName();
		String userUserIdField = this.product.getUserTableSchema().getUserIdFieldName();
		String userUserAgeField = this.product.getUserTableSchema().getUserAgeFieldName();

		List<AgeRatingPair> result = new ArrayList<AgeRatingPair>();
		
		try{
			Connection conn = getConnection();

			PreparedStatement pr = conn.prepareStatement(
					"SELECT " + userTableName + "." + userUserAgeField + "," +
					"AVG(" + reviewTableName + "." + reviewRatingField + ") FROM " +
					userTableName + "," + reviewTableName + " WHERE " +
					userTableName + "." + userUserIdField + "=" +
					reviewTableName + "." + reviewUserIdField + " AND " +
					userTableName + "." + userUserAgeField + "<=" +
					ApplicationContext.getInstance().getMaximumAge() + " AND " +
					reviewTableName + "." + reviewProductIdField + "=?" +
					" GROUP BY " + userTableName + "." + userUserAgeField);
			pr.setString(1, searchProduct.getID());
			
			ResultSet results = pr.executeQuery();
			
			while(results.next()) {
				result.add(new AgeRatingPair(results.getInt(1), results.getDouble(2)));
			}

			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result.toArray(new AgeRatingPair[result.size()]);
	}	
	
	public LocationRatingPair[] getRatingsByLocationOfUser(Product searchProduct)
	{
		String reviewTableName = this.product.getReviewTableSchema().getTableName();
		String reviewProductIdField = this.product.getReviewTableSchema().getProductIdFieldName();
		String reviewUserIdField = this.product.getReviewTableSchema().getUserIdFieldName();
		String reviewRatingField = this.product.getReviewTableSchema().getRatingFieldName();
		
		String userTableName = this.product.getUserTableSchema().getTableName();
		String userUserIdField = this.product.getUserTableSchema().getUserIdFieldName();
		String userUserLocationField = this.product.getUserTableSchema().getUserLocationFieldName();
		
		String[] states = {"alabama", "alaska", "arizona", "arkansas", "california", "colorado", "connecticut",
				"delaware", "florida", "georgia", "hawaii", "idaho", "illinois", "indiana", "iowa", "kansas", 
				"kentucky", "louisiana", "maine", "maryland", "massachusetts", "michigan", "minnesota", 
				"mississippi", "missouri", "montana", "nebraska","nevada", "new hampshire", "new jersey", 
				"new mexico", "new york", "north carolina", "north dakota", "ohio", "oklahoma", "oregon", 
				"pennsylvania", "rhode island", "south carolina", "south dakota", "tennessee", "texas", 
				"utah", "vermont", "virginia", "washington", "west virginia", "wisconsin", "wyoming"};
		
	
		Set<String> stateSet = new HashSet<String>();
		for (int i = 0; i < states.length; i++) {
			stateSet.add(states[i]);
		}
		
		List<LocationRatingPair> result = new ArrayList<LocationRatingPair>();
		
		try{
			Connection conn = getConnection();

			PreparedStatement pr = conn.prepareStatement(
					"SELECT " + userTableName + "." + userUserLocationField + "," +
					"AVG(" + reviewTableName + "." + reviewRatingField + ") FROM " +
					userTableName + "," + reviewTableName + " WHERE " +
					userTableName + "." + userUserIdField + "=" +
					reviewTableName + "." + reviewUserIdField + " AND " +
					userTableName + "." + userUserLocationField + " LIKE '%usa' AND " +
					reviewTableName + "." + reviewProductIdField + "=?" +
					" GROUP BY " + userTableName + "." + userUserLocationField);
			pr.setString(1, searchProduct.getID());
			
			ResultSet results = pr.executeQuery();
			Map<String, List<Double>> ratingsByLocation = new HashMap<String, List<Double>>();
			
			while(results.next()) {
				String location = results.getString(1);
				String[] locationSplit = location.split("[,]");
				if (locationSplit.length == 3) {
					String state = locationSplit[1].trim();
					if (stateSet.contains(state)) {
						List<Double> ratings = ratingsByLocation.get(state);
						if (ratings == null) {
							ratings = new ArrayList<Double>();
							ratingsByLocation.put(state, ratings);
						}
						ratings.add(results.getDouble(2));
					}
				}
			}
			
			Iterator<String> stateIt = ratingsByLocation.keySet().iterator();
			while (stateIt.hasNext()) {
				String state = stateIt.next();
				List<Double> stateRatings = ratingsByLocation.get(state);
				double sum = 0;
				for (int i = 0; i < stateRatings.size(); i++) {
					sum += stateRatings.get(i);
				}
				double avg = sum / stateRatings.size();
				result.add(new LocationRatingPair(state, avg));
			}

			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result.toArray(new LocationRatingPair[result.size()]);
	}
	
	public Product getProductById(String id) {

		String productTableName = this.product.getProductTableSchema().getTableName();
		String productIdField = this.product.getProductTableSchema().getIdFieldName();
		String productNameField = this.product.getProductTableSchema().getNameFieldName();
		String productFirstLevelClassiferField = this.product.getProductTableSchema().getFirstLevelClassifierFieldName();
		String productSecondLevelClassifierField = this.product.getProductTableSchema().getSecondLevelClassifierFieldName();
		
		try{
			Connection conn = getConnection();

			PreparedStatement pr = conn.prepareStatement("SELECT * FROM " + productTableName + " WHERE " + productIdField + "=?");
			pr.setString(1, id);
			
			ResultSet results = pr.executeQuery();
			
			while (results.next()) {
				Product product = new Product(
						results.getString(productIdField),
						results.getString(productNameField),
						results.getString(productFirstLevelClassiferField),
						results.getString(productSecondLevelClassifierField));
				return product;
			}
			
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Product[] getAllProducts() {

		String productTableName = this.product.getProductTableSchema().getTableName();
		String productIdField = this.product.getProductTableSchema().getIdFieldName();
		String productNameField = this.product.getProductTableSchema().getNameFieldName();
		String productFirstLevelClassiferField = this.product.getProductTableSchema().getFirstLevelClassifierFieldName();
		String productSecondLevelClassifierField = this.product.getProductTableSchema().getSecondLevelClassifierFieldName();
		
		List<Product> result = new ArrayList<Product>();
		

		try{
			Connection conn = getConnection();

			PreparedStatement pr = conn.prepareStatement("SELECT * FROM " + productTableName);
			
			ResultSet results = pr.executeQuery();
			
			while (results.next()) {
				Product product = new Product(
						results.getString(productIdField),
						results.getString(productNameField),
						results.getString(productFirstLevelClassiferField),
						results.getString(productSecondLevelClassifierField));
				result.add(product);
			}
			
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result.toArray(new Product[result.size()]);
	}
}
