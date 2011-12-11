package edu.gatech.cs7450.prodviz.amazon;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Classifier;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.IProductTableSchema;
import edu.gatech.cs7450.prodviz.data.IReviewTableSchema;
import edu.gatech.cs7450.prodviz.data.ITableSchema;
import edu.gatech.cs7450.prodviz.data.IUserTableSchema;
import edu.gatech.cs7450.prodviz.data.SQLDatabase;

public class InfoImport extends AbstractProduct {

	public InfoImport() {
		super("Books",
			  new Classifier("Title"),
			  new Classifier(""),
			  new IProductTableSchema() {
				
				@Override
				public String getTableName() {
					return "`BX-Books`";
				}
				
				@Override
				public String getIdFieldName() {
					return "ISBN";
				}
				
				@Override
				public String getNameFieldName() {
					return "`Book-Title`";
				}
				
				@Override
				public String getFirstLevelClassifierFieldName() {
					return "genre";
				}
				
				@Override
				public String getSecondLevelClassifierFieldName() {
					return "`Book-Author`";
				}
			  },
			  new IReviewTableSchema() {
					
				@Override
				public String getTableName() {
					return "`BX-Book-Ratings`";
				}
				
				@Override
				public String getIdFieldName() {
					return "`User-ID`,ISBN";
				}
				
				@Override
				public String getRatingFieldName() {
					return "`Book-Rating'";
				}
				
				@Override
				public String getProductIdFieldName() {
					return "ISBN";
				}
				
				@Override
				public String getUserIdFieldName() {
					return "`User-ID`";
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
