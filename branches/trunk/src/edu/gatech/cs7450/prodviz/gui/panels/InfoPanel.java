package edu.gatech.cs7450.prodviz.gui.panels;

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

public class InfoPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;

//	private static final int MAX_CUSTOMER_REVIEWS = 5;
	
	private GraphPanel parent;
	private JPanel container;

	public InfoPanel(GraphPanel parent) {
		super();
		this.parent = parent;
	}
	
	public void initComponents() {
		this.container = new JPanel();
		//this.container.setPreferredSize(new Dimension(400,400));
		this.container.setLayout(new BoxLayout(this.container, BoxLayout.PAGE_AXIS));
		container.setPreferredSize(new Dimension(700,600));
		JScrollPane scrollPane = new JScrollPane(this.container);
		this.add(scrollPane);
	}
	
	public void showProduct(Product product) {
		
		if (container == null) {
			this.initComponents();
		}
		
		if (product != null) {
			
			// load Amazon data
			product.fetchInfoFromAmazon(product.getID());
//			AmazonReview[] reviews = 
//				AmazonAPI.getInstance().extractReviews(product.getBookInformation().getCustReviewIframe());
			
			final Product aProduct = product;
			
			JLabel title = new JLabel(product.getName());
			title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 20));
			
			container.add(title);

			String imageUrl = aProduct.getBookInformation().getImageUrlL();
			if (imageUrl != null) {
				try {
					BufferedImage image = ImageIO.read(new URL(imageUrl));
					ImageIcon icon = new ImageIcon(image);
					container.add(new JLabel(icon));
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
				}
			});
	        
	        JPanel controls = new JPanel();
	        controls.add(seeAmazonPage);
	        controls.add(backToTop);
	        container.add(controls);
	        
//	        for (int i = 0; i < reviews.length && i < MAX_CUSTOMER_REVIEWS; i++) {
//	        	JLabel customerName = new JLabel(reviews[i].getName());
//	        	container.add(customerName);
//	        	JTextArea reviewContent = new JTextArea(reviews[i].getContents());
//	        	reviewContent.setEditable(false);
//	        	container.add(reviewContent);
//	        }
	        
		} else {
			container.add(new JLabel("Product not found, possible problem with Product Identifier"));
		}
		this.validate();
	}
	
	public void resize(Dimension dimension) {
		
	}
}
