package gui;

import net.miginfocom.swing.MigLayout;
import ostatni.ComboBoxItem;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class SloupecCustomGrafu extends JPanel {
    private JLabel lblNazev = new JLabel(); // popisek název
    private int index;
    private List<String> data;
    private JCheckBox useColumn = new JCheckBox();
    private JCheckBox useVariable = new JCheckBox();
    private JComboBox<String> operators = new JComboBox<>();
    private JComboBox<ComboBoxItem> cboxVariableValues = new JComboBox();
    private JTextField tfValue = new JTextField();
    private List<Boolean> detectedValues = new ArrayList<>();

    /**
     * Konstruktor panelu pro první sloupec s časovou osou
     *
     * @param nazev název dat
     * @param data datumy k zobrazení
     */
    public SloupecCustomGrafu(String nazev, List<String> data, int index, List<ComboBoxItem> variableValues, boolean includeHeader) {
        this.data = data;
        this.index = index;
        this.setBackground(Color.white);

        this.setLayout(new MigLayout("ins 0"));
        this.lblNazev.setText(nazev);
        Font font = new Font("Courier", Font.BOLD, 12);
        this.lblNazev.setFont(font);
        this.lblNazev.setHorizontalAlignment(SwingConstants.CENTER);

        for(ComboBoxItem varValue : variableValues){
            cboxVariableValues.addItem(varValue);
        }

        operators.addItem("=");
        operators.addItem("<=");
        operators.addItem(">=");
        operators.addItem("<");
        operators.addItem(">");
        operators.addItem("!=");

        useVariable.setToolTipText("Use variables");
        useVariable.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                remove(useColumn);
                remove(useVariable);
                remove(operators);
                if(useVariable.isSelected()){
                    remove(tfValue);
                    remove(lblNazev);
                    add(useColumn);
                    add(useVariable);
                    add(operators,"wrap");
                    if(!variableValues.isEmpty()) {
                        cboxVariableValues.setSelectedIndex(0);
                    }
                    add(cboxVariableValues,"grow, span 3, wrap, gapy 9");
                    add(lblNazev, "grow, span 3, wrap");
                } else {
                    remove(cboxVariableValues);
                    tfValue.setText("");
                    add(useColumn);
                    add(useVariable);
                    add(operators, "wrap");
                    add(tfValue, "grow, span 3, wrap, gapy 9");
                    add(lblNazev, "grow, span 3, wrap");
                }
                revalidate();
                repaint();
            }
        });

        if(includeHeader) {
            this.add(useColumn);
            this.add(useVariable);
            this.add(operators, "wrap");
            this.add(tfValue, "grow, span 3, wrap, gapy 9");
            this.add(this.lblNazev, "grow, span 3, wrap");
        } else {
            this.add(this.lblNazev, "grow, span 3, wrap, gapy 58");
        }


        this.lblNazev.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        JLabel lblHodnota;
        for (String hodnota : data) {
            lblHodnota = new JLabel(hodnota);
            lblHodnota.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
            lblHodnota.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(lblHodnota, "grow, span 3, wrap, dock south");
        }
    }
    
    public List<Boolean> detectValues(){
        Double userInput = Double.parseDouble(getDetectedValue());
        String operator = getOperator();
        List<Boolean> tempDetect = new ArrayList<>();
        switch (operator) {
            case "=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp.compareTo(userInput) == 0) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "<":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp < userInput) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case ">":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp > userInput) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case ">=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp >= userInput) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "<=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp <= userInput) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "!=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp.compareTo(userInput) != 0) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
        }
        this.detectedValues = tempDetect;
        return tempDetect;
    }

    public boolean useColum(){
        return this.useColumn.isSelected();
    }

    public boolean useVariable(){
        return this.useVariable.isSelected();
    }

    public String getDetectedValue(){
        return this.tfValue.getText();
    }

    public String getOperator(){
        return (String) this.operators.getSelectedItem();
    }

    public List<String> getData(){
        return this.data;
    }

    public String getName(){
        return lblNazev.getText();
    }
}
