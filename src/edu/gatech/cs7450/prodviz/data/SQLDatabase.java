package edu.gatech.cs7450.prodviz.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	public List<Review> getRatingsByAgeOfUser(List<Product> products)
	{
		String reviewTableName = this.product.getReviewTableSchema().getTableName();
		String reviewProductIDField = this.product.getReviewTableSchema().getProductIdFieldName();
		String reviewUserIdField = this.product.getReviewTableSchema().getUserIdFieldName();
		String reviewRatingField = this.product.getReviewTableSchema().getRatingFieldName();
		
		String userTableName = this.product.getUserTableSchema().getTableName();
		String userUserIdField = this.product.getUserTableSchema().getUserIdFieldName();
		String userUserAgeField = this.product.getUserTableSchema().getUserAgeFieldName();
						
		List<User> users = new ArrayList<User> ();
		
		List<Review> result = new ArrayList<Review>();
		
		
		
		return result;
	}
//getRatingsByAgeOfUser	
	//Select all from ratingTableName Where productIdField =? And not null
	//Create arraylist of users for that product
	//Create list of reviews (one for each age) and iterate through users averaging ratings as we go
	//Iterate through results to average rating for age
	//Return list of ratings
	
//getRatingsByLocationOfUser	
	//Get userIDs in specific location
	//Get average the ratings of all of these users
	//return list of ratings locations and average ratings
}
