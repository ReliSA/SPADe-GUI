package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import data.polozky.Polozka;
import data.polozky.PolozkaVytvoreni;
import databaze.ArtefaktDAO;
import databaze.IArtefaktDAO;
import databaze.IKonfiguraceDAO;
import databaze.KonfiguraceDAO;
import ostatni.Konstanty;

/**
 * Datová třída konfigurace zděděná ze třídy PolozkaVytvoreni
 */
public class Konfigurace extends PolozkaVytvoreni implements Serializable{


	private static final long serialVersionUID = 3243453625395646244L;
	private ArrayList<Artefakt> artefakty = new ArrayList<Artefakt>();	//seznam artefaktů patřící ke konfiguraci (tabulka artifact)
	private ArrayList<Polozka> vetve = new ArrayList<Polozka>();		//seznam větví patřící ke konfiguraci (tabulka branch)
	private ArrayList<Polozka> tagy = new ArrayList<Polozka>();			//seznam tagů patřící ke konfiguraci (tabulka tag)
	
	/**
	 * Konstruktor třídy Konfigurace - nastaví proměnné a spustí metodu nactiData()
	 * @param id id konfigurace
	 * @param nazev nazev konfigurace (sloupec indentifier tabulky commit)
	 * @param datumVytvoreni datum vytvoření konfigurace
	 */
	public Konfigurace(int id, String nazev, LocalDate datumVytvoreni){
		super(id, nazev, datumVytvoreni);
		nactiData();
	}

	/**
	 * Vrací seznam artefaktů patřící do konfigurace
	 * @return seznam artefaktů konfigurace
	 */
	public ArrayList<Artefakt> getArtefakty(){
		return this.artefakty;
	}
	
	/**
	 * Vrací seznam větví patřící do konfigurace
	 * @return seznam větví konfigurace
	 */
	public ArrayList<Polozka> getVetve(){
		return this.vetve;
	}
	
	/**
	 * Vrací seznam tagů patřící do konfigurace
	 * @return seznam tagů konfigurace
	 */
	public ArrayList<Polozka> getTagy(){
		return this.tagy;
	}
	
	/**
	 * Vrací počet artefaktů v konfiguraci
	 * @return počet artefaktů konfigurace
	 */
	public int getPocetArtefaktu(){
		return this.artefakty.size();
	}
	
	/**
	 * Vrací počet větví konfigurace
	 * @return počet větví konfigurace
	 */
	public int getPocetVetvi(){
		return this.vetve.size();
	}
	
	/**
	 * Vrací počet tagů konfigurace
	 * @return počet tagů
	 */
	public int getPocetTagu(){
		return this.tagy.size();
	}
	
	/**
	 * Vloží artefakt do seznamu artefaktů
	 * @param artefakt artefakt pro přidání
	 */
	public void addArtefakt(Artefakt artefakt){
		this.artefakty.add(artefakt);
	}

	/**
	 * Vloží větev do seznamu větví
	 * @param vetev větev pro přidání
	 */
	public void addVetev(Polozka vetev){
		this.vetve.add(vetev);
	}

	/**
	 * Vloží tag do seznamu tagů
	 * @param tag tag pro přidání
	 */
	public void addTag(Polozka tag){
		this.tagy.add(tag);
	}

	/**
	 * Načte všechny artefakty, větve a tagy spadající do konfigurace
	 */
	public void nactiData(){
		try{
			IKonfiguraceDAO databazeKonfigurace = new KonfiguraceDAO();
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
			this.artefakty.clear();
			this.vetve.clear();
			this.tagy.clear();
		
			this.artefakty = databazeArtefakt.getArtefaktyKonfigurace(this.getID());
			this.vetve = databazeKonfigurace.getVetveKonfigurace(this.getID());
			this.tagy = databazeKonfigurace.getTagyKonfigurace(this.getID());
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniKonfigurace") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Načte všechny artefakty, které patří do konfigurace a jsou ve vyfiltrovaném seznamu
	 * @param seznamIdArtefaktu id artefaktů, které je možné vložit do seznamu
	 */
	public void nactiData(ArrayList<Integer> seznamIdArtefaktu){
		try{
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
			this.artefakty = databazeArtefakt.getArtefaktyKonfigurace(this.getID(), seznamIdArtefaktu);
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniKonfigurace") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}
	}
	
}
