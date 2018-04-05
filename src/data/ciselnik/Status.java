package data.ciselnik;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import databaze.CiselnikyDAO;
import databaze.ICiselnikyDAO;

/**
 * Třída statusů zděděná z třídy Ciselnik
 */
public class Status extends Ciselnik{

	private static final long serialVersionUID = -5769562217709388959L;

	/**
	 * Konstruktor číselníku
	 * @param idProjekt id projektu pro výběr statusů
	 */
	public Status(int idProjekt) {
		super(Konstanty.STATUS, idProjekt);
	}

	/**
	 * Načte položky z databáze a vloží je do číselníku
	 */
	public void nactiPolozky(int idProjekt) {
		try{
			ICiselnikyDAO ciselnik = new CiselnikyDAO();
			ArrayList<PolozkaCiselnik> statusy = ciselnik.getStatus(idProjekt);
			for(int i = 0; i < statusy.size(); i++){
				this.addPolozkaCiselniku(statusy.get(i));
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacitaniStatusu"));
			e.printStackTrace();
		}		
	}
	
	

}
