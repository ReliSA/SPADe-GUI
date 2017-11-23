package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;

/**
 * Třída zajišťující výběr priorit, severit, statusu, resoluci a osob z databáze implementující rozhraní ICiselnikyDAO
 * @author michalvselko
 *
 */
public class CiselnikyDAO implements ICiselnikyDAO {
	private Connection pripojeni;				//připojení k databázi
	
	/**
	 * Konstruktor třídy
	 */
	public CiselnikyDAO(){
		this.pripojeni = Konstanty.PRIPOJENI;	//nastaví připojení uložené ve třídě Konstanty
	}
	
	/**
	 * Vrací seznam priorit patřících do projektu
	 * @param idProjekt id projektu pro výběr priorit
	 * @return seznam priorit (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getPriority(int idProjekt){
		String sql =  "SELECT p.id, p.name, c.id as idTrida, c.class, c.superClass "
				 	+ "FROM priority p "
				 	+ "left join priority_classification c on p.classId = c.id "
				 	+ "left join project_instance i on p.projectInstanceId = i.id "
				 	+ "where i.projectId = ? "
				 	+ "order by p.classId ";
		return getData(sql, idProjekt);
	}
	
	/**
	 * Vrací seznam severit patřících do projektu
	 * @param idProjekt id projektu pro výběr severit
	 * @return seznam severit (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getSeverity(int idProjekt){
		String sql = "SELECT s.id, s.name, c.id as idTrida, c.class, c.superClass "
				   + "FROM severity s "
				   + "left join severity_classification c on s.classId = c.id "
				   + "left join project_instance i on s.projectInstanceId = i.id "
				   + "where i.projectId = ? "
				   + "order by s.classId ";
		return getData(sql, idProjekt);
	}

	/**
	 * Vrací seznam statusů patřících do projektu
	 * @param idProjekt id projektu pro výběr statusů
	 * @return seznam statusů (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getStatus(int idProjekt){
		String sql =  "SELECT s.id, s.name, c.id as idTrida, c.class, c.superClass "
				 	+ "FROM status s "
				 	+ "left join status_classification c on s.classId = c.id "
				 	+ "left join project_instance i on s.projectInstanceId = i.id "
				 	+ "where i.projectId = ? "
				 	+ "order by s.classId";
		return getData(sql, idProjekt);
	}

	/**
	 * Vrací seznam typů patřících do projektu
	 * @param idProjekt id projektu pro výběr typů
	 * @return seznam typů (třída PolozkaCiselnik)
	 */
	public ArrayList<PolozkaCiselnik> getTyp(int idProjekt){
		String sql =  "SELECT t.id, t.name, c.id as idTrida, c.class, \"\" as superClass "
				 	+ "FROM wu_type t "
				 	+ "left join wu_type_classification c on t.classId = c.id "
				 	+ "left join project_instance i on t.projectInstanceId = i.id "
				 	+ "where i.projectId = ? "
				 	+ "order by t.classId";
		return getData(sql, idProjekt);
	}

	/**
	 * Vrací seznam resolucí patřících do projektu
	 * @param idProjekt id projektu pro výběr resolucí
	 * @return seznam resolucí (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getResoluce(int idProjekt){
		String sql =  "SELECT r.id, r.name, c.id as idTrida, c.class, c.superClass "
				 	+ "FROM resolution r "
				 	+ "left join resolution_classification c on r.classId = c.id "
				 	+ "left join project_instance i on r.projectInstanceId = i.id "
				 	+ "where i.projectId = ? "
				 	+ "order by r.classId";
		return getData(sql, idProjekt);
	}
	
	/**
	 * Vrací seznam osob patřících do projektu
	 * @param idProjekt id projektu pro výběr osob
	 * @return seznam osob (třída PolozkaCiselnik)
	 */	
	public ArrayList<PolozkaCiselnik> getOsoby(int idProjekt){
		String sql =  "SELECT p.id, p.name, 0 as idTrida, \"\" as class, \"\" as superClass "
				 	+ "FROM person p "
				 	+ "where p.projectId = ? ";
		return getData(sql, idProjekt);
	}

	/**
	 * Spustí zadaný skript a naplní seznam položek
	 * @param sql skript pro spuštění
	 * @param idProjekt identifikátor projektu
	 * @return seznam položek PolozkyCiselnik
	 */
	private ArrayList<PolozkaCiselnik> getData(String sql, int idProjekt){ 
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<PolozkaCiselnik> listPolozek = new ArrayList<PolozkaCiselnik>();
		try {
			stmt = pripojeni.prepareStatement(sql);
			stmt.setInt(1, idProjekt);
			rs = stmt.executeQuery();
			while(rs.next()){
				listPolozek.add(new PolozkaCiselnik(rs.getInt("id"), rs.getString("name"), rs.getInt("idTrida"), rs.getString("class"), rs.getString("superClass") ));
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu číselníků!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru dat číselníků z databáze!");
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
}
