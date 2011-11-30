package edu.gatech.cs7450.prodviz.amazon;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Classifier;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.IProductTableSchema;
import edu.gatech.cs7450.prodviz.data.IReviewTableSchema;
import edu.gatech.cs7450.prodviz.data.ITableSchema;
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
			  new ITableSchema() {
					
				@Override
				public String getTableName() {
					return "`BX-Users`";
				}
				
				@Override
				public String getIdFieldName() {
					return "`User-ID`";
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
