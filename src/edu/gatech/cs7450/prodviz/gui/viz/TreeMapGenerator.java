package edu.gatech.cs7450.prodviz.gui.viz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tree;
import prefuse.data.tuple.TableNode;
import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.User;
import edu.gatech.cs7450.prodviz.recommend.IRecommender;
import edu.gatech.cs7450.prodviz.recommend.ProductRecommendation;

public class TreeMapGenerator {
	
	public static Tree createTreeMap(AbstractProduct product, User user, IRecommender recommender) {
		
		Collection<ProductRecommendation> recommendations = 
			recommender.getRecommendations(product, user, ApplicationContext.getInstance().getRecursiveRecommenderDepth());
		
		// create tree structure (using hash maps) from the flat collection
		List<Map<String, Map<String, List<Product>>>> treeStructure = new ArrayList<Map<String, Map<String, List<Product>>>>();
		Iterator<ProductRecommendation> recommendationsIt = recommendations.iterator();
		while (recommendationsIt.hasNext()) {
			ProductRecommendation nextRecommendation = recommendationsIt.next();
			Product nextProduct = nextRecommendation.getProduct();
			int weight = nextRecommendation.getWeight();
			Map<String, Map<String, List<Product>>> firstLevel = treeStructure.get(0);
			if (firstLevel == null) {
				firstLevel = new HashMap<String, Map<String, List<Product>>>();
				treeStructure.add(firstLevel);
			}
			Map<String, List<Product>> secondLevel = firstLevel.get(nextRecommendation.getProduct().getSecondLevelClassifier());
			if (secondLevel == null) {
				secondLevel = new HashMap<String, List<Product>>();
				firstLevel.put(nextRecommendation.getProduct().getFirstLevelClassifier(), secondLevel);
			}
			List<Product> products = secondLevel.get(nextRecommendation.getProduct().getSecondLevelClassifier());
			if (products == null) {
				products = new ArrayList<Product>();
				secondLevel.put(nextRecommendation.getProduct().getSecondLevelClassifier(), products);
			}
			products.add(nextRecommendation.getProduct());
		}
		
		// Arrange recommendations in tree form
		Graph graph = new Graph();
		Node rootNode = new TableNode();
		
		// 1) First group by first level classifier
		Iterator<String> firstLevelIterator = treeStructure.get(0).keySet().iterator();
		while (firstLevelIterator.hasNext()) {
			String firstLevelName = firstLevelIterator.next();
			Map<String, List<Product>> secondLevel = treeStructure.get(0).get(firstLevelName);
			Node firstLevelNode = new TableNode();
			firstLevelNode.set(0, firstLevelName);
			graph.addEdge(rootNode, firstLevelNode);
			Iterator<String> secondLevelIterator = secondLevel.keySet().iterator();
			while (secondLevelIterator.hasNext()) {
				String secondLevelName = secondLevelIterator.next();
				Node secondLevelNode = new TableNode();
				secondLevelNode.set(0, secondLevelName);
				graph.addEdge(firstLevelNode, secondLevelNode);
				Iterator<Product> productsIt = treeStructure.get(0).get(firstLevelName).get(secondLevelName).iterator();
				while (productsIt.hasNext()) {
					Product aProduct = productsIt.next();
					Node productNode = new TableNode();
					productNode.set(0, aProduct);
					graph.addEdge(secondLevelNode, productNode);
				}
			}
		}
		
		Table nodeTable = graph.getNodeTable();
		Table edgeTable = graph.getEdgeTable();
		
		return new Tree(nodeTable, edgeTable);
	}
}
