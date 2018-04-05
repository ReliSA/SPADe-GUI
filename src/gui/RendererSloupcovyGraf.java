package gui;

import java.awt.Color; 
import java.awt.Paint;
import java.util.Arrays;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DatasetUtilities;
import ostatni.Konstanty;
import data.ciselnik.Ciselnik;
import data.ciselnik.Osoby;
import data.ciselnik.Typ;

/**
 * Render pro barevné zobrazení sloupcových grafů
 */
public class RendererSloupcovyGraf extends BarRenderer {
	
	private static final long serialVersionUID = -3672703362374480604L;
	private Ciselnik seznam;		//seznam položek číselníku
	private int typGrafu;			// typ grafu (hodnota, třída, supertřída)
	private Color[] barvy = {new Color(145, 0, 145), new Color(0, 150, 0), new Color(160, 160, 0), new Color(150, 0, 0), new Color(0, 0, 150)};	//pole barev
	
	/**
	 * Konstruktor renderu
	 * @param seznam seznam položek číselníku
	 * @param typGrafu typ grafu (hodnota, třída, supertřída)
	 */
	public RendererSloupcovyGraf(Ciselnik seznam, int typGrafu) { 
		this.seznam = seznam;
		this.typGrafu = typGrafu; 
	}
	
	/**
	 * Předefinovaná metoda getItemPaint, vrací barvu sloupce
	 */
	public Paint getItemPaint(final int row, final int column){
		if(seznam != null){
			String nazevSloupce = getPlot().getDataset().getColumnKey(column).toString();
			String superTrida = "";
			
			switch(typGrafu){
			case Konstanty.HODNOTA: 	superTrida = seznam.getSuperTrida(nazevSloupce, false);
										break;
			case Konstanty.TRIDA:		superTrida = seznam.getSuperTrida(nazevSloupce, true);
										break;
			case Konstanty.SUPERTRIDA:	superTrida = nazevSloupce;
										break;
			default:break;					
			}		
			if(Arrays.asList(Konstanty.NIZKASUPERTRIDA).contains(superTrida))
				return barvy[1];
			if(Arrays.asList(Konstanty.STREDNISUPERTRIDA).contains(superTrida))
				return barvy[2];
			if(Arrays.asList(Konstanty.VYSOKASUPERTRIDA).contains(superTrida))
				return barvy[3];
			
			if(seznam instanceof Typ){				//obezlička kvůli číselníku Typ, jelikož nemá supertřídu, rozlišuje se pouze podle třídy
				if(typGrafu == Konstanty.TRIDA)
					return barvy[seznam.getIdTridy(nazevSloupce, true) % barvy.length];
				else
					return barvy[seznam.getIdTridy(nazevSloupce, false) % barvy.length];
			}
		}
		
		if(seznam == null || seznam instanceof Osoby ){	//pro grafy osob, nemají třídu ani supertřídu
			double maximum = (double)DatasetUtilities.findMaximumRangeValue(getPlot().getDataset());
			double hodnota = (double)getPlot().getDataset().getValue(row, column);
			if(hodnota >= (maximum / 3) * 2)
				return barvy[3];
			else if(hodnota >= maximum / 3)
				return barvy[2];
			else 
				return barvy[1];
		}
		
		return Color.BLUE; 
	 } 
}
