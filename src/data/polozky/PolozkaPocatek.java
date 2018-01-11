package data.polozky;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Třída položek zděděná ze třídy PolozkaVytvoreni, k původním atributům id, název a 
 * datumVytvoreni přidává navíc atribut datumPocatku
 * Používá se pro položky, které obsahují datum vytvoření i počátku (Ukol, Segment)
 * @author michalvselko
 *
 */
public class PolozkaPocatek extends PolozkaVytvoreni implements Serializable{

	private static final long serialVersionUID = -445890088181258869L;
	private LocalDate datumPocatku;		//datum počátku položky
	
	/**
	 * Konstruktor třídy, nastaví atributy
	 * @param id id položky
	 * @param nazev název položky
	 * @param datumVytvoreni datum vytvoření položky
	 * @param datumPocatku datum počátku položky
	 */
	public PolozkaPocatek(int id, String nazev, LocalDate datumVytvoreni, LocalDate datumPocatku){
		super(id, nazev, datumVytvoreni);
		this.datumPocatku = datumPocatku;
	}
	
	/**
	 * Vrací datum počátku položky
	 * @return datum počátku položky
	 */
	public LocalDate getDatumPocatku(){
		return this.datumPocatku;
	}
	
	/**
	 * Nastaví datum počátku položky
	 * @param datumPocatku datum počátku položky
	 */
	public void setDatumPocatku(LocalDate datumPocatku){
		this.datumPocatku = datumPocatku;
	}

}
