package edu.gatech.cs7450.prodviz.amazon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class AmazonAPI {

	private static AmazonAPI INSTANCE;
	
	// SINGLETON PATTERN
	private AmazonAPI() {
		
	}
	
	public static AmazonAPI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AmazonAPI();
		}
		return INSTANCE;
	}
	
	public AmazonReview[] extractReviews(String url) {
		
		List<AmazonReview> reviews = new ArrayList<AmazonReview>();
		
		HttpGet httpGet = new HttpGet(url);
		
		try {
			ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
				@Override
				public byte[] handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
			        if (entity != null) {
			            return EntityUtils.toByteArray(entity);
			        } else {
			            return null;
			        }
				}
			};
			
			HttpClient httpClient = new DefaultHttpClient();
			byte[] responseBytes = httpClient.execute(httpGet, handler);
			
			String responseString = new String(responseBytes);
			
			Parser parser = Parser.createParser(responseString, "UTF-8");

			// Extract Names ---------------------------------------------------------
			NodeFilter filter = new AndFilter(new NodeFilter[] { new TagNameFilter("span"), new HasParentFilter(new TagNameFilter("a"))});
			
			NodeList nameNodes = parser.extractAllNodesThatMatch(filter);
			
			SimpleNodeIterator it = nameNodes.elements();
			while (it.hasMoreNodes()) {
				AmazonReview newReview = new AmazonReview();
				Node nextNode = it.nextNode();
				newReview.setName(nextNode.getFirstChild().getText());
				reviews.add(newReview);
			}
			
			
			// Extract Contents -------------------------------------------------------
			parser = Parser.createParser(responseString, "UTF-8");
			filter = new AndFilter(new NodeFilter[] { new TagNameFilter("div"), new HasAttributeFilter("style", "margin-left:0.5em;"), new HasParentFilter(new TagNameFilter("td"))});
			
			NodeList contentNodes = parser.extractAllNodesThatMatch(filter);
			
			it = contentNodes.elements();
			int i = 0;
			while (it.hasMoreNodes()) {
				AmazonReview newReview = reviews.get(i++);
				Node nextNode = it.nextNode();
				NodeList children = nextNode.getChildren();
				SimpleNodeIterator cIt = children.elements();
				List<Node> textNodes = new ArrayList<Node>();
				while (cIt.hasMoreNodes()) {
					Node next = cIt.nextNode();
					if (next instanceof TextNode) {
						textNodes.add(next);
					}
				}
				for (int j = 0; j < textNodes.size(); j++) {
					String content = newReview.getContents();
					newReview.setContents(content + textNodes.get(j).getText());
				}
				newReview.setContents(newReview.getContents().trim());
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return reviews.toArray(new AmazonReview[reviews.size()]);
	}
	
	
}
