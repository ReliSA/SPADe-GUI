package data.ciselnik;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída severit zděděná z třídy Ciselnik
 * @author michalvselko
 *
 */
public class Severity extends Ciselnik{

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr severit
	 */
	public Severity(int idProjekt){
		super(Konstanty.SEVERITY, idProjekt);
	}
	
	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> severity = ciselnik.getSeverity(idProjekt);
			for(int i = 0; i < severity.size(); i++){
				this.addPolozkaCiselniku(severity.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniSeverit"));
			e.printStackTrace();
		}		
	}

}
