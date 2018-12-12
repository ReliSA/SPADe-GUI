package gui;

import net.miginfocom.swing.MigLayout;
import ostatni.Konstanty;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OknoMigLayout {

    private static int constraintPanelWidth = 200;
    private static JFrame mainFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OknoMigLayout frame = new OknoMigLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OknoMigLayout() {
        mainFrame = new JFrame();
        mainFrame.setLayout(new MigLayout());
        mainFrame.setBounds(100,100,1600,800);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel constantsPanel = new JPanel(new MigLayout());
        constantsPanel.setBackground(Color.gray);

        JButton addConstantBtn = new JButton("Add constant");

        addConstantBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ConstantsForm constForm = new ConstantsForm();
                if(!constForm.wasCancelled()) {
                    ConstantPanel constPanel = new ConstantPanel(constForm.getConstName(), constForm.getConstValue());

                    constantsPanel.add(constPanel);
                    constantsPanel.revalidate();
                    constantsPanel.repaint();
                }
            }
        });

        constantsPanel.add(addConstantBtn);
        //northPanel.add(new JButton("Add variable"), "grow");

        mainFrame.add(constantsPanel,"dock north");

        JPanel variablesPanel = new JPanel(new MigLayout());
        constantsPanel.setBackground(Color.cyan);

        JButton addVariableBtn = new JButton("Add variable");

        addVariableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ConstantsForm constForm = new ConstantsForm();
                if(!constForm.wasCancelled()) {
                    JLabel label = new JLabel(constForm.getConstName() + ": " + constForm.getConstValue());
                    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
                    label.setBorder(border);

                    variablesPanel.add(label);
                    variablesPanel.revalidate();
                    variablesPanel.repaint();
                }
            }
        });

        variablesPanel.add(addVariableBtn);
        //northPanel.add(new JButton("Add variable"), "grow");

        mainFrame.add(variablesPanel,"dock north");

        JPanel centerPanel = new JPanel(new MigLayout());
        centerPanel.setBackground(Color.PINK);

        mainFrame.add(centerPanel, "dock center");

        JPanel centerNorthPanel = new JPanel(new MigLayout());
        centerNorthPanel.setBackground(Color.orange);
        JButton addConstraintBtn = new JButton("Add constraint");
        addConstraintBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ConstraintForm constraintForm = new ConstraintForm();
                Map<String, List<JComboBox>> attMap = constraintForm.getFormData();
                if (!attMap.isEmpty()) {
                    centerPanel.add(new ConstraintPanel(attMap), "dock west, height 100%, width " + constraintPanelWidth);
                    centerPanel.revalidate();
                }
            }
        });

        centerNorthPanel.add(addConstraintBtn);

        centerPanel.add(centerNorthPanel, "dock north, width 100%");
    }

    private class ConstraintPanel extends JPanel {

        ConstraintPanel thisPanel;

        public ConstraintPanel(Map<String, List<JComboBox>> attMap) {
            super();
            thisPanel = this;
            this.setLayout(new MigLayout());
            Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();
            JLabel label = new JLabel(entry.getKey());
            this.add(label, "wrap");

            for (JComboBox att : entry.getValue()) {
                JLabel attValue = new JLabel((String) att.getSelectedItem());
                this.add(attValue, "wrap");
            }

            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });
            this.add(editBtn, "wrap");

            JButton removeBtn = new JButton("Remove");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisPanel.getParent().remove(thisPanel);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });
            this.add(removeBtn);
        }
    }

    private class ConstantPanel extends JPanel{
        ConstantPanel thisPanel;

        public ConstantPanel(String varName, String varValue) {
            super();
            thisPanel = this;
            this.setLayout(new MigLayout());
            JLabel label = new JLabel(varName + ":" + varValue);
            this.add(label);

            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            this.setBorder(border);

            JButton editBtn = new JButton("E");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    ConstantsForm constantsForm = new ConstantsForm(varName, varValue);
                    label.setText(constantsForm.getConstName() + ":" + constantsForm.getConstValue());
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });
            this.add(editBtn);

            JButton removeBtn = new JButton("R");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisPanel.getParent().remove(thisPanel);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });
            this.add(removeBtn);
        }
    }
}

class ConstantsForm extends JDialog
{
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField tfName = new JTextField();
    private JTextField tfValue = new JTextField();
    private String constName = "";
    private String constValue = "";
    private boolean cancelled = false;

