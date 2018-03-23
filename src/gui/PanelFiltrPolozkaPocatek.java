package gui;

import java.awt.Color;
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
import javax.swing.ListSelectionModel;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ostatni.Konstanty;
import data.polozky.PolozkaPocatek;

/**
 * Panel filtrů pro úkoly a segmenty zděděný z PanelFiltr
 * @author michalvselko
 *
 */
public class PanelFiltrPolozkaPocatek extends PanelFiltr{

	private JComboBox<String> cbOperatorDatum;		//seznam operatoru pro datum				
	private JDatePickerImpl dpDatumOD;				//datum od
    private JDatePickerImpl dpDatumDO;				//datum do
    private JComboBox<String> cbTypPodfiltru;
	
    /**
     * Konstruktor třídy
     * @param nazev název panelu filtru
     * @param seznam seznam položek
     */
    public PanelFiltrPolozkaPocatek(String nazev, ArrayList seznam) {
		super(seznam);
		nastavPanel(nazev);
	}
    
    public PanelFiltrPolozkaPocatek(String nazev, ArrayList seznam,JComboBox<String> cbTypPodfiltru) {
		super(seznam);
		this.cbTypPodfiltru=cbTypPodfiltru;
		nastavPanel(nazev);
	}
	
    /**
     * Vrací seznam id odpovídajících zadaným podmínkám
     * @return seznam id odpovídajících zadaným podmínkám
     */
	public ArrayList<Integer> getSeznamId(){		
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		
		if(jePouzit()){															//musí být zaškrtnuto Použít filtr
			for(int i = 0; i < this.seznam.size(); i++){						//projde všechny položky seznamu
				PolozkaPocatek polozka = (PolozkaPocatek)this.seznam.get(i);
				switch(cbOperatorDatum.getSelectedItem().toString()){			//podle zadaného typu operátoru volá konkrétní kontrolní metodu
				case "mezi":if(vlozitDoSeznamuMezi(polozka))					
								seznamId.add(polozka.getID());					//pokud metoda vrátí true, vloží se id do seznamu
							break;
				case "between":if(vlozitDoSeznamuMezi(polozka))					
					seznamId.add(polozka.getID());								//pokud metoda vrátí true, vloží se id do seznamu
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
	private boolean vlozitDoSeznamuMezi(PolozkaPocatek polozka){
		try{
			/*Zkontroluje zda není datum od a do prázdné*/
			if(dpDatumOD.getModel().getValue() != null && dpDatumDO.getModel().getValue() != null){
				LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate datumDo = ((Date)dpDatumDO.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				/*zkontroluje se zda je položka v seznamu a zároveň je datum počátku mezi vybranými datumy*/
				if(jeVSeznamu(polozka) && (polozka.getDatumPocatku().isAfter(datumOd) || polozka.getDatumPocatku().isEqual(datumOd) )  
									   && (polozka.getDatumPocatku().isBefore(datumDo) || polozka.getDatumPocatku().isEqual(datumDo) )){
					return true;
				}
			} else{
				if(jeVSeznamu(polozka))	//pokud je datum od nebo do prázdný, kontroluje se pouze zda je položka v seznamu
					return true;
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaFiltrPocatek"));
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">"
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsi(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumPocatku().isAfter(datumOd)){
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
	private boolean vlozitDoSeznamuVetsiRovno(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && (polozka.getDatumPocatku().isAfter(datumOd) || polozka.getDatumPocatku().isEqual(datumOd)))
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
	private boolean vlozitDoSeznamuRovno(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumPocatku().isEqual(datumOd))
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
	private boolean vlozitDoSeznamuNerovno(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && !polozka.getDatumPocatku().isEqual(datumOd))
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
	private boolean vlozitDoSeznamuMensiRovno(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && (polozka.getDatumPocatku().isBefore(datumOd) || polozka.getDatumPocatku().isEqual(datumOd)))
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
	private boolean vlozitDoSeznamuMensi(PolozkaPocatek polozka){
		if(dpDatumOD.getModel().getValue() != null){
			LocalDate datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(jeVSeznamu(polozka) && polozka.getDatumPocatku().isBefore(datumOd)){
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
	protected boolean jeVSeznamu(PolozkaPocatek polozka){		
		try{
			List seznam = this.lsSeznamFiltr.getSelectedValuesList();
			if(seznam.isEmpty() || seznam.contains(polozka))
				return true;
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaFiltrPocatek"));
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
                
        JLabel lblDatum = new JLabel(Konstanty.POPISY.getProperty("popisDatumPocatku")+": ");
        cbOperatorDatum.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
        dpDatumOD.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
        dpDatumDO.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
        	
        this.add(lblDatum);
        this.add(cbOperatorDatum);
        this.add(dpDatumOD);
        this.add(dpDatumDO);
        if(this.getName().equals(Konstanty.POPISY.getProperty("nazevUkoly"))) {
        this.add(new JLabel(Konstanty.POPISY.getProperty("filtrovatPodle")));	
        this.add(cbTypPodfiltru);
        cbTypPodfiltru.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
        }
        
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
