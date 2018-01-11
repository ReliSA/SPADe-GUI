package gui;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

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

	/**
	 * Kontruktor DropChartPanelu. Vytvoří panel a zakáže zoomování grafu.
	 * 
	 * @param chart
	 *            JFreeChart graf
	 */
	public DropChartPanel(JFreeChart chart) {
		super(chart);
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
			dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY,
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

}
