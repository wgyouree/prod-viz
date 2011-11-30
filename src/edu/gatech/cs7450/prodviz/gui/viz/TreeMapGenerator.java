package edu.gatech.cs7450.prodviz.gui.viz;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
	}
	
	public static Tree createTreeMap(AbstractProduct product, User user, IRecommender recommender) {
		
		Collection<ProductRecommendation> recommendations = 
			recommender.getRecommendations(product, user, ApplicationContext.getInstance().getRecursiveRecommenderDepth());
		
		// create tree structure (using hash maps) from the flat collection

		Map<String, Map<String, List<Product>>> firstLevel = new HashMap<String, Map<String, List<Product>>>();
		
		Iterator<ProductRecommendation> recommendationsIt = recommendations.iterator();
		//System.out.println("system found [" + recommendations.size() + "] recommendations");
		//int i = 0;
		while (recommendationsIt.hasNext()) {
			//i++;
			ProductRecommendation nextRecommendation = recommendationsIt.next();
			//Product nextProduct = nextRecommendation.getProduct();
			//int weight = nextRecommendation.getWeight();
			Map<String, List<Product>> secondLevel = firstLevel.get(nextRecommendation.getProduct().getFirstLevelClassifier());
			if (secondLevel == null) {
				secondLevel = new HashMap<String, List<Product>>();
				firstLevel.put(nextRecommendation.getProduct().getFirstLevelClassifier(), secondLevel);
			}
			//System.out.println("Num authors for category [" + nextRecommendation.getProduct().getFirstLevelClassifier() + "] is [" + secondLevel.size() + "]");
			List<Product> products = secondLevel.get(nextRecommendation.getProduct().getSecondLevelClassifier());
			if (products == null) {
				products = new ArrayList<Product>();
				secondLevel.put(nextRecommendation.getProduct().getSecondLevelClassifier(), products);
			}
			products.add(nextRecommendation.getProduct());
			//System.out.println("Rec number [" + i + "] : Num products for [" + nextRecommendation.getProduct().getSecondLevelClassifier() + "] is [" + products.size() + "]");
		}
		
		// Arrange recommendations in tree form
		Node rootNode = new Node("root");
		
		// create the tree
		Iterator<String> firstLevelIterator = firstLevel.keySet().iterator();
		while (firstLevelIterator.hasNext()) {
			String firstLevelName = firstLevelIterator.next();
			Map<String, List<Product>> secondLevel = firstLevel.get(firstLevelName);
			Node firstLevelNode = new Node(firstLevelName);
			rootNode.addChild(firstLevelNode);
			
			Iterator<String> secondLevelIterator = secondLevel.keySet().iterator();
			while (secondLevelIterator.hasNext()) {
				String secondLevelName = secondLevelIterator.next();
				Node secondLevelNode = new Node(secondLevelName);
				firstLevelNode.addChild(secondLevelNode);
				Iterator<Product> productsIt = firstLevel.get(firstLevelName).get(secondLevelName).iterator();
				while (productsIt.hasNext()) {
					Product aProduct = productsIt.next();
					Node productNode = new Node(aProduct.getName());
					secondLevelNode.addChild(productNode);
				}
			}
		}
		
		// Because Prefuse is stupid, we must now write the tree to an XML file in a particular format....
		// and then, obviously, we have Prefuse parse that XML file into a Table!! Yeah, useless file I/O!!
		Tree t = null;
        try {
			File tempFile = new File("tmp.xml");
            t = (Tree)new TreeMLReader().readGraph(new FileInputStream(outputTreeToFile(rootNode, tempFile)));
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
		
		return t;
	}
	
	private static File outputTreeToFile(Node rootNode, File tempFile) {
		
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
	        Element rootBranch = createElement(doc, "branch", "name", "value", "First Level Classifier");
	        root.appendChild(rootBranch);
	        
	        // transform tree into XML
	        Iterator<Node> firstLevelIt = rootNode.getChildren().iterator();
	        while (firstLevelIt.hasNext()) {
	        	Node firstLevelNode = firstLevelIt.next();
	        	Element firstLevelBranch = createElement(doc, "branch", "name", "value", firstLevelNode.getValue());
	        	rootBranch.appendChild(firstLevelBranch);
	        	
	        	Iterator<Node> secondLevelIt = firstLevelNode.getChildren().iterator();
	        	while (secondLevelIt.hasNext()) {
	        		Node secondLevelNode = secondLevelIt.next();
	        		Element secondLevelBranch = createElement(doc, "branch", "name", "value", secondLevelNode.getValue());
	        		firstLevelBranch.appendChild(secondLevelBranch);
	        		
	        		Iterator<Node> thirdLevelIt = secondLevelNode.getChildren().iterator();
	        		while (thirdLevelIt.hasNext()) {
	        			Node thirdLevelNode = thirdLevelIt.next();
	        			Element leafNode = createElement(doc, "leaf", "name", "value", thirdLevelNode.getValue());
	        			secondLevelBranch.appendChild(leafNode);
	        		}
	        	}
	        	
	        }
	        
	        return writeXmlToFile(doc, tempFile);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static File writeXmlToFile(Document doc, File tempFile) {
		
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
            
            // write to file
            FileWriter fw = new FileWriter(tempFile);
            BufferedWriter fbw = new BufferedWriter(fw);
            fbw.write(xmlString);
            fbw.close();
            
			return new File("tmp2.xml");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Element createElement(Document doc, String name, String nodeName, String attrName, String attrValue) {
		Element newElement = doc.createElement(name);
		
		Element attribute = doc.createElement("attribute");
		attribute.setAttribute("name", nodeName);
		attribute.setAttribute(attrName, attrValue);
		newElement.appendChild(attribute);
		
		return newElement;
	}
}
