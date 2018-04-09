package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import data.sablonaCustomGrafu;
import ostatni.Konstanty;

public class tlacitkoEditaceGrafu extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -1349817940520879175L;
	sablonaCustomGrafu sablona;	
	public tlacitkoEditaceGrafu(sablonaCustomGrafu sablona) {
		super();
		this.sablona=sablona;
		this.setText(Konstanty.POPISY.getProperty("edituj"));	
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(() -> {		
			OknoCustomGraf example = new OknoCustomGraf(sablona,sablona.getProjekt());
			example.setLocationRelativeTo(null);
			example.setVisible(true);
		});

	}
}
