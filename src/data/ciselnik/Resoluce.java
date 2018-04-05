package data.ciselnik;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída resolucí zděděná z třídy Ciselnik
 */
public class Resoluce extends Ciselnik{

	private static final long serialVersionUID = -9053050661284648953L;

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr resolucí
	 */
	public Resoluce(int idProjekt) {
		super(Konstanty.RESOLUCE, idProjekt);
	}

	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> resoluce = ciselnik.getResoluce(idProjekt);
			for(int i = 0; i < resoluce.size(); i++){
				this.addPolozkaCiselniku(resoluce.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniCiselnikResoluce"));
			e.printStackTrace();
		}		
	}
}
