package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import ostatni.Konstanty;

/**
 * Color chooser pro výběr barvy grafu
 */
public class ColorChooserButton extends JButton {

	private static final long serialVersionUID = 4240004211439922869L;
	private Color current; // právě zvolená barva
	private List<ColorChangedListener> listeners = new ArrayList<ColorChangedListener>();

	/**
	 * Konstruktor třídy
	 * 
	 * @param c
	 *            zvolená barva
	 */
	public ColorChooserButton(Color c) {

		this.setPreferredSize(Konstanty.VELIKOST_COLOR_PICKER);
		current = c;
		setIcon(createIcon(current, 60, 13));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
				setSelectedColor(newColor);
			}
		});
	}

	/**
	 * Metoda pro získání zvolené barvy chooseru
	 * 
	 * @return zvolená barva
	 */
	public Color getSelectedColor() {
		return current;
	}

	/**
	 * Nastaví novou barvu colorpickeru
	 * 
	 * @param newColor
	 *            nová barva
	 */
	public void setSelectedColor(Color newColor) {
		setSelectedColor(newColor, true);
	}

	/**
	 * Nastaví novou barvu colorpickeru
	 * 
	 * @param newColor
	 *            nová barva
	 * @param notify
	 *            určuje zda mají být notifikováni registrovaní listeners
	 */
	public void setSelectedColor(Color newColor, boolean notify) {

		if (newColor == null)
			return;

		current = newColor;

		setIcon(createIcon(current, this.getWidth() - 10, this.getHeight() - 10));

		repaint();

		if (notify) {
			for (ColorChangedListener l : listeners) {
				l.colorChanged(newColor);
			}
		}
	}

	/**
	 * Rozhraní ColorChangedListener
	 */
	public static interface ColorChangedListener {
		public void colorChanged(Color newColor);
	}

	/**
	 * Přidá nový ColorChangedListener
	 * 
	 * @param toAdd
	 *            ColorChangedListener pro přidání
	 */
	public void addColorChangedListener(ColorChangedListener toAdd) {
		listeners.add(toAdd);
	}

	/**
	 * Metoda vykreslí ikonu na tlačítko v aktuálně vybrané barvě
	 * 
	 * @param main
	 *            barva ikony
	 * @param width
	 *            šířka ikony
	 * @param height
	 *            výška ikony
	 * @return ImageIcon ikona
	 */
	private static ImageIcon createIcon(Color main, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(main);
		graphics.fillRect(0, 0, width, height);
		graphics.setXORMode(Color.DARK_GRAY);
		graphics.drawRect(0, 0, width - 1, height - 1);
		image.flush();
		ImageIcon icon = new ImageIcon(image);
		return icon;
	}
}