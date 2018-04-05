package gui;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import ostatni.Konstanty;
/**
 * Dialogové okno zobrazující progres načítání dat
 */
public class OknoProgresNacitani extends JDialog{
	private JProgressBar pbProgres = new JProgressBar(0, Konstanty.POCET_KROKU_PROGRESU); 	//progres načítání
    private JLabel lblPopis = new JLabel();													//popisek s textem, co se v dané chvíli načítá
    private JLabel lblProcent = new JLabel();												//popisek zobrazující procenta načítání
	
    /**
     * Konstruktor pro spuštění okna bez rodiče (z okna Přihlášení)
     */
    public OknoProgresNacitani(){
    	super();
    	nastavZobrazeni();
    }
    
    /**
     * Konstruktor pro spuštění okna s rodičem (z Hlavního okna)
     * @param rodic vlastník okna
     */
	public OknoProgresNacitani(Component rodic){
		super(SwingUtilities.windowForComponent(rodic));
		nastavZobrazeni();	    
	}
	
	/**
	 * Nastaví zobrazení okna
	 */
	private void nastavZobrazeni(){
		this.setTitle(Konstanty.POPISY.getProperty("titleProgres"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lblNadpis = new JLabel((Konstanty.POPISY.getProperty("nadpisProgres")));
		lblNadpis.setFont(Konstanty.FONT_NADPIS);
		panel.add(lblNadpis);
		panel.add(lblPopis);
		panel.add(lblProcent);
		panel.add(pbProgres);
		this.add(panel);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.setSize(Konstanty.VELIKOST_NACITACIHO_OKNA);
	    this.setLocationRelativeTo(null);
	    
	    this.setVisible(true);

	}
	
	/**
	 * Nastavení progresu načítání
	 */
	public void nastavProgres(){
		this.pbProgres.setValue(Konstanty.CITAC_PROGRESU);	//progres se nastavuje podle čítače ve třídě Konstanty
		String text = "";
		switch (Konstanty.CITAC_PROGRESU){					//podle velikosti čítače se zobrazuje text, co se načítá
		case 0:  text = Konstanty.POPISY.getProperty("nacitaniUkolu");
			     break;
		case 1:  text = Konstanty.POPISY.getProperty("nacitaniFazi");
		   	     break;
		case 2:  text = Konstanty.POPISY.getProperty("nacitaniIteraci");
		   	     break;
		case 3:	 text = Konstanty.POPISY.getProperty("nacitaniAktivit");
		         break;
		case 4:  text = Konstanty.POPISY.getProperty("nacitaniOsob");
		         break;
		case 5:  text = Konstanty.POPISY.getProperty("nacitaniKonfiguraci");
		         break;
		case 6:  text = Konstanty.POPISY.getProperty("nacitaniArtefaktu");
		       	 break;
		default: text = Konstanty.POPISY.getProperty("nacitaniDat");
		}
		
		this.lblPopis.setText(text);
		this.lblProcent.setText(Konstanty.POPISY.getProperty("textHotovo") + ": " + Math.round(this.pbProgres.getPercentComplete() * 100) + "%");	//zobrazuje procenta načítání
	}
	
}
