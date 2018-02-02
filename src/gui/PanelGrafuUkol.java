package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ostatni.Konstanty;
import data.Projekt;
import data.Segment;
import data.Ukol;
import data.ciselnik.Ciselnik;
import data.ciselnik.Osoby;
import data.ciselnik.Priority;
import data.ciselnik.Resoluce;
import data.ciselnik.Severity;
import data.ciselnik.Status;
import data.ciselnik.Typ;

/**
 * Panel zobrazující grafy týkající se úkolů, zděděný ze třídy PanelGrafuRodic
 * @author michalvselko
 *
 */
public class PanelGrafuUkol extends PanelGrafuRodic{	
	
	private Priority priority = new Priority(projekt.getID());	//vytvoří číselník priorit
	private Severity severity = new Severity(projekt.getID());	//vytvoří číselník severit
	private Status statusy = new Status(projekt.getID());		//vytvoří číselník statusů
	private Typ typy = new Typ(projekt.getID());				//vytvoří číselník typů
	private Resoluce resoluce = new Resoluce(projekt.getID());	//vytvoří číselník resolucí
	private Osoby osoby = new Osoby(projekt.getID());			//vytvoří číselník osob

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelGrafuUkol(Projekt projekt){
		super(projekt); 
		this.vlozGrafy();
	}

