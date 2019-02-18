package gui;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import ostatni.Konstanty;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.*;

public class OknoMigLayout {

    private static int constraintPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private static String constantFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\konstanty\\";
    //private URL constantFolderPathUrl = this.getClass().getClassLoader().getResource("zdroje/konstanty");
    private static String queryFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\dotazy\\";
    private static String variableFolderPath = "C:\\WorkspaceSchool\\SPADE\\src\\zdroje\\promenne\\";
    private static JButton addConstantBtn = new JButton("Add constant");
    private static JButton addVariableBtn = new JButton("Add variable");
    private static JButton addConstraintBtn = new JButton("Add constraint");
    private static JButton createQueryBtn = new JButton("Create query");
    private static JButton loadQueryBtn = new JButton("Load query");
    private static JPanel centerNorthPanel = new JPanel(new MigLayout());
    private static JPanel centerPanel = new JPanel(new MigLayout());
    private static JPanel bottomPanel = new JPanel(new MigLayout());
    private static JPanel axisPanel = new JPanel(new MigLayout());
    private static JLabel lblName = new JLabel("Name");
    private static JTextField varOrQueryNameTf = new JTextField(10);
    private static final JFileChooser fileChooser = new JFileChooser();

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

        mainFrame.add(constantsPanel,"dock north");

        JPanel variablesPanel = new JPanel(new MigLayout());
        variablesPanel.add(addVariableBtn);

        File variableFolder = new File(variableFolderPath);
        if (!variableFolder.exists()){
            variableFolder.mkdirs();
        } else {
            File[] files = variableFolder.listFiles();
            for (File file : files) {
                try {
                    String content = FileUtils.readFileToString(file, "utf-8");
                    VariablePanel varPanel = new VariablePanel(file.getName().substring(0, file.getName().indexOf('.')), content);
                    variablesPanel.add(varPanel);
                    variablesPanel.revalidate();
                    variablesPanel.repaint();
                } catch(Exception e) {
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
                centerNorthPanel.add(lblName);
                centerNorthPanel.add(varOrQueryNameTf);

                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                centerPanel.revalidate();
                centerPanel.repaint();

                centerNorthPanel.revalidate();
                centerNorthPanel.repaint();
            }
        });

        mainFrame.add(variablesPanel,"dock north");

        centerPanel.setBackground(Color.PINK);

        mainFrame.add(centerPanel, "dock center");

        centerNorthPanel.setBackground(Color.orange);

        addConstraintBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ConstraintsForm constraintForm = new ConstraintsForm();
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
                centerNorthPanel.add(lblName);
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

        loadQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.setCurrentDirectory(new File(queryFolderPath));
                int returnVal = fileChooser.showOpenDialog(centerNorthPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    centerPanel.removeAll();
                    centerNorthPanel.removeAll();

                    centerNorthPanel.add(addConstraintBtn);
                    varOrQueryNameTf.setText(file.getName().substring(0, file.getName().indexOf('.')));
                    centerNorthPanel.add(varOrQueryNameTf);

                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    centerPanel.add(axisPanel, "dock west, height 100%, width " + constraintPanelWidth);

                    try {
                        String content = FileUtils.readFileToString(file, "utf-8");
                        JSONObject obj = new JSONObject(content);

                        JComboBox axisCBox = (JComboBox) axisPanel.getComponent(0);
                        axisCBox.setSelectedItem(obj.getString("axis"));

                        JSONArray constraints = (JSONArray) obj.get("constraints");
                        for (Object cons : constraints)
                        {
                            JSONObject object = (JSONObject) cons;
                            ConstraintPanel panel = new ConstraintPanel(object);
                            centerPanel.add(panel, "dock west, height 100%, width " + constraintPanelWidth);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                        }
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }

                    centerPanel.revalidate();
                    centerPanel.repaint();

                    centerNorthPanel.revalidate();
                    centerNorthPanel.repaint();
                } else {
                    // user cancelled the action - nothing happens
                }
            }

        });

        centerNorthPanel.add(createQueryBtn);
        centerNorthPanel.add(loadQueryBtn);

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
                File file;
                JSONObject jsonObject = new JSONObject();
                if (!variableCreation) {
                    file = new File(queryFolderPath + varOrQueryNameTf.getText() + ".json");
                    jsonObject.put("axis", (String) cboxAxisOptions.getSelectedItem());
                } else {
                    file = new File(variableFolderPath + varOrQueryNameTf.getText() + ".json");
                }

                Component centerComponents[] = centerPanel.getComponents();
                List<JSONObject> constList = new ArrayList<>();

                for (Component component: centerComponents) {
                    if(component instanceof ConstraintPanel) {
                        ConstraintPanel constPanel = (ConstraintPanel) component;
                        if(constPanel.getAttributeMap() == null){
                            constList.add(constPanel.getJsonConstraint());
                        } else {
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
                }
                JSONObject[] constArray = new JSONObject[constList.size()];
                constArray = constList.toArray(constArray);
                jsonObject.put("constraints", constArray);
                String result = jsonObject.toString(2);
                if(variableCreation){
                    File variableFolder = new File(variableFolderPath);
                    File[] files = variableFolder.listFiles();
                    if (files != null) {
                        List<String> fileNames = Arrays.asList(files)
                                .stream()
                                .map(f -> f.getName().substring(0, f.getName().indexOf('.')))
                                .collect(Collectors.toList());
                        if(!fileNames.contains(varOrQueryNameTf.getText())){
                            VariablePanel varPanel = new VariablePanel(varOrQueryNameTf.getText(), result);
                            variablesPanel.add(varPanel);
                            variablesPanel.revalidate();
                            variablesPanel.repaint();
                        } else {
                            //get var panel a nastavit mu content (result)
                            Component[] components = variablesPanel.getComponents();
                            for(Component comp :  components){
                                if(comp instanceof VariablePanel){
                                    VariablePanel varPanel = (VariablePanel) comp;
                                    if(varPanel.getName().equals(varOrQueryNameTf.getText())) {
                                        varPanel.setContent(result);
                                    }
                                }
                            }
                        }
                    }
                    variableCreation = false;
                }
                Writer writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file), "utf-8"));
                    writer.write(result);
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
                centerNorthPanel.add(loadQueryBtn);
                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();

                JOptionPane.showMessageDialog(null, "Save successful");
            }
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                centerNorthPanel.removeAll();
                centerPanel.removeAll();
                mainFrame.remove(bottomPanel);

                varOrQueryNameTf.setText("");
                centerNorthPanel.add(createQueryBtn);
                centerNorthPanel.add(loadQueryBtn);
                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
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

            JButton editBtn = new JButton();
            try {
                Image img = ImageIO.read(getClass().getClassLoader().getResource("zdroje/obrazky/editImage.png"));
                if (img != null){
                    Image newimg = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
                    editBtn.setIcon(new ImageIcon(newimg));
                } else {
                    editBtn.setText("E");
                }
                editBtn.setMargin(new Insets(1,1,1,1));
                editBtn.setBorderPainted(false);
                editBtn.setContentAreaFilled(false);
                editBtn.setFocusPainted(false);
                editBtn.setOpaque(false);
            } catch (Exception ex) {
                System.out.println(ex);
            }
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

            JButton removeBtn = new JButton();
            try {
                Image img = ImageIO.read(getClass().getClassLoader().getResource("zdroje/obrazky/deleteImage.png"));
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

    private class VariablePanel extends JPanel{
        VariablePanel thisPanel;
        String name;
        String content;

        public VariablePanel(String varName, String varContent) {
            super();
            thisPanel = this;
            name = varName;
            content = varContent;
            this.setLayout(new MigLayout());
            JLabel label = new JLabel(name);
            this.add(label);

            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            this.setBorder(border);

            JButton editBtn = new JButton();
            try {
                Image img = ImageIO.read(getClass().getClassLoader().getResource("zdroje/obrazky/editImage.png"));
                if (img != null){
                    Image newimg = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
                    editBtn.setIcon(new ImageIcon(newimg));
                } else {
                    editBtn.setText("E");
                }
                editBtn.setMargin(new Insets(1,1,1,1));
                editBtn.setBorderPainted(false);
                editBtn.setContentAreaFilled(false);
                editBtn.setFocusPainted(false);
                editBtn.setOpaque(false);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    variableCreation = true;
                    centerPanel.removeAll();
                    centerNorthPanel.removeAll();

                    centerNorthPanel.add(addConstraintBtn);
                    varOrQueryNameTf.setText(name);
                    centerNorthPanel.add(varOrQueryNameTf);

                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    //centerPanel.add(axisPanel, "dock west, height 100%, width " + constraintPanelWidth);

                    centerPanel.revalidate();
                    centerPanel.repaint();

                    centerNorthPanel.revalidate();
                    centerNorthPanel.repaint();

                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray constraints = (JSONArray) jsonObject.get("constraints");
                    for (Object cons : constraints)
                    {
                        JSONObject object = (JSONObject) cons;
                        ConstraintPanel panel = new ConstraintPanel(object);
                        centerPanel.add(panel, "dock west, height 100%, width " + constraintPanelWidth);
                        centerPanel.revalidate();
                        centerPanel.repaint();
                    }
                }

            });
            this.add(editBtn);

            JButton removeBtn = new JButton();
            try {
                Image img = ImageIO.read(getClass().getClassLoader().getResource("zdroje/obrazky/deleteImage.png"));
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
                    thisPanel.getParent().remove(thisPanel);
                    try {
                        File file = new File(variableFolderPath + name + ".json");
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

        public String getName(){
            return this.name;
        }

        public void setContent(String content){
            this.content = content;
        }
    }

    private class ConstraintPanel extends JPanel {

        ConstraintPanel thisPanel;
        Map<String, List<JComboBox>> attMap;
        JSONObject constraints;

        public ConstraintPanel(Map<String, List<JComboBox>> attMap) {
            super();
            thisPanel = this;
            this.attMap = attMap;
            this.setLayout(new MigLayout());
            Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();

            JSONObject jsonConstraint = new JSONObject();
            jsonConstraint.put("table", entry.getKey());
            JSONArray attributes = new JSONArray();

            for (JComboBox att : entry.getValue()) {
                attributes.put(att.getSelectedItem());
            }

            jsonConstraint.put("attributes", attributes);
            this.constraints = jsonConstraint;

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
                    JSONObject constraint = getJsonConstraint();
                    ConstraintsForm form = new ConstraintsForm(constraint);

                    Component[] components = getComponents();
                    for(int i = 0; i < components.length; i++){
                        if(components[i] instanceof JLabel){
                            remove(components[i]);
                        }
                    }

                    Map<String, List<JComboBox>> attMap = form.getFormData();
                    if (!attMap.isEmpty()) {
                        Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();
                        JLabel label = new JLabel(entry.getKey());
                        add(label, "wrap, dock north");
                        // delete reminder box
                        //attMap.get(entry.getKey()).remove(0);
                        setAttMap(attMap);
                        setConstraints(attMap);
                        for(int i = 0; i< entry.getValue().size(); i++){
                            JLabel attValue = new JLabel((String) entry.getValue().get(i).getSelectedItem());
                            add(attValue, "wrap, dock north");
                        }
                    }

                    setBorder(new EmptyBorder(10, 10, 10, 10));
                    centerPanel.revalidate();
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

        public ConstraintPanel(JSONObject constraints) {
            super();
            thisPanel = this;
            this.constraints = constraints;

            String tableName = constraints.getString("table");

            this.setLayout(new MigLayout());
            JLabel label = new JLabel(tableName);
            this.add(label, "wrap");

            JSONArray attributes = (JSONArray) constraints.get("attributes");
            for(Object attribute: attributes){
                JLabel attValue = new JLabel((String) attribute);
                this.add(attValue, "wrap");
            }

            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    JSONObject constraint = getJsonConstraint();
                    ConstraintsForm form = new ConstraintsForm(constraint);

                    Component[] components = getComponents();
                    for(int i = 0; i < components.length; i++){
                        if(components[i] instanceof JLabel){
                            remove(components[i]);
                        }
                    }
                    Map<String, List<JComboBox>> attMap = form.getFormData();
                    if (!attMap.isEmpty()) {
                        Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();
                        JLabel label = new JLabel(entry.getKey());
                        add(label, "wrap, dock north");
                        // delete reminder box
                        //attMap.get(entry.getKey()).remove(0);
                        setAttMap(attMap);
                        setConstraints(attMap);
                        for(int i = 0; i< entry.getValue().size(); i++){
                            JLabel attValue = new JLabel((String) entry.getValue().get(i).getSelectedItem());
                            add(attValue, "wrap, dock north");
                        }
                    }
                    setBorder(new EmptyBorder(10, 10, 10, 10));
                    centerPanel.revalidate();
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

        public void setAttMap(Map<String, List<JComboBox>> attMap){
            this.attMap = attMap;
        }

        public void setAttMap(JSONObject constraints){
            this.constraints = constraints;
        }

        public void setConstraints(Map<String, List<JComboBox>> attMap){
            Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();
            JSONObject jsonConstraint = new JSONObject();
            jsonConstraint.put("table", entry.getKey());
            JSONArray attributes = new JSONArray();

            for (JComboBox att : entry.getValue()) {
                attributes.put(att.getSelectedItem());
            }

            jsonConstraint.put("attributes", attributes);
            this.constraints = jsonConstraint;
        }

        public Map<String, List<JComboBox>> getAttributeMap(){
            return this.attMap;
        }

        public JSONObject getJsonConstraint() {
            return this.constraints;
        }
    }
}

class ConstantsForm extends JDialog
{
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private JTextField tfName = new JTextField();
    private JTextField tfValue = new JTextField();
    private String constName;
    private String constValue;
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

class ConstraintsForm extends JDialog
{
    private static final long serialVersionUID = -8229943813762614201L;
    private JButton btnSubmit = new JButton("OK");
    private JButton btnClose = new JButton("CANCEL");
    private JButton btnAdd = new JButton("Add");
    private JTextField tfAttribute = new JTextField();
    private boolean closed = true;

    JTextField tf = new JTextField(8);
    JLabel lblType = new JLabel("Type");
    JLabel lblAttribute = new JLabel("Attribute");
    String[] tables = { "Table1", "Table2", "Table3", "Table4", "Table5" };
    JComboBox cboxTables = new JComboBox(tables);
    String[] attributes = { "Att1", "Att2", "Att3", "Att4", "Att5" };
    JComboBox cboxAttributes = new JComboBox(attributes);
    List<JComboBox> attValuesList = new ArrayList<>();
    String[] operators = { "<", "=", ">", "!=" };
    JComboBox cboxOperators = new JComboBox(operators);
    JTextField tfAttValue = new JTextField("Value");

    public ConstraintsForm()
    {
        setModal(true);
        setLocation(400,300);
        // TODO - cancel on close - don't know how
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //attValuesList.add(new JComboBox());
        //attValuesList.clear();

        setSize(600,150);
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

                                         setSize(getWidth(),getHeight() + 33);
                                         remove(btnClose);
                                         remove(btnSubmit);
                                         remove(btnAdd);

                                         add(cboxAttributes, "width 40%");
                                         attValuesList.add(cboxAttributes);
                                         add(cboxOperators, "width 15%");
                                         add(tfAttValue, "width 20%, wrap");
                                         add(btnAdd);
                                         add(new JLabel());
                                         add(btnSubmit, "width 15%");
                                         add(btnClose, "width 15%");
                                     }
                                 }
        );

        this.setLayout(new MigLayout());
        this.add(lblType);
        this.add(cboxTables, "width 40%, wrap");
        this.add(lblAttribute);
        this.add(cboxAttributes, "width 40%");
        this.add(cboxOperators, "width 15%");
        this.add(tfAttValue, "width 20%, wrap");
        attValuesList.add(cboxAttributes);
        this.add(btnAdd);
        this.add(new JLabel());
        this.add(btnSubmit, "width 15%");
        this.add(btnClose, "width 15%");
        this.setVisible(true);
    }

    public ConstraintsForm(JSONObject constraint){
        setModal(true);
        setLocation(400,300);
        // TODO - cancel on close - don't know how
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int temp = 0;

        //attValuesList.add(new JComboBox());
        //attValuesList.clear();

        setSize(600,150);
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

                                         setSize(getWidth(),getHeight() + 33);
                                         remove(btnClose);
                                         remove(btnSubmit);
                                         remove(btnAdd);

                                         add(cboxAttributes, "width 40%");
                                         attValuesList.add(cboxAttributes);
                                         add(cboxOperators, "width 15%");
                                         add(tfAttValue, "width 20%, wrap");
                                         add(btnAdd);
                                         add(new JLabel());
                                         add(btnSubmit, "width 15%");
                                         add(btnClose, "width 15%");
                                     }
                                 }
        );

        this.setLayout(new MigLayout());
        this.add(lblType);
        String tableName = constraint.getString("table");
        cboxTables.setSelectedItem(tableName);
        this.add(cboxTables, "width 40%, wrap");
        this.add(lblAttribute);
        //attValuesList.add(cboxAttributes);

        JSONArray atts = (JSONArray) constraint.get("attributes");
        for(Object attribute: atts) {
            cboxAttributes = new JComboBox(attributes);
            cboxOperators = new JComboBox(operators);
            cboxAttributes.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
            tfAttValue = new JTextField("Value");

            setSize(getWidth(), getHeight() + 33);

            cboxAttributes.setSelectedItem((String) attribute);
            if (temp != 0) {
                add(new JLabel());
            }
            temp++;
            add(cboxAttributes, "width 40%");
            attValuesList.add(cboxAttributes);
            add(cboxOperators, "width 15%");
            add(tfAttValue, "width 20%, wrap");
        }
        temp = 0;

        this.add(btnAdd);
        this.add(new JLabel());
        this.add(btnSubmit, "width 15%");
        this.add(btnClose, "width 15%");
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

