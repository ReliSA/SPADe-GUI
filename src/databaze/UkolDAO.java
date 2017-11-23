package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.Ukol;

/**
 * Třída zajišťující výběr dat úkolů z databáze implementující rozhraní IUkolDAO
 * @author michalvselko
 *
 */
public class UkolDAO implements IUkolDAO {

	private Connection pripojeni;					//připojení k databázi
	
	public UkolDAO(){
		this.pripojeni = Konstanty.PRIPOJENI;		//nastaví připojení uložené ve třídě Konstanty
	}

	/**
	 * Vrací seznam úkolů patřících do projektu s id v parametru
	 * @param idProjekt id projektu pro výběr úkolů
	 * @return seznam úkolů
	 */	
	public ArrayList<Ukol> getUkolProjekt(int idProjekt) {
		//String podminka = "WHERE w.projectId = " + idProjekt;
		//return getUkol(podminka, null, null, null, null, null, null, null);
		String podminka = "WHERE w.projectId = ?";
		return getUkol(podminka, idProjekt, null, null, null, null, null, null, null);
	}

	/**
	 * Vrací seznam úkolů patřících do fáze s id v parametru
	 * @param idFaze id fáze pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolFaze(int idFaze) {
		String podminka = "WHERE w.phaseId = ?";
		return getUkol(podminka, idFaze, null, null, null, null, null, null, null);
	}

	/**
	 * Vrací seznam úkolů patřících do iterace s id v parametru
	 * @param idIterace id iterace pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolIterace(int idIterace) {
		String podminka = "WHERE w.iterationId = ?";
		return getUkol(podminka, idIterace, null, null, null, null, null, null, null);
	}

	/**
	 * Vrací seznam úkolů patřících do aktivity s id v parametru
	 * @param idAktivity id aktivity pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolAktivity(int idAktivity) {
		String podminka = "WHERE w.activityId = ?";
		return getUkol(podminka, idAktivity, null, null, null, null, null, null, null);
	}

	/**
	 * Vrací seznam úkolů patřících osobě s id v parametru
	 * @param idOsoby id osoby pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolOsoba(int idOsoby) {
		String podminka = "WHERE w.assigneeId = ?";
		return getUkol(podminka, idOsoby, null, null, null, null, null, null, null);
	}
	
	/**
	 * Vrací seznam úkolů patřících do artefaktu s id v parametru
	 * @param idArtefakt id artefaktu pro výběr úkolů
	 * @return seznam úkolů
	 */
	public ArrayList<Ukol> getUkolArtefakt(int idArtefakt) {
		String podminka = "left join artifact a on w.id = a.id "
						+ "WHERE a.id = ?";
		return getUkol(podminka, idArtefakt, null, null, null, null, null, null, null);
	}

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
	public ArrayList<Ukol> getUkolProjekt(int idProjekt, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			 											 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "WHERE w.projectId = ?";				
		return getUkol(podminka, idProjekt, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}

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
			 									   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "WHERE w.phaseId = ?";
		return getUkol(podminka, idFaze, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}

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
														 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "WHERE w.iterationId = ?";
		return getUkol(podminka, idIterace, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}

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
														   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "WHERE w.activityId = ?";
		return getUkol(podminka, idAktivity, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}

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
			   										 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "WHERE w.assigneeId = ?";
		return getUkol(podminka, idOsoby, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}

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
				 										   ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {

		String podminka = "left join artifact a on w.id = a.id "
						+ "WHERE a.id = ?";
		return getUkol(podminka, idArtefakt, seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob);
	}
	
	/**
	 * Vytvoří a spustí skript - pokud některý seznam z parametrů není prázdný, doplní příslušnou podmínku
	 * @param podminka podmínka pro dotaz skriptu
	 * @param id identifikator polozky
	 * @param seznamIdUkolu seznam id povolených ukolů
	 * @param seznamIdPriorit seznam id povolených priorit
	 * @param seznamIdSeverit seznam id povolených severit
	 * @param seznamIdResoluci seznam id povolených resolucí
	 * @param seznamIdStatusu seznam id povolených statusů
	 * @param seznamIdTypu seznam id povolených typů
	 * @param seznamIdOsob seznam id povolených osob
	 * @return
	 */
	private ArrayList<Ukol> getUkol(String podminka, int id, ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, ArrayList<Integer> seznamIdResoluci, 
			 										 		 ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, ArrayList<Integer> seznamIdOsob) {		
		PreparedStatement stmt = null;
		ResultSet rs = null;		

		if(seznamIdUkolu != null && !seznamIdUkolu.isEmpty())
			podminka += " and w.id in (" + Konstanty.getZnakyParametru(seznamIdUkolu) + ")"; 		
		
		if(seznamIdPriorit != null && !seznamIdPriorit.isEmpty()) 
			podminka += " and w.priorityId in (" + Konstanty.getZnakyParametru(seznamIdPriorit) + ")"; 

		if(seznamIdSeverit != null && !seznamIdSeverit.isEmpty()) 
			podminka += " and w.severityId in (" + Konstanty.getZnakyParametru(seznamIdSeverit) + ")"; 

		if(seznamIdResoluci != null && !seznamIdResoluci.isEmpty()) 
			podminka += " and w.resolutionId in (" + Konstanty.getZnakyParametru(seznamIdResoluci) + ")"; 

		if(seznamIdStatusu != null && !seznamIdStatusu.isEmpty()) 
			podminka += " and w.statusId in (" + Konstanty.getZnakyParametru(seznamIdStatusu) + ")"; 

		if(seznamIdTypu != null && !seznamIdTypu.isEmpty()) 
			podminka += " and w.wuTypeId in (" + Konstanty.getZnakyParametru(seznamIdTypu) + ")"; 
		
		if(seznamIdOsob != null && !seznamIdOsob.isEmpty()) 
			podminka += " and w.assigneeId in (" + Konstanty.getZnakyParametru(seznamIdOsob) + ")"; 
		
		ArrayList<Ukol> listUkoly = new ArrayList<Ukol>();
		try {
			String sql = "SELECT w.id, i.name, "
					   + "coalesce(i.created, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d') ) as created, "
					   + "coalesce(w.startDate, STR_TO_DATE('"+ Konstanty.DATUM_PRAZDNY + "', '%Y-%m-%d')) as startDate, "
					   + "w.estimatedTime,  "
					   + "w.spentTime, "
					   + "w.priorityId, w.severityId, w.statusId, w.wuTypeId, w.resolutionId, w.assigneeId, "
					   + "i.authorId "
					   + "FROM work_unit w "
					   + "left join work_item i on w.id = i.id "
					   + podminka;

			stmt = pripojeni.prepareStatement(sql);
			
			int i = 1;
			stmt.setInt(i++, id);
			if(seznamIdUkolu != null && !seznamIdUkolu.isEmpty()){ 
				for(int x = 0; x < seznamIdUkolu.size(); x++)
					stmt.setInt(i++, seznamIdUkolu.get(x));
			}
			
			if(seznamIdPriorit != null && !seznamIdPriorit.isEmpty()){ 
				for(int x = 0; x < seznamIdPriorit.size(); x++)
					stmt.setInt(i++, seznamIdPriorit.get(x));
			} 
				
			if(seznamIdSeverit != null && !seznamIdSeverit.isEmpty()){ 
				for(int x = 0; x < seznamIdSeverit.size(); x++)
					stmt.setInt(i++, seznamIdSeverit.get(x));
			} 
			
			if(seznamIdResoluci != null && !seznamIdResoluci.isEmpty()){ 
				for(int x = 0; x < seznamIdResoluci.size(); x++)
					stmt.setInt(i++, seznamIdResoluci.get(x));
			} 
			
			if(seznamIdStatusu != null && !seznamIdStatusu.isEmpty()){ 
				for(int x = 0; x < seznamIdStatusu.size(); x++)
					stmt.setInt(i++, seznamIdStatusu.get(x));
			} 
			
			if(seznamIdTypu != null && !seznamIdTypu.isEmpty()){ 
				for(int x = 0; x < seznamIdTypu.size(); x++)
					stmt.setInt(i++, seznamIdTypu.get(x));
			} 
			
			if(seznamIdOsob != null && !seznamIdOsob.isEmpty()){ 
				for(int x = 0; x < seznamIdOsob.size(); x++)
					stmt.setInt(i++, seznamIdOsob.get(x));
			} 
			
			rs = stmt.executeQuery();
			while(rs.next()){				
				Ukol ukol = new Ukol(rs.getInt("id"),
									 rs.getString("name"),
									 rs.getDate("created").toLocalDate(),
									 rs.getDate("startDate").toLocalDate(),
									 rs.getDouble("estimatedTime"),
									 rs.getDouble("spentTime"),
									 rs.getInt("priorityId"),
									 rs.getInt("severityId"),
									 rs.getInt("statusId"),
									 rs.getInt("wuTypeId"),
									 rs.getInt("resolutionId"),
									 rs.getInt("assigneeId"),
									 rs.getInt("authorId"));				
				listUkoly.add(ukol);
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění skriptu úkolů!");
			e.printStackTrace();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Chyba při výběru dat úkolů z databáze!");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listUkoly;
	}
	

}

