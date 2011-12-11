package edu.gatech.cs7450.prodviz.imp.book;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Classifier;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.IProductTableSchema;
import edu.gatech.cs7450.prodviz.data.IReviewTableSchema;
import edu.gatech.cs7450.prodviz.data.ITableSchema;
import edu.gatech.cs7450.prodviz.data.IUserTableSchema;
import edu.gatech.cs7450.prodviz.data.SQLDatabase;

public class BookImp extends AbstractProduct {

	public BookImp() {
		super("Books",
			  new Classifier("Genre"),
			  new Classifier("Author"),
			  new IProductTableSchema() {
				
				@Override
				public String getTableName() {
					return "BX_Books";
				}
				
				@Override
				public String getIdFieldName() {
					return "ISBN";
				}
				
				@Override
				public String getNameFieldName() {
					return "Book_Title";
				}
				
				@Override
				public String getFirstLevelClassifierFieldName() {
					return "genre";
				}
				
				@Override
				public String getSecondLevelClassifierFieldName() {
					return "Book_Author";
				}
			  },
			  new IReviewTableSchema() {
					
				@Override
				public String getTableName() {
					return "BX_Book_Ratings";
				}
				
				@Override
				public String getIdFieldName() {
					return "User_ID,ISBN";
				}
				
				@Override
				public String getRatingFieldName() {
					return "Book_Rating";
				}
				
				@Override
				public String getProductIdFieldName() {
					return "ISBN";
				}
				
				@Override
				public String getUserIdFieldName() {
					return "User_ID";
				}
			  },
			  new IUserTableSchema() {
				
				@Override
				public String getTableName() {
					return "BX_Users";
				}
				
				@Override
				public String getIdFieldName() {
					return "User_ID";
				}
				
				@Override
				public String getUserLocationFieldName() {
					return "Location";
				}
				
				@Override
				public String getUserIdFieldName() {
					return "User_ID";
				}
				
				@Override
				public String getUserAgeFieldName() {
					return "Age";
				}
			});

		  this.setDatabase(new SQLDatabase(this, new DatabaseConfig(
			"com.mysql.jdbc.Driver",
			"jdbc:mysql://localhost/",
			"books",
			"admin",
			"admin")));
	}
}
