package gui;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import ostatni.Atribut;
import ostatni.Konstanty;
import ostatni.Sloupec;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public FormularVytvoreniOmezeni(Map<String, List<Sloupec>> strukturaPohledu, JSONObject constraint){
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
                                         AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()));
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
                AttributePanel attributePanel = new AttributePanel(null, strukturaPohledu.get(cboxTables.getSelectedItem()));
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

                if (temp != 0) {
        //                add(new JLabel());
                }
                temp++;

                JSONObject jsonObject = (JSONObject) attribute;
                AttributePanel attributePanel = new AttributePanel(jsonObject.getString("name"), jsonObject.getString("operator"), jsonObject.getString("value"), strukturaPohledu.get((tableName)));
                add(attributePanel, "width 100%, wrap");
                attributeList.add(attributePanel.getAtribut());
            }
            temp = 0;
        } else {
            AttributePanel attributePanel = new AttributePanel(strukturaPohledu.get((String) cboxTables.getSelectedItem()));
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

    public Map<String, List<Atribut>> getFormData()
    {
        Map<String, List<Atribut>> dataMap = new LinkedHashMap<>();

        if(!closed) {
            dataMap.put((String) cboxTables.getSelectedItem(), attributeList);
        }

        return dataMap;
    }

    private class AttributePanel extends JPanel{
        Atribut atribut;
        AttributePanel thisPanel;
        JComboBox<String> cboxAttributes = new JComboBox<>();
        JComboBox<String> cboxOperators = new JComboBox<>();
        JTextField tfValue = new JTextField();

        public AttributePanel(List<Sloupec> sloupce) {
            this(null, sloupce);
        }

        public AttributePanel(String name, String operator, String value, List<Sloupec> sloupce) {
            this(new Atribut(name, operator, value), sloupce);
        }

        public AttributePanel(Atribut newAtribut, List<Sloupec> sloupce){
            super();
            thisPanel = this;
            this.setLayout(new MigLayout());
            JComboBox variableValues = new JComboBox();

            List<String> firstOperators = getOperatorForType(sloupce.iterator().next().getType());
            for(Sloupec sloupec : sloupce){
                cboxAttributes.addItem(sloupec.getName());
            }
            for(String operator : firstOperators){
                cboxOperators.addItem(operator);
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
                    if(sloupec.getType().equals("bigint") || sloupec.getType().equals("int") || sloupec.getType().equals("double")){
                        add(variableValues);
                    } else {
                        // att panel ma max 4 componenty - pokud to neni cislo tak 4. vyhodit
                        if(getComponents().length > 3){
                            remove(getComponents()[3]);
                        }
                        revalidate();
                        repaint();
                    }
                }
            });

            cboxOperators.addActionListener (new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    atribut.setOperator((String) cboxOperators.getSelectedItem());
                }
            });

            tfValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    atribut.setValue(tfValue.getText());
                }
                public void removeUpdate(DocumentEvent e) {
                    atribut.setValue(tfValue.getText());
                }
                public void insertUpdate(DocumentEvent e) {
                    atribut.setValue(tfValue.getText());
                }

//                public void warn() {
//                    if (Integer.parseInt(tfValue.getText())<=0){
//                        JOptionPane.showMessageDialog(null,
//                                "Error: Please enter number bigger than 0", "Error Massage",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
//                }
            });

            this.add(cboxAttributes,"width 40%");
            this.add(cboxOperators,"width 15%");
            this.add(tfValue,"width 20%");
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