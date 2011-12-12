package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import prefuse.util.FontLib;

import edu.gatech.cs7450.prodviz.amazon.AmazonAPI;
import edu.gatech.cs7450.prodviz.amazon.AmazonReview;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;

public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

//	private static final int MAX_CUSTOMER_REVIEWS = 5;
	
	private GraphPanel parent;
	private Product product;

	public InfoPanel(GraphPanel parent, Product product) {
		//super();
		this.parent = parent;
		this.product = product;
		this.initComponents();
	}
//	
	public void initComponents() {
		this.setLayout(new BorderLayout());
		
		if (product != null) {
			
			// load Amazon data
			product.fetchInfoFromAmazon(product.getID());
//			AmazonReview[] reviews = 
//				AmazonAPI.getInstance().extractReviews(product.getBookInformation().getCustReviewIframe());
			
			final Product aProduct = product;
			
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
			
			JLabel title = new JLabel(product.getName());
			title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 20));
			JPanel titlePanel = new JPanel();
			titlePanel.add(title);
			
			topPanel.add(titlePanel);

			String imageUrl = aProduct.getBookInformation().getImageUrlL();
			if (imageUrl != null) {
				try {
					BufferedImage image = ImageIO.read(new URL(imageUrl));
					ImageIcon icon = new ImageIcon(image);
					this.add(new JLabel(icon), BorderLayout.CENTER);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Failed to display image from [" + imageUrl + "]");
				}
			} else {
				System.err.println("Product has no Amazon Image");
			}

			final JButton seeAmazonPage = new JButton("See the Amazon Product page");
			seeAmazonPage.setVerticalAlignment(SwingConstants.BOTTOM);
			seeAmazonPage.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
			seeAmazonPage.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String url = aProduct.getBookInformation().getProductUrl();
					if (url == null) {
						System.err.println("No Amazon Product Page URL");
						return;
					}
					try {
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			});
			
	        final JButton backToTop = new JButton("Back to Top");
	        backToTop.setVerticalAlignment(SwingConstants.BOTTOM);
	        backToTop.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
	        backToTop.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					parent.swapPanel(null, null);
					parent.resizeAll();
				}
			});
	        
	        JPanel controls = new JPanel();
	        controls.add(seeAmazonPage);
	        controls.add(backToTop);
	        topPanel.add(controls);
	        
	        this.add(topPanel, BorderLayout.NORTH);
	        
//	        JPanel customerReviewPanel = new JPanel();
//	        for (int i = 0; i < reviews.length && i < MAX_CUSTOMER_REVIEWS; i++) {
//	        	JLabel customerName = new JLabel(reviews[i].getName());
//	        	customerReviewPanel.add(customerName);
//	        	JTextArea reviewContent = new JTextArea(reviews[i].getContents());
//	        	reviewContent.setEditable(false);
//	        	customerReviewPanel.add(reviewContent);
//	        }
//	        this.add(customerReviewPanel, BorderLayout.CENTER);
	        
		} else {
			this.add(new JLabel("Product not found, possible problem with Product Identifier"));
		}
		
		this.validate();
	}
}
