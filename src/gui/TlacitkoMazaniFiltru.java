package gui;

import java.awt.Component;
import javax.swing.JButton;
import ostatni.Konstanty;

/**
 * Tlačítko pro mázání filtrů grafů. Uchovává odkaz na panel filtru, který má odstranit
 */
public class TlacitkoMazaniFiltru extends JButton {
	
	private static final long serialVersionUID = -8567560808117527374L;
	private Component comp; // odkaz na panel, který má po stisku odstranit
	
	/**
	 * Vrací odkaz na panel filtru
	 * @return panel filtru
	 */
	public Component getComp() {
		return comp;
	}

	/**
	 * Konstruktor tlačítka
	 * @param comp odkaz na panel, který má po stisku odstranit
	 */
	public TlacitkoMazaniFiltru(Component comp) {
		super();
		this.setFont(Konstanty.FONT_TLACITKA);
		this.comp = comp;
	}

}
