package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;
import data.Artefakt;
import data.polozky.PolozkaVytvoreni;
import ostatni.Konstanty;
/**
 * Panel filtrů pro artefakty a konfigurace zděděný z PanelFiltrPolozkaVytvoreni. Přidává možnost filtrování artefaktů podle velikosti.s
 */
public class PanelFiltrPolozkaVytvoreniArtefakt extends PanelFiltrPolozkaVytvoreni {

	private static final long serialVersionUID = 1533287073732672810L;
	private JComboBox<String> cbOperatorVelikost; // seznam operatoru
	private JComboBox<String> cbSpojeni; // combobox pro výběr logické operace pro spojení
	private JFormattedTextField min; // pole pro udání minimální velikosti
	private JFormattedTextField max; // pole pro udání maximální velikosti
	private NumberFormat format;
	private NumberFormatter formatter;
	
	/**
	 * Konstruktor panelu
	 * @param nazev název panelu
	 * @param seznam seznam položek
	 */
	public PanelFiltrPolozkaVytvoreniArtefakt(String nazev, ArrayList seznam) {
		super(nazev, seznam);
	}
	
	/**
	 * Vrací seznam id odpovídajících zadaným podmínkám velikosti artefaktu
	 * 
	 * @return seznam id odpovídajících zadaným podmínkám velikosti artefaktu
	 */
	public ArrayList<Integer> getSeznamIdVelikost() {
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		if (jePouzit()) { // musí být zaškrtnuto Použít filtr
			for (int i = 0; i < this.seznam.size(); i++) { // projde všechny položky seznamu
				PolozkaVytvoreni polozka = (PolozkaVytvoreni) this.seznam.get(i);
				switch (cbOperatorVelikost.getSelectedItem().toString()) { // podle zadaného typu operátoru volá
																			// konkrétní kontrolní metodu
				case "mezi":
				case "between":
					if (vlozitDoSeznamuMeziVelikost(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case ">":
					if (vlozitDoSeznamuVetsiVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				case ">=":
					if (vlozitDoSeznamuVetsiRovnoVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				case "=":
					if (vlozitDoSeznamuRovnoVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				case "!=":
					if (vlozitDoSeznamuNerovnoVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<=":
					if (vlozitDoSeznamuMensiRovnoVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<":
					if (vlozitDoSeznamuMensiVelikost(polozka))
						seznamId.add(polozka.getID());
					break;
				}
			}
		}
		if (seznamId.isEmpty()) // pokud je seznam prázdný doplním alespoň -1 jinak by při načítání dat seznam
								// jevil jako nepoužitý
			seznamId.add(-1);
		return seznamId;
	}
	
	/**
	 * Vrací seznam id odpovídajících zadaným podmínkám
	 * 
	 * @return seznam id odpovídajících zadaným podmínkám
	 */
	public ArrayList<Integer> getSeznamId() {
		ArrayList<Integer> datum = getSeznamIdVelikost();
		ArrayList<Integer> velikost = super.getSeznamId();
		
		if (cbSpojeni.getSelectedItem().equals(Konstanty.POPISY.getProperty("a"))) {
			
			datum.retainAll(velikost);			
	}

		else {
			datum.removeAll(velikost);
			datum.addAll(velikost);
		}
		
		if (datum.isEmpty()) {
			datum.add(-1);
		}
		
		return datum;
	}

	
	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru
	 * "mezi"
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMeziVelikost(PolozkaVytvoreni polozka) {
		try {
			if (min.getValue() != null && max.getValue() != null) {

				double minVal = (double) min.getValue();
				double maxVal = (double) max.getValue();
				Artefakt artefakt = (Artefakt) polozka;

				if ((artefakt.getVelikost() > minVal || artefakt.getVelikost() == minVal)
						&& (artefakt.getVelikost() < maxVal || artefakt.getVelikost() == maxVal)) {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaFiltrPocatek"));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">"
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsiVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() > minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">="
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsiRovnoVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() > minVal || artefakt.getVelikost() == minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "="
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuRovnoVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() == minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "!="
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuNerovnoVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() != minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<="
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensiRovnoVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() < minVal || artefakt.getVelikost() == minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<"
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensiVelikost(PolozkaVytvoreni polozka) {
		if (min.getValue() != null) {
			Integer minVal = (Integer) min.getValue();
			Artefakt artefakt = (Artefakt) polozka;
			if (artefakt.getVelikost() < minVal) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * Nastavení panelu, spustí se nastavení z předka a poté se přidávají nové komponenty
	 * @param nazev název filtru
	 */
	protected void nastavPanel(String nazev){
		super.nastavPanel(nazev);
		        
        format = NumberFormat.getInstance();
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(true);
		min = new JFormattedTextField(formatter);
		max = new JFormattedTextField(formatter);
		JLabel lblVelikost = new JLabel(Konstanty.POPISY.getProperty("velikostArtefakt") + ": ");
		cbSpojeni = new JComboBox<String>(Konstanty.POLE_PODMINEK);

		cbOperatorVelikost.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		min.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		max.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		
		this.add(cbSpojeni);
		this.add(lblVelikost);
		this.setName(nazev);
		this.add(cbOperatorVelikost);
		this.add(min);
		this.add(max);      
	}
	
		
	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce(){
				
		super.nastavAkce();
		
		cbOperatorVelikost = new JComboBox<String>(Konstanty.POLE_OPERATORU);

		/*Akce pro změně zaškrtávátka Použít filtr*/
		ActionListener actPouzitFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ckPouzitFiltr.isSelected()){ 
					min.setText(null);
					max.setText(null); 
				}
			}
		};
		
		ActionListener actZmenaOperatoruDatum = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(cbOperatorVelikost.getSelectedIndex() == 0)
					max.setVisible(true);
				else
					max.setVisible(false);
			}
		};
				
		cbOperatorVelikost.addActionListener(actZmenaOperatoruDatum);	
		ckPouzitFiltr.addActionListener(actPouzitFiltr);
	}
	
}
