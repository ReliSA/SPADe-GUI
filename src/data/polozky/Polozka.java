package data.polozky;

import java.io.Serializable;

/**
 * Základní třída položek dat obsahující id a název
 */
public class Polozka implements Serializable{

	private static final long serialVersionUID = -6053794199760884507L;
	private int id;			//id položky
	private String nazev;	//název položky
	
	/**
	 * Konstruktor třídy, nastaví atributy
	 * @param id id položky
	 * @param nazev název položky
	 */
	public Polozka(int id, String nazev){
		this.id = id;
		this.nazev = nazev;
	}
	
	/**
	 * Vrací id položky
	 * @return id položky
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Vrací název položky
	 * @return název položky
	 */
	public String getNazev(){
		return this.nazev;
	}
	
	/**
	 * Vrací název položky
	 * @return název položky
	 */
	public String toString(){
		return this.nazev;
	}
}
