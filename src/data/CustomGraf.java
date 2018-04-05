package data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomGraf {
	private ArrayList<String> nazvySloupcu = new ArrayList<String>();
	private ArrayList<String> datumy = new ArrayList<String>();
	private ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();

	public CustomGraf(int pocetSloupcu) {
		for (int i = 0; i < pocetSloupcu; i++) {
			data.add(new ArrayList<Double>());
		}
	}

	public String getNazvySloupcu(int radek) {
		return nazvySloupcu.get(radek);
	}

	public int pocetSloupcu() {
		return nazvySloupcu.size();
	}

	public int pocetRadku() {
		return datumy.size();
	}

	public String getDatumy(int radek) {
		return datumy.get(radek);
	}

	public Double getData(int sloupec, int radek) {
		return data.get(sloupec).get(radek);
	}

	public void addNazvySloupcu(String nazev) {
		nazvySloupcu.add(nazev);
	}

	public void addData(int sloupec, double value) {
		sloupec = sloupec - 2;
		data.get(sloupec).add(value);
	}

	public void addDatum(String value) {
		datumy.add(value);
	}

	public Double getMaxData() {
		ArrayList<Double> list = new ArrayList<Double>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).size() != 0) {
				list.add(Collections.max(data.get(i)));
			}
		}
		return Collections.max(list);
	}
}
