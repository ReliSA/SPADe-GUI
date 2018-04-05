package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ostatni.Konstanty;
import ostatni.Ukladani;
import data.Artefakt;
import data.Projekt;

/**
 * Panel zobrazující grafy týkající se artefaktů, zděděná ze třídy
 * PanelGrafuRodic
 * 
 * @author michalvselko
 *
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
		//setsize();
	}
/*
	private void setsize() {

		int sirkaPanelu = this.getWidth();

		if (this.getWidth() == 0) {
			sirkaPanelu = 1656;
		}

		int pocet = this.getComponentCount();
		double radkaDouble = sirkaPanelu / 280;
		int radka = (int) Math.floor(radkaDouble);
		int radek = (int) Math.ceil(pocet / (double) radka);

		System.out.println(radek);
		int vyska = 210 * (radek);
		this.setPreferredSize(new Dimension(0, vyska));

	}*/

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
		//setsize();
		this.revalidate();
		this.repaint();
	}

}
