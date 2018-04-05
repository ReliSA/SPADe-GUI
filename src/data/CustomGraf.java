package data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Objekt sloužící pro uložení dat sloužících k vykreslení custom grafu
 */
public class CustomGraf {
	private ArrayList<String> nazvySloupcu = new ArrayList<String>(); // arraylist s nazvy sloupcu dat
	private ArrayList<String> datumy = new ArrayList<String>(); // arraylist s hodnotami casove osy
	private ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>(); // arraylist s daty

	/**
	 * Konstruktor objektu
	 * @param pocetSloupcu
	 */
	public CustomGraf(int pocetSloupcu) {
		for (int i = 0; i < pocetSloupcu; i++) {
			data.add(new ArrayList<Double>());
		}
	}

	/**
	 * Vrací název sloupce
	 * @param sloupec id požadovaného sloupce
	 * @return název sloupce
	 */
	public String getNazevSloupce(int sloupec) {
		return nazvySloupcu.get(sloupec);
	}

	/**
	 * Vrací počet sloupců tabulky dat
	 * @return počet sloupců
	 */
	public int pocetSloupcu() {
		return nazvySloupcu.size();
	}

	/**
	 * Vrací počet řádků tabulky dat
	 * @return počet řádků
	 */
	public int pocetRadku() {
		return datumy.size();
	}

	/**
	 * Vrací datum uložené na řádku daným parametrem radek
	 * @param radek id radku
	 * @return datum
	 */
	public String getDatumy(int radek) {
		return datumy.get(radek);
	}

	/**
	 * Vrací data uložené na požadovaném řádku a sloupci
	 * @param sloupec
	 * @param radek
	 * @return požadovaná hodnota
	 */
	public Double getData(int sloupec, int radek) {
		return data.get(sloupec).get(radek);
	}

	/**
	 * Přidá název sloupce
	 * @param nazev
	 */
	public void addNazvySloupcu(String nazev) {
		nazvySloupcu.add(nazev);
	}

	/**
	 * Přidá data
	 * @param sloupec
	 * @param value
	 */
	public void addData(int sloupec, double value) {
		sloupec = sloupec - 2;
		data.get(sloupec).add(value);
	}

	/**
	 * Přidá datum
	 * @param value
	 */
	public void addDatum(String value) {
		datumy.add(value);
	}

	/**
	 * Vrátí maximální hodnotu z dat
	 * @return maximální hodnota
	 */
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
