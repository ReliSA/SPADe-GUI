package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import data.sablonaCustomGrafu;

public class tlacitkoEditaceGrafu extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -1349817940520879175L;
	sablonaCustomGrafu sablona;

	public tlacitkoEditaceGrafu(sablonaCustomGrafu sablona) {
		super();
		this.sablona=sablona;
		this.setText("Edituj graf");	
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(() -> {
			OknoCustomGraf example = new OknoCustomGraf(sablona);
			example.setLocationRelativeTo(null);
			example.setVisible(true);
		});

	}
}
