package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import data.prepravkaUkladaniCustom;
import ostatni.Konstanty;
import ostatni.Ukladani;

/**
 * Okno náhledu custom grafu. Slouží pro náhled vytvořeného grafu a jeho následné uložení.
 */
public class OknoCustomNahled extends JFrame {

	private static final long serialVersionUID = 8910356012762472124L;
	
	private JFreeChart chart; // odkaz na vykreslený graf
	private int projectID; // ID projektu
	private int typGrafu; // typ grafu
	private String nazev; // nazev grafu
	private JButton save; // tlačítko pro uložení vytvořeného grafu
	private JFrame close = this; // odkaz na toto okno

	/**
	 * Konstruktor okna pro všechny grafy kromě koláčových
	 * @param title název grafu
	 * @param bary dataset pro sloupcové grafy
	 * @param body dataset pro bodové grafy
	 * @param spojnice dataset pro spojnicové grafy
	 * @param area dataset pro plošné grafy
	 * @param detected dataset pro detekce
	 * @param colors hashmapa s barvami grafů
	 * @param projectID ID projektu
	 */
	public OknoCustomNahled(String title, DefaultCategoryDataset bary, DefaultCategoryDataset body,
			DefaultCategoryDataset spojnice, DefaultCategoryDataset area, DefaultCategoryDataset detected,
			HashMap<String, Color> colors, int projectID) {
		super(title);

		CategoryPlot plot = new CategoryPlot();
		this.projectID = projectID;
		this.typGrafu = 0;
		this.nazev = title;

		//Vykreslení detekcí
		CategoryItemRenderer detectRenderer = new BarRenderer();
		detectRenderer.setSeriesPaint(0, new Color(255, 0, 0, 50));
		((BarRenderer) detectRenderer).setShadowVisible(false);
		((BarRenderer) detectRenderer).setBarPainter(new StandardBarPainter());
		plot.setDataset(0, detected);
		plot.setRenderer(0, detectRenderer);

		//Vykreslení bodů
		CategoryItemRenderer lineRenderer = new LineAndShapeRenderer(true, false);
		for (int j = 0; j < spojnice.getRowKeys().size(); j++) {
			lineRenderer.setSeriesPaint(j, colors.get(spojnice.getRowKey(j)));
		}
		plot.setDataset(1, spojnice);
		plot.setRenderer(1, lineRenderer);

		//Vykreslení spojnic
		CategoryItemRenderer dotRenderer = new LineAndShapeRenderer(false, true);
		for (int j = 0; j < body.getRowKeys().size(); j++) {
			dotRenderer.setSeriesPaint(j, colors.get(body.getRowKey(j)));
		}
		plot.setDataset(2, body);
		plot.setRenderer(2, dotRenderer);

		//Vykreslení sloupců
		CategoryItemRenderer baRenderer = new BarRenderer();
		for (int j = 0; j < bary.getRowKeys().size(); j++) {
			baRenderer.setSeriesPaint(j, colors.get(bary.getRowKey(j)));
		}
		plot.setDataset(3, bary);
		plot.setRenderer(3, baRenderer);

		//Vykreslení plošných
		CategoryItemRenderer areaRenderer = new AreaRenderer();
		for (int j = 0; j < area.getRowKeys().size(); j++) {
			areaRenderer.setSeriesPaint(j, colors.get(area.getRowKey(j)));
		}
		plot.setDataset(4, area);
		plot.setRenderer(4, areaRenderer);

		plot.setDomainAxis(new CategoryAxis(Konstanty.POPISY.getProperty("cas")));
		plot.setRangeAxis(new NumberAxis(Konstanty.POPISY.getProperty("hodnoty")));

		plot.setNoDataMessage(Konstanty.POPISY.getProperty("noData"));
		plot.setNoDataMessageFont(new Font("SansSerif",Font.BOLD,24));
		plot.setNoDataMessagePaint(Color.RED);
		
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2));

		chart = new JFreeChart(plot);
		chart.setTitle(title);

		save = new JButton(Konstanty.POPISY.getProperty("ulozGraf"));
		ChartPanel panel = new ChartPanel(chart);
		JPanel vysledek = new JPanel(new BorderLayout());
		vysledek.add(panel, BorderLayout.CENTER);
		vysledek.add(save, BorderLayout.SOUTH);
		setContentPane(vysledek);
		nastavAkce();
	}

	/**
	 * Konstruktor okna pro koláčové grafy
	 * @param title název grafu
	 * @param dataset dataset koláčového grafu
	 * @param projectID ID projektu
	 */
	public OknoCustomNahled(String title, DefaultPieDataset dataset, int projectID) {
		super(title);
		this.projectID = projectID;
		this.typGrafu = Konstanty.PIE;
		this.nazev = title;
		chart = ChartFactory.createPieChart(title, dataset, true, true, false);
		chart.removeLegend();

		ChartPanel panel = new ChartPanel(chart);
		JPanel vysledek = new JPanel(new BorderLayout());
		save = new JButton(Konstanty.POPISY.getProperty("ulozGraf"));
		vysledek.add(panel, BorderLayout.CENTER);
		vysledek.add(save, BorderLayout.SOUTH);
		setContentPane(vysledek);
		nastavAkce();
	}

	/**
	 * Nastavení akcí jednotlivých komponent okna
	 */
	protected void nastavAkce() {

		// Akce pro uložení grafu
		ActionListener actSaveButton = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				prepravkaUkladaniCustom prepravka = new prepravkaUkladaniCustom(chart, projectID, typGrafu, nazev);
				Ukladani.add(prepravka);				
				close.dispose();
				
			}
		};

		save.addActionListener(actSaveButton);
	}

}