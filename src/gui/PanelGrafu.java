package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import ostatni.Konstanty;
import data.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Centrální panel zobrazující statistický panel a panely s grafy
 * @author michalvselko
 *
 */
public class PanelGrafu extends JPanel {
	
	private Projekt projekt;		//aktuálně vybraný projekt
	private JScrollPane scroll;		//scroll panel
	private DropTarget dropTarget;
	private DropHandler dropHandler;
	private DropTarget dropTarget2;
	private DropHandler dropHandler2;
	
	/**
	 * Konstruktor třídy, nastaví projekt, spustí načtení dat projektu a nastaví zobrazení okna
	 * @param projekt aktuálně vybraný projekt
	 */
	public PanelGrafu(Projekt projekt){
		super();
		this.setBackground(Color.WHITE);
		
		this.projekt = projekt;
		this.projekt.nactiData();
		this.nastavZobrazeni();		
	}
	
	
	/**
	 * Nastaví velikost scroll panelu v závislosti na velikosti panelu
	 */
	public void setSizeScroll(){
		scroll.setPreferredSize(new Dimension(this.getWidth() - 20, this.getHeight() - 20));
		scroll.revalidate();
	}
	
	/**
	 * Nastaví nový projekt a spustí nové načtení dat
	 * @param projekt nový projekt
	 */
	public void setProjekt(Projekt projekt){
		this.projekt = projekt;
		this.projekt.nactiData();
		
		this.nastavZobrazeni();
		this.revalidate();
		
	}

	/**
	 * Spustí nové načtení dat projektu odpovídající zadaným podmínkám
	 * @param seznamIdUkolu seznam id možných úkolů pro vložení
	 * @param seznamIdPriorit seznam id priorit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdSeverit seznam id severit jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdResoluci seznam id resolucí jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdStatusu seznam id statusů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdTypu seznam id typů jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdOsob seznam id osob jež obsahují úkoly které lze vložit do seznamu
	 * @param seznamIdFazi seznam id možných fází pro vložení
	 * @param seznamIdIteraci seznam id možných iterací pro vložení
	 * @param seznamIdAktivit seznam id možných aktivit pro vložení
	 * @param seznamIdKonfiguraci seznam id možných konfigurací pro vložení
	 * @param seznamIdArtefaktu seznam id možných artefaktů pro vložení
	 */
	public void setPodminkyProjektu(ArrayList<Integer> seznamIdUkolu, ArrayList<Integer> seznamIdPriorit, ArrayList<Integer> seznamIdSeverit, 
									ArrayList<Integer> seznamIdResoluci, ArrayList<Integer> seznamIdStatusu, ArrayList<Integer> seznamIdTypu, 
									ArrayList<Integer> seznamIdOsob, ArrayList<Integer> seznamIdFazi, ArrayList<Integer> seznamIdIteraci, 
									ArrayList<Integer> seznamIdAktivit, ArrayList<Integer> seznamIdKonfiguraci, ArrayList<Integer> seznamIdArtefaktu){
		this.projekt.nactiData(seznamIdUkolu, seznamIdPriorit, seznamIdSeverit, seznamIdResoluci, seznamIdStatusu, seznamIdTypu, seznamIdOsob, seznamIdFazi, seznamIdIteraci, seznamIdAktivit, seznamIdKonfiguraci, seznamIdArtefaktu);
		
		this.nastavZobrazeni();
		this.revalidate();
		
	}
	
