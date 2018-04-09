package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;
import data.Ukol;
import data.polozky.PolozkaPocatek;
import ostatni.Konstanty;

/**
 * Panel filtrů pro strávený a předpokládaný čas zděděný z PanelFiltr
 */

public class PanelFiltrCas extends PanelFiltr {

	private static final long serialVersionUID = 750037095334073721L;
	private JCheckBox ckPouzitFiltr = new JCheckBox(Konstanty.POPISY.getProperty("checkBoxPouzitFiltr"), true); //check box určující zda se má použít tento filtr
	private JComboBox<String> cbOperatorStraveny; // seznam operatoru
	private JComboBox<String> cbOperatorPredpokladany; // seznam operatoru
	private JComboBox<String> cbSpojeni; //operátor pro spojení podmínek (a, nebo)
	private JFormattedTextField stravenyOD; // textové pole pro vyplnění minima stráveného času
	private JFormattedTextField stravenyDO; // textové pole pro vyplnění maxima stráveného času
	private JFormattedTextField predpokladanyOD; // textové pole pro vyplnění minima předpokládaného času
	private JFormattedTextField predpokladanyDO; // textové pole pro vyplnění maxima předpokládaného času
	private NumberFormat format;
	private NumberFormatter formatter;

	/**
	 * Konstruktor třídy, nastaví seznam výběru
	 * 
	 * @param seznam
	 *            seznam výběru
	 */
	public PanelFiltrCas(String nazev, ArrayList seznam) {
		super(seznam);
		nastavPanel(nazev);
	}

	/**
	 * Zjistí zda je zaškrtnuto Použít filtr
	 * 
	 * @return true, pokud je Použít filtr zaškrtnuto
	 */
	public boolean jePouzit() {
		return this.ckPouzitFiltr.isSelected();
	}
	
	/**
	 * Vrací seznam id odpovídajících zadaným podmínkám
	 * 
	 * @return seznam id odpovídajících zadaným podmínkám
	 */
	public ArrayList<Integer> getSeznamId() {
		ArrayList<Integer> straveny = getSeznamIdStraveny();
		ArrayList<Integer> predpokladany = getSeznamIdPredpokladany();
		
		if (cbSpojeni.getSelectedItem().equals(Konstanty.POPISY.getProperty("a"))) {			
			straveny.retainAll(predpokladany);					
		}

		else {
			straveny.removeAll(predpokladany);
			straveny.addAll(predpokladany);
		}
		
		if (straveny.isEmpty()) {
			straveny.add(-1);
		}
		return straveny;
	}
		
