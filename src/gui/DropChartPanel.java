package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;

import ostatni.Konstanty;

/**
 * Třída zděděná od ChartPanel. Přidává podporu drag and drop.
 * 
 * @author Lukas Haringer
 *
 */
public class DropChartPanel extends ChartPanel {

	private static final long serialVersionUID = 2761733014332749739L;
	private DragGestureRecognizer dgr;
	private DragGestureHandler dragGestureHandler;

	/** Slouží pro povolení a zakázaní přetahování panelu. */
	private boolean dragable = true;

	private int typGrafu;
	protected int typPanelu;

	/**
	 * Kontruktor DropChartPanelu. Vytvoří panel a zakáže zoomování grafu.
	 * 
	 * @param chart
	 *            JFreeChart graf
	 */
	public DropChartPanel(JFreeChart chart, int typGrafu, int typPanelu) {
		super(chart);
		this.typGrafu = typGrafu;
		this.typPanelu = typPanelu;
		this.setDomainZoomable(false);
		this.setRangeZoomable(false);
	}

	/**
	 * Metoda pro vytvoření a přidání drag and drop handleru.
	 */
	@Override
	public void addNotify() {

		super.addNotify();

		if (dgr == null && dragable) {

			dragGestureHandler = new DragGestureHandler(this);
			dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE,
					dragGestureHandler);

		}

	}

	/**
	 * Metoda pro odebrání drag and drop handleru.
	 */
	@Override
	public void removeNotify() {

		if (dgr != null) {

			dgr.removeDragGestureListener(dragGestureHandler);
			dragGestureHandler = null;

		}

		dgr = null;
		super.removeNotify();

	}

	/**
	 * Metoda pro zjištení zda lze panel přetahovat.
	 * 
	 * @return true/false
	 */
	public boolean isDragable() {
		return dragable;
	}

	/**
	 * Metoda pro nastavení zda lze panel přetahovat.
	 * 
	 * @param dragable
	 *            true/false
	 */
	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}

	
	
	
	
	/**
	 * Metoda pro zobrazení/skrytí legendy grafu
	 * @param zobraz určuje zda se má legenda zobrazit nebo schovat
	 */
	protected void zobrazLegendu(boolean zobraz) {
		try {

			JFreeChart graf = this.getChart();

			if (typGrafu == Konstanty.SLOUPCOVY || typGrafu == Konstanty.GANTT) {
				CategoryPlot plot = (CategoryPlot) graf.getPlot();
				ValueAxis rangeAxis = plot.getRangeAxis();
				CategoryAxis domainAxis = plot.getDomainAxis();
				if (zobraz == true) {
					rangeAxis.setVisible(true);
					domainAxis.setVisible(true);
				} else {
					rangeAxis.setVisible(false);
					domainAxis.setVisible(false);
				}
			}

			if (typGrafu == Konstanty.HISTOGRAM || typGrafu == Konstanty.SPOJNICOVY) {
				XYPlot xyplot = graf.getXYPlot();
				DateAxis domainAxis = new DateAxis();
				ValueAxis rangeAxis = xyplot.getRangeAxis();

				xyplot.setDomainAxis(domainAxis);
				rangeAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);
				domainAxis.setTickLabelFont(Konstanty.FONT_POPISKY_GRAFU);

				if (zobraz == true) {
					rangeAxis.setVisible(true);
					domainAxis.setVisible(true);
				} else {
					rangeAxis.setVisible(false);
					domainAxis.setVisible(false);
				}

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("chybaNastaveniVzhleduGrafu"));
			e.printStackTrace();
		}
	}

}
