package ostatni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import data.prepravkaUkladaniCustom;
import gui.DropChartPanel;
import gui.PanelProjektu;
import gui.PanelGrafuCustom;

/**
 * Třída pro ukládání custom grafů.
 */
public class Ukladani {

	private static ArrayList<prepravkaUkladaniCustom> save = new ArrayList<prepravkaUkladaniCustom>(); // Arraylist všech custom grafů
	private static PanelGrafuCustom panelMiniatur; // odkaz na panel miniatur custom grafů
	private static JMenu menu; // odkaz na položku JMenu smaž graf
	private static PanelProjektu panelProjektu; //odkaz na panel projektu

	/**
	 * Přidá přepravku s custom grafem do arraylistu a miniaturu do panelu miniatur
	 * @param prepravka pro přidání
	 */
	public static void add(prepravkaUkladaniCustom prepravka) {
		save.add(prepravka);
		JMenuItem item = new JMenuItem(prepravka.getNazev());
		item.addActionListener(actSmazaniGrafuJmeno);
		menu.add(item);
		DropChartPanel graf = new DropChartPanel(prepravka.getPanel(), prepravka.getTypGrafu());
		graf.setName(prepravka.getNazev());			
		panelMiniatur.vlozGraf(graf);
		save();
	}

	/**
	 * Uloží arraylist custom grafů do souboru
	 */
	public static void save() {
		try {
			FileOutputStream fos = new FileOutputStream(Konstanty.GRAFY_SOUBOR);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(save);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Uloží arraylist custom grafů do souboru specifikovaným parametrem
	 * @param file souboru pro uložení
	 */
	public static void save(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(save);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Uloží arraylist custom grafů určitého projektu do souboru specifikovaným parametrem
	 * @param file souboru pro uložení
	 * @param id projektu grafů
	 */
	public static void save(File file, int id) {

		ArrayList<prepravkaUkladaniCustom> export = new ArrayList<prepravkaUkladaniCustom>();

		for (int i = 0; i < save.size(); i++) {
			if (save.get(i).getProjectID() == id) {
				export.add(save.get(i));
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(file + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(export);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Načte custom grafy ze souboru do arraylistu
	 */
	public static void load() {
		try {
			FileInputStream fis = new FileInputStream(Konstanty.GRAFY_SOUBOR);
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (ArrayList<prepravkaUkladaniCustom>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Importuje grafy ze souboru specifikovaného parametrem
	 * @param file souboru pro načtení
	 * @param id aktuálně zvoleného projektu
	 */
	public static void importGrafu(File file, int id) {

		ArrayList<prepravkaUkladaniCustom> nove = new ArrayList<prepravkaUkladaniCustom>();
		int pocitadlo = 2;
		String nazev;

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			nove = (ArrayList<prepravkaUkladaniCustom>) ois.readObject();
			ois.close();

			for (int i = 0; i < nove.size(); i++) {
				nazev = nove.get(i).getNazev();
				if (kontrolaNazvu(nazev)) {
					nazev = nazev + " import";

					while (kontrolaNazvu(nazev + " " + pocitadlo)) {
						pocitadlo++;
					}

					nove.get(i).setNazev(nazev + " " + pocitadlo);
					nove.get(i).getPanel().setTitle(nove.get(i).getNazev());

					save.add(nove.get(i));
				} else {
					save.add(nove.get(i));
				}
			}

			save();
			nactiGrafy(id);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Načte grafy daného projektu do panelu miniatur a Jmenu pro smazání
	 * @param projektID id projektu
	 */
	public static void nactiGrafy(int projektID) {
		DropChartPanel graf = null;
		panelMiniatur.removeAll();
		menu.removeAll();
		for (Iterator<prepravkaUkladaniCustom> iterator = save.iterator(); iterator.hasNext();) {
			prepravkaUkladaniCustom prepravka = (prepravkaUkladaniCustom) iterator.next();
			if (prepravka.getProjectID() == projektID) {
				graf = new DropChartPanel(prepravka.getPanel(), prepravka.getTypGrafu());
				graf.setName(prepravka.getNazev());
				JMenuItem item = new JMenuItem(prepravka.getNazev());
				item.addActionListener(actSmazaniGrafuJmeno);
				menu.add(item);
				panelMiniatur.vlozGraf(graf);
			}
		}
		panelMiniatur.revalidate();
		panelMiniatur.repaint();
	}

	/**
	 * Zkontroluje zda se název v arraylistu již nachází
	 * @param title název ke kontrole
	 * @return true/false
	 */
	public static boolean kontrolaNazvu(String title) {
		boolean bool = false;
		for (int i = 0; i < save.size(); i++) {
			if (save.get(i).getNazev().equals(title)) {
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * Odstraní z arraylistu grafy daného projektu, uloží arraylist na disk a znovu načte grafy
	 * @param idProjektu id projektu
	 */
	public static void odstranGrafyProjektu(int idProjektu) {
		int dialogResult = JOptionPane.showConfirmDialog(null, Konstanty.POPISY.getProperty("mazaniOknoVseText"),
				Konstanty.POPISY.getProperty("mazaniOknoPopisek"), JOptionPane.YES_NO_OPTION);
		if (dialogResult == 0) {
			ArrayList<prepravkaUkladaniCustom> smazat = new ArrayList<prepravkaUkladaniCustom>();
			for (int i = 0; i < save.size(); i++) {
				if (save.get(i).getProjectID() == idProjektu) {
					smazat.add(save.get(i));
				}
			}
			save.removeAll(smazat);
			save();
			panelProjektu.nastavDropSloty();
			nactiGrafy(idProjektu);
		}
	}

	/**
	 * Vrací panel projektu
	 * @return panel projektu
	 */
	public static PanelProjektu getPanelProjektu() {
		return panelProjektu;
	}

	/**
	 * Nastaví panel projektu
	 * @return panel projektu
	 */
	public static void setPanelProjektu(PanelProjektu panelGrafu) {
		Ukladani.panelProjektu = panelGrafu;
	}

	/**
	 * Vrací položku JMenu pro smazání grafů
	 * @return položka JMenu
	 */
	public static JMenu getMenu() {
		return menu;
	}

	/**
	 * Nastaví položku JMenu pro smazání grafů
	 * @return položka JMenu
	 */
	public static void setMenu(JMenu menu) {
		Ukladani.menu = menu;
	}

	/**
	 * Vrací panel miniatur custom grafů
	 * @return panel miniatur custom grafů
	 */
	public static PanelGrafuCustom getPanel() {
		return panelMiniatur;
	}

	/**
	 * Nastaví panel miniatur custom grafů
	 * @return panel miniatur custom grafů
	 */
	public static void setPanel(PanelGrafuCustom panel) {
		Ukladani.panelMiniatur = panel;
	}
	
	/**
	 * Akce pro smazání grafu dle jeho jména
	 */
	private static ActionListener actSmazaniGrafuJmeno = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			int dialogResult = JOptionPane.showConfirmDialog(null, Konstanty.POPISY.getProperty("mazaniOknoText"),
					Konstanty.POPISY.getProperty("mazaniOknoPopisek"), JOptionPane.YES_NO_OPTION);
			if (dialogResult == 0) {
				String smazat = ((JMenuItem) e.getSource()).getText();
				int id;

				for (int i = 0; i < save.size(); i++) {
					if (save.get(i).getNazev().equals(smazat)) {
						id = save.get(i).getProjectID();
						save.remove(save.get(i));
						save();
						panelProjektu.nastavDropSloty();
						nactiGrafy(id);
						return;
					}
				}
			}
		}
	};

}
