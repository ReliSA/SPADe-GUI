package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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
    private JCheckBox checkBoxUseColumn;
    private JCheckBox checkBoxCompareColumns;
    private JComboBox<String> operators;
    private JComboBox<ComboBoxItem> cboxVariableValues;
    private JComboBox<ComboBoxItem> cboxVariableValues2;
    private JComboBox<ComboBoxItem> cboxVariableValues3;
    private JComboBox<ComboBoxItem> cboxVariableValues4;
    private JComboBox<String> cboxColumns;
    private JComboBox<String> cboxColumns2;
    private JComboBox<String> cboxAritmethics;
    private JComboBox<String> cboxAritmethics2;
    private List<Boolean> detectedValues = new ArrayList<>();
    private JPanel headerPanel;
    private JPanel inputPanel;
    private JPanel inputPanel2;
    private static JFrame mainFrame;
    private boolean between;
    static Logger log = Logger.getLogger(SloupecCustomGrafu.class);

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

                    JSONObject detection = new JSONObject();
//                    detection.put("operator", "between");
//                    detection.put("detect", "true");
//                    detection.put("compareColumns", "false");
//                    JSONObject firstInput = new JSONObject();
//                    firstInput.put("value1", "1");
//                    firstInput.put("arithmetic", "+");
//                    firstInput.put("value2", "2");
//                    detection.put("firstInput", firstInput);
//                    JSONObject secondInput = new JSONObject();
//                    secondInput.put("value1", "3");
//                    secondInput.put("arithmetic", "-");
//                    secondInput.put("value2", "4");
//                    detection.put("secondInput", secondInput);

                    detection.put("operator", "between");
                    detection.put("detect", "true");
                    detection.put("compareColumns", "true");
                    detection.put("column1", "col1");
                    detection.put("column2", "col2");

                    JSONObject object = new JSONObject();
                    object.put("detection", detection);

                    SloupecCustomGrafu graf = new SloupecCustomGrafu("ColumnName", new ArrayList<>(Arrays.asList("1","2","3")), 0,
                            new ArrayList<>(Arrays.asList(new ComboBoxItem("jmeno", "text", "name"), new ComboBoxItem("cislo","number","5"))),
                            true, new ArrayList<>(Arrays.asList("col1","col2","col3")), object);

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
    public SloupecCustomGrafu(String nazev, List<String> data, int index, List<ComboBoxItem> variableValues, boolean includeHeader, List<String> columnNames, JSONObject query) {
        this.data = data;
        this.index = index;
        setBackground(Color.white);
        setLayout(new MigLayout("ins 0"));

        checkBoxUseColumn = new JCheckBox("Detect");
        checkBoxCompareColumns = new JCheckBox("Com. col.");
        operators = new JComboBox<>();
        cboxVariableValues = new JComboBox<ComboBoxItem>();
        cboxVariableValues2 = new JComboBox<ComboBoxItem>();
        cboxVariableValues3 = new JComboBox<ComboBoxItem>();
        cboxVariableValues4 = new JComboBox<ComboBoxItem>();
        cboxColumns = new JComboBox<>();
        cboxColumns2 = new JComboBox<>();
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

        for(String columnName : columnNames){
            cboxColumns.addItem(columnName);
            cboxColumns2.addItem(columnName);
        }

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
        cboxAritmethics.addItem("*");
        cboxAritmethics.addItem("/");
        cboxAritmethics2.addItem("");
        cboxAritmethics2.addItem("+");
        cboxAritmethics2.addItem("-");
        cboxAritmethics2.addItem("*");
        cboxAritmethics2.addItem("/");

        operators.addItem("=");
        operators.addItem("<=");
        operators.addItem(">=");
        operators.addItem("<");
        operators.addItem(">");
        operators.addItem("!=");
        operators.addItem("between");
        operators.addItem("not between");

        between = false;

        operators.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(operators.getSelectedItem().toString().equals("between") || operators.getSelectedItem().toString().equals("not between")){
                    if(checkBoxCompareColumns.isSelected()){
                        headerPanel.add(cboxColumns2, "span 3, width 50%");
                    } else {
                        headerPanel.add(inputPanel2, "span 3");
                    }
                    between = true;
                } else {
                    if(checkBoxCompareColumns.isSelected()){
                        headerPanel.remove(cboxColumns2);
                    } else {
                        headerPanel.remove(inputPanel2);
                    }
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

        checkBoxCompareColumns.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkBoxCompareColumns.isSelected()){
                    headerPanel.remove(inputPanel);
                    headerPanel.remove(inputPanel2);
                    headerPanel.add(cboxColumns, "span 3, wrap, width 50%");
                    if(operators.getSelectedItem().toString().equals("between") || operators.getSelectedItem().toString().equals("not between")){
                        headerPanel.add(cboxColumns2, "span 3, width 50%");
                    }
                } else {
                    headerPanel.remove(cboxColumns);
                    headerPanel.remove(cboxColumns2);
                    headerPanel.add(inputPanel, "span 3, wrap");
                    if(operators.getSelectedItem().toString().equals("between") || operators.getSelectedItem().toString().equals("not between")){
                        headerPanel.add(inputPanel2, "span 3");
                    }
                }
                revalidate();
                repaint();
            }
        });

        if(includeHeader) {
            headerPanel.add(operators);
            headerPanel.add(checkBoxUseColumn);
            headerPanel.add(checkBoxCompareColumns, "wrap");
            inputPanel.add(cboxVariableValues);
            inputPanel.add(cboxAritmethics);
            inputPanel2.add(cboxVariableValues3);
            inputPanel2.add(cboxAritmethics2);
            headerPanel.add(inputPanel, "span 3, wrap");
            headerPanel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
            headerPanel.setSize(300, 500);
            add(headerPanel, "width 100%, height 80, wrap, dock north");
            this.add(this.lblNazev, "grow, span 3, wrap, dock north");
        } else {
            this.add(this.lblNazev, "grow, span 3, wrap, gapy 70");
        }

        if(query != null){
            if(query.has("detection")) {
                JSONObject detection = query.getJSONObject("detection");
                String operator = detection.getString("operator");
                operators.setSelectedItem(operator);
                boolean detect = detection.getBoolean("detect");
                if (detect) {
                    checkBoxUseColumn.setSelected(true);
                } else {
                    checkBoxUseColumn.setSelected(false);
                }
                boolean compareColumns = detection.getBoolean("compareColumns");
                if (!compareColumns) {
                    checkBoxCompareColumns.setSelected(false);
                    JSONObject firstInput = detection.getJSONObject("firstInput");
                    String value1 = firstInput.getString("value1");
                    cboxVariableValues.setSelectedItem(value1);
                    if (firstInput.has("arithmetic")) {
                        String arithmetic = firstInput.getString("arithmetic");
                        cboxAritmethics.setSelectedItem(arithmetic);
                        String value2 = firstInput.getString("value2");
                        cboxVariableValues2.setSelectedItem(value2);
                    }
                    if (operator.equals("between") || operator.equals("not between")) {
                        JSONObject secondInput = detection.getJSONObject("secondInput");
                        String value3 = secondInput.getString("value1");
                        cboxVariableValues3.setSelectedItem(value3);
                        if (secondInput.has("arithmetic")) {
                            String arithmetic = secondInput.getString("arithmetic");
                            cboxAritmethics2.setSelectedItem(arithmetic);
                            String value4 = secondInput.getString("value2");
                            cboxVariableValues4.setSelectedItem(value4);
                        }
                    }
                } else {
                    checkBoxCompareColumns.doClick();
                    String column1 = detection.getString("column1");
                    cboxColumns.setSelectedItem(column1);
                    if (operator.equals("between") || operator.equals("not between")) {
                        String column2 = detection.getString("column2");
                        cboxColumns2.setSelectedItem(column2);
                    }
                }
            }
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
        this.firstValid = true;
        this.secondValid = true;
        this.thirdValid = true;
        this.fourthValid = true;
        if(!compareColumns()) {
            String value = getDetectedValue(cboxVariableValues);
            String value2 = null;
            String value3 = null;
            String value4 = null;
            if (!cboxAritmethics.getSelectedItem().toString().equals("")) {
                value2 = getDetectedValue(cboxVariableValues2);
            }
            if (between) {
                value3 = getDetectedValue(cboxVariableValues3);
                if (!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                    value4 = getDetectedValue(cboxVariableValues4);
                }
            }

            setCboxVariableValuesOk(cboxVariableValues);
            setCboxVariableValuesOk(cboxVariableValues2);
            setCboxVariableValuesOk(cboxVariableValues3);
            setCboxVariableValuesOk(cboxVariableValues4);

            try {
                Float.parseFloat(value);
            } catch (NumberFormatException e) {
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e1) {
                    try {
                        Integer.parseInt(value);
                    } catch (NumberFormatException e2) {
                        try {
                            Long.parseLong(value);
                        } catch (NumberFormatException e3) {
                            log.warn(e3);
                            setFirstValid(false);
                            setCboxVariableValuesWarning(cboxVariableValues);
                        }
                    }
                }
            }
            if (value2 != null) {
                try {
                    Float.parseFloat(value2);
                } catch (NumberFormatException e) {
                    try {
                        Double.parseDouble(value2);
                    } catch (NumberFormatException e1) {
                        try {
                            Integer.parseInt(value2);
                        } catch (NumberFormatException e2) {
                            try {
                                Long.parseLong(value2);
                            } catch (NumberFormatException e3) {
                                log.warn(e3);
                                setSecondValid(false);
                                setCboxVariableValuesWarning(cboxVariableValues2);
                            }
                        }
                    }
                }
            }
            if (value3 != null) {
                try {
                    Float.parseFloat(value3);
                } catch (NumberFormatException e) {
                    try {
                        Double.parseDouble(value3);
                    } catch (NumberFormatException e1) {
                        try {
                            Integer.parseInt(value3);
                        } catch (NumberFormatException e2) {
                            try {
                                Long.parseLong(value3);
                            } catch (NumberFormatException e3) {
                                log.warn(e3);
                                setThirdValid(false);
                                setCboxVariableValuesWarning(cboxVariableValues3);
                            }
                        }
                    }
                }
            }
            if (value4 != null) {
                try {
                    Float.parseFloat(value4);
                } catch (NumberFormatException e) {
                    try {
                        Double.parseDouble(value4);
                    } catch (NumberFormatException e1) {
                        try {
                            Integer.parseInt(value4);
                        } catch (NumberFormatException e2) {
                            try {
                                Long.parseLong(value4);
                            } catch (NumberFormatException e3) {
                                log.warn(e3);
                                setFourthValid(false);
                                setCboxVariableValuesWarning(cboxVariableValues4);
                            }
                        }
                    }
                }
            }
        }
        return isFirstValid() && isSecondValid() && isThirdValid() && isFourthValid();
    }
    
    public List<Boolean> detectValues(List<SloupecCustomGrafu> sloupce){
        List<Boolean> tempDetect = new ArrayList<>();
        String operator = getOperator();
        if(compareColumns()){
            SloupecCustomGrafu sloupec1 = null;
            SloupecCustomGrafu sloupec2 = null;
            List<String> columnData = getData();
            for(SloupecCustomGrafu sloupec : sloupce){
                if(sloupec.getName().equals(cboxColumns.getSelectedItem().toString())){
                    sloupec1 = sloupec;
                } else if (sloupec.getName().equals(cboxColumns2.getSelectedItem().toString())){
                    sloupec2 = sloupec;
                }
            }
            switch (operator) {
                case "=":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp.compareTo(temp2) == 0) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case "<":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp < temp2) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case ">":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp > temp2) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case ">=":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp >= temp2) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case "<=":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp <= temp2) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case "!=":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        if (temp.compareTo(temp2) != 0) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case "between":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        Double temp3 = Double.parseDouble(sloupec2.getData().get(i));
                        Double change = temp2;
                        if (change > temp3) {
                            temp2 = temp3;
                            temp3 = change;
                        }
                        if (temp > temp2 && temp < temp3) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
                case "not between":
                    for (int i = 0; i < columnData.size(); i++) {
                        Double temp = Double.parseDouble(columnData.get(i));
                        Double temp2 = Double.parseDouble(sloupec1.getData().get(i));
                        Double temp3 = Double.parseDouble(sloupec2.getData().get(i));
                        Double change = temp2;
                        if (change > temp3) {
                            temp2 = temp3;
                            temp3 = change;
                        }
                        if (temp < temp2 || temp > temp3) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
            }
        } else {
            Double userInput = Double.parseDouble(getDetectedValue(cboxVariableValues));
            Double userInput2;
            Double userInput3;
            Double userInput4;

            Double resultValue = userInput;
            Double resultValue2 = null;
            if (!cboxAritmethics.getSelectedItem().toString().equals("")) {
                userInput2 = Double.parseDouble(getDetectedValue(cboxVariableValues2));
                switch (cboxAritmethics.getSelectedItem().toString()) {
                    case "+":
                        resultValue += userInput2;
                        break;
                    case "-":
                        resultValue -= userInput2;
                        break;
                }
            }
            if (between) {
                userInput3 = Double.parseDouble(getDetectedValue(cboxVariableValues3));
                resultValue2 = userInput3;
                if (!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                    userInput4 = Double.parseDouble(getDetectedValue(cboxVariableValues4));
                    switch (cboxAritmethics2.getSelectedItem().toString()) {
                        case "+":
                            resultValue2 += userInput4;
                            break;
                        case "-":
                            resultValue2 -= userInput4;
                            break;
                    }
                }
            }
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
                        if (change > resultValue2) {
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
                case "not between":
                    for (String data : getData()) {
                        Double temp = Double.parseDouble(data);
                        Double change = resultValue;
                        if (change > resultValue2) {
                            resultValue = resultValue2;
                            resultValue2 = change;
                        }
                        if (temp < resultValue || temp > resultValue2) {
                            tempDetect.add(true);
                        } else {
                            tempDetect.add(false);
                        }
                    }
                    break;
            }
        }
        this.detectedValues = tempDetect;
        return tempDetect;
    }

    public String getComboBoxValue(JComboBox comboBox) {
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

    public String getArithmeticOperator(JComboBox comboBox) {
        return comboBox.getSelectedItem().toString();
    }

    public boolean useColum(){
        return this.checkBoxUseColumn.isSelected();
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

    public boolean compareColumns(){
        return checkBoxCompareColumns.isSelected();
    }

    public JComboBox<ComboBoxItem> getCboxVariableValues() {
        return cboxVariableValues;
    }

    public JComboBox<ComboBoxItem> getCboxVariableValues2() {
        return cboxVariableValues2;
    }

    public JComboBox<ComboBoxItem> getCboxVariableValues3() {
        return cboxVariableValues3;
    }

    public JComboBox<ComboBoxItem> getCboxVariableValues4() {
        return cboxVariableValues4;
    }

    public JComboBox<String> getCboxColumns() {
        return cboxColumns;
    }

    public JComboBox<String> getCboxColumns2() {
        return cboxColumns2;
    }

    public JComboBox<String> getCboxAritmethics() {
        return cboxAritmethics;
    }

    public JComboBox<String> getCboxAritmethics2() {
        return cboxAritmethics2;
    }
}
