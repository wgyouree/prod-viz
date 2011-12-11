package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int DEFAULT_WIDTH = 800;
	public static int DEFAULT_HEIGHT = 200;
	
	public BottomPanel() {
		this.configureAsBottomPanel(this);
	}
	
	public JPanel configureAsBottomPanel(JPanel panel) {
		panel.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		return panel;
	}
}
