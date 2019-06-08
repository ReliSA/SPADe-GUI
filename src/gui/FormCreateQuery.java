package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ostatni.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Okno pro vytvoření dotazu
 *
 * @author Patrik Bezděk
 */
class FormCreateQuery extends JDialog
{
    private static final long serialVersionUID = -8229943813762614201L;
    private JButton btnSubmit = new JButton(Konstanty.POPISY.getProperty("tlacitkoOk"));
    private JButton btnClose = new JButton(Konstanty.POPISY.getProperty("tlacitkoZrusit"));
    private JButton btnAdd;
    private JPanel mainPanel = new JPanel();
    private JPanel bottomPanel = new JPanel(new MigLayout("ins 0"));
    private ButtonGroup btnGroupAggregate = new ButtonGroup();
    private JComboBox<String> cboxTables = new JComboBox<>();
    private JComboBox<String> cboxColumns = new JComboBox<>();
    private JComboBox<String> cboxJoinColumn = new JComboBox<>();
    private JTextField tfAttValue = new JTextField(Konstanty.POPISY.getProperty("popisHodnota"));
    private FormCreateQuery parentForm;
    /**
     * Seznam podmínek v dotazu
     */
    private List<ConditionPanel> conditionPanels = new ArrayList<>();
    /**
     * Ukazatel toho, jestli byl formulář zrušen
     */
    private boolean wasCancelled = true;
    private int heightDifference = 28;
    private int conditionCount = 1;
    private int windowHeight = 250;
    private int windowWidth = 700;

    static Logger log = Logger.getLogger(FormCreateQuery.class);

    /**
     * Konstruktor okna
     * @param viewStructure struktura sloupců pohledů
     * @param constraint podmínka zapsaná v JSON objektu
     * @param variableValues seznam vytvořených konstant/proměnných
     */
    public FormCreateQuery(Map<String, List<Column>> viewStructure, JSONObject constraint, List<ComboBoxItem> variableValues){
        this.setModal(true);
        this.setLocation(400,300);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(Konstanty.IMG_ICON.getImage());
        String tableName = "";
        JLabel lblTable = new JLabel(Konstanty.POPISY.getProperty("popisTabulka"));
        JLabel lblAgrColumn = new JLabel(Konstanty.POPISY.getProperty("sloupecAgr"));
        JLabel lblJoinColumn = new JLabel(Konstanty.POPISY.getProperty("sloupecJoin"));
        JLabel lblCondition = new JLabel(Konstanty.POPISY.getProperty("popisPodminka"));
        JLabel lblAggregate = new JLabel(Konstanty.POPISY.getProperty("agregace"));
        JTextField tfAttribute = new JTextField();
        JRadioButton radioSum = new JRadioButton("SUM");
        JRadioButton radioAvg = new JRadioButton("AVG");
        JRadioButton radioMin = new JRadioButton("MIN");
        JRadioButton radioMax = new JRadioButton("MAX");
        JRadioButton radioCount = new JRadioButton("COUNT");
        JLabel lblDateHint = new JLabel(Konstanty.POPISY.getProperty("textFormatDatumu"));
        JLabel lblWarningText = new JLabel(Konstanty.POPISY.getProperty("textSpatnyFormat"));
        Font f = lblDateHint.getFont();
        lblDateHint.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        lblWarningText.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        btnAdd = new JButton();
        try {
            Image img = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("/res/addImage.png"));
            if (img != null){
                Image newimg = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
                btnAdd.setIcon(new ImageIcon(newimg));
            } else {
                btnAdd.setText("A");
            }
            btnAdd.setMargin(new Insets(1,1,1,1));
            btnAdd.setBorderPainted(false);
            btnAdd.setContentAreaFilled(false);
            btnAdd.setFocusPainted(false);
            btnAdd.setOpaque(false);
        } catch (Exception ex) {
            log.error(ex);
        }

        parentForm = this;
        boolean isFirst = true;
        mainPanel.setLayout(new MigLayout());
        mainPanel.setAutoscrolls(true);

        btnGroupAggregate.add(radioAvg);
        btnGroupAggregate.add(radioMin);
        btnGroupAggregate.add(radioMax);
        btnGroupAggregate.add(radioCount);
        btnGroupAggregate.add(radioSum);

