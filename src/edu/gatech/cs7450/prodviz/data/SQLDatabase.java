package edu.gatech.cs7450.prodviz.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
		String reviewUserIdField = this.product.getReviewTableSchema().getUserIdFieldName();
		
		List<User> result = new ArrayList<User>();
		
		try {
			Connection conn = getConnection();
			
			Iterator<Product> productIt = products.iterator();
			while (productIt.hasNext()) {
				Product nextProduct = productIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM ? WHERE ?>=? && ?=?");
				pr.setString(1, reviewTableName);
				pr.setString(2, reviewRatingField);
				pr.setInt(3, ApplicationContext.getInstance().getPositiveRatingThreshhold());
				pr.setString(4, reviewProductIdField);
				pr.setString(5, nextProduct.getID());
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					User user = new User(results.getInt(reviewUserIdField) + "", "none");
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
		
		String productTableName = this.product.getProductTableSchema().getTableName();
		String productIdField = this.product.getProductTableSchema().getIdFieldName();
		String productNameField = this.product.getProductTableSchema().getNameFieldName();
		String productFirstLevelClassifierField = this.product.getProductTableSchema().getFirstLevelClassifierFieldName();
		String productSecondLevelClassifierField = this.product.getProductTableSchema().getSecondLevelClassifierFieldName();

		List<Product> result = new ArrayList<Product>();
		
		try {
			Connection conn = getConnection();
			
			List<String> productIds = new ArrayList<String>();
			
			Iterator<User> userIt = otherUsers.iterator();
			while (userIt.hasNext()) {
				User nextUser = userIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM ? WHERE ?=?");
				pr.setString(1, reviewTableName);
				pr.setString(2, reviewUserIdField);
				pr.setString(3, nextUser.getID());
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					productIds.add(results.getString(reviewProductIdField));
				}
			}
			
			Iterator<String> productIdsIt = productIds.iterator();
			while (productIdsIt.hasNext()) {
				String productId = productIdsIt.next();
				
				PreparedStatement pr = conn.prepareStatement("SELECT * FROM ? WHERE ?=?");
				pr.setString(1, productTableName);
				pr.setString(2, productIdField);
				pr.setString(3, productId);
				
				ResultSet results = pr.executeQuery();
				while (results.next()) {
					Product product = new Product(
											results.getString(productIdField),
											results.getString(productNameField),
											results.getString(productFirstLevelClassifierField),
											results.getString(productSecondLevelClassifierField)
											);
					result.add(product);
				}
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
