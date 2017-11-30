package data;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.Segment;
import data.Ukol;
import databaze.ArtefaktDAO;
import databaze.IArtefaktDAO;
import databaze.IKonfiguraceDAO;
import databaze.IProjektDAO;
import databaze.IUkolDAO;
import databaze.KonfiguraceDAO;
import databaze.ProjektDAO;
import databaze.UkolDAO;

/**
 * Datová třída pro informace o projektu
 * @author michalvselko
 *
 */
public class Projekt{

	private int id;																	//id projektu
	private String nazev;															//název projektu	
	
	private LocalDate datumPocatku;													//datum počátku projektu	
	private LocalDate datumKonce;													//datum ukončení projektu
	
	private ArrayList<Ukol> ukoly = new ArrayList<Ukol>();							//seznam úkolů patřících k projektu
	private ArrayList<Segment> faze = new ArrayList<Segment>();						//seznam fází projektu
	private ArrayList<Segment> iterace = new ArrayList<Segment>();					//seznam iterací projektu
	private ArrayList<Segment> aktivity = new ArrayList<Segment>();					//seznam aktivit projektu
	private ArrayList<Konfigurace> konfigurace = new ArrayList<Konfigurace>();		//seznam konfigurací projektu
	private ArrayList<Artefakt> artefakty = new ArrayList<Artefakt>();				//seznam artefaktů projektu
	private ArrayList<Osoba> osoby = new ArrayList<Osoba>();						//seznam lidí pracujících na projektu
	
	/**
	 * Konstruktor projektu
	 * @param id id projektu
	 * @param nazev název projektu
	 * @param datumPocatku datum počátku projektu
	 * @param datumKonce datum ukončení projektu
	 */
	public Projekt(int id, String nazev, LocalDate datumPocatku, LocalDate datumKonce){
		this.id = id;
		this.nazev = nazev; 
		this.datumPocatku = datumPocatku;
		this.datumKonce = datumKonce;
	}
	
	/**
	 * Vrací id projektu
	 * @return id projektu
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Vrací název projektu
	 * @return název projektu
	 */
	public String getNazev(){
		return this.nazev;
	}
	
	/**
	 * Vrací datum počátku projektu
	 * @return datum počátku projektu
	 */
	public LocalDate getDatumPocatku(){
		return this.datumPocatku;
	}
	
	/**
	 * Vrací datum konce projektu
	 * @return datum konce projektu
	 */
	public LocalDate getDatumKonce(){
		return this.datumKonce;
	}
	
	/**
	 * Vrací seznam úkolů přiřazených projektu
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkoly(){
		return this.ukoly;
	}
	
	/**
	 * Vrací seznam fází přiřazených projektu
	 * @return seznam fází
	 */
	public ArrayList<Segment> getFaze(){
		return this.faze;
	}

	/**
	 * Vrací seznam iterací přiřazených projektu
	 * @return seznam iterací
	 */
	public ArrayList<Segment> getIterace(){
		return this.iterace;
	}

	/**
	 * Vrací seznam aktivit přiřazených projektu
	 * @return seznam aktivit
	 */
	public ArrayList<Segment> getAktivity(){
		return this.aktivity;
	}

	/**
	 * Vrací seznam konfigurací přiřazených projektu
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfigurace(){
		return this.konfigurace;
	}

	/**
	 * Vrací seznam artefaktů projektu
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefakty(){
		return this.artefakty;
	}
	
	/**
	 * Vrací seznam osob pracujících na projektu
	 * @return seznam osob
	 */
	public ArrayList<Osoba> getOsoby(){
		return this.osoby;
	}
	
	/**
	 * Vrací počet úkolů v seznamu
	 * @return počet úkolů
	 */
	public int getPocetUkolu(){
		return this.ukoly.size();
	}
	
	/**
	 * Vrací počet fází v seznamu
	 * @return počet fází
	 */
	public int getPocetFazi(){
		return this.faze.size();
	}
	
	/**
	 * Vrací počet iterací v seznamu
	 * @return počet iterací
	 */
	public int getPocetIteraci(){
		return this.iterace.size();
	}
	
