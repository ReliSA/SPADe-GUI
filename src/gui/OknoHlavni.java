package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
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
import java.util.concurrent.SynchronousQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
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

import ostatni.Konstanty;
import data.*;
import data.ciselnik.*;
import databaze.IProjektDAO;
import databaze.ProjektDAO;

/**
 * Hlavní okno na které se dodávají jednotlivé panely, dědí ze třídy JFrame
 * 
 * @author michalvselko
 */
public class OknoHlavni extends JFrame {

	private JPanel panelMenu; // levý panel pro výběr projektu a dodání filtrů
	private PanelGrafu panelGrafu; // centrální panel zobrazující statistiky a grafy
	private JPanel centralPanel;

	private JMenuBar menuBar; // horní ovládací bar

	private DefaultComboBoxModel<Projekt> modelProjekt = new DefaultComboBoxModel<Projekt>(); // model pro seznam
																								// projektů
	private ComboBoxDynamicky lsSeznamProjektu = new ComboBoxDynamicky(modelProjekt); // seznam projektů
	private JComboBox<String> cbTypFiltru = new JComboBox<String>(Konstanty.POLE_FILTRU); // seznam možných filtrů
	
	private JButton btPridejFiltr = new JButton(Konstanty.POPISY.getProperty("tlacitkoPridejFiltr")); // tlačítko pro
																										// přidání
																										// filtru do
																										// panelu menu
	private Box pnBoxFiltru = Box.createVerticalBox(); // box s vybranými filtry
	private JButton btZapniFiltr = new JButton(Konstanty.POPISY.getProperty("tlacitkoZapniFiltr")); // tlačítko pro
																									// spuštění podmínek
	
	private JButton btSipkaFiltr = new JButton("<");
	private JMenu fileMenu; // Tlačítko menu baru
	private JMenu settingsMenu; // Tlačítko menu baru
	private JMenu languageMenu; // Tlačítko menu baru
	private JMenuItem exitAction; // Tlačítko menu baru
	private JMenuItem czech; // Tlačítko menu baru
	private JMenuItem english; // Tlačítko menu baru
	private JCheckBoxMenuItem filtry; // Tlačítko menu baru

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
		settingsMenu = new JMenu(Konstanty.POPISY.getProperty("menuNastaveni"));
		languageMenu = new JMenu(Konstanty.POPISY.getProperty("menuJazyk"));
		exitAction = new JMenuItem(Konstanty.POPISY.getProperty("menuExit"));
		czech = new JMenuItem(Konstanty.POPISY.getProperty("menuCestina"));
		english = new JMenuItem(Konstanty.POPISY.getProperty("menuAnglictina"));
		filtry = new JCheckBoxMenuItem(Konstanty.POPISY.getProperty("filtryTrue"), true);

		JLabel nadpis = new JLabel(Konstanty.POPISY.getProperty("nadpisFiltru"));
		panelGrafu = new PanelGrafu(this.getProjekt());
		panelMenu = new JPanel();
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JPanel pnFiltr = new JPanel();
		JScrollPane scScrollFiltru = new JScrollPane(pnBoxFiltru, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		nastavAkce(); // nastaví akce k jednotlivým komponentám
		panelMenu.setPreferredSize(Konstanty.VELIKOST_PANELU_MENU);
		panelMenu.setLayout(Konstanty.FLOW_LAYOUT);
		nadpis.setFont(Konstanty.FONT_NADPIS);

		pnFiltr.setLayout(Konstanty.FLOW_LAYOUT);
		pnFiltr.add(cbTypFiltru);
		pnFiltr.add(btPridejFiltr);
		pnBoxFiltru.add(pnFiltr);
		pnBoxFiltru.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleFiltrVyberu")));

		lsSeznamProjektu.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);

