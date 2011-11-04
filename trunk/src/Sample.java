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
		com.ECS.client.jax.ItemSearchRequest itemRequest = new com.ECS.client.jax.ItemSearchRequest();

		//Fill in the request object:
		itemRequest.setSearchIndex("Books");
		itemRequest.setPower("ISBN:9781451648539");

		com.ECS.client.jax.ItemSearch ItemElement= new com.ECS.client.jax.ItemSearch();
		ItemElement.setAWSAccessKeyId("AKIAIL3MUE4F2EWIMGRQ");
		ItemElement.setAssociateTag("bookvisuasoft-20");
		ItemElement.getRequest().add(itemRequest);
		//Call the Web service operation and store the response
		//in the response object:
		com.ECS.client.jax.ItemSearchResponse response = port.itemSearch(ItemElement);
	
		// Get the Title names of all the books for all the items returned in the response
		for (Items itemList : response.getItems()) {
			for (Item item : itemList.getItem()){
				System.out.println("Book Name: " +
				item.getItemAttributes().getTitle()+
				item.getItemAttributes().getProductGroup());
			}
		}
	}

}
