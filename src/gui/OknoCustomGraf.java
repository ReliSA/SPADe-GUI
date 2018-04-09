package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import data.CustomGraf;
import data.Projekt;
import data.Segment;
import data.ciselnik.Osoby;
import data.polozky.PolozkaCiselnik;
import databaze.CustomGrafDAO;
import ostatni.Konstanty;
import ostatni.Ukladani;

import javax.swing.JComboBox;

/**
 * Okno pro nastavení vlastností custom grafu
 */
public class OknoCustomGraf extends JFrame {

	private static final long serialVersionUID = -2359690226410306884L;
	private int parametryZvolenehoSql; // udává parametry potřebné pro zvolené SQL
	private Projekt projekt; // zvolený projekt
	private JButton nakresli; // tlačítko pro vykreslení grafu
	private JPanel dataPanel; // panel s načtenými daty
	private JPanel controlPanel; // panel s ovládácími componentami
	private JTextField tfNazev; // textové pole pro zadání názvu
	private JLabel lblNazevGrafu; // popisek název grafu
	private JLabel lblIterace; // popisek iterace
	private JLabel lblOsoba; // popisek osoby
	private JLabel lblSql; // popisek SQL
	private JComboBox<String> cbSql; // combobox pro volbu SQL
	private JComboBox<?> cbIterace; // combobox pro volbu iterace
	private JComboBox<?> cbOsoby; // combobox pro volbu osoby
	private HashMap<String, Color> hmap = new HashMap<String, Color>(); // hashmapa pro uchování zvolených barev pro graf
	private ArrayList<PolozkaCiselnik> osoby; // arraylist osob zvoleného projektu
	private ArrayList<Segment> iterace; // arraylist iterací zvoleného projektu
	private CustomGraf data; // objekt s načtenými daty pro vykreslení grafu

	/**
	 * Kontruktor okna
	 * 
	 * @param projekt zvolený projekt
	 */
	public OknoCustomGraf(Projekt projekt) {
		super(Konstanty.POPISY.getProperty("menuVytvorGraf"));

		this.projekt = projekt;
		osoby = (new Osoby(projekt.getID())).getSeznam();
		iterace = projekt.getIterace();

		nastavZobrazení();
		nastavAkce();
	}

	/**
	 * Nastaví componenty a zobrazení okna
	 */
	private void nastavZobrazení() {
		this.setLayout(new BorderLayout());
		JPanel panelHorni = new JPanel(new BorderLayout());
		
		
		dataPanel = new JPanel(new GridLayout(1, 0));
		JScrollPane scrollDataPanel = new JScrollPane(dataPanel);
		controlPanel = new JPanel(new GridLayout(1, 0));
		tfNazev = new JTextField();
		lblIterace = new JLabel(Konstanty.POPISY.getProperty("nazevIterace"));
		lblOsoba = new JLabel(Konstanty.POPISY.getProperty("osoba"));
		lblNazevGrafu = new JLabel(Konstanty.POPISY.getProperty("nazevGrafu"));
		cbSql = new JComboBox<String>(Konstanty.getSQLKeys());
		cbIterace = new JComboBox(getModel(iterace));
		cbOsoby = new JComboBox(getModel(osoby));
		lblSql = new JLabel("SQL:");
		nakresli = new JButton(Konstanty.POPISY.getProperty("vytvorGraf"));

		cbSql.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		tfNazev.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);

		lblIterace.setHorizontalAlignment(JLabel.CENTER);
		lblNazevGrafu.setHorizontalAlignment(JLabel.CENTER);
		lblOsoba.setHorizontalAlignment(JLabel.CENTER);
		lblSql.setHorizontalAlignment(JLabel.CENTER);

		panelHorni.add(scrollDataPanel, BorderLayout.CENTER);
		panelHorni.add(controlPanel, BorderLayout.NORTH);

		this.add(panelHorni,BorderLayout.NORTH);	
		
		nastavMenu();

//		this.add(comp, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}

