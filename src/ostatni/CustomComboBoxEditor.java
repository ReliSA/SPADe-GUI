package ostatni;

import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

public class CustomComboBoxEditor extends BasicComboBoxEditor {

    public Component getEditorComponent(){
        Component comp = super.getEditorComponent();
        return comp;
    }

    public void changeBackground(Color color){
        Component comp = super.getEditorComponent();
        comp.setBackground(color);
    }

}
