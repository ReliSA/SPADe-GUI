package test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

import javax.swing.UIManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gui.OknoCustomGraf;
import gui.OknoHlavni;
import gui.PanelCustomNahled;
import ostatni.Konstanty;
import ostatni.Ukladani;

public class OknoCustomGrafTest {

	private static Connection pripojeni;
	private OknoHlavni okno;
	private OknoCustomGraf customOkno;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Konstanty.POPISY
				.load(new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH), "UTF8"));
		Konstanty.SQLs.load(new InputStreamReader(new FileInputStream(Konstanty.SQLSoubor), "UTF8"));
		Konstanty.SQLsVar.load(new InputStreamReader(new FileInputStream(Konstanty.SQLVarSoubor), "UTF8"));
		Locale.setDefault(new Locale("en", "US"));
		Konstanty.nastavPopisComboBox();
		Ukladani.load();

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, "ppicha", "Phjdhg3h3gws.s");
		
	}

	@Before
	public void setUp() throws Exception {
		okno = new OknoHlavni(pripojeni);
		customOkno = new OknoCustomGraf(okno.getProjekt());
	}
	
	
	@Test
	public void testUlozeniGrafu() throws InterruptedException {
		int puvodni = okno.removeChartMenu.getItemCount();
		PanelCustomNahled panel = (PanelCustomNahled) customOkno.nahled.getComponent(0);
		customOkno.tfNazev.setText("pokus"+puvodni);
		panel.save.doClick();
		Thread.sleep(1000);
		assertEquals(puvodni+1, okno.removeChartMenu.getItemCount());
	}
	
	@Test
	public void testUlozeniSablony() throws InterruptedException {
		int puvodni = okno.removeSablonaMenu.getItemCount();
		PanelCustomNahled panel = (PanelCustomNahled) customOkno.nahled.getComponent(0);
		customOkno.tfNazev.setText("pokus"+okno.removeSablonaMenu.getItemCount());
		panel.saveTemplate.doClick();
		Thread.sleep(1000);
		assertEquals(puvodni+1, okno.removeSablonaMenu.getItemCount());
	}

	@Test
	public void testMazaniGrafu() throws InterruptedException {
		int puvodni = okno.removeChartMenu.getItemCount();
		okno.removeChartMenu.getItem(0).doClick();
		Thread.sleep(1000);
		assertEquals(puvodni-1, okno.removeChartMenu.getItemCount());
	}
	
	@Test
	public void testMazaniSablony() throws InterruptedException {
		int puvodni = okno.removeSablonaMenu.getItemCount();
		okno.removeSablonaMenu.getItem(0).doClick();
		Thread.sleep(1000);
		assertEquals(puvodni-1, okno.removeSablonaMenu.getItemCount());
	}
	
}
