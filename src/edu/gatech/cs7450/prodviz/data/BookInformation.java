package edu.gatech.cs7450.prodviz.data;

import java.math.BigInteger;

public class BookInformation {

	
	private String title;
	private String author;
	private String price;
	private String availability;
	private String language;
	private BigInteger numPages;
	private String publisher;
	private String publishDate;
	private String salesRank;
	private String imageUrlL;
	private String imageUrlM;
	private String imageUrlS;
	private String imageUrlSS;
	private String asin;
	private String productUrl;
	private String custReviewIframe;
	private String[] editorialReviewSource;
	private String[] editorialReviewContent;
	private String[] customerReviewSource;
	private String[] customerReviewContent;
	private int numCustReviews;
	private int numEditorialReviews;
	
	
	
	public BookInformation() {
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	public void setPrice(String price){
		this.price =  price;
	}
	public void setAvailability(String availability){
		this.availability = availability;
	}	
	public void setLanguage(String language){
		this.language =  language;
	}
	public void setNumPages(BigInteger numPages){
		this.numPages = numPages;
	}
	public void setPublisher(String publisher){
		this.publisher= publisher;
	}
	public void setPublishDate(String publishDate){
		this.publishDate = publishDate;
	}
	public void setSalesRank(String salesRank){
		this.salesRank = salesRank;
	}
	public void setImageUrlL(String imageUrlL){
		this.imageUrlL = imageUrlL;
	}
	public void setImageUrlM(String imageUrlM){
		this.imageUrlM = imageUrlM;
	}
	public void setImageUrlS(String imageUrlS){
		this.imageUrlS= imageUrlS;
	}
	public void setImageUrlSS(String imageUrlSS){
		this.imageUrlSS =  imageUrlSS;
	}
	public void setAsin(String asin){
		this.asin = asin;
	}
	public void setProductUrl(String productUrl){
		this.productUrl =  productUrl;
	}
	public void setCustReviewIframe(String custReviewIframe){
		this.custReviewIframe= custReviewIframe;
	}
	public  void setEditorialReviewSource(String[] editorialReviewSource){
		this.editorialReviewSource = editorialReviewSource;
	}
	public void setEditorialReviewContent(String[] editorialReviewContent){
		this.editorialReviewContent = editorialReviewContent;
	}
	public void setCustomerReviewSource(String[] customerReviewSource){
		this.customerReviewSource = customerReviewSource;
	}
	public void setCustomerReviewContent(String[] customerReviewContent){
		this.customerReviewContent =  customerReviewContent;
	}
	public void setNumCustReviews(int numCustReviews){
		this.numCustReviews =  numCustReviews;
	}
	public void setNumEditorialReviews(int numEditorialReviews){
		this.numEditorialReviews =  numEditorialReviews;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAuthor(){
		return author;
	}
	public String getPrice(){
		return price;
	}
	public String getAvailability(){
		return availability;
	}	
	public String getLanguage(){
		return language;
	}
	public BigInteger getNumPages(){
		return numPages;
	}
	public String getPublisher(){
		return publisher;
	}
	public String getPublishDate(){
		return publishDate;
	}
	public String getSalesRank(){
		return salesRank;
	}
	public String getImageUrlL(){
		return imageUrlL;
	}
	public String getImageUrlM(){
		return imageUrlM;
	}
	public String getImageUrlS(){
		return imageUrlS;
	}
	public String getImageUrlSS(){
		return imageUrlSS;
	}
	public String getAsin(){
		return asin;
	}
	public String getProductUrl(){
		return productUrl;
	}
	public String getCustReviewIframe(){
		return custReviewIframe;
	}
	public  String[] getEditorialReviewSource(){
		return editorialReviewSource;
	}
	public String[] getEditorialReviewContent(){
		return editorialReviewContent;
	}
	public String[] getCustomerReviewSource(){
		return customerReviewSource;
	}
	public String[] getCustomerReviewContent(){
		return customerReviewContent;
	}
	public int getNumCustReviews(){
		return numCustReviews;
	}
	public int getNumEditorialReviews(){
		return numEditorialReviews;
	}
}
