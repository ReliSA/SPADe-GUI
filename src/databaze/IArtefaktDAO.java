package databaze;

import java.util.ArrayList;

import data.Artefakt;

/**
 * Rozhraní pro výběr artefaktů z databáze
 */
public interface IArtefaktDAO {
	
	/**
	 * Vrací seznam artefaktů patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyProjekt(int idProjekt);

	/**
	 * Vrací seznam artefaktů patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyKonfigurace(int idKonfigurace);

	/**
	 * Vrací seznam artefaktů patřících osobě s id v parametru
	 * @param idOsoby id osoby pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyOsoba(int idOsoby);

	/**
	 * Vrací seznam artefaktů patřících do projektu s id v parametru a splňující podmínky filtru artefaktů
	 * @param idProjekt id projektu pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyProjekt(int idProjekt, ArrayList<Integer> seznamIdArtefaktu);
	
	/**
	 * Vrací seznam artefaktů patřících do konfigurace s id v parametru a splňující podmínky filtru artefaktů
	 * @param idKonfigurace id konfigurace pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyKonfigurace(int idKonfigurace, ArrayList<Integer> seznamIdArtefaktu);
	
	/**
	 * Vrací seznam artefaktů patřících osobě s id v parametru a splňující podmínky filtru výběru
	 * @param idOsoby id osoby pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyOsoba(int idOsoby, ArrayList<Integer> seznamIdArtefaktu);
}
