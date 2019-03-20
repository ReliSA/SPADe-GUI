package gui;

import databaze.PohledDAO;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import ostatni.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.*;

public class OknoMigLayout extends JFrame{

    public static OknoMigLayout instance;
    private static int constraintPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private static String constantFolderPath = "zdroje\\konstanty\\";
    private static String queryFolderPath = "zdroje\\dotazy\\";
    private static String variableFolderPath = "zdroje\\promenne\\";
    private static JButton addConstantBtn = new JButton("Add constant");
    private static JButton addVariableBtn = new JButton("Add variable");
    private static JButton addConstraintBtn = new JButton("Add constraint");
    private static JButton createQueryBtn = new JButton("Create query");
    private static JButton loadQueryBtn = new JButton("Load query");
    private static JButton testVarQueryBtn = new JButton("Test query");
    private static JButton testQueryBtn = new JButton("Test query");
    private static ButtonGroup group = new ButtonGroup();
    private static List<JRadioButton> radioButtonList = new ArrayList<>();
    private static JRadioButton radioSum = new JRadioButton("SUM");
    private static JRadioButton radioCount = new JRadioButton("COUNT");
    private static JRadioButton radioMin = new JRadioButton("MIN");
    private static JRadioButton radioMax = new JRadioButton("MAX");
    private static JPanel centerNorthPanel = new JPanel(new MigLayout());
    private static JPanel centerPanel = new JPanel(new MigLayout());
    private static JPanel bottomPanel = new JPanel(new MigLayout());
    private static JPanel axisPanel = new JPanel(new MigLayout());
    private static JLabel lblName = new JLabel("Name");
    private static JTextField varOrQueryNameTf = new JTextField(10);
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final Map<String, List<Sloupec>> strukturyPohledu = new TreeMap<>();
    private static List<ComboBoxItem> preparedVariableValues = new ArrayList<>();

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
//        super(Konstanty.POPISY.getProperty("menuVytvorGraf"));
//        if (instance != null)
//            instance.dispose();
//        instance = this;

        mainFrame = new JFrame();
        mainFrame.setLayout(new MigLayout());
        mainFrame.setBounds(100,100,1600,800);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PohledDAO pohledDAO = new PohledDAO();
        PohledEnum[] pohledy = PohledEnum.values();
        List<Sloupec> sloupce;

