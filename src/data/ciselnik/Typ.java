package data.ciselnik;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída typů zděděná z třídy Ciselnik
 * @author michalvselko
 *
 */
public class Typ extends Ciselnik{

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr typů
	 */
	public Typ(int idProjekt) {
		super(Konstanty.TYP, idProjekt);
	}

	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> typy = ciselnik.getTyp(idProjekt);
			for(int i = 0; i < typy.size(); i++){
				this.addPolozkaCiselniku(typy.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data číselníku typů! Zkuste znovu načíst projekt a filtr typů.");
			e.printStackTrace();
		}		
	}
}
