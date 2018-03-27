package data;

import java.time.LocalDate;
import java.util.ArrayList;

public class CustomGraf {
	private ArrayList<String> nazvySloupcu = new ArrayList<String>();
	private ArrayList<LocalDate> datumy = new ArrayList<LocalDate>();
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

	public LocalDate getDatumy(int radek) {
		return datumy.get(radek);
	}

	public Double getData(int sloupec, int radek) {
		return data.get(sloupec).get(radek);
	}

	public void addNazvySloupcu(String nazev) {
		nazvySloupcu.add(nazev);		
	}
	
	public void addData(int sloupec,double value) {
		sloupec=sloupec-2;
		data.get(sloupec).add(value);		
	}
	
	public void addDatum(LocalDate value) {
		datumy.add(value);
	}	
}
