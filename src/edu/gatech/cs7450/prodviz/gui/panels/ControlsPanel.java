package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.gui.AbstractAppPanel;

public class ControlsPanel extends AbstractAppPanel {

	private static final long serialVersionUID = 1L;
	
	public ControlsPanel() {
		this.setBackground(Color.YELLOW);
		this.setLayout(new GridBagLayout());
	}
	
	public void initComponents() {
		ComboBoxModel productModel = new DefaultComboBoxModel(ApplicationContext.getInstance().getProducts());
		JComboBox productBox = new JComboBox(productModel);
		GridBagConstraints productBoxConstraints = new GridBagConstraints();
		productBoxConstraints.gridx = 0;
		productBoxConstraints.gridy = 0;
		productBoxConstraints.gridwidth = 1;
		productBoxConstraints.gridheight = 1;
		productBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		productBoxConstraints.anchor = GridBagConstraints.PAGE_START;
		productBoxConstraints.weightx = 0.5;
		productBoxConstraints.weighty = 0.5;
		this.add(productBox, productBoxConstraints);
	}
}
