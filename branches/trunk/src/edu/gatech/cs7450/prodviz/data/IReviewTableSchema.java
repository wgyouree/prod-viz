package edu.gatech.cs7450.prodviz.data;

public interface IReviewTableSchema extends ITableSchema {

	public String getRatingFieldName();
	
	public String getProductIdFieldName();
	
	public String getUserIdFieldName();
}
