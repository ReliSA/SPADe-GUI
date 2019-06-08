package databaze;

import org.apache.log4j.Logger;
import ostatni.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída poskytující data z pohledů, implementuje rozhraní IViewDAO
 *
 * @author Patrik Bezděk
 */
public class ViewDAO implements IViewDAO {
    private Connection pripojeni;					//připojení k databázi
    static Logger log = Logger.getLogger(ViewDAO.class);

    public ViewDAO(){
        this.pripojeni = Konstanty.PRIPOJENI;		//nastaví připojení uložené ve třídě Konstanty
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
        String logHeader = "\n==============================\nX axis: Person" + "\n==============================\n";
        try {
            query = "SELECT\n\tDISTINCT name\nFROM\n\tpersonView\nwhere\n\tprojectId = " + projektId + "\nORDER BY\n\tname ASC";
            log.info(logHeader + query);
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

    public List<Column> getViewStructure(String viewName){
        List<Column> columns = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query;
        String logHeader = "\n==============================\nData: Structure for " + viewName + "\n==============================\n";
        try {
            query = "SELECT\n\tCOLUMN_NAME, DATA_TYPE\nFROM\n\tINFORMATION_SCHEMA.COLUMNS\nWHERE\n\tTABLE_NAME='" + viewName + "'";
            log.info(logHeader + query);
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                columns.add(new Column(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE")) );
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
        return columns;
    }
}
