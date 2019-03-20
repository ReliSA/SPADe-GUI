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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class FormularVytvoreniOmezeni extends JDialog
{
    private static final long serialVersionUID = -8229943813762614201L;
    private JButton btnSubmit = new JButton("OK");
    private JButton btnClose = new JButton("CANCEL");
    private JButton btnAdd = new JButton("Add");
    private JTextField tfAttribute = new JTextField();
    private boolean closed = true;
    private List<Atribut> attributeList = new ArrayList<>();

    private JLabel lblType = new JLabel("Type");
    private JLabel lblAttribute = new JLabel("Attribute");
    private JComboBox cboxTables = new JComboBox();
    private JTextField tfAttValue = new JTextField("Value");

    public FormularVytvoreniOmezeni(Map<String, List<Sloupec>> strukturaPohledu, JSONObject constraint, List<ComboBoxItem> variableValues){
        setModal(true);
        setLocation(400,300);
        // TODO - cancel on close - don't know how
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int temp = 0;
        String tableName = "";

        setSize(600,150);
        setLocationRelativeTo(null);
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
                                            closed = false;
                                            dispose();
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

                                         setSize(getWidth(),getHeight() + 43);
                                         remove(btnClose);
                                         remove(btnSubmit);
                                         remove(btnAdd);

                                         add(new JLabel());
                                         AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()), variableValues);
                                         add(attributePanel, "width 100%, wrap");
                                         attributeList.add(attributePanel.getAtribut());
                                         add(btnAdd);
                                         add(btnSubmit, "width 15%");
                                         add(btnClose, "width 15%");
                                     }
                                 }
        );

        cboxTables.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                Component[] components = getContentPane().getComponents();
                for(Component comp : components){
                    if(comp instanceof AttributePanel){
                        remove(comp);
                    } else if (comp instanceof JLabel){
                        if(((JLabel) comp).getText().equals("")){
                            remove(comp);
                        }
                    }
                }
                remove(btnClose);
                remove(btnSubmit);
                remove(btnAdd);
                setSize(600,165);

                attributeList.clear();
                strukturaPohledu.get(cboxTables.getSelectedItem());
                AttributePanel attributePanel = new AttributePanel(null, strukturaPohledu.get(cboxTables.getSelectedItem()), variableValues);
                add(attributePanel, "width 100%, wrap");
                attributeList.add(attributePanel.getAtribut());
                System.out.println();
                add(btnAdd);
