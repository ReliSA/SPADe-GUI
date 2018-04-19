package test;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.UIManager;
import org.junit.Before;
import org.junit.Test;

import gui.OknoPrihlasovani;
import ostatni.Konstanty;

public class OknoPrihlasovaniTest {

	@Before
	public void setUp() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Konstanty.POPISY.load(new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH), "UTF8"));		
	}

	@Test
	public void test() {
		OknoPrihlasovani okno = new OknoPrihlasovani();
		assertTrue(okno.isVisible());
	}
	
	@Test
	public void testPrihlasit1() {
		OknoPrihlasovani okno = new OknoPrihlasovani();
		okno.prihlasit("ppicha", "Phjdhg3h3gws.s");
		assertFalse(okno.isVisible());
	}

	@Test
	public void testPrihlasit2() {
		OknoPrihlasovani okno = new OknoPrihlasovani();
		Konstanty.CITAC_PROGRESU = 10; 
		okno.prihlasit("ppicha", "Phjdhg3h3gws.s");
	}

	@Test
	public void testPrihlasit4() {
		OknoPrihlasovani okno = new OknoPrihlasovani();
		okno.tfLogin.setText("");
		okno.btPrihlasit.doClick();
	}

	@Test
	public void testPrihlasit5() {
		OknoPrihlasovani okno = new OknoPrihlasovani();
		okno.tfLogin.setText("ppicha");
		okno.pfHeslo.setText("Phjdhg3h3gws.s");
		
		okno.btPrihlasit.doClick();
	}
	
}
