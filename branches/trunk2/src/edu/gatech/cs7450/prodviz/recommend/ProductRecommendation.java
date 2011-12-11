package edu.gatech.cs7450.prodviz.recommend;

import edu.gatech.cs7450.prodviz.data.Product;

public class ProductRecommendation {

	private Product product;
	private int weight;
	
	public ProductRecommendation(Product product, int weight) {
		this.product = product;
		this.weight = weight;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
