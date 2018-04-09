package data;

import java.awt.Color;
import java.io.Serializable;

/**
 * Třída pro uložení nastavení sloupce dat custom grafu
 */
public class nastaveniCustomSloupec implements Serializable{

	private static final long serialVersionUID = 5463862874537236741L;
	private boolean pouzit; // označuje zda se mají data z tohoto sloupce použít
	private int typGrafu; // typ grafu, který mají tyto data vykreslit
	private Color barva; // barva vykresleného grafu
		
	/**
	 * Kontruktor třídy
	 * @param pouzit Boolean zda se májí data použít v grafu
	 * @param typGrafu Typ grafu pro vykreslení
	 * @param barva Barva grafu pro vykreslení
	 */
	public nastaveniCustomSloupec(boolean pouzit, int typGrafu, Color barva) {
		super();
		this.pouzit = pouzit;
		this.typGrafu = typGrafu;
		this.barva = barva;
	}
	
	/**
	 * Vrací boolean zda se mají data použít
	 * @return boolean zda se mají data použít
	 */
	public boolean isPouzit() {
		return pouzit;
	}
	
	/**
	 * Nastavuje boolean zda se mají data použít
	 * @return boolean zda se mají data použít
	 */
	public void setPouzit(boolean pouzit) {
		this.pouzit = pouzit;
	}
	
	/**
	 * Vrací typ grafu
	 * @return typ grafu
	 */
	public int getTypGrafu() {
		return typGrafu;
	}
	
	/**
	 * Nastavuje typ grafu
	 * @param typGrafu typ grafu
	 */
	public void setTypGrafu(int typGrafu) {
		this.typGrafu = typGrafu;
	}
	
	/**
	 * Vrací barvu pro vykreslení grafu
	 * @return barva pro vykreslení grafu
	 */
	public Color getBarva() {
		return barva;
	}
	
	/**
	 * Nastavuje barvu pro vykreslení grafu
	 * @param barva pro vykreslení grafu
	 */
	public void setBarva(Color barva) {
		this.barva = barva;
	}
	
}
