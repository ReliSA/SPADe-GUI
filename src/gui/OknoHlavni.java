package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.keypoint.PngEncoder;

import ostatni.Konstanty;
import data.*;
import data.ciselnik.*;
import data.polozky.PolozkaVytvoreni;
import databaze.CustomGrafDAO;
import databaze.IProjektDAO;
import databaze.ProjektDAO;

/**
 * Hlavní okno na které se dodávají jednotlivé panely, dědí ze třídy JFrame
 * 
 * @author michalvselko
 */
public class OknoHlavni extends JFrame {

	private JPanel panelMenu; // levý panel pro výběr projektu a dodání filtrů
	private JPanel listaFiltru;
	private JPanel panelProjektMenu;
	private PanelGrafu panelGrafu; // centrální panel zobrazující statistiky a grafy
	private JMenuBar menuBar; // horní ovládací bar

	private DefaultComboBoxModel<Projekt> modelProjekt = new DefaultComboBoxModel<Projekt>(); // model pro seznam
																								// projektů
	private ComboBoxDynamicky lsSeznamProjektu = new ComboBoxDynamicky(modelProjekt); // seznam projektů
	private JComboBox<String> cbTypFiltru = new JComboBox<String>(Konstanty.POLE_FILTRU); // seznam možných filtrů
	private JComboBox<String> cbTypPodfiltru = new JComboBox<String>(Konstanty.POLE_PODFILTRU);
	private JPanel pnBoxFiltru = new JPanel(); // box s vybranými filtry
	private JButton btZapniFiltr = new JButton(Konstanty.POPISY.getProperty("tlacitkoZapniFiltr")); // tlačítko pro
																									// spuštění podmínek
	private JButton btSipkaFiltry;
	private JScrollPane scScrollFiltru;
	private JMenu fileMenu; // Tlačítko menu baru
	private JMenu customGrafMenu; // Tlačítko menu baru
	private JMenuItem vytvorGraf; // Tlačítko menu baru
	private JMenu settingsMenu; // Tlačítko menu baru
	private JMenu languageMenu; // Tlačítko menu baru
	private JMenuItem exitAction; // Tlačítko menu baru
	private JMenuItem czech; // Tlačítko menu baru
	private JMenuItem english; // Tlačítko menu baru
	private JCheckBoxMenuItem filtry; // Tlačítko menu baru
	private int polohaPaneluUkol;

	/**
	 * Konstruktor třídy, naplní ve třídě konstant připojení, načte projekty a
	 * nastaví zobrazení a akce
	 * 
	 * @param pripojeni
	 *            připojení k databázi
	 */
	public OknoHlavni(Connection pripojeni) {
		Konstanty.PRIPOJENI = pripojeni;
		nactiProjekty();
		nastavZobrazeni();
		this.setVisible(true);
	}

	/**
	 * Vrátí vybraný projekt
	 * 
	 * @return vybraný projekt
	 */
	public Projekt getProjekt() {
		return (Projekt) this.modelProjekt.getSelectedItem();
	}

