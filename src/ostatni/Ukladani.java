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
import javax.swing.JTabbedPane;
import data.prepravkaUkladaniCustom;
import gui.DropChartPanel;
import gui.PanelGrafuCustom;

public class Ukladani {

	private static ArrayList<prepravkaUkladaniCustom> save = new ArrayList<prepravkaUkladaniCustom>();
	private static PanelGrafuCustom panel;
	private static JMenu menu;

	public static JMenu getMenu() {
		return menu;
	}

	public static void setMenu(JMenu menu) {
		Ukladani.menu = menu;
	}

	public static PanelGrafuCustom getPanel() {
		return panel;
	}

	public static void setPanel(PanelGrafuCustom panel) {
		Ukladani.panel = panel;
	}

	public static void add(prepravkaUkladaniCustom prepravka) {
		save.add(prepravka);
		JMenuItem item = new JMenuItem(prepravka.getNazev());
		item.addActionListener(actNacteniGrafuProSmazani);
		menu.add(item);
		((JTabbedPane) panel.getParent()).setSelectedIndex(4);
		save();
	}

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

	public static void importGrafu(File file, int id) {

		ArrayList<prepravkaUkladaniCustom> nove = new ArrayList<prepravkaUkladaniCustom>();
		int pocitadlo = 2;

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			nove = (ArrayList<prepravkaUkladaniCustom>) ois.readObject();
			ois.close();

			for (int i = 0; i < nove.size(); i++) {
				if (kontrolaNazvu(nove.get(i).getNazev())) {
					nove.get(i).setNazev(nove.get(i).getNazev() + " import");
					nove.get(i).getPanel().setTitle(nove.get(i).getNazev());

					while (kontrolaNazvu(nove.get(i).getNazev())) {
						
						nove.get(i).setNazev(nove.get(i).getNazev() + " " + pocitadlo);
						nove.get(i).getPanel().setTitle(nove.get(i).getNazev());
						
						pocitadlo++;
					}					
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

	public static void nactiGrafy(int projektID) {
		DropChartPanel graf = null;
		panel.removeAll();
		menu.removeAll();
		for (Iterator<prepravkaUkladaniCustom> iterator = save.iterator(); iterator.hasNext();) {
			prepravkaUkladaniCustom prepravka = (prepravkaUkladaniCustom) iterator.next();
			if (prepravka.getProjectID() == projektID) {
				graf = new DropChartPanel(prepravka.getPanel(), prepravka.getTypGrafu());
				graf.setName(prepravka.getNazev());
				JMenuItem item = new JMenuItem(prepravka.getNazev());
				item.addActionListener(actNacteniGrafuProSmazani);
				menu.add(item);
				panel.vlozGraf(graf);
			}
		}
		panel.revalidate();
		panel.repaint();
	}

	public static boolean kontrolaNazvu(String title) {
		boolean bool = false;
		for (int i = 0; i < save.size(); i++) {
			if (save.get(i).getNazev().equals(title)) {
				bool = true;
			}
		}
		return bool;
	}

	static ActionListener actNacteniGrafuProSmazani = new ActionListener() {
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
						nactiGrafy(id);
					}
				}
			}
		}
	};

}
