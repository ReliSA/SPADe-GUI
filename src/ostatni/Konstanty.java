package ostatni;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Třída konstant
 * @author michalvselko
 *
 */
public class Konstanty {
	/*----------------Velikosti komponent------------------*/
	public static final int SIRKA_OKNA = 1024;
	public static final int VYSKA_OKNA = 768;
	public static final int SIRKA_PANELU_MENU = 300;
	public static final int SIRKA_PANELU_GRAFU = 2200;
	
	public static final int VYSKA_PANELU_GRAFU_STANDART = 600;
	public static final int POCET_STANDART_PANELU = 4;
	public static final int VYSKA_PANELU_GRAFU_UKOL = 3600;
	
	public static final Dimension VELIKOST_OKNA = new Dimension(SIRKA_OKNA, VYSKA_OKNA);
	public static final Dimension VELIKOST_PRIHLASOVACIHO_OKNA = new Dimension(380, 180);
	public static final Dimension VELIKOST_NACITACIHO_OKNA = new Dimension(500, 150);
	public static final Dimension MINIMALNI_VELIKOST_OKNA = new Dimension(SIRKA_PANELU_MENU + 100, 750);
	
	public static final Dimension VELIKOST_PANELU_MENU = new Dimension(SIRKA_PANELU_MENU, VYSKA_OKNA);
	public static final Dimension VELIKOST_PANELU_GRAFU = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_OKNA);
	
	public static final Dimension VELIKOST_PANELU = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_PANELU_GRAFU_STANDART * POCET_STANDART_PANELU + VYSKA_PANELU_GRAFU_UKOL);
	public static final Dimension VELIKOST_PANELU_STANDARD = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_PANELU_GRAFU_STANDART);
	public static final Dimension VELIKOST_PANELU_UKOL = new Dimension(SIRKA_PANELU_GRAFU, VYSKA_PANELU_GRAFU_UKOL);
	
	public static final Dimension VELIKOST_GRAFU = new Dimension(700, 500);
	
	public static final Dimension VELIKOST_SEZNAMU_CELA_SIRKA = new Dimension(230, 80);
	public static final Dimension VELIKOST_CELA_SIRKA = new Dimension(230, 28);
	public static final Dimension VELIKOST_POLOVICNI_SIRKA = new Dimension(115, 28);
	public static final Dimension VELIKOST_CTVRTINOVA_SIRKA = new Dimension(55, 28);
	
	/*---------------------------------------------------------*/
	
	
	public static final String [] POLE_OPERATORU = {"mezi", ">", ">=", "=", "!=", "<=", "<"};	//hodnoty seznamů comboboxů ve filtrech
	public static final String [] POLE_PODMINEK = {"a", "nebo"};								//hodnoty seznamů podmínkových comboboxů ve filtrech
	public static final String [] POLE_FILTRU = {"Úkoly", "Priority", "Severity", "Statusy", "Typy", "Resoluce", "Osoby", "Fáze", "Iterace", "Aktivity", "Konfigurace", "Artefakty"}; //seznam možných filtrů	
	/*-------------Nastavení grafů------------------*/
	public static final FlowLayout FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT, 10, 10);
	
	public static final Font FONT_NADPIS = new Font("TimesRoman", Font.BOLD, 25);
	public static final Font FONT_POPISKY_GRAFU = new Font("TimesRoman", Font.PLAIN, 10);
	public static final Font FONT_NAZEV_GRAFU = new Font("TimesRoman", Font.PLAIN, 20);
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

	
	public static final int HODNOTA = 0;
	public static final int TRIDA = 1;
	public static final int SUPERTRIDA = 2;

	/*-------------Typy grafů------------------------*/
	public static final int SLOUPCOVY = 0;
	public static final int HISTOGRAM = 1;
	public static final int SPOJNICOVY = 2;
	public static final int GANTT = 3;
	/*-----------------------------------------------*/
	
	/*--------------Typy supertříd--------------------*/
	public static final String[] NIZKASUPERTRIDA = {"LOW", "MINOR", "OPEN", "FINISHED"};
	public static final String[] STREDNISUPERTRIDA = {"NORMAL", "CLOSED", "UNFINISHED"};
	public static final String[] VYSOKASUPERTRIDA = {"HIGH", "MAJOR"};
	/*-----------------------------------------------*/
	
	public static Connection PRIPOJENI = null;														//připojení k databázi
	public static final String CESTA_K_DATABAZI = "jdbc:mysql://students.kiv.zcu.cz:3306/ppicha";	//cesta k databázi
	
	public static int CITAC_PROGRESU = 0;															//čítač progresu
	public static final int POCET_KROKU_PROGRESU = 7;												//počet načítaných skupin dat
	
	public static final LocalDate DATUM_PRAZDNY = LocalDate.of(1900, 12, 31);						//prázdný datum

	public static final String NAZEV_SOUBORU_POPISU_CZECH = "czech.properties";
	public static final String NAZEV_SOUBORU_POPISU_ENGLISH = "english.properties";
	public static Properties POPISY = new Properties();
	public static final int DELKA_POPISKU_OS = 20;
	
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
}
