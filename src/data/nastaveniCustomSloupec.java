package data;

import java.awt.Color;
import java.io.Serializable;

public class nastaveniCustomSloupec implements Serializable{

	private static final long serialVersionUID = 5463862874537236741L;
	private boolean pouzit;
	private int typGrafu;
	private Color barva;
		
	public nastaveniCustomSloupec(boolean pouzit, int typGrafu, Color barva) {
		super();
		this.pouzit = pouzit;
		this.typGrafu = typGrafu;
		this.barva = barva;
	}
	
	public boolean isPouzit() {
		return pouzit;
	}
	public void setPouzit(boolean pouzit) {
		this.pouzit = pouzit;
	}
	public int getTypGrafu() {
		return typGrafu;
	}
	public void setTypGrafu(int typGrafu) {
		this.typGrafu = typGrafu;
	}
	public Color getBarva() {
		return barva;
	}
	public void setBarva(Color barva) {
		this.barva = barva;
	}
	
}
