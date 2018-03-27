package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jfree.data.category.DefaultCategoryDataset;

import data.CustomGraf;
import databaze.CustomGrafDAO;
import ostatni.Konstanty;

import javax.swing.JComboBox;

public class OknoCustomGraf extends JFrame {
	JLabel lbLabelNazevOkna;

	JButton nacti = new JButton("Načti data");
	JButton nakresli = new JButton(Konstanty.POPISY.getProperty("vytvorGraf"));
	JPanel dataPanel;
	JPanel westPanel;
	JTextField tfFieldProjekt;
	JTextField tfFieldIterace;
	JLabel lbLblProjekt;
	JLabel lbLblIterace;
	JComboBox cmbCombo0;
	JLabel lbLblSql;
	HashMap<String, Color> hmap = new HashMap<String, Color>();

	CustomGraf data;

	public OknoCustomGraf() {
		super(Konstanty.POPISY.getProperty("menuVytvorGraf"));
		nastavZobrazení();
		nastavAkce();

	}

	public void nastavZobrazení() {
		this.setLayout(new BorderLayout());

		dataPanel = new JPanel(new GridLayout(0, 1));
		JScrollPane scpPanel9 = new JScrollPane(dataPanel);
		westPanel = new JPanel(new GridLayout(1, 0));
		tfFieldProjekt = new JTextField();
		tfFieldIterace = new JTextField();
		lbLblProjekt = new JLabel(Konstanty.POPISY.getProperty("nazevProjekt"));
		lbLblIterace = new JLabel(Konstanty.POPISY.getProperty("nazevIterace"));
		cmbCombo0 = new JComboBox(Konstanty.getSQLKeys());
		lbLblSql = new JLabel("SQL:");

		cmbCombo0.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		tfFieldProjekt.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);
		tfFieldIterace.setPreferredSize(Konstanty.VELIKOST_CTVRTINOVA_SIRKA);

		this.setBackground(Color.white);
		dataPanel.setBackground(Color.white);	
		
		lbLblIterace.setHorizontalAlignment(JLabel.CENTER);
		lbLblProjekt.setHorizontalAlignment(JLabel.CENTER);
		lbLblSql.setHorizontalAlignment(JLabel.CENTER);
		
		westPanel.add(lbLblSql);
		westPanel.add(cmbCombo0);
		westPanel.add(lbLblProjekt);
		westPanel.add(tfFieldProjekt);
		westPanel.add(lbLblIterace);
		westPanel.add(tfFieldIterace);
		westPanel.add(nacti);
		westPanel.add(nakresli);

		this.add(scpPanel9, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.NORTH);

		this.setVisible(true);

	}

	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce() {

		/* Akce pro změně operatoru datumů */
		ActionListener actNactiData = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				CustomGrafDAO dao = new CustomGrafDAO();
				data = dao.getCustomGrafData((String)cmbCombo0.getSelectedItem(), Integer.parseInt(tfFieldProjekt.getText()),
						Integer.parseInt(tfFieldIterace.getText()));

				dataPanel.removeAll();

				for (int i = 1; i < data.pocetSloupcu(); i++) {
					dataPanel.add(new panelDatCustomGrafu(data.getNazvySloupcu(i),i-1));
				}

				getContentPane().revalidate();
				getContentPane().repaint();
			}
		};

		ActionListener actNakresliGraf = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DefaultCategoryDataset bary = new DefaultCategoryDataset();
				DefaultCategoryDataset body = new DefaultCategoryDataset();
				DefaultCategoryDataset spojnice = new DefaultCategoryDataset();
				DefaultCategoryDataset area = new DefaultCategoryDataset();

				for (int i = 1; i < data.pocetSloupcu(); i++) {

					if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).pouzit()) {

						if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 0) {
							hmap.put(data.getNazvySloupcu(i),
									((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
							for (int j = 0; j < data.pocetRadku(); j++) {
								bary.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i), data.getDatumy(j));
							}
						}

						if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 1) {
							hmap.put(data.getNazvySloupcu(i),
									((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
							for (int j = 0; j < data.pocetRadku(); j++) {
								spojnice.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i), data.getDatumy(j));
							}
						}

						if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 2) {
							hmap.put(data.getNazvySloupcu(i),
									((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
							for (int j = 0; j < data.pocetRadku(); j++) {
								body.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i), data.getDatumy(j));
							}
						}

						if (((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getTyp() == 3) {
							hmap.put(data.getNazvySloupcu(i),
									((panelDatCustomGrafu) dataPanel.getComponent(i - 1)).getColor());
							for (int j = 0; j < data.pocetRadku(); j++) {
								area.addValue(data.getData(i - 1, j), data.getNazvySloupcu(i), data.getDatumy(j));
							}
						}
					}
				}

				SwingUtilities.invokeLater(() -> {
					PanelGrafuCustom example = new PanelGrafuCustom((String)cmbCombo0.getSelectedItem(), bary, body,
							spojnice, area,hmap);
					example.setSize(800, 400);
					example.setLocationRelativeTo(null);
					example.setVisible(true);
				});
			}
		};

		nacti.addActionListener(actNactiData);
		nakresli.addActionListener(actNakresliGraf);
	}
}
