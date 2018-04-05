package gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.datatransfer.*;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.Serializable;

import javax.swing.JPanel;

/**
 * Třída sloužící jako handler pro gesto přetažení. Implementuje
 * {@link DragGestureListener} a {@link DragSourceListener}
 * 
 * @author LukasHaringer
 *
 */

public class DragGestureHandler implements DragGestureListener, DragSourceListener{

	private Container parent;
	private JPanel child;

	/**
	 * Konstruktor třídy DragGestureHandler
	 * 
	 * @param child
	 *            přetahovaný JPanel
	 */
	public DragGestureHandler(JPanel child) {

		this.child = child;
	}

	/**
	 * Metoda pro zachycení gesta přetažení. Odebere položku z conteineru, vytvoří
	 * transferable wrapper a zahájí proces přetažení.
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {

		Container parent = getPanel().getParent();

		setParent(parent);

		parent.remove(getPanel());

		parent.invalidate();
		parent.repaint();

		Transferable transferable = new PanelTransferable(getPanel());

		DragSource ds = dge.getDragSource();
		ds.startDrag(dge, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), transferable, this);

	}

	/**
	 * Metoda pro ukončení přetažení, v případě neúspěšného přetažení vrátí panel
	 * předchozímu rodiči.
	 */
	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {

		if (!dsde.getDropSuccess()) {

			getParent().add(getPanel());

			getParent().invalidate();
			getParent().repaint();

		}
	}

	/**
	 * Vrací refenci na daný JPanel
	 * 
	 * @return refence přetahovaného JPanelu
	 */
	public JPanel getPanel() {
		return child;
	}

	/**
	 * Slouží pro nastavení rodičovského containeru
	 * 
	 * @param parent
	 *            rodičovský container.
	 */
	public void setParent(Container parent) {
		this.parent = parent;
	}

	/**
	 * Vrací referenci na rodičovský container.
	 * 
	 * @return rodičovský container
	 */
	public Container getParent() {
		return parent;
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
	}
}