package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static int DEFAULT_WIDTH = 800;
	private static int DEFAULT_HEIGHT = 200;
	
	public BottomPanel() {
		this.configureAsBottomPanel(this);
	}
	
	public JPanel configureAsBottomPanel(JPanel panel) {
		panel.setMaximumSize(new Dimension(MainFrame.MAXIMUM_SIZE, MainFrame.MAXIMUM_SIZE));
		panel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setPreferredSize(new Dimension(MainFrame.MAXIMUM_SIZE, DEFAULT_HEIGHT));
		return panel;
	}
}
