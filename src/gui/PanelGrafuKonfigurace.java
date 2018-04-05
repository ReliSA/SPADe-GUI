package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import ostatni.Konstanty;
import data.Konfigurace;
import data.Projekt;

/**
 * Panel zobrazující grafy týkající se konfigurací, zděděná ze třídy PanelGrafuRodic
 */
public class PanelGrafuKonfigurace extends PanelGrafuRodic{

	private static final long serialVersionUID = -7192735188487952687L;

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelGrafuKonfigurace(Projekt projekt){
		super(projekt);
		this.vlozGrafy();
	}

	/**
	 * Vytvoří grafy, vloží je do panelu a spustí nastavení zobrazení
	 */
	protected void vlozGrafy(){
		this.setPreferredSize(Konstanty.VELIKOST_PANELU_STANDARD);		
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("nazevKonfigurace")));
		DropChartPanel panelGrafu1 = new DropChartPanel(this.getHistogramKonfigurace(),Konstanty.HISTOGRAM ,Konstanty.KONFIGURACE);
		DropChartPanel panelGrafu2 = new DropChartPanel(this.getSpojnicovyGrafPocet(this.projekt.getKonfigurace(), Konstanty.POPISY.getProperty("konfiguraceNadpisSpojnicovy")),Konstanty.SPOJNICOVY ,Konstanty.KONFIGURACE);
		DropChartPanel panelGrafu3 = new DropChartPanel(this.getAutorGraf(Konstanty.KONFIGURACE, false),Konstanty.SLOUPCOVY,Konstanty.KONFIGURACE);
		
        panelGrafu1.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        panelGrafu2.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        panelGrafu3.setPreferredSize(Konstanty.VELIKOST_GRAFU);
        
        vlozCheckBoxBezNulAutor(panelGrafu3, Konstanty.KONFIGURACE);

 		this.add(panelGrafu1);
		this.add(panelGrafu2);
		this.add(panelGrafu3);
        
		this.odstranMenu();
		this.nastavZobrazeni();
	}

	/**
	 * Vytvoří histogram počtu konfigurací v závislosti na datu vytvoření
	 * @return histogram počtu konfigurací podle data vytvoření
	 */
	private JFreeChart getHistogramKonfigurace(){
		TimeSeriesCollection dataset = null;
		try{
			ArrayList<Konfigurace> konfigurace = this.projekt.getKonfigurace();
			
			konfigurace.sort((o1, o2) -> o1.getDatumVytvoreni().compareTo(o2.getDatumVytvoreni()));
			ArrayList<Month> mesice = new ArrayList<Month>();
			ArrayList<Integer> pocet = new ArrayList<Integer>();
			
			for(int i = 0; i < konfigurace.size(); i++){
				boolean nalezeno = false;
				LocalDate datum = konfigurace.get(i).getDatumVytvoreni();
				Month mesic = new Month(datum.getMonthValue(), datum.getYear());				
				for(int j = 0; j < mesice.size(); j++){
					if(mesic.equals(mesice.get(j))){
						int pomocPocet = pocet.get(j) + 1;
						pocet.set(j, pomocPocet);
						
						nalezeno = true;
					}
				}
				if(!nalezeno && datum.isAfter(Konstanty.DATUM_PRAZDNY)){
					mesice.add(mesic);
					pocet.add(1);				
				}
			}
			TimeSeries serie1 = new TimeSeries(Konstanty.POPISY.getProperty("popisPocetKonfiguraci"));
			for(int i = 0; i < mesice.size(); i++){
				serie1.add( mesice.get(i), (double)pocet.get(i) );
			}
			dataset = new TimeSeriesCollection(serie1);
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaHistogramKonfiguraci"));
			e.printStackTrace();
		}	
		JFreeChart graf = ChartFactory.createHistogram(
				Konstanty.POPISY.getProperty("konfiguraceNadpisHistogram"),
				Konstanty.POPISY.getProperty("popisDatum"),            
				Konstanty.POPISY.getProperty("popisPocetKonfiguraci"),            
		         dataset,          
		         PlotOrientation.VERTICAL,           
		         true, true, true); 
		nastavGraf(graf, Konstanty.HISTOGRAM);
		
		return graf;
	}

}
