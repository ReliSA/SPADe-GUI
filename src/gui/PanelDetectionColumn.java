package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ostatni.ComboBoxItem;
import ostatni.CustomComboBoxEditor;
import ostatni.Konstanty;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Panel zobrazující data v tabulce výsledků
 *
 * @author Patrik Bezděk
 */
public class PanelDetectionColumn extends JPanel {
    private JLabel lblName = new JLabel();
    private int index;
    private boolean firstValid;
    private boolean secondValid;
    private boolean thirdValid;
    private boolean fourthValid;
    private List<String> data;
    private JCheckBox checkBoxUseColumn;
    private JCheckBox checkBoxInvertValues;
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
    private JRadioButton radioBtnCompareConst;
    private JRadioButton radioBtnCompareColumn;
    private ButtonGroup btnGroupCompare;
    private JRadioButton radioBtnAnd;
    private JRadioButton radioBtnOr;
    private ButtonGroup btnGroupCompareLogic;
    private JPanel headerPanel;
    private JPanel inputPanel;
    private JPanel inputPanel2;
    private JLabel lblCompare;
    private JLabel lblLogic;
    private JPanel radioButtonComparePanel;
    private JPanel radioButtonLogicPanel;
    private JButton detectBtn;
    private static JFrame mainFrame;
    private boolean between;
    private WindowCreatePatternDetection okno;
    static Logger log = Logger.getLogger(PanelDetectionColumn.class);