		btPridejFiltr.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
		btZapniFiltr.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);

		scScrollFiltru.setPreferredSize(new Dimension(270, 550));
		
		btSipkaFiltr.setPreferredSize(Konstanty.VELIKOST_SIPKY);
		btSipkaFiltr.setFont(new Font("Arial", Font.PLAIN, 15));
		btSipkaFiltr.setMargin(new Insets(0, 0, 0, 0));
		panelMenu.add(nadpis);
		panelMenu.add(lsSeznamProjektu);
		panelMenu.add(scScrollFiltru);
		panelMenu.add(btZapniFiltr);

		menuBar.add(fileMenu);
		menuBar.add(settingsMenu);
		settingsMenu.add(languageMenu);
		settingsMenu.add(filtry);
		fileMenu.add(exitAction);
		languageMenu.add(czech);
		languageMenu.add(english);

		centralPanel = new JPanel(new BorderLayout());
		centralPanel.add(panelGrafu, BorderLayout.CENTER);
		centralPanel.add(btSipkaFiltr, BorderLayout.WEST);

		this.setLayout(new BorderLayout());
		this.add(panelMenu, BorderLayout.WEST);
		this.add(centralPanel, BorderLayout.CENTER);
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

	private void odstranPanelFiltru() {
		this.remove(panelMenu);

		if (this.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			this.setExtendedState(JFrame.ICONIFIED);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			this.setSize(getWidth() + 1, getHeight() + 1);
		}
	}

	private void pridejPanelFiltru() {

		this.add(panelMenu, BorderLayout.WEST);
		if (this.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			this.setExtendedState(JFrame.ICONIFIED);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			this.setSize(getWidth() + 1, getHeight() + 1);
		}
	}

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
							btPridejFiltr.setEnabled(false);
							btZapniFiltr.setEnabled(false);

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									break;
								}
								Thread.yield();
							}
							btPridejFiltr.setEnabled(true);
							btZapniFiltr.setEnabled(true);
							oknoProgres.setVisible(false);
						}
					});
					/* vlákno načítá data do panelu grafů */
					Thread t2 = new Thread(new Runnable() {

						@Override
						public void run() {
							for (int i = 0; i < pnBoxFiltru.getComponentCount(); i++) {
								if (pnBoxFiltru.getComponents()[i] instanceof PanelFiltr)
									pnBoxFiltru.remove(i--);
							}
							panelGrafu.setProjekt(getProjekt());
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
					switch (cbTypFiltru.getSelectedIndex()) { // podle zadaného typu filtru vloží konkrétní panel filtru
					case 0:
						if (!jeZadanyFiltr("Ukol", pnBoxFiltru.getComponents())) { // pokud je panel již vložen, nelze
																					// ho znovu dodat
							if (getProjekt().getUkoly().isEmpty()) // zkontroluje, zda projekt obsahuje data nutná k
																	// filtrování v daném panelu
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozUkol"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatek("Ukol", getProjekt().getUkoly())); // do
																												// boxu
																												// filtrů
																												// vloží
																												// panel
																												// konkrétním
																												// filtrem
						}
						break;
					case 1:
						if (!jeZadanyFiltr("Priority", pnBoxFiltru.getComponents())) {
							if ((new Priority(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozPriority"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik("Priority",
										(new Priority(getProjekt().getID())).getSeznam()));
						}
						break;
					case 2:
						if (!jeZadanyFiltr("Severity", pnBoxFiltru.getComponents())) {
							if ((new Severity(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozSeverity"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik("Severity",
										(new Severity(getProjekt().getID())).getSeznam()));
						}
						break;
					case 3:
						if (!jeZadanyFiltr("Status", pnBoxFiltru.getComponents())) {
							if ((new Status(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozStatusy"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik("Status",
										(new Status(getProjekt().getID())).getSeznam()));
						}
						break;
					case 4:
						if (!jeZadanyFiltr("Typ", pnBoxFiltru.getComponents())) {
							if ((new Typ(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozTypy"));
							else
								pnBoxFiltru.add(
										new PanelFiltrCiselnik("Typ", (new Typ(getProjekt().getID())).getSeznam()));
						}
						break;
					case 5:
						if (!jeZadanyFiltr("Resoluce", pnBoxFiltru.getComponents())) {
							if ((new Resoluce(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozResoluce"));
							else
								pnBoxFiltru.add(new PanelFiltrCiselnik("Resoluce",
										(new Resoluce(getProjekt().getID())).getSeznam()));
						}
						break;
					case 6:
						if (!jeZadanyFiltr("Osoby", pnBoxFiltru.getComponents())) {
							if ((new Osoby(getProjekt().getID())).getSeznam().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozOsoby"));
							else
								pnBoxFiltru.add(
										new PanelFiltrCiselnik("Osoby", (new Osoby(getProjekt().getID())).getSeznam()));
						}
						break;
					case 7:
						if (!jeZadanyFiltr("Faze", pnBoxFiltru.getComponents())) {
							if (getProjekt().getFaze().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozFaze"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatek("Faze", getProjekt().getFaze()));
						}
						break;
					case 8:
						if (!jeZadanyFiltr("Iterace", pnBoxFiltru.getComponents())) {
							if (getProjekt().getIterace().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozIterace"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatek("Iterace", getProjekt().getIterace()));
						}
						break;
					case 9:
						if (!jeZadanyFiltr("Aktivity", pnBoxFiltru.getComponents())) {
							if (getProjekt().getAktivity().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozAktivity"));
							else
								pnBoxFiltru.add(new PanelFiltrPolozkaPocatek("Aktivity", getProjekt().getAktivity()));
						}
						break;
					case 10:
						if (!jeZadanyFiltr("Konfigurace", pnBoxFiltru.getComponents())) {
							if (getProjekt().getKonfigurace().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozKonfigurace"));
							else
								pnBoxFiltru.add(
										new PanelFiltrPolozkaVytvoreni("Konfigurace", getProjekt().getKonfigurace()));
						}
						break;
					case 11:
						if (!jeZadanyFiltr("Artefakty", pnBoxFiltru.getComponents())) {
							if (getProjekt().getArtefakty().isEmpty())
								JOptionPane.showMessageDialog(pnBoxFiltru,
										Konstanty.POPISY.getProperty("chybaVlozArtefakty"));
							else
								pnBoxFiltru
										.add(new PanelFiltrPolozkaVytvoreni("Artefakty", getProjekt().getArtefakty()));
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

		/* akce pro spuštění zadaných filtrů */
		ActionListener actZapniFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {
						public void run() {
							btPridejFiltr.setEnabled(false);
							btZapniFiltr.setEnabled(false);

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									break;
								}
								Thread.yield();
							}
							btPridejFiltr.setEnabled(true);
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
							// TODO Auto-generated method stub
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

							for (int i = 1; i < pnBoxFiltru.getComponentCount(); i++) { // prochází všechny vložené
																						// filtry
								PanelFiltr pnPanelFiltr = (PanelFiltr) (pnBoxFiltru.getComponents()[i]);
								if (pnPanelFiltr.jePouzit()) { // je-li zaškrtnuto použít, zjistí se o jaký filtr jde
									switch (pnPanelFiltr.getName()) { // pomocí názvu se zjišťuje o jaký jde filtr
									case "Ukol":
										seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId(); // zjistí
																													// seznam
																													// id
																													// odpovídající
																													// zadaným
																													// podmínkám
										break;
									case "Priority":
										seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Severity":
										seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Status":
										seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Typ":
										seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Resoluce":
										seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Osoby":
										seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										break;
									case "Faze":
										seznamIdFazi = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Iterace":
										seznamIdIteraci = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Aktivity":
										seznamIdAktivit = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
										break;
									case "Konfigurace":
										seznamIdKonfiguraci = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr).getSeznamId();
										break;
									case "Artefakty":
										seznamIdArtefaktu = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr).getSeznamId();
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
							pnBoxFiltru.revalidate();
							/* spustí se nastavení podmínek a tím i nové načtení panelu grafů */
							panelGrafu.setPodminkyProjektu(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit,
									seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob, seznamIdFazi,
									seznamIdIteraci, seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
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

		/* akce pro schování panelu filtrů  */
		ActionListener actZobrazeniFiltruMenu = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filtry.isSelected() == true) {
					pridejPanelFiltru();
					btSipkaFiltr.setText("<");
				} else {
					odstranPanelFiltru();
					btSipkaFiltr.setText(">");
				}
			}
		};
		
		/* akce pro schování panelu filtrů  */
		ActionListener actZobrazeniFiltruSipka = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filtry.isSelected() == false) {
					pridejPanelFiltru();
					filtry.setSelected(true);
					btSipkaFiltr.setText("<");
				} else {
					odstranPanelFiltru();
					btSipkaFiltr.setText(">");
					filtry.setSelected(false);
				}
			}
		};

		/* vložení akcí k příslušným komponentám */
		lsSeznamProjektu.addActionListener(actZmenaProjektu);
		btPridejFiltr.addActionListener(actVlozFiltr);
		btZapniFiltr.addActionListener(actZapniFiltr);
		exitAction.addActionListener(actZavri);
		czech.addActionListener(actJazykCzech);
		english.addActionListener(actJazykEnglish);
		filtry.addActionListener(actZobrazeniFiltruMenu);
		btSipkaFiltr.addActionListener(actZobrazeniFiltruSipka);

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
