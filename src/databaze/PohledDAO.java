package databaze;

import gui.SloupecCustomGrafu;
import ostatni.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída vrací struktury databázových pohledů a implementuje rozhraní IPohledDAO
 */
public class PohledDAO {
    private Connection pripojeni;					//připojení k databázi
    private static String name = "";
    private static String pass = "";

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

    public List<SloupecCustomGrafu> dotaz(String query, List<ComboBoxItem> preparedVariableValues){
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        List<String> nazvySloupcu = new ArrayList<>();
        List<SloupecCustomGrafu> sloupce = new ArrayList<>();


        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement(query);

//            rs = stmt.executeQuery();
//            while(rs.next()){
//
//            }

            boolean isResultSet = stmt.execute();
            while (true) {
                if (isResultSet) {
                    rs = stmt.getResultSet();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    for (int i = 1; i <= columnsNumber; i++) {
                        nazvySloupcu.add(rsmd.getColumnName(i));
                    }
                    for (int i = 0; i < columnsNumber; i++) {
                        data.add(new ArrayList<String>());
                    }
                    
                    while(rs.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
//                        data.addNazvySloupcu(rs.getString(i));
                            System.out.println(rs.getString(i));
                            if(rs.getString(i).equals("")){
                                data.get(i-1).add("N/A");
                            } else {
                                data.get(i - 1).add(rs.getString(i));
                            }
                        }
                        System.out.println("------------------------------");
//                    data = new CustomGraf(columnsNumber);
                    }
                    for (int i = 0; i < columnsNumber; i++) {
                        sloupce.add(new SloupecCustomGrafu(nazvySloupcu.get(i), data.get(i), i, preparedVariableValues, true));
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
        return sloupce;
    }

    public Long createVariable(String query){
        Long result = null;
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement(query);

            rs = stmt.executeQuery();
            while(rs.next()){
                result = rs.getLong(1);
                if(rs.wasNull()){
                    result = null;
                }
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
        return result;
    }

    public List<Sloupec> nactecniStrukturyPohledu(String viewName){
        List<Sloupec> sloupce = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='" + viewName + "'");

            rs = stmt.executeQuery();
            while(rs.next()){
                sloupce.add(new Sloupec(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE")) );
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
        return sloupce;
    }

    public List<ArtifactView> nactiArtifactView(String query){
        List<ArtifactView> artifactViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from artifactView");

            rs = stmt.executeQuery();
            while(rs.next()){
                artifactViews.add(new ArtifactView(rs.getString("workItemName"), rs.getString("description"),
                        rs.getTimestamp("created"), rs.getString("url"), rs.getString("artifactClass"), rs.getString("mimeType"),
                        rs.getLong("size"), rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass")) );
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
        return artifactViews;
    }

    public List<CommitView> nactiCommitView(String query){
        List<CommitView> commitViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from commitView");

            rs = stmt.executeQuery();
            while(rs.next()){
                commitViews.add(new CommitView(rs.getString("workItemType"), rs.getString("workItemDesc"),
                        rs.getTimestamp("created"), rs.getString("url"), rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass"), rs.getString("name"),
                        rs.getString("description"), rs.getTimestamp("committed"), rs.getBoolean("isRelease"), rs.getString("tagName"), rs.getString("branchName"),
                        rs.getBoolean("isMain"), rs.getLong("projectId")));
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
        return commitViews;
    }

    public List<CommitedConfigView> nactiCommitedConfigView(String query){
        List<CommitedConfigView> commitedConfigViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from commitedConfigView");

            rs = stmt.executeQuery();
            while(rs.next()){
                commitedConfigViews.add(new CommitedConfigView(rs.getString("workItemType"), rs.getString("workItemName"), rs.getString("workItemDesc"),
                        rs.getTimestamp("created"), rs.getLong("authorId"), rs.getString("authorName"), rs.getString("authorRoleName"), rs.getString("authorClass"), rs.getString("authorSuperClass"),
                        rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass"),
                        rs.getString("name"), rs.getTimestamp("committed"), rs.getLong("projectId")) );
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
        return commitedConfigViews;
    }

    public List<ConfigurationView> nactiConfigurationView(String query){
        List<ConfigurationView> configurationViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from configurationView");

            rs = stmt.executeQuery();
            while(rs.next()){
                configurationViews.add(new ConfigurationView(rs.getString("workItemType"), rs.getString("workItemName"), rs.getString("workItemDesc"),
                        rs.getTimestamp("created"), rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass"),
                        rs.getString("name"), rs.getLong("projectId")) );
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
        return configurationViews;
    }

    public List<FieldChangeView> nactiFieldChangeView(String query){
        List<FieldChangeView> fieldChangeViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from fieldChangeView");

            rs = stmt.executeQuery();
            while(rs.next()){
                fieldChangeViews.add(new FieldChangeView(rs.getString("fieldChangeName"), rs.getLong("fieldChangeId"), rs.getString("newValue"),
                        rs.getString("oldValue"), rs.getLong("changedWorkItemId"), rs.getString("workItemChangeName"), rs.getString("workItemChangeDesc"),
                        rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass")));
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
        return fieldChangeViews;
    }

    public List<WorkUnitView> nactiWorkUnitView(String query){
        List<WorkUnitView> workUnitViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from workUnitView");

            rs = stmt.executeQuery();
            while(rs.next()){
                workUnitViews.add(new WorkUnitView(rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass"),
                        rs.getLong("assigneeId"), rs.getString("assigneeName"), rs.getString("assigneeRoleName"), rs.getString("assigneeClass"), rs.getString("assigneeSuperClass"), rs.getDate("dueDate"),
                        rs.getDouble("estimatedTime"), rs.getInt("progress"), rs.getDouble("spentTime"),
                        rs.getDate("startDate"), rs.getString("activityName"), rs.getString("activityDesc"), rs.getDate("activityEndDate"),
                        rs.getDate("activityStartDate"), rs.getString("iterationName"), rs.getString("iterationDesc"),
                        rs.getDate("iterationStartDate"), rs.getDate("iterationEndDate"), rs.getTimestamp("iterationCreated"),
                        rs.getString("phaseName"), rs.getString("phaseDesc"), rs.getDate("phaseStartDate"), rs.getDate("phaseEndDate"),
                        rs.getTimestamp("phaseCreated"), rs.getString("priorityName"), rs.getString("priorityDesc"), rs.getString("prioClass"),
                        rs.getString("prioSuperClass"), rs.getString("severityName"), rs.getString("severityDesc"), rs.getString("severityClass"),
                        rs.getString("severitySuperClass"), rs.getString("resolutionName"), rs.getString("resolutionDescription"),
                        rs.getString("resolutionClass"), rs.getString("resolutionSuperClass"), rs.getString("statusName"), rs.getString("statusDescription"),
                        rs.getString("statusClass"), rs.getString("statusSuperClass"), rs.getString("wuTypeName"), rs.getString("wuTypeDescription"),
                        rs.getString("wuTypeClass"), rs.getString("categoryName"), rs.getString("categoryDesc")));
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
        return workUnitViews;
    }

    public List<PersonView> nactiPersonView(String query){
        List<PersonView> personViews = new ArrayList<>();
        Connection pripojeni;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, name, pass);
            stmt = pripojeni.prepareStatement("select * from personView");

            rs = stmt.executeQuery();
            while(rs.next()){
                personViews.add(new PersonView(rs.getLong("personId"), rs.getString("personName"), rs.getString("roleName"), rs.getString("roleClass"), rs.getString("roleSuperClass")));
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
        return personViews;
    }
}