//                add(new JLabel());
                add(btnSubmit, "width 15%");
                add(btnClose, "width 15%");
                revalidate();
                repaint();
            }
        });

        this.setLayout(new MigLayout());
        this.add(lblType);
        this.add(cboxTables, "width 40%, wrap");
        this.add(lblAttribute);
        if( constraint != null) {
            JSONArray atts = (JSONArray) constraint.get("attributes");
            for (Object attribute : atts) {
                setSize(getWidth(), getHeight() + 40);

                // odsazení v layoutu
                if (temp != 0) {
                        add(new JLabel());
                }
                temp++;

                JSONObject jsonObject = (JSONObject) attribute;
                AttributePanel attributePanel = new AttributePanel(jsonObject.getString("name"), jsonObject.getString("operator"), jsonObject.getString("value"), strukturaPohledu.get((tableName)), variableValues);
                add(attributePanel, "width 100%, wrap");
                attributeList.add(attributePanel.getAtribut());
            }
        } else {
            AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()), variableValues);
            this.add(attributePanel, "width 100%, wrap");
            attributeList.add(attributePanel.getAtribut());
        }

        this.add(btnAdd);
        //this.add(new JLabel());
        this.add(btnSubmit, "width 15%");
        this.add(btnClose, "width 15%");
        this.setVisible(true);
    }

    public boolean wasClosed(){
        return this.closed;
    }

    public JSONObject getFormData()
    {
        JSONObject jsonConstraint = new JSONObject();
        if(!closed) {
            jsonConstraint.put("table", (String) cboxTables.getSelectedItem());
            JSONArray attributes = new JSONArray();

            for (Atribut att : attributeList) {
                JSONObject attribute = new JSONObject();
                attribute.put("name", att.getName());
                attribute.put("operator", att.getOperator());
                attribute.put("value", att.getValue());
                attributes.put(attribute);
            }

            jsonConstraint.put("attributes", attributes);
        }
        return jsonConstraint;
    }

    private class AttributePanel extends JPanel{
        Atribut atribut;
        AttributePanel thisPanel;
        JComboBox<String> cboxAttributes = new JComboBox<>();
        JComboBox<String> cboxOperators = new JComboBox<>();
        JComboBox<ComboBoxItem> cboxVariableValues = new JComboBox();
        JTextField tfValue = new JTextField();
        JCheckBox checkBocUseVariable = new JCheckBox();

        public AttributePanel(List<Sloupec> sloupce, List<ComboBoxItem> variableValues) {
            this(null, sloupce, variableValues);
        }

        public AttributePanel(String name, String operator, String value, List<Sloupec> sloupce, List<ComboBoxItem> variableValues) {
            this(new Atribut(name, operator, value), sloupce, variableValues);
        }

        public AttributePanel(Atribut newAtribut, List<Sloupec> sloupce, List<ComboBoxItem> variableValues){
            super();
            thisPanel = this;
            this.setLayout(new MigLayout());


            List<String> firstOperators = getOperatorForType(sloupce.iterator().next().getType());
            for(Sloupec sloupec : sloupce){
                cboxAttributes.addItem(sloupec.getName());
            }
            for(String operator : firstOperators){
                cboxOperators.addItem(operator);
            }
            for(ComboBoxItem varValue : variableValues){
                cboxVariableValues.addItem(varValue);
            }

            if(newAtribut == null) {
                atribut = new Atribut(getAttributeName(), getOperator(), getValue());
            } else {
                atribut = newAtribut;
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
                    Container container = thisPanel.getParent();
                    thisPanel.getParent().remove(thisPanel);
                    container.setSize(container.getWidth(), container.getHeight() - 40);
                    container.revalidate();
                    container.repaint();
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
                    for(String operator : getOperatorForType(sloupec.getType())){
                        cboxOperators.addItem(operator);
                    }
                }
            });

            cboxOperators.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    atribut.setOperator((String) cboxOperators.getSelectedItem());
                }
            });

            checkBocUseVariable.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if(checkBocUseVariable.isSelected()){
                        thisPanel.remove(tfValue);
                        thisPanel.remove(checkBocUseVariable);
                        thisPanel.add(cboxVariableValues, "width 20%");
                        if(!variableValues.isEmpty()) {
                            cboxVariableValues.setSelectedIndex(0);
                        }
                        thisPanel.add(checkBocUseVariable);
                        revalidate();
                        repaint();
                    } else {
                        thisPanel.remove(cboxVariableValues);
                        thisPanel.remove(checkBocUseVariable);
                        thisPanel.add(tfValue, "width 20%");
                        tfValue.setText("");
                        thisPanel.add(checkBocUseVariable);
                        revalidate();
                        repaint();
                    }
                }
            });

            tfValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    if(checkBocUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }
                public void removeUpdate(DocumentEvent e) {
                    if(checkBocUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }
                public void insertUpdate(DocumentEvent e) {
                    if(checkBocUseVariable.isSelected()) {
                        atribut.setValue(((ComboBoxItem) cboxVariableValues.getSelectedItem()).getValue());
                    } else {
                        atribut.setValue(tfValue.getText());
                    }
                }

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

            this.add(cboxAttributes,"width 40%");
            this.add(cboxOperators,"width 15%");
            this.add(tfValue,"width 20%");
            this.add(checkBocUseVariable);
            // doesnt work properly
//            this.add(removeBtn);
            this.setVisible(true);
        }

        private List<String> getOperatorForType(String columnType){
            List<String> operators = new ArrayList<>();
            switch(columnType){
                case "bigint":
                case "int":
                case "double":
                    operators.add("<");
                    operators.add(">");
                    operators.add("=");
                    break;
                case "varchar":
                    operators.add("like");
                    break;
                case "longtext":
                    operators.add("like");
                    break;
                case "bit":
                    operators.add("=");
                    break;
                case "date":
                    operators.add("dunno");
                    break;
                case "datetime":
                    operators.add("dunno");
                    break;
            }
            return operators;
        }

        public String getAttributeName(){
            return (String) cboxAttributes.getSelectedItem();
        }

        public String getOperator(){
            return (String) cboxOperators.getSelectedItem();
        }

        public String getValue(){
            return tfValue.getText();
        }

        public Atribut getAtribut(){
            return atribut;
        }
    }
}