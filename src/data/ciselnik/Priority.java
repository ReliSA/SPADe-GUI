package data.ciselnik;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída priorit zděděná z třídy Ciselnik
 * @author michalvselko
 *
 */
public class Priority extends Ciselnik{

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr priorit
	 */
	public Priority(int idProjekt){
		super(Konstanty.PRIORITY, idProjekt);
	}
	
	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> priority = ciselnik.getPriority(idProjekt);
			for(int i = 0; i < priority.size(); i++){
				this.addPolozkaCiselniku(priority.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , "Nepodařilo se správně načíst data číselníku priorit! Zkuste znovu načíst projekt a filtr priorit.");
			e.printStackTrace();
		}		
	}

}
