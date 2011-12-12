package edu.gatech.cs7450.prodviz.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.gatech.cs7450.prodviz.data.LocationRatingPair;
import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.panels.BarPanel;
import edu.gatech.cs7450.prodviz.gui.panels.ControlsPanel;
import edu.gatech.cs7450.prodviz.gui.panels.GraphPanel;

public class MainFrame extends AppFrame {

	private static final long serialVersionUID = 1L;
	
	//public static int MAXIMUM_SIZE = 10000;
	
	private AbstractAppPanel DEFAULT_MAIN_PANEL;
	private AbstractBottomAppPanel DEFAULT_BOTTOM_PANEL;
	private AbstractAppPanel DEFAULT_CONTROLS_PANEL;
	
	private AbstractBottomAppPanel bottomGraphPanel;
//	private AbstractBottomAppPanel bottomMapPanel;
	
	private static int DEFAULT_WIDTH = 1000;
	private static int DEFAULT_HEIGHT = 800;
	
	private MainPanel mainPanel;
	private BottomPanel bottomPanel;
	
	public MainFrame(String title) {
		
		DEFAULT_BOTTOM_PANEL = new BarPanel();
		DEFAULT_MAIN_PANEL = new GraphPanel(this);
		DEFAULT_CONTROLS_PANEL = new ControlsPanel(this);
		
		this.setTitle(title);
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println("RESIZED");
				resizeAll();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void resizeAll() {
		this.mainPanel.resize(new Dimension(this.getWidth(), this.getHeight()));
	}
	
	public void initializeGUI() {
		
		JPanel contentPane = new JPanel();
		
		BorderLayout layout = new BorderLayout();
		contentPane.setLayout(layout);
		
		this.mainPanel = new MainPanel();
		this.bottomPanel = new BottomPanel();
		
		contentPane.add(this.mainPanel, BorderLayout.CENTER);
		contentPane.add(this.bottomPanel, BorderLayout.SOUTH);
		
		this.setContentPane(contentPane);
		
		this.constructGUI(DEFAULT_MAIN_PANEL, DEFAULT_BOTTOM_PANEL, DEFAULT_CONTROLS_PANEL);
	}
	
	public void constructGUI(AbstractAppPanel main, AbstractBottomAppPanel bottom, AbstractAppPanel controls) {
		main.initComponents();
		this.mainPanel.configureAsMainPanel(main);
		bottom.initComponents();
		this.bottomPanel.configureAsBottomPanel(bottom);
		controls.initComponents();
		//this.rightPanel.configureAsRightPanel(right);
		this.setMainPanel(main);
		this.setBottomPanel(bottom, controls);
	}
	
	public void setMainPanel(AbstractAppPanel panel) {
		this.mainPanel.removeAll();
		this.mainPanel.addAppPanel(panel);
		this.validate();
	}
	
	public void setBottomPanel(AbstractBottomAppPanel panel, AbstractAppPanel controls) {
		this.bottomPanel.removeAll();
		this.bottomPanel.add(panel, BorderLayout.CENTER);
		this.add(controls, BorderLayout.NORTH);
		this.bottomGraphPanel = panel;
		this.validate();
	}
	
	public void updateAgePlot(Product product) {
		this.bottomGraphPanel.updateAgePlot(product);
	}

	
	public void updateUSMap(LocationRatingPair[] pair){
		this.bottomGraphPanel.updateMap(pair);
	}

}