	/**
	 * Vrací počet aktivit v seznamu
	 * @return počet aktivit
	 */
	public int getPocetAktivit(){
		return this.aktivity.size();
	}
	
	/**
	 * Vrací počet konfigurací v seznamu
	 * @return počet konfigurací
	 */
	public int getPocetKonfiguraci(){
		return this.konfigurace.size();
	}

	/**
	 * Vrací počet artefaktů v seznamu
	 * @return počet artefaktů
	 */
	public int getPocetArtefaktu(){
		return this.artefakty.size();
	}

	/**
	 * Vrací počet osob v seznamu
	 * @return počet osob
	 */
	public int getPocetOsob(){
		return this.osoby.size();
	}
	
	/**
	 * Vrací počet větví ve všech konfiguracích v seznamu dohromady
	 * @return počet větví ve všech konfiguracích projektu
	 */
	public int getPocetVetvi(){
		int pocet = 0;
		for(int i = 0; i < getPocetKonfiguraci(); i++){
			pocet += this.konfigurace.get(i).getPocetVetvi();
		}
		return pocet;
	}

	/**
	 * Vrací počet tagů ve všech konfiguracích v seznamu dohromady
	 * @return počet tagů ve všech konfiguracích projektu
	 */
	public int getPocetTagu(){
		int pocet = 0;
		for(int i = 0; i < getPocetKonfiguraci(); i++){
			pocet += this.konfigurace.get(i).getPocetTagu();
		}
		return pocet;
	}
	
	/**
	 * Vrací minimální počet úkolů ve fázi ze seznamu fází
	 * @return minimální počet úkolů na fázi
	 */
	public int getMinFaze(){
		return getMinMaxSegment(Konstanty.MINIMUM, faze);		
	}
	
	/**
	 * Vrací minimální počet úkolů v iteraci ze seznamu iterací
	 * @return minimální počet úkolů na iteraci
	 */
	public int getMinIterace(){
		return getMinMaxSegment(Konstanty.MINIMUM, iterace);	
	}
	
	/**
	 * Vrací minimální počet úkolů v aktivitě ze seznamu aktivit
	 * @return minimální počet úkolů na aktivitu
	 */
	public int getMinAktivity(){
		return getMinMaxSegment(Konstanty.MINIMUM, aktivity);	
	}

	/**
	 * Vrací minimální předpokládaný čas úkolu ze seznamu úkolů
	 * @return minimální předpokládaný čas úkolu
	 */
	public double getMinPredpokladanyCas(){
		return getPredpokladanyCas(Konstanty.MINIMUM);			
	}
	
	/**
	 * Vrací minimální strávený čas úkolu ze seznamu úkolů
	 * @return minimální strávený čas
	 */
	public double getMinStravenyCas(){
		return getStravenyCas(Konstanty.MINIMUM);				
	}
	
	/**
	 * Vrací minimální počet úkolů přiřazených osobě
	 * @return minimální počet úkolů na osobu
	 */
	public int getMinOsobaUkol(){
		return getMinMaxOsoba(Konstanty.MINIMUM, Konstanty.UKOL);
	}

	/**
	 * Vrací minimální počet konfigurací u osoby
	 * @return minimální počet konfigurací na osobu
	 */
	public int getMinOsobaKonf(){
		return getMinMaxOsoba(Konstanty.MINIMUM, Konstanty.KONFIGURACE);
	}
	
	/**
	 * Vrací minimální počet artefaktů na osobu
	 * @return minimální počet artefaktů na osobu
	 */
	public int getMinOsobaArtef(){
		return getMinMaxOsoba(Konstanty.MINIMUM, Konstanty.ARTEFAKT);
	}
	
	/**
	 * Vrací minimální počet artefaktů konfigurace
	 * @return minimální počet artefaktů konfigurace
	 */
	public int getMinKonfiguraceArtef(){
		return getMinMaxKonfigurace(Konstanty.MINIMUM);
	}
	
