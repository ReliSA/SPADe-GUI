package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;

public class TlacitkoMazaniFiltru extends JButton {
	
	private Component comp;
	
	public Component getComp() {
		return comp;
	}

	public TlacitkoMazaniFiltru(Component comp) {
		super();
		this.comp = comp;
		this.setMargin(new Insets(1, 1, 1, 1));
		//this.setPreferredSize(new Dimension(70, 30));
	}

}
