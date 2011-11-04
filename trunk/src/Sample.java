import com.ECS.client.jax.Item;
import com.ECS.client.jax.Items;


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
		itemRequest.getResponseGroup().add("EditorialReviews");
		
		
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
				item.getItemAttributes().getTitle()+ " " +
				item.getEditorialReviews().toString());
			}
		}
	}

}
