package gui;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import ostatni.Konstanty;
import data.Projekt;
import data.Segment;

/**
 * Panel zobrazující grafy týkající se segmentů (fází, iterací a aktivit), zděděná ze třídy PanelGrafuRodic
 */
public class PanelGrafuSegment extends PanelGrafuRodic{
	
	private static final long serialVersionUID = 6478867166531575827L;

	/**
	 * Konstruktor třídy, nastaví projekt a spustí vložení grafů
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelGrafuSegment(Projekt projekt){
		super(projekt);
		this.vlozGrafy();
	}

	/**
	 * Vytvoří grafy, vloží je do panelu a spustí nastavení zobrazení
	 */
	protected void vlozGrafy(){
		this.setPreferredSize(Konstanty.VELIKOST_PANELU_STANDARD);		
		
		this.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleSegmenty")));
		DropChartPanel panelGrafu1 = new DropChartPanel(this.getGanttGraf(Konstanty.FAZE),Konstanty.GANTT ,Konstanty.SEGMENT);
		DropChartPanel panelGrafu2 = new DropChartPanel(this.getGanttGraf(Konstanty.ITERACE),Konstanty.GANTT ,Konstanty.SEGMENT);
		DropChartPanel panelGrafu3 = new DropChartPanel(this.getGanttGraf(Konstanty.AKTIVITY),Konstanty.GANTT ,Konstanty.SEGMENT);
		       
 		this.add(panelGrafu1);
 		this.add(panelGrafu2);
 		this.add(panelGrafu3);
        
 		this.odstranMenu();
		this.nastavZobrazeni();
	}
		
	/**
	 * Vytvoří ganttův diagram pro fáze, iterace nebo aktivity podle zadaného parametru
	 * @param typGrafu typ grafu, který je požadován
	 * @return gantt graf pro fáze, iterace nebo aktivity
	 */
	private JFreeChart getGanttGraf(int typGrafu){
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		String nazevOsy = "";
		try{
			TaskSeries serie1 = new TaskSeries(projekt.getNazev());
			ArrayList<Segment> segmenty = new ArrayList<Segment>();
			switch(typGrafu){
			case Konstanty.FAZE: 	 segmenty = projekt.getFaze();
								 	 nazevOsy = Konstanty.POPISY.getProperty("nazevFaze");
									 break;
			case Konstanty.ITERACE:	 segmenty = projekt.getIterace();
									 nazevOsy = Konstanty.POPISY.getProperty("nazevIterace");
									 break;
			case Konstanty.AKTIVITY: segmenty = projekt.getAktivity();
			 						 nazevOsy = Konstanty.POPISY.getProperty("nazevAktivity");
									 break;
			default:break;
			}
			
			for(int i = 0; i < segmenty.size(); i++){
				Segment segment = segmenty.get(i);
				String delsiText = "";
				int delka = Math.min(segmenty.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);											
				if(delka < segmenty.get(i).getNazev().length())
					delsiText = "...";
				Task task = new Task(segment.getNazev().substring(0, delka) + delsiText,  
									Date.from(segment.getDatumPocatku().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) , 
									Date.from(segment.getDatumKonce().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())); 
				serie1.add(task);			
			}
			dataset.add(serie1);
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaGanttSegmentu"));
			e.printStackTrace();
		}        
        JFreeChart graf = ChartFactory.createGanttChart(
        		Konstanty.POPISY.getProperty("nazevGanttSegmentu") + " - " + nazevOsy,
                nazevOsy,              
                Konstanty.POPISY.getProperty("popisDatum"),              
                dataset,             
                false,               
                true,                
                false                
            ); 
        

        this.nastavGraf(graf, Konstanty.GANTT);
        return graf;
	}
}
