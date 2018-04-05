package gui;

import javax.swing.BorderFactory;
import ostatni.Konstanty;
import ostatni.Ukladani;
import data.Projekt;

/**
 * Panel zobrazující grafy týkající se artefaktů, zděděná ze třídy
 * PanelGrafuRodic
 */
public class PanelGrafuCustom extends PanelGrafuRodic {

	Projekt projekt;

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * 
	 * @param projekt
	 *            aktuálně vybraný projekt
	 */
	public PanelGrafuCustom(Projekt projekt) {
		super(projekt);
		this.projekt = projekt;
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("nazevArtefakty")));
	}

	protected void vlozGrafy() {

		Ukladani.nactiGrafy(projekt.getID());
		odstranMenu();
		nastavZobrazeni();
	}

	/**
	 * Vytvoří grafy, vloží je do panelu a spustí nastavení zobrazení
	 */
	public void vlozGraf(DropChartPanel panel) {

		panel.setDragable(true);
		panel.setDomainZoomable(false);
		panel.setRangeZoomable(false);
		panel.zobrazLegendu(false);
		panel.setPreferredSize(Konstanty.VELIKOST_GRAFU);
		panel.setPopupMenu(null);
		this.add(panel);
		this.revalidate();
		this.repaint();
	}

}