	/**
	 * Vrací maximální počet úkolů ve fázi ze seznamu fází
	 * @return minimální počet úkolů na fázi
	 */
	public int getMaxFaze(){
		return getMinMaxSegment(Konstanty.MAXIMUM, faze);
	}
	
	/**
	 * Vrací maximální počet úkolů ve iteraci ze seznamu iterací
	 * @return minimální počet úkolů na iteraci
	 */
	public int getMaxIterace(){
		return getMinMaxSegment(Konstanty.MAXIMUM, iterace);
	}
	
	/**
	 * Vrací maximální počet úkolů v aktivitě ze seznamu aktivit
	 * @return minimální počet úkolů na aktivitu
	 */
	public int getMaxAktivity(){
		return getMinMaxSegment(Konstanty.MAXIMUM, aktivity);
	}
	
	/**
	 * Vrací maximální předpokládaný čas úkolu ze seznamu úkolů
	 * @return maximální předpokládaný čas úkolu
	 */
	public double getMaxPredpokladanyCas(){
		return getPredpokladanyCas(Konstanty.MAXIMUM);
	}

	/**
	 * Vrací maximální strávený čas úkolu ze seznamu úkolů
	 * @return maximální strávený čas úkolu
	 */
	public double getMaxStravenyCas(){
		return getStravenyCas(Konstanty.MAXIMUM);
	}

	/**
	 * Vrací maximální počet úkolů přiřazených osobě
	 * @return maximální počet úkolů na osobu
	 */
	public int getMaxOsobaUkol(){
		return getMinMaxOsoba(Konstanty.MAXIMUM, Konstanty.UKOL);
	}

	/**
	 * Vrací maximální počet konfigurací osoby
	 * @return maximální počet konfigurací na osobu
	 */
	public int getMaxOsobaKonf(){
		return getMinMaxOsoba(Konstanty.MAXIMUM, Konstanty.KONFIGURACE);
	}
	
	/**
	 * Vrací maximální počet artefaktů osoby
	 * @return maximální počet artefaktů na osobu
	 */
	public int getMaxOsobaArtef(){
		return getMinMaxOsoba(Konstanty.MAXIMUM, Konstanty.ARTEFAKT);
	}

	/**
	 * Vrací maximální počet artefaktů konfigurace
	 * @return maximální počet artefaktů konfigurace
	 */
	public int getMaxKonfiguraceArtef(){
		return getMinMaxKonfigurace(Konstanty.MAXIMUM);
	}
	
	/**
	 * Vrací průměrný počet úkolů ve fázi ze seznamu fází
	 * @return průměrný počet úkolů na fázi
	 */
	public double getPrumerFaze(){
		return getPrumerSegment(faze);
	}

	/**
	 * Vrací průměrný počet úkolů ve iteraci ze seznamu iterací
	 * @return průměrný počet úkolů na iteraci
	 */
	public double getPrumerIterace(){
		return getPrumerSegment(iterace);
	}

	/**
	 * Vrací průměrný počet úkolů v aktivitě ze seznamu aktivit
	 * @return průměrný počet úkolů na aktivitu
	 */
	public double getPrumerAktivity(){
		return getPrumerSegment(aktivity);
	}

	/**
	 * Vrací průměrný předpokládaný čas úkolu ze seznamu úkolů
	 * @return průměrný předpokládaný čas úkolu
	 */	
	public double getPrumerPredpokladanyCas(){
		return getPredpokladanyCas(Konstanty.PRUMER);
	}

	/**
	 * Vrací průměrný strávený čas úkolu ze seznamu úkolů
	 * @return průměrný strávený čas úkolu
	 */	
	public double getPrumerStravenyCas(){
		return getStravenyCas(Konstanty.PRUMER);
	}

	/**
	 * Vrací průměrný počet úkolů přiřazených osobě
	 * @return průměrný počet úkolů na osobu
	 */
	public double getPrumerOsobaUkol(){
		return getPrumerOsoba(Konstanty.UKOL);
	}

	/**
	 * Vrací průměrný počet konfigurací osoby
	 * @return průměrný počet konfigurací na osobu
	 */
	public double getPrumerOsobaKonf(){
		return getPrumerOsoba(Konstanty.KONFIGURACE);
	}
	
