
import gui.OknoPrihlasovani;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ostatni.Konstanty;

/**
 * Spuštění aplikace
 * @author michalvselko
 *
 */
public class Aplikace {

	public static void main(String[] args) {
		try {			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Konstanty.POPISY.load(new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_CZECH), "UTF8"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaSoubor")+ Konstanty.NAZEV_SOUBORU_POPISU_CZECH +Konstanty.POPISY.getProperty("chybaSoubor2"));			
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniSouboru2")+ Konstanty.NAZEV_SOUBORU_POPISU_CZECH +Konstanty.POPISY.getProperty("chybaNacitaniSouboru2"));			
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
