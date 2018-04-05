package databaze;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import data.CustomGraf;
import ostatni.Konstanty;

public class CustomGrafDAO {
	private Connection pripojeni; // připojení k databázi

	public CustomGrafDAO() {
		this.pripojeni = Konstanty.PRIPOJENI; // nastaví připojení uložené ve třídě Konstanty
	}

	/**
	 * Spustí zadaný skript
	 */
	public CustomGraf getCustomGrafData(String sql, int idProjektu, int idIterace, int idOsoba, int var) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CustomGraf data = null;
		String dotaz = Konstanty.SQLs.getProperty(sql);

		try {

			stmt = pripojeni.prepareStatement(dotaz);

			if (var == 3) {
				stmt.setInt(1, idProjektu);
				stmt.setInt(2, idIterace);
				stmt.setInt(3, idOsoba);
			} else if (var == 2) {
				stmt.setInt(1, idProjektu);
				stmt.setInt(2, idOsoba);
			} else if (var == 1) {
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
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaScriptArtefakt"));
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaDataArtefakt"));
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
