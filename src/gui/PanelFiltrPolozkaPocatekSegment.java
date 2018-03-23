package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import data.Segment;
import data.polozky.PolozkaPocatek;
import ostatni.Konstanty;

public class PanelFiltrPolozkaPocatekSegment extends PanelFiltrPolozkaPocatek {

	private JComboBox<String> cbOperatorDatumKonec;
	private JDatePickerImpl dpDatumODKonec;
	private JDatePickerImpl dpDatumDOKonec;
	private JComboBox<String> cbSpojeniDatumu;

	public PanelFiltrPolozkaPocatekSegment(String nazev, ArrayList seznam) {
		super(nazev, seznam);
	}

	/**
	 * Vrací seznam id odpovídajících zadaným podmínkám
	 * 
	 * @return seznam id odpovídajících zadaným podmínkám
	 */
	public ArrayList<Integer> getSeznamIdKonec() {
		ArrayList<Integer> seznamId = new ArrayList<Integer>();

		if (jePouzit()) { // musí být zaškrtnuto Použít filtr
			for (int i = 0; i < this.seznam.size(); i++) { // projde všechny položky seznamu
				PolozkaPocatek polozka = (PolozkaPocatek) this.seznam.get(i);
				switch (cbOperatorDatumKonec.getSelectedItem().toString()) { // podle zadaného typu operátoru volá
																				// konkrétní kontrolní metodu
				case "mezi":
					if (vlozitDoSeznamuMeziKonec(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case "between":
					if (vlozitDoSeznamuMeziKonec(polozka))
						seznamId.add(polozka.getID()); // pokud metoda vrátí true, vloží se id do seznamu
					break;
				case ">":
					if (vlozitDoSeznamuVetsiKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				case ">=":
					if (vlozitDoSeznamuVetsiRovnoKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				case "=":
					if (vlozitDoSeznamuRovnoKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				case "!=":
					if (vlozitDoSeznamuNerovnoKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<=":
					if (vlozitDoSeznamuMensiRovnoKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				case "<":
					if (vlozitDoSeznamuMensiKonec(polozka))
						seznamId.add(polozka.getID());
					break;
				}
			}
		}
		if (seznamId.isEmpty()) // pokud je seznam prázdný doplní alespoň -1 jinak by při načítání dat seznam
								// jevil jako nepoužitý
			seznamId.add(-1);
		return seznamId;
	}

	public ArrayList<Integer> getSeznamId() {
		ArrayList<Integer> pocatek = getSeznamIdKonec();
		ArrayList<Integer> konec = super.getSeznamId();
		
		if (cbSpojeniDatumu.getSelectedItem().equals(Konstanty.POPISY.getProperty("a"))) {
			
			pocatek.retainAll(konec);			
	}

		else {
			pocatek.removeAll(konec);
			pocatek.addAll(konec);
		}
		
		if (pocatek.isEmpty()) {
			pocatek.add(-1);
		}
		
		return pocatek;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru
	 * "mezi"
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMeziKonec(PolozkaPocatek polozka) {
		try {
			/* Zkontroluje zda není datum od a do prázdné */
			if (dpDatumODKonec.getModel().getValue() != null && dpDatumDOKonec.getModel().getValue() != null) {
				LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate datumDo = ((Date) dpDatumDOKonec.getModel().getValue()).toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate();

				/*
				 * zkontroluje se zda je položka v seznamu a zároveň je datum počátku mezi
				 * vybranými datumy
				 */
				if (jeVSeznamu(polozka)
						&& (((Segment) polozka).getDatumKonce().isAfter(datumOd)
								|| ((Segment) polozka).getDatumKonce().isEqual(datumOd))
						&& (((Segment) polozka).getDatumKonce().isBefore(datumDo)
								|| ((Segment) polozka).getDatumKonce().isEqual(datumDo))) {
					return true;
				}
			} else {
				if (jeVSeznamu(polozka)) // pokud je datum od nebo do prázdný, kontroluje se pouze zda je položka v
											// seznamu
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
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsiKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && ((Segment) polozka).getDatumKonce().isAfter(datumOd)) {
				return true;
			}
		} else {
			if (jeVSeznamu(polozka)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru ">="
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuVetsiRovnoKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && (((Segment) polozka).getDatumKonce().isAfter(datumOd)
					|| ((Segment) polozka).getDatumKonce().isEqual(datumOd)))
				return true;
		} else {
			if (jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "="
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuRovnoKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && ((Segment) polozka).getDatumKonce().isEqual(datumOd))
				return true;
		} else {
			if (jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "!="
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuNerovnoKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && !((Segment) polozka).getDatumKonce().isEqual(datumOd))
				return true;
		} else {
			if (jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<="
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensiRovnoKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && (((Segment) polozka).getDatumKonce().isBefore(datumOd)
					|| ((Segment) polozka).getDatumKonce().isEqual(datumOd)))
				return true;
		} else {
			if (jeVSeznamu(polozka))
				return true;
		}
		return false;
	}

	/**
	 * Zkontroluje, zda položka odpovídá zadaným podmínkám při výběru operatoru "<"
	 * 
	 * @param polozka
	 *            kontrolovaná položka
	 * @return true pokud položka odpovídá parametrům
	 */
	private boolean vlozitDoSeznamuMensiKonec(PolozkaPocatek polozka) {
		if (dpDatumODKonec.getModel().getValue() != null) {
			LocalDate datumOd = ((Date) dpDatumODKonec.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if (jeVSeznamu(polozka) && ((Segment) polozka).getDatumKonce().isBefore(datumOd)) {
				return true;
			}
		} else {
			if (jeVSeznamu(polozka)) {
				return true;
			}
		}
		return false;
	}

	protected void nastavPanel(String nazev) {
		super.nastavPanel(nazev);

		UtilDateModel modelDatumODKonec = new UtilDateModel();
		UtilDateModel modelDatumDOKonec = new UtilDateModel();
		cbSpojeniDatumu = new JComboBox<String>(Konstanty.POLE_PODMINEK);

		Properties p = new Properties();
		p.put("text.today", Konstanty.POPISY.getProperty("kalendarDnes"));
		p.put("text.month", Konstanty.POPISY.getProperty("kalendarMesic"));
		p.put("text.year", Konstanty.POPISY.getProperty("kalendarRok"));

		JDatePanelImpl datumODPanelKonec = new JDatePanelImpl(modelDatumODKonec, p);
		JDatePanelImpl datumDOPanelKonec = new JDatePanelImpl(modelDatumDOKonec, p);
		dpDatumODKonec = new JDatePickerImpl(datumODPanelKonec, new DateComponentFormatter());
		dpDatumDOKonec = new JDatePickerImpl(datumDOPanelKonec, new DateComponentFormatter());

		JLabel lblDatum = new JLabel(Konstanty.POPISY.getProperty("popisDatumKonec") + ": ");
		cbOperatorDatumKonec.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		dpDatumODKonec.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		dpDatumDOKonec.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);

		this.add(cbSpojeniDatumu);
		this.add(lblDatum);
		this.add(cbOperatorDatumKonec);
		this.add(dpDatumODKonec);
		this.add(dpDatumDOKonec);
	}

	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce() {
		super.nastavAkce();

		/* Akce pro změně operatoru datumů */
		cbOperatorDatumKonec = new JComboBox<String>(Konstanty.POLE_OPERATORU);
		ActionListener actZmenaOperatoruDatumKonec = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (cbOperatorDatumKonec.getSelectedIndex() == 0)
					dpDatumDOKonec.setVisible(true);
				else
					dpDatumDOKonec.setVisible(false);
			}
		};
		cbOperatorDatumKonec.addActionListener(actZmenaOperatoruDatumKonec);
	}
}
