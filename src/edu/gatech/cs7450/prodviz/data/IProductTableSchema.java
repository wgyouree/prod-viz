package edu.gatech.cs7450.prodviz.data;

public interface IProductTableSchema extends ITableSchema {

	public String getNameFieldName();
	
	public String getFirstLevelClassifierFieldName();
	
	public String getSecondLevelClassifierFieldName();
}
