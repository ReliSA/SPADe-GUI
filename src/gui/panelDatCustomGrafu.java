package gui;

import java.awt.Color;
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

public class panelDatCustomGrafu extends JPanel {

	private JComboBox typGrafu = new JComboBox(Konstanty.TYPY_GRAFU);
	private JLabel nazev = new JLabel();
	private JLabel lblBarva = new JLabel(Konstanty.POPISY.getProperty("barvaGrafu"));
	private ColorPicker colorPicker = new ColorPicker();
	private JCheckBox pouzit = new JCheckBox(Konstanty.POPISY.getProperty("pouzitData"));

	public panelDatCustomGrafu(String nazev, int index) {
		this.setBorder(BorderFactory.createTitledBorder(""));
		colorPicker.setPreferredSize(Konstanty.VELIKOST_COLOR_PICKER);
		if (index >= 10) {
			index = 0;
		}
		colorPicker.setSelectedIndex(index);
		this.setBackground(Color.white);
		pouzit.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		this.nazev.setText(nazev);
		Font font = new Font("Courier", Font.BOLD, 12);
		this.nazev.setFont(font);
		this.add(this.nazev, gbc);
		this.add(pouzit, gbc);
		if (!nazev.equals("detected")) {
			this.add(lblBarva, gbc);
			this.add(typGrafu, gbc);
			this.add(new JLabel(), gbc);
			this.add(colorPicker, gbc);
			nastavAkce();
		}
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

	public boolean getPouzit() {
		return pouzit.isSelected();
	}
	
	public void setPouzit(boolean bool) {
		pouzit.setSelected(bool);
	}

	protected void nastavAkce() {

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
						((panelDatCustomGrafu)container.getComponent(i)).setPouzit(false);
					}

					container.setComponentZOrder(panel, 0);
					panel.setVisible(true);
					((panelDatCustomGrafu)panel).setPouzit(true);

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
