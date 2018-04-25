package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import ostatni.Konstanty;
import ostatni.Ukladani;
import data.*;
import data.ciselnik.*;
import data.custom.SablonaCustomGrafu;
import databaze.IProjektDAO;
import databaze.ProjektDAO;

/**
 * Hlavní okno na které se dodávají jednotlivé panely, dědí ze třídy JFrame
 */
public class OknoHlavni extends JFrame {

	private static final long serialVersionUID = 7642444463088891084L;
	private JPanel panelMenu; // levý panel pro výběr projektu a přidání filtrů
	private JPanel listaTlacitekSmazaniFiltru; // panel pro tlačítka pro smazání aktivních filtrů
	private JPanel panelProjektMenu; // panel pro výběr projektu
	private PanelProjektu panelGrafu; // centrální panel zobrazující statistiky a grafy

	private DefaultComboBoxModel<Projekt> modelProjekt = new DefaultComboBoxModel<Projekt>(); // model pro seznam projektů
	private ComboBoxDynamicky lsSeznamProjektu = new ComboBoxDynamicky(modelProjekt); // seznam projektů
	private JComboBox<String> cbTypFiltru = new JComboBox<String>(Konstanty.POLE_FILTRU); // seznam možných filtrů
	private JComboBox<String> cbTypPodfiltru = new JComboBox<String>(Konstanty.POLE_PODFILTRU); // seznam možných podfiltrů úkolu
	private JPanel pnBoxFiltru = new JPanel(); // box s vybranými filtry
	private JButton btZapniFiltr;// tlačítko pro načtení filtrů
	private JButton btSipkaFiltry; // tlačítko pro schování panelu filtrů
	private JScrollPane scScrollFiltru; // scrollpanel pro filtry

	private JMenuBar menuBar; // horní ovládací bar
	private JMenu fileMenu; // Menu horního baru "Soubor"
	private JMenu customGrafMenu; // Menu horního baru "Vlastní graf"
	private JMenuItem vytvorGraf; // Tlačítko horního menu pro vytvoření custom grafu
	private JMenu settingsMenu; // Menu horního baru "Nastavení"
	private JMenu languageMenu; // Menu horního baru pro nastavení jazyka programu
	private JMenu removeChartMenu; // Menu horního baru pro odstranění custom grafu podle jména
	private JMenu vytvorSablonaMenu; // Menu horního baru pro vytvoreni grafu ze sablony
	private JMenu removeSablonaMenu; // Menu horního baru pro odstranění sablony grafu
	private JMenu removeMainMenu; // Menu horního baru pro odstranění custom grafu
	private JMenuItem removeChartProject; // Tlačítko horního menu pro odstranění custom grafu
	private JMenu importExportMenu; // Menu horního baru pro import/export grafů
	private JMenuItem exportAll; // Tlačítko horního menu pro exportování všech grafů
	private JMenuItem exportProjekt; // Tlačítko horního menu pro exportování grafů projektu
	private JMenuItem importGrafy; // Tlačítko horního menu pro import grafů
	private JMenuItem exitAction; // // Tlačítko horního menu pro ukončení programu
	private JMenuItem czech; // Tlačítko horního menu pro přepnutí programu do češtiny
	private JMenuItem english; // Tlačítko horního menu pro přepnutí programu do angličtiny
	private JCheckBoxMenuItem filtry; // Checkbox pro nastavení zda se má zobrazovat
	private JCheckBoxMenuItem statistikyMenu;
	private int polohaPaneluUkol; // poloha panelu filtru úkol v panelu filtrů
	private SablonaCustomGrafu ulozeni = null;
	private OknoHlavni okno = this;

