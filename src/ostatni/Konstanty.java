package ostatni;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Třída konstant
 *
 */
public class Konstanty {
	/*----------------Velikosti komponent------------------*/
	public static final int SIRKA_OKNA = 1600;
	public static final int VYSKA_OKNA = 900;
	public static final int SIRKA_PANELU_MENU = 600;
	public static final int SIRKA_PANELU_GRAFU = 1800;
	
	public static final int VYSKA_PANELU_GRAFU_STANDART = 210;
	public static final int VYSKA_PANELU_GRAFU_UKOL = 850;
	
	public static final Dimension VELIKOST_OKNA = new Dimension(SIRKA_OKNA, VYSKA_OKNA);
	public static final Dimension VELIKOST_PRIHLASOVACIHO_OKNA = new Dimension(380, 180);
	public static final Dimension VELIKOST_NACITACIHO_OKNA = new Dimension(500, 150);
	public static final Dimension MINIMALNI_VELIKOST_OKNA = new Dimension(SIRKA_PANELU_MENU + 100, 750);
		
	public static final Dimension VELIKOST_PANELU = new Dimension(SIRKA_PANELU_GRAFU+40,2 * VYSKA_PANELU_GRAFU_STANDART +475);
	public static final Dimension VELIKOST_SIPKY_STATISTIKY = new Dimension(15, 2 * VYSKA_PANELU_GRAFU_STANDART +550);
	public static final Dimension VELIKOST_SIPKY_FILTRY = new Dimension(SIRKA_PANELU_GRAFU, 15);
	public static final Dimension VELIKOST_PANELU_STANDARD = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_PANELU_GRAFU_STANDART);
	public static final Dimension VELIKOST_PANELU_UKOL = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_PANELU_GRAFU_UKOL);
		
	public static final Dimension VELIKOST_GRAFU = new Dimension(280, 200);
	public static final Dimension VELIKOST_GRAFU_VELKY = new Dimension(835, 415);
	public static final Dimension VELIKOST_GRAFU_NEJVETSI = new Dimension(945, 415);

	
	public static final Dimension VELIKOST_SEZNAMU_CELA_SIRKA = new Dimension(230, 80);
	public static final Dimension VELIKOST_CELA_SIRKA = new Dimension(230, 28);
	public static final Dimension VELIKOST_POLOVICNI_SIRKA = new Dimension(115, 28);
	public static final Dimension VELIKOST_CTVRTINOVA_SIRKA = new Dimension(150, 23);
	public static final Dimension VELIKOST_COLOR_PICKER = new Dimension(70, 23);
	
	/*-------------Nastavení grafů------------------*/
	public static final FlowLayout FLOW_LAYOUT = new FlowLayout(FlowLayout.CENTER, 10, 10);
	
	public static final Font FONT_NADPIS = new Font("TimesRoman", Font.BOLD, 25);
	public static final Font FONT_POPISKY_GRAFU = new Font("TimesRoman", Font.PLAIN, 10);
	public static final Font FONT_NAZEV_GRAFU = new Font("TimesRoman", Font.PLAIN, 20);
	public static final Font FONT_SIPKA = new Font("Arial", Font.PLAIN, 15);
	public static final Font FONT_TLACITKA = new Font("TimesRoman", Font.BOLD, 12);
	/*-----------------------------------------------*/
	
	/*--------Nastavení panelu statistik-------------*/
	public static final Font FONT_NADPIS_STATISTIK = new Font("Dialog", Font.BOLD, 12);
	public static final Font FONT_STATISTIK = new Font("Dialog", Font.PLAIN, 12);
	/*-----------------------------------------------*/
	
	/*--------Typy údajů ve statistické panelu-------*/
	public static final int MINIMUM = 0;
	public static final int MAXIMUM = 1;
	public static final int PRUMER = 2;
	public static final int POCET = 3;
	/*-----------------------------------------------*/
	
	public static final int PRIORITY = 0;
	public static final int SEVERITY = 1;
	public static final int STATUS = 2;
	public static final int TYP = 3;
	public static final int RESOLUCE = 4;
	public static final int PRIRAZEN = 5;
	public static final int AUTOR = 6;
	public static final int FAZE = 7;
	public static final int ITERACE = 8;
	public static final int AKTIVITY = 9;
	
	public static final int UKOL = 10;
	public static final int KONFIGURACE = 11;
	public static final int ARTEFAKT = 12;
	public static final int SEGMENT = 13;
	public static final int CUSTOM = 14;

	
	public static final int HODNOTA = 0;
	public static final int TRIDA = 1;
	public static final int SUPERTRIDA = 2;

	/*-------------Typy grafů------------------------*/
	public static final int SLOUPCOVY = 0;
	public static final int HISTOGRAM = 1;
	public static final int SPOJNICOVY = 2;
	public static final int GANTT = 3;
	public static final int PIE = 4;
	/*-----------------------------------------------*/
	
	/*-------------Typy custom grafů------------------------*/
	public static final int CUSTOM_SLOUPCOVY = 0;
	public static final int CUSTOM_SPOJNICOVY = 1;
	public static final int CUSTOM_BODOVY = 2;
	public static final int CUSTOM_PLOSNY = 3;
	public static final int CUSTOM_PIE = 4;
	/*-----------------------------------------------*/
	
	/*--------------Typy supertříd--------------------*/
	public static final String[] NIZKASUPERTRIDA = {"LOW", "MINOR", "OPEN", "FINISHED"};
	public static final String[] STREDNISUPERTRIDA = {"NORMAL", "CLOSED", "UNFINISHED"};
	public static final String[] VYSOKASUPERTRIDA = {"HIGH", "MAJOR"};
	/*-----------------------------------------------*/
	
	/*--------------Barvy panelů--------------------*/
	public static final Color barvaUkol = new Color(180, 216, 231);
	public static final Color barvaOsoby = new Color(180, 216, 231);
	public static final Color barvaFaze = new Color(173, 223, 173);
	public static final Color barvaIterace = new Color(255, 153, 153);
	public static final Color barvaAktivity= new Color(190, 158, 217);
	public static final Color barvaKonfigurace = new Color(236,186,147);
	public static final Color barvaArtefakty = new Color(248,131,85);
	/*-----------------------------------------------*/
	
	/*-------------Typy SQL------------------------*/
	public static final int SQL_NIC = 0;
	public static final int SQL_ITERACE = 1;
	public static final int SQL_OSOBY = 2;
	public static final int SQL_OBOJE = 3;
	/*-----------------------------------------------*/
	
	/*-------------Typy grafů bez nul hodnot-------------*/
	public static final int BEZ_AUTOR = 0;
	public static final int BEZ_CISELNIKY = 1;
	public static final int BEZ_SEGMENTY = 2;
	/*-----------------------------------------------*/
	
	public static Connection PRIPOJENI = null;														//připojení k databázi
	public static final String CESTA_K_DATABAZI = "jdbc:mysql://127.0.0.1:3306/ppicha?allowMultiQueries=true";				//cesta k databázi
	//public static final String CESTA_K_DATABAZI = "jdbc:mysql://students.kiv.zcu.cz:3306/ppicha";
	
	public static int CITAC_PROGRESU = 0;															//čítač progresu
	public static final int POCET_KROKU_PROGRESU = 7;												//počet načítaných skupin dat
	
	public static final LocalDate DATUM_PRAZDNY = LocalDate.of(1900, 12, 31);						//prázdný datum

	public static final String NAZEV_SOUBORU_POPISU_CZECH = "czech.properties";
	public static final String NAZEV_SOUBORU_POPISU_ENGLISH = "english.properties";
	public static final String SQLSoubor = "sql.properties";
	public static final String SQLVarSoubor = "sqlVar.properties";
	public static final String GRAFY_SOUBOR = "CustomCharts.dat";
	public static Properties POPISY = new Properties();
	public static Properties SQLs = new Properties();
	public static Properties SQLsVar = new Properties();
	public static final int DELKA_POPISKU_OS = 20;
	
