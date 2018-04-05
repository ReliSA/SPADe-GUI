package data.ciselnik;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída priorit zděděná z třídy Ciselnik
 */
public class Priority extends Ciselnik{

	private static final long serialVersionUID = -3576657021794701207L;

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
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniCiselnikPriorit"));
			e.printStackTrace();
		}		
	}

}
