package databaze;

import data.polozky.Polozka;
import ostatni.Konstanty;
import ostatni.Sloupec;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída vrací struktury databázových pohledů a implementuje rozhraní IPohledDAO
 */
public class PohledDAO {
    private Connection pripojeni;					//připojení k databázi

    public PohledDAO(){
        this.pripojeni = Konstanty.PRIPOJENI;		//nastaví připojení uložené ve třídě Konstanty
    }

    public List<Sloupec> nactecniStrukturyPohledu(String viewName){
        List<Sloupec> sloupce = new ArrayList<>();
        Connection pripojeniTest;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            pripojeniTest = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, "bezdekp", "XXX");
            stmt = pripojeniTest.prepareStatement("select COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='" + viewName + "'");

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
}
