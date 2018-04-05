package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import ostatni.Konstanty;
import ostatni.Ukladani;
import data.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Centrální panel zobrazující statistický panel, dropsloty, panel miniatur a filtry
 */

public class PanelProjektu extends JPanel {

	private static final long serialVersionUID = -8756957474284941689L;
	public Projekt projekt; // aktuálně vybraný projekt
	private JScrollPane scroll; // scroll panel
	private JPanel panel; //hlavní panel
	private JPanel grafy; // panel pro zobrazení miniatur grafů
	private JPanel panelStatistikSipka; // panel pro zobrazení statistik a šipky pro odebrání statistik
	protected JPanel panelFiltrySipka; // panel pro zobrazení filtrů a šipky pro schování filtrů
	private JPanel panelStatistik; // panel pro zobrazení statistik
	public boolean statistikyVisible = true; // udává zda jsou statistik zobrazené nebo ne
	private JButton btSipkaStatistiky; // tlačítko pro schování statistik

	private JPanel dropSloty; // panel dropslotů
	private JPanel dropSlot; // dropslot pro přetažení miniatur
	private JPanel dropSlot2; // dropslot pro přetažení miniatur
	private DropTarget dropTarget; // drop target pro miniatury
	private DropHandler dropHandler; // drop handler pro přetažení miniatur
	private DropTarget dropTarget2; // drop target pro miniatury
	private DropHandler dropHandler2; // drop handler pro přetažení miniatur

	private PanelGrafuSegment panelSegment; // panel pro zobrazení miniatur segmentů
	private PanelGrafuUkol panelUkol; // panel pro zobrazení miniatur úkolů
	private PanelGrafuKonfigurace panelKonfigurace; // panel pro zobrazení miniatur konfigurací
	private PanelGrafuArtefakt panelArtefakt; // panel pro zobrazení miniatur artefaktů
	private PanelGrafuCustom panelCustom; // panel pro zobrazení miniatur custom grafů

	/**
	 * Konstruktor třídy, nastaví projekt, spustí načtení dat projektu a nastaví
	 * zobrazení okna
	 * 
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelProjektu(Projekt projekt) {
		super();
		this.setBackground(Color.WHITE);

		this.projekt = projekt;
		this.projekt.nactiData();
		this.nastavZobrazeni();
		this.nastavAkce();
		Ukladani.setPanelProjektu(this);
		Ukladani.nactiGrafy(projekt.getID());
	}

	/**
	 * Nastaví velikost scroll panelu v závislosti na velikosti panelu
	 */
	public void setSizeScroll() {
		scroll.setPreferredSize(new Dimension(this.getWidth() - 5, this.getHeight() - 5));
		scroll.revalidate();
	}

	/**
	 * Nastaví nový projekt a spustí nové načtení dat
	 * 
	 * @param projekt
	 *            nový projekt
	 */
	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
		this.projekt.nactiData();

