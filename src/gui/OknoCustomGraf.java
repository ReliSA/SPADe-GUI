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

public class OknoCustomGraf extends JFrame {

	private static final long serialVersionUID = -2359690226410306884L;

	JLabel lbLabelNazevOkna;

	int var;
	Projekt projekt;
	JButton nakresli = new JButton(Konstanty.POPISY.getProperty("vytvorGraf"));
	JPanel dataPanel;
	JPanel westPanel;
	JTextField tfNazev;
	JLabel lblNazev;
	JLabel lblIterace;
	JLabel lblOsoba;
	JComboBox<String> cbSql;
	JComboBox<?> cbIterace;
	JComboBox<?> cbOsoby;
	JLabel lbLblSql;
	HashMap<String, Color> hmap = new HashMap<String, Color>();
	ArrayList<PolozkaCiselnik> osoby;
	ArrayList<Segment> iterace;

	CustomGraf data;

	public OknoCustomGraf(Projekt projekt) {
		super(Konstanty.POPISY.getProperty("menuVytvorGraf"));

		this.projekt = projekt;
		osoby = (new Osoby(projekt.getID())).getSeznam();
		iterace = projekt.getIterace();

		nastavZobrazení();
		nastavAkce();
	}

	public void nastavZobrazení() {
		this.setLayout(new BorderLayout());

		dataPanel = new JPanel(new GridLayout(0, 1));
		JScrollPane scrollDataPanel = new JScrollPane(dataPanel);
		westPanel = new JPanel(new GridLayout(1, 0));
		tfNazev = new JTextField();
		lblIterace = new JLabel(Konstanty.POPISY.getProperty("nazevIterace"));
		lblOsoba = new JLabel(Konstanty.POPISY.getProperty("osoba"));
		lblNazev = new JLabel(Konstanty.POPISY.getProperty("nazevGrafu"));
		cbSql = new JComboBox<String>(Konstanty.getSQLKeys());
		cbIterace = new JComboBox(getModel(iterace));
		cbOsoby = new JComboBox(getModel(osoby));
		lbLblSql = new JLabel("SQL:");

		cbSql.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		tfNazev.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);

		this.setBackground(Color.white);
		dataPanel.setBackground(Color.white);

		lblIterace.setHorizontalAlignment(JLabel.CENTER);
		lblNazev.setHorizontalAlignment(JLabel.CENTER);
		lblOsoba.setHorizontalAlignment(JLabel.CENTER);
		lbLblSql.setHorizontalAlignment(JLabel.CENTER);

		this.add(scrollDataPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.NORTH);

		nastavMenu();

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

	private boolean dataCheck() {

		boolean bool = true;
		for (int i = 0; i < dataPanel.getComponentCount(); i++) {

			if (((panelDatCustomGrafu) dataPanel.getComponent(i)).getPouzit()) {
				bool = false;
			}
		}
		return bool;
	}

	private void nastavMenu() {

		String sql = (String) cbSql.getSelectedItem();
		var = Integer.parseInt(Konstanty.SQLsVar.getProperty(sql));

		westPanel.removeAll();

		westPanel.add(lbLblSql);
		westPanel.add(cbSql);

		if (var == 3) {
			westPanel.add(lblIterace);
			westPanel.add(cbIterace);
			westPanel.add(lblOsoba);
			westPanel.add(cbOsoby);
		} else if (var == 2) {
			westPanel.add(lblOsoba);
			westPanel.add(cbOsoby);
		} else if (var == 1) {
			westPanel.add(lblIterace);
			westPanel.add(cbIterace);
		}

		westPanel.add(lblNazev);
		westPanel.add(tfNazev);
		westPanel.add(nakresli);

		nactiData();
	}

	private void nactiData() {
		CustomGrafDAO dao = new CustomGrafDAO();
		int iteraceID = getIdIterace();

		data = dao.getCustomGrafData((String) cbSql.getSelectedItem(), projekt.getID(), getIdIterace(), getIdOsoby(),
				var);

		dataPanel.removeAll();
		for (int i = 1; i < data.pocetSloupcu(); i++) {
			dataPanel.add(new panelDatCustomGrafu(data.getNazvySloupcu(i), i - 1));
		}

		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce() {

		ActionListener actNactiData = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				nactiData();

			}
		};

		ActionListener actNastavMenu = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				nastavMenu();
			}
		};

		ActionListener actNakresliGraf = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (tfNazev.getText().equals("")) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("prazdnyNazev"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}

				else if (Ukladani.kontrolaNazvu(tfNazev.getText())) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("duplicitaNazev"),
							Konstanty.POPISY.getProperty("chyba"), JOptionPane.ERROR_MESSAGE);
				}
				
				else if (dataCheck()) {
					JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("nevybranyData"),
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

						if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getPouzit()) {

							if (!((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getNazev().equals("detected")) {

								if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 0) {
									hmap.put(data.getNazvySloupcu(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										bary.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i),
												data.getDatumy(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 1) {
									hmap.put(data.getNazvySloupcu(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										spojnice.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i),
												data.getDatumy(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 2) {
									hmap.put(data.getNazvySloupcu(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										body.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i),
												data.getDatumy(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 3) {
									hmap.put(data.getNazvySloupcu(i),
											((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
									for (int j = 0; j < data.pocetRadku(); j++) {
										area.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i),
												data.getDatumy(j));
									}
								}

								if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 4) {
									for (int j = 0; j < data.pocetRadku(); j++) {
										pie.setValue(data.getDatumy(j), data.getData(i - 1, j));
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

							} else {
								for (int j = 0; j < data.pocetRadku(); j++) {
									double max = data.getMaxData();
									detected.addValue(data.getData(i - 1, j) * max, data.getNazvySloupcu(i),
											data.getDatumy(j));
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

		cbIterace.addActionListener(actNactiData);
		cbOsoby.addActionListener(actNactiData);
		cbSql.addActionListener(actNastavMenu);
		nakresli.addActionListener(actNakresliGraf);
	}
}
