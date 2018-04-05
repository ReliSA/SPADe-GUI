package gui;

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
		this.projekt=projekt;
		this.setPreferredSize(Konstanty.VELIKOST_PANELU_STANDARD);
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
