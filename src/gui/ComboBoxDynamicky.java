package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * ComboBox pro zobrazení seznamu projektů
 */
public class ComboBoxDynamicky extends JComboBox{

	private static final long serialVersionUID = -732731792227789797L;
	private boolean layingOut = false; 
	
	/**
	 * Konstruktor třídy
	 * @param model datový model
	 */
	public ComboBoxDynamicky(ComboBoxModel<?> model) { 
        super(model); 
    } 
    
	/**
	 * Nastavení layoutu
	 */
    public void doLayout(){ 
        try{ 
            layingOut = true; 
            super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 
 
    /**
     * Vrátí velikost comboboxu
     */
    public Dimension getSize(){ 
        Dimension dim = super.getSize();         
        if(!layingOut) 
            dim.width = Math.max(getMaxSirka(), getPreferredSize().width); 
        
        return dim; 
    } 
    
    /**
     * Vrátí maximální šířku názvu projektu
     * @return maximální šířka názvu projektu
     */
    private int getMaxSirka() {
        Font font = this.getFont();
        FontMetrics metrics = this.getFontMetrics(font);
        int maxSirka = 0;
        for (int i = 0; i < this.getItemCount(); i++) {
            int novaSirka = metrics.stringWidth(this.getItemAt(i).toString());
            maxSirka = Math.max(maxSirka, novaSirka);
        }        
        return maxSirka + 30;
    }

}