	/**
	 * Konstruktor třídy, naplní ve třídě konstant připojení, načte projekty a
	 * nastaví zobrazení a akce
	 * 
	 * @param pripojeni
	 *            připojení k databázi
	 */
	public OknoHlavni(Connection pripojeni) {
		Konstanty.PRIPOJENI = pripojeni;
		Konstanty.projektVyber = lsSeznamProjektu;
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
		nastavOkno();
	
		btZapniFiltr = new JButton(Konstanty.POPISY.getProperty("tlacitkoZapniFiltr"));
		fileMenu = new JMenu(Konstanty.POPISY.getProperty("menuSoubor"));
		customGrafMenu = new JMenu(Konstanty.POPISY.getProperty("menuCustomGraf"));
		settingsMenu = new JMenu(Konstanty.POPISY.getProperty("menuNastaveni"));
		languageMenu = new JMenu(Konstanty.POPISY.getProperty("menuJazyk"));
		removeChartMenu = new JMenu(Konstanty.POPISY.getProperty("odstranGrafJmeno"));
		vytvorSablonaMenu = new JMenu(Konstanty.POPISY.getProperty("vytvorSablona"));
		removeSablonaMenu = new JMenu(Konstanty.POPISY.getProperty("odstranSablona"));
		Ukladani.setMenuMazaniGrafu(removeChartMenu);
		Ukladani.setMenuVytvoreniSablona(vytvorSablonaMenu);
		Ukladani.setMenuSmazaniSablona(removeSablonaMenu);
		removeMainMenu = new JMenu(Konstanty.POPISY.getProperty("odstranGraf"));
		removeChartProject = new JMenuItem(Konstanty.POPISY.getProperty("odstranGrafProjekt"));
		exitAction = new JMenuItem(Konstanty.POPISY.getProperty("menuExit"));
		importExportMenu = new JMenu(Konstanty.POPISY.getProperty("importExport"));
		exportAll = new JMenuItem(Konstanty.POPISY.getProperty("exportVse"));
		exportProjekt = new JMenuItem(Konstanty.POPISY.getProperty("exportProjekt"));
		importGrafy = new JMenuItem(Konstanty.POPISY.getProperty("importGrafu"));
		vytvorGraf = new JMenuItem(Konstanty.POPISY.getProperty("menuVytvorGraf"));
		czech = new JMenuItem(Konstanty.POPISY.getProperty("menuCestina"));
		english = new JMenuItem(Konstanty.POPISY.getProperty("menuAnglictina"));
		filtry = new JCheckBoxMenuItem(Konstanty.POPISY.getProperty("filtryTrue"), false);
		statistikyMenu = new JCheckBoxMenuItem(Konstanty.POPISY.getProperty("zobrazStatistiky"), true);

		JLabel nadpis = new JLabel(Konstanty.POPISY.getProperty("nadpisFiltru"));
		panelMenu = new JPanel();
		panelProjektMenu = new JPanel(new BorderLayout());
		panelMenu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		JPanel pnFiltr = new JPanel();
		panelGrafu = new PanelProjektu(this.getProjekt());
		menuBar = new JMenuBar();
		scScrollFiltru = new JScrollPane(pnBoxFiltru, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnBoxFiltru.setLayout(new BoxLayout(pnBoxFiltru, BoxLayout.PAGE_AXIS));

		pnBoxFiltru.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleFiltrVyberu")));
		panelProjektMenu.add(panelMenu, BorderLayout.WEST);
		listaTlacitekSmazaniFiltru = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		panelProjektMenu.add(listaTlacitekSmazaniFiltru, BorderLayout.CENTER);
		panelProjektMenu.setPreferredSize(Konstanty.VELIKOST_PROJEKT_MENU);
		nadpis.setFont(Konstanty.FONT_NADPIS);

		btSipkaFiltry = new JButton("v");
		btSipkaFiltry.setPreferredSize(Konstanty.VELIKOST_SIPKY_FILTRY);
		btSipkaFiltry.setFont(new Font("Arial", Font.PLAIN, 15));
		btSipkaFiltry.setMargin(new Insets(0, 0, 0, 0));
		panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);

		pnFiltr.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		pnFiltr.add(cbTypFiltru);
		pnFiltr.add(btZapniFiltr);

		lsSeznamProjektu.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);

		scScrollFiltru.setPreferredSize(Konstanty.VELIKOST_FILTRU);
		scScrollFiltru.getVerticalScrollBar().setUnitIncrement(15);

		setJMenuBar(menuBar);
		panelMenu.add(nadpis);
		panelMenu.add(lsSeznamProjektu);
		panelProjektMenu.add(pnFiltr, BorderLayout.EAST);
		menuBar.add(fileMenu);
		menuBar.add(customGrafMenu);
		menuBar.add(settingsMenu);
		settingsMenu.add(languageMenu);
		settingsMenu.add(filtry);
		settingsMenu.add(statistikyMenu);
		fileMenu.add(exitAction);
		importExportMenu.add(importGrafy);
		importExportMenu.add(new JSeparator());
		importExportMenu.add(exportAll);
		importExportMenu.add(exportProjekt);
		customGrafMenu.add(vytvorGraf);
		customGrafMenu.add(vytvorSablonaMenu);
		customGrafMenu.add(new JSeparator());
		customGrafMenu.add(removeSablonaMenu);
		customGrafMenu.add(removeMainMenu);
		removeMainMenu.add(removeChartMenu);
		removeMainMenu.add(removeChartProject);
		customGrafMenu.add(new JSeparator());
		customGrafMenu.add(importExportMenu);
		languageMenu.add(czech);
		languageMenu.add(english);

