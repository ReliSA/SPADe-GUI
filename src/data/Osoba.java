package data;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.Polozka;
import databaze.ArtefaktDAO;
import databaze.IArtefaktDAO;
import databaze.IKonfiguraceDAO;
import databaze.IUkolDAO;
import databaze.KonfiguraceDAO;
import databaze.UkolDAO;

/**
 * Datová třída seznamu osob zděděná ze třídy Polozka
 * @author michalvselko
 *
 */
public class Osoba extends Polozka{

	private ArrayList<Ukol> ukoly = new ArrayList<Ukol>();						//seznam úkolů přiřazených osobě
	private ArrayList<Konfigurace> konfigurace = new ArrayList<Konfigurace>();	//seznam konfigurací provedených osobou
	private ArrayList<Artefakt> artefakty = new ArrayList<Artefakt>();			//seznam artefaktů vložených osobou
	
	/**
	 * Konstrukto třídy Osoba
	 * @param id id osoby
	 * @param jmeno jméno osoby
	 */
	public Osoba(int id, String jmeno) {
		super(id, jmeno);
		nactiData();
	}

	/**
	 * Podle typu seznamu vrací počet položek
	 * @param typSeznamu typ seznamu jehož počet se má vráti
	 * @return počet položek v daném seznamu
	 */
	public int getPocet(int typSeznamu){
		switch(typSeznamu){
		case Konstanty.UKOL: return ukoly.size();
		case Konstanty.KONFIGURACE: return konfigurace.size();
		case Konstanty.ARTEFAKT: return artefakty.size();
		default: return 0;
		}		
	}
	
	/**
	 * Vrací počet úkolů přiřazených osobě
	 * @return počet úkolů
	 */
	public int getPocetUkolu(){
		return this.ukoly.size();
	}
	
	/**
	 * Vrací počet konfigurací provedených osobou
	 * @return počet konfigurací
	 */
	public int getPocetKonfiguraci(){
		return this.konfigurace.size();
	}
	
	/**
	 *Vrací počet artefaktů vložených osobou
	 * @return počet artefaktů
	 */
	public int getPocetArtefaktu(){
		return this.artefakty.size();
	}
	
	/**
	 * Vrací seznam úkolů přiřazených osobě
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkoly(){
		return this.ukoly;
	}
	
	/**
	 * Vrací seznam konfigurací provedených osobou
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfigurace(){
		return this.konfigurace;
	}
	
	/**
	 * Vrací seznam artefaktů vložených osobou
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefakty(){
		return this.artefakty;
	}
	
	/**
	 * Vloží úkol do seznamu úkolů
	 * @param ukol úkol pro vložení
	 */
	public void addUkol(Ukol ukol){
		this.ukoly.add(ukol);
	}

	/**
	 * Vloží konfiguraci do seznamu konfigurací
	 * @param konfigurace konfigurace pro vložení
	 */
	public void addKonfigurace(Konfigurace konfigurace){
		this.konfigurace.add(konfigurace);
	}
	
	/**
	 * Vloží artefakt do seznamu artefaktů
	 * @param artefakt artefakt pro vložení
	 */
	public void addArtefakt(Artefakt artefakt){
		this.artefakty.add(artefakt);
	}
	
	/**
	 * Načte všechny úkoly, konfigurace a artefakty spadající pod osobu
	 */
	public void nactiData(){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();
			IKonfiguraceDAO databazeKonfigurace = new KonfiguraceDAO();
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
	
			this.ukoly.clear();
			this.konfigurace.clear();
			this.artefakty.clear();
	
			this.ukoly = databazeUkoly.getUkolOsoba(this.getID());
			this.konfigurace = databazeKonfigurace.getKonfiguraceOsoba(this.getID());
			this.artefakty = databazeArtefakt.getArtefaktyOsoba(this.getID());
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data osob! Zkuste přejít na jiný projekt a vrátit se.");
			e.printStackTrace();
		}				
	}

	/**
	 * Načte úkoly, konfigurace a artefakty spadající pod osobu a odpovídající podmínkám
	 * @param seznamIdUkolu seznam id možných úkolů pro vložení
	 * @param seznamIdPriorit seznam id priorit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdSeverit seznam id severit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdResoluci seznam id resolucí jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdStatusu seznam id statusů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdTypu seznam id typů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdOsob seznam id osob jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdKonfiguraci seznam id možných konfigurací pro vložení
	 * @param seznamIdArtefaktu seznam id možných artefaktů pro vložení
	 */
	public void nactiData(ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, 
						  ArrayList<Integer> seznamIdResoluci, ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, 
						  ArrayList<Integer> seznamIdOsob, ArrayList<Integer> seznamIdKonfiguraci, ArrayList<Integer> seznamIdArtefaktu){
		try{
			IUkolDAO databazeUkoly = new UkolDAO();
			IKonfiguraceDAO databazeKonfigurace = new KonfiguraceDAO();
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
	
			this.ukoly = databazeUkoly.getUkolOsoba(this.getID(), seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, 
													seznamIdStatusu, seznamIdTypu, seznamIdOsob);
			this.konfigurace = databazeKonfigurace.getKonfiguraceOsoba(this.getID(), seznamIdKonfiguraci);
			this.artefakty = databazeArtefakt.getArtefaktyOsoba(this.getID(), seznamIdArtefaktu);
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data osob! Zkuste přejít na jiný projekt a vrátit se.");
			e.printStackTrace();
		}		
	}

}
