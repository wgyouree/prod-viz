package edu.gatech.cs7450.prodviz.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends AppFrame {

	private static final long serialVersionUID = 1L;

	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	
	public MainFrame() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeGUI() {
		
	}
}
