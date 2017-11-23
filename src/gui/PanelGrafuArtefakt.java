package gui;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ostatni.Konstanty;
import data.Artefakt;
import data.Projekt;

/**
 * Panel zobrazující grafy týkající se artefaktů, zděděná ze třídy PanelGrafuRodic
 * @author michalvselko
 *
 */
public class PanelGrafuArtefakt extends PanelGrafuRodic {

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelGrafuArtefakt(Projekt projekt){
		super(projekt);
		this.vlozGrafy();
	}

	/**
	 * Vytvoří grafy, vloží je do panelu a spustí nastavení zobrazení
	 */
	protected void vlozGrafy(){
		this.setPreferredSize(Konstanty.VELIKOST_PANELU_STANDARD);		
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("nazevArtefakty")));
		
		ChartPanel panelGrafu1 = new ChartPanel(this.getHistogramArtefakty() );
		ChartPanel panelGrafu2 = new ChartPanel(this.getSpojnicovyGrafPocet(this.projekt.getArtefakty(), Konstanty.POPISY.getProperty("artefaktNadpisSpojnicovy")) );
		ChartPanel panelGrafu3 = new ChartPanel(this.getAutorGraf(Konstanty.ARTEFAKT, false) );

		vlozCheckBoxBezNulAutor(panelGrafu3, Konstanty.ARTEFAKT);

        panelGrafu1.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        panelGrafu2.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        panelGrafu3.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        
 		this.add(panelGrafu1);
 		this.add(panelGrafu2);
 		this.add(panelGrafu3);
            
		this.nastavZobrazeni();
	}

	/**
	 * Vytvoří histogram počtu artefaktů v závislosti na datu vytvoření
	 * @return histogram počtu artefaktů podle data vytvoření
	 */
	private JFreeChart getHistogramArtefakty(){
		TimeSeriesCollection dataset = null;
		try{
			ArrayList<Artefakt> artefakt = this.projekt.getArtefakty();
			
			artefakt.sort((o1, o2) -> o1.getDatumVytvoreni().compareTo(o2.getDatumVytvoreni()));
			ArrayList<Month> mesice = new ArrayList<Month>();
			ArrayList<Integer> pocet = new ArrayList<Integer>();
			
			for(int i = 0; i < artefakt.size(); i++){
				boolean nalezeno = false;
				LocalDate datum = artefakt.get(i).getDatumVytvoreni();
				Month mesic = new Month(datum.getMonthValue(), datum.getYear());				
				for(int j = 0; j < mesice.size(); j++){
					if(mesic.equals(mesice.get(j))){
						int pomocPocet = pocet.get(j) + 1;
						pocet.set(j, pomocPocet);
						
						nalezeno = true;
					}
				}
				if(!nalezeno  && datum.isAfter(Konstanty.DATUM_PRAZDNY)){
					mesice.add(mesic);
					pocet.add(1);				
				}
			}
			
			TimeSeries serie1 = new TimeSeries(Konstanty.POPISY.getProperty("popisPocetArtefaktu"));
			for(int i = 0; i < mesice.size(); i++){
				serie1.add( mesice.get(i), (double)pocet.get(i) );
			}
			dataset = new TimeSeriesCollection(serie1);
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaHistogramArtefakt"));
			e.printStackTrace();
		}		
		
		JFreeChart graf = ChartFactory.createHistogram(
				Konstanty.POPISY.getProperty("artefaktNadpisHistogram"),
				Konstanty.POPISY.getProperty("popisDatum"),            
				Konstanty.POPISY.getProperty("popisPocetArtefaktu"),            
		         dataset,          
		         PlotOrientation.VERTICAL,           
		         true, true, true); 

        nastavGraf(graf, Konstanty.HISTOGRAM);
		
		return graf;
	}

}
