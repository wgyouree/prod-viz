package edu.gatech.cs7450.prodviz.data;

import com.ECS.client.jax.Item;
import com.ECS.client.jax.Items;

import edu.gatech.cs7450.prodviz.amazon.AmazonAPI;
import edu.gatech.cs7450.prodviz.amazon.AmazonReview;
import edu.gatech.cs7450.prodviz.amazon.AwsHandlerResolver;


public class Product extends PersistentObject {

	private String name;
	private String firstLevelClassifier;
	private String secondLevelClassifier;
	private BookInformation book;
	
	public Product(String ID, String name, String firstLevelClassifier, String secondLevelClassifier) {
		super(ID);
		this.name = name;
		this.firstLevelClassifier = firstLevelClassifier;
		this.secondLevelClassifier = secondLevelClassifier;
		this.book = new BookInformation();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLevelClassifier() {
		return firstLevelClassifier;
	}

	public String getSecondLevelClassifier() {
		return secondLevelClassifier;
	}
	
	public void setBookInformaiton(BookInformation book){
		this.book = book;
	}
	
	public BookInformation getBookInformation(){
		return book;
	}
	
	public void fetchInfoFromAmazon(String isbn){
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
		itemRequest.getItemId().add(isbn);
		itemRequest.getResponseGroup().add("Large");
		
		com.ECS.client.jax.ItemLookup ItemElement= new com.ECS.client.jax.ItemLookup();
		ItemElement.setAWSAccessKeyId("AKIAIL3MUE4F2EWIMGRQ");
		ItemElement.setAssociateTag("bookvisuasoft-20");
		ItemElement.getRequest().add(itemRequest);

		com.ECS.client.jax.ItemLookupResponse response = port.itemLookup(ItemElement);
		
		// Get the Title names of all the books for all the items returned in the response
		for (Items itemList : response.getItems()) {
			for (Item item : itemList.getItem()){
				
				book.setTitle (item.getItemAttributes().getTitle());
				book.setAuthor(item.getItemAttributes().getAuthor().get(0));
				book.setPrice(item.getItemAttributes().getListPrice().getFormattedPrice());
				book.setAvailability (item.getOffers().getOffer().get(0).getOfferListing().get(0).getAvailability());
				book.setLanguage(item.getItemAttributes().getLanguages().getLanguage().get(0).getName());
				book.setNumPages (item.getItemAttributes().getNumberOfPages());
				book.setPublisher (item.getItemAttributes().getPublisher());
				book.setPublishDate( item.getItemAttributes().getPublicationDate());
				book.setSalesRank (item.getSalesRank());
				book.setImageUrlL(item.getImageSets().get(0).getImageSet().get(0).getLargeImage().getURL());
				book.setImageUrlM (item.getImageSets().get(0).getImageSet().get(0).getMediumImage().getURL());
				book.setImageUrlS(item.getImageSets().get(0).getImageSet().get(0).getSmallImage().getURL());
				book.setImageUrlSS (item.getImageSets().get(0).getImageSet().get(0).getSwatchImage().getURL());
				book.setAsin( item.getASIN());
				book.setProductUrl ("http://www.amazon.com/gp/product/" + item.getASIN());
				book.setCustReviewIframe (item.getCustomerReviews().getIFrameURL());
				
				book.setNumEditorialReviews (item.getEditorialReviews().getEditorialReview().size());
				
				String[] editorialSource =  new String[item.getEditorialReviews().getEditorialReview().size()];
				String[] editorialContent =  new String[item.getEditorialReviews().getEditorialReview().size()];
				
				for (int i = 0; i< item.getEditorialReviews().getEditorialReview().size(); i++){
					editorialSource[i] = item.getEditorialReviews().getEditorialReview().get(i).getSource();
					editorialContent[i] = item.getEditorialReviews().getEditorialReview().get(i).getContent();
				}
				
				book.setEditorialReviewSource(editorialSource);
				book.setEditorialReviewContent(editorialContent);
				
				AmazonReview[] reviews = AmazonAPI.getInstance().extractReviews(item.getCustomerReviews().getIFrameURL());
				
				book.setNumCustReviews(reviews.length);
				
				String[] customerSource = new String[reviews.length];
				String[] customerContent = new String[reviews.length];
				
				for (int i = 0; i < reviews.length; i++) {
					customerSource[i] = reviews[i].getName();
					customerContent[i] =  reviews[i].getContents();
				}
				
				book.setCustomerReviewContent(customerContent);
				book.setCustomerReviewSource(customerSource);
				
				break; //Only Display the first response
			}
		}		
	}
}
