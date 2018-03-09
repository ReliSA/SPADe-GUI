package gui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ostatni.Konstanty;
import data.polozky.PolozkaCiselnik;

/**
 * Panel filtrů pro číselníky (priority, severity ...) zděděný z PanelFiltr
 * @author michalvselko
 *
 */
public class PanelFiltrCiselnik extends PanelFiltr{
	
	private JList<String> lsSeznamTrid = new JList<String>();												//seznam tříd
	private JList<String> lsSeznamSuperTrid = new JList<String>();											//seznam supertříd
	private JComboBox<String> cbLogOperace = new JComboBox<>(Konstanty.POLE_LOG_OPERATORU);
	private JComboBox<String> cbSpojeniTridy = new JComboBox<String>(Konstanty.POLE_PODMINEK);				//operátor pro spojení s podmínkou třídy (a, nebo)
	private JComboBox<String> cbSpojeniSuperTridy = new JComboBox<String>(Konstanty.POLE_PODMINEK);			//operátor pro spojení s podmínkou supertřídy (a, nebo)	
	
	/**
	 * Konstruktor třídy
	 * @param nazev název panelu filtru
	 * @param seznam seznam položek
	 */
	public PanelFiltrCiselnik(String nazev, ArrayList<PolozkaCiselnik> seznam){
		super(seznam);
		nastavPanel(nazev);
	}
	
	public String getLogOperand() {
		return (String) cbLogOperace.getSelectedItem();
	}
	