		this.nastavZobrazeni();
		this.nastavAkce();
		Ukladani.nactiGrafy(projekt.getID());
		this.revalidate();

	}

	/**
	 * Spustí nové načtení dat projektu odpovídající zadaným podmínkám
	 * 
	 * @param seznamIdUkolu
	 *            seznam id možných úkolů pro vložení
	 * @param seznamIdPriorit
	 *            seznam id priorit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdSeverit
	 *            seznam id severit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdResoluci
	 *            seznam id resolucí jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdStatusu
	 *            seznam id statusů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdTypu
	 *            seznam id typů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdOsob
	 *            seznam id osob jež obsahují úkoly které lze vložit do seznamu
	 * @param logPriorit
	 *            logický operátor pro priority
	 * @param logSeverit
	 *            logický operátor pro severity
	 * @param logResolution
	 *            logický operátor pro resoluce
	 * @param logStatusu
	 *            logický operátor pro statusy
	 * @param logTypu
	 *            logický operátor pro typy
	 * @param logOsob
	 *            logický operátor pro osoby
	 * @param seznamIdFazi
	 *            seznam id možných fází pro vložení
	 * @param seznamIdIteraci
	 *            seznam id možných iterací pro vložení
	 * @param seznamIdAktivit
	 *            seznam id možných aktivit pro vložení
	 * @param seznamIdKonfiguraci
	 *            seznam id možných konfigurací pro vložení
	 * @param seznamIdArtefaktu
	 *            seznam id možných artefaktů pro vložení
	 */
	public void setPodminkyProjektu(ArrayList<Integer> seznamIdUkolu, String logPriorit,
			ArrayList<Integer> seznamIdPriorit, String logSeverit, ArrayList<Integer> seznamIdSeverit,
			String logResolution, ArrayList<Integer> seznamIdResoluci, String logStatusu,
			ArrayList<Integer> seznamIdStatusu, String logTypu, ArrayList<Integer> seznamIdTypu, String logOsob,
			ArrayList<Integer> seznamIdOsob, ArrayList<Integer> seznamIdFazi, ArrayList<Integer> seznamIdIteraci,
			ArrayList<Integer> seznamIdAktivit, ArrayList<Integer> seznamIdKonfiguraci,
			ArrayList<Integer> seznamIdArtefaktu) {
		this.projekt.nactiData(seznamIdUkolu, logPriorit, seznamIdPriorit, logSeverit, seznamIdSeverit, logResolution,
				seznamIdResoluci, logStatusu, seznamIdStatusu, logTypu, seznamIdTypu, logOsob, seznamIdOsob,
				seznamIdFazi, seznamIdIteraci, seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);

		this.nastavZobrazeni();
		this.nastavAkce();
		Ukladani.nactiGrafy(projekt.getID());
		this.revalidate();
	}

	/**
	 * Nastaví zobrazení panelu, vloží panel statistik a panely s grafy
	 */
	private void nastavZobrazeni() {
		this.removeAll();
		panel = new JPanel(new BorderLayout());
		scroll = new JScrollPane(panel);
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		panel.setPreferredSize(Konstanty.VELIKOST_PANELU);
		this.setSizeScroll();

		btSipkaStatistiky = new JButton("<");
		btSipkaStatistiky.setPreferredSize(Konstanty.VELIKOST_SIPKY_STATISTIKY);
		btSipkaStatistiky.setFont(Konstanty.FONT_SIPKA);
		btSipkaStatistiky.setMargin(new Insets(0, 0, 0, 0));

		panelSegment = new PanelGrafuSegment(this.projekt);
		panelUkol = new PanelGrafuUkol(this.projekt);
		panelKonfigurace = new PanelGrafuKonfigurace(this.projekt);
		panelArtefakt = new PanelGrafuArtefakt(this.projekt);
		panelCustom = new PanelGrafuCustom(this.projekt);

		JScrollPane scrollUkoly = new JScrollPane(panelUkol);
		scrollUkoly.getHorizontalScrollBar().setUnitIncrement(25);
		scrollUkoly.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollUkoly.setPreferredSize(
				new Dimension(Konstanty.SIRKA_PANELU_GRAFU + 40, Konstanty.VYSKA_PANELU_GRAFU_STANDART + 40));
		scrollUkoly.revalidate();

		JScrollPane scrollCustom = new JScrollPane(panelCustom);
		scrollCustom.getHorizontalScrollBar().setUnitIncrement(25);
		scrollCustom.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollCustom.setPreferredSize(
				new Dimension(Konstanty.SIRKA_PANELU_GRAFU + 40, Konstanty.VYSKA_PANELU_GRAFU_STANDART + 40));
		scrollCustom.revalidate();

		JTabbedPane tabbedPanelGrafu = new JTabbedPane();
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("ukoly"), scrollUkoly);
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("segment"), panelSegment);
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("konfigurace"), panelKonfigurace);
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("artefakty"), panelArtefakt);
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("nazevCustom"), scrollCustom);
		Ukladani.setPanel(panelCustom);

		grafy = new JPanel(new BorderLayout());
		dropSloty = new JPanel(new BorderLayout());

		nastavDropSloty();
		
		grafy.add(tabbedPanelGrafu, BorderLayout.NORTH);
		grafy.add(dropSloty, BorderLayout.CENTER);

		panelStatistik = this.getPopisProjektu();

		panelStatistikSipka = new JPanel(new BorderLayout());
		panelStatistikSipka.add(panelStatistik, BorderLayout.WEST);
		panelStatistikSipka.add(btSipkaStatistiky, BorderLayout.EAST);

		panelFiltrySipka = new JPanel(new BorderLayout());
		panel.add(panelStatistikSipka, BorderLayout.WEST);
		panel.add(grafy, BorderLayout.CENTER);
		panel.add(panelFiltrySipka, BorderLayout.NORTH);

		this.add(scroll);

	}

	/**
	 * Nastaví komponenty a zobrazení dropslotů pro miniatury grafů
	 */
	public void nastavDropSloty() {
		dropSloty.removeAll();
		dropSlot = new JPanel(new BorderLayout());
		dropSlot.setBackground(Color.WHITE);
		dropSlot.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
		dropSlot2 = new JPanel(new BorderLayout());
		dropSlot2.setBackground(Color.WHITE);
		dropSlot2.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);

		dropHandler = new DropHandler(panelSegment, panelUkol, panelKonfigurace, panelArtefakt, panelCustom);
		dropTarget = new DropTarget(dropSlot, DnDConstants.ACTION_MOVE, dropHandler, true);
		dropHandler2 = new DropHandler(panelSegment, panelUkol, panelKonfigurace, panelArtefakt, panelCustom);
		dropTarget2 = new DropTarget(dropSlot2, DnDConstants.ACTION_MOVE, dropHandler2, true);

		JLabel label = new JLabel(Konstanty.POPISY.getProperty("grafTady"));
		JLabel label2 = new JLabel(Konstanty.POPISY.getProperty("grafTady"));
		label.setFont(new Font("Arial", Font.PLAIN, 40));
		label2.setFont(new Font("Arial", Font.PLAIN, 40));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);
		label2.setVerticalAlignment(JLabel.CENTER);

		dropSlot.add(label);
		dropSlot2.add(label2);

		dropSloty.add(dropSlot, BorderLayout.WEST);
		dropSloty.add(dropSlot2, BorderLayout.EAST);
		dropSloty.revalidate();
		dropSloty.repaint();
	}

	/**
	 * Vytvoří panel s popisem projektu
	 * 
	 * @return panel projektu
	 */
	private JPanel getPopisProjektu() {
		JTextArea lblNazev = new JTextArea(Konstanty.POPISY.getProperty("popisNazev") + ": " + projekt.getNazev());
		lblNazev.setLineWrap(true);
		lblNazev.setWrapStyleWord(true);
		lblNazev.setEditable(false);
		JLabel lblDatumOD = new JLabel(Konstanty.POPISY.getProperty("popisDatumOd") + ": "
				+ projekt.getDatumPocatku().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		JLabel lblDatumDO;
		if (projekt.getDatumKonce().equals(Konstanty.DATUM_PRAZDNY))
			lblDatumDO = new JLabel(Konstanty.POPISY.getProperty("popisDatumDoNeomezeny"));
		else
			lblDatumDO = new JLabel(Konstanty.POPISY.getProperty("popisDatumDo") + ": "
					+ projekt.getDatumKonce().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

		lblNazev.setFont(Konstanty.FONT_NADPIS_STATISTIK);
		lblDatumOD.setFont(Konstanty.FONT_NADPIS_STATISTIK);
		lblDatumDO.setFont(Konstanty.FONT_NADPIS_STATISTIK);

		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());

		GridBagConstraints g = new GridBagConstraints();

		g.insets = new Insets(3, 3, 5, 5);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		g.gridy = 0;
		panel.add(lblNazev, g);
		g.gridy++;
		panel.add(lblDatumOD, g);
		g.gridy++;
		panel.add(lblDatumDO, g);
		g.gridy++;
		panel.add(getPanelStatistiky(true), g);
		g.gridy = 5;
		g.gridx = 0;
		g.gridheight = 5;
		panel.add(getPanelStatistiky(false), g);

		panel.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titlePanelStatistik")));
		panel.setBackground(Color.WHITE);
		return panel;
	}

	/**
	 * Vytvoří panel statistik projektu s počty nebo s minimem, maximem a průměrem
	 * hodnot (dle zadaného parametru)
	 * 
	 * @param panelPocet určuje, zda se bude tvořit panel s počtem hodnot (true) nebo s
	 *            maximem, minimem a průměrem (false)
	 * @return panel statistik
	 */
	private JPanel getPanelStatistiky(boolean panelPocet) {
		JPanel panelStatistik = new JPanel();
		try {
			panelStatistik.setBackground(Color.WHITE);
			panelStatistik.setLayout(new GridBagLayout());
			GridBagConstraints grid = new GridBagConstraints();
			grid.insets = new Insets(3, 3, 3, 3);
			grid.fill = GridBagConstraints.HORIZONTAL;
			grid.gridx = 0;
			grid.gridy = 0;

			if (panelPocet) {
				panelStatistik.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titlePocet")));

				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevFaze") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetFazi()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevIterace") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetIteraci()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevAktivity") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetAktivit()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevUkoly") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetUkolu()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevKonfigurace") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetKonfiguraci()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevVetve") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetVetvi()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevTagy") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetTagu()), grid);
				grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevArtefakty") + ": "), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetArtefaktu()), grid);
				grid.gridx--;
				for (int i = 0; i < panelStatistik.getComponentCount(); i++)
					panelStatistik.getComponent(i).setFont(Konstanty.FONT_STATISTIK);

			} else {
				JLabel lblUkolyNa = new JLabel(Konstanty.POPISY.getProperty("popisUkolyNa") + ":");
				JLabel lblMinimum = new JLabel("Min");
				JLabel lblPrumer = new JLabel(Konstanty.POPISY.getProperty("nadpisPrumer"));
				JLabel lblMaximum = new JLabel("Max");
				JLabel lblUkol = new JLabel(Konstanty.POPISY.getProperty("popisUkol") + ": ");
				JLabel lblKonfiguraceNa = new JLabel(Konstanty.POPISY.getProperty("popisKonfiguraceNa") + ": ");
				JLabel lblArtefaktNa = new JLabel(Konstanty.POPISY.getProperty("popisArtefaktNa") + ": ");

				panelStatistik
						.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleMinMaxAvg")));
				panelStatistik.add(new JLabel(""), grid);
				grid.gridx++;
				panelStatistik.add(lblMinimum, grid);
				grid.gridx++;
				panelStatistik.add(lblPrumer, grid);
				grid.gridx++;
				panelStatistik.add(lblMaximum, grid);
				grid.gridx = 0;
				grid.gridy++;
				panelStatistik.add(lblUkolyNa, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisFazi") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinFaze()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerFaze()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxFaze()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisIteraci") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinIterace()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerIterace()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxIterace()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisAktivitu") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinAktivity()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerAktivity()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxAktivity()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaUkol()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaUkol()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaUkol()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				grid.insets = new Insets(0, 0, 0, 0);
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx = 0;
				grid.insets = new Insets(3, 3, 3, 3);
				grid.gridy++;
				panelStatistik.add(lblUkol, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCasovyOdhad") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinPredpokladanyCas()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerPredpokladanyCas()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxPredpokladanyCas()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisStravenyCas") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinStravenyCas()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerStravenyCas()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxStravenyCas()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				grid.insets = new Insets(0, 0, 0, 0);
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx = 0;
				grid.insets = new Insets(3, 3, 3, 3);
				grid.gridy++;
				panelStatistik.add(lblKonfiguraceNa, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaKonf()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaKonf()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaKonf()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				grid.insets = new Insets(0, 0, 0, 0);
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx++;
				panelStatistik.add(new JSeparator(), grid);
				grid.gridx = 0;
				grid.insets = new Insets(3, 3, 3, 3);
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(lblArtefaktNa, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisKonfiguraci") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinKonfiguraceArtef()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerKonfiguraceArtef()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxKonfiguraceArtef()), grid);
				grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid);
				grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaArtef()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaArtef()), grid);
				grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaArtef()), grid);

				for (int i = 0; i < panelStatistik.getComponentCount(); i++)
					panelStatistik.getComponent(i).setFont(Konstanty.FONT_STATISTIK);

				lblUkolyNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMinimum.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblPrumer.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMaximum.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblUkol.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblKonfiguraceNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblArtefaktNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaNacteniStatistik"));
			e.printStackTrace();
		}
		return panelStatistik;
	}
	
	/**
	 * Metoda pro zobrazení panelu filtrů
	 * @param panel panel filtrů
	 */
	public void zobrazFiltry(JScrollPane panel) {
		this.panelFiltrySipka.add(panel, BorderLayout.NORTH);
		this.revalidate();
	}

	/**
	 * Metoda pro schování panelu filtrů
	 * @param panel panel filtrů
	 */
	public void schovejFiltry(JScrollPane panel) {
		this.panelFiltrySipka.remove(panel);
		this.revalidate();
	}

	/**
	 * Nastaví akce komponentám panelu
	 */
	public void nastavAkce() {

		/* akce pro schovani panelu statistik */
		ActionListener actZobrazeniStatistikButton = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (statistikyVisible == false) {
					panelStatistikSipka.add(panelStatistik);
					statistikyVisible = true;
					dropSlot.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
					dropSlot2.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
					btSipkaStatistiky.setText("<");
					panelStatistikSipka.revalidate();
				} else {
					panelStatistikSipka.remove(panelStatistik);
					statistikyVisible = false;
					dropSlot.setPreferredSize(Konstanty.VELIKOST_GRAFU_NEJVETSI);
					dropSlot2.setPreferredSize(Konstanty.VELIKOST_GRAFU_NEJVETSI);
					btSipkaStatistiky.setText(">");
					panelStatistikSipka.revalidate();
				}
			}
		};

		btSipkaStatistiky.addActionListener(actZobrazeniStatistikButton);
	}
}
