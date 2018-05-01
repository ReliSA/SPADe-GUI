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
import javax.swing.SwingUtilities;

import data.Projekt;
import data.custom.PrepravkaUkladaniCustom;
import data.custom.SablonaCustomGrafu;
import gui.DropChartPanel;
import gui.OknoCustomGraf;
import gui.PanelProjektu;
import gui.TlacitkoEditaceGrafu;
import gui.PanelGrafuCustom;

/**
 * Třída pro ukládání custom grafů.
 */
public class Ukladani {

	private static ArrayList<PrepravkaUkladaniCustom> save = new ArrayList<PrepravkaUkladaniCustom>(); // Arraylist všech custom grafů
	private static ArrayList<SablonaCustomGrafu> sablony = new ArrayList<SablonaCustomGrafu>();
	private static PanelGrafuCustom panelMiniatur; // odkaz na panel miniatur custom grafůTrida U
	private static JMenu menuMazaniGrafu; // odkaz na položku JMenu smaž graf
	private static JMenu menuVytvoreniSablona; // odkaz na položku JMenu smaž graf
	private static JMenu menuSmazaniSablona; // odkaz na položku JMenu smaž graf
	private static PanelProjektu panelProjektu; // odkaz na panel projektu

	/**
	 * Přidá přepravku s custom grafem do arraylistu a miniaturu do panelu miniatur
	 * 
	 * @param prepravka pro přidání
	 */
	public static void add(PrepravkaUkladaniCustom prepravka) {
		save.add(prepravka);
		JMenuItem item = new JMenuItem(prepravka.getNazev());
		item.addActionListener(actSmazaniGrafuJmeno);
		menuMazaniGrafu.add(item);
		DropChartPanel graf = new DropChartPanel(prepravka.getPanel(), prepravka.getTypGrafu(),Konstanty.CUSTOM);
		graf.setName(prepravka.getNazev());

		graf.getMenu().add(new TlacitkoEditaceGrafu(prepravka.getNastaveni()));

		panelMiniatur.vlozGraf(graf);
		save();
	}

	/**
	 * Přidá sablonu do arraylistu
	 * 
	 * @param sablona
	 */
	public static void add(SablonaCustomGrafu sablona) {
		sablony.add(sablona);
		saveSablony();
		loadSablony();
	}

