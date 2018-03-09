package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
 * 
 * @author michalvselko
 *
 */
public abstract class PanelFiltr extends JPanel {

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
	private DefaultComboBoxModel getModel(ArrayList seznam) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0; i < seznam.size(); i++)
			model.addElement(seznam.get(i));
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
		switch (nazev) {
		case "Tasks":
		case "Úkoly":
			titulek = Konstanty.POPISY.getProperty("nazevUkoly");
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Priorities":
		case "Priority":
			titulek = Konstanty.POPISY.getProperty("nazevPriority");
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Severity":
			titulek = Konstanty.POPISY.getProperty("nazevSeverity");
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Status":
		case "Statusy":
			titulek = Konstanty.POPISY.getProperty("nazevStatusy");
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Types":
		case "Typy":
			titulek = Konstanty.POPISY.getProperty("nazevTypy");
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Resolution":
		case "Rezoluce":
			titulek = Konstanty.POPISY.getProperty("nazevResoluce");	
			this.setBackground(Konstanty.barvaUkol);
			break;
		case "Persons":
		case "Osoby":
			titulek = Konstanty.POPISY.getProperty("nazevOsoby");
			this.setBackground(Konstanty.barvaOsoby);
			break;
		case "Phase":
		case "Fáze":
			titulek = Konstanty.POPISY.getProperty("nazevFaze");
			this.setBackground(Konstanty.barvaFaze);
			break;
		case "Iterations":
		case "Iterace":
			titulek = Konstanty.POPISY.getProperty("nazevIterace");
			this.setBackground(Konstanty.barvaIterace);
			break;
		case "Activities":
		case "Aktivity":
			titulek = Konstanty.POPISY.getProperty("nazevAktivity");
			this.setBackground(Konstanty.barvaAktivity);
			break;
		case "Configurations":
		case "Konfigurace":
			titulek = Konstanty.POPISY.getProperty("nazevKonfigurace");
			this.setBackground(Konstanty.barvaKonfigurace);
			break;
		case "Artifacts":
		case "Artefakty":
			titulek = Konstanty.POPISY.getProperty("nazevArtefakty");
			this.setBackground(Konstanty.barvaArtefakty);
			break;
		default:
			break;
		}
		this.setBorder(BorderFactory.createTitledBorder(titulek));

		lsSeznamFiltr = new JList(getModel(this.seznam));
		lsSeznamFiltr.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lsSeznamFiltr.setVisibleRowCount(Math.min(lsSeznamFiltr.getModel().getSize(), 3));
		JScrollPane scScrollPane = new JScrollPane(lsSeznamFiltr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scScrollPane.setPreferredSize(Konstanty.VELIKOST_SEZNAMU_CELA_SIRKA);
		JLabel lblNazev = new JLabel(nazev+": ");

		nastavAkce();

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 30,0));
		this.add(ckPouzitFiltr);
		this.add(lblNazev);
		this.add(scScrollPane);
		this.setName(nazev);
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
