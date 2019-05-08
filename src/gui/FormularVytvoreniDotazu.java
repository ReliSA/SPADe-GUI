package gui;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import ostatni.Atribut;
import ostatni.ComboBoxItem;
import ostatni.Konstanty;
import ostatni.Sloupec;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

class FormularVytvoreniDotazu extends JDialog
{
    private static final long serialVersionUID = -8229943813762614201L;
    private JButton btnSubmit = new JButton("OK");
    private JButton btnClose = new JButton("CANCEL");
    private JButton btnAdd = new JButton("Add");
    private boolean closed = true;
    private List<Atribut> attributeList = new ArrayList<>();
    private List<AttributePanel> attributePanels = new ArrayList<>();
    private JPanel mainPanel = new JPanel();
    private int heightDifference = 30;
    private int attributeCount = 1;
    private int windowHeight = 270;
    private int windowWidth = 600;

    private ButtonGroup btnGroupAggregate = new ButtonGroup();
    private JComboBox<String> cboxTables = new JComboBox<>();
    private JComboBox<String> cboxColumns = new JComboBox<>();
    private JComboBox<String> cboxJoinColumn = new JComboBox<>();
    private JTextField tfAttValue = new JTextField("Value");

    private FormularVytvoreniDotazu parentForm;

    public FormularVytvoreniDotazu(Map<String, List<Sloupec>> strukturaPohledu, JSONObject constraint, List<ComboBoxItem> variableValues){
        this.setModal(true);
        this.setLocation(400,300);
        // TODO - cancel on close - don't know how
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        String tableName = "";
        JLabel lblTable = new JLabel("Table");
        JLabel lblAgrColumn = new JLabel("Agr Col");
        JLabel lblJoinColumn = new JLabel("Join Col");
        JLabel lblCondition = new JLabel("Condition");
        JLabel lblAggregate = new JLabel("Aggregate");
        JTextField tfAttribute = new JTextField();
        JRadioButton radioSum = new JRadioButton("SUM");
        JRadioButton radioAvg = new JRadioButton("AVG");
        JRadioButton radioMin = new JRadioButton("MIN");
        JRadioButton radioMax = new JRadioButton("MAX");
        JRadioButton radioCount = new JRadioButton("COUNT");
        JLabel lblDateHint = new JLabel("*Only DD-MM-YYYY format for dates is supported");
        JLabel lblWarningText = new JLabel("*Value in red text are in wrong format.");

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
        this.setTitle("Attributes");

        for(Map.Entry<String, List<Sloupec>> entry : strukturaPohledu.entrySet()) {
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
        btnAdd.setPreferredSize(new Dimension(60,28));

        btnSubmit.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(validateForm(attributePanels)){
                        closed = false;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(parentForm, "You have 1 or more wrong values.", "Warning", JOptionPane.WARNING_MESSAGE);
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
                     mainPanel.remove(btnClose);
                     mainPanel.remove(btnSubmit);
                     mainPanel.remove(btnAdd);

                     AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()), variableValues, parentForm, false);
                     mainPanel.add(attributePanel, "gapleft 65, span 2, wrap");
                     attributePanels.add(attributePanel);
                     attributeList.add(attributePanel.getAtribut());
                     mainPanel.add(btnAdd);
                     mainPanel.add(btnSubmit, "split 2");
                     mainPanel.add(btnClose);
                     scroll(scrollPane, ScrollDirection.DOWN);
                     mainPanel.revalidate();
                     attributeCount++;
                 }
             }
        );

        cboxTables.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                Component[] components = mainPanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof AttributePanel){
                        mainPanel.remove(comp);
                    } else if (comp instanceof JLabel){
                        if(((JLabel) comp).getText().equals("")){
                            remove(comp);
                        }
                    }
                }
                mainPanel.remove(btnClose);
                mainPanel.remove(btnSubmit);
                mainPanel.remove(btnAdd);
                setSize(windowWidth,windowHeight);

                cboxColumns.removeAllItems();
                cboxJoinColumn.removeAllItems();
                // join pres Person
                cboxJoinColumn.addItem("personName");
                for(Sloupec s : strukturaPohledu.get(cboxTables.getSelectedItem())){
                    cboxColumns.addItem(s.getName());
                    if(s.getType().equals("datetime") || s.getType().equals("date")){
                        cboxJoinColumn.addItem(s.getName());
                    }
                }

                attributeList.clear();
                attributePanels.clear();
                AttributePanel attributePanel = new AttributePanel(null, strukturaPohledu.get(cboxTables.getSelectedItem()), variableValues, parentForm, true);
                mainPanel.add(attributePanel, "wrap");
                attributePanels.add(attributePanel);
                attributeList.add(attributePanel.getAtribut());
                mainPanel.add(btnAdd);
                mainPanel.add(btnSubmit, "split 2");
                mainPanel.add(btnClose);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        cboxJoinColumn.addItem("personName");
        for (Sloupec s : strukturaPohledu.get(cboxTables.getSelectedItem())){
            cboxColumns.addItem(s.getName());
            if(s.getType().equals("datetime") || s.getType().equals("date")){
                cboxJoinColumn.addItem(s.getName());
            }
        }

        mainPanel.add(lblDateHint, "wrap, span 3");
        mainPanel.add(lblWarningText, "wrap, span 3");
        mainPanel.add(lblTable, "w 50");
        mainPanel.add(cboxTables, "w 150, wrap");
        mainPanel.add(lblJoinColumn, "w 50");
        mainPanel.add(cboxJoinColumn, "w 150, wrap");
        mainPanel.add(lblAgrColumn, "w 50");
        mainPanel.add(cboxColumns, "w 150, wrap");
        mainPanel.add(lblAggregate, "w 50");
        mainPanel.add(radioSum, "split 5");
        mainPanel.add(radioCount);
        mainPanel.add(radioMax);
        mainPanel.add(radioMin);
        mainPanel.add(radioAvg, "wrap");
        mainPanel.add(lblCondition, "w 50");

        if( constraint != null) {
            for (Enumeration<AbstractButton> buttons = btnGroupAggregate.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.getText().equals(constraint.getString("aggregate"))) {
                    button.setSelected(true);
                }
            }

            cboxJoinColumn.setSelectedItem(constraint.getString("joinColumn"));
            cboxColumns.setSelectedItem(constraint.getString("agrColumn"));

            JSONArray atts = (JSONArray) constraint.get("attributes");
            for (Object attribute : atts) {
                if(getHeight() <= 400) {
                    setSize(getWidth(), getHeight() + heightDifference);
                }

                JSONObject jsonObject = (JSONObject) attribute;
                AttributePanel attributePanel;
                if(isFirst){
                    attributePanel = new AttributePanel(jsonObject.getString("name"), jsonObject.getString("operator"), jsonObject.getString("value"), jsonObject.getString("type"), strukturaPohledu.get((tableName)), variableValues, parentForm, true);
                    mainPanel.add(attributePanel, "w 400, wrap");
                    isFirst = false;
                } else {
                    attributePanel = new AttributePanel(jsonObject.getString("name"), jsonObject.getString("operator"), jsonObject.getString("value"), jsonObject.getString("type"), strukturaPohledu.get((tableName)), variableValues, parentForm, false);
                    mainPanel.add(attributePanel, "gapleft 65, span 2, wrap");
                }
                attributePanels.add(attributePanel);
                attributeList.add(attributePanel.getAtribut());
            }
        } else {
            AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()), variableValues, parentForm, true);
            mainPanel.add(attributePanel, "w 400, wrap");
            attributePanels.add(attributePanel);
            attributeList.add(attributePanel.getAtribut());
        }

        mainPanel.add(btnAdd);
        mainPanel.add(btnSubmit, "split 2");
        mainPanel.add(btnClose);
        mainPanel.setVisible(true);
        this.setVisible(true);
    }

    public boolean validateForm(List<AttributePanel> attributePanels){
        boolean validForm = true;
        for(AttributePanel attributePanel : attributePanels){
            boolean valid = attributePanel.getAtribut().validate();
            if(valid){
                if(attributePanel.useVariables()){
                    attributePanel.setCboxValuesOk();
                } else {
                    attributePanel.setFieldOk();
                }
            } else {
                if(attributePanel.useVariables()){
                    attributePanel.setCboxValuesWarning();
                } else {
                    attributePanel.setFieldWarning();
                }
            }
        }
        for(AttributePanel attributePanel : attributePanels){
            if(!attributePanel.getAtribut().isValid()){
                validForm = false;
                break;
            }
        }
        return validForm;
    }

    public boolean wasClosed(){
        return this.closed;
    }

    public JSONObject getFormData()
    {
        JSONObject jsonConstraint = new JSONObject();
        if(!closed) {
            jsonConstraint.put("table", cboxTables.getSelectedItem());
            jsonConstraint.put("joinColumn", cboxJoinColumn.getSelectedItem());
            jsonConstraint.put("agrColumn", cboxColumns.getSelectedItem());
            for (Enumeration<AbstractButton> buttons = btnGroupAggregate.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    jsonConstraint.put("aggregate", button.getText());
                }
            }

            JSONArray attributes = new JSONArray();

            for (Atribut att : attributeList) {
                JSONObject attribute = new JSONObject();
                attribute.put("name", att.getName());
                attribute.put("operator", att.getOperator());
                attribute.put("value", att.getValue());
                attribute.put("type", att.getType());
                attributes.put(attribute);
            }

            jsonConstraint.put("attributes", attributes);
        }
        return jsonConstraint;
    }

    public void resizeComponent(Atribut attribute){
        if(attributeCount < 8) {
            setSize(getWidth(), getHeight() - heightDifference);
        }
        attributeCount--;
        attributeList.remove(attribute);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void scroll(JScrollPane scrollPane, ScrollDirection direction) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        int topOrBottom = direction == ScrollDirection.UP ?
                verticalBar.getMinimum() :
                verticalBar.getMaximum();

        AdjustmentListener scroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(topOrBottom);
                // We have to remove the listener, otherwise the user would be unable to scroll afterwards
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(scroller);
    }

    public enum ScrollDirection {
        UP, DOWN
    }

    private class AttributePanel extends JPanel{
        Atribut atribut;
        AttributePanel thisPanel;
        JComboBox<String> cboxAttributes = new JComboBox<>();
        JComboBox<String> cboxOperators = new JComboBox<>();
        JComboBox<ComboBoxItem> cboxVariableValues = new JComboBox();
        JTextField tfValue = new JTextField();
        JCheckBox checkBoxUseVariable = new JCheckBox();
        FormularVytvoreniDotazu parentForm;
        boolean useVariables = false;

        public AttributePanel(List<Sloupec> sloupce, List<ComboBoxItem> variableValues, FormularVytvoreniDotazu parentForm, boolean isFirst) {
            this(null, sloupce, variableValues, parentForm, isFirst);
        }

        public AttributePanel(String name, String operator, String value, String type, List<Sloupec> sloupce, List<ComboBoxItem> variableValues, FormularVytvoreniDotazu parentForm, boolean isFirst) {
            this(new Atribut(name, operator, value, type), sloupce, variableValues, parentForm, isFirst);
        }

        public AttributePanel(Atribut newAtribut, List<Sloupec> sloupce, List<ComboBoxItem> variableValues, FormularVytvoreniDotazu parentForm, boolean isFirst){
            super();
            thisPanel = this;
            this.parentForm = parentForm;
            this.setLayout(new MigLayout("insets 0"));

            for(ComboBoxItem varValue : variableValues){
                cboxVariableValues.addItem(varValue);
            }
            for(Sloupec sloupec : sloupce){
                cboxAttributes.addItem(sloupec.getName());
            }

            if(newAtribut == null) {
                Sloupec sl = sloupce.iterator().next();
                atribut = new Atribut();
                atribut.setName(sl.getName());
                List<String> operators = getOperatorForColumnType(sl.getType());
                for(String operator : operators){
                    cboxOperators.addItem(operator);
                }
                atribut.setOperator(getOperator());
                atribut.setValue("");
            } else {
                atribut = newAtribut;
                List<String> operators = getOperatorForAtributType(atribut.getType());
                for(String operator : operators){
                    cboxOperators.addItem(operator);
                }
                cboxAttributes.setSelectedItem(atribut.getName());
                cboxOperators.setSelectedItem(atribut.getOperator());
                tfValue.setText(atribut.getValue());
            }

            JButton removeBtn = new JButton();
            try {
                Image img = ImageIO.read(new File("zdroje/obrazky/deleteImage.png"));
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
                System.out.println(ex);
            }
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentForm.resizeComponent(atribut);
                    thisPanel.getParent().remove(thisPanel);
                }
            });

            cboxAttributes.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    atribut.setName((String) cboxAttributes.getSelectedItem());
                    cboxOperators.removeAllItems();
                    Sloupec sloupec = sloupce.stream()
                            .filter(sl -> atribut.getName().equals(sl.getName()))
                            .findAny()
                            .orElse(null);
                    for(String operator : getOperatorForColumnType(sloupec.getType())){
                        cboxOperators.addItem(operator);
                    }
                    // TODO - přidat atributu type
                }
            });

            cboxOperators.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    atribut.setOperator((String) cboxOperators.getSelectedItem());
                }
            });
            checkBoxUseVariable.setToolTipText("Use variables");
            checkBoxUseVariable.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if(checkBoxUseVariable.isSelected()){
                        thisPanel.remove(tfValue);
                        thisPanel.remove(checkBoxUseVariable);
                        thisPanel.remove(removeBtn);
                        thisPanel.add(cboxVariableValues, "w 150");
                        if(!variableValues.isEmpty()) {
                            cboxVariableValues.setSelectedIndex(0);
                        }
                        thisPanel.add(checkBoxUseVariable);
                        if(!isFirst) {
                            thisPanel.add(removeBtn);
                        }
                        thisPanel.setUseVariables(true);
                        revalidate();
                        repaint();
                    } else {
                        thisPanel.remove(cboxVariableValues);
                        thisPanel.remove(checkBoxUseVariable);
                        thisPanel.add(tfValue, "w 150");
                        tfValue.setText("");
                        thisPanel.add(checkBoxUseVariable);
                        if(!isFirst) {
                            thisPanel.add(removeBtn);
                        }
                        thisPanel.setUseVariables(false);
                        revalidate();
                        repaint();
                    }
                }
            });

            tfValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    if(checkBoxUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }
                public void removeUpdate(DocumentEvent e) {
                    if(checkBoxUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }
                public void insertUpdate(DocumentEvent e) {
                    if(checkBoxUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }
                //kdyby bylo potřeba použít
//                public void warn() {
//                    if (Integer.parseInt(tfValue.getText())<=0){
//                        JOptionPane.showMessageDialog(null,
//                                "Error: Please enter number bigger than 0", "Error Massage",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
            });

            cboxVariableValues.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                }
            });

            this.add(cboxAttributes, "w 150");
            this.add(cboxOperators, "w 50");
            this.add(tfValue, "w 150");
            this.add(checkBoxUseVariable);

            if(!isFirst) {
                this.add(removeBtn);
            }
            this.setVisible(true);
        }

        private void setCboxValuesOk(){
            cboxVariableValues.setForeground(Color.BLACK);
        }

        private void setCboxValuesWarning(){
            cboxVariableValues.setForeground(Color.RED);
        }

        private void setFieldOk(){
            tfValue.setForeground(Color.BLACK);
        }

        private void setFieldWarning(){
            tfValue.setForeground(Color.RED);
        }

        private List<String> getOperatorForAtributType(String attType){
            List<String> operators = new ArrayList<>();
            switch(attType){
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
                    break;
                case "boolean":
                    operators.add("=");
                    break;
            }
            return operators;
        }

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
                    atribut.setType("number");
                    break;
                case "varchar":
                case "longtext":
                    operators.add("like");
                    atribut.setType("text");
                    break;
                case "bit":
                    operators.add("=");
                    atribut.setType("boolean");
                    break;
                case "date":
                case "datetime":
                    operators.add("<");
                    operators.add(">");
                    operators.add("=");
                    operators.add("<=");
                    operators.add(">=");
                    atribut.setType("date");
                    break;
            }
            return operators;
        }

        public String getAttributeName() {
            return (String) cboxAttributes.getSelectedItem();
        }

        public String getOperator() {
            return (String) cboxOperators.getSelectedItem();
        }

        public String getValue() {
            return tfValue.getText();
        }

        public Atribut getAtribut() {
            return atribut;
        }

        public boolean useVariables() {
            return useVariables;
        }

        public void setUseVariables(boolean useVariables) {
            this.useVariables = useVariables;
        }

        public String getJoinColumn(){
            return (String) cboxJoinColumn.getSelectedItem();
        }
    }
}