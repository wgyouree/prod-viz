package edu.gatech.cs7450.prodviz.gui.viz;

import prefuse.data.Tree;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.User;
import edu.gatech.cs7450.prodviz.recommend.IRecommender;
import edu.gatech.cs7450.prodviz.recommend.ProductRecommendation;

public class TreeMapGenerator {
	
	private static final int DEPTH = 2;
	
	public static Tree createTreeMap(Product product, User user, IRecommender recommender) {
		// create Tree from data
		Tree tree = new Tree();
		
		ProductRecommendation[] recommendations = recommender.getRecommendations(product, user, DEPTH);
		
		// Arrange recommendations in tree form
		
		
		// 1) First group by first level classifier
		
		
		// 2) Next sub-group by second level classifier
		
		
		// 3) Put the nodes in tree form and return it (tree of depth 3, root->1st->2nd)
		
		
		return tree;
	}
}
