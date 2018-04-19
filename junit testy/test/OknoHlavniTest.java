package test;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import javax.swing.UIManager;

import gui.OknoCustomGraf;
import gui.OknoHlavni;
import gui.PanelFiltr;
import gui.PanelFiltrPolozkaPocatek;
import gui.TlacitkoMazaniFiltru;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ostatni.Konstanty;
import ostatni.Ukladani;

public class OknoHlavniTest {

	private static Connection pripojeni;
	private OknoHlavni okno;

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
	}

	@Test
	public void testOknoHlavni() {
		OknoHlavni okno = new OknoHlavni(pripojeni);
		assertEquals("SPADe", okno.getTitle());
	}

	@Test
	public void testNastavAkce() {
		okno.lsSeznamProjektu.setSelectedIndex(2);
	}

	@Test
	public void testPridejFiltr1() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);

		assertEquals("Tasks", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr2() {
		okno.cbTypFiltru.setSelectedIndex(1);

		assertEquals("Phase", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr3() {
		okno.cbTypFiltru.setSelectedIndex(2);

		assertEquals("Iterations", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr4() {
		okno.cbTypFiltru.setSelectedIndex(3);

		assertEquals("Activities", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr5() {
		okno.cbTypFiltru.setSelectedIndex(4);

		assertEquals("Configurations", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr6() {
		okno.cbTypFiltru.setSelectedIndex(5);

		assertEquals("Artifacts", okno.pnBoxFiltru.getComponents()[0].getName());
	}

	@Test
	public void testPridejFiltr7() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(0);
		assertEquals("Priorities", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr8() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(1);
		assertEquals("Severity", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr9() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(2);
		assertEquals("Status", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr10() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(3);
		assertEquals("Types", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr11() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(4);
		assertEquals("Resolution", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr12() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(5);
		assertEquals("People", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testPridejFiltr13() {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(6);
		assertEquals("Time", okno.pnBoxFiltru.getComponents()[1].getName());
	}

	@Test
	public void testZapniFiltr1() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(1, okno.getProjekt().getPocetUkolu());
	}

	@Test
	public void testZapniFiltr2() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(1);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(1, okno.getProjekt().getPocetFazi());
	}

	@Test
	public void testZapniFiltr3() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(2);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(200);
		assertEquals(1, okno.getProjekt().getPocetIteraci());
	}

	@Test
	public void testZapniFiltr4() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(3);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(200);
		assertEquals(1, okno.getProjekt().getPocetAktivit());
	}

	@Test
	public void testZapniFiltr5() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(4);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(2000);
		assertEquals(1, okno.getProjekt().getPocetKonfiguraci());
	}

	@Test
	public void testZapniFiltr6() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(5);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[0])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(2000);
		assertEquals(1, okno.getProjekt().getPocetArtefaktu());
	}

	@Test
	public void testZapniFiltr7() throws InterruptedException {

		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(0);
		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[1])).lsSeznamFiltr.setSelectedIndex(1);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(3, okno.getProjekt().getUkoly().get(0).getPriorityID());
	}

	@Test
	public void testZapniFiltr8() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(1);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[1])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(1, okno.getProjekt().getUkoly().get(0).getSeverityID());
	}

	@Test
	public void testZapniFiltr9() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(2);
		Thread.sleep(1000);
		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[1])).lsSeznamFiltr.setSelectedIndex(14);
		okno.btZapniFiltr.doClick();
		Thread.sleep(1000);
		assertEquals(1, okno.getProjekt().getUkoly().get(0).getStatusID());
	}

	@Test
	public void testZapniFiltr10() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(3);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[1])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(3, okno.getProjekt().getUkoly().get(0).getTypID());
	}

	@Test
	public void testZapniFiltr11() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);
		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(4);

		((PanelFiltr) (okno.pnBoxFiltru.getComponents()[1])).lsSeznamFiltr.setSelectedIndex(0);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		assertEquals(1, okno.getProjekt().getUkoly().get(0).getResoluceID());
	}

	@Test
	public void testZmenaProjektu() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);

		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(0);
		panel.cbTypPodfiltru.setSelectedIndex(1);
		panel.cbTypPodfiltru.setSelectedIndex(2);
		panel.cbTypPodfiltru.setSelectedIndex(3);
		panel.cbTypPodfiltru.setSelectedIndex(4);
		panel.cbTypPodfiltru.setSelectedIndex(5);
		panel.cbTypPodfiltru.setSelectedIndex(6);

		okno.cbTypFiltru.setSelectedIndex(1);
		okno.cbTypFiltru.setSelectedIndex(2);
		okno.cbTypFiltru.setSelectedIndex(3);
		okno.cbTypFiltru.setSelectedIndex(4);
		okno.cbTypFiltru.setSelectedIndex(5);
		okno.btZapniFiltr.doClick();
		Thread.sleep(100);
		okno.lsSeznamProjektu.setSelectedIndex(3);
		Thread.sleep(100);

		assertEquals(94, okno.getProjekt().getPocetUkolu());
	}

	@Test
	public void testMazaniFiltru() throws InterruptedException {
		okno.cbTypFiltru.setSelectedIndex(0);

		PanelFiltrPolozkaPocatek panel = (PanelFiltrPolozkaPocatek) okno.pnBoxFiltru.getComponents()[0];
		panel.cbTypPodfiltru.setSelectedIndex(0);
		panel.cbTypPodfiltru.setSelectedIndex(1);
		panel.cbTypPodfiltru.setSelectedIndex(2);
		panel.cbTypPodfiltru.setSelectedIndex(3);
		panel.cbTypPodfiltru.setSelectedIndex(4);
		panel.cbTypPodfiltru.setSelectedIndex(5);

		okno.cbTypFiltru.setSelectedIndex(1);
		okno.cbTypFiltru.setSelectedIndex(2);
		okno.cbTypFiltru.setSelectedIndex(3);
		okno.cbTypFiltru.setSelectedIndex(4);
		okno.cbTypFiltru.setSelectedIndex(5);
		Thread.sleep(3000);
		okno.btZapniFiltr.doClick();

		Thread.sleep(3000);

		((TlacitkoMazaniFiltru) okno.listaTlacitekSmazaniFiltru.getComponent(1)).doClick();

		assertEquals(61, okno.getProjekt().getPocetUkolu());
	}
	
	@Test
	public void testSchovaniStatistik1() throws InterruptedException {		
		okno.statistikyMenu.doClick();
		assertFalse(okno.panelGrafu.statistikyVisible);
	}
	
	@Test
	public void testSchovaniStatistik2() throws InterruptedException {		
		okno.statistikyMenu.doClick();
		okno.statistikyMenu.doClick();
		assertTrue(okno.panelGrafu.statistikyVisible);
	}
	
	@Test
	public void testSchovaniFiltru1() throws InterruptedException {		
		okno.cbTypFiltru.setSelectedIndex(0);
		okno.btSipkaFiltry.doClick();
		assertFalse(okno.filtry.isSelected());
	}
	
	@Test
	public void testSchovaniFiltru2() throws InterruptedException {		
		okno.cbTypFiltru.setSelectedIndex(0);
		assertTrue(okno.filtry.isSelected());	
	}
	
	@Test
	public void testSchovaniFiltru3() throws InterruptedException {		
		okno.cbTypFiltru.setSelectedIndex(0);
		okno.btSipkaFiltry.doClick();
		okno.btSipkaFiltry.doClick();
		assertTrue(okno.filtry.isSelected());
	}
	
}