		this.setLayout(new BorderLayout());
		this.add(panelProjektMenu, BorderLayout.NORTH);
		this.add(panelGrafu, BorderLayout.CENTER);

		schovaniSipky();
		nastavAkce();
	}

	/**
	 * Nastaví atributy okna a akci po ukončení
	 */
	private void nastavOkno() {
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setMinimumSize(Konstanty.MINIMALNI_VELIKOST_OKNA);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SPADe");
	}

	/**
	 * Nastaví akce k jednotlivým komponentám
	 */
	private void nastavAkce() {
		/* akce při změně projektu v panelu menu */
		ActionListener actZmenaProjektu = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Konstanty.CITAC_PROGRESU=0;
				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {

						@Override
						public void run() {
							cbTypFiltru.setEnabled(false);
							btZapniFiltr.setEnabled(false);
							filtry.setSelected(false);
							panelGrafu.statistikyVisible = true;
							lsSeznamProjektu.setEnabled(false);
							schovaniSipky();
							zakazTlacitka();
							boolean ulozeniCustomGrafu = false;

							if (OknoCustomGraf.instance != null) { // Uloží data pro vytvoření custom grafu
								if (OknoCustomGraf.instance.isVisible()) {
									ulozeni = OknoCustomGraf.instance.ulozNastaveni();
									ulozeni.setIterace(-1);
									ulozeni.setOsoby(-1);
									ulozeniCustomGrafu = true;
									OknoCustomGraf.instance.dispose();
								}
							}

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
							lsSeznamProjektu.setEnabled(true);
							povolTlacitka();
							oknoProgres.setVisible(false);

							if (ulozeniCustomGrafu) { // Otevře okno pro vytvoření custom grafu
								SwingUtilities.invokeLater(() -> {
									OknoCustomGraf example = new OknoCustomGraf(ulozeni, getProjekt());
									example.setLocationRelativeTo(null);
									example.setVisible(true);
								});
							}
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
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Priority(getProjekt().getID())).getSeznam());
											operandy[0] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Severity":
										if ((new Severity(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozSeverity"));
										} else {
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Severity(getProjekt().getID())).getSeznam());
											operandy[1] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Status":
									case "Statusy":
										if ((new Status(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozStatusy"));
										} else {
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Status(getProjekt().getID())).getSeznam());
											operandy[2] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Types":
									case "Typy":
										if ((new Typ(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozTypy"));
										} else {
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Typ(getProjekt().getID())).getSeznam());
											operandy[3] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										}
										break;
									case "Resolution":
									case "Rezoluce":
										if ((new Resoluce(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozResoluce"));
										} else {
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Resoluce(getProjekt().getID())).getSeznam());
											operandy[4] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
										}
										break;
									case "People":
									case "Osoby":
										if ((new Osoby(getProjekt().getID())).getSeznam().isEmpty()) {
											JOptionPane.showMessageDialog(pnBoxFiltru,
													Konstanty.POPISY.getProperty("chybaVlozOsoby"));
										} else {
											((PanelFiltr) pnPanelFiltr).lsSeznamFiltr.clearSelection();
											((PanelFiltrCiselnik) pnPanelFiltr)
													.nactiNovyProjekt((new Osoby(getProjekt().getID())).getSeznam());
											operandy[5] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
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
							btSipkaFiltry.setText("v");
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
						/* pokud je panel již vložen, nelze ho znovu dodat */
						if (!jeZadanyFiltr(Konstanty.POPISY.getProperty("nazevUkoly"), pnBoxFiltru.getComponents())) {
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
					btSipkaFiltry.setText("ʌ");
					schovaniSipky();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaVlozFiltr"));
					e1.printStackTrace();
				}
			}
		};

		/* akce pro combobox přidání podlfiltru pro úkoly */
		ActionListener actVlozPodFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					switch (cbTypPodfiltru.getSelectedIndex()) {
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

		// Akce pro tlačítka pro smazání filtru
		ActionListener actSmazaniFiltru = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TlacitkoMazaniFiltru tlacitko = (TlacitkoMazaniFiltru) e.getSource();
				pnBoxFiltru.remove(tlacitko.getComp());
				listaTlacitekSmazaniFiltru.remove(tlacitko);

				/* Smaže tlačítko smazat všechny filtry pokud zbývá pouze jedno tlačítko */
				if (listaTlacitekSmazaniFiltru.getComponentCount() == 2) {
					listaTlacitekSmazaniFiltru.remove(0);
				}
				
				listaTlacitekSmazaniFiltru.revalidate();
				listaTlacitekSmazaniFiltru.repaint();

				try {
					/* vlákno zobrazuje okno s progresem načítání */
					Thread t1 = new Thread(new Runnable() {
						public void run() {
							cbTypFiltru.setEnabled(false);
							btZapniFiltr.setEnabled(false);
							lsSeznamProjektu.setEnabled(false);
							zakazTlacitka();

							OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
							while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
								oknoProgres.nastavProgres();
								if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
									break;
								}
								Thread.yield();
							}
							povolTlacitka();
							lsSeznamProjektu.setEnabled(true);
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
										seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
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
									case "People":
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
							schovaniSipky();
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

		/* akce pro zapnutí vybraných filtrů */
		ActionListener actZapniFiltr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (pnBoxFiltru.getComponentCount() == 0) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("zadneFiltry"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}

				else {
					Konstanty.CITAC_PROGRESU = 0;
					try {
						/* vlákno zobrazuje okno s progresem načítání */
						Thread t1 = new Thread(new Runnable() {
							public void run() {
								cbTypFiltru.setEnabled(false);
								btZapniFiltr.setEnabled(false);
								lsSeznamProjektu.setEnabled(false);
								zakazTlacitka();

								OknoProgresNacitani oknoProgres = new OknoProgresNacitani(panelGrafu);
								while (Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU) {
									oknoProgres.nastavProgres();
									if (Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU) {
										break;
									}
									Thread.yield();
								}
								povolTlacitka();
								lsSeznamProjektu.setEnabled(true);
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
									if (pnPanelFiltr.jePouzit()) { // je-li zaškrtnuto použít, zjistí se o jaký filtr
																	// jde
										switch (pnPanelFiltr.getName()) { // pomocí názvu se zjišťuje o jaký jde filtr
										case "Tasks":
										case "Úkoly":
											seznamIdUkolu = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevUkoly") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Priorities":
										case "Priority":
											seznamIdPriorit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevPriority") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[0] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "Severity":
											seznamIdSeverit = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevSeverity") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[1] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "Status":
										case "Statusy":
											seznamIdStatusu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevStatusy") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[2] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "Types":
										case "Typy":
											seznamIdTypu = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(Konstanty.POPISY.getProperty("nazevTypy") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[3] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "Resolution":
										case "Rezoluce":
											seznamIdResoluci = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevResoluce") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[4] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "People":
										case "Osoby":
											seznamIdOsob = ((PanelFiltrCiselnik) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevOsoby") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											operandy[5] = ((PanelFiltrCiselnik) pnPanelFiltr).getLogOperand();
											break;
										case "Phase":
										case "Fáze":
											seznamIdFazi = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(Konstanty.POPISY.getProperty("nazevFaze") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Iterations":
										case "Iterace":
											seznamIdIteraci = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevIterace") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Activities":
										case "Aktivity":
											seznamIdAktivit = ((PanelFiltrPolozkaPocatek) pnPanelFiltr).getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevAktivity") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Configurations":
										case "Konfigurace":
											seznamIdKonfiguraci = ((PanelFiltrPolozkaVytvoreni) pnPanelFiltr)
													.getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevKonfigurace") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Artifacts":
										case "Artefakty":
											seznamIdArtefaktu = ((PanelFiltrPolozkaVytvoreniArtefakt) pnPanelFiltr)
													.getSeznamId();
											vytvorTlacitkoMazaniFiltru(
													Konstanty.POPISY.getProperty("nazevArtefakty") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										case "Time":
										case "Čas":
											seznamIdUkolu.retainAll(((PanelFiltrCas) pnPanelFiltr).getSeznamId());

											if (seznamIdUkolu.isEmpty()) {
												seznamIdUkolu.add(-1);
											}

											vytvorTlacitkoMazaniFiltru(Konstanty.POPISY.getProperty("cas") + " X",
													pnBoxFiltru.getComponents()[i]).addActionListener(actSmazaniFiltru);
											break;
										default:
											break;
										}

									} else {
										smazTlacitkoListyFiltru(pnBoxFiltru.getComponents()[i].getName() + " X");
										pnBoxFiltru.remove(i--); // není-li zaškrtnuto Použít filtr, filtr se z boxu
																	// smaže
																	// kontrola musí přejí o jedno míst zpět (před
																	// smazaný
																	// filtr)

									}
								}
								filtry.setSelected(false);
								panelGrafu.statistikyVisible = true;
								btSipkaFiltry.setText("v");
								schovaniSipky();
								pnBoxFiltru.revalidate();
								/* spustí se nastavení podmínek a tím i nové načtení panelu grafů */
								panelGrafu.setPodminkyProjektu(seznamIdUkolu, operandy[0], seznamIdPriorit, operandy[1],
										seznamIdSeverit, operandy[2], seznamIdResoluci, operandy[3], seznamIdStatusu,
										operandy[4], seznamIdTypu, operandy[5], seznamIdOsob, seznamIdFazi,
										seznamIdIteraci, seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
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

		/* akce pro schování panelu filtrů z horního menu */
		ActionListener actZobrazSchovejStatistiky = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statistikyMenu.setSelected(!panelGrafu.getStatistikyVisible());
				panelGrafu.zobrazSchovejStatistiky();
			}
		};
		
		/* akce pro schování panelu filtrů z horního menu */
		ActionListener actZobrazeniFiltruMenu = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (pnBoxFiltru.getComponentCount() == 0) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("zadneFiltry"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
					filtry.setSelected(false);
				} else {
					if (filtry.isSelected() == true) {
						panelGrafu.zobrazFiltry(scScrollFiltru);
						btSipkaFiltry.setText("ʌ");
					} else {
						panelGrafu.schovejFiltry(scScrollFiltru);
						btSipkaFiltry.setText("v");
					}
				}
			}
		};

		/* akce pro schování panelu filtrů pomocí tlačítka šipky */
		ActionListener actZobrazeniFiltruButton = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pnBoxFiltru.getComponentCount() == 0) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("zadneFiltry"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				} else {
					if (filtry.isSelected() == false) {
						panelGrafu.zobrazFiltry(scScrollFiltru);
						btSipkaFiltry.setText("ʌ");
						filtry.setSelected(true);
					} else {
						panelGrafu.schovejFiltry(scScrollFiltru);
						btSipkaFiltry.setText("v");
						filtry.setSelected(false);
					}
				}
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

		/* akce pro export všech custom grafů */
		ActionListener actExportAll = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Ukladani.save(file);
				}

			}
		};

		/* akce pro export custom grafů projektu */
		ActionListener actExportProjekt = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Ukladani.save(file, getProjekt().getID());
				}

			}
		};

		/* akce pro import custom grafů */
		ActionListener actImportGrafu = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Ukladani.importGrafu(file, getProjekt().getID());
				}
			}
		};

		/* akce pro smazání grafů projektu */
		ActionListener actRemoveChartProject = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ukladani.odstranGrafyProjektu(getProjekt().getID());
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
		exportAll.addActionListener(actExportAll);
		exportProjekt.addActionListener(actExportProjekt);
		importGrafy.addActionListener(actImportGrafu);
		removeChartProject.addActionListener(actRemoveChartProject);
		statistikyMenu.addActionListener(actZobrazSchovejStatistiky);

		this.addComponentListener(actResizePaneluGrafu);
		this.addWindowListener(actUkonceniOkna);

	}

	/**
	 * Vytvoří tlačítko pro smazání filtru
	 * 
	 * @param nazev
	 *            název tlačítka
	 * @param comp
	 *            odkaz na filtr, který má tlačítko odstranit
	 * @return tlačítko pro smazání filtru
	 */
	private TlacitkoMazaniFiltru vytvorTlacitkoMazaniFiltru(String nazev, Component comp) {

		for (int i = 0; i < listaTlacitekSmazaniFiltru.getComponentCount(); i++) { // vymaže duplicitní tlačítka
			if (listaTlacitekSmazaniFiltru.getComponent(i).getName().equals(nazev)) {
				listaTlacitekSmazaniFiltru.remove(i);
			}
		}

		TlacitkoMazaniFiltru tlacitkoMazaniFilru = new TlacitkoMazaniFiltru(comp);
		tlacitkoMazaniFilru.setName(nazev);
		tlacitkoMazaniFilru.setText(nazev);
		listaTlacitekSmazaniFiltru.add(tlacitkoMazaniFilru);
		tlacitkoMazaniFilru.setContentAreaFilled(false);
		tlacitkoMazaniFilru.setOpaque(true);

		nazev = nazev.substring(0, nazev.length() - 2);
		switch (nazev) { // nastaví barvu tlačítka filtru podle typu filtru
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
		case "People":
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

		if (listaTlacitekSmazaniFiltru.getComponentCount() > 1) { // pokud jsou zapnuty více než dva filtry, přidá
																	// tlačítko pro odstranění všech filtrů
			if (!(listaTlacitekSmazaniFiltru.getComponent(0).getName()
					.equals(Konstanty.POPISY.getProperty("smazVse")))) {
				JButton tlacitkoMazaniVse = new JButton();
				tlacitkoMazaniVse.setFont(Konstanty.FONT_TLACITKA);
				tlacitkoMazaniVse.setName(Konstanty.POPISY.getProperty("smazVse"));
				tlacitkoMazaniVse.setText(Konstanty.POPISY.getProperty("smazVse"));
				tlacitkoMazaniVse.setBackground(Color.WHITE);
				listaTlacitekSmazaniFiltru.add(tlacitkoMazaniVse);
				listaTlacitekSmazaniFiltru.setComponentZOrder(tlacitkoMazaniVse, 0);
				tlacitkoMazaniVse.setContentAreaFilled(false);
				tlacitkoMazaniVse.setOpaque(true);
				tlacitkoMazaniVse.addActionListener(actSmazVsechnyFiltry);
			}
		}

		return tlacitkoMazaniFilru;
	}

	/**
	 * Metoda odebe tlačítko pro smazání filtru z panelu pro tlačítka
	 * 
	 * @param nazev
	 *            název filtru
	 */
	private void smazTlacitkoListyFiltru(String nazev) {

		for (int i = 0; i < listaTlacitekSmazaniFiltru.getComponentCount(); i++) {
			if (listaTlacitekSmazaniFiltru.getComponent(i).getName().equals(nazev)) {
				listaTlacitekSmazaniFiltru.remove(i);
			}
		}

		if (listaTlacitekSmazaniFiltru.getComponentCount() > 2) {
			listaTlacitekSmazaniFiltru.remove(0);
		}

		listaTlacitekSmazaniFiltru.repaint();
	}

	/**
	 * Akce pro odstranění všech filtrů
	 */
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

			Thread t2 = new Thread(new Runnable() {
				public void run() {
					listaTlacitekSmazaniFiltru.removeAll();
					pnBoxFiltru.removeAll();
					panelGrafu.setProjekt(getProjekt());
					panelGrafu.panelFiltrySipka.add(btSipkaFiltry, BorderLayout.SOUTH);
					schovaniSipky();
					listaTlacitekSmazaniFiltru.revalidate();
					listaTlacitekSmazaniFiltru.repaint();
				}
			});
			t1.start();
			t2.start();
		}
	};

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

	/**
	 * Metoda pro zablokování ovládacích tlačítek během načítaní
	 */
	private void zakazTlacitka() {
		for (int i = 0; i < listaTlacitekSmazaniFiltru.getComponentCount(); i++) {
			listaTlacitekSmazaniFiltru.getComponent(i).setEnabled(false);
		}
	}

	/**
	 * Metoda pro povolení ovládacích tlačítek během načítaní
	 */
	private void povolTlacitka() {
		for (int i = 0; i < listaTlacitekSmazaniFiltru.getComponentCount(); i++) {
			listaTlacitekSmazaniFiltru.getComponent(i).setEnabled(true);
		}
	}
	
	/**
	 * Metoda pro schování a oběvení sipky filtru, podle poctu zvolených filtrů 
	 */
	private void schovaniSipky() {
		if (pnBoxFiltru.getComponentCount()>0) {
			btSipkaFiltry.setVisible(true);			
		}
		else{
			btSipkaFiltry.setVisible(false);
			listaTlacitekSmazaniFiltru.removeAll();
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

}
