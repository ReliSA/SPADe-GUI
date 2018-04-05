package gui;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JFrame;

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

public class PanelGrafuCustom extends JFrame {

	private static final long serialVersionUID = 1L;

	public PanelGrafuCustom(String title, DefaultCategoryDataset bary, DefaultCategoryDataset body,
			DefaultCategoryDataset spojnice, DefaultCategoryDataset area, DefaultCategoryDataset detected,
			HashMap<String, Color> hmap) {
		super(title);

		CategoryPlot plot = new CategoryPlot();

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
		/*
		 * try { FileOutputStream fos = new FileOutputStream("output.out");
		 * ObjectOutputStream oos = new ObjectOutputStream(fos); oos.writeObject(plot);
		 * oos.close(); } catch (Exception ex) { ex.printStackTrace(); }
		 */

		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(title);

		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	public PanelGrafuCustom(String title,DefaultPieDataset dataset) {
		super(title);
		
		JFreeChart chart = ChartFactory.createPieChart(      
		         title,  		   // chart title 
		         dataset,          // data    
		         true,             // include legend   
		         true, 
		         false);
		
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

}