        for(PohledEnum pohled : pohledy){
            sloupce = pohledDAO.nactecniStrukturyPohledu(pohled.getViewName());
            strukturyPohledu.put(pohled.getViewName(), sloupce);
        }

//        List<ArtifactView> artifactViews = pohledDAO.nactiArtifactView();
//        List<CommitedConfigView> commitedConfigViews = pohledDAO.nactiCommitedConfigView();
//        List<CommitView> commitViews = pohledDAO.nactiCommitView();
//        List<ConfigurationView> configurationViews = pohledDAO.nactiConfigurationView();
//        List<FieldChangeView> fieldChangeViews = pohledDAO.nactiFieldChangeView();
//        List<WorkUnitView> workUnitViews = pohledDAO.nactiWorkUnitView();

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
                    ComboBoxItem comboBoxItem = new ComboBoxItem(obj.getString("name"), "konstanta", obj.getString("value"));
                    preparedVariableValues.add(comboBoxItem);
                    System.out.println(preparedVariableValues);
                    constantsPanel.revalidate();
                    constantsPanel.repaint();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        addConstantBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FormularVytvoreniKonstanty constForm = new FormularVytvoreniKonstanty();
                if(!constForm.wasCancelled()) {
                    ComboBoxItem comboBoxItem = new ComboBoxItem(constForm.getConstName(), "konstanta",constForm.getConstValue());
                    preparedVariableValues.add(comboBoxItem);
                    System.out.println(preparedVariableValues);
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
                    String varName = file.getName().substring(0, file.getName().indexOf('.'));
                    VariablePanel varPanel = new VariablePanel(varName, content);
                    JSONObject obj = new JSONObject(content);
                    ComboBoxItem comboBoxItem = new ComboBoxItem(varName, "promenna", obj.getString("queryResult"));
                    preparedVariableValues.add(comboBoxItem);
                    System.out.println(preparedVariableValues);
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
                centerNorthPanel.add(radioSum);
                centerNorthPanel.add(radioCount);
                centerNorthPanel.add(radioMin);
                centerNorthPanel.add(radioMax);
                radioSum.setSelected(true);
                centerNorthPanel.add(testVarQueryBtn);

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

        group.add(radioSum);
        group.add(radioCount);
        group.add(radioMin);
        group.add(radioMax);
        // default
        radioSum.setSelected(true);

        radioButtonList.add(radioCount);
        radioButtonList.add(radioMax);
        radioButtonList.add(radioMin);
        radioButtonList.add(radioSum);

        addConstraintBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FormularVytvoreniOmezeni constraintForm = new FormularVytvoreniOmezeni(strukturyPohledu, null, preparedVariableValues);
                JSONObject attributes = constraintForm.getFormData();
                if (!attributes.isEmpty()) {
                    centerPanel.add(new ConstraintPanel(attributes), "dock west, height 100%, width " + constraintPanelWidth);
                    centerPanel.revalidate();
                }
            }
        });

        testVarQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                testQuery();
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

        String[] axisOptions = { "Person", "Iteration"};
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
                            JSONObject jsonConstraint = constPanel.getJsonConstraint();
                            if(variableCreation){
                                jsonObject = writeQueryResult(jsonObject);
                                ComboBoxItem comboBoxItem = new ComboBoxItem(varOrQueryNameTf.getText(), "promenna", jsonObject.getString("queryResult"));
                                preparedVariableValues.add(comboBoxItem);
                                System.out.println(preparedVariableValues);
                            }
                            constList.add(jsonConstraint);
                        } else {
                            Map.Entry<String, List<Atribut>> entry = constPanel.getAttributeMap().entrySet().iterator().next();
                            JSONObject jsonConstraint = new JSONObject();
                            jsonConstraint.put("table", entry.getKey());
                            if (variableCreation) {
                                jsonObject = writeQueryResult(jsonObject);
                                ComboBoxItem comboBoxItem = new ComboBoxItem(varOrQueryNameTf.getText(), "promenna", jsonObject.getString("queryResult"));
                                preparedVariableValues.add(comboBoxItem);
                                System.out.println(preparedVariableValues);
                            }
                            JSONArray attributes = new JSONArray();

                            for (Atribut att : entry.getValue()) {
                                JSONObject attribute = new JSONObject();
                                attribute.put("name", att.getName());
                                attribute.put("operator", att.getOperator());
                                attribute.put("value", att.getValue());
                                attributes.put(attribute);
                            }

                            jsonConstraint.put("attributes", attributes);
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

        JButton runQueryBtn = new JButton("Run");
        runQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String condition = "";
                String axisTable = "";
                Component[] components = centerPanel.getComponents();
                List<ConstraintPanel> constraintPanels = new ArrayList<>();
                List<String> conditions = new ArrayList<>();
                for(Component comp : components){
                    if(comp instanceof ConstraintPanel){
                        constraintPanels.add((ConstraintPanel) comp);
                    } else if (comp instanceof JPanel) {
                        JPanel axisPanel = (JPanel) comp;
                        for(Component component : axisPanel.getComponents()){
                            if(component instanceof JComboBox){
                                JComboBox cBox = (JComboBox) component;
                                axisTable = ((String) cBox.getSelectedItem()).toLowerCase();
                            }
                        }
                    }
                }
                // dotaz nad jednou tabulkou bez ohledu na osu zatím - vytváření proměnných
                if(constraintPanels.size() == 1) {
                    ConstraintPanel constraintPanel = constraintPanels.iterator().next();
                    JSONObject object = constraintPanel.getJsonConstraint();
                    String tableName = object.getString("table");
                    String query = "select * from " + tableName + " where ";

                    JSONArray atts = (JSONArray) object.get("attributes");

                    Iterator<Object> iterator = atts.iterator();
                    while(iterator.hasNext()) {
                        JSONObject jsonObject = (JSONObject) iterator.next();
                        condition = jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
                        if(jsonObject.getString("operator").equals("like")){
                            condition += "\"" + jsonObject.getString("value") + "\"";
                        } else {
                            condition += jsonObject.getString("value") ;
                        }
                        if (iterator.hasNext()) {
                            condition += " AND ";
                        }
                        query += condition;
                    }

                    //a poslat data někam dál a co s nima?
//                    switch(tableName){
//                        case "artifactView":
//                            List<ArtifactView> artifactViews = pohledDAO.nactiArtifactView(query);
//                            break;
//                        case "configurationView":
//                            List<ConfigurationView> configurationViews = pohledDAO.nactiConfigurationView(query);
//                            break;
//                        case "commitedConfigView":
//                            List<CommitedConfigView> commitedConfigViews = pohledDAO.nactiCommitedConfigView(query);
//                            break;
//                        case "commitView":
//                            List<CommitView> commitViews = pohledDAO.nactiCommitView(query);
//                            break;
//                        case "workUnitView":
//                            List<WorkUnitView> workUnitViews = pohledDAO.nactiWorkUnitView(query);
//                            break;
//                        case "fieldChangeView":
//                            List<FieldChangeView> fieldChangeViews = pohledDAO.nactiFieldChangeView(query);
//                            break;
//                        case "personView":
//                            List<PersonView> personViews = pohledDAO.nactiPersonView(query);
//                            break;
//                    }
                    System.out.println(query);

                } else {
                    // join přes více tabulek - vytváření dotazů
                    String query = "select * from " + axisTable + " ";
                    for(ConstraintPanel constraintPanel : constraintPanels) {
                        JSONObject object = constraintPanel.getJsonConstraint();
                        String tableName = object.getString("table");
                        query += "join " + tableName + " on " + tableName + ".personId = " + axisTable + ".id ";

                        JSONArray atts = (JSONArray) object.get("attributes");

                        Iterator<Object> iterator = atts.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject = (JSONObject) iterator.next();
                            condition = tableName + "." + jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
                            if (jsonObject.getString("operator").equals("like")) {
                                condition += "\"" + jsonObject.getString("value") + "\"";
                            } else {
                                condition += jsonObject.getString("value");
                            }
                            conditions.add(condition);
                        }
                    }
                    query += " where ";
                    Iterator<String> iterator = conditions.iterator();
                    while(iterator.hasNext()){
                        String cond = iterator.next();
                        query += cond;
                        if (iterator.hasNext()) {
                            query += " AND ";
                        }
                    }
                    // query se dá pustit
                    System.out.println(query);
                }
            }

        });

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        bottomPanel.add(runQueryBtn);
    }

    private JButton createButtonWithImage(String buttonOperation){
        JButton button = new JButton();
        Image img = null;
        try {
            if(buttonOperation.equals("edit") || buttonOperation.equals("delete")) {
                img = ImageIO.read(new File("zdroje/obrazky/" + buttonOperation + "Image.png"));
            }
            if (img != null){
                Image newimg = img.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(newimg));
            } else {
                if(buttonOperation.equals("edit")) {
                    button.setText("E");
                } else if (buttonOperation.equals("delete")){
                    button.setText("R");
                } else {
                    button.setText("X");
                }
            }
            button.setMargin(new Insets(1,1,1,1));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return button;
    }

    private JSONObject writeQueryResult(JSONObject jsonConstraint){
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                jsonConstraint.put("function", button.getText());
            }
        }

        Long result = testQuery();
        if(result == null) {
            jsonConstraint.put("queryResult", "NULL");
        } else {
            jsonConstraint.put("queryResult", result.toString());
        }

        return jsonConstraint;
    }

    private Long testQuery(){
        PohledDAO pohledDAO = new PohledDAO();
        String condition = "";
        String axisTable = "";
        Long result = null;
        Component[] components = centerPanel.getComponents();
        List<ConstraintPanel> constraintPanels = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        for(Component comp : components){
            if(comp instanceof ConstraintPanel){
                constraintPanels.add((ConstraintPanel) comp);
            } else if (comp instanceof JPanel) {
                JPanel axisPanel = (JPanel) comp;
                for(Component component : axisPanel.getComponents()){
                    if(component instanceof JComboBox){
                        JComboBox cBox = (JComboBox) component;
                        axisTable = ((String) cBox.getSelectedItem()).toLowerCase();
                    }
                }
            }
        }
        // dotaz nad jednou tabulkou bez ohledu na osu zatím - vytváření proměnných
        if(constraintPanels.size() == 1) {
            String function = "";
            String selectedColumn = "";
            for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    function = button.getText();
                }
            }

            ConstraintPanel constraintPanel = constraintPanels.iterator().next();
            JSONObject object = constraintPanel.getJsonConstraint();
            String tableName = object.getString("table");
            String query = "";
            for(Component comp : constraintPanel.getComponents()){
                if(comp instanceof JRadioButton){
                    JRadioButton radioButton = (JRadioButton) comp;
                    if(radioButton.isSelected()){
                        selectedColumn = radioButton.getText();
                    }
                }
            }
            if(function.equals("COUNT")) {
                query = "select count(*) from " + tableName + " where ";
            } else {
                query = "select " + function + "(" + selectedColumn + ") from " + tableName + " where ";
            }

            JSONArray atts = (JSONArray) object.get("attributes");

            Iterator<Object> iterator = atts.iterator();
            while(iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                condition = jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
                if(jsonObject.getString("operator").equals("like")){
                    condition += "\"" + jsonObject.getString("value") + "\"";
                } else {
                    condition += jsonObject.getString("value") ;
                }
                if (iterator.hasNext()) {
                    condition += " AND ";
                }
                query += condition;
            }
            result = pohledDAO.dotaz(query);
            if( result == null ) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Query result is NULL",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Query result is " + result);
            }

        } else {
            JOptionPane.showMessageDialog(mainFrame,
                    "Only 1 table queries are supported now.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
//                    // join přes více tabulek - vytváření dotazů
//                    String query = "select * from " + axisTable + " ";
//                    for(ConstraintPanel constraintPanel : constraintPanels) {
//                        JSONObject object = constraintPanel.getJsonConstraint();
//                        String tableName = object.getString("table");
//                        query += "join " + tableName + " on " + tableName + ".personId = " + axisTable + ".id ";
//
//                        JSONArray atts = (JSONArray) object.get("attributes");
//
//                        Iterator<Object> iterator = atts.iterator();
//                        while (iterator.hasNext()) {
//                            JSONObject jsonObject = (JSONObject) iterator.next();
//                            condition = tableName + "." + jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
//                            if (jsonObject.getString("operator").equals("like")) {
//                                condition += "\"" + jsonObject.getString("value") + "\"";
//                            } else {
//                                condition += jsonObject.getString("value");
//                            }
//                            conditions.add(condition);
//                            }
//                    }
//                    query += " where ";
//                    Iterator<String> iterator = conditions.iterator();
//                    while(iterator.hasNext()){
//                        String cond = iterator.next();
//                        query += cond;
//                        if (iterator.hasNext()) {
//                            query += " AND ";
//                        }
//                    }
//                    // query se dá pustit
//                    System.out.println(query);
        }
        return result;
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

            JButton editBtn = createButtonWithImage("edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    preparedVariableValues.removeIf(item -> item.getName().equals(name));
                    System.out.println(preparedVariableValues);
                    FormularVytvoreniKonstanty constantsForm = new FormularVytvoreniKonstanty(name, value);
                    String oldName = name;
                    name = constantsForm.getConstName();
                    value = constantsForm.getConstValue();
                    label.setText(constantsForm.getConstName() + ":" + constantsForm.getConstValue());
                    ComboBoxItem comboBoxItem = new ComboBoxItem(constantsForm.getConstName(), "konstanta",constantsForm.getConstValue());
                    preparedVariableValues.add(comboBoxItem);
                    System.out.println(preparedVariableValues);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    File oldFile = new File(constantFolderPath + oldName + ".json");
                    oldFile.delete();
                    File newFile = new File(constantFolderPath + name + ".json");
                    String jsonString = new JSONObject()
                            .put("name", name)
                            .put("value", value).toString(2);
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(newFile), "utf-8"));
                        writer.write(jsonString);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            writer.close();
                        } catch (Exception ex) {/*ignore*/}
                    }

                }

            });
            this.add(editBtn);

            JButton removeBtn = createButtonWithImage("delete");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    preparedVariableValues.removeIf(item -> item.getName().equals(name));
                    System.out.println(preparedVariableValues);
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

            JButton editBtn = createButtonWithImage("edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    variableCreation = true;
                    centerPanel.removeAll();
                    centerNorthPanel.removeAll();

                    centerNorthPanel.add(addConstraintBtn);
                    varOrQueryNameTf.setText(name);
                    centerNorthPanel.add(varOrQueryNameTf);
                    centerNorthPanel.add(radioSum);
                    centerNorthPanel.add(radioCount);
                    centerNorthPanel.add(radioMin);
                    centerNorthPanel.add(radioMax);
                    centerNorthPanel.add(testVarQueryBtn);

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
                        String function = object.getString("function");

                        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                            AbstractButton button = buttons.nextElement();
                            if (button.getText().equals(function)) {
                                button.setSelected(true);
                            }
                        }

                        ConstraintPanel panel = new ConstraintPanel(object);
                        centerPanel.add(panel, "dock west, height 100%, width " + constraintPanelWidth);
                        centerPanel.revalidate();
                        centerPanel.repaint();
                    }
                }

            });
            this.add(editBtn);

            JButton removeBtn = createButtonWithImage("delete");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    preparedVariableValues.removeIf(item -> item.getName().equals(name));
                    System.out.println(preparedVariableValues);
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
        Map<String, List<Atribut>> attMap;
        JSONObject constraints;

        public ConstraintPanel(JSONObject constraints) {
            super();
            thisPanel = this;
            this.constraints = constraints;
            ButtonGroup buttonGroup = new ButtonGroup();
            String tableName = constraints.getString("table");

            this.setLayout(new MigLayout());
            JLabel label = new JLabel(tableName.toUpperCase());
            this.add(label, "wrap");

            JSONArray attributes = (JSONArray) constraints.get("attributes");
            for(Object attribute: attributes){
                JSONObject jsonObject = (JSONObject) attribute;
                JRadioButton radioButton = new JRadioButton(jsonObject.getString("name"));
                buttonGroup.add(radioButton);
                radioButton.setSelected(true);
                this.add(radioButton, "wrap");
            }

            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    JSONObject constraint = getJsonConstraint();
                    FormularVytvoreniOmezeni form = new FormularVytvoreniOmezeni(strukturyPohledu, constraint, preparedVariableValues);

                    if(!form.wasClosed()) {
                        Component[] components = getComponents();
                        for (int i = 0; i < components.length; i++) {
                            if (components[i] instanceof JLabel || components[i] instanceof JRadioButton) {
                                remove(components[i]);
                            }
                        }
                        JSONObject attributes = form.getFormData();
                        if (!attributes.isEmpty()) {
                            JLabel label = new JLabel(attributes.getString("table"));
                            add(label, "wrap, dock north");
                            // delete reminder box
                            //attMap.get(entry.getKey()).remove(0);
                            setAttributes(attributes);
                            JSONArray atts = (JSONArray) attributes.get("attributes");
                            for (Object attribute : atts) {
                                JSONObject jsonObject = (JSONObject) attribute;
                                JRadioButton radioButton = new JRadioButton(jsonObject.getString("name"));
                                buttonGroup.add(radioButton);
                                radioButton.setSelected(true);
                                add(radioButton, "wrap, dock north");
                            }
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

        public void setAttributes(JSONObject constraints){
            this.constraints = constraints;
        }

        public Map<String, List<Atribut>> getAttributeMap(){
            return this.attMap;
        }

        public JSONObject getJsonConstraint() {
            return this.constraints;
        }
    }
}