package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;

public class ColorPicker extends JComboBox {
	static Color[] colors = {Color.red, Color.blue, Color.green, Color.BLACK, Color.ORANGE, Color.YELLOW, Color.PINK, Color.magenta,Color.CYAN,Color.lightGray};

	public ColorPicker() {
		super(colors);
		this.setMaximumRowCount(10);
		this.setPreferredSize(new Dimension(50, 20));
		this.setRenderer(new ColorPickerRenderer());
	}
	
	public Color getColor() {
		int index = this.getSelectedIndex();
		
		return colors[index];
	}

}
