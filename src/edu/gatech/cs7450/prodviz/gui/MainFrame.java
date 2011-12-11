package edu.gatech.cs7450.prodviz.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.gatech.cs7450.prodviz.data.Product;
import edu.gatech.cs7450.prodviz.gui.panels.BarPanel;
import edu.gatech.cs7450.prodviz.gui.panels.ControlsPanel;
import edu.gatech.cs7450.prodviz.gui.panels.GraphPanel;

public class MainFrame extends AppFrame {

	private static final long serialVersionUID = 1L;
	
	//public static int MAXIMUM_SIZE = 10000;
	
	private AbstractAppPanel DEFAULT_MAIN_PANEL;
	private AbstractBottomAppPanel DEFAULT_BOTTOM_PANEL = new BarPanel();
	private AbstractAppPanel DEFAULT_RIGHT_PANEL = new ControlsPanel();
	
	private AbstractBottomAppPanel bottomGraphPanel;
	
	private static int DEFAULT_WIDTH = 800;
	private static int DEFAULT_HEIGHT = 600;
	
	private MainPanel mainPanel;
	private BottomPanel bottomPanel;
	private RightPanel rightPanel;
	
	public MainFrame(String title) {
		
		DEFAULT_MAIN_PANEL = new GraphPanel(this);
		
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
	
	private void resizeAll() {
		this.mainPanel.resize(new Dimension(this.getWidth(), this.getHeight()));
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
	
	public void constructGUI(AbstractAppPanel main, AbstractBottomAppPanel bottom, AbstractAppPanel right) {
		main.initComponents();
		this.mainPanel.configureAsMainPanel(main);
		bottom.initComponents();
		this.bottomPanel.configureAsBottomPanel(bottom);
		right.initComponents();
		this.rightPanel.configureAsRightPanel(right);
		this.setMainPanel(main);
		this.setBottomPanel(bottom);
		this.setRightPanel(right);
	}
	
	public void setMainPanel(AbstractAppPanel panel) {
		this.mainPanel.removeAll();
		this.mainPanel.addAppPanel(panel);
		this.invalidate();
	}
	
	public void setBottomPanel(AbstractBottomAppPanel panel) {
		this.bottomPanel.removeAll();
		this.bottomPanel.add(panel);
		this.bottomGraphPanel = panel;
		this.invalidate();
	}
	
	public void setRightPanel(AbstractAppPanel panel) {
		this.rightPanel.removeAll();
		this.rightPanel.add(panel);
		this.invalidate();
	}
	
	public void updateAgePlot(Product product) {
		this.bottomGraphPanel.updateAgePlot(product);
	}
}
