
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
			Konstanty.POPISY.load(new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU), "UTF8"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null , "Nepodařilo se nalézt soubor "+ Konstanty.NAZEV_SOUBORU_POPISU +" nutný k načtení popisů! Aplikace se nespustí!");			
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null , "Chyba při načítání souboru "+ Konstanty.NAZEV_SOUBORU_POPISU +" nutný k načtení popisů! Aplikace se nespustí!");			
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null , "Chyba při spuštění aplikace! Aplikace se ukončí!");			
			e.printStackTrace();
			System.exit(0);
		}
		new OknoPrihlasovani();
	}	
}