	/**
	 * Vrací průměrný počet artefaktů osoby
	 * @return průměrný počet artefaktů na osobu
	 */
	public double getPrumerOsobaArtef(){
		return getPrumerOsoba(Konstanty.ARTEFAKT);
	}
	
	/**
	 * Vrací průměrný počet artefaktů konfigurace
	 * @return průměrný počet artefaktů konfigurace
	 */
	public double getPrumerKonfiguraceArtef(){
		return getPrumerKonfigurace();
	}
	
	/**
	 * Projde všechny segmenty ze seznamu (fáze, iterace nebo aktivity) 
	 * a vrátí jejich maximální nebo minimální počet úkolů na segment podle zadaného parametru
	 * @param typVypoctu určuje zda se hledá maximum nebo minimum
	 * @param segmenty seznam fází, iterací nebo aktivit, které se mají prohledat
	 * @return minimální nebo maximální počet úkolů na segment
	 */
	private int getMinMaxSegment(int typVypoctu, ArrayList<Segment> segmenty){
		int vystup = 0;
		if(segmenty.size() > 0 ){
			vystup = segmenty.get(0).getPocetUkolu();													//do výstupu vloží počet úkolů prvního segmentu
			for(int i = 1; i < segmenty.size(); i++){													//projde všechny ostatní segmenty
				switch(typVypoctu){																		//typVypoctu určuje, zda hledat minimum či maximum
				case Konstanty.MINIMUM:	vystup = Math.min(segmenty.get(i).getPocetUkolu(), vystup);		//porovná počet úkolů segmentu s hodnotou ve proměnné vystup
										break;
				case Konstanty.MAXIMUM:	vystup = Math.max(segmenty.get(i).getPocetUkolu(), vystup);
										break;		
				default: 				vystup = 0;
										break;						
				}
			}
		}
		return vystup;
	}
	
	/**
	 * Projde všechny segmenty ze seznamu (fáze, iterace nebo aktivity)
	 * a vrátí jejich průměrný počet úkolů na segment
	 * @param segmenty seznam fází, iterací nebo aktivit, které se mají prohledat
	 * @return průměrný počet úkolů na segment zaokrouhlený na dvě desetinná místa
	 */
	private double getPrumerSegment(ArrayList<Segment> segmenty){
		int pocetUkolu = 0;
		for(int i = 0; i < segmenty.size(); i++){
			pocetUkolu += segmenty.get(i).getPocetUkolu();								//zjistí počet úkolů
		}
		if(segmenty.size() > 0){
			return Math.round((double)pocetUkolu / segmenty.size() * 100) / 100.00;		//vypočte průměr a zaokrouhlý na dvě desetinná místa
		}
		else
			return 0;
	}
	
	/**
	 * Projde všechny úkoly ze seznamu úkolů projektu a vrátí jejich maximální, minimální 
	 * nebo průměrný předpokládaný čas podle zadaného parametru
	 * @param typVypoctu určuje zda se hledá maximum, munimum nebo průměr
	 * @return maximální, minimální nebo průměrný předpokládaný čas zaokrouhlený na dvě desetinná čísla
	 */
	private double getPredpokladanyCas(int typVypoctu){
		double vystup = 0;
		if(ukoly.size() > 0){
			vystup = this.ukoly.get(0).getPredpokladanyCas();
			for(int i = 1; i < getPocetUkolu(); i++){
				switch(typVypoctu){
				case Konstanty.MINIMUM: vystup = Math.min(this.ukoly.get(i).getPredpokladanyCas(), vystup);
										break;
				case Konstanty.MAXIMUM: vystup = Math.max(this.ukoly.get(i).getPredpokladanyCas(), vystup);
										break;
				case Konstanty.PRUMER: 	vystup += this.ukoly.get(i).getPredpokladanyCas();
										break;
				default: 				vystup = 0;
										break;						
				}
			}
			if(typVypoctu == Konstanty.PRUMER){
				if(getPocetUkolu() > 0)
					vystup = vystup / getPocetUkolu();
				else
					vystup = 0;
			}
		}
		return Math.round(vystup * 100) / 100;
	}

