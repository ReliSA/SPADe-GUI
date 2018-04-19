package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import data.custom.CustomGraf;
import ostatni.Konstanty;

/**
 * Třída zajišťující výběr dat pro custom grafy z databáze
 */
public class CustomGrafDAO {
	private Connection pripojeni; // připojení k databázi

	/**
	 * Konstruktor třídy
	 */
	public CustomGrafDAO() {
		this.pripojeni = Konstanty.PRIPOJENI; // nastaví připojení uložené ve třídě Konstanty
	}

	/**
	 * Nastaví parametry sql a spustí zadaný script
	 * @param sql sql pro spuštění
	 * @param idProjektu id projektu
	 * @param idIterace id iterace
	 * @param idOsoba id osoby
	 * @param parametrySQL parametry potřebné pro SQL
	 * @return CustomGraf data pro custom graf
	 */
	public CustomGraf getCustomGrafData(String sql, int idProjektu, int idIterace, int idOsoba, int parametrySQL) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CustomGraf data = null;
		String dotaz = Konstanty.SQLs.getProperty(sql);

		try {

			stmt = pripojeni.prepareStatement(dotaz);

			if (parametrySQL == 3) {
				stmt.setInt(1, idProjektu);
				stmt.setInt(2, idIterace);
				stmt.setInt(3, idOsoba);
			} else if (parametrySQL == 2) {
				stmt.setInt(1, idProjektu);
				stmt.setInt(2, idOsoba);
			} else if (parametrySQL == 1) {
				stmt.setInt(1, idProjektu);
				stmt.setInt(2, idIterace);
			} else {
				stmt.setInt(1, idProjektu);
			}

			boolean isResultSet = stmt.execute();
			while (true) {
				if (isResultSet) {
					rs = stmt.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					data = new CustomGraf(columnsNumber);
					rs.next();
					for (int i = 1; i <= columnsNumber; i++) {
						data.addNazvySloupcu(rs.getString(i));
					}

					while (rs.next()) {
						data.addDatum(rs.getString(1));
						for (int i = 2; i <= columnsNumber; i++) {
							data.addData(i, rs.getDouble(i));
						}
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
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaScriptCustom"));
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaScriptCustom"));
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

}
