package data.polozky;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Třída položek zděděná ze třídy Polozka, k původním atributům id a nazev
 * navíc přidává atribut datumVytvoreni.
 * Používá se pro položky, které mají datum vytvoření ale nemají datum počátku (artefakt, konfigurace).
 * @author michalvselko
 *
 */
public class PolozkaVytvoreni extends Polozka implements Serializable{
	
	private static final long serialVersionUID = 4294442297563556464L;
	private LocalDate datumVytvoreni;		//datum vytvoření položky
	
	/**
	 * Konstruktor třídy, nastaví atributy
	 * @param id id položky
	 * @param nazev název položky 
	 * @param datumVytvoreni datum vytvoření položky
	 */
	public PolozkaVytvoreni(int id, String nazev, LocalDate datumVytvoreni) {
		super(id, nazev);
		this.datumVytvoreni = datumVytvoreni;
	}
	
	/**
	 * Vrací datum vytvoření položky
	 * @return datum vytvoření položky
	 */
	public LocalDate getDatumVytvoreni(){
		return this.datumVytvoreni;
	}
	
	/**
	 * Nastaví datum vytvoření položky
	 * @param datumVytvoreni nové datum vytvoření položky
	 */
	public void setDatumVytvoreni(LocalDate datumVytvoreni){
		this.datumVytvoreni = datumVytvoreni;
	}


}