	/**
	 * Vrací seznam id odpovídajících zadanému strávenému času 
	 * 
	 * @return seznam id odpovídajících zadanému strávenému času 
	 */
	public ArrayList<Integer> getSeznamIdStraveny() {
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		if (jePouzit()) { // musí být zaškrtnuto Použít filtr
			for (int i = 0; i < this.seznam.size(); i++) { // projde všechny položky seznamu
				PolozkaPocatek polozka = (PolozkaPocatek) this.seznam.get(i);
				switch (cbOperatorStraveny.getSelectedItem().toString()) { // podle zadaného typu operátoru volá
																			// konkrétní kontrolní metodu
				case "mezi":
					if (vlozitDoSeznamuMeziStraveny(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case "between":
					if (vlozitDoSeznamuMeziStraveny(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case ">":
					if (vlozitDoSeznamuVetsiStraveny(polozka))
						seznamId.add(polozka.getID());
					break;
				case ">=":
					if (vlozitDoSeznamuVetsiRovnoStraveny(polozka))
						seznamId.add(polozka.getID());
					break;
				case "=":
					if (vlozitDoSeznamuRovnoStraveny(polozka))
						seznamId.add(polozka.getID());
					break;
				case "!=":
					if (vlozitDoSeznamuNerovnoStraveny(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<=":
					if (vlozitDoSeznamuMensiRovnoStraveny(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<":
					if (vlozitDoSeznamuMensiStraveny(polozka))
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
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru
	 * "mezi"
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMeziStraveny(PolozkaPocatek polozka) {
		try {
			if (stravenyOD.getValue() != null && stravenyDO.getValue() != null) {

				double stravenyMin = (double) stravenyOD.getValue();
				double stravenyMax = (double) stravenyDO.getValue();
				Ukol ukol = (Ukol) polozka;

				if ((ukol.getStravenyCas() > stravenyMin || ukol.getStravenyCas() == stravenyMin)
						&& (ukol.getStravenyCas() < stravenyMax || ukol.getStravenyCas() == stravenyMax)) {
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
	private boolean vlozitDoSeznamuVetsiStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() > stravenyMin) {
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
	private boolean vlozitDoSeznamuVetsiRovnoStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() > stravenyMin || ukol.getStravenyCas() == stravenyMin) {
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
	private boolean vlozitDoSeznamuRovnoStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() == stravenyMin) {
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
	private boolean vlozitDoSeznamuNerovnoStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() != stravenyMin) {
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
	private boolean vlozitDoSeznamuMensiRovnoStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() < stravenyMin || ukol.getStravenyCas() == stravenyMin) {
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
	private boolean vlozitDoSeznamuMensiStraveny(PolozkaPocatek polozka) {
		if (stravenyOD.getValue() != null) {
			Integer stravenyMin = (Integer) stravenyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getStravenyCas() < stravenyMin) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	
	/**
	 * Vrací seznam id odpovídajících zadanému strávenému času
	 * 
	 * @return seznam id odpovídajících zadanému strávenému času
	 */
	public ArrayList<Integer> getSeznamIdPredpokladany() {
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		if (jePouzit()) { // musí být zaškrtnuto Použít filtr
			for (int i = 0; i < this.seznam.size(); i++) {
				PolozkaPocatek polozka = (PolozkaPocatek) this.seznam.get(i);
				switch (cbOperatorStraveny.getSelectedItem().toString()) { // podle zadaného typu operátoru volá konkrétní kontrolní metodu
				case "mezi":
					if (vlozitDoSeznamuMeziPredpokladany(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case "between":
					if (vlozitDoSeznamuMeziPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case ">":
					if (vlozitDoSeznamuVetsiPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case ">=":
					if (vlozitDoSeznamuVetsiRovnoPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case "=":
					if (vlozitDoSeznamuRovnoPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case "!=":
					if (vlozitDoSeznamuNerovnoPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<=":
					if (vlozitDoSeznamuMensiRovnoPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<":
					if (vlozitDoSeznamuMensiPredpokladany(polozka))
						seznamId.add(polozka.getID());
					break;
				}
			}
		}
		if (seznamId.isEmpty()) { // pokud je seznam prázdný doplním alespoň -1 jinak by při načítání dat seznam jevil jako nepoužitý
			seznamId.add(-1);}
		return seznamId;
	}

	
	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru
	 * "mezi"
	 * 
	 * @param polozka kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMeziPredpokladany(PolozkaPocatek polozka) {
		try {
			/* Zkontroluje zda není datum od a do prázdné */
			if (predpokladanyOD.getValue() != null && predpokladanyDO.getValue() != null) {

				double predpokladanyMin = (double) predpokladanyOD.getValue();
				double predpokladanyMax = (double) predpokladanyDO.getValue();
				Ukol ukol = (Ukol) polozka;

				if ((ukol.getStravenyCas() > predpokladanyMin || ukol.getStravenyCas() == predpokladanyMin)
						&& (ukol.getPredpokladanyCas() < predpokladanyMax || ukol.getPredpokladanyCas() == predpokladanyMax)) {
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
	private boolean vlozitDoSeznamuVetsiPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() > predpokladanyMin) {
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
	private boolean vlozitDoSeznamuVetsiRovnoPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() > predpokladanyMin || ukol.getPredpokladanyCas() == predpokladanyMin) {
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
	private boolean vlozitDoSeznamuRovnoPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() == predpokladanyMin) {
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
	private boolean vlozitDoSeznamuNerovnoPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() != predpokladanyMin) {
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
	private boolean vlozitDoSeznamuMensiRovnoPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() < predpokladanyMin || ukol.getPredpokladanyCas() == predpokladanyMin) {
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
	private boolean vlozitDoSeznamuMensiPredpokladany(PolozkaPocatek polozka) {
		if (predpokladanyOD.getValue() != null) {
			Integer predpokladanyMin = (Integer) predpokladanyOD.getValue();
			Ukol ukol = (Ukol) polozka;
			if (ukol.getPredpokladanyCas() < predpokladanyMin) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * Nastavení zobrazení panelu
	 *
	 * @param nazev Název panelu
	 */
	protected void nastavPanel(String nazev) {
		String titulek = "";
		ckPouzitFiltr.setOpaque(false);

		titulek = Konstanty.POPISY.getProperty("cas");
		this.setBackground(Konstanty.barvaUkol);
		this.setBorder(BorderFactory.createTitledBorder(titulek));

		format = NumberFormat.getInstance();
		formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(true);
		stravenyOD = new JFormattedTextField(formatter);
		stravenyDO = new JFormattedTextField(formatter);
		predpokladanyOD = new JFormattedTextField(formatter);
		predpokladanyDO = new JFormattedTextField(formatter);
		JLabel lblStraveny = new JLabel(Konstanty.POPISY.getProperty("popisStravenyCas") + ": ");
		JLabel lblPredpokladany = new JLabel(Konstanty.POPISY.getProperty("popisCasovyOdhad") + ": ");
		cbSpojeni = new JComboBox<String>(Konstanty.POLE_PODMINEK);

		nastavAkce();

		cbOperatorStraveny.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		stravenyOD.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		stravenyDO.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		cbOperatorPredpokladany.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		predpokladanyOD.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		predpokladanyDO.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
		this.add(ckPouzitFiltr);
		this.add(lblStraveny);
		this.setName(nazev);
		this.add(cbOperatorStraveny);
		this.add(stravenyOD);
		this.add(stravenyDO);
		this.add(cbSpojeni);
		this.add(lblPredpokladany);
		this.add(cbOperatorPredpokladany);
		this.add(predpokladanyOD);
		this.add(predpokladanyDO);
	}

	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce() {
		cbOperatorStraveny = new JComboBox<String>(Konstanty.POLE_OPERATORU);
		cbOperatorPredpokladany = new JComboBox<String>(Konstanty.POLE_OPERATORU);

		/*Akce pro změně zaškrtávátka Použít filtr*/
		ActionListener actPouzitFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ckPouzitFiltr.isSelected()){
					stravenyOD.setText(null); 
					stravenyDO.setText(null);  
					predpokladanyOD.setText(null);
					predpokladanyDO.setText(null); 
				}
			}
		};
		
		/* Akce pro změnu operátoru předpokládaného času */
		ActionListener actZmenaOperatoruPredpokladany = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (cbOperatorPredpokladany.getSelectedIndex() == 0)
					predpokladanyDO.setVisible(true);
				else
					predpokladanyDO.setVisible(false);
			}
		};

		/* Akce pro změnu operátoru stráveného času */
		ActionListener actZmenaOperatoruStraveny = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (cbOperatorStraveny.getSelectedIndex() == 0)
					stravenyDO.setVisible(true);
				else
					stravenyDO.setVisible(false);
			}
		};

		cbOperatorStraveny.addActionListener(actZmenaOperatoruStraveny);
		cbOperatorPredpokladany.addActionListener(actZmenaOperatoruPredpokladany);
		ckPouzitFiltr.addActionListener(actPouzitFiltr);
	}

}
