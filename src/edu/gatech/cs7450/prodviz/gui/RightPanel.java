package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class RightPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static int DEFAULT_WIDTH = 200;
	private static int DEFAULT_HEIGHT = 600;
	
	public RightPanel() {
		this.setMaximumSize(new Dimension(MainFrame.MAXIMUM_SIZE, MainFrame.MAXIMUM_SIZE));
		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, MainFrame.MAXIMUM_SIZE));
	}
}
