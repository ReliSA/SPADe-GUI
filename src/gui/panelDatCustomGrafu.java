package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ostatni.Konstanty;

public class panelDatCustomGrafu extends JPanel{
	
	private JComboBox typGrafu = new JComboBox(Konstanty.TYPY_GRAFU);
	private JLabel nazev = new JLabel();
	private ColorPicker colorPicker = new ColorPicker();
	private JCheckBox pouzit = new JCheckBox(Konstanty.POPISY.getProperty("pouzitData"));
		
	public panelDatCustomGrafu(String nazev,int index) {
		this.setBorder(BorderFactory.createTitledBorder(""));
		colorPicker.setPreferredSize(Konstanty.VELIKOST_COLOR_PICKER);
		colorPicker.setSelectedIndex(index);
		this.setBackground(Color.white);
		pouzit.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		this.nazev.setText(nazev);
		Font font = new Font("Courier", Font.BOLD,12);
		this.nazev.setFont(font);
		this.add(this.nazev,gbc);
		this.add(pouzit,gbc);
		this.add(new JLabel(Konstanty.POPISY.getProperty("typGrafu")),gbc);
		this.add(typGrafu,gbc);	
		this.add(new JLabel(Konstanty.POPISY.getProperty("barvaGrafu")),gbc);
		this.add(colorPicker,gbc);
	}

	public int getTyp() {
		return this.typGrafu.getSelectedIndex();
	}
	
	public String getNazev() {
		return this.nazev.getText();
	}
	
	public Color getColor() {
		return colorPicker.getColor();
	}
	
	public boolean pouzit() {
		return pouzit.isSelected();
	}
	
}
