package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import data.custom.SablonaCustomGrafu;
import ostatni.Konstanty;

/**
 * Tlačítko pro otevření editace uloženého custom grafu
 */
public class TlacitkoEditaceGrafu extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -1349817940520879175L;
	private SablonaCustomGrafu sablona;	// Sablona pro vytvoření grafu
	
	/**
	 * Kontruktor třídy
	 * @param sablona sablona pro vykreslení grafu
	 */
	public TlacitkoEditaceGrafu(SablonaCustomGrafu sablona) {
		super();
		this.sablona=sablona;
		this.setText(Konstanty.POPISY.getProperty("edituj"));	
		this.addActionListener(this);
	}
 
	/**
	 * Akce pro stisknutí tlačítka editace grafu
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(() -> {		
			OknoCustomGraf example = new OknoCustomGraf(sablona,sablona.getProjekt());
			example.setLocationRelativeTo(null);
			example.setVisible(true);
		});

	}
}
