package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import ostatni.Konstanty;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FormularVytvoreniKonstanty extends JDialog
{
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField tfName = new JTextField();
    private JTextField tfValue = new JTextField();
    private String constName;
    private String constValue;
    private boolean cancelled = false;
    static Logger log = Logger.getLogger(FormularVytvoreniKonstanty.class);

    public FormularVytvoreniKonstanty(String name, String value){
        setModal(true);

        setSize(Konstanty.VELIKOST_PRIHLASOVACIHO_OKNA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setTitle("Create constant");

        JLabel lblName = new JLabel("Name");
        JLabel lblValue = new JLabel("Value");

        tfName.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
        tfValue.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
        btnOk.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        btnCancel.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);

        nastavAkce();        //nastaví akce tlačítek

        constName = name;
        constValue = value;
        tfName.setText(constName);
        tfValue.setText(constValue);

        this.setLayout(new MigLayout());
        this.add(lblName);
        this.add(tfName, "wrap, span 2");
        this.add(lblValue);
        this.add(tfValue, "wrap, span 2");
        this.add(new JLabel());
        this.add(btnOk);
        this.add(btnCancel);
        this.setVisible(true);
    }

    public FormularVytvoreniKonstanty() {
        this("","");
    }

    public String getConstName(){
        String returnValue = this.constName;
        if(!cancelled){
            returnValue = tfName.getText();
        }
        return returnValue;
    }

    public String getConstValue(){
        String returnValue = this.constValue;
        if(!cancelled){
            returnValue = tfValue.getText();
        }
        return returnValue;
    }

    public boolean wasCancelled(){
        return cancelled;
    }

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
    }
}
