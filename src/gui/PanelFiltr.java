package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ostatni.Konstanty;

/**
 * Abstraktní třída, počáteční panel filtrů ze kterého ostatní panely filtru dědí,
 * nastavuje checkbox Použít filtr a seznam výběru daného typu
 * @author michalvselko
 *
 */
public abstract class PanelFiltr extends JPanel{
	
	protected ArrayList<?> seznam;																					//seznam možného výběru
	protected JCheckBox ckPouzitFiltr = new JCheckBox(Konstanty.POPISY.getProperty("checkBoxPouzitFiltr"), true);	//zaškrtávátko Použít filtr
	protected JList<?> lsSeznamFiltr;																				//zobrazený seznam
	private GridBagConstraints grid = new GridBagConstraints();														//grid pro nastavení pozic komponent	
    
	/**
	 * Konstruktor třídy, nastaví seznam výběru
	 * @param seznam seznam výběru
	 */
	public PanelFiltr(ArrayList seznam){
		this.seznam = seznam;
	}
	
	/**
	 * Vrací seznam vybraných položek
	 * @return seznam vybraných položek
	 */
	public List getSeznamVybranych(){
		return this.lsSeznamFiltr.getSelectedValuesList();
	}

	/**
	 * Vrací grid pro nastavení pozic komponent
	 * @return grid komponent
	 */
	public GridBagConstraints getGrid(){
		return this.grid;
	}
	
	/**
	 * Vrací model ze seznamu z parametru
	 * @param seznam seznam, ze kterého se vytvoří model 
	 * @return vytvořený model
	 */
	private DefaultComboBoxModel getModel(ArrayList seznam){
		DefaultComboBoxModel model = new DefaultComboBoxModel();	    
		for(int i = 0; i < seznam.size() ; i++)
			model.addElement(seznam.get(i));		
		return model;
	}
	
	/**
	 * Zjistí zda je zaškrtnuto Použít filtr
	 * @return true, pokud je Použít filtr zaškrtnuto
	 */
	public boolean jePouzit(){
		return this.ckPouzitFiltr.isSelected();
	}
		
	/**
	 * Nastaví panel filtrů
	 * @param nazev název panelu filtrů
	 */
	protected void nastavPanel(String nazev){
		String titulek = "";
		switch(nazev){
		case "Ukol": 		titulek = Konstanty.POPISY.getProperty("nazevUkoly");
 							break;
		case "Priority": 	titulek = Konstanty.POPISY.getProperty("nazevPriority");
							break;			
		case "Severity": 	titulek = Konstanty.POPISY.getProperty("nazevSeverity");
							break;
		case "Status": 		titulek = Konstanty.POPISY.getProperty("nazevStatusy");
							break;
		case "Typ": 	 	titulek = Konstanty.POPISY.getProperty("nazevTypy");
							break;
		case "Resoluce": 	titulek = Konstanty.POPISY.getProperty("nazevResoluce");
							break;
		case "Osoby": 		titulek = Konstanty.POPISY.getProperty("nazevOsoby");
							break;
		case "Faze": 		titulek = Konstanty.POPISY.getProperty("nazevFaze");
							break;
		case "Iterace": 	titulek = Konstanty.POPISY.getProperty("nazevIterace");
							break;
		case "Aktivity": 	titulek = Konstanty.POPISY.getProperty("nazevAktivity");
							break;
		case "Konfigurace":	titulek = Konstanty.POPISY.getProperty("nazevKonfigurace");
							break;
		case "Artefakty":	titulek = Konstanty.POPISY.getProperty("nazevArtefakty");
							break;
		default: break;	
		}
		this.setBorder(BorderFactory.createTitledBorder(titulek)); 
		
		lsSeznamFiltr = new JList(getModel(this.seznam));
		lsSeznamFiltr.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		lsSeznamFiltr.setVisibleRowCount(3);
		lsSeznamFiltr.setVisibleRowCount(Math.min(lsSeznamFiltr.getModel().getSize(), 3));
		JScrollPane scScrollPane = new JScrollPane(lsSeznamFiltr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		ckPouzitFiltr.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
		scScrollPane.setPreferredSize(Konstanty.VELIKOST_SEZNAMU_CELA_SIRKA);
		
		nastavAkce();
		
		grid.fill = GridBagConstraints.HORIZONTAL;
        
        this.setLayout(new GridBagLayout());
        grid.insets = new Insets(5, 5, 5, 5);
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 4;
        this.add(ckPouzitFiltr, grid);
        grid.gridy++;
        this.add(scScrollPane, grid);

        this.setName(nazev);
	}
	
	/**
	 * Abstraktní třída pro nastavení akcí u jednotlivých komponent
	 */
	protected abstract void nastavAkce();
	
	/**
	 * Abstraktní třída pro vrácení seznamu id odpovídajících zadaným podmínkám filtru
	 * @return seznam povolených id
	 */
	public abstract ArrayList<Integer> getSeznamId();		
		
}
