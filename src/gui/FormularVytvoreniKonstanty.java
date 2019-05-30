package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import ostatni.Konstanty;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Okno pro vytvoření konstanty
 */
class FormularVytvoreniKonstanty extends JDialog
{
    private JButton btnOk = new JButton(Konstanty.POPISY.getProperty("tlacitkoOk"));
    private JButton btnCancel = new JButton(Konstanty.POPISY.getProperty("tlacitkoZrusit"));
    private JTextField tfName = new JTextField();
    private JTextField tfValue = new JTextField();
    private String constName;
    private String constValue;
    private boolean cancelled = false;
    static Logger log = Logger.getLogger(FormularVytvoreniKonstanty.class);

    /**
     * Konstruktor okna
     */
    public FormularVytvoreniKonstanty() {
        this("","");
    }

    /**
     * Konstruktor okna
     * @param name název konstanty
     * @param value hodnota konstanty
     */
    public FormularVytvoreniKonstanty(String name, String value){
        setModal(true);

        setSize(380, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setTitle(Konstanty.POPISY.getProperty("vytvorKonstantu"));
        this.setIconImage(Konstanty.IMG_ICON.getImage());

        JLabel lblName = new JLabel(Konstanty.POPISY.getProperty("popisNazev"));
        JLabel lblValue = new JLabel(Konstanty.POPISY.getProperty("popisHodnota"));

        tfName.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
        tfValue.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
        btnOk.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        btnCancel.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);

        nastavAkce();        //nastaví akce tlačítek

        constName = name;
        constValue = value;
        tfName.setText(constName);
        tfValue.setText(constValue);

        this.setLayout(new MigLayout("ins 25 45 0 0"));
        this.add(lblName);
        this.add(tfName, "wrap, span 2");
        this.add(lblValue);
        this.add(tfValue, "wrap, span 2");
        this.add(new JLabel());
        this.add(btnOk);
        this.add(btnCancel);
        this.setVisible(true);
    }

    /**
     * Vrací název konstany
     * @return název konstanty
     */
    public String getConstName(){
        String returnValue = this.constName;
        if(!cancelled){
            returnValue = tfName.getText();
        }
        return returnValue;
    }

    /**
     * Vrací hodnotu konstanty
     * @return hodnotu konstanty
     */
    public String getConstValue(){
        String returnValue = this.constValue;
        if(!cancelled){
            returnValue = tfValue.getText();
        }
        return returnValue;
    }

    /**
     * Vrací informaci jestli byl formulář potvrzen nebo zrušen
     * @return true pokud nebyl formulář potvrzen
     */
    public boolean wasCancelled(){
        return cancelled;
    }

    /**
     * Nastavení akcí pro ovládací prvky
     */
    private void nastavAkce(){
        /*akce po kliknutí na tlačítko přihlásit*/
        ActionListener actOk = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        /*akce po kliknutí na tlačítko ukončit*/
        ActionListener actCancel = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelled = true;
                dispose();
            }
        };
        /*připojení akcí k jednotlivým komponentám*/
        btnOk.addActionListener(actOk);
        btnCancel.addActionListener(actCancel);

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                cancelled = true;
            }
        });
    }
}
