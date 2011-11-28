package edu.gatech.cs7450.prodviz.recommend;

import java.util.Collection;

import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.User;

public interface IRecommender {
	
	public Collection<ProductRecommendation> getRecommendations(AbstractProduct product, User user, int depth);
}
