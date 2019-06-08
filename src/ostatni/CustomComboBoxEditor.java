package ostatni;

import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

/**
 * Třída pro vytvoření vlastního editoru JComboBoxu
 *
 * @author Patrik Bezděk
 */
public class CustomComboBoxEditor extends BasicComboBoxEditor {

    /**
     * Vrací editor komponenty
     * @return editor komponenty
     */
    public Component getEditorComponent(){
        Component comp = super.getEditorComponent();
        return comp;
    }

    /**
     * Nastaví barvu pozadí komponenty
     * @param color barva pozadí komponenty
     */
    public void changeBackground(Color color){
        Component comp = super.getEditorComponent();
        comp.setBackground(color);
    }

}