	/**
	 * Nastaví zobrazení panelu, vloží panel statistik a panely s grafy
	 */
	private void nastavZobrazeni(){
		this.removeAll();
		JPanel panel = new JPanel();
		
		JPanel dropSlot = new JPanel(new BorderLayout());
		dropSlot.setBackground(Color.WHITE);
		dropSlot.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
		
		JPanel dropSlot2 = new JPanel(new BorderLayout());
		dropSlot2.setBackground(Color.WHITE);
		dropSlot2.setPreferredSize(Konstanty.VELIKOST_GRAFU_VELKY);
				
		dropHandler = new DropHandler();
		dropTarget = new DropTarget(dropSlot, DnDConstants.ACTION_COPY, dropHandler, true);
		
		dropHandler2 = new DropHandler();
		dropTarget2 = new DropTarget(dropSlot2, DnDConstants.ACTION_COPY, dropHandler2, true);
			
		panel.setLayout(Konstanty.FLOW_LAYOUT);

		scroll = new JScrollPane(panel);
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		panel.setPreferredSize(Konstanty.VELIKOST_PANELU);
		this.setSizeScroll();
		
		JScrollPane scrollUkoly = new JScrollPane(new PanelGrafuUkol(this.projekt));
		scrollUkoly.getVerticalScrollBar().setUnitIncrement(15);
		scrollUkoly.setPreferredSize(new Dimension(Konstanty.SIRKA_PANELU_GRAFU+20,Konstanty.VYSKA_PANELU_GRAFU_STANDART+20));
		scrollUkoly.revalidate();
		
		JTabbedPane tabbedPanelGrafu = new JTabbedPane();		
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("segment"),new PanelGrafuSegment(this.projekt));
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("ukoly"),scrollUkoly);
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("konfigurace"),new PanelGrafuKonfigurace(this.projekt));
		tabbedPanelGrafu.add(Konstanty.POPISY.getProperty("artefakty"),new PanelGrafuArtefakt(this.projekt));
		
		
		JLabel label = new JLabel(Konstanty.POPISY.getProperty("grafTady"));
		JLabel label2= new JLabel(Konstanty.POPISY.getProperty("grafTady"));
		label.setFont(new Font("Arial", Font.PLAIN, 40));
		label2.setFont(new Font("Arial", Font.PLAIN, 40));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);
		label2.setVerticalAlignment(JLabel.CENTER);
				
		dropSlot.add(label);
		dropSlot2.add(label2);
	
		panel.add(getPopisProjektu());
		panel.add(tabbedPanelGrafu);
		panel.add(dropSlot);
		panel.add(dropSlot2);
		
        this.add(scroll);
      
	}

	/**
	 * Vytvoří panel s popisem projektu
	 * @return panel projektu
	 */
	private JPanel getPopisProjektu(){
		JLabel lblNazev = new JLabel(Konstanty.POPISY.getProperty("popisNazev") + ": " + projekt.getNazev());
		JLabel lblDatumOD = new JLabel(Konstanty.POPISY.getProperty("popisDatumOd") + ": " + projekt.getDatumPocatku().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		JLabel lblDatumDO;
		if(projekt.getDatumKonce().equals(Konstanty.DATUM_PRAZDNY))
			lblDatumDO = new JLabel(Konstanty.POPISY.getProperty("popisDatumDoNeomezeny"));
		else
			lblDatumDO = new JLabel(Konstanty.POPISY.getProperty("popisDatumDo") + ": " + projekt.getDatumKonce().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		
		lblNazev.setFont(Konstanty.FONT_NADPIS_STATISTIK);
		lblDatumOD.setFont(Konstanty.FONT_NADPIS_STATISTIK);
		lblDatumDO.setFont(Konstanty.FONT_NADPIS_STATISTIK);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		
		g.insets = new Insets(3, 3, 5, 5);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		g.gridy = 0;
		panel.add(lblNazev, g); g.gridy++;
		panel.add(lblDatumOD, g); g.gridy++;
		panel.add(lblDatumDO, g); g.gridy++;
		g.gridheight=4;
		g.gridy=0;
		g.gridx++;
		panel.add(getPanelStatistiky(true), g);
		g.gridy = 0;
		g.gridx++;
		panel.add(getPanelStatistiky(false), g);
		
		panel.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titlePanelStatistik")));
		panel.setBackground(Color.WHITE);
		return panel;
	}
	
	/**
	 * Vytvoří panel statistik projektu s počty nebo s minimem, maximem a průměrem hodnot (dle zadaného parametru)
	 * @param panelPocet určuje, zda se bude tvořit panel s počtem hodnot (true) nebo s maximem, minimem a průměrem (false)
	 * @return panel statistik
	 */
	private JPanel getPanelStatistiky(boolean panelPocet){
		JPanel panelStatistik = new JPanel();		
		try{
			panelStatistik.setBackground(Color.WHITE);
			panelStatistik.setLayout(new GridBagLayout());
			GridBagConstraints grid = new GridBagConstraints();
			grid.insets = new Insets(3, 3, 3, 3);
			grid.fill = GridBagConstraints.HORIZONTAL;
			grid.gridx = 0;
			grid.gridy = 0;
			
			if(panelPocet){
				panelStatistik.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titlePocet")));
				
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevFaze") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetFazi()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevIterace") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetIteraci()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevAktivity") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetAktivit()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevUkoly") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetUkolu()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevKonfigurace") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetKonfiguraci()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevVetve") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetVetvi()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevTagy") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetTagu()), grid); grid.gridx--;
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("nazevArtefakty") + ": "), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPocetArtefaktu()), grid); grid.gridx--;
				for(int i = 0; i < panelStatistik.getComponentCount(); i++)
					panelStatistik.getComponent(i).setFont(Konstanty.FONT_STATISTIK);
				
			}
			else {
				JLabel lblUkolyNa = new JLabel(Konstanty.POPISY.getProperty("popisUkolyNa") + ":");
				JLabel lblUkol = new JLabel(Konstanty.POPISY.getProperty("popisUkol") + ": ");
				JLabel lblKonfiguraceNa = new JLabel(Konstanty.POPISY.getProperty("popisKonfiguraceNa") + ": ");
				JLabel lblArtefaktNa = new JLabel(Konstanty.POPISY.getProperty("popisArtefaktNa") + ": ");
				
				JLabel lblMinimum = new JLabel(Konstanty.POPISY.getProperty("nadpisMinimum"));
				JLabel lblPrumer = new JLabel(Konstanty.POPISY.getProperty("nadpisPrumer"));
				JLabel lblMaximum = new JLabel(Konstanty.POPISY.getProperty("nadpisMaximum"));				
				JLabel lblMinimum2 = new JLabel(Konstanty.POPISY.getProperty("nadpisMinimum"));
				JLabel lblPrumer2 = new JLabel(Konstanty.POPISY.getProperty("nadpisPrumer"));
				JLabel lblMaximum2 = new JLabel(Konstanty.POPISY.getProperty("nadpisMaximum"));				
				JLabel lblMinimum3 = new JLabel(Konstanty.POPISY.getProperty("nadpisMinimum"));
				JLabel lblPrumer3 = new JLabel(Konstanty.POPISY.getProperty("nadpisPrumer"));
				JLabel lblMaximum3 = new JLabel(Konstanty.POPISY.getProperty("nadpisMaximum"));				
				JLabel lblMinimum4 = new JLabel(Konstanty.POPISY.getProperty("nadpisMinimum"));
				JLabel lblPrumer4 = new JLabel(Konstanty.POPISY.getProperty("nadpisPrumer"));
				JLabel lblMaximum4 = new JLabel(Konstanty.POPISY.getProperty("nadpisMaximum"));
								
				panelStatistik.setBorder(BorderFactory.createTitledBorder(Konstanty.POPISY.getProperty("titleMinMaxAvg")));	
				panelStatistik.add(new JLabel(""), grid); grid.gridx++;		
				panelStatistik.add(lblMinimum, grid); grid.gridx++;		
				panelStatistik.add(lblPrumer, grid); grid.gridx++;		
				panelStatistik.add(new JSeparator(SwingConstants.VERTICAL),grid);
				panelStatistik.add(lblMaximum, grid); grid.gridx++;	grid.gridx++;
				panelStatistik.add(lblMinimum2, grid); grid.gridx++;		
				panelStatistik.add(lblPrumer2, grid); grid.gridx++;		
				panelStatistik.add(lblMaximum2, grid); grid.gridx++;grid.gridx++;	
				panelStatistik.add(lblMinimum3, grid); grid.gridx++;		
				panelStatistik.add(lblPrumer3, grid); grid.gridx++;		
				panelStatistik.add(lblMaximum3, grid); grid.gridx++;grid.gridx++;	
				panelStatistik.add(lblMinimum4, grid); grid.gridx++;		
				panelStatistik.add(lblPrumer4, grid); grid.gridx++;		
				panelStatistik.add(lblMaximum4, grid); grid.gridx = 0;	
				
				grid.gridy++;
				panelStatistik.add(lblUkolyNa, grid);		
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisFazi") + ": "), grid);  grid.gridx++; 
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinFaze()), grid);  grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerFaze()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxFaze()), grid); grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisIteraci") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinIterace()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerIterace()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxIterace()), grid); grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisAktivitu") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinAktivity()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerAktivity()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxAktivity()), grid); grid.gridx = 0;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaUkol()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaUkol()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaUkol()), grid); grid.gridx = 0;
				grid.fill = GridBagConstraints.HORIZONTAL;			
				grid.gridy=1;
				grid.gridx=4;
				panelStatistik.add(lblUkol, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCasovyOdhad") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinPredpokladanyCas()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerPredpokladanyCas()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxPredpokladanyCas()), grid); grid.gridx = 4;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisStravenyCas") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinStravenyCas()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerStravenyCas()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxStravenyCas()), grid); grid.gridx = 0;
				grid.fill = GridBagConstraints.HORIZONTAL;
				grid.gridy=1;
				grid.gridx=8;
				panelStatistik.add(lblKonfiguraceNa, grid); 
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaKonf()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaKonf()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaKonf()), grid); grid.gridx = 8;
				grid.fill = GridBagConstraints.HORIZONTAL;
				grid.gridy=1;
				grid.gridx=12;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(lblArtefaktNa, grid);
				grid.gridy++;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisKonfiguraci") + ": "), grid); grid.gridx++;		
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinKonfiguraceArtef()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerKonfiguraceArtef()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxKonfiguraceArtef()), grid); grid.gridx =12;
				grid.gridy++;
				grid.fill = GridBagConstraints.HORIZONTAL;
				panelStatistik.add(new JLabel(Konstanty.POPISY.getProperty("popisCloveka") + ": "), grid); grid.gridx++;
				grid.fill = GridBagConstraints.EAST;
				panelStatistik.add(new JLabel("" + projekt.getMinOsobaArtef()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getPrumerOsobaArtef()), grid); grid.gridx++;
				panelStatistik.add(new JLabel("" + projekt.getMaxOsobaArtef()), grid);
				
				for(int i = 0; i < panelStatistik.getComponentCount(); i++)
					panelStatistik.getComponent(i).setFont(Konstanty.FONT_STATISTIK);
				
				lblUkolyNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMinimum.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblPrumer.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMaximum.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMinimum2.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblPrumer2.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMaximum2.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMinimum3.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblPrumer3.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMaximum3.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMinimum4.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblPrumer4.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblMaximum4.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblUkol.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblKonfiguraceNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
				lblArtefaktNa.setFont(Konstanty.FONT_NADPIS_STATISTIK);
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaNacteniStatistik"));
			e.printStackTrace();
		}
		return panelStatistik;
	}
}