	/**
	 * Projde všechny úkoly ze seznamu úkolů projektu a vrátí jejich maximální, minimální 
	 * nebo průměrný strávený čas podle zadaného parametru
	 * @param typVypoctu určuje zda se hledá maximum, munimum nebo průměr
	 * @return maximální, minimální nebo průměrný strávený čas zaokrouhlený na dvě desetinná čísla
	 */
	private double getStravenyCas(int typVypoctu){
		double vystup = 0;
		if(ukoly.size() > 0){
			vystup = this.ukoly.get(0).getStravenyCas();
			for(int i = 1; i < getPocetUkolu(); i++){
				switch(typVypoctu){
				case Konstanty.MINIMUM: vystup = Math.min(this.ukoly.get(i).getStravenyCas(), vystup);
										break;
				case Konstanty.MAXIMUM: vystup = Math.max(this.ukoly.get(i).getStravenyCas(), vystup);
										break;
				case Konstanty.PRUMER: 	vystup += this.ukoly.get(i).getStravenyCas();
										break;
				default: 				vystup = 0;
										break;						
				}
			}
			if(typVypoctu == Konstanty.PRUMER)
				if(getPocetUkolu() > 0)
					vystup = vystup / getPocetUkolu();
				else
					vystup = 0;
		}
		return Math.round(vystup * 100) / 100.00;
	}
	
	/**
	 * Projde všechny osoby ze seznamu osob projektu a vrátí minimální nebo maximální počet
	 * úkolů, konfigurací nebo artefaktů podle zadaných parametrů
	 * @param typVypoctu určuje zda se má vráti minimum nebo maximum
	 * @param typSeznamu určuje zda se hledá počet úkolů, konfigurací nebo artefaktů na osobu
	 * @return minimální nebo maximální počet úkolů, konfigurací nebo artefaktů na osobu
	 */
	private int getMinMaxOsoba(int typVypoctu, int typSeznamu){
		int vystup = 0;
		if(osoby.size() > 0){
			vystup = osoby.get(0).getPocet(typSeznamu);
			for(int i = 1; i < osoby.size(); i++){
				switch(typVypoctu){
				case Konstanty.MINIMUM:	vystup = Math.min(osoby.get(i).getPocet(typSeznamu), vystup);
										break;
				case Konstanty.MAXIMUM:	vystup = Math.max(osoby.get(i).getPocet(typSeznamu), vystup);
										break;		
				default: 				vystup = 0;
										break;						
				}
			}
		}
		return vystup;		
	}
	
	/**
	 * Projde všechny osoby ze seznamu osob projektu a vrátí průměrný počet
	 * úkolů, konfigurací nebo artefaktů zaokrouhlený na dvě desetinná místa
	 * @param typSeznamu určuje zda se hledá počet úkolů, konfigurací nebo artefaktů na osobu
	 * @return průměrný počet úkolů, konfigurací nebo artefaktů na osobu zaokrouhlený na dvě desetinná místa
	 */
	private double getPrumerOsoba(int typSeznamu){
		int pocet = 0;
		for(int i = 0; i < osoby.size(); i++){
			pocet += osoby.get(i).getPocet(typSeznamu);			
		}
		if(osoby.size() > 0)
			return Math.round((double)pocet / osoby.size() * 100) / 100.00;
		else
			return 0;
	}

	/**
	 * Projde všechny konfigurace ze seznamu konfigurací projektu 
	 * a vrátí minimální nebo maximální počet artefaktů na konfiguraci podle zadaného parametru
	 * @param typVypoctu určuje zda se hledá maximum nebo minimum
	 * @return maximální nebo minimální počet artefaků na konfiguraci
	 */
	private int getMinMaxKonfigurace(int typVypoctu){
		int vystup = 0;
		if(konfigurace.size() > 0){
			vystup = konfigurace.get(0).getPocetArtefaktu();
			for(int i = 1; i < konfigurace.size(); i++){
				switch(typVypoctu){
				case Konstanty.MINIMUM:	vystup = Math.min(konfigurace.get(i).getPocetArtefaktu(), vystup);
										break;
				case Konstanty.MAXIMUM:	vystup = Math.max(konfigurace.get(i).getPocetArtefaktu(), vystup);
										break;		
				default: 				vystup = 0;
										break;						
				}
			}
		}
		return vystup;		
	}
	
