package gui;

import net.miginfocom.swing.MigLayout;
import ostatni.ComboBoxItem;
import ostatni.CustomComboBoxEditor;

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
    private boolean valid;
    private List<String> data;
    private JCheckBox useColumn = new JCheckBox();
    private JComboBox<String> operators = new JComboBox<>();
    private JComboBox<ComboBoxItem> cboxVariableValues = new JComboBox();
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
        this.setSize(100, this.getHeight());

        this.setLayout(new MigLayout("ins 0"));
        this.lblNazev.setText(nazev);
        Font font = new Font("Courier", Font.BOLD, 12);
        this.lblNazev.setFont(font);
        this.lblNazev.setHorizontalAlignment(SwingConstants.CENTER);

        cboxVariableValues.setEditor(new CustomComboBoxEditor());
        cboxVariableValues.setEditable(true);

        for(ComboBoxItem varValue : variableValues){
            cboxVariableValues.addItem(varValue);
        }
        cboxVariableValues.setSelectedIndex(-1);

        operators.addItem("=");
        operators.addItem("<=");
        operators.addItem(">=");
        operators.addItem("<");
        operators.addItem(">");
        operators.addItem("!=");
//        operators.addItem("between");

        if(includeHeader) {
            this.add(useColumn);
            this.add(operators, "wrap");
            this.add(cboxVariableValues, "grow, span 3, wrap, gapy 9");
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

    public boolean validateInput(){
        String value = getDetectedValue();
        try { Float.parseFloat(value);
        } catch(NumberFormatException e) {
            try { Double.parseDouble(value);
            } catch(NumberFormatException e1) {
                try { Integer.parseInt(value);
                } catch(NumberFormatException e2) {
                    try { Long.parseLong(value);
                    } catch(NumberFormatException e3) {
                        System.out.println(e3);
                        setValid(false);
                        return false;
                    }
                }
            }
        }
        setValid(true);
        return true;
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

    public void changeValueBackground(Color color){
        ((CustomComboBoxEditor) cboxVariableValues.getEditor()).changeBackground(color);
    }

    public boolean useColum(){
        return this.useColumn.isSelected();
    }

    public String getDetectedValue(){
        String value;
        if(cboxVariableValues.getEditor().getItem() instanceof ComboBoxItem){
            value = ((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue();
        } else {
            try {
                value = cboxVariableValues.getEditor().getItem().toString();
            } catch (NullPointerException e) {
                value = "";
            }
        }
        return value;
    }

    public void setCboxVariableValuesOk(){
        ((CustomComboBoxEditor) cboxVariableValues.getEditor()).changeBackground(null);
    }

    public void setCboxVariableValuesWarning(){
        ((CustomComboBoxEditor) cboxVariableValues.getEditor()).changeBackground(Color.PINK);
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
