package gui;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JButton;

public class TlacitkoMazaniFiltru extends JButton {
	
	private Component comp;
	
	public Component getComp() {
		return comp;
	}

	public TlacitkoMazaniFiltru(Component comp) {
		this.comp = comp;
		this.setMargin(new Insets(1, 1, 1, 1));
	}

}
