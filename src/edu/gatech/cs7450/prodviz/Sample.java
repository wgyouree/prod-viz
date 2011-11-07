package edu.gatech.cs7450.prodviz;
import java.io.IOException;

import javax.swing.JEditorPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.ECS.client.jax.Item;
import com.ECS.client.jax.Items;

import edu.gatech.cs7450.prodviz.amazon.AmazonAPI;
import edu.gatech.cs7450.prodviz.amazon.AmazonReview;
import edu.gatech.cs7450.prodviz.amazon.AwsHandlerResolver;


public class Sample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set the service:
		com.ECS.client.jax.AWSECommerceService service = new com.ECS.client.jax.AWSECommerceService();
		service.setHandlerResolver(new AwsHandlerResolver("Z+atru3sGKyGk1Kat34iXtpCwEPegwuveQrsFiI1"));

		//Set the service port:
		com.ECS.client.jax.AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

		//Get the operation object:
		//com.ECS.client.jax.ItemSearchRequest itemRequest = new com.ECS.client.jax.ItemSearchRequest();
		com.ECS.client.jax.ItemLookupRequest itemRequest = new com.ECS.client.jax.ItemLookupRequest();
		
		
		//Fill in the request object:
		itemRequest.setSearchIndex("Books");
		itemRequest.setIdType("ISBN");
		itemRequest.getItemId().add("9780385535137");
		//itemRequest.getResponseGroup().add("Medium");
		itemRequest.getResponseGroup().add("Large");
		
		com.ECS.client.jax.ItemLookup ItemElement= new com.ECS.client.jax.ItemLookup();
		ItemElement.setAWSAccessKeyId("AKIAIL3MUE4F2EWIMGRQ");
		ItemElement.setAssociateTag("bookvisuasoft-20");
		ItemElement.getRequest().add(itemRequest);
		//Call the Web service operation and store the response
		//in the response object:

		com.ECS.client.jax.ItemLookupResponse response = port.itemLookup(ItemElement);
	
		// Get the Title names of all the books for all the items returned in the response
		for (Items itemList : response.getItems()) {
			for (Item item : itemList.getItem()){
				System.out.println("Book Name: " +
				item.getItemAttributes().getTitle()+ " \n");
				
				
				for (int i = 0; i< item.getEditorialReviews().getEditorialReview().size(); i++)
					System.out.printf(item.getEditorialReviews().getEditorialReview().get(i).getSource() + "\n" + item.getEditorialReviews().getEditorialReview().get(i).getContent() + "\n");
				
				System.out.println("Link to customer reviews iFrame " + item.getCustomerReviews().getIFrameURL());
				
				AmazonReview[] reviews = AmazonAPI.getInstance().extractReviews(item.getCustomerReviews().getIFrameURL());
				
				for (int i = 0; i < reviews.length; i++) {
					System.out.println("----------------------------------------");
					System.out.println("Name: " + reviews[i].getName());
					System.out.println("Contents: " + reviews[i].getContents());
				}
				
				System.out.println("Link to medium sized image " + item.getImageSets().get(0).getImageSet().get(0).getMediumImage().getURL());
				
				break; //Only Display the first response
			}
		}
	}

}
