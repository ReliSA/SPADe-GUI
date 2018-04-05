package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ostatni.Konstanty;

/**
 * Panel pro zobrazení načtených dat pro custom graf
 */
public class panelDatCustomGrafu extends JPanel {

	private static final long serialVersionUID = 4184829276809100638L;
	private Color[] colors = { Color.red, Color.blue, Color.green, Color.BLACK, Color.ORANGE, Color.YELLOW, Color.PINK, // pole předvybraných barev
			Color.magenta, Color.CYAN, Color.lightGray };
	private JComboBox typGrafu = new JComboBox(Konstanty.TYPY_GRAFU); // combobox pro výběr typu grafu
	private JLabel nazev = new JLabel(); // popisek název
	private JLabel lblBarva = new JLabel(Konstanty.POPISY.getProperty("barvaGrafu"));// popisek barva
	private JLabel lblTyp = new JLabel(Konstanty.POPISY.getProperty("typGrafu")); // popisek typ grafu
	private JCheckBox pouzit = new JCheckBox(Konstanty.POPISY.getProperty("pouzitData")); // checkbox pro výběr zda použít data tohoto panelu pro graf
	private ColorChooserButton colorPicker; // colorchooser pro výběr barvy grafu


	/**
	 * Konstruktor panelu
	 * @param nazev název dat
	 * @param index udává pozici, na které je tento panel zobrazen
	 */
	public panelDatCustomGrafu(String nazev, int index) {
		this.setBorder(BorderFactory.createTitledBorder(""));
		if (index >= 10) {
			index = 0;
		}
		colorPicker = new ColorChooserButton(colors[index]);
		this.setBackground(Color.white);
		pouzit.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		this.nazev.setText(nazev);
		this.nazev.setPreferredSize(new Dimension(100, 40));
		Font font = new Font("Courier", Font.BOLD, 12);
		this.nazev.setFont(font);
		this.add(this.nazev, gbc);
		this.add(pouzit, gbc);
		if (!nazev.equals("detected")) {
			this.add(lblTyp, gbc);
			this.add(typGrafu, gbc);
			this.add(lblBarva, gbc);
			this.add(colorPicker, gbc);
			nastavAkce();
		}
	}

	/**
	 * Vrací zvolený typ grafu
	 * @return typ grafu
	 */
	public int getTyp() {
		return this.typGrafu.getSelectedIndex();
	}

	/**
	 * Vrací název dat
	 * @return název dat
	 */
	public String getNazev() {
		return this.nazev.getText();
	}

	/**
	 * Vrací zvolenou barvu pro graf
	 * @return barva pro graf
	 */
	public Color getColor() {
		return colorPicker.getSelectedColor();
	}

	/**
	 * Vrací boolean zda se mají tyto data použít
	 * @return true/false
	 */
	public boolean getPouzit() {
		return pouzit.isSelected();
	}

	/**
	 * Nastaví zda se mají tyto data použít
	 * @param bool true/false
	 */
	public void setPouzit(boolean bool) {
		pouzit.setSelected(bool);
	}

	/**
	 * Nastaví akce komponentám panelu
	 */
	protected void nastavAkce() {

		//Akce pro combobox typGrafu
		ActionListener actZmenaOperatoru = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JComboBox cbBox = (JComboBox) e.getSource();
				JPanel panel = (JPanel) cbBox.getParent();
				JPanel container = (JPanel) panel.getParent();

				if (typGrafu.getSelectedIndex() == 4) {
					lblBarva.setVisible(false);
					colorPicker.setVisible(false);

					for (int i = 0; i < container.getComponentCount(); i++) {
						container.getComponent(i).setVisible(false);
						((panelDatCustomGrafu) container.getComponent(i)).setPouzit(false);
					}

					container.setComponentZOrder(panel, 0);
					panel.setVisible(true);
					((panelDatCustomGrafu) panel).setPouzit(true);

					container.revalidate();
					container.repaint();

				} else {
					lblBarva.setVisible(true);
					colorPicker.setVisible(true);
					for (int i = 0; i < container.getComponentCount(); i++) {
						container.getComponent(i).setVisible(true);
					}
				}
			}
		};

		typGrafu.addActionListener(actZmenaOperatoru);
	}

}
