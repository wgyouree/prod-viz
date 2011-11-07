package edu.gatech.cs7450.prodviz.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.gatech.cs7450.prodviz.gui.panels.BarPanel;
import edu.gatech.cs7450.prodviz.gui.panels.ControlsPanel;
import edu.gatech.cs7450.prodviz.gui.panels.GraphPanel;

public class MainFrame extends AppFrame {

	private static final long serialVersionUID = 1L;

	public enum AppPanel {
		GRAPH(new GraphPanel()), BAR(new BarPanel()), CONTROLS(new ControlsPanel());
		
		private JPanel panel;
		
		private AppPanel(JPanel panel) {
			this.panel = panel;
		}
		
		public JPanel getPanel() {
			return this.panel;
		}
	}
	
	public static int MAXIMUM_SIZE = 10000;
	
	private static AppPanel DEFAULT_MAIN_PANEL = AppPanel.GRAPH;
	private static AppPanel DEFAULT_BOTTOM_PANEL = AppPanel.BAR;
	private static AppPanel DEFAULT_RIGHT_PANEL = AppPanel.CONTROLS;
	
	private static int DEFAULT_WIDTH = 800;
	private static int DEFAULT_HEIGHT = 600;
	
	private MainPanel mainPanel;
	private BottomPanel bottomPanel;
	private RightPanel rightPanel;
	
	public MainFrame(String title) {
		this.setTitle(title);
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeGUI() {
		
		JPanel contentPane = new JPanel();
		
		BorderLayout layout = new BorderLayout();
		contentPane.setLayout(layout);
		
		this.mainPanel = new MainPanel();
		this.bottomPanel = new BottomPanel();
		this.rightPanel = new RightPanel();
		
		contentPane.add(this.mainPanel, BorderLayout.CENTER);
		contentPane.add(this.bottomPanel, BorderLayout.SOUTH);
		contentPane.add(this.rightPanel, BorderLayout.EAST);
		
		this.setContentPane(contentPane);
		
		this.constructGUI(DEFAULT_MAIN_PANEL, DEFAULT_BOTTOM_PANEL, DEFAULT_RIGHT_PANEL);
	}
	
	public void constructGUI(AppPanel main, AppPanel bottom, AppPanel right) {
		this.setMainPanel(main);
		this.setBottomPanel(bottom);
		this.setRightPanel(right);
	}
	
	public void setMainPanel(AppPanel panel) {
		this.mainPanel.removeAll();
		this.mainPanel.add(panel.getPanel());
		this.invalidate();
	}
	
	public void setBottomPanel(AppPanel panel) {
		this.bottomPanel.removeAll();
		this.bottomPanel.add(panel.getPanel());
		this.invalidate();
	}
	
	public void setRightPanel(AppPanel panel) {
		this.rightPanel.removeAll();
		this.rightPanel.add(panel.getPanel());
		this.invalidate();
	}
}
