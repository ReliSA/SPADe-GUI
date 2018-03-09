package gui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;
import data.polozky.PolozkaPocatek;
import data.polozky.PolozkaVytvoreni;

/**
 * Panel filtrů pro artefakty a konfigurace zděděný z PanelFiltr
 * @author michalvselko
 *
 */
public class PanelFiltrPolozkaVytvoreni extends PanelFiltr{

	private JComboBox<String> cbOperatorDatum;	//seznam operatoru pro datum					
	private JDatePickerImpl dpDatumOD;			//datum od
    private JDatePickerImpl dpDatumDO;			//datum do
    
    /**
     * Konstruktor třídy
     * @param nazev název panelu filtru
     * @param seznam seznam položek
     */
	public PanelFiltrPolozkaVytvoreni(String nazev, ArrayList seznam) {
		super(seznam);
		nastavPanel(nazev);
	}
	
   /**
    * Vrací seznam id odpovídajících zadaným podmínkám
    * @return seznam id odpovídajících zadaným podmínkám
    */	
	public ArrayList<Integer> getSeznamId(){		
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		
		if(jePouzit()){																//musí být zaškrtnuto Použít filtr
			for(int i = 0; i < this.seznam.size(); i++){							//projde všechny položky seznamu
				PolozkaVytvoreni polozka = (PolozkaVytvoreni)this.seznam.get(i);
				
				switch(cbOperatorDatum.getSelectedItem().toString()){				//podle zadaného typu operátoru volá konkrétní kontrolní metodu
				case "mezi":if(vlozitDoSeznamuMezi(polozka))
								seznamId.add(polozka.getID());						//pokud metoda vrátí true, vloží se id do seznamu
							break;
				case "between":if(vlozitDoSeznamuMezi(polozka))
					seznamId.add(polozka.getID());									//pokud metoda vrátí true, vloží se id do seznamu
				break;
				case ">":	if(vlozitDoSeznamuVetsi(polozka))
								seznamId.add(polozka.getID());
							break;
				case ">=":	if(vlozitDoSeznamuVetsiRovno(polozka))
								seznamId.add(polozka.getID());
							break;
				case "=":	if(vlozitDoSeznamuRovno(polozka))
								seznamId.add(polozka.getID());
							break;
				case "!=":	if(vlozitDoSeznamuNerovno(polozka))
								seznamId.add(polozka.getID());
							break;
				case "<=":	if(vlozitDoSeznamuMensiRovno(polozka))
								seznamId.add(polozka.getID());
							break;
				case "<":	if(vlozitDoSeznamuMensi(polozka))
								seznamId.add(polozka.getID());
							break;
				}				
			}						
		}		
		if(seznamId.isEmpty())		//pokud je seznam prázdný doplním alespoň -1 jinak by při načítání dat seznam jevil jako nepoužitý
			seznamId.add(-1);
		return seznamId;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "mezi"
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMezi(PolozkaVytvoreni polozka){
		try{
			if(dpDatumOD.getModel().getValue() != null && dpDatumDO.getModel().getValue() != null){
				LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate datumDo = ((Date)dpDatumDO.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				if(jeVSeznamu(polozka) && (polozka.getDatumVytvoreni().isAfter(datumOd) || polozka.getDatumVytvoreni().isEqual(datumOd) )  
									   && (polozka.getDatumVytvoreni().isBefore(datumDo) || polozka.getDatumVytvoreni().isEqual(datumDo) )){
					return true;
				}
			} else{
				if(jeVSeznamu(polozka))
					return true;
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaFiltrVytvoreni"));
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">"
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsi(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumVytvoreni().isAfter(datumOd)){
				return true;
			}
		} else{
			if(jeVSeznamu(polozka)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">="
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsiRovno(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && (polozka.getDatumVytvoreni().isAfter(datumOd) || polozka.getDatumVytvoreni().isEqual(datumOd)))
				return true;
		} else{
			if(jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "="
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuRovno(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumVytvoreni().isEqual(datumOd))
				return true;
		} else{
			if(jeVSeznamu(polozka))
				return true;
		}
		return false;
	}	

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "!="
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuNerovno(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && !polozka.getDatumVytvoreni().isEqual(datumOd))
				return true;
		} else{
			if(jeVSeznamu(polozka))
				return true;
		}
		return false;
	}	

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<="
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensiRovno(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && (polozka.getDatumVytvoreni().isBefore(datumOd) || polozka.getDatumVytvoreni().isEqual(datumOd)))
				return true;
		} else{
			if(jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<"
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensi(PolozkaVytvoreni polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumVytvoreni().isBefore(datumOd)){
				return true;
			}
		} else{
			if(jeVSeznamu(polozka)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Zkontroluje, zda je položka v seznamu vybraných
	 * @param polozka kontrolovaná položka
	 * @return true, pokud je položka v seznamu vybraných nebo nejsou vybrány žádné konkrétní položky
	 */
	private boolean jeVSeznamu(PolozkaVytvoreni polozka){		
		try{
			List seznam = this.lsSeznamFiltr.getSelectedValuesList();
			if(seznam.isEmpty() || seznam.contains(polozka))
				return true;
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaFiltrVytvoreni"));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Nastavení panelu, spustí se nastavení z předka a poté se do gridu přidávají nové komponenty
	 * @param nazev název filtru
	 */
	protected void nastavPanel(String nazev){
		super.nastavPanel(nazev);
		
		UtilDateModel modelDatumOD = new UtilDateModel();
		UtilDateModel modelDatumDO = new UtilDateModel();
        Properties p = new Properties();
		p.put("text.today", Konstanty.POPISY.getProperty("kalendarDnes"));
		p.put("text.month", Konstanty.POPISY.getProperty("kalendarMesic"));
		p.put("text.year", Konstanty.POPISY.getProperty("kalendarRok"));
	
		JDatePanelImpl datumODPanel = new JDatePanelImpl(modelDatumOD, p);		
		JDatePanelImpl datumDOPanel = new JDatePanelImpl(modelDatumDO, p);		
        dpDatumOD = new JDatePickerImpl(datumODPanel, new DateComponentFormatter());
        dpDatumDO = new JDatePickerImpl(datumDOPanel, new DateComponentFormatter());        
                
        JLabel lblDatum = new JLabel(Konstanty.POPISY.getProperty("popisDatumVytvoreni") + ": ");        		
        lblDatum.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        cbOperatorDatum.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        
        dpDatumOD.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
        dpDatumDO.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		
		GridBagConstraints grid = this.getGrid();
		grid.gridx = 5;
        grid.gridy = 0;
        grid.gridwidth = 2;
        this.add(lblDatum, grid);
        grid.gridx = 7;
        this.add(cbOperatorDatum, grid);
        grid.gridx = 5;
        grid.gridy++;     
        this.add(dpDatumOD, grid);
        grid.gridx = 7;
        this.add(dpDatumDO, grid);
        
        
	}
	
	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce(){
		/*Akce pro změně zaškrtávátka Použít filtr*/
		ActionListener actZmenaPouzitFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ckPouzitFiltr.isSelected()){
					lsSeznamFiltr.clearSelection();
					dpDatumOD.getModel().setValue(null);
					dpDatumDO.getModel().setValue(null);
				}
			}
		};
		
		cbOperatorDatum = new JComboBox<String>(Konstanty.POLE_OPERATORU);
		/*Akce pro změně operatoru datumů*/
		ActionListener actZmenaOperatoruDatum = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(cbOperatorDatum.getSelectedIndex() == 0)
					dpDatumDO.setVisible(true);
				else
					dpDatumDO.setVisible(false);
			}
		};
				
		ckPouzitFiltr.addActionListener(actZmenaPouzitFiltr);
		cbOperatorDatum.addActionListener(actZmenaOperatoruDatum);		
	}

}
