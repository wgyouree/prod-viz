package edu.gatech.cs7450.prodviz.recommend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.Review;
import edu.gatech.cs7450.prodviz.data.User;

public class BFSRecommender implements IRecommender {
	
	@Override
	public Collection<ProductRecommendation> getRecommendations(AbstractProduct product, User user, int depth) {
		// terminating condition
		if (depth == 0) {
			return new ArrayList<ProductRecommendation>();
		}
		
		Database database = product.getDatabase();
		
		// what products has the user reviewed
		Set<Review> reviews = user.getReviews();
		List<Product> reviewedProducts = new ArrayList<Product>();
		Iterator<Review> reviewIt = reviews.iterator();
		while (reviewIt.hasNext()) {
			Review nextReview = reviewIt.next();
			// if we have a positive review, add the product to the list
			if (nextReview.getRating() >= ApplicationContext.getInstance().getPositiveRatingThreshhold()) {
				reviewedProducts.add(nextReview.getProduct());
			}
		}
		
		// who else gave these products a positive review?
		Collection<User> otherUsers = database.getOtherUsersWithPositiveReviews(reviewedProducts);
		
		// what other products did they recommend
		Collection<Product> otherProducts = database.getProductsRecommendedByUsers(otherUsers);
		
		// rank the products based on score
		Map<Product, ProductRecommendation> recommendations = new HashMap<Product, ProductRecommendation>();
		Iterator<Product> otherProductsIt = otherProducts.iterator();
		while (otherProductsIt.hasNext()) {
			Product otherProduct = otherProductsIt.next();
			if (!recommendations.containsKey(otherProduct)) {
				recommendations.put(otherProduct, new ProductRecommendation(otherProduct, 1));
			} else {
				ProductRecommendation currRecommendation = recommendations.get(otherProduct);
				currRecommendation.setWeight(currRecommendation.getWeight() + 1);
			}
		}
		
		// recurse
		List<ProductRecommendation> otherRecommendations = new ArrayList<ProductRecommendation>();
		Iterator<User> otherUsersIt = otherUsers.iterator();
		while (otherUsersIt.hasNext()) {
			User otherUser = otherUsersIt.next();
			otherRecommendations.addAll(getRecommendations(product, otherUser, depth - 1));
		}
		
		// merge collections of recommendations
		recommendations = mergeRecommendations(recommendations, otherRecommendations);
		
		return recommendations.values();
	}
	
	private Map<Product, ProductRecommendation> mergeRecommendations(
			Map<Product, ProductRecommendation> r1, Collection<ProductRecommendation> r2) {
		Iterator<ProductRecommendation> r2It = r2.iterator();
		while (r2It.hasNext()) {
			ProductRecommendation nextRecommendation = r2It.next();
			ProductRecommendation currRecommendation = r1.get(nextRecommendation.getProduct());
			if (currRecommendation == null) {
				r1.put(nextRecommendation.getProduct(),
					   new ProductRecommendation(nextRecommendation.getProduct(), nextRecommendation.getWeight()));
			} else {
				currRecommendation.setWeight(currRecommendation.getWeight() + nextRecommendation.getWeight());
			}
		}
		return r1;
	}
}
