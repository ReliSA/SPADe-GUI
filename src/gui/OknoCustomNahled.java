package gui;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class OknoCustomNahled extends JFrame {

	private static final long serialVersionUID = 1L;

	JFreeChart chart;
	int projectID;
	int typGrafu;
	JButton save = new JButton("Ulož graf");
	JFrame close = this;

	public OknoCustomNahled(String title, DefaultCategoryDataset bary, DefaultCategoryDataset body,
			DefaultCategoryDataset spojnice, DefaultCategoryDataset area, DefaultCategoryDataset detected,
			HashMap<String, Color> hmap, int projectID) {
		super(title);

		CategoryPlot plot = new CategoryPlot();
		this.projectID = projectID;
		this.typGrafu = 0;

		CategoryItemRenderer detectRenderer = new BarRenderer();
		detectRenderer.setSeriesPaint(0, new Color(255, 0, 0, 50));
		((BarRenderer) detectRenderer).setShadowVisible(false);
		((BarRenderer) detectRenderer).setBarPainter(new StandardBarPainter());
		plot.setDataset(0, detected);
		plot.setRenderer(0, detectRenderer);

		CategoryItemRenderer lineRenderer = new LineAndShapeRenderer(true, false);
		for (int j = 0; j < spojnice.getRowKeys().size(); j++) {
			lineRenderer.setSeriesPaint(j, hmap.get(spojnice.getRowKey(j)));
		}
		plot.setDataset(1, spojnice);
		plot.setRenderer(1, lineRenderer);

		CategoryItemRenderer dotRenderer = new LineAndShapeRenderer(false, true);
		for (int j = 0; j < body.getRowKeys().size(); j++) {
			dotRenderer.setSeriesPaint(j, hmap.get(body.getRowKey(j)));
		}
		plot.setDataset(2, body);
		plot.setRenderer(2, dotRenderer);

		CategoryItemRenderer baRenderer = new BarRenderer();
		for (int j = 0; j < bary.getRowKeys().size(); j++) {
			baRenderer.setSeriesPaint(j, hmap.get(bary.getRowKey(j)));
		}
		plot.setDataset(3, bary);
		plot.setRenderer(3, baRenderer);

		CategoryItemRenderer areaRenderer = new AreaRenderer();
		for (int j = 0; j < area.getRowKeys().size(); j++) {
			areaRenderer.setSeriesPaint(j, hmap.get(area.getRowKey(j)));
		}
		plot.setDataset(4, area);
		plot.setRenderer(4, areaRenderer);

		plot.setDomainAxis(new CategoryAxis("Čas"));
		plot.setRangeAxis(new NumberAxis("Hodnoty"));

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2));

		chart = new JFreeChart(plot);
		chart.setTitle(title);

		ChartPanel panel = new ChartPanel(chart);
		JPanel vysledek = new JPanel(new BorderLayout());
		vysledek.add(panel, BorderLayout.CENTER);
		vysledek.add(save, BorderLayout.SOUTH);
		setContentPane(vysledek);
		nastavAkce();
	}

	public OknoCustomNahled(String title, DefaultPieDataset dataset, int projectID) {
		super(title);
		this.projectID = projectID;
		this.typGrafu = Konstanty.PIE;
		chart = ChartFactory.createPieChart(title, dataset, true, true, false);
		chart.removeLegend();
		

		ChartPanel panel = new ChartPanel(chart);
		JPanel vysledek = new JPanel(new BorderLayout());
		vysledek.add(panel, BorderLayout.CENTER);
		vysledek.add(save, BorderLayout.SOUTH);
		setContentPane(vysledek);
		nastavAkce();
	}

	protected void nastavAkce() {

		ActionListener actSaveButton = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				prepravkaUkladaniCustom prepravka = new prepravkaUkladaniCustom(chart, projectID, typGrafu);
				Ukladani.add(prepravka);
				Ukladani.getPanel().vlozGraf(new DropChartPanel(chart, typGrafu));
				close.dispose();
			}
		};

		save.addActionListener(actSaveButton);
	}

}