package edu.gatech.cs7450.prodviz.gui.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.gui.AppPanelImp;

public class ControlsPanel extends AppPanelImp {

	private static final long serialVersionUID = 1L;
	
	public ControlsPanel() {
		this.setBackground(Color.YELLOW);
	}
	
	public void initComponents() {
		GridBagLayout layout = new GridBagLayout();
		ComboBoxModel productModel = new DefaultComboBoxModel(ApplicationContext.getInstance().getProducts());
		JComboBox productBox = new JComboBox(productModel);
		GridBagConstraints productBoxConstraints = new GridBagConstraints();
		productBoxConstraints.gridx = 1;
		productBoxConstraints.gridy = 1;
		productBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(productBox, productBoxConstraints);
	}
}
