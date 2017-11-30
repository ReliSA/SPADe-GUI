package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.Osoba;
import data.Projekt;
import data.Segment;

/**
 * Třída zajišťující výběr dat projektů z databáze implementující rozhraní IProjektDAO
 * @author michalvselko
 *
 */
public class ProjektDAO implements IProjektDAO {

	private Connection pripojeni;				//připojení k databázi
	
	/**
	 * Konstruktor třídy
	 */
	public ProjektDAO(){
		this.pripojeni = Konstanty.PRIPOJENI;	//nastaví připojení uložené ve třídě Konstanty
	}
	
	/**
	 * Vrací seznam projektů v databázi
	 * @return seznam projektů
	 */
	public ArrayList<Projekt> getProjekt(){
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Projekt> listProjektu = new ArrayList<Projekt>();
		try {
			stmt = pripojeni.prepareStatement("SELECT p.id, p.name, "
					 						+ "coalesce(p.startDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as startDate, "
					 						+ "coalesce(p.endDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as endDate "
					 						+ "FROM project p");
			rs = stmt.executeQuery();
			while(rs.next()){
				listProjektu.add(new Projekt(rs.getInt("id"), rs.getString("name"), rs.getDate("startDate").toLocalDate(), rs.getDate("endDate").toLocalDate()) );
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu projektů!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru projektů z databáze!");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listProjektu;
	}

	/**
	 * Vrací seznam fází patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr fází
	 * @return seznam fází (třída Segment)
	 */
	public ArrayList<Segment> getFaze(int idProjekt) {
		return getFaze(idProjekt, null);		
	}

	/**
	 * Vrací seznam iterací patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr iterací
	 * @return seznam iterací (třída Segment)
	 */	
	public ArrayList<Segment> getIterace(int idProjekt) {
		return getIterace(idProjekt, null);
	}

	/**
	 * Vrací seznam aktivit patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr aktivit
	 * @return seznam aktivit (třída Segment)
	 */	
	public ArrayList<Segment> getAktivity(int idProjekt) {
		return getAktivity(idProjekt, null);
	}

	/**
	 * Vrací seznam osob patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr osob
	 * @return seznam osob
	 */	
	public ArrayList<Osoba> getOsoby(int idProjekt) {
		return getOsoby(idProjekt, null);
	}

	/**
	 * Vrací seznam fází patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr fází
	 * @param seznamIdFazi seznam povolených fází
	 * @return seznam fází (třída Segment)
	 */	
	public ArrayList<Segment> getFaze(int idProjekt, ArrayList<Integer> seznamIdFazi) {
		String sql = "SELECT p.id, p.name, "
				   + Konstanty.FAZE + " as typSegmentu,"
				   + "coalesce(p.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as created, "
				   + "coalesce(p.startDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as startDate, "
				   + "coalesce(p.endDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as endDate "
		 		   + "FROM phase p "
		 		   + "WHERE p.superProjectId = ? ";
		
		if(seznamIdFazi != null && !seznamIdFazi.isEmpty()) 
			sql += " and p.id in ("+ Konstanty.getZnakyParametru(seznamIdFazi) +")"; 

		PreparedStatement stmt = null;
		try {
			stmt = pripojeni.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, idProjekt);
			if(seznamIdFazi != null && !seznamIdFazi.isEmpty()) { 
				for(int x = 0; x < seznamIdFazi.size(); x++)
					stmt.setInt(i++, seznamIdFazi.get(x));
			}
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při nastavení skriptu fází projektu!");
			e.printStackTrace();
		}
				
		return getSegmentData(stmt);
	}

	/**
	 * Vrací seznam iterací patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr iterací
	 * @param seznamIdIteraci seznam povolených iterací
	 * @return seznam iterací (třída Segment)
	 */	
	public ArrayList<Segment> getIterace(int idProjekt, ArrayList<Integer> seznamIdIteraci) {
		String sql = "SELECT i.id, i.name, "
				   + Konstanty.ITERACE + " as typSegmentu,"
				   + "coalesce(i.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as created, "
				   + "coalesce(i.startDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as startDate, "
				   + "coalesce(i.endDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as endDate "
	 	 		   + "FROM iteration i "
	 	 		   + "WHERE i.superProjectId = ? ";
		
		if(seznamIdIteraci != null && !seznamIdIteraci.isEmpty()) 
			sql += " and i.id in ("+ Konstanty.getZnakyParametru(seznamIdIteraci) +")"; 

		PreparedStatement stmt = null;
		try {
			stmt = pripojeni.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, idProjekt);
			if(seznamIdIteraci != null && !seznamIdIteraci.isEmpty()){ 
				for(int x = 0; x < seznamIdIteraci.size(); x++)
					stmt.setInt(i++, seznamIdIteraci.get(x));
			}
	
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při nastavení skriptu iterací projektu!");
			e.printStackTrace();
		}
		
		return getSegmentData(stmt);
	}