	/**
	 * Uloží arraylist custom grafů do souboru specifikovaným parametrem
	 * 
	 * @param file
	 *            souboru pro uložení
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
	 * Uloží arraylist custom grafů určitého projektu do souboru specifikovaným
	 * parametrem
	 * 
	 * @param file
	 *            souboru pro uložení
	 * @param id
	 *            projektu grafů
	 */
	public static void save(File file, int id) {

		ArrayList<PrepravkaUkladaniCustom> export = new ArrayList<PrepravkaUkladaniCustom>();

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
	 * Načte custom grafy ze souboru do arraylistu
	 */
	public static void load() {
		try {
			FileInputStream fis = new FileInputStream(Konstanty.GRAFY_SOUBOR);
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (ArrayList<PrepravkaUkladaniCustom>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Uloží arraylist sablon do souboru
	 */
	public static void saveSablony() {
		try {
			FileOutputStream fos = new FileOutputStream(Konstanty.SABLONY_SOUBOR);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sablony);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Načte sablony ze souboru do arraylistu
	 */
	public static void loadSablony() {
		menuSmazaniSablona.removeAll();
		menuVytvoreniSablona.removeAll();
		try {
			FileInputStream fis = new FileInputStream(Konstanty.SABLONY_SOUBOR);
			ObjectInputStream ois = new ObjectInputStream(fis);
			sablony = (ArrayList<SablonaCustomGrafu>) ois.readObject();
			ois.close();
			
			for (Iterator<SablonaCustomGrafu> iterator = sablony.iterator(); iterator.hasNext();) {
				SablonaCustomGrafu sablona = (SablonaCustomGrafu) iterator.next();

				JMenuItem itemVytvoreni = new JMenuItem(sablona.getNazev());
				itemVytvoreni.addActionListener(actVytvorZeSablony);
				menuVytvoreniSablona.add(itemVytvoreni);

				JMenuItem itemMazani = new JMenuItem(sablona.getNazev());
				itemMazani.addActionListener(actSmazSablonu);
				menuSmazaniSablona.add(itemMazani);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	

	/**
	 * Importuje grafy ze souboru specifikovaného parametrem
	 * 
	 * @param file
	 *            souboru pro načtení
	 * @param id
	 *            aktuálně zvoleného projektu
	 */
	public static void importGrafu(File file, int id) {

		ArrayList<PrepravkaUkladaniCustom> nove = new ArrayList<PrepravkaUkladaniCustom>();
		int pocitadlo = 2;
		String nazev;

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			nove = (ArrayList<PrepravkaUkladaniCustom>) ois.readObject();
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
	 * 
	 * @param projektID
	 *            id projektu
	 */
	public static void nactiGrafy(int projektID) {
		DropChartPanel graf = null;
		panelMiniatur.removeAll();
		menuMazaniGrafu.removeAll();
		for (Iterator<PrepravkaUkladaniCustom> iterator = save.iterator(); iterator.hasNext();) {
			PrepravkaUkladaniCustom prepravka = (PrepravkaUkladaniCustom) iterator.next();
			if (prepravka.getProjectID() == projektID) {
				graf = new DropChartPanel(prepravka.getPanel(), prepravka.getTypGrafu(),Konstanty.CUSTOM);
				graf.setName(prepravka.getNazev());

				graf.getMenu().add(new TlacitkoEditaceGrafu(prepravka.getNastaveni()));

				JMenuItem item = new JMenuItem(prepravka.getNazev());
				item.addActionListener(actSmazaniGrafuJmeno);
				menuMazaniGrafu.add(item);
				panelMiniatur.vlozGraf(graf);
			}
		}
		panelMiniatur.revalidate();
		panelMiniatur.repaint();
	}

	/**
	 * Zkontroluje zda se název v arraylistu již nachází
	 * 
	 * @param title
	 *            název ke kontrole
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
	 * Zkontroluje zda se název v arraylistu šablon již nachází
	 * 
	 * @param title
	 *            název ke kontrole
	 * @return true/false
	 */
	public static boolean kontrolaNazvuSablon(String title) {
		boolean bool = false;
		for (int i = 0; i < sablony.size(); i++) {
			if (sablony.get(i).getNazev().equals(title)) {
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * Odstraní z arraylistu grafy daného projektu, uloží arraylist na disk a znovu
	 * načte grafy
	 * 
	 * @param idProjektu
	 *            id projektu
	 */
	public static void odstranGrafyProjektu(int idProjektu) {
		int dialogResult = JOptionPane.showConfirmDialog(null, Konstanty.POPISY.getProperty("mazaniOknoVseText"),
				Konstanty.POPISY.getProperty("mazaniOknoPopisek"), JOptionPane.YES_NO_OPTION);
		if (dialogResult == 0) {
			ArrayList<PrepravkaUkladaniCustom> smazat = new ArrayList<PrepravkaUkladaniCustom>();
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
	 * 
	 * @return panel projektu
	 */
	public static PanelProjektu getPanelProjektu() {
		return panelProjektu;
	}

	/**
	 * Nastaví panel projektu
	 */
	public static void setPanelProjektu(PanelProjektu panelGrafu) {
		Ukladani.panelProjektu = panelGrafu;
	}

	/**
	 * Vrací odkaz na položku menu pro smazání grafu
	 * @return JMenu pro smazání grafu
	 */
	public static JMenu getMenuMazaniGrafu() {
		return menuMazaniGrafu;
	}

	/**
	 * Nastaví odkaz na položku menu pro smazání grafu
	 * @param menuMazaniGrafu JMenu pro smazání grafu
	 */
	public static void setMenuMazaniGrafu(JMenu menuMazaniGrafu) {
		Ukladani.menuMazaniGrafu = menuMazaniGrafu;
	}

	/**
	 * Vrací odkaz na položku menu pro vytvoření grafu ze šablony
	 * @return JMenu pro smazání grafu
	 */
	public static JMenu getMenuVytvoreniSablona() {
		return menuVytvoreniSablona;
	}

	/**
	 * Nastaví odkaz na položku menu pro vytvoření grafu ze šablony 
	 * @param menuVytvoreniSablona JMenu pro vytvoření grafu ze šablony 
	 */
	public static void setMenuVytvoreniSablona(JMenu menuVytvoreniSablona) {
		Ukladani.menuVytvoreniSablona = menuVytvoreniSablona;
	}

	/**
	 * Vrací odkaz na položku menu pro smazání šablony
	 * @return JMenu pro smazání šablony
	 */
	public static JMenu getMenuSmazaniSablona() {
		return menuSmazaniSablona;
	}

	/**
	 * Nastaví odkaz na položku menu pro vytvoření grafu ze šablony 
	 * @param menuSmazaniSablona JMenu pro smazání šablony
	 */
	public static void setMenuSmazaniSablona(JMenu menuSmazaniSablona) {
		Ukladani.menuSmazaniSablona = menuSmazaniSablona;
	}

	/**
	 * Vrací panel miniatur custom grafů
	 * 
	 * @return panel miniatur custom grafů
	 */
	public static PanelGrafuCustom getPanel() {
		return panelMiniatur;
	}

	/**
	 * Nastaví panel miniatur custom grafů 
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
				smazGraf(smazat);
			}
		}
	};
	
	/**
	 * Metoda pro smazání grafu
	 * @param nazev grafu
	 */
	public static void smazGraf(String nazev) {
		int id;
		for (int i = 0; i < save.size(); i++) {
			if (save.get(i).getNazev().equals(nazev)) {
				id = save.get(i).getProjectID();
				save.remove(save.get(i));
				save();
				panelProjektu.nastavDropSloty();
				nactiGrafy(id);
				return;
			}
		}
	}

	/**
	 * Akce pro vytvoření grafu ze sablony
	 */
	private static ActionListener actVytvorZeSablony = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

				String sablonaNazev = ((JMenuItem) e.getSource()).getText();
				SablonaCustomGrafu sablona;

				for (int i = 0; i < sablony.size(); i++) {
					if (sablony.get(i).getNazev().equals(sablonaNazev)) {
						sablona = sablony.get(i);

						SwingUtilities.invokeLater(() -> {
							OknoCustomGraf example = new OknoCustomGraf(sablona,
									(Projekt) Konstanty.projektVyber.getSelectedItem());
							example.setLocationRelativeTo(null);
							example.setVisible(true);
						});
						break;
					}
				}
			
		}
	};

	/**
	 * Akce pro smazání sablony
	 */
	private static ActionListener actSmazSablonu = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			int dialogResult = JOptionPane.showConfirmDialog(null, Konstanty.POPISY.getProperty("mazaniSablonaText"),
					Konstanty.POPISY.getProperty("mazaniSablonaPopisek"), JOptionPane.YES_NO_OPTION);
			if (dialogResult == 0) {
				String smazat = ((JMenuItem) e.getSource()).getText();
				smazSablonu(smazat);
			}
		}
	};

	/**
	 * Metoda pro odstranění šablony
	 * @param nazev šablony
	 */
	public static void smazSablonu(String nazev) {
		for (int i = 0; i < sablony.size(); i++) {
			if (sablony.get(i).getNazev().equals(nazev)) {
				sablony.remove(i);
				saveSablony();
				loadSablony();
				break;
			}
		}
	}
	
}