    public ConstantsForm(String name, String value){
        setModal(true);

        setSize(Konstanty.VELIKOST_PRIHLASOVACIHO_OKNA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setTitle("Create constants");

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
        this.add(tfName, "wrap");
        this.add(lblValue);
        this.add(tfValue, "wrap");
        this.add(btnOk);
        this.add(btnCancel);
        this.setVisible(true);
    }

    public ConstantsForm() {
        this("","");
    }

    public String getConstName(){
        String returnValue = null;
        if(!cancelled){
            returnValue = tfName.getText();
        }
        return returnValue;
    }

    public String getConstValue(){
        String returnValue = null;
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

class ConstraintForm extends JDialog
{
    private static final long serialVersionUID = -8229943813762614201L;
    private JButton btnSubmit = new JButton("OK");
    private JButton btnClose = new JButton("CANCEL");
    private JButton btnAdd = new JButton("Add");
    private JTextField tfAttribute = new JTextField();
    private int row = 2;
    private boolean closed = true;

    JTextField tf = new JTextField(8);
    JLabel lblType = new JLabel("Type");
    JLabel lblAttribute = new JLabel("Attribute");
    String[] tables = { "Table1", "Table2", "Table3", "Table4", "Table5" };
    JComboBox cboxTables = new JComboBox(tables);
    String[] attributes = { "Att1", "Att2", "Att3", "Att4", "Att5" };
    JComboBox cboxAttributes = new JComboBox(attributes);
    java.util.List<JComboBox> attValuesList = new ArrayList<>();
    String[] operators = { "<", "=", ">", "!=" };
    JComboBox cboxOperators = new JComboBox(operators);
    JTextField tfAttValue = new JTextField("Value");

    public ConstraintForm()
    {
        setModal(true);
        setLocation(400,300);
        // TODO - cancel on close - don't know how
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        attValuesList.add(new JComboBox());
        attValuesList.clear();

        setSize(600,200);
        setLocationRelativeTo(null);
        this.setTitle("Attributes");

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
                                         cboxAttributes = new JComboBox(attributes);
                                         cboxOperators = new JComboBox(operators);
                                         cboxAttributes.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
                                         tfAttValue = new JTextField("Value");
                                         GridBagConstraints g = new GridBagConstraints();
                                         g.insets = new Insets(5, 5, 5, 5);
                                         g.fill = GridBagConstraints.HORIZONTAL;
                                         g.gridx = 1;
                                         g.gridy = row;
                                         g.weightx = 1.0;
                                         g.weighty = 0.0;
                                         g.gridwidth = 4;
                                         setSize(getWidth(),getHeight() + 38);
                                         remove(btnClose);
                                         remove(btnSubmit);
                                         add(cboxAttributes, g);
                                         attValuesList.add(cboxAttributes);
                                         g.gridx = 5;
                                         g.gridy = row++;
                                         g.gridwidth = 1;
                                         add(cboxOperators, g);
                                         g.gridx = 6;
                                         add(tfAttValue, g);
                                         GridBagConstraints g2 = new GridBagConstraints();
                                         g2.insets = new Insets(5, 5, 5, 5);
                                         g2.fill = GridBagConstraints.HORIZONTAL;
                                         g2.weightx = 1.0;
                                         g2.weighty = 0.0;
                                         g2.gridx = 1;
                                         g2.gridy = row++;
                                         g2.gridwidth = 1;
                                         add(btnSubmit, g2);
                                         g2.gridx = 4;
                                         add(btnClose, g2);
                                     }
                                 }
        );

        this.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1.0;
        g.weighty = 0.0;
        g.gridwidth = 1;
        this.add(lblType, g);
        g.gridx = 1;
        g.gridwidth = 4;
        this.add(cboxTables, g);
        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 1;
        this.add(lblAttribute, g);
        g.gridx = 1;
        g.gridwidth = 4;
        this.add(cboxAttributes, g);
        g.gridx = 5;
        g.gridwidth = 1;
        this.add(cboxOperators, g);
        g.gridx = 6;
        this.add(tfAttValue, g);
        attValuesList.add(cboxAttributes);
        g.gridx = 1;
        g.gridy = 2;
        this.add(btnSubmit, g);
        g.gridx = 4;
        this.add(btnClose, g);
        g.gridx = 0;
        g.gridy = 2;
        this.add(btnAdd, g);
        this.setVisible(true);

    }

    public Map<String, java.util.List<JComboBox>> getFormData()
    {
        Map<String, List<JComboBox>> dataMap = new LinkedHashMap<>();

        if(!closed) {
            dataMap.put((String) cboxTables.getSelectedItem(), attValuesList);
        }

        return dataMap;
    }
}
