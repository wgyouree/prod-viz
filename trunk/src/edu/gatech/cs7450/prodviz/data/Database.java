package edu.gatech.cs7450.prodviz.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public abstract class Database {

	protected AbstractProduct product;
	protected IDatabaseConfig config;
	protected boolean initialized = false;
	
	protected ITableSchema productTableSchema;
	protected ITableSchema reviewTableSchema;
	protected ITableSchema userTableSchema;
	
	protected Database(AbstractProduct product) {
		this.product = product;
		this.productTableSchema = product.getProductTableSchema();
		this.reviewTableSchema = product.getReviewTableSchema();
		this.userTableSchema = product.getUserTableSchema();
	}
	
	public abstract boolean initialize();
	
	public void ensureInitialized() {
		if (!this.initialized) {
			this.initialize();
		}
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public abstract Connection getConnection() throws SQLException;
	
	public abstract List<User> getOtherUsersWithPositiveReviews(List<Product> product);

	public abstract Collection<Product> getProductsRecommendedByUsers(Collection<User> otherUsers);

}