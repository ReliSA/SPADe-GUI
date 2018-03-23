package databaze;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.Artefakt;

/**
 * Třída zajišťující výběr dat artefaktů z databáze implementující rozhraní IArtefaktDAO
 * @author michalvselko
 *
 */
public class ArtefaktDAO implements IArtefaktDAO{
	private Connection pripojeni;				//připojení k databázi
	
	/**
	 * Konstruktor třídy
	 */
	public ArtefaktDAO(){
		this.pripojeni = Konstanty.PRIPOJENI;	//nastaví připojení uložené ve třídě Konstanty
	}

	/**
	 * Vrací seznam artefaktů patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyProjekt(int idProjekt) {		
		return getArtefaktyProjekt(idProjekt, null);
	}

	/**
	 * Vrací seznam artefaktů patřících do konfigurace s id v parametru
	 * @param idKonfigurace id konfigurace pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyKonfigurace(int idKonfigurace) {
		return getArtefaktyKonfigurace(idKonfigurace, null);
	}
	
	/**
	 * Vrací seznam artefaktů patřících osobě s id v parametru
	 * @param idOsoby id osoby pro výběr artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyOsoba(int idOsoby) {
		return getArtefaktyOsoba(idOsoby, null);
	}

	/**
	 * Vrací seznam artefaktů patřících do projektu s id v parametru a splňující podmínky filtru artefaktů
	 * @param idProjekt id projektu pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyProjekt(int idProjekt, ArrayList<Integer> seznamIdArtefaktu) {		
		String sql = "SELECT a.id, i.name, "
				   + "coalesce(i.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as created, "
				   + "a.mimeType, a.size "
				   + "FROM artifact a "
				   + "left join work_item i on a.id = i.id "
				   + "left join person p on i.authorId = p.id "
				   + "WHERE p.projectId = ? ";
				
		if(seznamIdArtefaktu != null && !seznamIdArtefaktu.isEmpty()) 
			sql += " and a.id in ("+ Konstanty.getZnakyParametru(seznamIdArtefaktu) +")"; 

		return getArtefakty(sql, idProjekt, seznamIdArtefaktu);		
	}

	/**
	 * Vrací seznam artefaktů patřících do konfigurace s id v parametru a splňující podmínky filtru artefaktů
	 * @param idKonfigurace id konfigurace pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyKonfigurace(int idKonfigurace, ArrayList<Integer> seznamIdArtefaktu) {
		String sql = "SELECT a.id, i.name, "
				   + "coalesce(i.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as created, "
				   + "a.mimeType, a.size "
				   + "FROM artifact a "
				   + "left join work_item i on a.id = i.id "
				   + "left join work_item_change c on i.id = c.workItemId "
				   + "left join configuration_change g on c.id = g.changeId "
				   + "WHERE g.configurationId = ? ";

		if(seznamIdArtefaktu != null && !seznamIdArtefaktu.isEmpty()) 
			sql += " and a.id in ("+ Konstanty.getZnakyParametru(seznamIdArtefaktu) +")"; 
		return getArtefakty(sql, idKonfigurace, seznamIdArtefaktu);		
	}

	/**
	 * Vrací seznam artefaktů patřících osobě s id v parametru a splňující podmínky filtru výběru
	 * @param idOsoby id osoby pro výběr artefaktů
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	public ArrayList<Artefakt> getArtefaktyOsoba(int idOsoby, ArrayList<Integer> seznamIdArtefaktu) {
		String sql = "SELECT a.id, a.artifactClass as name, "
				   + "coalesce(i.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as created, "
				   + "a.mimeType, a.size "
				   + "FROM artifact a "
				   + "left join work_item i on a.id = i.id "
				   + "WHERE i.authorId = ? ";
		
		if(seznamIdArtefaktu != null && !seznamIdArtefaktu.isEmpty()) 
			sql += " and a.id in ("+ Konstanty.getZnakyParametru(seznamIdArtefaktu) +")"; 

		return getArtefakty(sql, idOsoby, seznamIdArtefaktu);
	}

	/**
	 * Spustí zadaný skript a naplní seznam artefaktů
	 * @param sql skript pro spuštění
	 * @param id identifikátor výběru
	 * @param seznamIdArtefaktu seznam povolených artefaktů
	 * @return seznam artefaktů
	 */
	private ArrayList<Artefakt> getArtefakty(String sql, int id, ArrayList<Integer> seznamIdArtefaktu) {		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Artefakt> listArtefakt = new ArrayList<Artefakt>();
		try {
			stmt = pripojeni.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, id);
			if(seznamIdArtefaktu != null && !seznamIdArtefaktu.isEmpty()){ 
				for(int x = 0; x < seznamIdArtefaktu.size(); x++)
					stmt.setInt(i++, seznamIdArtefaktu.get(x));
			}			
			
			rs = stmt.executeQuery();
			while(rs.next()){
				listArtefakt.add(new Artefakt(rs.getInt("id"), 
											  rs.getString("name"), 
											  rs.getDate("created").toLocalDate() , 
											  rs.getString("mimeType"),
											  rs.getInt("size")) );
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptArtefakt"));
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataArtefakt"));
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listArtefakt;		
	}

}
