package data.custom;

import java.io.Serializable;
import java.util.ArrayList;

import data.Projekt;

/**
 * Třída sloužící pro uložení nastavení vykreslení custom grafu
 */
public class SablonaCustomGrafu implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nazev; // název grafu
	private Projekt projekt; // projekt
	private int sql = -1; // sql pro vykreslení
	private int osoby = -1; // vybrané osoby pro vykreslení
	private int iterace = -1; // vybrané iterace pro vykreslení
	private ArrayList<NastaveniCustomSloupec> sloupce;


	/**
	 * Konstruktor třídy
	 * @param nazev název grafu
	 * @param projekt projekt grafu
	 * @param sql pro vykreslení grafu
	 * @param osoby pro vykreslení grafu
	 * @param iterace pro vykreslení grafu
	 * @param sloupce nastavení sloupců dat pro vykreslení grafu
	 */
	private SablonaCustomGrafu(String nazev, Projekt projekt, int sql, int osoby, int iterace,
			ArrayList<NastaveniCustomSloupec> sloupce) {
		super();
		this.nazev = nazev;
		this.projekt = projekt;
		this.sql = sql;
		this.osoby = osoby;
		this.iterace = iterace;
		this.sloupce = sloupce;
	}

	/**
	 * Vrací instanci sablonaCustomGrafu s uloženým nastavením SQL, názvu, projektu a sloupců
	 * @param nazev název grafu
	 * @param projekt projekt grafu
	 * @param sql pro vykreslení grafu
	 * @param sloupce nastavení sloupců dat pro vykreslení grafu
	 * @return instance sablonaCustomGrafu
	 */
	public static SablonaCustomGrafu sablonaCustomGrafuNic(String nazev, Projekt projekt, int sql, ArrayList<NastaveniCustomSloupec> sloupce) {
		return new SablonaCustomGrafu(nazev, projekt, sql, -1, -1, sloupce);
	}
	
	/**
	 * Vrací instanci sablonaCustomGrafu s uloženým nastavením SQL, názvu, projektu, sloupců a iterací
	 * @param nazev název grafu
	 * @param projekt projekt grafu
	 * @param sql pro vykreslení grafu
	 * @param iterace pro vykreslení grafu
	 * @param sloupce nastavení sloupců dat pro vykreslení grafu
	 * @return instance sablonaCustomGrafu
	 */
	public static SablonaCustomGrafu sablonaCustomGrafuIterace(String nazev, Projekt projekt, int sql, int iterace,
			ArrayList<NastaveniCustomSloupec> sloupce) {
		return new SablonaCustomGrafu(nazev, projekt, sql, -1, iterace, sloupce);
	}

	/**
	 * Vrací instanci sablonaCustomGrafu s uloženým nastavením SQL, názvu, projektu, sloupců a osob
	 * @param nazev název grafu
	 * @param projekt projekt grafu
	 * @param sql pro vykreslení grafu
	 * @param sloupce nastavení sloupců dat pro vykreslení grafu
	 * @param osoby pro vykreslení grafu
	 * @return instance sablonaCustomGrafu
	 */
	public static SablonaCustomGrafu sablonaCustomGrafuOsoba(String nazev, Projekt projekt, int sql, int osoby, ArrayList<NastaveniCustomSloupec> sloupce) {
		return new SablonaCustomGrafu(nazev, projekt,sql, osoby, -1, sloupce);
	}
	
	/**
	 * Vrací instanci sablonaCustomGrafu s uloženým nastavením SQL, názvu, projektu, sloupců, iterací a osob
	 * @param nazev název grafu
	 * @param projekt projekt grafu
	 * @param sql pro vykreslení grafu
	 * @param sloupce nastavení sloupců dat pro vykreslení grafu
	 * @param osoby pro vykreslení grafu
	 * @param iterace pro vykreslení grafu
	 * @return instance sablonaCustomGrafu
	 */
	public static SablonaCustomGrafu sablonaCustomGrafuVse(String nazev, Projekt projekt,int sql, int osoby,int iterace, ArrayList<NastaveniCustomSloupec> sloupce) {
		return new SablonaCustomGrafu(nazev, projekt, sql, osoby, iterace, sloupce);
	}

	/**
	 * Vrací název grafu
	 * @return název grafu
	 */
	public String getNazev() {
		return nazev;
	}

	/**
	 * Nastavuje název grafu
	 * @param nazev název grafu
	 */
	public void setNazev(String nazev) {
		this.nazev = nazev;
	}

	/**
	 * Vrací projekt grafu
	 * @return projekt grafu
	 */
	public Projekt getProjekt() {
		return projekt;
	}

	/**
	 * Nastavuje projekt grafu
	 * @param projekt projekt grafu
	 */
	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	/**
	 * Vrací SQL použité pro vykreslení grafu
	 * @return SQL použité pro vykreslení grafu
	 */
	public int getSql() {
		return sql;
	}

	/**
	 * Nastavuje SQL použité pro vykreslení grafu
	 * @param sql SQL použité pro vykreslení grafu
	 */
	public void setSql(int sql) {
		this.sql = sql;
	}

	/**
	 * Vrací osobu použitou pro vykreslení grafu
	 * @return osoba použitá pro vykreslení grafu
	 */
	public int getOsoby() {
		return osoby;
	}

	/**
	 * Nastavuje osobu použitou pro vykreslení grafu
	 * @param osoby osoba použitá pro vykreslení grafu
	 */
	public void setOsoby(int osoby) {
		this.osoby = osoby;
	}

	/**
	 * Vrací iteraci použitou pro vykreslení grafu
	 * @return iterace použitá pro vykreslení grafu
	 */
	public int getIterace() {
		return iterace;
	}

	/**
	 * Nastavuje iteraci použitou pro vykreslení grafu
	 * @param iterace iteraca použitá pro vykreslení grafu
	 */
	public void setIterace(int iterace) {
		this.iterace = iterace;
	}

	/**
	 * Vrací nastavení sloupců dat použíté pro vykreslení grafu (barva,typ,pouziti)
	 * @return nastavení vykreslení sloupců dat
	 */ 
	public ArrayList<NastaveniCustomSloupec> getSloupce() {
		return sloupce;
	}

	/**
	 * Nastavuje nastavení sloupců dat použíté pro vykreslení grafu (barva,typ,pouziti)
	 * @param sloupce nastavení vykreslení sloupců dat
	 */
	public void setSloupce(ArrayList<NastaveniCustomSloupec> sloupce) {
		this.sloupce = sloupce;
	}

}
