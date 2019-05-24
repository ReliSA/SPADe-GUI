
import gui.OknoPrihlasovani;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
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
			Konstanty.APP.load(new InputStreamReader(new FileInputStream(Konstanty.AppSoubor), "UTF8"));
			Konstanty.SQLs.load(new InputStreamReader(new FileInputStream(Konstanty.SQLSoubor), "UTF8"));
			Konstanty.SQLsVar.load(new InputStreamReader(new FileInputStream(Konstanty.SQLVarSoubor), "UTF8"));
			Locale.setDefault(new Locale ("en", "US"));
			Konstanty.nastavPopisComboBox();
			Ukladani.load();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null , "Unable to find file "+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH + " needed to load descriptions! The application will not start!");			
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null , "Error loading file"+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH + "needed to load descriptions! The application will not start!");			
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
