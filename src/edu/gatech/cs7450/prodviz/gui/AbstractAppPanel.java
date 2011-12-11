package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class AbstractAppPanel extends JPanel {

	public abstract void initComponents();
	
	public abstract void resize(Dimension dimension);
}
