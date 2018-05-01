package gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import ostatni.Konstanty;

/**
 * Abstraktní třída, počáteční panel filtrů ze kterého ostatní panely filtru
 * dědí, nastavuje checkbox Použít filtr a seznam výběru daného typu
 */
public abstract class PanelFiltr extends JPanel {

	private static final long serialVersionUID = 6333285605158443235L;
	protected ArrayList<?> seznam; // seznam možného výběru
	protected JCheckBox ckPouzitFiltr = new JCheckBox(Konstanty.POPISY.getProperty("checkBoxPouzitFiltr"), true); // zaškrtávátko
																													// Použít
																													// filtr
	protected JList<?> lsSeznamFiltr; // zobrazený seznam
	private GridBagConstraints grid = new GridBagConstraints(); // grid pro nastavení pozic komponent

	/**
	 * Konstruktor třídy, nastaví seznam výběru
	 * 
	 * @param seznam
	 *            seznam výběru
	 */
	public PanelFiltr(ArrayList seznam) {
		this.seznam = seznam;
	}

	/**
	 * Vrací seznam vybraných položek
	 * 
	 * @return seznam vybraných položek
	 */
	public List getSeznamVybranych() {
		return this.lsSeznamFiltr.getSelectedValuesList();
	}

	/**
	 * Vrací grid pro nastavení pozic komponent
	 * 
	 * @return grid komponent
	 */
	public GridBagConstraints getGrid() {
		return this.grid;
	}

	/**
	 * Vrací model ze seznamu z parametru
	 * 
	 * @param seznam
	 *            seznam, ze kterého se vytvoří model
	 * @return vytvořený model
	 */
	protected DefaultComboBoxModel getModel(ArrayList seznam) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0; i < seznam.size(); i++) {
			if (seznam.get(i).toString() != null) {
				model.addElement(seznam.get(i));
			}
		}
		return model;
	}

	/**
	 * Zjistí zda je zaškrtnuto Použít filtr
	 * 
	 * @return true, pokud je Použít filtr zaškrtnuto
	 */
	public boolean jePouzit() {
		return this.ckPouzitFiltr.isSelected();
	}

	/**
	 * Nastaví panel filtrů
	 * 
	 * @param nazev
	 *            název panelu filtrů
	 */
	protected void nastavPanel(String nazev) {
		String titulek = "";
		ckPouzitFiltr.setOpaque(false);

		if (nazev.equals(Konstanty.POPISY.getProperty("nazevUkoly"))) {
			titulek = Konstanty.POPISY.getProperty("nazevUkoly");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevPriority"))) {
			titulek = Konstanty.POPISY.getProperty("nazevPriority");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevSeverity"))) {
			titulek = Konstanty.POPISY.getProperty("nazevSeverity");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevStatusy"))) {
			titulek = Konstanty.POPISY.getProperty("nazevStatusy");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevTypy"))) {
			titulek = Konstanty.POPISY.getProperty("nazevTypy");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevResoluce"))) {
			titulek = Konstanty.POPISY.getProperty("nazevResoluce");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("cas"))) {
			titulek = Konstanty.POPISY.getProperty("cas");
			this.setBackground(Konstanty.barvaUkol);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevOsoby"))) {
			titulek = Konstanty.POPISY.getProperty("nazevOsoby");
			this.setBackground(Konstanty.barvaOsoby);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevFaze"))) {
			titulek = Konstanty.POPISY.getProperty("nazevFaze");
			this.setBackground(Konstanty.barvaFaze);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevIterace"))) {
			titulek = Konstanty.POPISY.getProperty("nazevIterace");
			this.setBackground(Konstanty.barvaIterace);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevAktivity"))) {
			titulek = Konstanty.POPISY.getProperty("nazevAktivity");
			this.setBackground(Konstanty.barvaAktivity);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevKonfigurace"))) {
			titulek = Konstanty.POPISY.getProperty("nazevKonfigurace");
			this.setBackground(Konstanty.barvaKonfigurace);
		} else if (nazev.equals(Konstanty.POPISY.getProperty("nazevArtefakty"))) {
			titulek = Konstanty.POPISY.getProperty("nazevArtefakty");
			this.setBackground(Konstanty.barvaArtefakty);
		}

		this.setBorder(BorderFactory.createTitledBorder(titulek));

		lsSeznamFiltr = new JList(getModel(this.seznam));
		lsSeznamFiltr.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lsSeznamFiltr.setVisibleRowCount(Math.min(lsSeznamFiltr.getModel().getSize(), 3));
		JScrollPane scScrollPane = new JScrollPane(lsSeznamFiltr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scScrollPane.setPreferredSize(Konstanty.VELIKOST_SEZNAMU_CELA_SIRKA);
		JLabel lblNazev = new JLabel(nazev + ": ");

		nastavAkce();

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
		this.add(ckPouzitFiltr);
		this.add(lblNazev);
		this.add(scScrollPane);
		this.setName(nazev);
	}

	protected void nactiNovyProjekt(ArrayList seznam) {
		this.seznam = seznam;
		lsSeznamFiltr.setModel(getModel(seznam));
	}

	/**
	 * Abstraktní třída pro nastavení akcí u jednotlivých komponent
	 */
	protected abstract void nastavAkce();

	/**
	 * Abstraktní třída pro vrácení seznamu id odpovídajících zadaným podmínkám
	 * filtru
	 * 
	 * @return seznam povolených id
	 */
	public abstract ArrayList<Integer> getSeznamId();

}
