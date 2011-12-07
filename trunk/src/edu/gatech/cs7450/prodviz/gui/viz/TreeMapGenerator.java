package edu.gatech.cs7450.prodviz.gui.viz;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import prefuse.data.Graph;
import prefuse.data.Tree;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.TreeMLReader;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.AbstractProduct;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.User;
import edu.gatech.cs7450.prodviz.recommend.IRecommender;
import edu.gatech.cs7450.prodviz.recommend.ProductRecommendation;

public class TreeMapGenerator {
	
	public static class Node {
		
		private String value;
		private int weight = 1;
		private List<Node> children = new ArrayList<Node>();
		
		public Node(String value) {
			this.setValue(value);
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public List<Node> getChildren() {
			return this.children;
		}
		
		public void addChild(Node child) {
			this.children.add(child);
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}
	
	public static Tree createTreeMap(AbstractProduct product, User user, IRecommender recommender) {
		
		Collection<ProductRecommendation> recommendations = 
			recommender.getRecommendations(product, user, ApplicationContext.getInstance().getRecursiveRecommenderDepth());
		
		// create tree structure (using hash maps) from the flat collection

		Map<String, Map<String, Map<Product, Integer>>> firstLevel = new HashMap<String, Map<String, Map<Product, Integer>>>();
		
		Iterator<ProductRecommendation> recommendationsIt = recommendations.iterator();
		//System.out.println("system found [" + recommendations.size() + "] recommendations");
		//int i = 0;
		while (recommendationsIt.hasNext()) {
			//i++;
			ProductRecommendation nextRecommendation = recommendationsIt.next();
			//Product nextProduct = nextRecommendation.getProduct();
			//int weight = nextRecommendation.getWeight();
			Map<String, Map<Product, Integer>> secondLevel = firstLevel.get(nextRecommendation.getProduct().getFirstLevelClassifier());
			if (secondLevel == null) {
				secondLevel = new HashMap<String, Map<Product, Integer>>();
				firstLevel.put(nextRecommendation.getProduct().getFirstLevelClassifier(), secondLevel);
			}
			//System.out.println("Num authors for category [" + nextRecommendation.getProduct().getFirstLevelClassifier() + "] is [" + secondLevel.size() + "]");
			Map<Product, Integer> products = secondLevel.get(nextRecommendation.getProduct().getSecondLevelClassifier());
			if (products == null) {
				products = new HashMap<Product, Integer>();
				secondLevel.put(nextRecommendation.getProduct().getSecondLevelClassifier(), products);
			}
			products.put(nextRecommendation.getProduct(), nextRecommendation.getWeight());
			//System.out.println("Rec number [" + i + "] : Num products for [" + nextRecommendation.getProduct().getSecondLevelClassifier() + "] is [" + products.size() + "]");
		}
		
		// Arrange recommendations in tree form
		Node rootNode = new Node("root");
		
		// create the tree
		Iterator<String> firstLevelIterator = firstLevel.keySet().iterator();
		while (firstLevelIterator.hasNext()) {
			String firstLevelName = firstLevelIterator.next();
			Map<String, Map<Product, Integer>> secondLevel = firstLevel.get(firstLevelName);
			Node firstLevelNode = new Node(firstLevelName);
			rootNode.addChild(firstLevelNode);
			
			Iterator<String> secondLevelIterator = secondLevel.keySet().iterator();
			while (secondLevelIterator.hasNext()) {
				String secondLevelName = secondLevelIterator.next();
				Node secondLevelNode = new Node(secondLevelName);
				firstLevelNode.addChild(secondLevelNode);
				Iterator<Product> productsIt = firstLevel.get(firstLevelName).get(secondLevelName).keySet().iterator();
				while (productsIt.hasNext()) {
					Product aProduct = productsIt.next();
					Node productNode = new Node(aProduct.getName());
					productNode.setWeight(firstLevel.get(firstLevelName).get(secondLevelName).get(aProduct));
					secondLevelNode.addChild(productNode);
				}
			}
		}
		
		Tree t = null;
        try {
            t = (Tree)new TreeMLReader().readGraph(createInputStreamFromXML(rootNode));
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
		
		return t;
	}
	
	private static InputStream createInputStreamFromXML(Node rootNode) {
		
		try {
	        //We need a Document
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	
	        ////////////////////////value
	        //Creating the XML tree
	
	        //create the root element and add it to the document
	        Element root = doc.createElement("tree");
	        doc.appendChild(root);
	        
	        // add declarations
	        Element declarations = doc.createElement("declarations");
	        root.appendChild(declarations);
	        Element attributeDecl = doc.createElement("attributeDecl");
	        attributeDecl.setAttribute("name", "name");
	        attributeDecl.setAttribute("type", "String");
	        declarations.appendChild(attributeDecl);
	        
	        // create root branch
	        Element rootBranch = createElementWithAttribute(doc, "branch", "name", "First Level Classifier");
	        root.appendChild(rootBranch);
	        
	        // transform tree into XML
	        Iterator<Node> firstLevelIt = rootNode.getChildren().iterator();
	        while (firstLevelIt.hasNext()) {
	        	Node firstLevelNode = firstLevelIt.next();
	        	Element firstLevelBranch = createElementWithAttribute(doc, "branch", "name", firstLevelNode.getValue());
	        	rootBranch.appendChild(firstLevelBranch);
	        	
	        	Iterator<Node> secondLevelIt = firstLevelNode.getChildren().iterator();
	        	while (secondLevelIt.hasNext()) {
	        		Node secondLevelNode = secondLevelIt.next();
	        		Element secondLevelBranch = createElementWithAttribute(doc, "branch", "name", secondLevelNode.getValue());
	        		firstLevelBranch.appendChild(secondLevelBranch);
	        		
	        		Iterator<Node> thirdLevelIt = secondLevelNode.getChildren().iterator();
	        		while (thirdLevelIt.hasNext()) {
	        			Node thirdLevelNode = thirdLevelIt.next();
	        			Element leafNode = createElementWithAttribute(doc, "leaf", "name", 
	        					getWeightAsString(thirdLevelNode.getWeight()) + ":" + thirdLevelNode.getValue());
	        			secondLevelBranch.appendChild(leafNode);
	        		}
	        	}
	        	
	        }
	        
	        return writeXmlToInputStream(doc);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getWeightAsString(int weight) {
		String result = "1";
		if (weight > 0) {
			result = "" + weight;
		}
		return result;
	}
	
	private static InputStream writeXmlToInputStream(Document doc) {
		
		try {
			/////////////////
            // Output the XML

            // set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            
            System.out.println(xmlString);
            
			return new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Element createAttribute(Document doc, String attrName, String attrValue) {
		Element attribute = doc.createElement("attribute");
		attribute.setAttribute("name", attrName);
		attribute.setAttribute("value", attrValue);
		return attribute;
	}
	
	private static Element createElementWithAttribute(Document doc, String name, String attrName, String attrValue) {
		Element newElement = doc.createElement(name);
		newElement.appendChild(createAttribute(doc, attrName, attrValue));
		return newElement;
	}
}