	/**
	 * Načte projekty z databáze, seznamy v projektu se načítají až po vybrání
	 * projektu v panelu menu
	 */
	private void nactiProjekty() {
		try {
			IProjektDAO databazeProjekt = new ProjektDAO();
			ArrayList<Projekt> projekty = databazeProjekt.getProjekt();
			for (int i = 0; i < projekty.size(); i++) {
				this.modelProjekt.addElement(projekty.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaNacteniProjektu"));
			e.printStackTrace();
		}
	}

	/**
	 * Nastaví zobrazení okna
	 */
	private void nastavZobrazeni() {
		nastavOkno(); // nastaví atributy okna

		fileMenu = new JMenu(Konstanty.POPISY.getProperty("menuSoubor"));
		customGrafMenu = new JMenu(Konstanty.POPISY.getProperty("menuCustomGraf"));
		settingsMenu = new JMenu(Konstanty.POPISY.getProperty("menuNastaveni"));
		languageMenu = new JMenu(Konstanty.POPISY.getProperty("menuJazyk"));
		exitAction = new JMenuItem(Konstanty.POPISY.getProperty("menuExit"));
		vytvorGraf = new JMenuItem(Konstanty.POPISY.getProperty("menuVytvorGraf"));
		czech = new JMenuItem(Konstanty.POPISY.getProperty("menuCestina"));
		english = new JMenuItem(Konstanty.POPISY.getProperty("menuAnglictina"));
		filtry = new JCheckBoxMenuItem(Konstanty.POPISY.getProperty("filtryTrue"), false);

		JLabel nadpis = new JLabel(Konstanty.POPISY.getProperty("nadpisFiltru"));
		panelGrafu = new PanelGrafu(this.getProjekt());
		panelMenu = new JPanel();
		panelProjektMenu = new JPanel(new BorderLayout());
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JPanel pnFiltr = new JPanel();
		scScrollFiltru = new JScrollPane(pnBoxFiltru, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnBoxFiltru.setLayout(new BoxLayout(pnBoxFiltru, BoxLayout.PAGE_AXIS));

		panelMenu.setPreferredSize(Konstanty.VELIKOST_PANELU_MENU);
		panelProjektMenu.add(panelMenu, BorderLayout.WEST);
		listaFiltru = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		panelProjektMenu.add(listaFiltru, BorderLayout.CENTER);
		panelMenu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		nadpis.setFont(Konstanty.FONT_NADPIS);

		btSipkaFiltry = new JButton("v");
		btSipkaFiltry.setPreferredSize(Konstanty.VELIKOST_SIPKY_FILTRY);
		btSipkaFiltry.setFont(new Font("Arial", Font.PLAIN, 15));
		btSipkaFiltry.setMargin(new Insets(0, 0, 0, 0));
		panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);

		pnFiltr.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		pnFiltr.add(cbTypFiltru);
		pnFiltr.add(btZapniFiltr);
		pnBoxFiltru.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleFiltrVyberu")));

		lsSeznamProjektu.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);

		scScrollFiltru.setPreferredSize(new Dimension(270, 225));
		scScrollFiltru.getVerticalScrollBar().setUnitIncrement(15);

		panelMenu.add(nadpis);
		panelMenu.add(lsSeznamProjektu);
		panelProjektMenu.add(pnFiltr, BorderLayout.EAST);

		menuBar.add(fileMenu);
		menuBar.add(customGrafMenu);
		menuBar.add(settingsMenu);
		settingsMenu.add(languageMenu);
		settingsMenu.add(filtry);
		fileMenu.add(exitAction);
		customGrafMenu.add(vytvorGraf);
		languageMenu.add(czech);
		languageMenu.add(english);

		this.setLayout(new BorderLayout());
		this.add(panelProjektMenu, BorderLayout.NORTH);
		this.add(panelGrafu, BorderLayout.CENTER);

		nastavAkce(); // nastaví akce k jednotlivým komponentám
	}

	/**
	 * Nastaví atributy okna a akci po ukončení
	 */
	private void nastavOkno() {
		this.setSize(Konstanty.VELIKOST_OKNA);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setMinimumSize(Konstanty.MINIMALNI_VELIKOST_OKNA);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SPADe");
	}

	private TlacitkoMazaniFiltru pridejTlacitkoListyFiltru(String nazev, Component comp) {

		for (int i = 0; i < listaFiltru.getComponentCount(); i++) {
			if (listaFiltru.getComponent(i).getName().equals(nazev)) {
				listaFiltru.remove(i);
			}
		}

		TlacitkoMazaniFiltru tlacitkoMazaniFilru = new TlacitkoMazaniFiltru(comp);
		tlacitkoMazaniFilru.setName(nazev);
		tlacitkoMazaniFilru.setText(nazev);
		listaFiltru.add(tlacitkoMazaniFilru);
		tlacitkoMazaniFilru.setContentAreaFilled(false);
		tlacitkoMazaniFilru.setOpaque(true);

		nazev = nazev.substring(0, nazev.length() - 2);
		switch (nazev) {
		case "Tasks":
		case "Úkoly":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Priorities":
		case "Priority":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Severity":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Status":
		case "Statusy":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Types":
		case "Typy":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Resolution":
		case "Rezoluce":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		case "Persons":
		case "Osoby":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaOsoby);
			break;
		case "Phase":
		case "Fáze":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaFaze);
			break;
		case "Iterations":
		case "Iterace":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaIterace);
			break;
		case "Activities":
		case "Aktivity":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaAktivity);
			break;
		case "Configurations":
		case "Konfigurace":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaKonfigurace);
			break;
		case "Artifacts":
		case "Artefakty":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaArtefakty);
			break;
		case "Time":
		case "Čas":
			tlacitkoMazaniFilru.setBackground(Konstanty.barvaUkol);
			break;
		default:
			break;
		}

		if (listaFiltru.getComponentCount() > 1) {
			if (!(listaFiltru.getComponent(0).getName().equals(Konstanty.POPISY.getProperty("smazVse")))) {
				JButton tlacitkoMazaniVse = new JButton();
				tlacitkoMazaniVse.setName(Konstanty.POPISY.getProperty("smazVse"));
				tlacitkoMazaniVse.setText(Konstanty.POPISY.getProperty("smazVse"));
				tlacitkoMazaniVse.setBackground(Color.WHITE);
				listaFiltru.add(tlacitkoMazaniVse);
				listaFiltru.setComponentZOrder(tlacitkoMazaniVse, 0);
				tlacitkoMazaniVse.setContentAreaFilled(false);
				tlacitkoMazaniVse.setOpaque(true);
				tlacitkoMazaniVse.addActionListener(actSmazVsechnyFiltry);
			}
		}

		return tlacitkoMazaniFilru;
	}

	private void smazTlacitkoListyFiltru(String nazev) {

		for (int i = 0; i < listaFiltru.getComponentCount(); i++) {
			if (listaFiltru.getComponent(i).getName().equals(nazev)) {
				listaFiltru.remove(i);
			}
		}

		if (listaFiltru.getComponentCount() > 2) {
			listaFiltru.remove(0);
		}

		listaFiltru.repaint();
	}

	/* akce pro smazání všech filtrů */
	ActionListener actSmazVsechnyFiltry = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			Konstanty.CITAC_PROGRESU = 0;
			Thread t1 = new Thread(new Runnable() {
				public void run() {
					OknoProgresNacitani oknoProgres = new OknoProgresNacitani();
					while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
						oknoProgres.nastavProgres();
						if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
							break;
						}
						Thread.yield();
					}
					oknoProgres.setVisible(false);
				}
			});

			/* vlákno spustí hlavní okno programu */
			Thread t2 = new Thread(new Runnable() {
				public void run() {
					listaFiltru.removeAll();
					pnBoxFiltru.removeAll();
					panelGrafu.setProjekt(getProjekt());
					panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);
					listaFiltru.revalidate();
					listaFiltru.repaint();
				}
			});
			t1.start();
			t2.start();
		}
	};

	/**
	 * Metoda sloužící pro restart hlavního okna a nového načtení konfigurací.
	 */
	private void restartOkna() {
		setVisible(false);
		Konstanty.CITAC_PROGRESU = 0;
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				OknoProgresNacitani oknoProgres = new OknoProgresNacitani();
				while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
					oknoProgres.nastavProgres();
					if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
						break;
					}
					Thread.yield();
				}
				oknoProgres.setVisible(false);
			}
		});

		/* vlákno spustí hlavní okno programu */
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				new OknoHlavni(Konstanty.PRIPOJENI);
			}
		});
		t1.start();
		t2.start();
		dispose();
	}

	/**
	 * Nastaví akce k jednotlivým komponentám
	 */
	private void nastavAkce() {
		/* akce při změně projektu v panelu menu */
		ActionListener actZmenaProjektu = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {

						@Override
						public void run() {
							cbTypFiltru.setEnabled(false);
							btZapniFiltr.setEnabled(false);
							filtry.setSelected(false);
							panelGrafu.statistikyVisible = true;
							boolean dvakrat;
							if (pnBoxFiltru.getComponentCount() > 0) {
								dvakrat = false;
							} else {
								dvakrat = true;
							}

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									if (dvakrat) {
										break;
									} else {
										dvakrat = true;
										Konstanty.CITAC_PROGRESU = 0;
									}
								}
								Thread.yield();
							}
							cbTypFiltru.setEnabled(true);
							btZapniFiltr.setEnabled(true);
							oknoProgres.setVisible(false);
						}
					});
					/* vlákno načítá data do panelu grafů */
					Thread t2 = new Thread(new Runnable() {

						@Override
						public void run() {

							panelGrafu.setProjekt(getProjekt());

							ArrayList<Integer> seznamIdUkolu = null;
							ArrayList<Integer> seznamIdPriorit = null;
							ArrayList<Integer> seznamIdSeverit = null;
							ArrayList<Integer> seznamIdResoluci = null;
							ArrayList<Integer> seznamIdStatusu = null;
							ArrayList<Integer> seznamIdTypu = null;
							ArrayList<Integer> seznamIdOsob = null;
							ArrayList<Integer> seznamIdFazi = null;
							ArrayList<Integer> seznamIdIteraci = null;
							ArrayList<Integer> seznamIdAktivit = null;
							ArrayList<Integer> seznamIdKonfiguraci = null;
							ArrayList<Integer> seznamIdArtefaktu = null;
							String[] operandy = new String[] { "and", "and", "and", "and", "and", "and" };

							for (int i = 0; i < pnBoxFiltru.getComponentCount(); i++) {
								PanelFiltr pnPanelFiltr = (PanelFiltr) (pnBoxFiltru.getComponents()[i]);
								if (pnPanelFiltr.jePouzit()) {
									switch (pnPanelFiltr.getName()) {
									case "Tasks":
									case "Úkoly":
										if (getProjekt().getUkoly().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozUkol"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaPocatek) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getUkoly());
											seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Priorities":
									case "Priority":
										if ((new Priority(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozPriority"));
										} else {
											seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Priority(getProjekt().getID())).getSeznam());
											operandy[0] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Severity":
										if ((new Severity(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozSeverity"));
										} else {
											seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Severity(getProjekt().getID())).getSeznam());
											operandy[1] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Status":
									case "Statusy":
										if ((new Status(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozStatusy"));
										} else {
											seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Status(getProjekt().getID())).getSeznam());
											operandy[2] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Types":
									case "Typy":
										if ((new Typ(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozTypy"));
										} else {
											seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Typ(getProjekt().getID())).getSeznam());
											operandy[3] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Resolution":
									case "Rezoluce":
										if ((new Resoluce(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozResoluce"));
										} else {
											seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Resoluce(getProjekt().getID())).getSeznam());
											operandy[4] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Persons":
									case "Osoby":
										if ((new Osoby(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozOsoby"));
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Osoby(getProjekt().getID())).getSeznam());
											operandy[5] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										}
										break;
									case "Phase":
									case "Fáze":
										if (getProjekt().getFaze().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozFaze"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaPocatek) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getFaze());
											seznamIdFazi = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Iterations":
									case "Iterace":
										if (getProjekt().getIterace().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozIterace"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaPocatek) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getIterace());
											seznamIdIteraci = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Activities":
									case "Aktivity":
										if (getProjekt().getAktivity().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozAktivity"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaPocatek) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getAktivity());
											seznamIdAktivit = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Configurations":
									case "Konfigurace":
										if (getProjekt().getKonfigurace().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozKonfigurace"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaVytvoreni) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getKonfigurace());
											seznamIdKonfiguraci = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr)
													.getSeznamId();
										}
										break;
									case "Artifacts":
									case "Artefakty":
										if (getProjekt().getArtefakty().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozArtefakty"));
											pnBoxFiltru.remove(i--);
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrPolozkaVytvoreniArtefakt) pnPanelFiltr)
													.nactiNovyProjekt(getProjekt().getArtefakty());
											seznamIdArtefaktu = ((PanelFiltrPolozkaVytvoreniArtefakt) pnPanelFiltr)
													.getSeznamId();
										}
										break;
									default:
										break;
									}

								} else {
									pnBoxFiltru.remove(i--);
								}
							}
							if (pnBoxFiltru.getComponentCount() > 0) {
								/* spustí se nastavení podmínek a tím i nové načtení panelu grafů */
								panelGrafu.setPodminkyProjektu(seznamIdUkolu, operandy[0], seznamIdPriorit, operandy[1],
										seznamIdSeverit, operandy[2], seznamIdResoluci, operandy[3], seznamIdStatusu,
										operandy[4], seznamIdTypu, operandy[5], seznamIdOsob, seznamIdFazi,
										seznamIdIteraci, seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
							}

							filtry.setSelected(false);
							panelGrafu.statistikyVisible = true;
							pnBoxFiltru.revalidate();
							panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);

						}

					});
					t1.start();
					t2.start();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaVybraniProjektu"));
					e.printStackTrace();
				}
			}
		};

		/* akce pro tlačítko Přidej filtr */
		ActionListener actVlozFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					PanelFiltrPolozkaPocatek panelPridani = null;

					if (filtry.isSelected() == false) {
						panelGrafu.zobrazFiltry(scScrollFiltru);
						filtry.setSelected(true);
					}

					switch (cbTypFiltru.getSelectedIndex()) { // podle zadaného typu filtru vloží konkrétní panel filtru
					case 0:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevUkoly"), pnBoxFiltru.getComponents())) { // pokud
																														// je
																														// panel
																														// již
																														// vložen,
																														// nelze
																														// ho
																														// znovu
																														// dodat
							if (getProjekt().getUkoly().isEmpty()) // zkontroluje, zda projekt obsahuje data nutná k
																	// filtrování v daném panelu
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozUkol"));
							else
								panelPridani = new PanelFiltrPolozkaPocatek(Konstanty.POPISY.getProperty("nazevUkoly"),
										getProjekt().getUkoly(), cbTypPodfiltru);
							pnBoxFiltru.add(panelPridani);
							polohaPaneluUkol = pnBoxFiltru.getComponentZOrder(panelPridani);
						}
						break;

					case 1:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevFaze"), pnBoxFiltru.getComponents())) {
							if (getProjekt().getFaze().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozFaze"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatekSegment(
										Konstanty.POPISY.getProperty("nazevFaze"), getProjekt().getFaze()));
						}
						break;
					case 2:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevIterace"), pnBoxFiltru.getComponents())) {
							if (getProjekt().getIterace().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozIterace"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatekSegment(
										Konstanty.POPISY.getProperty("nazevIterace"), getProjekt().getIterace()));
						}
						break;
					case 3:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevAktivity"),
								pnBoxFiltru.getComponents())) {
							if (getProjekt().getAktivity().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozAktivity"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatekSegment(
										Konstanty.POPISY.getProperty("nazevAktivity"), getProjekt().getAktivity()));
						}
						break;
					case 4:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevKonfigurace"),
								pnBoxFiltru.getComponents())) {
							if (getProjekt().getKonfigurace().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozKonfigurace"));
							else
								pnBoxFiltru.add(
										new PanelFiltrPolozkaVytvoreni(Konstanty.POPISY.getProperty("nazevKonfigurace"),
												getProjekt().getKonfigurace()));
						}
						break;
					case 5:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevArtefakty"),
								pnBoxFiltru.getComponents())) {
							if (getProjekt().getArtefakty().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozArtefakty"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaVytvoreniArtefakt(
										Konstanty.POPISY.getProperty("nazevArtefakty"), getProjekt().getArtefakty()));
						}
						break;
					default:
						break;
					}
					pnBoxFiltru.revalidate();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaVlozFiltr"));
					e1.printStackTrace();
				}
			}
		};

		/* akce pro tlačítko Přidej filtr */
		ActionListener actVlozPodFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					switch (cbTypPodfiltru.getSelectedIndex()) { // podle zadaného typu filtru vloží konkrétní panel
																	// filtru
					case 0:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevPriority"),
								pnBoxFiltru.getComponents())) {
							if ((new Priority(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozPriority"));
							else
								pnBoxFiltru.add(
										new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevPriority"),
												(new Priority(getProjekt().getID())).getSeznam()),
										polohaPaneluUkol + 1);
						}
						break;
					case 1:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevSeverity"),
								pnBoxFiltru.getComponents())) {
							if ((new Severity(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozSeverity"));
							else
								pnBoxFiltru.add(
										new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevSeverity"),
												(new Severity(getProjekt().getID())).getSeznam()),
										polohaPaneluUkol + 1);
						}
						break;
					case 2:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevStatusy"), pnBoxFiltru.getComponents())) {
							if ((new Status(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozStatusy"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevStatusy"),
										(new Status(getProjekt().getID())).getSeznam()), polohaPaneluUkol + 1);
						}
						break;
					case 3:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevTypy"), pnBoxFiltru.getComponents())) {
							if ((new Typ(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozTypy"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevTypy"),
										(new Typ(getProjekt().getID())).getSeznam()), polohaPaneluUkol + 1);
						}
						break;
					case 4:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevResoluce"),
								pnBoxFiltru.getComponents())) {
							if ((new Resoluce(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozResoluce"));
							else
								pnBoxFiltru.add(
										new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevResoluce"),
												(new Resoluce(getProjekt().getID())).getSeznam()),
										polohaPaneluUkol + 1);
						}
						break;
					case 5:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevOsoby"), pnBoxFiltru.getComponents())) {
							if ((new Osoby(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozOsoby"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik(Konstanty.POPISY.getProperty("nazevOsoby"),
										(new Osoby(getProjekt().getID())).getSeznam()), polohaPaneluUkol + 1);
						}
						break;

					case 6:
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("cas"), pnBoxFiltru.getComponents())) {
							pnBoxFiltru.add(
									new PanelFiltrCas(Konstanty.POPISY.getProperty("cas"), getProjekt().getUkoly()),
									polohaPaneluUkol + 1);
						}
						break;
					default:
						break;
					}
					pnBoxFiltru.revalidate();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaVlozFiltr"));
					e1.printStackTrace();
				}
			}
		};

		ActionListener actSmazaniFiltru = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TlacitkoMazaniFiltru tlacitko = (TlacitkoMazaniFiltru) e.getSource();
				pnBoxFiltru.remove(tlacitko.getComp());
				listaFiltru.remove(tlacitko);

				/* Smaže tlačítko smazat všechny filtry pokud zbývá pouze jedno tlačítko */
				if (listaFiltru.getComponentCount() == 2) {
					listaFiltru.remove(0);
				}

				listaFiltru.revalidate();
				listaFiltru.repaint();

				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {
						public void run() {
							cbTypFiltru.setEnabled(false);
							btZapniFiltr.setEnabled(false);

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									break;
								}
								Thread.yield();
							}
							cbTypFiltru.setEnabled(true);
							btZapniFiltr.setEnabled(true);
							oknoProgres.setVisible(false);
						}

					});

					/*
					 * vlákno zjišťuje id odpovídající parametrům filtrů a spustí nové načtení
					 * panelu grafů, filtry které nemají zaškrtnuto Použít filtr se z boxu filtrů
					 * odmažou
					 */
					Thread t2 = new Thread(new Runnable() {

						@Override
						public void run() {
							String podminkaPriority = "";
							ArrayList<Integer> seznamIdUkolu = null;
							ArrayList<Integer> seznamIdPriorit = null;
							ArrayList<Integer> seznamIdSeverit = null;
							ArrayList<Integer> seznamIdResoluci = null;
							ArrayList<Integer> seznamIdStatusu = null;
							ArrayList<Integer> seznamIdTypu = null;
							ArrayList<Integer> seznamIdOsob = null;
							ArrayList<Integer> seznamIdFazi = null;
							ArrayList<Integer> seznamIdIteraci = null;
							ArrayList<Integer> seznamIdAktivit = null;
							ArrayList<Integer> seznamIdKonfiguraci = null;
							ArrayList<Integer> seznamIdArtefaktu = null;
							String[] operandy = new String[] { "and", "and", "and", "and", "and", "and" };

							for (int i = 0; i < pnBoxFiltru.getComponentCount(); i++) { // prochází všechny vložené
																						// filtry
								PanelFiltr pnPanelFiltr = (PanelFiltr) (pnBoxFiltru.getComponents()[i]);
								if (pnPanelFiltr.jePouzit()) { // je-li zaškrtnuto použít, zjistí se o jaký filtr jde
									switch (pnPanelFiltr.getName()) { // pomocí názvu se zjišťuje o jaký jde filtr
									case "Tasks":
									case "Úkoly":
										seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId(); // zjistí
																													// seznam
																													// id
																													// odpovídající
																													// zadaným
																													// podmínkám
										break;
									case "Priorities":
									case "Priority":
										seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[0] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Severity":
										seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[1] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Status":
									case "Statusy":
										seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[2] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Types":
									case "Typy":
										seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[3] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Resolution":
									case "Rezoluce":
										seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[4] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Persons":
									case "Osoby":
										seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										operandy[5] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Phase":
									case "Fáze":
										seznamIdFazi = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Iterations":
									case "Iterace":
										seznamIdIteraci = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Activities":
									case "Aktivity":
										seznamIdAktivit = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Configurations":
									case "Konfigurace":
										seznamIdKonfiguraci = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr).getSeznamId();
										break;
									case "Artifacts":
									case "Artefakty":
										seznamIdArtefaktu = ((PanelFiltrPolozkaVytvoreniArtefakt) pnPanelFiltr)
												.getSeznamId();
										break;
									case "Time":
									case "Čas":
										seznamIdUkolu.retainAll(((PanelFiltrCas) pnPanelFiltr).getSeznamId());
										if (seznamIdUkolu.isEmpty()) {
											seznamIdUkolu.add(-1);
										}
										break;
									default:
										break;
									}

								} else {
									pnBoxFiltru.remove(i--); // není-li zaškrtnuto Použít filtr, filtr se z boxu smaže
																// kontrola musí přejí o jedno míst zpět (před smazaný
																// filtr)
								}
							}
							filtry.setSelected(false);
							panelGrafu.statistikyVisible = true;
							pnBoxFiltru.revalidate();
							/* spustí se nastavení podmínek a tím i nové načtení panelu grafů */
							panelGrafu.setPodminkyProjektu(seznamIdUkolu, operandy[0], seznamIdPriorit, operandy[1],
									seznamIdSeverit, operandy[2], seznamIdResoluci, operandy[3], seznamIdStatusu,
									operandy[4], seznamIdTypu, operandy[5], seznamIdOsob, seznamIdFazi, seznamIdIteraci,
									seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
							panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);
						}

					});
					t1.start();
					t2.start();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaPodminekFiltru"));
					e1.printStackTrace();
				}
			}
		};

		/* akce pro spuštění zadaných filtrů */
		ActionListener actZapniFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {
						public void run() {
							cbTypFiltru.setEnabled(false);
							btZapniFiltr.setEnabled(false);

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									break;
								}
								Thread.yield();
							}
							cbTypFiltru.setEnabled(true);
							btZapniFiltr.setEnabled(true);
							oknoProgres.setVisible(false);
						}

					});

					/*
					 * vlákno zjišťuje id odpovídající parametrům filtrů a spustí nové načtení
					 * panelu grafů, filtry které nemají zaškrtnuto Použít filtr se z boxu filtrů
					 * odmažou
					 */
					Thread t2 = new Thread(new Runnable() {

						@Override
						public void run() {
							String podminkaPriority = "";
							ArrayList<Integer> seznamIdUkolu = null;
							ArrayList<Integer> seznamIdPriorit = null;
							ArrayList<Integer> seznamIdSeverit = null;
							ArrayList<Integer> seznamIdResoluci = null;
							ArrayList<Integer> seznamIdStatusu = null;
							ArrayList<Integer> seznamIdTypu = null;
							ArrayList<Integer> seznamIdOsob = null;
							ArrayList<Integer> seznamIdFazi = null;
							ArrayList<Integer> seznamIdIteraci = null;
							ArrayList<Integer> seznamIdAktivit = null;
							ArrayList<Integer> seznamIdKonfiguraci = null;
							ArrayList<Integer> seznamIdArtefaktu = null;
							String[] operandy = new String[] { "and", "and", "and", "and", "and", "and" };

							for (int i = 0; i < pnBoxFiltru.getComponentCount(); i++) { // prochází všechny vložené
																						// filtry
								PanelFiltr pnPanelFiltr = (PanelFiltr) (pnBoxFiltru.getComponents()[i]);
								if (pnPanelFiltr.jePouzit()) { // je-li zaškrtnuto použít, zjistí se o jaký filtr jde
									switch (pnPanelFiltr.getName()) { // pomocí názvu se zjišťuje o jaký jde filtr
									case "Tasks":
									case "Úkoly":
										seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId(); // zjistí
																													// id
																													// odpovídající
																													// zadaným
																													// podmínkám
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevUkoly") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Priorities":
									case "Priority":
										seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevPriority") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[0] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Severity":
										seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevSeverity") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[1] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Status":
									case "Statusy":
										seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevStatusy") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[2] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Types":
									case "Typy":
										seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevTypy") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[3] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Resolution":
									case "Rezoluce":
										seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevResoluce") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[4] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Persons":
									case "Osoby":
										seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevOsoby") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										operandy[5] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
										break;
									case "Phase":
									case "Fáze":
										seznamIdFazi = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevFaze") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Iterations":
									case "Iterace":
										seznamIdIteraci = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevIterace") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Activities":
									case "Aktivity":
										seznamIdAktivit = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevAktivity") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Configurations":
									case "Konfigurace":
										seznamIdKonfiguraci = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr).getSeznamId();
										pridejTlacitkoListyFiltru(
												Konstanty.POPISY.getProperty("nazevKonfigurace") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Artifacts":
									case "Artefakty":
										seznamIdArtefaktu = ((PanelFiltrPolozkaVytvoreniArtefakt) pnPanelFiltr)
												.getSeznamId();
										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("nazevArtefakty") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									case "Time":
									case "Čas":
										seznamIdUkolu.retainAll(((PanelFiltrCas) pnPanelFiltr).getSeznamId());

										if (seznamIdUkolu.isEmpty()) {
											seznamIdUkolu.add(-1);
										}

										pridejTlacitkoListyFiltru(Konstanty.POPISY.getProperty("cas") + " X",
												pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
										break;
									default:
										break;
									}

								} else {
									smazTlacitkoListyFiltru(pnBoxFiltru.getComponents()[i].getName() + " X");
									pnBoxFiltru.remove(i--); // není-li zaškrtnuto Použít filtr, filtr se z boxu smaže
																// kontrola musí přejí o jedno míst zpět (před smazaný
																// filtr)

								}
							}
							filtry.setSelected(false);
							panelGrafu.statistikyVisible = true;
							pnBoxFiltru.revalidate();
							/* spustí se nastavení podmínek a tím i nové načtení panelu grafů */
							panelGrafu.setPodminkyProjektu(seznamIdUkolu, operandy[0], seznamIdPriorit, operandy[1],
									seznamIdSeverit, operandy[2], seznamIdResoluci, operandy[3], seznamIdStatusu,
									operandy[4], seznamIdTypu, operandy[5], seznamIdOsob, seznamIdFazi, seznamIdIteraci,
									seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
							panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);
						}

					});
					t1.start();
					t2.start();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaPodminekFiltru"));
					e1.printStackTrace();
				}
			}

		};

		/* akce pro změnu velikosti panelu grafů */
		ComponentAdapter actResizePaneluGrafu = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				panelGrafu.setSizeScroll();
				panelGrafu.revalidate();
			}
		};

		/* akce pro ukončení spojení s databází pri ukončení okna */
		WindowAdapter actUkonceniOkna = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					Konstanty.PRIPOJENI.close();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkonceniSpojeni"));
					e1.printStackTrace();
					System.exit(1);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkonceniSpojeni"));
					e1.printStackTrace();
					System.exit(1);
				}
			}
		};

		/* akce pro zavření okna a připojení k databázi */
		ActionListener actZavri = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Konstanty.PRIPOJENI.close();
					System.exit(0);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkonceniSpojeni"));
					e1.printStackTrace();
					System.exit(1);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkonceniSpojeni"));
					e1.printStackTrace();
					System.exit(1);
				}
			}
		};

		/* akce pro změnu jazyka na angličtinu */
		ActionListener actJazykEnglish = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Konstanty.POPISY.load(
							new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH), "UTF8"));
					Konstanty.nastavPopisComboBox();
					cbTypFiltru.setModel(new DefaultComboBoxModel(Konstanty.POLE_FILTRU));
					restartOkna();

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaSoubor")
							+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH + Konstanty.POPISY.getProperty("chybaSoubor2"));
					e1.printStackTrace();
					System.exit(0);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,
							Konstanty.POPISY.getProperty("chybaNacitaniSouboru")
									+ Konstanty.NAZEV_SOUBORU_POPISU_ENGLISH
									+ Konstanty.POPISY.getProperty("chybaNacitaniSouboru2"));
					e1.printStackTrace();
					System.exit(0);
				}

			}
		};

		/* akce pro změnu jazyka na čestinu */
		ActionListener actJazykCzech = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					Konstanty.POPISY.load(
							new InputStreamReader(new FileInputStream(Konstanty.NAZEV_SOUBORU_POPISU_CZECH), "UTF8"));
					Konstanty.nastavPopisComboBox();
					cbTypFiltru.setModel(new DefaultComboBoxModel(Konstanty.POLE_FILTRU));
					restartOkna();

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaSoubor")
							+ Konstanty.NAZEV_SOUBORU_POPISU_CZECH + Konstanty.POPISY.getProperty("chybaSoubor2"));
					e1.printStackTrace();
					System.exit(0);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,
							Konstanty.POPISY.getProperty("chybaNacitaniSouboru") + Konstanty.NAZEV_SOUBORU_POPISU_CZECH
									+ Konstanty.POPISY.getProperty("chybaNacitaniSouboru2"));
					e1.printStackTrace();
					System.exit(0);
				}

			}
		};

		/* akce pro schování panelu filtrů */
		ActionListener actZobrazeniFiltruMenu = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filtry.isSelected() == true) {
					panelGrafu.zobrazFiltry(scScrollFiltru);
				} else {
					panelGrafu.schovejFiltry(scScrollFiltru);
				}
			}
		};

		/* akce pro schování panelu filtrů */
		ActionListener actZobrazeniFiltruButton = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (filtry.isSelected() == false) {
					panelGrafu.zobrazFiltry(scScrollFiltru);
					btSipkaFiltry.setText("^");
					filtry.setSelected(true);
				} else {
					panelGrafu.schovejFiltry(scScrollFiltru);
					btSipkaFiltry.setText("v");
					filtry.setSelected(false);
				}
			}
		};

		/* akce pro smazání všech filtrů */
		ActionListener actSmazVsechnyFiltry = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listaFiltru.removeAll();
				pnBoxFiltru.removeAll();
				panelGrafu.setProjekt(getProjekt());
			}
		};

		
		/* akce pro otevření okna custom grafů */
		ActionListener actCustomGraf = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingUtilities.invokeLater(() -> {
					OknoCustomGraf example = new OknoCustomGraf(getProjekt());
					example.setSize(800, 400);
					example.setLocationRelativeTo(null);
					example.setVisible(true);
				});

			}
		};

		/* vložení akcí k příslušným komponentám */
		lsSeznamProjektu.addActionListener(actZmenaProjektu);
		cbTypFiltru.addActionListener(actVlozFiltr);
		cbTypPodfiltru.addActionListener(actVlozPodFiltr);
		btZapniFiltr.addActionListener(actZapniFiltr);
		exitAction.addActionListener(actZavri);
		czech.addActionListener(actJazykCzech);
		english.addActionListener(actJazykEnglish);
		filtry.addActionListener(actZobrazeniFiltruMenu);
		btSipkaFiltry.addActionListener(actZobrazeniFiltruButton);
		vytvorGraf.addActionListener(actCustomGraf);

		this.addComponentListener(actResizePaneluGrafu);
		this.addWindowListener(actUkonceniOkna);

	}

	/**
	 * Zjistí podle názvu filtru, zda je filtr již zadaný
	 * 
	 * @param nazevFiltru
	 *            název filtru který se hledá
	 * @param ulozeneKomponenty
	 *            seznam zadaných filtrů
	 * @return true pokud je filtr již v box filtrů
	 */
	private boolean jeZadanyFiltr(String nazevFiltru, Component[] ulozeneKomponenty) {
		boolean vystup = false;
		for (int i = 0; i < ulozeneKomponenty.length; i++) {
			if (nazevFiltru.equals(ulozeneKomponenty[i].getName())) {
				vystup = true;
				break;
			}
		}
		return vystup;
	}

}
