package data;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import data.polozky.PolozkaVytvoreni;
import databaze.IUkolDAO;
import databaze.UkolDAO;
import ostatni.Konstanty;

/**
 * Datová třída artefaktů zděděná ze třídy PolozkaVytvoreni
 * @author michalvselko
 *
 */
public class Artefakt extends PolozkaVytvoreni{

	private String typ;										//typ artefaktu - sloupec mimeType tabulky Artifact
	private int velikost;									//velikost artefaktu - sloupec size tabulky Artifact
	private ArrayList<Ukol> ukoly = new ArrayList<Ukol>();	//seznam úkolů obsahující daný artefakt
	
	/**
	 * Konstruktor třídy artefakt - nastaví proměnné dle atributů a spustí metodu nactiData()
	 * @param id id artefaktu
	 * @param nazev nazev artefaktu - polozka artifactClass
	 * @param datumVytvoreni datum vytvoření artefaktu (položky work_unitu) - datum v tabulce work_item
	 * @param typ typ artefaktu - polozka mimeType
	 * @param velikost velikost artefaktu - polozka size
	 */
	public Artefakt(int id, String nazev, LocalDate datumVytvoreni, String typ, int velikost) {
		super(id, nazev, datumVytvoreni);
		this.typ = typ;
		this.velikost = velikost;
		nactiData();
	}
	
	/**
	 * Vrací typ artefaktu (mimeType)
	 * @return typ artefaktu
	 */
	public String getTyp(){
		return this.typ;
	}
	
	/**
	 * Vrací velikost artefaktu (size)
	 * @return velikost artefaktu
	 */
	public int getVelikost(){
		return this.velikost;
	}
	
	/**
	 * Vrací seznam úkolů obsahující artefakt
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkoly(){
		return this.ukoly;
	}
	
	/**
	 * Nastaví typ artefaktu
	 * @param typ typ artefaktu
	 */
	public void setTyp(String typ){
		this.typ = typ;
	}
	
	/**
	 * Nastaví velikost artefaktu
	 * @param velikost velikost artefaktu
	 */
	public void setVelikost(int velikost){
		this.velikost = velikost;
	}
	
	/**
	 * Vloží další úkol do seznamu úkolů
	 * @param ukol úkol artefaktu
	 */
	public void addUkol(Ukol ukol){
		this.ukoly.add(ukol);
	}
	
	/**
	 * Načte všechny úkoly, které obsahují artefakt
	 */
	public void nactiData(){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();
			this.ukoly.clear();
			this.ukoly = databazeUkoly.getUkolArtefakt(this.getID());			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniArtefaktu") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Nastaví do seznamu pouze ty úkoly, které odpovídají podmínkám (jsou-li hodnota null, daný filtr se nespustí) 
	 * a obsahují artefakt
	 * @param seznamIdUkolu seznam id úkolů (po odfiltrování), které lze do seznamu vložit
	 * @param seznamIdPriorit seznam id priorit (po odfiltrování), které musí úkoly v seznamu obsahovat
	 * @param seznamIdSeverit seznam id severit (po odfiltrování), které musí úkoly v seznamu obsahovat
	 * @param seznamIdResoluci seznam id resolucí (po odfiltrování), které musí úkoly v seznamu obsahovat
	 * @param seznamIdStatusu seznam id statusů (po odfiltrování), které musí úkoly v seznamu obsahovat
	 * @param seznamIdTypu seznam id typů (po odfiltrování), které musí úkoly v seznamu obsahovat
	 * @param seznamIdOsob seznam id osob (po odfiltrování), které musí úkoly v seznamu obsahovat
	 */
	public void nactiData(ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, 
						  ArrayList<Integer> seznamIdResoluci, ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, 
						  ArrayList<Integer> seznamIdOsob){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();		
			this.ukoly = databazeUkoly.getUkolFaze(this.getID(), seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, 
		   														seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);			
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniArtefaktu") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}
		
	}
}
