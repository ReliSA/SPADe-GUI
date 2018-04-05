package data;

import java.io.Serializable;
import org.jfree.chart.JFreeChart;

public class prepravkaUkladaniCustom implements Serializable{
	
	private static final long serialVersionUID = 4881143472479215994L;
	
	JFreeChart panel;
	String Nazev;
	int projectID;
	int typGrafu;
	
	public int getTypGrafu() {
		return typGrafu;
	}

	public void setTypGrafu(int typGrafu) {
		this.typGrafu = typGrafu;
	}

	public prepravkaUkladaniCustom(JFreeChart panel, int projectID, int typGrafu,String Nazev) {
		this.panel = panel;
		this.Nazev=Nazev;
		this.projectID = projectID;
		this.typGrafu = typGrafu;
	}
	
	public String getNazev() {
		return Nazev;
	}

	public void setNazev(String nazev) {
		Nazev = nazev;
	}

	public JFreeChart getPanel() {
		return panel;
	}
	
	public void setPanel(JFreeChart panel) {
		this.panel = panel;
	}
	
	public int getProjectID() {
		return projectID;
	}
	
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

}
