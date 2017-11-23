package databaze;

import java.util.ArrayList;

import data.Konfigurace;
import data.polozky.Polozka;

/**
 * Rozhraní pro výběr konfigurací z databáze
 * @author michalvselko
 *
 */
public interface IKonfiguraceDAO {

	/**
	 * Vrací seznam konfigurací patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr konfigurací
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfiguraceProjekt(int idProjekt);
	
	/**
	 * Vrací seznam konfigurací patřících osobě s id v parametru
	 * @param idOsoba id osoby pro výběr konfigurací
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfiguraceOsoba(int idOsoba);
	
	/**
	 * Vrací seznam položek větví patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr větví
	 * @return seznam položek větví
	 */
	public ArrayList<Polozka> getVetveKonfigurace(int idKonfigurace);
	
	/**
	 * Vrací seznam položek tagů patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr tagů
	 * @return seznam položek tagů
	 */
	public ArrayList<Polozka> getTagyKonfigurace(int idKonfigurace);

	/**
	 * Vrací seznam konfigurací patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr konfigurací
	 * @param seznamIdKonfiguraci seznam povolených konfigurací
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfiguraceProjekt(int idProjekt, ArrayList<Integer> seznamIdKonfiguraci);
	
	/**
	 * Vrací seznam konfigurací patřících osobě s id v parametru a splňující podmínky filtru výběru
	 * @param idOsoba id osoby pro výběr konfigurací
	 * @param seznamIdKonfiguraci seznam povolených konfigurací
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfiguraceOsoba(int idOsoba, ArrayList<Integer> seznamIdKonfiguraci);	
}
