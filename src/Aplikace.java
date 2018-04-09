
import gui.OknoPrihlasovani;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import ostatni.Konstanty;
import ostatni.Ukladani;

/**
 * Spuštění aplikace
 */
public class Aplikace {

	public static void main(String[] args) {
		try {			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Konstanty.POPISY.load(new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH), "UTF8"));
			Konstanty.SQLs.load(new InputStreamReader(new FileInputStream(Konstanty.SQLSoubor), "UTF8"));
			Konstanty.SQLsVar.load(new InputStreamReader(new FileInputStream(Konstanty.SQLVarSoubor), "UTF8"));
			Konstanty.nastavPopisComboBox();
			Ukladani.load();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaSoubor")+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH +Konstanty.POPISY.getProperty("chybaSoubor2"));			
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniSouboru2")+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH +Konstanty.POPISY.getProperty("chybaNacitaniSouboru2"));			
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNespusti"));			
			e.printStackTrace();
			System.exit(0);
		}
		new OknoPrihlasovani();
	}	
}
