package edu.gatech.cs7450.prodviz.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.gatech.cs7450.prodviz.ApplicationContext;
import edu.gatech.cs7450.prodviz.events.BaseEvent;

public class ApplicationListener implements ActionListener {
	
	private ApplicationContext context;
	
	public ApplicationListener(ApplicationContext context) {
		this.context = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println("ActionEvent caught with no associated command");
	}
	
	
}
