package gui;

import javafx.scene.control.ComboBox;
import net.miginfocom.swing.MigLayout;
import ostatni.ComboBoxItem;
import ostatni.CustomComboBoxEditor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SloupecCustomGrafu extends JPanel {
    private JLabel lblNazev = new JLabel(); // popisek název
    private int index;
    private boolean firstValid;
    private boolean secondValid;
    private boolean thirdValid;
    private boolean fourthValid;
    private List<String> data;
    private JCheckBox useColumn;
    private JComboBox<String> operators;
    private JComboBox<ComboBoxItem> cboxVariableValues;
    private JComboBox<ComboBoxItem> cboxVariableValues2;
    private JComboBox<ComboBoxItem> cboxVariableValues3;
    private JComboBox<ComboBoxItem> cboxVariableValues4;
    private JComboBox<String> cboxAritmethics;
    private JComboBox<String> cboxAritmethics2;
    private List<Boolean> detectedValues = new ArrayList<>();
    private JPanel headerPanel;
    private JPanel inputPanel;
    private JPanel inputPanel2;
    private static JFrame mainFrame;
    private boolean between;

    /* For testing only - remove in final version*/
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null , "Chyba");
                    e.printStackTrace();
                    System.exit(0);
                }
                try {

                    SloupecCustomGrafu graf = new SloupecCustomGrafu("ColumnName", new ArrayList<>(Arrays.asList("1","2","3")), 0,
                            new ArrayList<>(Arrays.asList(new ComboBoxItem("jmeno", "text", "name"), new ComboBoxItem("cislo","number","5"))), true);

                    mainFrame = new JFrame();
                    mainFrame.setLayout(new MigLayout("ins 0"));
                    mainFrame.setBounds(100,100,1600,800);
                    graf.setBackground(Color.PINK);

                    mainFrame.add(graf, "width 240");

                    mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Konstruktor panelu pro první sloupec s časovou osou
     *
     * @param nazev název dat
     * @param data datumy k zobrazení
     */
    public SloupecCustomGrafu(String nazev, List<String> data, int index, List<ComboBoxItem> variableValues, boolean includeHeader) {
        this.data = data;
        this.index = index;
        setBackground(Color.white);
        setLayout(new MigLayout("ins 0"));

        useColumn = new JCheckBox();
        operators = new JComboBox<>();
        cboxVariableValues = new JComboBox<ComboBoxItem>();
        cboxVariableValues2 = new JComboBox<ComboBoxItem>();
        cboxVariableValues3 = new JComboBox<ComboBoxItem>();
        cboxVariableValues4 = new JComboBox<ComboBoxItem>();
        cboxAritmethics = new JComboBox<>();
        cboxAritmethics2 = new JComboBox<>();
        detectedValues = new ArrayList<>();

        headerPanel = new JPanel();
        headerPanel.setLayout(new MigLayout("ins 0"));

        inputPanel = new JPanel();
        inputPanel.setLayout(new MigLayout("ins 0"));
        inputPanel2 = new JPanel();
        inputPanel2.setLayout(new MigLayout("ins 0"));

        this.lblNazev.setText(nazev);
        Font font = new Font("Courier", Font.BOLD, 12);
        this.lblNazev.setFont(font);
        this.lblNazev.setHorizontalAlignment(SwingConstants.CENTER);

        cboxVariableValues.setEditor(new CustomComboBoxEditor());
        cboxVariableValues.setEditable(true);
        cboxVariableValues2.setEditor(new CustomComboBoxEditor());
        cboxVariableValues2.setEditable(true);
        cboxVariableValues3.setEditor(new CustomComboBoxEditor());
        cboxVariableValues3.setEditable(true);
        cboxVariableValues4.setEditor(new CustomComboBoxEditor());
        cboxVariableValues4.setEditable(true);

        for(ComboBoxItem varValue : variableValues){
            cboxVariableValues.addItem(varValue);
            cboxVariableValues2.addItem(varValue);
            cboxVariableValues3.addItem(varValue);
            cboxVariableValues4.addItem(varValue);
        }
        cboxVariableValues.setSelectedIndex(-1);
        cboxVariableValues2.setSelectedIndex(-1);
        cboxVariableValues3.setSelectedIndex(-1);
        cboxVariableValues4.setSelectedIndex(-1);

        cboxAritmethics.addItem("");
        cboxAritmethics.addItem("+");
        cboxAritmethics.addItem("-");
        cboxAritmethics2.addItem("");
        cboxAritmethics2.addItem("+");
        cboxAritmethics2.addItem("-");

        operators.addItem("=");
        operators.addItem("<=");
        operators.addItem(">=");
        operators.addItem("<");
        operators.addItem(">");
        operators.addItem("!=");
        operators.addItem("between");

        between = false;

        operators.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(operators.getSelectedItem().toString().equals("between")){
                    headerPanel.add(inputPanel2, "span 2");
                    between = true;
                } else {
                    headerPanel.remove(inputPanel2);
                    between = false;
                }
                revalidate();
                repaint();
            }
        });

        cboxAritmethics.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!cboxAritmethics.getSelectedItem().toString().equals("")){
                    inputPanel.add(cboxVariableValues2);
                } else {
                    inputPanel.remove(cboxVariableValues2);
                }
                revalidate();
                repaint();
            }
        });

        cboxAritmethics2.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!cboxAritmethics2.getSelectedItem().toString().equals("")){
                    inputPanel2.add(cboxVariableValues4);
                } else {
                    inputPanel2.remove(cboxVariableValues4);
                }
                revalidate();
                repaint();
            }
        });

        if(includeHeader) {
            headerPanel.add(useColumn);
            headerPanel.add(operators, "wrap");
            inputPanel.add(cboxVariableValues);
            inputPanel.add(cboxAritmethics);
            inputPanel2.add(cboxVariableValues3);
            inputPanel2.add(cboxAritmethics2);
            headerPanel.add(inputPanel, "span 2, wrap");
            headerPanel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
            headerPanel.setSize(300, 500);
            add(headerPanel, "width 100%, height 80, wrap, dock north");
            this.add(this.lblNazev, "grow, span 2, wrap, dock north");
        } else {
            this.add(this.lblNazev, "grow, span 2, wrap, gapy 70");
        }

        this.lblNazev.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        JLabel lblHodnota;
        for (String hodnota : data) {
            lblHodnota = new JLabel(hodnota);
            lblHodnota.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
            lblHodnota.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(lblHodnota, "grow, span 4, wrap, dock south");
        }

        revalidate();
        repaint();
    }

    public boolean validateInput(){
        String value = getDetectedValue(cboxVariableValues);
        String value2 = null;
        String value3 = null;
        String value4 = null;
        if(!cboxAritmethics.getSelectedItem().toString().equals("")) {
            value2 = getDetectedValue(cboxVariableValues2);
        }
        if(between) {
            value3 = getDetectedValue(cboxVariableValues3);
            if(!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                value4 = getDetectedValue(cboxVariableValues4);
            }
        }

        setFirstValid(true);
        setSecondValid(true);
        setThirdValid(true);
        setFourthValid(true);

        setCboxVariableValuesOk(cboxVariableValues);
        setCboxVariableValuesOk(cboxVariableValues2);
        setCboxVariableValuesOk(cboxVariableValues3);
        setCboxVariableValuesOk(cboxVariableValues4);

        try { Float.parseFloat(value);
        } catch(NumberFormatException e) {
            try { Double.parseDouble(value);
            } catch(NumberFormatException e1) {
                try { Integer.parseInt(value);
                } catch(NumberFormatException e2) {
                    try { Long.parseLong(value);
                    } catch(NumberFormatException e3) {
                        System.out.println(e3);
                        setFirstValid(false);
                        setCboxVariableValuesWarning(cboxVariableValues);
                    }
                }
            }
        }
        if(value2 != null){
            try { Float.parseFloat(value2);
            } catch(NumberFormatException e) {
                try { Double.parseDouble(value2);
                } catch(NumberFormatException e1) {
                    try { Integer.parseInt(value2);
                    } catch(NumberFormatException e2) {
                        try { Long.parseLong(value2);
                        } catch(NumberFormatException e3) {
                            System.out.println(e3);
                            setSecondValid(false);
                            setCboxVariableValuesWarning(cboxVariableValues2);
                        }
                    }
                }
            }
        }
        if(value3 != null){
            try { Float.parseFloat(value3);
            } catch(NumberFormatException e) {
                try { Double.parseDouble(value3);
                } catch(NumberFormatException e1) {
                    try { Integer.parseInt(value3);
                    } catch(NumberFormatException e2) {
                        try { Long.parseLong(value3);
                        } catch(NumberFormatException e3) {
                            System.out.println(e3);
                            setThirdValid(false);
                            setCboxVariableValuesWarning(cboxVariableValues3);
                        }
                    }
                }
            }
        }
        if(value4 != null){
            try { Float.parseFloat(value4);
            } catch(NumberFormatException e) {
                try { Double.parseDouble(value4);
                } catch(NumberFormatException e1) {
                    try { Integer.parseInt(value4);
                    } catch(NumberFormatException e2) {
                        try { Long.parseLong(value4);
                        } catch(NumberFormatException e3) {
                            System.out.println(e3);
                            setFourthValid(false);
                            setCboxVariableValuesWarning(cboxVariableValues4);
                        }
                    }
                }
            }
        }
        return isFirstValid() && isSecondValid() && isThirdValid() && isFourthValid();
    }
    
    public List<Boolean> detectValues(){
        Double userInput = Double.parseDouble(getDetectedValue(cboxVariableValues));
        Double userInput2;
        Double userInput3;
        Double userInput4;

        Double resultValue = userInput;
        Double resultValue2 = null;
        if(!cboxAritmethics.getSelectedItem().toString().equals("")) {
            userInput2 = Double.parseDouble(getDetectedValue(cboxVariableValues2));
            switch (cboxAritmethics.getSelectedItem().toString()){
                case "+":
                    resultValue += userInput2;
                    break;
                case "-":
                    resultValue -= userInput2;
                    break;
            }
        }
        if(between) {
            userInput3 = Double.parseDouble(getDetectedValue(cboxVariableValues3));
            resultValue2 = userInput3;
            if(!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                userInput4 = Double.parseDouble(getDetectedValue(cboxVariableValues4));
                switch (cboxAritmethics2.getSelectedItem().toString()){
                    case "+":
                        resultValue2 += userInput4;
                        break;
                    case "-":
                        resultValue2 -= userInput4;
                        break;
                }
            }
        }
        String operator = getOperator();
        List<Boolean> tempDetect = new ArrayList<>();
        switch (operator) {
            case "=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp.compareTo(resultValue) == 0) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "<":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp < resultValue) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case ">":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp > resultValue) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case ">=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp >= resultValue) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "<=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp <= resultValue) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "!=":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    if (temp.compareTo(resultValue) != 0) {
                        tempDetect.add(true);
                    } else {
                        tempDetect.add(false);
                    }
                }
                break;
            case "between":
                for (String data : getData()) {
                    Double temp = Double.parseDouble(data);
                    Double change = resultValue;
                    if(change > resultValue2){
                        resultValue = resultValue2;
                        resultValue2 = change;
                    }
                    if (temp > resultValue && temp < resultValue2) {
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

    public String getDetectedValue(JComboBox comboBox){
        String value;
        if(comboBox.getEditor().getItem() instanceof ComboBoxItem){
            value = ((ComboBoxItem) comboBox.getSelectedItem()).getValue();
        } else {
            try {
                value = comboBox.getEditor().getItem().toString();
            } catch (NullPointerException e) {
                value = "";
            }
        }
        return value;
    }

    public void setCboxVariableValuesOk(JComboBox comboBox){
        ((CustomComboBoxEditor) comboBox.getEditor()).changeBackground(null);
    }

    public void setCboxVariableValuesWarning(JComboBox comboBox){
        ((CustomComboBoxEditor) comboBox.getEditor()).changeBackground(Color.PINK);
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

    public boolean isFirstValid() {
        return firstValid;
    }

    public void setFirstValid(boolean firstValid) {
        this.firstValid = firstValid;
    }

    public boolean isSecondValid() {
        return secondValid;
    }

    public void setSecondValid(boolean secondValid) {
        this.secondValid = secondValid;
    }

    public boolean isThirdValid() {
        return thirdValid;
    }

    public void setThirdValid(boolean thirdValid) {
        this.thirdValid = thirdValid;
    }

    public boolean isFourthValid() {
        return fourthValid;
    }

    public void setFourthValid(boolean fourthValid) {
        this.fourthValid = fourthValid;
    }

    public boolean isBetween(){
        return between;
    }

    public void setBetween(boolean between){
        this.between = between;
    }
}