	/**
	 * Projde všechny konfigurace ze seznamu konfigurací projektu 
	 * a vrátí průměrný počet artefaktů na konfiguraci
	 * @return průměrný počet artefaktů na konfiguraci zaokrouhlený na dvě desetinná čísla
	 */
	private double getPrumerKonfigurace(){
		int pocet = 0;
		for(int i = 0; i < konfigurace.size(); i++){
			pocet += konfigurace.get(i).getPocetArtefaktu();			
		}
		if(konfigurace.size() > 0)
			return Math.round((double)pocet / konfigurace.size() * 100) / 100.00;
		else
			return 0;
	}

	/**
	 * Vloží úkol do seznamu úkolů
	 * @param ukol nový úkol pro vložení
	 */
	public void addUkol(Ukol ukol){
		this.ukoly.add(ukol);
	}
	
	/**
	 * Vloží fázi do seznamu fází
	 * @param faze nová fáze pro vložení
	 */
	public void addFaze(Segment faze){
		this.faze.add(faze);
	}

	/**
	 * Vloží iteraci do seznamu iterací
	 * @param iterace nová iterace pro vložení
	 */
	public void addIterace(Segment iterace){
		this.iterace.add(iterace);
	}

	/**
	 * Vloží aktivitu do seznamu aktivit
	 * @param aktivita nová aktivita pro vložení
	 */
	public void addAktivita(Segment aktivita){
		this.aktivity.add(aktivita);
	}	
	
	/**
	 * Vloží konfiguraci do seznamu konfigurací
	 * @param konfigurace nová konfigurace pro vložení
	 */
	public void addKonfigurace(Konfigurace konfigurace){
		this.konfigurace.add(konfigurace);
	}

	/**
	 * Vloží artefakt do seznamu artefaktů
	 * @param artefakt nový artefakt pro vložení
	 */
	public void addArtefakt(Artefakt artefakt){
		this.artefakty.add(artefakt);
	}
	
	/**
	 * Vloží osobu do seznamu osob
	 * @param osoba nová osoba pro vložení
	 */
	public void addOsoba(Osoba osoba){
		this.osoby.add(osoba);
	}

	/**
	 * Vrátí název projektu
	 */
	public String toString(){
		return this.getNazev();
	}
	