        radioSum.setSelected(true);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);

        this.setSize(windowWidth, windowHeight);
        this.setLocationRelativeTo(null);
        this.setTitle(Konstanty.POPISY.getProperty("textVytvoreniDotazu"));

        for(Map.Entry<String, List<Column>> entry : viewStructure.entrySet()) {
            cboxTables.addItem(entry.getKey());
        }

        if(constraint != null) {
            tableName = constraint.getString("table");
            cboxTables.setSelectedItem(tableName);
        }

        tfAttribute.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
        btnSubmit.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        btnClose.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
        tfAttValue.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);

        btnSubmit.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(validateForm(conditionPanels)){
                        wasCancelled = false;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(parentForm, Konstanty.POPISY.getProperty("textNevalidniHodnoty"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        );

        btnClose.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){
                   dispose();
               }
           }
        );

        btnAdd.addActionListener(new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     tfAttValue = new JTextField("Value");

                     if(getHeight() <= 400) {
                         setSize(getWidth(), getHeight() + heightDifference);
                     }
                     mainPanel.remove(bottomPanel);

                     ConditionPanel conditionPanel = new ConditionPanel(viewStructure.get((String) cboxTables.getSelectedItem()), variableValues, parentForm, false);
                     mainPanel.add(conditionPanel, "gapleft 80, span 3, wrap");
                     if(conditionPanels.size() == 1){
                        conditionPanels.get(0).addRemoveButton();
                     }
                     conditionPanels.add(conditionPanel);
                     mainPanel.add(bottomPanel, "span 3");
                     scroll(scrollPane, ScrollDirection.DOWN);
                     mainPanel.revalidate();
                     conditionCount++;
                 }
             }
        );

        cboxTables.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                Component[] components = mainPanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof ConditionPanel){
                        mainPanel.remove(comp);
                    } else if (comp instanceof JLabel){
                        if(((JLabel) comp).getText().equals("")){
                            remove(comp);
                        }
                    }
                }
                mainPanel.remove(bottomPanel);
                setSize(windowWidth,windowHeight);

                cboxColumns.removeAllItems();
                cboxJoinColumn.removeAllItems();
                for(Column col : viewStructure.get(cboxTables.getSelectedItem())){
                    cboxColumns.addItem(col.getName());
                    if(col.getName().equals("authorName") || col.getName().equals("relatedName") || col.getName().equals("assigneeName")){
                        cboxJoinColumn.addItem(col.getName());
                    }
                    if(col.getType().equals("datetime") || col.getType().equals("date")){
                        cboxJoinColumn.addItem(col.getName());
                    }
                }

                conditionPanels.clear();
                ConditionPanel conditionPanel = new ConditionPanel(null, viewStructure.get(cboxTables.getSelectedItem()), variableValues, parentForm, true);
                mainPanel.add(conditionPanel, "wrap");
                conditionPanels.add(conditionPanel);
                mainPanel.add(bottomPanel, "span 3");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        for (Column col : viewStructure.get(cboxTables.getSelectedItem())){
            cboxColumns.addItem(col.getName());
            if(col.getName().equals("authorName") || col.getName().equals("assigneeName")){
                cboxJoinColumn.addItem(col.getName());
            }
            if(col.getType().equals("datetime") || col.getType().equals("date")){
                cboxJoinColumn.addItem(col.getName());
            }
        }

        mainPanel.add(lblDateHint, "wrap, span 4");
        mainPanel.add(lblWarningText, "wrap, span 4");
        mainPanel.add(lblTable, "w 50, span 2");
        mainPanel.add(cboxTables, "w 150, wrap");
        mainPanel.add(lblJoinColumn, "w 50, span 2");
        mainPanel.add(cboxJoinColumn, "w 150, wrap");
        mainPanel.add(lblAgrColumn, "w 50, span 2");
        mainPanel.add(cboxColumns, "w 150, wrap");
        mainPanel.add(lblAggregate, "w 50, span 2");
        mainPanel.add(radioSum, "split 5");
        mainPanel.add(radioCount);
        mainPanel.add(radioMax);
        mainPanel.add(radioMin);
        mainPanel.add(radioAvg, "wrap");
        mainPanel.add(lblCondition, "w 50");
        mainPanel.add(btnAdd);

        if( constraint != null) {
            for (Enumeration<AbstractButton> buttons = btnGroupAggregate.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.getText().equals(constraint.getString("aggregate"))) {
                    button.setSelected(true);
                }
            }

            cboxJoinColumn.setSelectedItem(constraint.getString("joinColumn"));
            cboxColumns.setSelectedItem(constraint.getString("agrColumn"));

            JSONArray conditions = (JSONArray) constraint.get("conditions");
            for (Object obj : conditions) {
                JSONObject condition = (JSONObject) obj;
                ConditionPanel conditionPanel;
                if(isFirst){
                    conditionPanel = new ConditionPanel(condition.getString("name"), condition.getString("operator"), condition.getString("value"), condition.getString("type"), viewStructure.get((tableName)), variableValues, parentForm, true);
                    mainPanel.add(conditionPanel, "span 3, wrap");
                    isFirst = false;
                } else {
                    conditionPanel = new ConditionPanel(condition.getString("name"), condition.getString("operator"), condition.getString("value"), condition.getString("type"), viewStructure.get((tableName)), variableValues, parentForm, false);
                    mainPanel.add(conditionPanel, "gapleft 80, span 3, wrap");
                }
                conditionPanels.add(conditionPanel);
                if(getHeight() <= 400 && conditionPanels.size() != 1) {
                    setSize(getWidth(), getHeight() + heightDifference);
                }
            }
            if(conditionPanels.size() > 1){
                conditionPanels.get(0).addRemoveButton();
            }
        } else {
            ConditionPanel conditionPanel = new ConditionPanel(viewStructure.get((String) cboxTables.getSelectedItem()), variableValues, parentForm, true);
            mainPanel.add(conditionPanel, "span 3, wrap");
            conditionPanels.add(conditionPanel);
        }

        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnClose);
        mainPanel.add(bottomPanel,"span 4");
        mainPanel.setVisible(true);
        this.setVisible(true);
    }

    /**
     * Zkontroluje jestli souhlasí typy zadaných hodnot s typy slupců u všechn panelů podmínek
     * @param conditionPanels seznam panelů s podmínkami
     * @return true pokud souhlasí zadaná hodnota s typem sloupce
     */
    private boolean validateForm(List<ConditionPanel> conditionPanels){
        boolean validForm = true;
        for(ConditionPanel conditionPanel : conditionPanels){
            boolean valid = conditionPanel.getCondition().validate();
            if(valid){
                conditionPanel.setCboxValuesOk();
            } else {
                conditionPanel.setCboxValuesWarning();
                validForm = false;
            }
        }
        return validForm;
    }

    /**
     * Vrací informaci jestli byl formulář potvrzen nebo zrušen
     * @return true pokud byl formulář zrušen
     */
    public boolean wasClosed(){
        return this.wasCancelled;
    }

    /**
     * Vrací hodnoty vyplněné ve formuláři v podobě JSON
     * @return hodnoty vyplněné ve formuláři v JSON objektu
     */
    public JSONObject getFormData()
    {
        JSONObject jsonConstraint = new JSONObject();
        if(!wasCancelled) {
            jsonConstraint.put("table", cboxTables.getSelectedItem());
            jsonConstraint.put("joinColumn", cboxJoinColumn.getSelectedItem());
            jsonConstraint.put("agrColumn", cboxColumns.getSelectedItem());
            for (Enumeration<AbstractButton> buttons = btnGroupAggregate.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    jsonConstraint.put("aggregate", button.getText());
                }
            }

            JSONArray conditions = new JSONArray();

            for (ConditionPanel conditionPanel : conditionPanels) {
                Condition condition = conditionPanel.getCondition();
                JSONObject conditionObject = new JSONObject();
                conditionObject.put("name", condition.getName());
                conditionObject.put("operator", condition.getOperator());
                conditionObject.put("value", condition.getValue());
                conditionObject.put("type", condition.getType());
                conditions.put(conditionObject);
            }

            jsonConstraint.put("conditions", conditions);
        }
        return jsonConstraint;
    }

    /**
     * Odstraní podmínkový panel z formuláře a pokud je potřeba tak okno formuláře změnší
     * @param conditionPanel panel podmínky, který se odstraňuje
     */
    private void resizeComponent(ConditionPanel conditionPanel){
        if(conditionCount < 8) {
            setSize(getWidth(), getHeight() - heightDifference);
        }
        conditionCount--;
        conditionPanels.remove(conditionPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Překreslí ve formuláři podmínkové panely
     */
    private void repaintConditions(){
        boolean first = true;
        for(ConditionPanel conditionPanel : conditionPanels){
            mainPanel.remove(conditionPanel);
        }
        mainPanel.remove(bottomPanel);
        for(int i = 1; i < conditionPanels.size(); i++){
            if(first){
                mainPanel.add(conditionPanels.get(i), "span 3, wrap");
                first = false;
            } else {
                mainPanel.add(conditionPanels.get(i), "gapleft 80, span 3, wrap");
            }
        }
        mainPanel.add(bottomPanel, "span 3");
    }

    /**
     * Odscrolluje vstupní panel úplně nahoru nebo úplně dolů
     * @param scrollPane scrollovací panel
     * @param direction směr scrollování
     */
    private void scroll(JScrollPane scrollPane, ScrollDirection direction) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        int topOrBottom = direction == ScrollDirection.UP ?
                verticalBar.getMinimum() :
                verticalBar.getMaximum();

        AdjustmentListener scroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(topOrBottom);
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(scroller);
    }

    /**
     * Seznam se směry scrollování
     *
     * @author Patrik Bezděk
     */
    public enum ScrollDirection {
        UP, DOWN
    }

    /**
     * Třída představuje jednotlivé podmínky dotazu vytvořeného ve formuláři
     *
     * @author Patrik Bezděk
     */
    private class ConditionPanel extends JPanel{
        Condition condition;
        ConditionPanel thisPanel;
        JComboBox<String> cboxAttributes = new JComboBox<>();
        JComboBox<String> cboxOperators = new JComboBox<>();
        JComboBox<ComboBoxItem> cboxVariableValues = new JComboBox();
        FormCreateQuery parentForm;
        JButton removeBtn;

        /**
         * Přetížený konstuktor panelu. Používá se pro vytvoření prvního panelu podmínky při otevření formuláře nebo přepnutí pohledu ve formuláři
         * @param columns seznam sloupců pohledu
         * @param variableValues seznam vytvořených konstant/proměnných
         * @param parentForm odkaz na formulář, ve kterém se panely vytváří
         * @param isFirst ukazatel jestli je panel jako první podmínka
         */
        public ConditionPanel(List<Column> columns, List<ComboBoxItem> variableValues, FormCreateQuery parentForm, boolean isFirst) {
            this(null, columns, variableValues, parentForm, isFirst);
        }

        /**
         * Přetížený konstuktor panelu. Používá se pro vytvoření panelů podmínek při editaci dotazu, kde už podmínky existují.
         * @param name název sloupce v podmínce
         * @param operator operátor porování
         * @param value hodnota k porovnání
         * @param type typ sloupce
         * @param columns seznam sloupců pohledu
         * @param variableValues seznam vytvořených konstant/proměnných
         * @param parentForm odkaz na formulář, ve kterém se panely vytváří
         * @param isFirst ukazatel jestli je panel jako první podmínka
         */
        public ConditionPanel(String name, String operator, String value, String type, List<Column> columns, List<ComboBoxItem> variableValues, FormCreateQuery parentForm, boolean isFirst) {
            this(new Condition(name, operator, value, type), columns, variableValues, parentForm, isFirst);
        }

        /**
         * Konstruktor panelu. Slouží jako přetěžovaný konstruktor pro ostatní
         * @param newCondition nová podmínka
         * @param columns seznam sloupců pohledu
         * @param variableValues seznam vytvořených konstant/proměnných
         * @param parentForm odkaz na formulář, ve kterém se panely vytváří
         * @param isFirst ukazatel jestli je panel jako první podmínka
         */
        public ConditionPanel(Condition newCondition, List<Column> columns, List<ComboBoxItem> variableValues, FormCreateQuery parentForm, boolean isFirst){
            super();
            thisPanel = this;
            this.parentForm = parentForm;
            this.setLayout(new MigLayout("insets 0"));

            cboxVariableValues.setEditor(new CustomComboBoxEditor());
            cboxVariableValues.setEditable(true);

            for(ComboBoxItem varValue : variableValues){
                cboxVariableValues.addItem(varValue);
            }
            cboxVariableValues.setSelectedIndex(-1);

            for(Column column : columns){
                cboxAttributes.addItem(column.getName());
            }

            if(newCondition == null) {
                Column col = columns.iterator().next();
                condition = new Condition();
                condition.setName(col.getName());
                List<String> operators = getOperatorForColumnType(col.getType());
                for(String operator : operators){
                    cboxOperators.addItem(operator);
                }
                condition.setOperator(getOperator());
                condition.setValue("");
            } else {
                condition = newCondition;
                List<String> operators = getOperatorForConditionType(condition.getType());
                for(String operator : operators){
                    cboxOperators.addItem(operator);
                }
                cboxAttributes.setSelectedItem(condition.getName());
                cboxOperators.setSelectedItem(condition.getOperator());
//                tfValue.setText(condition.getValue());
                cboxVariableValues.getEditor().setItem(condition.getValue());
            }

            removeBtn = new JButton();
            try {
                Image img = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("/res/deleteImage.png"));
                if (img != null){
                    Image newimg = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
                    removeBtn.setIcon(new ImageIcon(newimg));
                } else {
                    removeBtn.setText("R");
                }
                removeBtn.setMargin(new Insets(1,1,1,1));
                removeBtn.setBorderPainted(false);
                removeBtn.setContentAreaFilled(false);
                removeBtn.setFocusPainted(false);
                removeBtn.setOpaque(false);
            } catch (Exception ex) {
                log.error(ex);
            }
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisPanel.getParent().remove(thisPanel);
                    if(thisPanel.equals(conditionPanels.get(0))){
                        parentForm.repaintConditions();
                    }
                    conditionPanels.remove(thisPanel);
                    if(conditionPanels.size() == 1){
                        conditionPanels.get(0).removeRemoveButton();
                    }
                    parentForm.resizeComponent(thisPanel);
                }
            });

            cboxAttributes.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    condition.setName((String) cboxAttributes.getSelectedItem());
                    cboxOperators.removeAllItems();
                    Column column = columns.stream()
                            .filter(sl -> condition.getName().equals(sl.getName()))
                            .findAny()
                            .orElse(null);
                    for(String operator : getOperatorForColumnType(column.getType())){
                        cboxOperators.addItem(operator);
                    }
                    // TODO - přidat conditionu type
                }
            });

            cboxOperators.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    condition.setOperator((String) cboxOperators.getSelectedItem());
                }
            });

            this.add(cboxAttributes, "w 100");
            this.add(cboxOperators, "w 70");
            this.add(cboxVariableValues, "w 250");

            if(!isFirst) {
                this.add(removeBtn);
            }
            this.setVisible(true);
        }

        /**
         * Nastaví základní barvu pozadí komponenty
         */
        private void setCboxValuesOk(){
            ((CustomComboBoxEditor) cboxVariableValues.getEditor()).changeBackground(null);
        }

        /**
         * Nastaví varovnou barvu pozadí komponenty
         */
        private void setCboxValuesWarning(){
            ((CustomComboBoxEditor) cboxVariableValues.getEditor()).changeBackground(Color.PINK);
        }

        /**
         * Přidá do panelu tlačítko na smazání panelu
         */
        private void addRemoveButton(){
            thisPanel.add(removeBtn);
        }

        /**
         * Odstraní z panelu tlačítko na smazání panelu
         */
        private void removeRemoveButton(){
            thisPanel.remove(removeBtn);
        }

        /**
         * Vrací seznam operátorů pro zadaný typ podmínky
         * @param conditionType typ podmínky
         * @return sezman operátorů pro zadaný typ podmínky
         */
        private List<String> getOperatorForConditionType(String conditionType){
            List<String> operators = new ArrayList<>();
            switch(conditionType){
                case "number":
                case "date":
                    operators.add("<");
                    operators.add(">");
                    operators.add("=");
                    operators.add("<=");
                    operators.add(">=");
                    break;
                case "text":
                    operators.add("like");
                    operators.add("not like");
                    break;
                case "boolean":
                    operators.add("=");
                    break;
            }
            return operators;
        }

        /**
         * Vrací seznam operátorů pro zadaný typ sloupce a nastaví typ podmínky
         * @param columnType typ sloupce
         * @return sezman operátorů pro daný typ sloupce
         */
        private List<String> getOperatorForColumnType(String columnType){
            List<String> operators = new ArrayList<>();
            switch(columnType){
                case "bigint":
                case "int":
                case "double":
                    operators.add("<");
                    operators.add(">");
                    operators.add("=");
                    operators.add("<=");
                    operators.add(">=");
                    condition.setType("number");
                    break;
                case "varchar":
                case "longtext":
                    operators.add("like");
                    operators.add("not like");
                    condition.setType("text");
                    break;
                case "bit":
                    operators.add("=");
                    condition.setType("boolean");
                    break;
                case "date":
                case "datetime":
                    operators.add("<");
                    operators.add(">");
                    operators.add("=");
                    operators.add("<=");
                    operators.add(">=");
                    condition.setType("date");
                    break;
            }
            return operators;
        }

        /**
         * Vrací operátor podmínky
         * @return operátor podmínky
         */
        public String getOperator() {
            return (String) cboxOperators.getSelectedItem();
        }

        /**
         * Vrací hodnotu podmínky
         * @return hodnota podmínky
         */
        public String getValue() {
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

        /**
         * Vrací objekt podmínky
         * @return objekt podmínky
         */
        public Condition getCondition() {
            condition.setValue(getValue());
            return condition;
        }
    }
}