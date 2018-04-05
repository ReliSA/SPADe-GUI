package ostatni;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTabbedPane;

import data.prepravkaUkladaniCustom;
import gui.DropChartPanel;
import gui.PanelGrafuCustom;

public class Ukladani {

	private static ArrayList<prepravkaUkladaniCustom> save = new ArrayList<prepravkaUkladaniCustom>();
	private static PanelGrafuCustom panel;

	public static PanelGrafuCustom getPanel() {
		return panel;
	}

	public static void setPanel(PanelGrafuCustom panel) {
		Ukladani.panel = panel;
	}

	public static void add(prepravkaUkladaniCustom prepravka) {
		save.add(prepravka);
		((JTabbedPane)panel.getParent()).setSelectedIndex(4);
		save();
	}

	public static void save() {
		try {
			FileOutputStream fos = new FileOutputStream("CustomCharts.sav");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(save);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void nactiGrafy(int projektID) {
		for (Iterator iterator = save.iterator(); iterator.hasNext();) {
			prepravkaUkladaniCustom prepravka = (prepravkaUkladaniCustom) iterator.next();
			if (prepravka.getProjectID() == projektID) {
				panel.vlozGraf(new DropChartPanel(prepravka.getPanel(),prepravka.getTypGrafu()));
			}
		}
	}

	public static void load() {
		try {
			FileInputStream fis = new FileInputStream("CustomCharts.sav");
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (ArrayList<prepravkaUkladaniCustom>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