    /**
     * Konstruktor panelu
     * @param columnName název sloupce
     * @param data seznam s daty
     * @param index pořadí sloupce
     * @param variableValues hodnoty vytvořených konstant a proměnných
     * @param includeHeader true pokud chceme v panelu záhlaví s ovládacími prvky
     * @param columnNames názvy všech sloupců
     * @param query SQL dotaz zapsaný v JSON
     * @param isDetectionColumn true pokud se jedná o sloupce s hodnotami detekce
     */
    public PanelDetectionColumn(String columnName, List<String> data, int index, List<ComboBoxItem> variableValues, boolean includeHeader, List<String> columnNames, JSONObject query, boolean isDetectionColumn, WindowCreatePatternDetection window) {
        this.data = data;
        this.index = index;
        this.okno = window;
        setBackground(Color.white);
        setLayout(new MigLayout("ins 0, gap rel 0"));

        checkBoxUseColumn = new JCheckBox(Konstanty.POPISY.getProperty("pouzit"));
        checkBoxInvertValues = new JCheckBox(Konstanty.POPISY.getProperty("invertovatHodnoty"));
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
        lblCompare = new JLabel(Konstanty.POPISY.getProperty("porovnat"));
        lblLogic = new JLabel(Konstanty.POPISY.getProperty("operator"));
        radioBtnCompareConst = new JRadioButton(Konstanty.POPISY.getProperty("konstprom"));
        radioBtnCompareColumn = new JRadioButton(Konstanty.POPISY.getProperty("sloupec"));
        btnGroupCompare = new ButtonGroup();
        btnGroupCompare.add(radioBtnCompareConst);
        btnGroupCompare.add(radioBtnCompareColumn);
        radioBtnCompareConst.setSelected(true);
        radioBtnAnd = new JRadioButton("AND");
        radioBtnOr = new JRadioButton("OR");
        btnGroupCompareLogic = new ButtonGroup();
        btnGroupCompareLogic.add(radioBtnAnd);
        btnGroupCompareLogic.add(radioBtnOr);
        radioBtnAnd.setSelected(true);
        detectBtn = new JButton(Konstanty.POPISY.getProperty("spocitat"));

        headerPanel = new JPanel();
        headerPanel.setLayout(new MigLayout("ins 0"));

        inputPanel = new JPanel(new MigLayout("ins 0"));
        inputPanel2 = new JPanel(new MigLayout("ins 0"));

        radioButtonComparePanel = new JPanel(new MigLayout("ins 0"));
        radioButtonComparePanel.add(lblCompare);
        radioButtonComparePanel.add(radioBtnCompareConst);
        radioButtonComparePanel.add(radioBtnCompareColumn);
        radioButtonLogicPanel = new JPanel(new MigLayout("ins 0"));
        radioButtonLogicPanel.add(radioBtnAnd);
        radioButtonLogicPanel.add(radioBtnOr);

        this.lblName.setText(columnName);
        Font font = new Font("Courier", Font.BOLD, 12);
        this.lblName.setFont(font);
        this.lblName.setHorizontalAlignment(SwingConstants.CENTER);

        for(String name : columnNames){
            cboxColumns.addItem(name);
            cboxColumns2.addItem(name);
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
                    inputPanel2.removeAll();
                    cboxAritmethics2.setSelectedIndex(0);
                    if(radioBtnCompareColumn.isSelected()){
                        inputPanel2.add(cboxColumns2);
                        inputPanel2.add(cboxAritmethics2);
                    } else {
                        cboxVariableValues3.setSelectedItem("");
                        cboxVariableValues4.setSelectedItem("");
                        inputPanel2.add(cboxVariableValues3);
                        inputPanel2.add(cboxAritmethics2);
                    }
                    headerPanel.add(inputPanel2, "span 3");
                    between = true;
                } else {
                    headerPanel.remove(inputPanel2);
                    between = false;
                }
                revalidate();
                repaint();
            }
        });

        detectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.detectValues(detectWithAnd());
            }
        });

        radioBtnCompareConst.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(radioBtnCompareConst.isSelected()) {
                    headerPanel.remove(inputPanel2);
                    inputPanel.removeAll();
                    inputPanel2.removeAll();
                    inputPanel.add(cboxVariableValues);
                    inputPanel.add(cboxAritmethics);
                    cboxAritmethics.setSelectedIndex(0);
                    cboxVariableValues.setSelectedItem("");
                    cboxVariableValues2.setSelectedItem("");
                    if(operators.getSelectedItem().toString().equals("between") || operators.getSelectedItem().toString().equals("not between")){
                        inputPanel2.add(cboxVariableValues3);
                        inputPanel2.add(cboxAritmethics2);
                        cboxAritmethics2.setSelectedIndex(0);
                        cboxVariableValues3.setSelectedItem("");
                        cboxVariableValues4.setSelectedItem("");
                        headerPanel.add(inputPanel2);
                    }
                }
                revalidate();
                repaint();
            }
        });

        radioBtnCompareColumn.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(radioBtnCompareColumn.isSelected()) {
                    headerPanel.remove(inputPanel2);
                    inputPanel.removeAll();
                    inputPanel2.removeAll();
                    inputPanel.add(cboxColumns);
                    inputPanel.add(cboxAritmethics);
                    cboxAritmethics.setSelectedIndex(0);
                    cboxVariableValues2.setSelectedItem("");
                    if(operators.getSelectedItem().toString().equals("between") || operators.getSelectedItem().toString().equals("not between")){
                        inputPanel2.add(cboxColumns2);
                        inputPanel2.add(cboxAritmethics2);
                        cboxAritmethics2.setSelectedIndex(0);
                        cboxVariableValues4.setSelectedItem("");
                        headerPanel.add(inputPanel2);
                    }
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

        checkBoxInvertValues.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.setDoInvertValues(checkBoxInvertValues.isSelected());

                Collections.replaceAll(data, "0","2");
                Collections.replaceAll(data, "1","0");
                Collections.replaceAll(data, "2","1");
                removeAll();

                add(lblLogic, "wrap, align 50% 50%, gapy 47");
                add(radioButtonLogicPanel, "span 3, wrap");
                add(detectBtn, "span 3, wrap, align 50% 50%");
                add(checkBoxInvertValues, "grow, span 3, wrap");
                add(lblName, "grow, span 3, wrap");
                JLabel lblValue;
                for (String value : data) {
                    lblValue = new JLabel(value);
                    lblValue.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
                    lblValue.setHorizontalAlignment(SwingConstants.CENTER);
                    add(lblValue, "grow, span 4, wrap");
                }
                revalidate();
                repaint();
            }
        });

        if(includeHeader) {
            headerPanel.add(checkBoxUseColumn, "wrap");
            headerPanel.add(radioButtonComparePanel, "span 3, wrap");
            headerPanel.add(operators, "wrap");
            inputPanel.add(cboxVariableValues);
            inputPanel.add(cboxAritmethics);
            inputPanel2.add(cboxVariableValues3);
            inputPanel2.add(cboxAritmethics2);
            headerPanel.add(inputPanel, "span 3, wrap");
            headerPanel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
            add(headerPanel, "width 100%, height 130, wrap");
            this.add(this.lblName, "grow, span 3, wrap");
        } else if (isDetectionColumn) {
            this.add(lblLogic, "wrap, align 50% 50%, gapy 47");
            radioButtonLogicPanel.add(radioBtnAnd);
            radioButtonLogicPanel.add(radioBtnOr);
            this.add(radioButtonLogicPanel, "span 3, wrap");
            this.add(detectBtn, "span 3, wrap, align 50% 50%");
            this.add(checkBoxInvertValues, "grow, span 3, wrap");
            this.add(this.lblName, "grow, span 3, wrap");
        } else {
            this.add(this.lblName, "grow, span 3, wrap, gapy 130");
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
                    radioBtnCompareConst.doClick();
                } else {
                    radioBtnCompareColumn.doClick();
                }

                JSONObject firstInput = detection.getJSONObject("firstInput");
                String value1 = firstInput.getString("value1");
                if(!compareColumns) {
                    cboxVariableValues.setSelectedItem(value1);
                } else {
                    cboxColumns.setSelectedItem(value1);
                }
                if (firstInput.has("arithmetic")) {
                    String arithmetic = firstInput.getString("arithmetic");
                    cboxAritmethics.setSelectedItem(arithmetic);
                    String value2 = firstInput.getString("value2");
                    cboxVariableValues2.setSelectedItem(value2);
                }
                if (operator.equals("between") || operator.equals("not between")) {
                    JSONObject secondInput = detection.getJSONObject("secondInput");
                    String value3 = secondInput.getString("value1");
                    if(!compareColumns) {
                        cboxVariableValues3.setSelectedItem(value3);
                    } else {
                        cboxColumns2.setSelectedItem(value3);
                    }
                    if (secondInput.has("arithmetic")) {
                        String arithmetic = secondInput.getString("arithmetic");
                        cboxAritmethics2.setSelectedItem(arithmetic);
                        String value4 = secondInput.getString("value2");
                        cboxVariableValues4.setSelectedItem(value4);
                    }
                }
            }
        }

        this.lblName.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        JLabel lblValue;
        for (String value : data) {
            lblValue = new JLabel(value);
            lblValue.setBorder(new MatteBorder(1,0,1,0, Color.BLACK));
            lblValue.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(lblValue, "grow, span 4, wrap");
        }

        revalidate();
        repaint();
    }

    /**
     * Zvaliduje uživatelské vstupy
     * @return true pokud jsou zadané vstupy validní
     */
    public boolean validateInput(){
        this.firstValid = true;
        this.secondValid = true;
        this.thirdValid = true;
        this.fourthValid = true;
        String value = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;

        if(!compareColumns()) {
            value = getComboBoxValue(cboxVariableValues);
        }
        if (!cboxAritmethics.getSelectedItem().toString().equals("")) {
            value2 = getComboBoxValue(cboxVariableValues2);
        }
        if (between) {
            if(!compareColumns()) {
                value3 = getComboBoxValue(cboxVariableValues3);
            }
            if (!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                value4 = getComboBoxValue(cboxVariableValues4);
            }
        }

        setCboxVariableValuesOk(cboxVariableValues);
        setCboxVariableValuesOk(cboxVariableValues2);
        setCboxVariableValuesOk(cboxVariableValues3);
        setCboxVariableValuesOk(cboxVariableValues4);

        if(value != null) {
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

        return isFirstValid() && isSecondValid() && isThirdValid() && isFourthValid();
    }

    /**
     * Provede detekci podle zadaných kritérií
     * @param columns seznam sloupců pro detekce
     * @return seznam výsledků validací
     */
    public List<Boolean> detectValues(List<PanelDetectionColumn> columns){
        List<Boolean> tempDetect = new ArrayList<>();
        String operator = getOperator();
        if(compareColumns()){
            PanelDetectionColumn column1 = null;
            PanelDetectionColumn column2 = null;
            List<String> columnData = getData();
            for(PanelDetectionColumn column : columns){
                if(column.getName().equals(cboxColumns.getSelectedItem().toString())){
                    column1 = column;
                } else if (column.getName().equals(cboxColumns2.getSelectedItem().toString())){
                    column2 = column;
                }
            }

            Double userInput2 = 0.0;
            Double userInput4 = 0.0;
            String arit1 = cboxAritmethics.getSelectedItem().toString();
            String arit2 = cboxAritmethics2.getSelectedItem().toString();

            if (!arit1.equals("")) {
                userInput2 = Double.parseDouble(getComboBoxValue(cboxVariableValues2));
            }
            if (between) {
                if (!arit2.equals("")) {
                    userInput4 = Double.parseDouble(getComboBoxValue(cboxVariableValues4));
                }
            }

            switch (operator) {
            case "=":
                for (int i = 0; i < columnData.size(); i++) {
                    Double temp = Double.parseDouble(columnData.get(i));
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
                    Double temp3 = arithmeticCount(Double.parseDouble(column2.getData().get(i)), arit1, userInput4);
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
                    Double temp2 = arithmeticCount(Double.parseDouble(column1.getData().get(i)), arit1, userInput2);
                    Double temp3 = arithmeticCount(Double.parseDouble(column2.getData().get(i)), arit1, userInput4);
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
            Double userInput = Double.parseDouble(getComboBoxValue(cboxVariableValues));
            Double userInput2;
            Double userInput3;
            Double userInput4;

            Double resultValue = userInput;
            Double resultValue2 = null;
            if (!cboxAritmethics.getSelectedItem().toString().equals("")) {
                userInput2 = Double.parseDouble(getComboBoxValue(cboxVariableValues2));
                switch (cboxAritmethics.getSelectedItem().toString()) {
                    case "+":
                        resultValue += userInput2;
                        break;
                    case "-":
                        resultValue -= userInput2;
                        break;
                    case "*":
                        resultValue *= userInput2;
                        break;
                    case "/":
                        resultValue /= userInput2;
                        break;
                }
            }
            if (between) {
                userInput3 = Double.parseDouble(getComboBoxValue(cboxVariableValues3));
                resultValue2 = userInput3;
                if (!cboxAritmethics2.getSelectedItem().toString().equals("")) {
                    userInput4 = Double.parseDouble(getComboBoxValue(cboxVariableValues4));
                    switch (cboxAritmethics2.getSelectedItem().toString()) {
                        case "+":
                            resultValue2 += userInput4;
                            break;
                        case "-":
                            resultValue2 -= userInput4;
                            break;
                        case "*":
                            resultValue2 *= userInput4;
                            break;
                        case "/":
                            resultValue2 /= userInput4;
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

    /**
     * Vrací výsledek početní operace mezi zadanými čísly a vstupním operátorem
     * @param number vstupní číslo
     * @param operator operátor
     * @param number2 druhé vstupní číslo
     * @return výsledek početní operace
     */
    public Double arithmeticCount(Double number, String operator, Double number2){
        Double result = number;
        switch (operator) {
            case "+":
                result += number2;
                break;
            case "-":
                result -= number2;
                break;
            case "*":
                result *= number2;
                break;
            case "/":
                result /= number2;
                break;
            case "":
                break;
        }
        return result;
    }

    /**
     * Vrací hodnotu z ComboBoxu
     * @param comboBox objekt ComboBoxu
     * @return hodnota v ComboBoxu
     */
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

    /**
     * Vrací rozhodnutí o tom, jestli je chceme použít sloupec při detekci
     * @return true pokud se má sloupec použít při detekci
     */
    public boolean useColum(){
        return this.checkBoxUseColumn.isSelected();
    }

    /**
     * Obarví pozadí ComboBoxu na neutrální barvu
     * @param comboBox ComboBox pro obarvení
     */
    public void setCboxVariableValuesOk(JComboBox comboBox){
        ((CustomComboBoxEditor) comboBox.getEditor()).changeBackground(null);
    }

    /**
     * Obarví pozadí ComboBoxu na řůžovou barvu
     * @param comboBox ComboBox pro obarvení
     */
    public void setCboxVariableValuesWarning(JComboBox comboBox){
        ((CustomComboBoxEditor) comboBox.getEditor()).changeBackground(Color.PINK);
    }

    /**
     * Vrací rozhodnutí, jestli se mají detekce počítat přes AND
     * @return true pokud se mají detekce počítat přes AND, false pokud přes OR
     */
    public boolean detectWithAnd(){
       return radioBtnAnd.isSelected();
    }

    /**
     * Nastaví přepínač operátoru na AND nebo OR
     * @param detectWithAnd true pokud se mají detekce počítat s AND, false pokus s OR
     */
    public void setDetectWithAnd(boolean detectWithAnd){
        if(detectWithAnd){
            radioBtnAnd.setSelected(true);
        } else {
            radioBtnOr.setSelected(true);
        }
    }

    /**
     * Vrací aritmetický operátor pro detekci
     * @return operátor
     */
    public String getOperator(){
        return (String) this.operators.getSelectedItem();
    }

    /**
     * Vrací data sloupce
     * @return seznam dat
     */
    public List<String> getData(){
        return this.data;
    }

    /**
     * Vrací název sloupce
     * @return název sloupce
     */
    public String getName(){
        return lblName.getText();
    }

    /**
     * Vrací rozhodnutí jestli se má provádět porovnání sloupců při detekce
     * @return true pokud se má provést porovnání sloupců při detekc
     */
    public boolean compareColumns(){
        return radioBtnCompareColumn.isSelected();
    }

    /**
     * Vrací rozhodnutí jestli se má provádět intervalová detekce
     * @return true pokud se má provést intervalová detekc
     */
    public boolean isBetween(){
        return between;
    }

    /**
     * Vrací rozhodnutí jestli se mají invertovat detekční hodnoty
     * @return true pokud se má provést inverze detekčních hodnot
     */
    public boolean doInvertValues(){
        return checkBoxInvertValues.isSelected();
    }

    /**
     * Vykoná se kliknutí na check box pro inverzi hodnot
     */
    public void invertValues(){
        checkBoxInvertValues.doClick();
    }

    /**
     * Vrací ukazatel validity prvního vstupu
     * @return true pokud je první vstup validní
     */
    private boolean isFirstValid() {
        return firstValid;
    }

    /**
     * Nastaví prvnímu vstupu jestli je validní nebo ne
     * @param firstValid ukazatel validity
     */
    private void setFirstValid(boolean firstValid) {
        this.firstValid = firstValid;
    }

    /**
     * Vrací ukazatel validity druhého vstupu
     * @return true pokud je druhý vstup validní
     */
    private boolean isSecondValid() {
        return secondValid;
    }

    /**
     * Nastaví druhému vstupu jestli je validní nebo ne
     * @param secondValid ukazatel validity
     */
    private void setSecondValid(boolean secondValid) {
        this.secondValid = secondValid;
    }

    /**
     * Vrací ukazatel validity třetího vstupu
     * @return true pokud je třetí vstup validní
     */
    private boolean isThirdValid() {
        return thirdValid;
    }

    /**
     * Nastaví třetímu vstupu jestli je validní nebo ne
     * @param thirdValid ukazatel validity
     */
    private void setThirdValid(boolean thirdValid) {
        this.thirdValid = thirdValid;
    }

    /**
     * Vrací ukazatel validity čtvrtého vstupu
     * @return true pokud je čtvrtý vstup validní
     */
    private boolean isFourthValid() {
        return fourthValid;
    }

    /**
     * Nastaví čtvrtému vstupu jestli je validní nebo ne
     * @param fourthValid ukazatel validity
     */
    private void setFourthValid(boolean fourthValid) {
        this.fourthValid = fourthValid;
    }

    /**
     * Vrací komponentu pro zadávání prvního vstupu
     * @return komponenta prvního vstupu
     */
    public JComboBox<ComboBoxItem> getCboxVariableValues() {
        return cboxVariableValues;
    }

    /**
     * Vrací komponentu pro zadávání druhého vstupu
     * @return komponenta druhého vstupu
     */
    public JComboBox<ComboBoxItem> getCboxVariableValues2() {
        return cboxVariableValues2;
    }

    /**
     * Vrací komponentu pro zadávání třetího vstupu
     * @return komponenta třetího vstupu
     */
    public JComboBox<ComboBoxItem> getCboxVariableValues3() {
        return cboxVariableValues3;
    }

    /**
     * Vrací komponentu pro zadávání čtvrtého vstupu
     * @return komponenta čtvrtého vstupu
     */
    public JComboBox<ComboBoxItem> getCboxVariableValues4() {
        return cboxVariableValues4;
    }

    /**
     * Vrací název sloupce v první podmínce
     * @return název sloupce v první podmínce
     */
    public String getFirstColumn() {
        return (String) cboxColumns.getSelectedItem();
    }

    /**
     * Vrací název sloupce ve druhé podmínce
     * @return název sloupce ve druhé podmínce
     */
    public String getSecondColumn() {
        return (String) cboxColumns2.getSelectedItem();
    }

    /**
     * Vrací první operátor
     * @return operátor první podmínky
     */
    public String getFirstArithmetic() {
        return (String) cboxAritmethics.getSelectedItem();
    }

    /**
     * Vrací druhý operátor
     * @return operátor druhé podmínky
     */
    public String getSecondArithmetic() {
        return (String) cboxAritmethics2.getSelectedItem();
    }
}
