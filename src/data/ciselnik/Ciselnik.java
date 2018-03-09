package data.ciselnik;

import java.io.Serializable;
import java.util.ArrayList;

import ostatni.Konstanty;
import data.polozky.Polozka;
import data.polozky.PolozkaCiselnik;

/**
 * Abstraktní třída pro naplnění číselníků (priority, severity, statusů, resolucí, typů a osob)
 * @author michalvselko
 *
 */
public abstract class Ciselnik implements Serializable{

	private static final long serialVersionUID = 1219725890839447209L;
	private ArrayList<PolozkaCiselnik> seznam = new ArrayList<PolozkaCiselnik>();	//seznam položek číselníku
	private int typ;																//typ číselníku
	
	/**
	 * Konstruktor číselníku
	 * @param typ typ číselníku
	 * @param idProjekt id projektu
	 */
	public Ciselnik(int typ, int idProjekt){
		this.typ = typ;
		this.nactiPolozky(idProjekt);	//spustí načtení položek
	}

	/**
	 * Abstraktní metoda, kterou si implementují jednotlivé třídy číselníku
	 * @param idProjekt identifikátor vybraného projektu
	 */
	public abstract void nactiPolozky(int idProjekt);	
	
	/**
	 * Vrací typ číselníku
	 * @return typ číselníku
	 */
	public int getTyp(){
		return this.typ;
	}
	
	/**
	 * Vrací seznam položek číselníku
	 * @return seznam položek číselníku
	 */
	public ArrayList<PolozkaCiselnik> getSeznam(){
		return this.seznam;
	}
	
	/**
	 * Vrací počet položek seznamu číselníku
	 * @return počet položek seznamu číselníku
	 */
	public int getPocetPolozek(){
		return this.seznam.size();
	}
	
	/**
	 * Podle zadaného id položky v seznamu vrátí její název
	 * @param id id položky v seznamu
	 * @return název položky
	 */
	public String getNazev(int id){
		String nazev = "";
		for(int i = 0; i < seznam.size(); i++){		//prochází všechny položky seznamu
			Polozka polozka = seznam.get(i);
			if(polozka.getID() == id){				//porovnává id
				nazev = polozka.getNazev();
				break;
			}
		}
		return nazev;
	}

	/**
	 * Podle zadaného názvu položky nebo třídy nalezne položku a vrátí id třídy
	 * @param nazev název položky nebo její třídy pro nalezení
	 * @param podleTridy true, pokud se má hledat podle názvu třídy, false podle názvu položky
	 * @return identifikátor třídy
	 */
	public int getIdTridy(String nazev, boolean podleTridy){
		int idTridy = 0;
		for(int i = 0; i < seznam.size(); i++){						//prochází všechny položky seznamu
			PolozkaCiselnik polozka = seznam.get(i);
			if(podleTridy){
				if(polozka.getTrida().equals(nazev)){
						idTridy = polozka.getIdTrida();
						break;
				}			
			} else {
				if(polozka.getNazev().equals(nazev)){
					idTridy = polozka.getIdTrida();
					break;
				}			
			}
		}
		return idTridy;
	}
	
	/**
	 * Podle zadaného názvu položky nebo třídy nalezne položku a vrátí název supertřídy
	 * @param nazev název položky nebo její třídy pro nalezení
	 * @param podleTridy true, pokud se má hledat podle názvu třídy, false podle názvu položky
	 * @return název supertřídy
	 */
	public String getSuperTrida(String nazev, boolean podleTridy){
		String superTrida = "";
		for(int i = 0; i < seznam.size(); i++){						//prochází všechny položky seznamu
			PolozkaCiselnik polozka = seznam.get(i);
			if(podleTridy){
				if(polozka.getTrida().equals(nazev)){
					superTrida = polozka.getSuperTrida();
					break;
				}	
			} else {
				if(polozka.getNazev().equals(nazev)){				
					superTrida = polozka.getSuperTrida();
					break;
				}
			}
		}
		return superTrida;
	}


	/**
	 * Nastaví typ seznamu
	 * @param typ nový typ seznamu
	 */
	public void setTyp(int typ){
		this.typ = typ;
	}
	
	/**
	 * Nastaví seznam položek číselníku
	 * @param seznam nový seznam číselníku
	 */
	public void setSeznam(ArrayList<PolozkaCiselnik> seznam){
		this.seznam = seznam;
	}
	
	/**
	 * Vloží položku do seznamu položek číselníku
	 * @param polozka nová položka pro vložení
	 */
	public void addPolozkaCiselniku(PolozkaCiselnik polozka){
		this.seznam.add(polozka);
	}
	
	/**
	 * Podle uloženého typu číselní vrátí jeho název
	 */
	public String toString(){
		switch(this.typ){
			case Konstanty.PRIORITY:return Konstanty.POPISY.getProperty("nazevPriority");
			case Konstanty.SEVERITY:return Konstanty.POPISY.getProperty("nazevSeverity");
			case Konstanty.STATUS: 	return Konstanty.POPISY.getProperty("nazevStatusy");
			case Konstanty.TYP: 	return Konstanty.POPISY.getProperty("nazevTypy");
			case Konstanty.RESOLUCE:return Konstanty.POPISY.getProperty("nazevResoluce");
			case Konstanty.PRIRAZEN:return Konstanty.POPISY.getProperty("nazevOsoby");
			default:return "Nedefinovaný typ";						
		}
	}
			
}
