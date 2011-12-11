package edu.gatech.cs7450.prodviz.events;

import java.awt.event.ActionEvent;

public class BaseEvent extends ActionEvent {

	private static final long serialVersionUID = 1L;

	public BaseEvent(ActionEvent e) {
		super(e.getSource(), e.getID(), e.getActionCommand());
	}
}
