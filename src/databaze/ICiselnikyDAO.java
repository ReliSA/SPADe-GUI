package databaze;

import java.util.ArrayList;

import data.polozky.PolozkaCiselnik;

/**
 * Rozhraní pro výběr priorit, severit, statusů, resolucí a osob z databáze
 * @author michalvselko
 *
 */
public interface ICiselnikyDAO {

	/**
	 * Vrací seznam priorit patřících do projektu
	 * @param idProjekt id projektu pro výběr priorit
	 * @return seznam priorit (třída PolozkaCiselnik)
	 */
	public ArrayList<PolozkaCiselnik> getPriority(int idProjekt);

	/**
	 * Vrací seznam severit patřících do projektu
	 * @param idProjekt id projektu pro výběr severit
	 * @return seznam severit (třída PolozkaCiselnik)
	 */
	public ArrayList<PolozkaCiselnik> getSeverity(int idProjekt);
	
	/**
	 * Vrací seznam statusů patřících do projektu
	 * @param idProjekt id projektu pro výběr statusů
	 * @return seznam statusů (třída PolozkaCiselnik)
	 */
	public ArrayList<PolozkaCiselnik> getStatus(int idProjekt);

	/**
	 * Vrací seznam typů patřících do projektu
	 * @param idProjekt id projektu pro výběr typů
	 * @return seznam typů (třída PolozkaCiselnik)
	 */
	public ArrayList<PolozkaCiselnik> getTyp(int idProjekt);
	
	/**
	 * Vrací seznam resolucí patřících do projektu
	 * @param idProjekt id projektu pro výběr resolucí
	 * @return seznam resolucí (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getResoluce(int idProjekt);

	/**
	 * Vrací seznam osob patřících do projektu
	 * @param idProjekt id projektu pro výběr osob
	 * @return seznam osob (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getOsoby(int idProjekt);
}
