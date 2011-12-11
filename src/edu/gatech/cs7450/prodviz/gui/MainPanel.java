package edu.gatech.cs7450.prodviz.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import edu.gatech.cs7450.prodviz.gui.panels.GraphPanel;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static int DEFAULT_WIDTH = 600;
	private static int DEFAULT_HEIGHT = 400;
	
	private AbstractAppPanel panel;
	
	public MainPanel() {
		this.setLayout(new BorderLayout());
		this.configureAsMainPanel(this);
	}
	
	public JPanel configureAsMainPanel(JPanel panel) {
		panel.setMaximumSize(new Dimension(MainFrame.MAXIMUM_SIZE, MainFrame.MAXIMUM_SIZE));
		panel.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setPreferredSize(new Dimension(MainFrame.MAXIMUM_SIZE, MainFrame.MAXIMUM_SIZE));
		return panel;
	}
	
	public void addAppPanel(AbstractAppPanel panel) {
		this.panel = panel;
		this.add(panel);
	}
	
	public void resize(Dimension dimension) {
		if (this.panel != null) {
			this.panel.resize(dimension);
		}
	}
}
