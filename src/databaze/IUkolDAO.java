package databaze;

import java.util.ArrayList;

import data.Ukol;

/**
 * Rozhraní pro výběr úkolů z databáze
 */
public interface IUkolDAO {
	
	/**
	 * Vrací seznam úkolů patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolProjekt(int idProjekt);
	
	/**
	 * Vrací seznam úkolů patřících do fáze s id v parametru
	 * @param idFaze id fáze pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolFaze(int idFaze);
	
	/**
	 * Vrací seznam úkolů patřících do iterace s id v parametru
	 * @param idIterace id iterace pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolIterace(int idIterace);
	
	/**
	 * Vrací seznam úkolů patřících do aktivity s id v parametru
	 * @param idAktivity id aktivity pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolAktivity(int idAktivity);
	
	/**
	 * Vrací seznam úkolů patřících osobě s id v parametru
	 * @param idOsoby id osoby pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolOsoba(int idOsoby);
	
	/**
	 * Vrací seznam úkolů patřících do artefaktu s id v parametru
	 * @param idArtefakt id artefaktu pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolArtefakt(int idArtefakt);
	
	/**
	 * Vrací seznam úkolů patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolProjekt(int idProjekt, ArrayList<Integer> seznamIdUkolu, String logPriorit, ArrayList<Integer> seznamIdPriorit, String logSeverit, ArrayList<Integer> seznamIdSeverit, String logResolution, ArrayList<Integer> seznamIdResoluci, 
			String logStatusu, ArrayList<Integer> seznamIdStatusu, String logTypu, ArrayList<Integer> seznamIdTypu, String logOsob, ArrayList<Integer> seznamIdOsob);	
	/**
	 * Vrací seznam úkolů patřících do fáze s id v parametru a splňující podmínky filtru výběru
	 * @param idFaze id fáze pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolFaze(int idFaze, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			  									   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob);
	
	/**
	 * Vrací seznam úkolů patřících do iterace s id v parametru a splňující podmínky filtru výběru
	 * @param idIterace id iterace pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolIterace(int idIterace, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			  											 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob);
	
	/**
	 * Vrací seznam úkolů patřících do aktivity s id v parametru a splňující podmínky filtru výběru
	 * @param idAktivity id aktivity pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolAktivity(int idAktivity, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			  											   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob);
	
	/**
	 * Vrací seznam úkolů patřících osobě s id v parametru a splňující podmínky filtru výběru
	 * @param idOsoby id osoby pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolOsoba(int idOsoby, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			  										 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob);
	
	/**
	 * Vrací seznam úkolů patřících artefaktu s id v parametru a splňující podmínky filtru výběru
	 * @param idArtefakt id artefaktu pro výběr úkolů
	 * @param seznamIdUkolu seznam povolených úkolů
	 * @param seznamIdPriorit seznam povolených priorit
	 * @param seznamIdSeverit seznam povolených severit
	 * @param seznamIdResoluci seznam povolených resolucí
	 * @param seznamIdStatusu seznam povolených statusů
	 * @param seznamIdTypu seznam povolených typů
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolArtefakt(int idArtefakt, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			  		 									   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob);

}