	/**
	 * Načte z databáze úkoly, fáze, iterace, aktivity, osoby, konfigurace a artefakty patřící do projektu
	 * a uloží je do příslušných seznamů
	 */
	public void nactiData(){
		try{
			Konstanty.CITAC_PROGRESU = 0;												//Nejprve se vynuluje čítač - důležité pro zobrazení progresu načítání dat
			IUkolDAO databazeUkoly = new UkolDAO();										//Vytvoří se příslušná DAO třída pro spuštění skriptů
			this.ukoly = databazeUkoly.getUkolProjekt(this.id);							//Z DAO třídy se získá a uloží požadovaný seznam
			Konstanty.CITAC_PROGRESU++;													//Zvýší se čítač, čímž se změní zobrazení progresu a uživatel přesně vidí kolik procent zbývá do úplného načtení
			
			IProjektDAO databazeProjekt = new ProjektDAO();
			this.faze = databazeProjekt.getFaze(this.id);
			Konstanty.CITAC_PROGRESU++;
			
			this.iterace = databazeProjekt.getIterace(this.id);
			Konstanty.CITAC_PROGRESU++;
			
			this.aktivity = databazeProjekt.getAktivity(this.id);
			Konstanty.CITAC_PROGRESU++;
			
			this.osoby = databazeProjekt.getOsoby(this.id);
			Konstanty.CITAC_PROGRESU++;
			
			IKonfiguraceDAO databazeKonfigurace = new KonfiguraceDAO();
			this.konfigurace = databazeKonfigurace.getKonfiguraceProjekt(this.id);
			Konstanty.CITAC_PROGRESU++;
			
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
			this.artefakty = databazeArtefakt.getArtefaktyProjekt(this.id);
			Konstanty.CITAC_PROGRESU++;
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniProjektu") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}		
	}

	/**
	 * Načte z databáze úkoly, fáze, iterace, aktivity, osoby, konfigurace a artefakty patřící do projektu
	 * a odpovidající podmínkám a uloží je do příslušných seznamů
	 * @param seznamIdUkolu seznam id možných úkolů pro vložení
	 * @param seznamIdPriorit seznam id priorit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdSeverit seznam id severit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdResoluci seznam id resolucí jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdStatusu seznam id statusů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdTypu seznam id typů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdOsob seznam id osob jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdFazi seznam id možných fází pro vložení
	 * @param seznamIdIteraci seznam id možných iterací pro vložení
	 * @param seznamIdAktivit seznam id možných aktivit pro vložení
	 * @param seznamIdKonfiguraci seznam id možných konfigurací pro vložení
	 * @param seznamIdArtefaktu seznam id možných artefaktů pro vložení
	 */
	public void nactiData(ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, 
						  ArrayList<Integer> seznamIdResoluci, ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, 
						  ArrayList<Integer> seznamIdOsob, ArrayList<Integer> seznamIdFazi, ArrayList<Integer> seznamIdIteraci, 
						  ArrayList<Integer> seznamIdAktivit, ArrayList<Integer> seznamIdKonfiguraci, ArrayList<Integer> seznamIdArtefaktu){
		try{
			Konstanty.CITAC_PROGRESU = 0;
			IUkolDAO databazeUkoly = new UkolDAO();
			this.ukoly = databazeUkoly.getUkolProjekt(this.id, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, 
													  seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob );		
			Konstanty.CITAC_PROGRESU++;
			
			IProjektDAO databazeProjekt = new ProjektDAO();
			this.faze = databazeProjekt.getFaze(this.id, seznamIdFazi);
			for(int i = 0; i < this.faze.size(); i++)
				this.faze.get(i).nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
			Konstanty.CITAC_PROGRESU++;
			
			this.iterace = databazeProjekt.getIterace(this.id, seznamIdIteraci);
			for(int i = 0; i < this.iterace.size(); i++)							//obsahují-li položky seznamu nějaké další seznamy na které je nutné aplikovat filtry, musí se spusti načtení pro každou položku
				this.iterace.get(i).nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
			Konstanty.CITAC_PROGRESU++;
			
			this.aktivity = databazeProjekt.getFaze(this.id, seznamIdAktivit);
			for(int i = 0; i < this.aktivity.size(); i++)
				this.aktivity.get(i).nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
			Konstanty.CITAC_PROGRESU++;
			
			this.osoby = databazeProjekt.getOsoby(this.id, seznamIdOsob);
			for(int i = 0; i < this.osoby.size(); i++)
				this.osoby.get(i).nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob, seznamIdKonfiguraci, seznamIdArtefaktu);
			Konstanty.CITAC_PROGRESU++;
				
			IKonfiguraceDAO databazeKonfigurace = new KonfiguraceDAO();
			this.konfigurace = databazeKonfigurace.getKonfiguraceProjekt(this.id, seznamIdKonfiguraci);
			for(int i = 0; i < this.konfigurace.size(); i++)
				this.konfigurace.get(i).nactiData(seznamIdArtefaktu);
			Konstanty.CITAC_PROGRESU++;
				
			IArtefaktDAO databazeArtefakt = new ArtefaktDAO();
			this.artefakty = databazeArtefakt.getArtefaktyProjekt(this.id, seznamIdArtefaktu);
			for(int i = 0; i < this.artefakty.size(); i++)
				this.artefakty.get(i).nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
			Konstanty.CITAC_PROGRESU++;
		}  catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniProjektu") + Konstanty.POPISY.getProperty("chybaZkusteJiny"));
			e.printStackTrace();
		}				
	}
	

}
