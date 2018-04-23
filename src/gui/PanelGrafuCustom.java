package gui;

import javax.swing.BorderFactory;
import ostatni.Konstanty;
import ostatni.Ukladani;
import data.Projekt;

/**
 * Panel zobrazující custom grafy, zděděná ze třídy PanelGrafuRodic
 */
public class PanelGrafuCustom extends PanelGrafuRodic {

	private static final long serialVersionUID = -2917129457265732546L;
	Projekt projekt; //aktuálně vybraný projekt

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * 
	 * @param projekt
	 *            aktuálně vybraný projekt
	 */
	public PanelGrafuCustom(Projekt projekt) {
		super(projekt);
		this.projekt = projekt;
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("nazevCustom")));
	}

	/**
	 * Načte grafy do panelu
	 */
	protected void vlozGrafy() {

		Ukladani.nactiGrafy(projekt.getID());
		odstranMenu();
		nastavZobrazeni();
	}

	/**
	 * Vloží grafy do panelu a nastaví zobrazení
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
