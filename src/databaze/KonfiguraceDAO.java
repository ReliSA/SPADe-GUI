package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.Konfigurace;
import data.polozky.Polozka;

/**
 * Třída zajišťující výběr dat konfigurací z databáze implementující rozhraní IKonfiguraceDAO
 * @author michalvselko
 *
 */
public class KonfiguraceDAO implements IKonfiguraceDAO{

	private Connection pripojeni;					//připojení k databázi
	
	public KonfiguraceDAO(){
		this.pripojeni = Konstanty.PRIPOJENI;		//nastaví připojení uložené ve třídě Konstanty
	}
	
	/**
	 * Vrací seznam konfigurací patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr konfigurací
	 * @return seznam konfigurací
	 */	
	public ArrayList<Konfigurace> getKonfiguraceProjekt(int idProjekt) {
		return getKonfiguraceProjekt(idProjekt, null);
		
	}

	/**
	 * Vrací seznam konfigurací patřících osobě s id v parametru
	 * @param idOsoba id osoby pro výběr konfigurací
	 * @return seznam konfigurací
	 */
	public ArrayList<Konfigurace> getKonfiguraceOsoba(int idOsoba) {
		return getKonfiguraceOsoba(idOsoba, null);
		
	}

	/**
	 * Vrací seznam položek větví patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr větví
	 * @return seznam položek větví
	 */	
	public ArrayList<Polozka> getVetveKonfigurace(int idKonfigurace) {
		String sql = "SELECT b.id, b.name "
			 	   + "FROM branch b "
			 	   + "left join configuration_branch c on b.id = c.branchId "
			 	   + "WHERE c.configurationId = ? ";
		return getPolozka(sql, idKonfigurace);
	}

	/**
	 * Vrací seznam položek tagů patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr tagů
	 * @return seznam položek tagů
	 */
	public ArrayList<Polozka> getTagyKonfigurace(int idKonfigurace) {
		String sql = "SELECT t.id, t.name "
			 	   + "FROM tag t "
			 	   + "WHERE t.configurationId = ? ";
		return getPolozka(sql, idKonfigurace);
	}

	/**
	 * Vrací seznam konfigurací patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr konfigurací
	 * @param seznamIdKonfiguraci seznam povolených konfigurací
	 * @return seznam konfigurací
	 */	
	public ArrayList<Konfigurace> getKonfiguraceProjekt(int idProjekt, ArrayList<Integer> seznamIdKonfiguraci) {
		String sql = "SELECT c.id, t.identifier as name, "
				   + "coalesce(m.committed, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as committed "
			 	   + "FROM configuration c "
			 	   + "left join committed_configuration m on c.id = m.id "
			 	   + "left join commit t on m.id = t.id "
			 	   + "WHERE c.projectId = ? ";
		
		if(seznamIdKonfiguraci != null && !seznamIdKonfiguraci.isEmpty()) 
			sql += " and c.id in ("+ Konstanty.getZnakyParametru(seznamIdKonfiguraci) +") "; 

		return getKonfigurace(sql, idProjekt, seznamIdKonfiguraci);
		
	}	

	/**
	 * Vrací seznam konfigurací patřících osobě s id v parametru a splňující podmínky filtru výběru
	 * @param idOsoba id osoby pro výběr konfigurací
	 * @param seznamIdKonfiguraci seznam povolených konfigurací
	 * @return seznam konfigurací
	 */	
	public ArrayList<Konfigurace> getKonfiguraceOsoba(int idOsoba, ArrayList<Integer> seznamIdKonfiguraci) {
		String sql = "SELECT c.id, t.identifier as name, "
				   + "coalesce(m.committed, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as committed "
			 	   + "FROM configuration c "
			 	   + "left join committed_configuration m on c.id = m.id "
			 	   + "left join commit t on m.id = t.id "
			 	   + "left join configuration_person_relation r on t.id = r.configurationId "
			 	   + "WHERE r.personId = ?";
		
		if(seznamIdKonfiguraci != null && !seznamIdKonfiguraci.isEmpty()) 
			sql += " and c.id in ("+ Konstanty.getZnakyParametru(seznamIdKonfiguraci) +")"; 

		return getKonfigurace(sql, idOsoba, seznamIdKonfiguraci);
		
	}	

	/**
	 * Spustí zadaný skript a naplní seznam položek větvemi nebo tagy
	 * @param sql skript pro spuštění
	 * @return seznam položek větví nebo tagů
	 */
	private ArrayList<Polozka> getPolozka(String sql, int idKonfigurace) {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Polozka> listPolozek = new ArrayList<Polozka>();
		try {
			stmt = pripojeni.prepareStatement(sql);
			stmt.setInt(1, idKonfigurace);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				listPolozek.add(new Polozka(rs.getInt("id"), rs.getString("name")) );
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu větví nebo tagů konfigurací!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru dat větví nebo tagů konfigurací z databáze!");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listPolozek;
	}

	/**
	 * Spustí zadaný skript a naplní seznam konfigurací
	 * @param sql skript pro spuštění
	 * @return seznam konfigurací
	 */
	private ArrayList<Konfigurace> getKonfigurace(String sql, int id, ArrayList<Integer> seznamIdKonfiguraci) {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Konfigurace> listKonfigurace = new ArrayList<Konfigurace>();
		try {
			stmt = pripojeni.prepareStatement(sql);
			
			int i = 1;
			stmt.setInt(i++, id);			
			if(seznamIdKonfiguraci != null && !seznamIdKonfiguraci.isEmpty()){ 
				for(int x = 0; x < seznamIdKonfiguraci.size(); x++)
					stmt.setInt(i++, seznamIdKonfiguraci.get(x));
			} 

			rs = stmt.executeQuery();
			while(rs.next()){
				listKonfigurace.add(new Konfigurace(rs.getInt("id"), 
													rs.getString("name"), 
													rs.getDate("committed").toLocalDate()) );
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu konfigurací!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru dat konfigurací z databáze!");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listKonfigurace;
		
	}

}
