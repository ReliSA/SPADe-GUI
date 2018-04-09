package data;

import java.io.Serializable;
import java.util.ArrayList;

public class sablonaCustomGrafu implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nazev;
	private Projekt projekt;
	private int sql = -1;
	private int osoby = -1;
	private int iterace = -1;
	private ArrayList<nastaveniCustomSloupec> sloupce;


	public sablonaCustomGrafu(String nazev, Projekt projekt, int sql, int osoby, int iterace,
			ArrayList<nastaveniCustomSloupec> sloupce) {
		super();
		this.nazev = nazev;
		this.projekt = projekt;
		this.sql = sql;
		this.osoby = osoby;
		this.iterace = iterace;
		this.sloupce = sloupce;
	}

	public static sablonaCustomGrafu sablonaCustomGrafuNic(String nazev, Projekt projekt, int sql, ArrayList<nastaveniCustomSloupec> sloupce) {
		return new sablonaCustomGrafu(nazev, projekt, sql, -1, -1, sloupce);
	}

	public static sablonaCustomGrafu sablonaCustomGrafuIterace(String nazev, Projekt projekt, int sql, int iterace,
			ArrayList<nastaveniCustomSloupec> sloupce) {
		return new sablonaCustomGrafu(nazev, projekt, sql, -1, iterace, sloupce);
	}

	public static sablonaCustomGrafu sablonaCustomGrafuOsoba(String nazev, Projekt projekt, int sql, int osoby, ArrayList<nastaveniCustomSloupec> sloupce) {
		return new sablonaCustomGrafu(nazev, projekt,sql, osoby, -1, sloupce);
	}
	
	public static sablonaCustomGrafu sablonaCustomGrafuVse(String nazev, Projekt projekt,int sql, int osoby,int iterace, ArrayList<nastaveniCustomSloupec> sloupce) {
		return new sablonaCustomGrafu(nazev, projekt, sql, osoby, iterace, sloupce);
	}

	public String getNazev() {
		return nazev;
	}

	public void setNazev(String nazev) {
		this.nazev = nazev;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public int getSql() {
		return sql;
	}

	public void setSql(int sql) {
		this.sql = sql;
	}

	public int getOsoby() {
		return osoby;
	}

	public void setOsoby(int osoby) {
		this.osoby = osoby;
	}

	public int getIterace() {
		return iterace;
	}

	public void setIterace(int iterace) {
		this.iterace = iterace;
	}

	public ArrayList<nastaveniCustomSloupec> getSloupce() {
		return sloupce;
	}

	public void setSloupce(ArrayList<nastaveniCustomSloupec> sloupce) {
		this.sloupce = sloupce;
	}

}
