package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import ostatni.Konstanty;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.*;

public class OknoMigLayout {

    private static int constraintPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private static String constantFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\konstanty\\";
    private static String queryFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\dotazy\\";
    private static String variableFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\promenne\\";
    private static JButton addConstantBtn = new JButton("Add constant");
    private static JButton addVariableBtn = new JButton("Add variable");
    private static JButton addConstraintBtn = new JButton("Add constraint");
    private static JButton createQueryBtn = new JButton("Create query");
    private static JPanel centerNorthPanel = new JPanel(new MigLayout());
    private static JPanel centerPanel = new JPanel(new MigLayout());
    private static JPanel bottomPanel = new JPanel(new MigLayout());
    private static JPanel axisPanel = new JPanel(new MigLayout());
    private static JTextField varOrQueryNameTf = new JTextField(10);

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
        constantsPanel.setBackground(Color.cyan);

        constantsPanel.add(addConstantBtn);

        File queryFolder = new File(queryFolderPath);
        if (!queryFolder.exists()) {
            queryFolder.mkdirs();
        }

        File constantFolder = new File(constantFolderPath);
        if (!constantFolder.exists()){
            constantFolder.mkdirs();
        } else {
            File[] files = constantFolder.listFiles();
            for (File file : files) {
                try {
                    String content = FileUtils.readFileToString(file, "utf-8");
                    JSONObject obj = new JSONObject(content);
                    ConstantPanel constPanel = new ConstantPanel(obj.getString("name"), obj.getString("value"));
                    constantsPanel.add(constPanel);
                    constantsPanel.revalidate();
                    constantsPanel.repaint();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        addConstantBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ConstantsForm constForm = new ConstantsForm();
                if(!constForm.wasCancelled()) {
                    ConstantPanel constPanel = new ConstantPanel(constForm.getConstName(), constForm.getConstValue());
                    String jsonString = new JSONObject()
                            .put("name", constForm.getConstName())
                            .put("value", constForm.getConstValue()).toString(2);
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(constantFolderPath + constForm.getConstName() + ".json"), "utf-8"));
                        writer.write(jsonString);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {writer.close();} catch (Exception ex) {/*ignore*/}
                    }
                    constantsPanel.add(constPanel);
                    constantsPanel.revalidate();
                    constantsPanel.repaint();
                }
            }
        });

        //northPanel.add(new JButton("Add variable"), "grow");

        mainFrame.add(constantsPanel,"dock north");

        JPanel variablesPanel = new JPanel(new MigLayout());

        File variableFolder = new File(variableFolderPath);
        if (!variableFolder.exists()){
            variableFolder.mkdirs();
        } else {
            File[] files = variableFolder.listFiles();
            for (File file : files) {
                try {
                    String content = FileUtils.readFileToString(file, "utf-8");


                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        addVariableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                variableCreation = true;
                centerPanel.removeAll();
                centerNorthPanel.removeAll();

                centerNorthPanel.add(addConstraintBtn);
                centerNorthPanel.add(varOrQueryNameTf);

                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                centerPanel.revalidate();
                centerPanel.repaint();

                centerNorthPanel.revalidate();
                centerNorthPanel.repaint();
            }
        });

        variablesPanel.add(addVariableBtn);
        //northPanel.add(new JButton("Add variable"), "grow");

        mainFrame.add(variablesPanel,"dock north");

        centerPanel.setBackground(Color.PINK);

        mainFrame.add(centerPanel, "dock center");

        centerNorthPanel.setBackground(Color.orange);

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

        createQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.removeAll();
                centerNorthPanel.removeAll();

                centerNorthPanel.add(addConstraintBtn);
                centerNorthPanel.add(varOrQueryNameTf);

                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                centerPanel.add(centerNorthPanel, "dock north, width 100%");
                centerPanel.add(axisPanel, "dock west, height 100%, width " + constraintPanelWidth);

                centerPanel.revalidate();
                centerPanel.repaint();

                centerNorthPanel.revalidate();
                centerNorthPanel.repaint();
            }
        });

        centerNorthPanel.add(createQueryBtn);

        centerPanel.add(centerNorthPanel, "dock north, width 100%");


        axisPanel.setBackground(new Color(168, 79, 25));

        String[] axisOptions = { "Person", "Iteration", "Days", "Weeks", "Months" };
        JComboBox cboxAxisOptions = new JComboBox(axisOptions);
        axisPanel.add(cboxAxisOptions, "width 90%");


        bottomPanel.setBackground(Color.GRAY);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = null;
                JSONObject jsonObject = new JSONObject();
                if (!variableCreation) {
                    file = new File(queryFolderPath + varOrQueryNameTf.getText() + ".json");
                    jsonObject.put("axis", (String) cboxAxisOptions.getSelectedItem());
                } else {
                    file = new File(variableFolderPath + varOrQueryNameTf.getText() + ".json");
                }
                variableCreation = false;

                Component centerComponents[] = centerPanel.getComponents();
                List<JSONObject> constList = new ArrayList<>();

                for (Component component: centerComponents) {
                    if(component instanceof ConstraintPanel) {
                        ConstraintPanel constPanel = (ConstraintPanel) component;
                        Map.Entry<String, List<JComboBox>> entry = constPanel.getAttributeMap().entrySet().iterator().next();
                        JSONObject jsonConstraint = new JSONObject();
                        jsonConstraint.put("table", entry.getKey());
                        List<String> attList = new ArrayList<>();

                        for (JComboBox att : entry.getValue()) {
                            attList.add((String) att.getSelectedItem());
                        }
                        String[] attArray = new String[attList.size()];
                        attArray = attList.toArray(attArray);
                        jsonConstraint.put("attributes", attArray);
                        constList.add(jsonConstraint);
                    }
                }
                JSONObject[] constArray = new JSONObject[constList.size()];
                constArray = constList.toArray(constArray);
                jsonObject.put("constraints", constArray);

                Writer writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file), "utf-8"));
                    writer.write(jsonObject.toString(2));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {writer.close();} catch (Exception ex) {/*ignore*/}
                }
                centerNorthPanel.removeAll();
                centerPanel.removeAll();
                mainFrame.remove(bottomPanel);

                varOrQueryNameTf.setText("");
                centerNorthPanel.add(createQueryBtn);
                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();

                JOptionPane.showMessageDialog(null, "Save successful");
            }
        });
        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
    }

    private class ConstantPanel extends JPanel{
        ConstantPanel thisPanel;
        String name;
        String value;

        public ConstantPanel(String constName, String constValue) {
            super();
            thisPanel = this;
            name = constName;
            value = constValue;
            this.setLayout(new MigLayout());
            JLabel label = new JLabel(name + ":" + value);
            this.add(label);

            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            this.setBorder(border);

            JButton editBtn = new JButton("E");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    ConstantsForm constantsForm = new ConstantsForm(name, value);
                    String oldName = name;
                    name = constantsForm.getConstName();
                    value = constantsForm.getConstValue();
                    label.setText(constantsForm.getConstName() + ":" + constantsForm.getConstValue());
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    File oldFile = new File(constantFolderPath + oldName + ".json");
                    String newFilePath = oldFile.getAbsolutePath().replace(oldFile.getName(), "") + name + ".json";
                    File newFile = new File(newFilePath);
                    String jsonString = new JSONObject()
                            .put("name", name)
                            .put("value", value).toString(2);
                    Writer writer = null;
                    try {
                        FileUtils.moveFile(oldFile, newFile);
                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(newFile), "utf-8"));
                        writer.write(jsonString);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {writer.close();} catch (Exception ex) {/*ignore*/}
                    }

                }

            });
            this.add(editBtn);

            JButton removeBtn = new JButton("R");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisPanel.getParent().remove(thisPanel);
                    try {
                        File file = new File(constantFolderPath + name + ".json");
                        if( !file.delete() ){
                            System.out.println("Delete operation failed.");
                        }
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });
            this.add(removeBtn);
        }
    }

    private class ConstraintPanel extends JPanel {

        ConstraintPanel thisPanel;
        Map<String, List<JComboBox>> attMap;

        public ConstraintPanel(Map<String, List<JComboBox>> attMap) {
            super();
            thisPanel = this;
            this.attMap = attMap;
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

        public Map<String, List<JComboBox>> getAttributeMap(){
            return this.attMap;
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