/*---------------------------------------------------------*/
	
	public static String [] POLE_LOG_OPERATORU;
	public static String [] POLE_OPERATORU;								//hodnoty seznamů comboboxů ve filtrech
	public static String [] POLE_PODMINEK;								//hodnoty seznamů podmínkových comboboxů ve filtrech
	public static String [] POLE_FILTRU; 	 							//seznam možných filtrů	
	public static String [] POLE_PODFILTRU;
	public static String[] TYPY_GRAFU;
	
	/**
	 * Vrací seznam ? podle seznamu id (využívá se v DAO třídách)
	 * @param seznamId seznam identifikátorů
	 * @return seznam ?
	 */
	public static String getZnakyParametru(ArrayList<Integer> seznamId){
		String vystup = "?";
		for(int i = 1; i < seznamId.size(); i++)
			vystup += ", ?";
		return vystup;
	}
	
	/**
	 * Nastaví popisky comboboxu
	 */
	public static void nastavPopisComboBox(){
		POLE_PODMINEK = new String[]{POPISY.getProperty("a"), POPISY.getProperty("nebo")};
		POLE_OPERATORU = new String[]{POPISY.getProperty("mezi"), ">", ">=", "=", "!=", "<=", "<"};
		POLE_FILTRU = new String[]{POPISY.getProperty("nazevUkoly"), POPISY.getProperty("nazevFaze"), POPISY.getProperty("nazevIterace"), POPISY.getProperty("nazevAktivity"), POPISY.getProperty("nazevKonfigurace"), POPISY.getProperty("nazevArtefakty")};
		POLE_LOG_OPERATORU = new String[]{POPISY.getProperty("and"),POPISY.getProperty("or"),POPISY.getProperty("xor"),POPISY.getProperty("not")};
		POLE_PODFILTRU = new String[]{POPISY.getProperty("nazevPriority"), POPISY.getProperty("nazevSeverity"), POPISY.getProperty("nazevStatusy"), POPISY.getProperty("nazevTypy"), POPISY.getProperty("nazevResoluce"), POPISY.getProperty("nazevOsoby"),POPISY.getProperty("cas")};
		TYPY_GRAFU = new String[] {POPISY.getProperty("sloupcovy"),POPISY.getProperty("spojnicovy"),POPISY.getProperty("bodovy"),POPISY.getProperty("plosny"),POPISY.getProperty("kolacovy")};
	}
	
	/**
	 * Vrací pole názvů SQL dotazů pro custom grafy
	 * @return pole názvů SQL dotazů
	 */
	public static String [] getSQLKeys(){ 
		ArrayList<String> list = new ArrayList<>();
		
        for(Object k:SQLs.keySet()){
            list.add((String)k);
        }     
        return list.toArray(new String[list.size()]);
    }
     
}
