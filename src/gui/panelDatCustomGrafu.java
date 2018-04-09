package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

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
	 * 
	 * @param nazev
	 *            název dat
	 * @param index
	 *            udává pozici, na které je tento panel zobrazen
	 */
	public panelDatCustomGrafu(String nazev, ArrayList<Double> data, int index) {
		if (index >= 10) {
			index = 0;
		}
		colorPicker = new ColorChooserButton(colors[index]);
		this.setBackground(Color.white);
		pouzit.setBackground(Color.white);
		this.setLayout(new GridLayout(0, 1));
		this.nazev.setText(nazev);
		Font font = new Font("Courier", Font.BOLD, 12);
		this.nazev.setFont(font);
		this.nazev.setHorizontalAlignment(SwingConstants.CENTER);			
		if (!nazev.equals("detected")) {
			this.add(pouzit);
			 this.add(typGrafu);
			 this.add(colorPicker);
			 this.add(this.nazev);
			nastavAkce();
		}
		else {
			this.add(new JLabel());
			this.add(pouzit);
			this.add(new JLabel());
			this.add(this.nazev);
		}
		this.nazev.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
		typGrafu.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
		pouzit.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
		colorPicker.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
		
		JLabel lblHodnota;
		for (Double hodnota : data) {
			lblHodnota=new JLabel(hodnota.toString());
			lblHodnota.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
			lblHodnota.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(lblHodnota);
		} 
	}
	
	/**
	 * Konstruktor panelu pro první sloupec s časovou osou
	 * 
	 * @param nazev název dat
	 * @param data datumy k zobrazení
	 */
	public panelDatCustomGrafu(String nazev, ArrayList<String> data) {
		
		this.setBackground(Color.white);
		pouzit.setBackground(Color.white);
		
		this.setLayout(new GridLayout(0, 1));
		this.nazev.setText(nazev);
		Font font = new Font("Courier", Font.BOLD, 12);
		this.nazev.setFont(font);
		this.nazev.setHorizontalAlignment(SwingConstants.CENTER);
						
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.add(this.nazev);
		
		this.nazev.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
		
		JLabel lblHodnota;
		for (String hodnota : data) {
			lblHodnota=new JLabel(hodnota);
			lblHodnota.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
			lblHodnota.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(lblHodnota);
		} 
	}

	
	/**
	 * Nastaví listenery pro zobrazování náhledu grafu
	 * @param listener
	 */
	public void nastavListeneryPreview(ActionListener listener) {
		pouzit.addActionListener(listener);
		typGrafu.addActionListener(listener);
		colorPicker.addColorChangedListener(listener);
	}
	
	/**
	 * Vrací zvolený typ grafu
	 * 
	 * @return typ grafu
	 */
	public int getTyp() {
		System.out.println(this.typGrafu.getSelectedIndex());
		return this.typGrafu.getSelectedIndex();
	}

	/**
	 * Vrací název dat
	 * 
	 * @return název dat
	 */
	public String getNazev() {
		return this.nazev.getText();
	}

	/**
	 * Vrací zvolenou barvu pro graf
	 * 
	 * @return barva pro graf
	 */
	public Color getColor() {
		return colorPicker.getSelectedColor();
	}

	/**
	 * Vrací boolean zda se mají tyto data použít
	 * 
	 * @return true/false
	 */
	public boolean getPouzit() {
		return pouzit.isSelected();
	}

	/**
	 * Nastaví zda se mají tyto data použít
	 * 
	 * @param bool
	 *            true/false
	 */
	public void setPouzit(boolean bool) {
		pouzit.setSelected(bool);
	}

	/**
	 * Nastaví akce komponentám panelu
	 */
	protected void nastavAkce() {

		// Akce pro combobox typGrafu
		ActionListener actZmenaOperatoru = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JComboBox cbBox = (JComboBox) e.getSource();
				JPanel panel = (JPanel) cbBox.getParent();
				JPanel container = (JPanel) panel.getParent();

				if (typGrafu.getSelectedIndex() == 4) {
					lblBarva.setVisible(false);
					colorPicker.setVisible(false);

					for (int i = 1; i < container.getComponentCount(); i++) {

						container.getComponent(i).setVisible(false);
						((panelDatCustomGrafu) container.getComponent(i)).setPouzit(false);
					}

					container.setComponentZOrder(panel, 1);
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
