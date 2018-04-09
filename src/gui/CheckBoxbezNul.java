package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
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
import data.Osoba;
import data.Projekt;
import data.Segment;
import data.Ukol;
import data.ciselnik.Ciselnik;
import data.ciselnik.Osoby;
import data.ciselnik.Priority;
import data.ciselnik.Resoluce;
import data.ciselnik.Severity;
import data.ciselnik.Status;
import data.ciselnik.Typ;
import ostatni.Konstanty;

public class CheckBoxbezNul extends JCheckBox implements Serializable, ActionListener {

	private static final long serialVersionUID = -4935806712434825124L;
	ChartPanel chartPanel;
	int typGrafu;
	int typTlacitka;
	Ciselnik seznam;
	ArrayList<Segment> segmenty;
	Projekt projekt;

	public CheckBoxbezNul(Projekt projekt, ChartPanel chartPanel, int typVypoctu, int typ) {
		this.typGrafu = typVypoctu;
		this.typTlacitka = typ;
		this.chartPanel = chartPanel;
		this.projekt = projekt;
		this.addActionListener(this);
	}

	public CheckBoxbezNul(Projekt projekt, ChartPanel chartPanel, Ciselnik seznam, int typGrafu, int typ) {
		this.typGrafu = typGrafu;
		this.typTlacitka = typ;
		this.chartPanel = chartPanel;
		this.projekt = projekt;
		this.seznam = seznam;
		this.addActionListener(this);
	}

	public CheckBoxbezNul(Projekt projekt, ChartPanel chartPanel, ArrayList<Segment> segmenty, int typGrafu, int typ) {
		this.typGrafu = typGrafu;
		this.typTlacitka = typ;
		this.chartPanel = chartPanel;
		this.projekt = projekt;
		this.segmenty = segmenty;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (typTlacitka == Konstanty.BEZ_AUTOR) {

			if (this.isSelected()) {
				chartPanel.setChart(getAutorGraf(projekt, typGrafu, true));
				chartPanel.repaint();
			} else {
				chartPanel.setChart(getAutorGraf(projekt, typGrafu, false));
				chartPanel.repaint();
			}
		} else if (typTlacitka == Konstanty.BEZ_CISELNIKY) {

			if (this.isSelected()) {
				chartPanel.setChart(getCiselnikGraf(projekt, seznam, typGrafu, true));
				chartPanel.repaint();
			} else {
				chartPanel.setChart(getCiselnikGraf(projekt, seznam, typGrafu, false));
				chartPanel.repaint();
			}
		} else if (typTlacitka == Konstanty.BEZ_SEGMENTY) {
			if (this.isSelected()) {
				chartPanel.setChart(getSegmentGraf(segmenty, typGrafu, true));
				chartPanel.repaint();
			} else {
				chartPanel.setChart(getSegmentGraf(segmenty, typGrafu, false));
				chartPanel.repaint();
			}

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
	public JFreeChart getAutorGraf(Projekt projekt, int typVypoctu, boolean bezNul) {
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
	 * Podle typu grafu nastaví jeho zobrazení
	 * 
	 * @param graf
	 *            graf který se má nastavit
	 * @param typGrafu
	 *            typ grafu
	 */
	public void nastavGraf(JFreeChart graf, int typGrafu) {
		try {
			graf.getTitle().setFont(Konstanty.FONT_NAZEV_GRAFU);

			if (typGrafu == Konstanty.SLOUPCOVY) {
				CategoryPlot plot = (CategoryPlot) graf.getPlot();

				ValueAxis rangeAxis = plot.getRangeAxis();
				CategoryAxis domainAxis = plot.getDomainAxis();

				rangeAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);
				domainAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);

				domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			}

			if (typGrafu == Konstanty.HISTOGRAM || typGrafu == Konstanty.SPOJNICOVY) {
				XYPlot xyplot = graf.getXYPlot();
				DateAxis domainAxis = new DateAxis();
				ValueAxis rangeAxis = xyplot.getRangeAxis();

				xyplot.setDomainAxis(domainAxis);
				rangeAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);
				domainAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);

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
				DateAxis axis = (DateAxis) rangeAxis;
				axis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy"));

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaNastaveniVzhleduGrafu"));
			e.printStackTrace();
		}
	}

	/**
	 * Vytvoří sloupcový graf z předaného číselník podle zadaných typů
	 * 
	 * @param seznam
	 *            číselník hodnot pro vytvoření grafu
	 * @param typGrafu
	 *            typ grafu pro vytvoření (zda je to graf pro normální hodnotu,
	 *            třídu nebo supertřídu)
	 * @param bezNul
	 *            true, pokud se mají do datasetu vložit i nulové sloupce
	 * @return sloupcový graf zadaného číselníku
	 */
	private JFreeChart getCiselnikGraf(Projekt projekt, Ciselnik seznam, int typGrafu, boolean bezNul) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			ArrayList<Ukol> ukoly = projekt.getUkoly();

