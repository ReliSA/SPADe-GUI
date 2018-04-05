package data.ciselnik;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída osob zděděná z třídy Ciselnik
 */
public class Osoby extends Ciselnik{
	
	private static final long serialVersionUID = -6288320387652282637L;

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr osob
	 */
	public Osoby(int idProjekt) {
		super(Konstanty.PRIRAZEN, idProjekt);
	}

	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> osoby = ciselnik.getOsoby(idProjekt);
			for(int i = 0; i < osoby.size(); i++){
				this.addPolozkaCiselniku(osoby.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniCiselnikOsob"));
			e.printStackTrace();
		}		
	}
}
