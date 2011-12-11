package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class RightPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int DEFAULT_WIDTH = 200;
	public static int DEFAULT_HEIGHT = 600;
	
	public RightPanel() {
		this.configureAsRightPanel(this);
	}
	
	public JPanel configureAsRightPanel(JPanel panel) {
		panel.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		return panel;
	}
}