	/**
	 * Vrací seznam aktivit patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr aktivit
	 * @param seznamIdAktivit seznam povolených aktivit
	 * @return seznam aktivit (třída Segment)
	 */	
	public ArrayList<Segment> getAktivity(int idProjekt, ArrayList<Integer> seznamIdAktivit) {
		String sql = "SELECT a.id, a.name, "
				   + Konstanty.AKTIVITY + " as typSegmentu,"
				   + "STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') as created, "
				   + "coalesce(a.startDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as startDate, "
				   + "coalesce(a.endDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as endDate "
	 	 		   + "FROM activity a "
	 	 		   + "WHERE a.superProjectId = ?";
		
		if(seznamIdAktivit != null && !seznamIdAktivit.isEmpty()) 
			sql += " and a.id in ("+ Konstanty.getZnakyParametru(seznamIdAktivit) +")"; 

		PreparedStatement stmt = null;
		try {
			stmt = pripojeni.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, idProjekt);
			if(seznamIdAktivit != null && !seznamIdAktivit.isEmpty()){ 
				for(int x = 0; x < seznamIdAktivit.size(); x++)
					stmt.setInt(i++, seznamIdAktivit.get(x));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při nastavení skriptu aktivit projektu!");
			e.printStackTrace();
		}
		
		return getSegmentData(stmt);
	}
	
	/**
	 * Vrací seznam osob patřících do projektu s id v parametru a splňující podmínky filtru výběru
	 * @param idProjekt id projektu pro výběr osob
	 * @param seznamIdOsob seznam povolených osob
	 * @return seznam osob
	 */
	public ArrayList<Osoba> getOsoby(int idProjekt, ArrayList<Integer> seznamIdOsob) {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Osoba> listOsoba = new ArrayList<Osoba>();
		try {
			String sql = "SELECT p.id, p.name " +
						 "FROM person p " + 
						 "WHERE p.projectId = ? ";

			if(seznamIdOsob != null && !seznamIdOsob.isEmpty()) 
				sql += " and p.id in ("+ Konstanty.getZnakyParametru(seznamIdOsob) +") "; 

			stmt = pripojeni.prepareStatement(sql);

			int i = 1;
			stmt.setInt(i++, idProjekt);
			if(seznamIdOsob != null && !seznamIdOsob.isEmpty()){ 
				for(int x = 0; x < seznamIdOsob.size(); x++)
					stmt.setInt(i++, seznamIdOsob.get(x));
			}

			rs = stmt.executeQuery();
			while(rs.next()){
				listOsoba.add(new Osoba(rs.getInt("id"), 
										rs.getString("name")) );
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu osob projektu!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru dat osob projektu z databáze!");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listOsoba;
	}

	/**
	 * Spustí zadaný skript a naplní seznam segmentů
	 * @param stmt prepare statement pro spuštění
	 * @return seznam segmentů
	 */
	private ArrayList<Segment> getSegmentData(PreparedStatement stmt){ 
		ResultSet rs = null;		
		ArrayList<Segment> listSegment = new ArrayList<Segment>();
		try {
			rs = stmt.executeQuery();
			while(rs.next()){
				LocalDate datumKonce = rs.getDate("endDate").toLocalDate();
				if(datumKonce.isBefore(rs.getDate("startDate").toLocalDate()))
					datumKonce = rs.getDate("startDate").toLocalDate();
							
				listSegment.add(new Segment(rs.getInt("id"), 
											rs.getString("name"),
											rs.getInt("typSegmentu"),
											rs.getDate("created").toLocalDate(),
											rs.getDate("startDate").toLocalDate(),
											datumKonce));
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptSegment"));
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataSegment"));
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listSegment;
	}

}
