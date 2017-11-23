package data;

import java.time.LocalDate;

import data.polozky.PolozkaPocatek;

/**
 * Datová třída work_unitu zděděná ze třídy PolozkaPocatek
 * @author michalvselko
 *
 */
public class Ukol extends PolozkaPocatek{

	private double predpokladanyCas;	//předpokládaný čas úkolu
	private double stravenyCas;			//skutečně strávený čas na úkolu
	
	private int priorityID;				//id priority
	private int severityID;				//id severity
	private int statusID;				//id statusu
	private int typID;					//id typu
	private int resoluceID;				//id resoluce
	private int prirazenID;				//id osoby která má úkol přířazen
	private int autorID;				//id autora úkolu
	
	/**
	 * Konstruktor úkolu
	 * @param id id úkolu
	 * @param nazev název úkolu
	 * @param datumVytvoreni datum vytvoření úkolu
	 * @param datumPocatku datum začátku úkolu
	 * @param predpokladanyCas předpokládaný doba úkolu
	 * @param stravenyCas skutečně strávený čas na úkolu
	 * @param priorityID id priority
	 * @param severityID id severity
	 * @param statusID id statusu
	 * @param typID id typu
	 * @param resoluceID id resoluce
	 * @param prirazenID id osoby která má úkol přiřazen
	 * @param autorID id autora úkolu
	 */
	public Ukol(int id, String nazev, LocalDate datumVytvoreni, LocalDate datumPocatku, double predpokladanyCas, double stravenyCas, int priorityID, int severityID, 
					 int statusID, int typID, int resoluceID, int prirazenID, int autorID){
		
		super(id, nazev, datumVytvoreni,datumPocatku);
		this.predpokladanyCas = predpokladanyCas;
		this.stravenyCas = stravenyCas;
		this.priorityID = priorityID;
		this.severityID = severityID;
		this.statusID = statusID;
		this.typID = typID;
		this.resoluceID = resoluceID;
		this.prirazenID = prirazenID;
		this.autorID = autorID;				
	}
	
	/**
	 * Vrací předpokládanou dobu potřebnou k vyešení úkolu
	 * @return předpokládaná doba úkolu
	 */
	public double getPredpokladanyCas(){
		return this.predpokladanyCas;
	}
	
	/**
	 * Vrací skutečně strávenou dobu na úkolu
	 * @return skutečně strávený čas na úkolu
	 */
	public double getStravenyCas(){
		return this.stravenyCas;
	}
	
	/**
	 * Vrací id priority úkolu
	 * @return id priority
	 */
	public int getPriorityID(){
		return this.priorityID;
	}
		
	/**
	 * Vrací id severity úkolu
	 * @return id severity
	 */
	public int getSeverityID(){
		return this.severityID;
	}
		
	/**
	 * Vrací id statusu úkolu
	 * @return id statusu
	 */
	public int getStatusID(){
		return this.statusID;
	}
		
	/**
	 * Vrací id typu úkolu
	 * @return id typu
	 */
	public int getTypID(){
		return this.typID;
	}
	
	/**
	 * Vrací id resoluce úkolu
	 * @return id resoluce
	 */
	public int getResoluceID(){
		return this.resoluceID;
	}
	
	/**
	 * Vrací id osoby která má úkol přiřazen
	 * @return id osoby řešitele
	 */
	public int getPrirazenID(){
		return this.prirazenID;
	}
	
	/**
	 * Vrací id autora úkolu
	 * @return id autora
	 */
	public int getAutorID(){
		return this.autorID;
	}
}
