package databaze;

import java.util.ArrayList;

import data.Osoba;
import data.Projekt;
import data.Segment;

/**
 * Rozhraní pro výběr dat podle projektu (projekty, segmenty a osoby)
 * @author michalvselko
 *
 */
public interface IProjektDAO {
	
	/**
	 * Vrací seznam projektů v databázi
	 * @return seznam projektů
	 */
	public ArrayList<Projekt> getProjekt();
	
	/**
	 * Vrací seznam fází patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr fází
	 * @return seznam fází (třída Segment)
	 */
	public ArrayList<Segment> getFaze(int idProjekt);
	
	/**
	 * Vrací seznam iterací patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr iterací
	 * @return seznam iterací (třída Segment)
	 */
	public ArrayList<Segment> getIterace(int idProjekt);
	
	/**
	 * Vrací seznam aktivit patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr aktivit
	 * @return seznam aktivit (třída Segment)
	 */
	public ArrayList<Segment> getAktivity(int idProjekt);
	
	/**
	 * Vrací seznam osob patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr osob
	 * @return seznam osob
	 */
	public ArrayList<Osoba> getOsoby(int idProjekt);
	
	/**
	 * Vrací seznam fází patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr fází
	 * @param seznamIdFazi seznam povolených fází
	 * @return seznam fází (třída Segment)
	 */
	public ArrayList<Segment> getFaze(int idProjekt, ArrayList<Integer> seznamIdFazi);
	
	/**
	 * Vrací seznam iterací patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr iterací
	 * @param seznamIdIteraci seznam povolených iterací
	 * @return seznam iterací (třída Segment)
	 */
	public ArrayList<Segment> getIterace(int idProjekt, ArrayList<Integer> seznamIdIteraci);
	
	/**
	 * Vrací seznam aktivit patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr aktivit
	 * @param seznamIdAktivit seznam povolených aktivit
	 * @return seznam aktivit (třída Segment)
	 */
	public ArrayList<Segment> getAktivity(int idProjekt, ArrayList<Integer> seznamIdAktivit);	
	
	/**
	 * Vrací seznam osob patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr osob
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam osob
	 */
	public ArrayList<Osoba> getOsoby(int idProjekt, ArrayList<Integer> seznamIdOsob);

}