			for (int i = 0; i < seznam.getPocetPolozek(); i++) {
				int pocet = 0;
				for (int j = 0; j < ukoly.size(); j++) {
					int id = -1;

					if (seznam instanceof Priority)
						id = ukoly.get(j).getPriorityID();
					else if (seznam instanceof Severity)
						id = ukoly.get(j).getSeverityID();
					else if (seznam instanceof Status)
						id = ukoly.get(j).getStatusID();
					else if (seznam instanceof Typ)
						id = ukoly.get(j).getTypID();
					else if (seznam instanceof Resoluce)
						id = ukoly.get(j).getResoluceID();
					else if (seznam instanceof Osoby) {
						id = ukoly.get(j).getPrirazenID();
						typGrafu = Konstanty.HODNOTA;
					}

					if (seznam.getSeznam().get(i).getID() == id) {
						pocet++;
					}
				}

				String nazev = "";
				switch (typGrafu) {
				case Konstanty.HODNOTA:
					if (pocet != 0 || !bezNul)
						dataset.addValue(pocet, projekt.getNazev(), seznam.getSeznam().get(i).getNazev());
					break;
				case Konstanty.TRIDA:
					nazev = seznam.getSeznam().get(i).getTrida();
					break;
				case Konstanty.SUPERTRIDA:
					nazev = seznam.getSeznam().get(i).getSuperTrida();
					break;
				}
				if (typGrafu == Konstanty.TRIDA || typGrafu == Konstanty.SUPERTRIDA) {
					boolean nalezeno = false;
					for (int j = 0; j < dataset.getColumnCount(); j++) {
						if (dataset.getColumnKeys().get(j).equals(nazev)) {
							if (pocet != 0 || !bezNul)
								dataset.incrementValue((double) pocet, projekt.getNazev(),
										(Comparable<?>) dataset.getColumnKeys().get(j));
							nalezeno = true;
							break;
						}
					}
					if (!nalezeno)
						if (pocet != 0 || !bezNul)
							dataset.addValue(pocet, projekt.getNazev(), nazev);
				}

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkolCiselnik"));
			e.printStackTrace();
		}

		String nazevGrafu = "";

		if (seznam instanceof Priority)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevPriority");
		else if (seznam instanceof Severity)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevSeverity");
		else if (seznam instanceof Status)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevStatusy");
		else if (seznam instanceof Typ)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevTypy");
		else if (seznam instanceof Resoluce)
			nazevGrafu = Konstanty.POPISY.getProperty("nazevResoluce");
		else if (seznam instanceof Osoby) {
			nazevGrafu = Konstanty.POPISY.getProperty("nazevPrirazeno");
			typGrafu = Konstanty.HODNOTA;
		}

		if (typGrafu == Konstanty.TRIDA)
			nazevGrafu += " - " + Konstanty.POPISY.getProperty("popisTridy");
		if (typGrafu == Konstanty.SUPERTRIDA)
			nazevGrafu += " - " + Konstanty.POPISY.getProperty("popisSupertridy");

		JFreeChart graf = ChartFactory.createBarChart(nazevGrafu, seznam.toString(),
				Konstanty.POPISY.getProperty("popisPocet"), dataset, PlotOrientation.VERTICAL, false, true, false);

		this.nastavGraf(graf, Konstanty.SLOUPCOVY);

		CategoryItemRenderer renderer = new RendererSloupcovyGraf(seznam, typGrafu);
		graf.getCategoryPlot().setRenderer(renderer);

		return graf;

	}

	/**
	 * Vytvoří sloupcový graf s počtem úkolů za segment (fáze, iterace, aktivity)
	 * 
	 * @param seznam
	 *            seznam segmentů s úkoly
	 * @param typGrafu
	 *            typ segmentu
	 * @param bezNul
	 *            true, pokud se mají do datasetu vložit i nulové sloupce
	 * @return sloupcový graf počtu úkolů na segment
	 */
	private JFreeChart getSegmentGraf(ArrayList<Segment> seznam, int typGrafu, boolean bezNul) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String nazev = "";
		try {
			for (int i = 0; i < seznam.size(); i++) {
				if (seznam.get(i).getPocetUkolu() != 0 || !bezNul) {
					int delka = Math.min(seznam.get(i).getNazev().length(), Konstanty.DELKA_POPISKU_OS);
					String delsiText = "";
					if (delka < seznam.get(i).getNazev().length())
						delsiText = "...";
					dataset.addValue(seznam.get(i).getPocetUkolu(), projekt.getNazev(),
							seznam.get(i).getNazev().substring(0, delka) + delsiText);
				}
			}

			switch (typGrafu) {
			case Konstanty.FAZE:
				nazev = Konstanty.POPISY.getProperty("nazevFaze");
				break;
			case Konstanty.ITERACE:
				nazev = Konstanty.POPISY.getProperty("nazevIterace");
				break;
			case Konstanty.AKTIVITY:
				nazev = Konstanty.POPISY.getProperty("nazevAktivity");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaUkolSegment"));
			e.printStackTrace();
		}
		JFreeChart graf = ChartFactory.createBarChart(nazev, nazev, Konstanty.POPISY.getProperty("popisPocet"), dataset,
				PlotOrientation.VERTICAL, false, true, false);

		this.nastavGraf(graf, Konstanty.SLOUPCOVY);

		CategoryItemRenderer renderer = new RendererSloupcovyGraf(null, -1);
		graf.getCategoryPlot().setRenderer(renderer);

		return graf;

	}

}