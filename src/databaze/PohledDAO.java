package databaze;

import org.apache.log4j.Logger;
import ostatni.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída poskytující data z pohledů, implementuje rozhraní IPohledDAO
 */
public class PohledDAO implements IPohledDAO{
    private Connection pripojeni;					//připojení k databázi
    static Logger log = Logger.getLogger(PohledDAO.class);

    public PohledDAO(){
        this.pripojeni = Konstanty.PRIPOJENI;		//nastaví připojení uložené ve třídě Konstanty
    }

    public int getRows(ResultSet res){
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        }
        catch(Exception ex)  {
            return 0;
        }
        return totalRows ;
    }

    public List<List<String>> runQuery(String query){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<List<String>> data = new ArrayList<>();

        try {
            stmt = pripojeni.prepareStatement(query);

            boolean isResultSet = stmt.execute();
            while (true) {
                if (isResultSet) {
                    rs = stmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    for (int i = 0; i < columnsNumber; i++) {
                        data.add(new ArrayList<String>());
                    }
                    
                    while(rs.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            log.debug(rs.getString(i));
                            data.get(i - 1).add(rs.getString(i));
                        }
                        log.debug("------------------------------");
                    }

                    rs.close();
                } else {
                    if (stmt.getUpdateCount() == -1) {
                        break;
                    }
                }
                isResultSet = stmt.getMoreResults();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptCustom"));
            e.printStackTrace();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaData"));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public Long testVariable(String query){
        Long result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                result = rs.getLong(1);
                if(rs.wasNull()){
                    result = null;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptPromenna"));
            e.printStackTrace();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataPromenna"));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<String> getPeopleForProject(int projektId){
        ArrayList<String> result = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query;
        try {
            query = "SELECT DISTINCT name FROM personView where projectId = " + projektId + " ORDER BY name ASC";
            log.info(query);
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                result.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptOsoby"));
            e.printStackTrace();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataOsoby"));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<Iteration> getIterationsForProject(int projektId){
        ArrayList<Iteration> result = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query;
        try {
            query = "SELECT startDate, endDate, name FROM iteration WHERE superProjectId = " + projektId + " ORDER BY name ASC";
            log.info(query);
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                result.add(new Iteration(rs.getDate("startDate").toLocalDate(), rs.getDate("endDate").toLocalDate(), rs.getString("name")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptIterace"));
            e.printStackTrace();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataIterace"));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<Sloupec> getViewStructure(String viewName){
        List<Sloupec> sloupce = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query;
        try {
            query = "SELECT COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='" + viewName + "'";
            log.info(query);
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                sloupce.add(new Sloupec(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE")) );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaScriptStrukturyPohledu"));
            e.printStackTrace();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaDataStrukturyPohledu"));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sloupce;
    }
}
