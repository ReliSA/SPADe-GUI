package data.custom;

import java.io.Serializable;
import org.jfree.chart.JFreeChart;

/**
 * Třída sloužící pro uložení custom grafu
 */
public class prepravkaUkladaniCustom implements Serializable{
	
	private static final long serialVersionUID = 4881143472479215994L;
	
	private JFreeChart panel; // panel grafu
	private String Nazev; // název grafu
	private int projectID; // id projektu, kterému graf patří
	private int typGrafu; // typ grafu
	private sablonaCustomGrafu nastaveni;
	
	/**
	 * Konstuktor třídy
	 * @param panel
	 * @param projectID
	 * @param typGrafu
	 * @param Nazev
	 */
	public prepravkaUkladaniCustom(JFreeChart panel, int projectID, int typGrafu,String Nazev, sablonaCustomGrafu nastaveni) {
		this.panel = panel;
		this.Nazev=Nazev;
		this.projectID = projectID;
		this.typGrafu = typGrafu;
		this.nastaveni=nastaveni;
	}
	
	public sablonaCustomGrafu getNastaveni() {
		return nastaveni;
	}

	public void setNastaveni(sablonaCustomGrafu nastaveni) {
		this.nastaveni = nastaveni;
	}

	/**
	 * Vrací typ grafu
	 * @return typ grafu
	 */
	public int getTypGrafu() {
		return typGrafu;
	}
 
	/**
	 * Nastaví typ grafu
	 * @param typGrafu
	 */
	public void setTypGrafu(int typGrafu) {
		this.typGrafu = typGrafu;
	}
	
	/**
	 * Vrací název grafu
	 * @return název grafu
	 */
	public String getNazev() {
		return Nazev;
	}

	/**
	 * Nastaví název grafu
	 * @param nazev název grafu
	 */
	public void setNazev(String nazev) {
		Nazev = nazev;
	}

	/**
	 * Vrací panel grafu
	 * @return panel grafu
	 */
	public JFreeChart getPanel() {
		return panel;
	}
	 /**
	  * Nastaví panel grafu
	  * @param panel panel s grafem
	  */
	public void setPanel(JFreeChart panel) {
		this.panel = panel;
	}
	
	/**
	 * Vrací ID projektu
	 * @return ID projektu
	 */
	public int getProjectID() {
		return projectID;
	}
	
	/**
	 * Nastavé ID projektu
	 * @param projectID ID projektu
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

}
