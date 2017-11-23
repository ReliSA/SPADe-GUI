package data;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaPocatek;
import databaze.IUkolDAO;
import databaze.UkolDAO;

/**
 * Datová třída pro uložení segmentů (fází, iterací a aktivit) zděděná ze třídy PolozkaPocatek
 * @author michalvselko
 *
 */
public class Segment extends PolozkaPocatek{

	private int typSegmentu;								//Typ segmentu -> Třída Konstanty: 7=Fáze, 8=Iterace, 9=Aktivity
	private LocalDate datumKonce;							//Datum konce segmentu
	private ArrayList<Ukol> ukoly = new ArrayList<Ukol>();	//Seznam úkolu spadající do daného segmentu
	
	/**
	 * Konstuktor třídy Segment
	 * @param id id segmentu
	 * @param nazev název segmentu
	 * @param typSegmentu typ - Třída Konstanty: 7=Fáze, 8=Iterace, 9=Aktivity
	 * @param datumVytvoreni datum vytvoření segmentu
	 * @param datumPocatku datum počátku segmentu
	 * @param datumKonce datum ukončení segmentu
	 */
	public Segment(int id, String nazev, int typSegmentu, LocalDate datumVytvoreni, LocalDate datumPocatku, LocalDate datumKonce){
		super(id, nazev, datumVytvoreni, datumPocatku);
		this.typSegmentu = typSegmentu;
		this.datumKonce = datumKonce;
		nactiData();
	}
	
	/**
	 * Vrací typ segmentu
	 * @return typ segmentu
	 */
	public int getTypSegmentu(){
		return this.typSegmentu;
	}
	/**
	 * Vrací název typu segmentu
	 * @return název typu segmentu
	 */
	public String getNazevTypuSegmentu(){
		switch(this.typSegmentu){
		case Konstanty.FAZE: return "Fáze";
		case Konstanty.ITERACE: return "Iterace";
		case Konstanty.AKTIVITY: return "Aktivity";
		default: return "Nedefinovaný typ";
		}
	}
	
	/**
	 * Vrací datum ukončení segmentu
	 * @return datum ukončení
	 */
	public LocalDate getDatumKonce(){
		return this.datumKonce;
	}
	
	/**
	 * Vrací seznam úkolů přiřazených segmentu
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkoly(){
		return this.ukoly;
	}
	
	/**
	 * Vrací počet úkolů segmentu
	 * @return počet úkolů
	 */
	public int getPocetUkolu(){
		return this.ukoly.size();
	}
	
	/**
	 * Nastaví typ segmentu
	 * @param typSegmentu nový typ segmentu
	 */
	public void setTypSegmentu(int typSegmentu){
		this.typSegmentu = typSegmentu;
	}
	
	/**
	 * Nastaví datum ukončení segmentu
	 * @param datumKonce nový datum ukončení
	 */
	public void setDatumKonce(LocalDate datumKonce){
		this.datumKonce = datumKonce;
	}

	/**
	 * Vloží nový úkol do seznamu úkolů
	 * @param ukol úkol pro vložení do seznamu
	 */
	public void addUkol(Ukol ukol){
		this.ukoly.add(ukol);
	}
	
	/**
	 * Načte všechny úkoly spadající do příslušného segmentu
	 */
	public void nactiData(){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();		
			this.ukoly.clear();
			switch(this.typSegmentu){
			case Konstanty.FAZE:	this.ukoly = databazeUkoly.getUkolFaze(this.getID());
									break;
			case Konstanty.ITERACE:	this.ukoly = databazeUkoly.getUkolIterace(this.getID());
									break;
			case Konstanty.AKTIVITY:this.ukoly = databazeUkoly.getUkolAktivity(this.getID());
									break;
			default: break;
			}				
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data segmentu: " + this.getNazevTypuSegmentu() + "! Zkuste přejít na jiný projekt a vrátit se.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Načte úkoly spadající pod segment a odpovídající podmínkám
	 * @param seznamIdUkolu seznam id možných úkolů pro vložení
	 * @param seznamIdPriorit seznam id priorit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdSeverit seznam id severit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdResoluci seznam id resolucí jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdStatusu seznam id statusů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdTypu seznam id typů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdOsob seznam id osob přiřazených k úkolům které lze vložit do seznamu
	 */
	public void nactiData(ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, 
						  ArrayList<Integer> seznamIdResoluci, ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, 
						  ArrayList<Integer> seznamIdOsob){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();		
			switch(this.typSegmentu){
			case Konstanty.FAZE:	this.ukoly = databazeUkoly.getUkolFaze(this.getID(), seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, 
		   														seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
									break;
			case Konstanty.ITERACE:	this.ukoly = databazeUkoly.getUkolIterace(this.getID(), seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, 
															  seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
									break;
			case Konstanty.AKTIVITY:this.ukoly = databazeUkoly.getUkolAktivity(this.getID(), seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, 
																seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
									break;
			default: break;
			}				
	
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data segmentu: " + this.getNazevTypuSegmentu() + "! Zkuste přejít na jiný projekt a vrátit se.");
			e.printStackTrace();
		}
	}

}
