package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import ostatni.Konstanty;
import data.Osoba;
import data.Projekt;
import data.polozky.PolozkaVytvoreni;

/**
 * Abstraktní třída, předek panelů s grafy ze kterého ostatní panely dědí
 */
public abstract  class PanelGrafuRodic extends JPanel {

	private static final long serialVersionUID = -3293103160690992576L;
	
	protected Projekt projekt; // vybraný projekt

	/**
	 * Konstruktor třídy, nastaví projekt
	 * 
	 * @param projekt
	 *            vybraný projekt
	 */
	public PanelGrafuRodic(Projekt projekt) {
		super();
		this.setBackground(Color.WHITE);

		this.projekt = projekt;
		this.removeAll();
	}

	/**
	 * Abstraktní metoda pro vložení grafů do panelu
	 */
	protected abstract void vlozGrafy();

	/**
	 * Nastaví zobrazení panelu
	 */
	protected void nastavZobrazeni() {
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setPreferredSize(Konstanty.VELIKOST_GRAFU);
		}
		;
	};
	
	/**
	 * Odstraní menu panelu
	 */
	protected void odstranMenu() {
		for (int i = 0; i < this.getComponentCount(); i++) {
			((DropChartPanel)this.getComponent(i)).setPopupMenu(null);
		}
		;
	};

	/**
	 * Vrátí miniaturu grafu zpet do příslušného panelu
	 */
	protected void vratGrafMiniatura(DropChartPanel panel) {

		panel.setDragable(true);
		panel.setDomainZoomable(false);
		panel.setRangeZoomable(false);
		panel.zobrazLegendu(false);
		panel.setPopupMenu(null);
		this.add(panel);

	}

	/**
	 * Podle typu grafu nastaví jeho zobrazení
	 * 
	 * @param graf
	 *            graf který se má nastavit
	 * @param typGrafu
	 *            typ grafu
	 */
	protected void nastavGraf(JFreeChart graf, int typGrafu) {
		try {
			graf.getTitle().setFont(Konstanty.FONT_NAZEV_GRAFU);
			
			if (typGrafu == Konstanty.SLOUPCOVY) {
				CategoryPlot plot = (CategoryPlot) graf.getPlot();

				ValueAxis rangeAxis = plot.getRangeAxis();
				CategoryAxis domainAxis = plot.getDomainAxis();

				rangeAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);
				domainAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);

				domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

				rangeAxis.setVisible(false);
				domainAxis.setVisible(false);
			}

			if (typGrafu == Konstanty.HISTOGRAM || typGrafu == Konstanty.SPOJNICOVY) {
				XYPlot xyplot = graf.getXYPlot();
				DateAxis domainAxis = new DateAxis();
				ValueAxis rangeAxis = xyplot.getRangeAxis();

				xyplot.setDomainAxis(domainAxis);
				rangeAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);
				domainAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);

				domainAxis.setVisible(false);
				rangeAxis.setVisible(false);

				if (typGrafu == Konstanty.HISTOGRAM)
					domainAxis.setDateFormatOverride(new SimpleDateFormat("MM/yyyy"));
				else {
					xyplot.getRenderer().setSeriesStroke(0, new BasicStroke(4));
					xyplot.getRenderer().setSeriesPaint(0, new Color(145, 0, 145));
					domainAxis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy"));
				}

			}

			if (typGrafu == Konstanty.GANTT) {
				CategoryPlot ganttPlot = (CategoryPlot) graf.getPlot();
				ValueAxis rangeAxis = ganttPlot.getRangeAxis();
				CategoryAxis domainAxis = ganttPlot.getDomainAxis();
				DateAxis axis = (DateAxis) rangeAxis;
				axis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy"));

				rangeAxis.setVisible(false);
				domainAxis.setVisible(false);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaNastaveniVzhleduGrafu"));
			e.printStackTrace();
		}
	}

	/**
	 * Podle typu vytvoří sloupcový graf podle autorů
	 * 
	 * @param typVypoctu
	 *            určuje, jaký počet se u autora sleduje (počet úkolů, konfigurací
	 *            nebo artefaktů)
	 * @param bezNul
	 *            true, pokud se mají do datasetu vložit i nulové sloupce
	 * @return sloupcový graf počtu podle autorů
	 */
	protected  JFreeChart getAutorGraf(int typVypoctu, boolean bezNul) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String nazevGrafu = "";
		try {
			ArrayList<Osoba> osoby = projekt.getOsoby();
			switch (typVypoctu) {
			case Konstanty.UKOL:
				for (int i = 0; i < osoby.size(); i++) {
					if (osoby.get(i).getPocetUkolu() != 0 || !bezNul) {
						int delka = Math.min(osoby.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);
						String delsiText = "";
						if (delka < osoby.get(i).getNazev().length())
							delsiText = "...";
						dataset.addValue(osoby.get(i).getPocetUkolu(), projekt.getNazev(),
								osoby.get(i).getNazev().substring(0, delka) + delsiText);
					}
				}
				nazevGrafu = Konstanty.POPISY.getProperty("nazevGrafuAutorUkol");
				break;
			case Konstanty.KONFIGURACE:
				for (int i = 0; i < osoby.size(); i++) {
					if (osoby.get(i).getPocetKonfiguraci() != 0 || !bezNul) {
						int delka = Math.min(osoby.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);
						String delsiText = "";
						if (delka < osoby.get(i).getNazev().length())
							delsiText = "...";
						dataset.addValue(osoby.get(i).getPocetKonfiguraci(), projekt.getNazev(),
								osoby.get(i).getNazev().substring(0, delka) + delsiText);
					}
				}
				nazevGrafu = Konstanty.POPISY.getProperty("nazevGrafuAutorKonfigurace");
				break;
			case Konstanty.ARTEFAKT:
				for (int i = 0; i < osoby.size(); i++) {
					if (osoby.get(i).getPocetArtefaktu() != 0 || !bezNul) {
						int delka = Math.min(osoby.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);
						String delsiText = "";
						if (delka < osoby.get(i).getNazev().length())
							delsiText = "...";
						dataset.addValue(osoby.get(i).getPocetArtefaktu(), projekt.getNazev(),
								osoby.get(i).getNazev().substring(0, delka) + delsiText);
					}
				}
				nazevGrafu = Konstanty.POPISY.getProperty("nazevGrafuAutorArtefakt");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaGrafuAutor"));
			e.printStackTrace();
		}
		JFreeChart graf = ChartFactory.createBarChart(nazevGrafu, Konstanty.POPISY.getProperty("popisAutor"),
				Konstanty.POPISY.getProperty("popisPocet"), dataset, PlotOrientation.VERTICAL, false, true, false);

		nastavGraf(graf, Konstanty.SLOUPCOVY);
		CategoryItemRenderer renderer = new RendererSloupcovyGraf(null, -1);
		graf.getCategoryPlot().setRenderer(renderer);

		return graf;
	}

	/**
	 * Vloží checkbox pro rozhodnutí zda zobrazit nulové sloupce do panelu grafu
	 * autoru
	 * 
	 * @param chartPanel
	 *            panel grafu, do kterého se má checkbox vložit
	 * @param typVypoctu
	 *            parametr pro znovu načtení grafu, určuje, jaký počet se u autora
	 *            sleduje (počet úkolů, konfigurací nebo artefaktů)
	 */
	public void vlozCheckBoxBezNulAutor(ChartPanel chartPanel, int typVypoctu) {
		CheckBoxbezNul ckBezNul =  new CheckBoxbezNul(projekt, chartPanel, typVypoctu,Konstanty.BEZ_AUTOR);
		ckBezNul.setText(Konstanty.POPISY.getProperty("bezNul"));
		chartPanel.getPopupMenu().add(ckBezNul);
	}

	/**
	 * Vytvoří spojnicový graf rostoucího počtu položek v seznamu (úkolů,
	 * konfigurací, artefaktů) v závislosti na datu vytvoření
	 * 
	 * @param seznam
	 *            seznam položek jejichž počet se bude zkoumat
	 * @param nazevGrafu
	 *            název grafu
	 * @return spojnicový graf počtu položek v závislosti na datu vytvoření
	 */
	protected JFreeChart getSpojnicovyGrafPocet(ArrayList seznam, String nazevGrafu) {
		TimeSeriesCollection dataset = null;
		try {
			ArrayList<LocalDate> datumy = new ArrayList<LocalDate>();
			ArrayList<Integer> pocet = new ArrayList<Integer>();

			for (int i = 0; i < seznam.size(); i++) {
				boolean nalezeno = false;
				LocalDate datum = ((PolozkaVytvoreni) seznam.get(i)).getDatumVytvoreni();
				for (int j = 0; j < datumy.size(); j++) {
					if (datum.isBefore(datumy.get(j))) {
						int pomocPocet = pocet.get(j) + 1;
						pocet.set(j, pomocPocet);

						nalezeno = true;
					}
				}
				if (!nalezeno && datum.isAfter(Konstanty.DATUM_PRAZDNY)) {
					datumy.add(datum);
					if (pocet.size() == 0)
						pocet.add(1);
					else
						pocet.add(pocet.get(pocet.size() - 1) + 1);
				}
			}

			TimeSeries serie = new TimeSeries(nazevGrafu);
			for (int i = 0; i < datumy.size(); i++) {
				serie.addOrUpdate(
						new Day(datumy.get(i).getDayOfMonth(), datumy.get(i).getMonthValue(), datumy.get(i).getYear()),
						(double) pocet.get(i));
			}
			dataset = new TimeSeriesCollection(serie);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaRodicSpojnicovy") + " " + nazevGrafu
					+ "! " + Konstanty.POPISY.getProperty("chybaRodicZnovunacteni"));
			e.printStackTrace();
		}

		JFreeChart graf = ChartFactory.createTimeSeriesChart(nazevGrafu, Konstanty.POPISY.getProperty("popisDatum"),
				Konstanty.POPISY.getProperty("popisPocet"), dataset, true, true, true);

		nastavGraf(graf, Konstanty.SPOJNICOVY);
		return graf;
	}

}
