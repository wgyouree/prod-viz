package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static int DEFAULT_WIDTH = 800;
	private static int DEFAULT_HEIGHT = 200;
	
	public BottomPanel() {
		this.setMaximumSize(new Dimension(MainFrame.MAXIMUM_SIZE, MainFrame.MAXIMUM_SIZE));
		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setPreferredSize(new Dimension(MainFrame.MAXIMUM_SIZE, DEFAULT_HEIGHT));
	}
}
