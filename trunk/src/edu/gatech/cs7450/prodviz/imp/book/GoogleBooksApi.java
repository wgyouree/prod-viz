package edu.gatech.cs7450.prodviz.imp.book;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GoogleBooksApi {

	private static GoogleBooksApi INSTANCE;
	private GoogleBooksApi() {
		
	}
	
	public static GoogleBooksApi getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GoogleBooksApi();
		}
		return INSTANCE;
	}
	
	public String getGenre(String isbn) {
		String queryUrl = "https://www.googleapis.com/books/v1/volumes?key=" +
				URLEncoder.encode("AIzaSyD1IMNEs34j0ce4YN0Owk-HXmC2SZtj5WU") +
				"&q=isbn+" + isbn;
		
		try {
			// Send data
		    URL url = new URL(queryUrl);
		    URLConnection conn = url.openConnection();
	
		    int status = ((HttpURLConnection) conn).getResponseCode();
		    if (status != 200) {
		    	// connection refused - back off and try again
		    	System.err.println("connection refused, backing off");
		    	Thread.sleep(10 * 1000);
		    	return getGenre(isbn);
		    } else if (status == 404) {
		    	// book not found
		    	return "Not Specified";
		    }
		    
		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    
		    // parse the JSON response
		    String result = "";
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject)parser.parse(rd);
		    JSONArray items = (JSONArray)obj.get("items");
		    if (items != null && items.size() > 0) {
			    JSONObject item = (JSONObject)items.get(0);
			    JSONObject volumeInfo = (JSONObject)item.get("volumeInfo");
			    if (volumeInfo != null) {
			    	JSONArray categories = (JSONArray)volumeInfo.get("categories");
			    	if (categories != null) {
			    		result = (String)categories.get(0);
			    	}
			    }
		    }
		    rd.close();
		    
		    if (result != null && !result.equals("")) {
		    	return result;
		    }
		    
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return "Not Specified";
	}
}
