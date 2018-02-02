package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ostatni.Konstanty;

/**
 * Třída sloužící jako handler pro zpracování dropu drag and drop gesta.
 * 
 * @author LukasHaringer
 *
 */
public class DropHandler implements DropTargetListener {

	/**
	 * Metoda pro urření zda lze zpracovat data daného dropu.
	 */
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {

		if (dtde.isDataFlavorSupported(PanelDataFlavor.SHARED_INSTANCE)) {

			dtde.acceptDrag(DnDConstants.ACTION_MOVE);

		} else {

			dtde.rejectDrag();

		}

	}

	/**
	 * Metoda pro zpracování dat dropu. Rozbalí data z tranferable wrapperu,
	 * nastavaví parametry DropChartPanelu, přidá DropChartPanel do odpovídajícího
	 * containeru a revaliduje.
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {

		boolean success = false;

		if (dtde.isDataFlavorSupported(PanelDataFlavor.SHARED_INSTANCE)) {

			Transferable transferable = dtde.getTransferable();
			try {

				Object data = transferable.getTransferData(PanelDataFlavor.SHARED_INSTANCE);
				if (data instanceof JPanel) {

					DropChartPanel panel = (DropChartPanel) data;

					DropTargetContext dtc = dtde.getDropTargetContext();
					Component component = dtc.getComponent();

					if (component instanceof JComponent) {

						Container parent = panel.getParent();
						if (parent != null) {

							parent.remove(panel);

						}
						
						panel.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
						panel.setDragable(false);
						panel.setDomainZoomable(true);
						panel.setRangeZoomable(true);
						panel.zobrazLegendu(true);

						((JComponent) component).removeAll();
						((JComponent) component).add(panel);

						success = true;
						dtde.acceptDrop(DnDConstants.ACTION_MOVE);

						panel.revalidate();

					} else {

						success = false;
						dtde.rejectDrop();

					}

				} else {

					success = false;
					dtde.rejectDrop();

				}

			} catch (Exception exp) {

				success = false;
				dtde.rejectDrop();
				exp.printStackTrace();

			}

		} else {

			success = false;
			dtde.rejectDrop();

		}

		dtde.dropComplete(success);

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

}