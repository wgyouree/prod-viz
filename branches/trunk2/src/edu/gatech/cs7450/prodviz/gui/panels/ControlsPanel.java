package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.data.Review;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;
import edu.gatech.cs7450.prodviz.gui.MainFrame;

public class ControlsPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;
	
	private MainFrame parent;
	
	public ControlsPanel(MainFrame parent) {
		this.parent = parent;
		this.setBackground(Color.YELLOW);
		this.setLayout(new GridBagLayout());
	}
	
	public void initComponents() {
		ComboBoxModel productModel = new DefaultComboBoxModel(ApplicationContext.getInstance().getProducts());
		JComboBox productBox = new JComboBox(productModel);
//		
//		GridBagConstraints productBoxConstraints = new GridBagConstraints();
//		productBoxConstraints.gridx = 0;
//		productBoxConstraints.gridy = 0;
//		productBoxConstraints.gridwidth = 1;
//		productBoxConstraints.gridheight = 1;
//		productBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
//		productBoxConstraints.anchor = GridBagConstraints.PAGE_START;
//		productBoxConstraints.weightx = 0.5;
//		productBoxConstraints.weighty = 0.5;
//		this.add(productBox, productBoxConstraints);

		final JTextField currentProduct = new JTextField();
		currentProduct.setText(ApplicationContext.getInstance().getActiveReviewName());
		currentProduct.setEditable(false);
		
		JPanel buttonPanel = new JPanel();
		JButton changeSelectedProductButton = new JButton("Change Product");
		changeSelectedProductButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Product[] products = ApplicationContext.getInstance().getActiveProduct().getDatabase().getAllProducts();
				Product selectedProduct = (Product)JOptionPane.showInputDialog(
						parent,
						"Select the product which will be used to generate recommendations:",
						"Current Product Selector",
						JOptionPane.PLAIN_MESSAGE,
						null,
						products,
						null);
				
				if (selectedProduct != null) {
					ApplicationContext.getInstance().setActiveReview(new Review( "1", 1, 10, selectedProduct));
					parent.resizeAll();
					currentProduct.setText(selectedProduct.getName());
				}
			}
		});
		buttonPanel.add(changeSelectedProductButton);
		
//		GridBagConstraints buttonConstraints = new GridBagConstraints();
//		buttonConstraints.gridx = 0;
//		buttonConstraints.gridy = 1;
//		buttonConstraints.gridwidth = 1;
//		buttonConstraints.gridheight = 1;
//		buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
//		buttonConstraints.anchor = GridBagConstraints.PAGE_START;
//		buttonConstraints.weightx = 0.5;
//		buttonConstraints.weighty = 0.5;
//		this.add(currentProduct, buttonConstraints);
//		
//		buttonConstraints.gridy = 2;
//		this.add(buttonPanel, buttonConstraints);
		
		this.setLayout(new BorderLayout());
		this.add(productBox, BorderLayout.WEST);
		this.add(currentProduct, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.EAST);
	}
	
	public void resize(Dimension dimension) {
		
	}
}