	/**
	 * Vytvoří grafy, přidá do popupmenu checkbox, vloží je do panelu a spustí nastavení zobrazení
	 */
	protected void vlozGrafy(){
		this.setPreferredSize(Konstanty.VELIKOST_PANELU_UKOL);		
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("nazevUkoly")));
		this.removeAll();

		DropChartPanel panelGrafu1 = new DropChartPanel(this.getCiselnikGraf(this.priority, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);        
		DropChartPanel panelGrafu2 = new DropChartPanel(this.getCiselnikGraf(this.priority, Konstanty.TRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu3 = new DropChartPanel(this.getCiselnikGraf(this.priority, Konstanty.SUPERTRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
                
		DropChartPanel panelGrafu4 = new DropChartPanel(this.getCiselnikGraf(this.severity, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu5 = new DropChartPanel(this.getCiselnikGraf(this.severity, Konstanty.TRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu6 = new DropChartPanel(this.getCiselnikGraf(this.severity, Konstanty.SUPERTRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
        
		DropChartPanel panelGrafu7 = new DropChartPanel(this.getCiselnikGraf(this.statusy, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu8 = new DropChartPanel(this.getCiselnikGraf(this.statusy, Konstanty.TRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu9 = new DropChartPanel(this.getCiselnikGraf(this.statusy, Konstanty.SUPERTRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
        
        
		DropChartPanel panelGrafu10 = new DropChartPanel(this.getCiselnikGraf(this.resoluce, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu11 = new DropChartPanel(this.getCiselnikGraf(this.resoluce, Konstanty.TRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu12 = new DropChartPanel(this.getCiselnikGraf(this.resoluce, Konstanty.SUPERTRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
        
		DropChartPanel panelGrafu13 = new DropChartPanel(this.getCiselnikGraf(this.typy, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu14 = new DropChartPanel(this.getCiselnikGraf(this.typy, Konstanty.TRIDA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
		DropChartPanel panelGrafu15 = new DropChartPanel(this.getCiselnikGraf(this.osoby, Konstanty.HODNOTA, false),Konstanty.SLOUPCOVY ,Konstanty.UKOL);
        
        DropChartPanel panelGrafu16 = new DropChartPanel(this.getSegmentGraf(this.projekt.getFaze(), Konstanty.FAZE, false), Konstanty.SLOUPCOVY,Konstanty.UKOL);
        DropChartPanel panelGrafu17 = new DropChartPanel(this.getSegmentGraf(this.projekt.getIterace(), Konstanty.ITERACE, false), Konstanty.SLOUPCOVY,Konstanty.UKOL);
        DropChartPanel panelGrafu18 = new DropChartPanel(this.getHistogramCasy(),Konstanty.HISTOGRAM ,Konstanty.UKOL);
        DropChartPanel panelGrafu19 = new DropChartPanel(this.getSpojnicovyGrafPocet(this.projekt.getUkoly(), Konstanty.POPISY.getProperty("popisPocetUkolu")),Konstanty.SPOJNICOVY ,Konstanty.UKOL);
        DropChartPanel panelGrafu20 = new DropChartPanel(this.getAutorGraf(Konstanty.UKOL, false), Konstanty.SLOUPCOVY,Konstanty.UKOL);
        
        vlozCheckBoxBezNulCiselnik(panelGrafu1, this.priority, Konstanty.HODNOTA);
        vlozCheckBoxBezNulCiselnik(panelGrafu2, this.priority, Konstanty.TRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu3, this.priority, Konstanty.SUPERTRIDA );
        vlozCheckBoxBezNulCiselnik(panelGrafu4, this.severity, Konstanty.HODNOTA);
        vlozCheckBoxBezNulCiselnik(panelGrafu5, this.severity, Konstanty.TRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu6, this.severity, Konstanty.SUPERTRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu7, this.statusy, Konstanty.HODNOTA);
        vlozCheckBoxBezNulCiselnik(panelGrafu8, this.statusy, Konstanty.TRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu9, this.statusy, Konstanty.SUPERTRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu10, this.resoluce, Konstanty.HODNOTA);
        vlozCheckBoxBezNulCiselnik(panelGrafu11, this.resoluce, Konstanty.TRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu12, this.resoluce, Konstanty.SUPERTRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu13, this.typy, Konstanty.HODNOTA);
        vlozCheckBoxBezNulCiselnik(panelGrafu14, this.typy, Konstanty.TRIDA);
        vlozCheckBoxBezNulCiselnik(panelGrafu15, this.osoby, Konstanty.HODNOTA);
        vlozCheckBoxBezNulSegment(panelGrafu16, this.projekt.getFaze(), Konstanty.FAZE);
        vlozCheckBoxBezNulSegment(panelGrafu17, this.projekt.getIterace(), Konstanty.ITERACE);
        vlozCheckBoxBezNulAutor(panelGrafu20, Konstanty.UKOL);

        this.add(panelGrafu1);		
        this.add(panelGrafu2);
		this.add(panelGrafu3);
		this.add(panelGrafu4);
		this.add(panelGrafu5);
		this.add(panelGrafu6);
		this.add(panelGrafu7);
		this.add(panelGrafu8);
		this.add(panelGrafu9);
		this.add(panelGrafu10);
		this.add(panelGrafu11);
		this.add(panelGrafu12);		
        this.add(panelGrafu13);		
        this.add(panelGrafu14);
		this.add(panelGrafu15);
		this.add(panelGrafu16);
		this.add(panelGrafu17);
		this.add(panelGrafu18);		
        this.add(panelGrafu19);		
        this.add(panelGrafu20);		
        
		this.nastavZobrazeni();      
	}
	
	/**
	 * Vytvoří sloupcový graf z předaného číselník podle zadaných typů
	 * @param seznam číselník hodnot pro vytvoření grafu
	 * @param typGrafu typ grafu pro vytvoření (zda je to graf pro normální hodnotu, třídu nebo supertřídu)
	 * @param bezNul true, pokud se mají do datasetu vložit i nulové sloupce
	 * @return sloupcový graf zadaného číselníku
	 */
	private JFreeChart getCiselnikGraf(Ciselnik seznam, int typGrafu, boolean bezNul){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try{
			ArrayList<Ukol> ukoly = this.projekt.getUkoly();
			
			for(int i = 0; i < seznam.getPocetPolozek(); i++){
				int pocet = 0;
				for(int j = 0; j < ukoly.size(); j++){
					int id = -1;
					
					if(seznam instanceof Priority)
						id = ukoly.get(j).getPriorityID();
					else if(seznam instanceof Severity)
						id = ukoly.get(j).getSeverityID();
					else if(seznam instanceof Status)
						id = ukoly.get(j).getStatusID();
					else if(seznam instanceof Typ)
						id = ukoly.get(j).getTypID();
					else if(seznam instanceof Resoluce)
						id = ukoly.get(j).getResoluceID();
					else if(seznam instanceof Osoby){
						id = ukoly.get(j).getPrirazenID();
						typGrafu = Konstanty.HODNOTA;
					}
									
					if(seznam.getSeznam().get(i).getID() == id){
						pocet++;
					}
				}			
				
				String nazev = "";
				switch(typGrafu){
				case Konstanty.HODNOTA:	   	if(pocet != 0 || !bezNul)
												dataset.addValue(pocet, projekt.getNazev(), seznam.getSeznam().get(i).getNazev());
								  	  	   	break;
				case Konstanty.TRIDA:  	   	nazev = seznam.getSeznam().get(i).getTrida();
									  	   	break;	
				case Konstanty.SUPERTRIDA: 	nazev = seznam.getSeznam().get(i).getSuperTrida();
											break;
				}
				if(typGrafu == Konstanty.TRIDA || typGrafu == Konstanty.SUPERTRIDA){
					boolean nalezeno = false;
					for(int j = 0; j < dataset.getColumnCount(); j++){
						if(dataset.getColumnKeys().get(j).equals(nazev)){
							if(pocet != 0 || !bezNul)
								dataset.incrementValue((double)pocet, projekt.getNazev(), (Comparable<?>) dataset.getColumnKeys().get(j));
							nalezeno = true;
							break;
						}
					}
					if(!nalezeno)		
						if(pocet != 0 || !bezNul)
							dataset.addValue(pocet, projekt.getNazev(), nazev);
				}
				
								
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaUkolCiselnik"));
			e.printStackTrace();
		}
		
		String nazevGrafu = "";
		
		if(seznam instanceof Priority)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevPriority");
		else if(seznam instanceof Severity)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevSeverity");
		else if(seznam instanceof Status)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevStatusy");
		else if(seznam instanceof Typ)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevTypy");
		else if(seznam instanceof Resoluce)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevResoluce");
		else if(seznam instanceof Osoby){
			nazevGrafu = Konstanty.POPISY.getProperty("nazevPrirazeno");
			typGrafu = Konstanty.HODNOTA;
		}
		
		if(typGrafu == Konstanty.TRIDA)
			nazevGrafu += " - " + Konstanty.POPISY.getProperty("popisTridy");
		if(typGrafu == Konstanty.SUPERTRIDA)
			nazevGrafu += " - " + Konstanty.POPISY.getProperty("popisSupertridy");
				
		JFreeChart graf = ChartFactory.createBarChart(
		         nazevGrafu,
		         seznam.toString(),            
		         Konstanty.POPISY.getProperty("popisPocet"),            
		         dataset,          
		         PlotOrientation.VERTICAL,           
		         false, true, false); 

		this.nastavGraf(graf, Konstanty.SLOUPCOVY);	
		
		CategoryItemRenderer renderer = new RendererSloupcovyGraf(seznam, typGrafu); 
		graf.getCategoryPlot().setRenderer(renderer);
		
		return graf;

	}
	
	
	/**
	 * Vytvoří sloupcový graf s počtem úkolů za segment (fáze, iterace, aktivity) 
	 * @param seznam seznam segmentů s úkoly
	 * @param typGrafu typ segmentu
	 * @param bezNul true, pokud se mají do datasetu vložit i nulové sloupce
	 * @return sloupcový graf počtu úkolů na segment
	 */
	private JFreeChart getSegmentGraf(ArrayList<Segment> seznam, int typGrafu, boolean bezNul){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String nazev = "";
		try{
			for(int i = 0; i < seznam.size(); i++){
				if(seznam.get(i).getPocetUkolu() != 0 || !bezNul){
					int delka = Math.min(seznam.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);
					String delsiText = "";
					if(delka < seznam.get(i).getNazev().length())
						delsiText = "...";
					dataset.addValue(seznam.get(i).getPocetUkolu(), projekt.getNazev(), seznam.get(i).getNazev().substring(0, delka) + delsiText);
				}
			}
		
			switch (typGrafu){
			case Konstanty.FAZE:	nazev = Konstanty.POPISY.getProperty("nazevFaze");
									break;
			case Konstanty.ITERACE:	nazev = Konstanty.POPISY.getProperty("nazevIterace");
									break;
			case Konstanty.AKTIVITY:nazev = Konstanty.POPISY.getProperty("nazevAktivity");
									break;
			default:break;
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaUkolSegment"));
			e.printStackTrace();
		}
		JFreeChart graf = ChartFactory.createBarChart(
		         nazev,           
		         nazev,            
		         Konstanty.POPISY.getProperty("popisPocet"),            
		         dataset,          
		         PlotOrientation.VERTICAL,           
		         false, true, false); 

		this.nastavGraf(graf, Konstanty.SLOUPCOVY);
		
		CategoryItemRenderer renderer = new RendererSloupcovyGraf(null, -1); 
		graf.getCategoryPlot().setRenderer(renderer);
		
		return graf;

	}
	
	/**
	 * Vytvoří histogram předpokládané a skutečné doby úkolů podle datumů vytvoření
	 * @return histogram předpokládané a skutečné doby úkolů podle datumů vytvoření
	 */
	private JFreeChart getHistogramCasy(){
		TimeSeriesCollection dataset1 = null;
		TimeSeriesCollection dataset2 = null;
		
		try{
			ArrayList<Ukol> ukoly = this.projekt.getUkoly();
			
			ukoly.sort((o1, o2) -> o1.getDatumVytvoreni().compareTo(o2.getDatumVytvoreni()));
			ArrayList<Month> mesice = new ArrayList<Month>();
			ArrayList<Double> sumPredpoklCas = new ArrayList<Double>();
			ArrayList<Double> sumStravenyCas = new ArrayList<Double>();
			
			for(int i = 0; i < ukoly.size(); i++){
				boolean nalezeno = false;
				LocalDate datum = ukoly.get(i).getDatumVytvoreni();
				Month mesic = new Month(datum.getMonthValue(), datum.getYear());
				double predpoklCas = ukoly.get(i).getPredpokladanyCas();
				double stravenyCas = ukoly.get(i).getStravenyCas();
				for(int j = 0; j < mesice.size(); j++){
					if(mesic.equals(mesice.get(j))){
						predpoklCas += sumPredpoklCas.get(j);
						stravenyCas += sumStravenyCas.get(j);
						sumPredpoklCas.set(j, predpoklCas);
						sumStravenyCas.set(j, stravenyCas);
						
						nalezeno = true;
						break;
					}
				}
				if(!nalezeno  && datum.isAfter(Konstanty.DATUM_PRAZDNY)){
					mesice.add(mesic);
					sumPredpoklCas.add(predpoklCas);
					sumStravenyCas.add(stravenyCas);
					
				}
			}
			TimeSeries serie1 = new TimeSeries(Konstanty.POPISY.getProperty("popisStravenyCas"));
			TimeSeries serie2 = new TimeSeries(Konstanty.POPISY.getProperty("popisCasovyOdhad"));
			TimeSeries serie3 = new TimeSeries(Konstanty.POPISY.getProperty("popisPrumer"));
			double pomerCelkem = 0;
			for(int i = 0; i < mesice.size(); i++){
				serie1.add( mesice.get(i), (double)sumStravenyCas.get(i) * 100 / sumPredpoklCas.get(i) );
				serie2.add( mesice.get(i), 100.0 );
				pomerCelkem += (double)sumStravenyCas.get(i) * 100 / sumPredpoklCas.get(i);
			}
			double prumer = pomerCelkem / mesice.size();
			for(int i = 0; i < mesice.size(); i++)
				serie3.add(mesice.get(i), prumer);
			
			dataset1 = new TimeSeriesCollection(serie1);
			dataset1.addSeries(serie2);
			dataset2 = new TimeSeriesCollection(serie3);
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaHistogramCasy"));
			e.printStackTrace();
		}		
		JFreeChart graf = ChartFactory.createHistogram(
				Konstanty.POPISY.getProperty("nazevGrafuCasy"),
				Konstanty.POPISY.getProperty("popisDatum"),            
				Konstanty.POPISY.getProperty("popisPocet"),            
		         dataset1,          
		         PlotOrientation.VERTICAL,           
		         true, true, true); 
        
        XYPlot plot = graf.getXYPlot();        
        plot.setDataset(1, dataset2);
        final XYItemRenderer xyLineRenderer = new XYLineAndShapeRenderer(true, false);
        xyLineRenderer.setSeriesStroke(0, new BasicStroke(4));
        
        plot.setRenderer(1, xyLineRenderer);        
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);        
    	
        nastavGraf(graf, Konstanty.HISTOGRAM);	
        
        return graf;
	}

	
	/**
	 * Vloží checkbox pro rozhodnutí zda zobrazit nulové sloupce do panelu grafu číselníku
	 * @param chartPanel panel grafu, do kterého se má checkbox vložit
	 * @param seznam parametr pro znovu načtení grafu, číselník hodnot pro vytvoření grafu
	 * @param typGrafu parametr pro znovu načtení grafu, typ grafu pro vytvoření (zda je to graf pro normální hodnotu, třídu nebo supertřídu)
	 */
	private void vlozCheckBoxBezNulCiselnik(ChartPanel chartPanel, Ciselnik seznam, int typGrafu){
		JCheckBox ckBezNul = new JCheckBox("Bez nulobých hodnot");
			ckBezNul.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(ckBezNul.isSelected()){
						chartPanel.setChart(getCiselnikGraf(seznam, typGrafu, true));
						chartPanel.repaint();						
					}
					else{
						chartPanel.setChart(getCiselnikGraf(seznam, typGrafu, false));
						chartPanel.repaint();						
					}
				}
			});
	        chartPanel.getPopupMenu().add(ckBezNul);	    
	}

	/**
	 * Vloží checkbox pro rozhodnutí zda zobrazit nulové sloupce do panelu grafu segmetů
	 * @param chartPanel panel grafu, do kterého se má checkbox vložit
	 * @param seznam parametr pro znovu načtení grafu, seznam segmentů s úkoly
	 * @param typGrafu parametr pro znovu načtení grafu, typ segmentu
	 */
	private void vlozCheckBoxBezNulSegment(ChartPanel chartPanel, ArrayList<Segment> seznam, int typGrafu){
		JCheckBox ckBezNul = new JCheckBox("Bez nulobých hodnot");
			ckBezNul.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(ckBezNul.isSelected()){
						chartPanel.setChart(getSegmentGraf(seznam, typGrafu, true));
						chartPanel.repaint();						
					}
					else{
						chartPanel.setChart(getSegmentGraf(seznam, typGrafu, false));
						chartPanel.repaint();						
					}
				}
			});
	        chartPanel.getPopupMenu().add(ckBezNul);	    
	}



}