	/**
	 * Vrací model ze seznamu z parametru
	 * 
	 * @param seznam
	 *            seznam, ze kterého se vytvoří model
	 * @return vytvořený model
	 */
	private DefaultComboBoxModel getModel(ArrayList seznam) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0; i < seznam.size(); i++)
			model.addElement(seznam.get(i));
		return model;
	}

	/**
	 * Metoda vrací ID iterace zvolené v comboboxu
	 * 
	 * @return ID iterace
	 */
	private int getIdIterace() {
		String nazev = iterace.get(cbIterace.getSelectedIndex()).getNazev();
		for (int i = 0; i < iterace.size(); i++) {
			Segment iter = iterace.get(i);
			if (iter.getNazev().equals(nazev)) {
				return iter.getID();
			}
		}
		return -1;
	}

	/**
	 * Metoda vrací ID osoby zvolené v comboboxu
	 * 
	 * @return ID osoby
	 */
	private int getIdOsoby() {
		String nazev = osoby.get(cbOsoby.getSelectedIndex()).getNazev();
		for (int i = 0; i < osoby.size(); i++) {
			PolozkaCiselnik osob = osoby.get(i);
			if (osob.getNazev().equals(nazev)) {
				return osob.getID();
			}
		}
		return -1;
	}

	/**
	 * Metoda pro kontrolu zda jsou zvoleny nějaká data pro vykreslení grafu
	 * 
	 * @return true pokud jsou zvoleny nějaká data, jinak false
	 */
	private boolean dataCheck() {

		boolean bool = true;
		for (int i = 0; i < dataPanel.getComponentCount(); i++) {
			if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getPouzit()) {
				bool = false;
			}
		}
		return bool;
	}

	/**
	 * Metoda nastaví componenty ovládacího panelu okna
	 */
	private void nastavMenu() {

		String sql = (String) cbSql.getSelectedItem();
		parametryZvolenehoSql = Integer.parseInt(Konstanty.SQLsVar.getProperty(sql));

		controlPanel.removeAll();

		controlPanel.add(lblSql);
		controlPanel.add(cbSql);

		if (parametryZvolenehoSql == 3) {
			controlPanel.add(lblIterace);
			controlPanel.add(cbIterace);
			controlPanel.add(lblOsoba);
			controlPanel.add(cbOsoby);
		} else if (parametryZvolenehoSql == 2) {
			controlPanel.add(lblOsoba);
			controlPanel.add(cbOsoby);
		} else if (parametryZvolenehoSql == 1) {
			controlPanel.add(lblIterace);
			controlPanel.add(cbIterace);
		}

		controlPanel.add(lblNazevGrafu);
		controlPanel.add(tfNazev);
		controlPanel.add(nakresli);

		nactiData();
	}

	/**
	 * Metoda dle údajů zvolených v ovládacím menu načte data pro vykreslení grafu
	 */
	private void nactiData() {
		CustomGrafDAO dao = new CustomGrafDAO();

		data = dao.getCustomGrafData((String) cbSql.getSelectedItem(), projekt.getID(), getIdIterace(), getIdOsoby(),
				parametryZvolenehoSql);

		dataPanel.removeAll();
		
		dataPanel.add(new panelDatCustomGrafu(data.getNazevSloupce(0),data.getDatumy()));
		
		for (int i = 1; i < data.pocetSloupcu(); i++) {
			dataPanel.add(new panelDatCustomGrafu(data.getNazevSloupce(i),data.getDataSloupec(i-1), i - 1));
		}

		getContentPane().revalidate();
		getContentPane().repaint();
	}

	
	
	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce() {

		// Akce pro načtení dat
		ActionListener actNactiData = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				nactiData();

			}
		};

		// Akce pro nastavení ovládacího menu
		ActionListener actNastavMenu = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				nastavMenu();
			}
		};

		// Akce pro vykreslení grafu
		ActionListener actNakresliGraf = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (tfNazev.getText().equals("")) { // Kontrola zda název není prázdný
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("prazdnyNazev"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}

				else if (dataCheck()) { // Kontrola zda název není prázdný
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("nevybranyData"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}

				else if (Ukladani.kontrolaNazvu(tfNazev.getText())) { // Kontrola jedinečnosti zadaného názvu
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("duplicitaNazev"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}

				else {

					DefaultCategoryDataset bary = new DefaultCategoryDataset();
					DefaultCategoryDataset body = new DefaultCategoryDataset();
					DefaultCategoryDataset spojnice = new DefaultCategoryDataset();
					DefaultCategoryDataset area = new DefaultCategoryDataset();
					DefaultCategoryDataset detected = new DefaultCategoryDataset();
					DefaultPieDataset pie = new DefaultPieDataset();

					for (int i = 1; i < data.pocetSloupcu(); i++) {

						if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getPouzit()) { // Kontrola zda mají být data z tohoto panelu použita

							if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getNazev().equals("detected")) { // Vykreslení detekcí

								for (int j = 0; j < data.pocetRadku(); j++) {
									double max = data.getMaxData();
									detected.addValue(data.getData(i - 1, j) * max, data.getNazevSloupce(i),
											data.getDatum(j));
								}

							} else {

								if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getTyp() == Konstanty.CUSTOM_SLOUPCOVY) { // Vykreslení sloupců
									hmap.put(data.getNazevSloupce(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										bary.addValue(data.getData(i - 1, j), data.getNazevSloupce(i),
												data.getDatum(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getTyp() == Konstanty.CUSTOM_SPOJNICOVY) { // Vykreslení spojnic
									hmap.put(data.getNazevSloupce(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										spojnice.addValue(data.getData(i - 1, j), data.getNazevSloupce(i),
												data.getDatum(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getTyp() == Konstanty.CUSTOM_BODOVY) { // Vykreslení bodů
									hmap.put(data.getNazevSloupce(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										body.addValue(data.getData(i - 1, j), data.getNazevSloupce(i),
												data.getDatum(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getTyp() == Konstanty.CUSTOM_PLOSNY) { // Vykreslení plošných
									hmap.put(data.getNazevSloupce(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										area.addValue(data.getData(i - 1, j), data.getNazevSloupce(i),
												data.getDatum(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getTyp() == Konstanty.CUSTOM_PIE) { // Vykreslení koláčů
									for (int j = 0; j < data.pocetRadku(); j++) {
										pie.setValue(data.getDatum(j), data.getData(i - 1, j));
									}

									SwingUtilities.invokeLater(() -> {
										OknoCustomNahled example = new OknoCustomNahled(tfNazev.getText(), pie,
												projekt.getID());
										example.setSize(800, 400);
										example.setLocationRelativeTo(null);
										example.setVisible(true);
									});
									return;
								}
							}
						}
					}

					SwingUtilities.invokeLater(() -> {
						OknoCustomNahled example = new OknoCustomNahled(tfNazev.getText(), bary, body, spojnice, area,
								detected, hmap, projekt.getID());
						example.setSize(800, 400);
						example.setLocationRelativeTo(null);
						example.setVisible(true);
					});
				}
			}
		};
		
		// Přiřazení akcí komponentám
		cbIterace.addActionListener(actNactiData);
		cbOsoby.addActionListener(actNactiData);
		cbSql.addActionListener(actNastavMenu);
		nakresli.addActionListener(actNakresliGraf);
	}
}