	/**
	 * Vrací seznam id odpovídajících zadaným podmínkám
	 * @return seznam id odpovídajících zadaným podmínkám
	 */
	public ArrayList<Integer> getSeznamId(){		
		ArrayList<Integer> seznamId = new ArrayList<Integer>();
		if(jePouzit()){																	//musí být zaškrtnuto Použít filtr
			for(int i = 0; i < this.seznam.size(); i++){								//projde položky seznamu
				PolozkaCiselnik polozka = (PolozkaCiselnik)this.seznam.get(i);
				
				/*zjišťuje, jaké hodnoty jsou vybrány v podmínkách spojení třídy a supertřídy
				 * a podle toho kontroluje hodnoty v položce*/
				if(cbSpojeniTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("a")) && cbSpojeniSuperTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("a"))){
					if(jeVSeznamu(polozka, Konstanty.HODNOTA) && jeVSeznamu(polozka, Konstanty.TRIDA) && jeVSeznamu(polozka, Konstanty.SUPERTRIDA))
						seznamId.add(polozka.getID());
				} else if(cbSpojeniTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("a")) && cbSpojeniSuperTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("nebo"))){
					if(jeVSeznamu(polozka, Konstanty.HODNOTA) && jeVSeznamu(polozka, Konstanty.TRIDA) || jeVSeznamu(polozka, Konstanty.SUPERTRIDA))
						seznamId.add(polozka.getID());
					
				} else if(cbSpojeniTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("nebo")) && cbSpojeniSuperTridy.getSelectedItem().equals(Konstanty.POPISY.getProperty("a"))){
					if(jeVSeznamu(polozka, Konstanty.HODNOTA) || jeVSeznamu(polozka, Konstanty.TRIDA) && jeVSeznamu(polozka, Konstanty.SUPERTRIDA))
						seznamId.add(polozka.getID());
				} else {
					if(jeVSeznamu(polozka, Konstanty.HODNOTA) || jeVSeznamu(polozka, Konstanty.TRIDA) || jeVSeznamu(polozka, Konstanty.SUPERTRIDA))
						seznamId.add(polozka.getID());
				}				
			}						
		}
		if(seznamId.isEmpty())		//pokud je seznam prázdný doplním alespoň -1 jinak by při načítání dat seznam jevil jako nepoužitý
			seznamId.add(-1);
		return seznamId;
	}

	/**
	 * Zjišťuje, zda je zadaná položka v seznamu vybraných položek podle typu
	 * @param polozka položka, která se hledá v seznamu
	 * @param typVypoctu typ seznamu, který se prohledává (první seznam, seznam tříd nebo seznam supertříd)
	 * @return true pokud je položka v seznamu
	 */
	private boolean jeVSeznamu(PolozkaCiselnik polozka, int typVypoctu){		
		List<?> seznam;
		try{
			switch(typVypoctu){																		//podle typu vybírá konkrétní seznam vybraných položek pro prohledání
			case Konstanty.HODNOTA:    	seznam = this.lsSeznamFiltr.getSelectedValuesList();
										if(seznam.isEmpty() || seznam.contains(polozka))			//metoda vrátí true, pokud je položka v seznamu vybraných nebo pokud je seznam prázdný 
											return true;											//pokud je seznam prázdný tak nebyla vybrána žádná konkrétní položka a nemá se na tomto seznamu filtrovat
										break;
			case Konstanty.TRIDA:	   	seznam = this.lsSeznamTrid.getSelectedValuesList();
										if(seznam.isEmpty() || seznam.contains(polozka.getTrida()))
											return true;
										break;
			case Konstanty.SUPERTRIDA: 	seznam = this.lsSeznamSuperTrid.getSelectedValuesList();
										if(seznam.isEmpty() || seznam.contains(polozka.getSuperTrida()))
											return true;
										break;
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaFiltrCiselniku"));
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Vrací model tříd ze seznamu z parametru
	 * @param seznam seznam, ze kterého se vytvoří model
	 * @return vytvořený model tříd
	 */
	private DefaultComboBoxModel getModelTrid(ArrayList seznam){
		DefaultComboBoxModel model = new DefaultComboBoxModel();	    
		for(int i = 0; i < seznam.size() ; i++){
			PolozkaCiselnik polozka = (PolozkaCiselnik)seznam.get(i);
			if(model.getIndexOf(polozka.getTrida()) == -1)
				model.addElement(polozka.getTrida());
		}
		return model;
	}

	/**
	 * Vrací model supertříd ze seznamu z parametru
	 * @param seznam seznam, ze kterého se vytvoří model
	 * @return vytvořený model supertříd
	 */
	private DefaultComboBoxModel getModelSuperTrid(ArrayList seznam){
		DefaultComboBoxModel model = new DefaultComboBoxModel();	    
		for(int i = 0; i < seznam.size() ; i++){
			PolozkaCiselnik polozka = (PolozkaCiselnik)seznam.get(i);
			if(model.getIndexOf(polozka.getSuperTrida()) == -1)
				model.addElement(polozka.getSuperTrida());
		}
		
		return model;
	}

	/**
	 * Nastavení panelu, spustí se nastavení z předka a poté se do gridu přidávají nové komponenty
	 * @param nazev název filtru
	 */
	protected void nastavPanel(String nazev){
		super.nastavPanel(nazev);
		
		lsSeznamTrid = new JList(getModelTrid(this.seznam));
		lsSeznamSuperTrid = new JList(getModelSuperTrid(this.seznam));

		lsSeznamTrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lsSeznamSuperTrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		lsSeznamTrid.setVisibleRowCount(Math.min(lsSeznamTrid.getModel().getSize(), 3));
		lsSeznamSuperTrid.setVisibleRowCount(Math.min(lsSeznamSuperTrid.getModel().getSize(), 3));
		
		JScrollPane scScrollPaneTrid = new JScrollPane(lsSeznamTrid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		JScrollPane scScrollPaneSuperTrid = new JScrollPane(lsSeznamSuperTrid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		
		cbSpojeniTridy.setEnabled(false);
		cbSpojeniSuperTridy.setEnabled(false);

		nastavAkce();
		
		GridBagConstraints grid = this.getGrid();
		
		grid.gridx = 0;
		grid.gridy = 3;
		this.add(cbLogOperace,grid);
		
		if(!nazev.equals(Konstanty.POPISY.getProperty("nazevOsoby"))){			
	        grid.gridx = 5;
	        grid.gridy = 0;
	        grid.gridwidth = 1;
	        this.add(cbSpojeniTridy, grid);
	        grid.gridx = 6;
	        grid.gridwidth = 3;
	        this.add(scScrollPaneTrid, grid);
	        if(!nazev.equals(Konstanty.POPISY.getProperty("nazevTypy"))){
		        grid.gridy++;
		        grid.gridx = 5;
		        grid.gridwidth = 1;
		        this.add(cbSpojeniSuperTridy, grid);
		        grid.gridx = 6;
		        grid.gridwidth = 3;
		        this.add(scScrollPaneSuperTrid, grid);
		     }
		}
        this.setName(nazev);
	}
	
	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	protected void nastavAkce(){
		/*Akce pro změně zaškrtávátka Použít filtr*/
		ActionListener actZmenaPouzitFiltr = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!ckPouzitFiltr.isSelected()){
					lsSeznamFiltr.clearSelection();			//Hodnoty se vynulují
					lsSeznamTrid.clearSelection();
					lsSeznamSuperTrid.clearSelection();
					cbSpojeniTridy.setEnabled(false);
					cbSpojeniSuperTridy.setEnabled(false);
				}
			}
		};
		
		/*Akce pro změnu výběru tříd*/
		ListSelectionListener actZmenaVyberTrid = new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				cbSpojeniTridy.setEnabled(true);			//spojení se povoluje pouze po vybrání tříd
			}
		};
		
		/*Akce pro změnu výběru supertříd*/
		ListSelectionListener actZmenaVyberSuperTrid = new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				cbSpojeniSuperTridy.setEnabled(true);
			}
		};

		ckPouzitFiltr.addActionListener(actZmenaPouzitFiltr);
		lsSeznamTrid.addListSelectionListener(actZmenaVyberTrid);
		lsSeznamSuperTrid.addListSelectionListener(actZmenaVyberSuperTrid);
	}
	

}
