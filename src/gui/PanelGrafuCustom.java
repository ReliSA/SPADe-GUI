package gui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JFrame;

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
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import data.CustomGraf;

public class PanelGrafuCustom extends JFrame {

	private static final long serialVersionUID = 1L;

	public PanelGrafuCustom(String title, DefaultCategoryDataset bary, DefaultCategoryDataset body,
			DefaultCategoryDataset spojnice, DefaultCategoryDataset area, HashMap<String, Color> hmap) {
		super(title);

		CategoryPlot plot = new CategoryPlot();

		CategoryItemRenderer lineRenderer = new LineAndShapeRenderer(true, false);
		for (int j = 0; j < spojnice.getRowKeys().size(); j++) {
			lineRenderer.setSeriesPaint(j, hmap.get(spojnice.getRowKey(j)));
		}
		plot.setDataset(0, spojnice);
		plot.setRenderer(0, lineRenderer);

		CategoryItemRenderer dotRenderer = new LineAndShapeRenderer(false, true);
		for (int j = 0; j < body.getRowKeys().size(); j++) {
			dotRenderer.setSeriesPaint(j, hmap.get(body.getRowKey(j)));
		}
		plot.setDataset(1, body);
		plot.setRenderer(1, dotRenderer);

		CategoryItemRenderer baRenderer = new BarRenderer();
		for (int j = 0; j < bary.getRowKeys().size(); j++) {
			baRenderer.setSeriesPaint(j, hmap.get(bary.getRowKey(j)));
		}
		plot.setDataset(2, bary);
		plot.setRenderer(2, baRenderer);

		CategoryItemRenderer areaRenderer = new AreaRenderer();
		for (int j = 0; j < area.getRowKeys().size(); j++) {
			areaRenderer.setSeriesPaint(j, hmap.get(area.getRowKey(j)));
		}
		plot.setDataset(3, area);
		plot.setRenderer(3, areaRenderer);

		plot.setDomainAxis(new CategoryAxis("Čas"));
		plot.setRangeAxis(new NumberAxis("Hodnoty"));

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2));
		
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(title);

		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

}