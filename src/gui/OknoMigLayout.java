package gui;

import data.Projekt;
import data.custom.CustomGraf;
import databaze.PohledDAO;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import ostatni.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.*;

public class OknoMigLayout extends JFrame{

    public static OknoMigLayout instance;
    private static int constraintPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private Projekt projekt;
    private CustomGraf graphData;
    private static String constantFolderPath = "zdroje\\konstanty\\";
    private static String queryFolderPath = "zdroje\\dotazy\\";
    private static String variableFolderPath = "zdroje\\promenne\\";
    private static JButton addConstantBtn = new JButton("Add constant");
    private static JButton addVariableBtn = new JButton("Add variable");
    private static JButton addConstraintBtn = new JButton("Add constraint");
    private static JButton createQueryBtn = new JButton("Create query");
    private static JButton loadQueryBtn = new JButton("Load query");
    private static JButton testVarQueryBtn = new JButton("Test query");
    private static JButton runQueryBtn = new JButton("Run");
    private static JButton goBackBtn = new JButton("Back");
    private static JButton detectBtn = new JButton("Detect");
    private static JButton showGraphBtn = new JButton("Show graph");
    private static JPanel centerNorthPanel = new JPanel(new MigLayout());
    private static JPanel centerPanel = new JPanel(new MigLayout("ins 0"));
    private static JPanel centerTablePanel = new JPanel(new MigLayout("gap rel 0, ins 0"));
    private static JPanel bottomPanel = new JPanel(new MigLayout());
    private static JPanel axisPanel = new JPanel(new MigLayout());
    private static JLabel lblName = new JLabel("Name");
    private static JLabel lblFromDate = new JLabel("From:");
    private static JLabel lblToDate = new JLabel("To:");
    private static JTextField varOrQueryNameTf = new JTextField(10);
    private JDatePickerImpl dpDatumOD;			//datum od
    private JDatePickerImpl dpDatumDO;			//datum do
    private static List<Iterace> iteraceList = new ArrayList<>();
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final Map<String, List<Sloupec>> strukturyPohledu = new TreeMap<>();
    private static List<ComboBoxItem> preparedVariableValues = new ArrayList<>();
    private SloupecCustomGrafu detected;
    private List<ConstraintPanel> constraintPanels = new ArrayList<>();
    private int columnsNumber = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OknoMigLayout frame = new OknoMigLayout(new Projekt(12, null, null ,null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OknoMigLayout(Projekt projekt) {
//        super(Konstanty.POPISY.getProperty("menuVytvorGraf"));
//        if (instance != null)
//            instance.dispose();
//        instance = this;
        this.projekt = projekt;

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

        iteraceList = pohledDAO.nactiIteraceProProjekt(projekt.getID());

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
                centerNorthPanel.add(testVarQueryBtn);

                bottomPanel.remove(runQueryBtn);
                bottomPanel.remove(detectBtn);
                bottomPanel.remove(showGraphBtn);
                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        mainFrame.add(variablesPanel,"dock north");

        centerPanel.setBackground(Color.PINK);

        JScrollPane scrollFrame = new JScrollPane(centerPanel);
        centerPanel.setAutoscrolls(true);
        mainFrame.add(scrollFrame, "dock center");

        centerNorthPanel.setBackground(Color.orange);

        addConstraintBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FormularVytvoreniOmezeni constraintForm = new FormularVytvoreniOmezeni(strukturyPohledu, null, preparedVariableValues);
                JSONObject attributes = constraintForm.getFormData();
                if (!attributes.isEmpty()) {
                    centerPanel.add(new ConstraintPanel(attributes, false), "dock west, height 800, width " + constraintPanelWidth);
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

                centerPanel.add(centerNorthPanel, "dock north, width 100%");
                centerPanel.add(axisPanel, "dock west, height 800, width " + constraintPanelWidth);

                bottomPanel.add(runQueryBtn);
                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();
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

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    centerPanel.add(axisPanel, "dock west, height 800, width " + constraintPanelWidth);

                    try {
                        String content = FileUtils.readFileToString(file, "utf-8");
                        JSONObject obj = new JSONObject(content);

                        JComboBox axisCBox = (JComboBox) axisPanel.getComponent(0);
                        axisCBox.setSelectedItem(obj.getString("axis"));

                        JSONArray constraints = (JSONArray) obj.get("constraints");
                        for (Object cons : constraints)
                        {
                            JSONObject object = (JSONObject) cons;
                            ConstraintPanel panel = new ConstraintPanel(object, false);
                            centerPanel.add(panel, "dock west, height 800, width " + constraintPanelWidth);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                        }
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }

                    bottomPanel.add(runQueryBtn);
                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    mainFrame.revalidate();
                    mainFrame.repaint();
                } else {
                    // user cancelled the action - nothing happens
                }
            }

        });

        centerNorthPanel.add(createQueryBtn);
        centerNorthPanel.add(loadQueryBtn);

        centerPanel.add(centerNorthPanel, "dock north, width 100%");


        axisPanel.setBackground(new Color(168, 79, 25));

        JComboBox<String> cboxAxisOptions = new JComboBox<String>();
        cboxAxisOptions.addItem("Person");
        cboxAxisOptions.addItem("Day");
        cboxAxisOptions.addItem("Between");
        for(Iterace iterace : iteraceList){
            cboxAxisOptions.addItem(iterace.getName());
        }
        axisPanel.add(cboxAxisOptions, "width 90%, wrap, span 3");

        UtilDateModel modelDatumOD = new UtilDateModel();
        UtilDateModel modelDatumDO = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datumODPanel = new JDatePanelImpl(modelDatumOD, p);
        JDatePanelImpl datumDOPanel = new JDatePanelImpl(modelDatumDO, p);
        dpDatumOD = new JDatePickerImpl(datumODPanel, new DateComponentFormatter());
        dpDatumDO = new JDatePickerImpl(datumDOPanel, new DateComponentFormatter());

        cboxAxisOptions.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                axisPanel.remove(lblFromDate);
                axisPanel.remove(dpDatumOD);
                axisPanel.remove(lblToDate);
                axisPanel.remove(dpDatumDO);
                if(cboxAxisOptions.getSelectedItem().equals("Day")){
                    axisPanel.add(lblFromDate);
                    axisPanel.add(dpDatumOD);
                }else if(cboxAxisOptions.getSelectedItem().equals("Between")){
                    axisPanel.add(lblFromDate);
                    axisPanel.add(dpDatumOD, "wrap");
                    axisPanel.add(lblToDate);
                    axisPanel.add(dpDatumDO);
                }
                axisPanel.revalidate();
                axisPanel.repaint();
            }
        });

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
                        JSONObject jsonConstraint = constPanel.getJsonConstraint();
                        if(variableCreation){
                            jsonObject = writeQueryResult(jsonObject);
                            ComboBoxItem comboBoxItem = new ComboBoxItem(varOrQueryNameTf.getText(), "promenna", jsonObject.getString("queryResult"));
                            preparedVariableValues.add(comboBoxItem);
                            System.out.println(preparedVariableValues);
                        }
                        constList.add(jsonConstraint);
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
                bottomPanel.removeAll();
                bottomPanel.add(cancelBtn);
                bottomPanel.add(saveBtn);
                mainFrame.remove(bottomPanel);

                varOrQueryNameTf.setText("");
                centerNorthPanel.add(createQueryBtn);
                centerNorthPanel.add(loadQueryBtn);
                centerPanel.add(centerNorthPanel, "dock north, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        goBackBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.remove(centerTablePanel);
                centerPanel.add(axisPanel, "dock west, height 800");

                for(ConstraintPanel panel : constraintPanels){
                    centerPanel.add(panel, "dock west, width " + constraintPanelWidth);
                }
                constraintPanels.clear();

                bottomPanel.remove(detectBtn);
                bottomPanel.remove(showGraphBtn);
                bottomPanel.remove(goBackBtn);
                bottomPanel.add(runQueryBtn);

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });


        runQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 2;
                columnsNumber = 0;
                constraintPanels.clear();
                String condition = "";
                String axisTable = "";
                List<String> firstColumn = new ArrayList<>();
                boolean isDate = true;

                Component[] components = centerPanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof ConstraintPanel){
                        constraintPanels.add((ConstraintPanel) comp);
                    } else if (comp instanceof JPanel) {
                        JPanel axisPanel = (JPanel) comp;
                        for(Component component : axisPanel.getComponents()){
                            if(component instanceof JComboBox){
                                JComboBox cBox = (JComboBox) component;
                                axisTable = ((String) cBox.getSelectedItem());
                            }
                        }
                    }
                }


                graphData = new CustomGraf(constraintPanels.size() + 1);
                graphData.addNazvySloupcu("osa X");

                if(axisTable.equals("Person")){
                    isDate = false;
                    firstColumn = pohledDAO.nactiOsobyProProjekt(projekt.getID());
                    for(String s : firstColumn){
                        graphData.addDatum(s);
                    }
                } else if (axisTable.equals("Day")) {
                    LocalDate datum;
                    if(dpDatumOD.getModel().getValue() != null){
                        datum = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        firstColumn.add(datum.toString());
                    } else {
                        firstColumn.add(LocalDate.now().toString());
                    }

                    for(String s : firstColumn){
                        graphData.addDatum(s);
                    }
                } else if (axisTable.equals("Between")) {
                    LocalDate datumOd = null;
                    LocalDate datumDo = null;
                    if(dpDatumOD.getModel().getValue() != null){
                        datumOd = ((Date)dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } else {
                        datumOd = LocalDate.now();
                    }
                    if(dpDatumDO.getModel().getValue() != null){
                        datumDo = ((Date)dpDatumDO.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } else {
                        datumDo = LocalDate.now().plusDays(1);
                    }
                    firstColumn = prepareDatesBetween(datumOd, datumDo);

                    for(String s : firstColumn){
                        graphData.addDatum(s);
                    }
                } else {
                    Iterace temp = null;
                    for(Iterace iterace : iteraceList){
                        if (iterace.getName().equals(axisTable)){
                            temp = iterace;
                            break;
                        }
                    }

                    firstColumn = prepareDatesForIteration(temp);
                    for (String s : firstColumn) {
                        graphData.addDatum(s);
                    }
                }

                SloupecCustomGrafu sl = new SloupecCustomGrafu(axisTable + " name", firstColumn, -1, preparedVariableValues, false);
                centerPanel.removeAll();
                centerTablePanel.removeAll();
                centerPanel.add(centerNorthPanel, "dock north");
                centerTablePanel.add(sl, "dock west, grow");
                columnsNumber++;

                // dotaz nad jednou tabulkou bez ohledu na osu zatím - vytváření proměnných
                for(ConstraintPanel panel : constraintPanels) {
                    columnsNumber++;
                    JSONObject object = panel.getJsonConstraint();
                    String tableName = object.getString("table");
                    String aggregate = object.getString("aggregate");
                    String column = object.getString("column");
                    String selection = "";

//                    den, týden, měsíc, kvartál, rok - den - 1 date picker; týden a měsíc ne - 2 date pickery od do a je to; kavartál a rok nevím
                    if(axisTable.equals("Person")) {
                        selection = "personName";
                    } else if(axisTable.equals("Day")){

                    } else {
                        List<Sloupec> sloupce = strukturyPohledu.get(tableName);
                        for(Sloupec sloupec : sloupce){
                            if(sloupec.getType().equals("datetime") || sloupec.getType().equals("date") ){
                                selection = sloupec.getName();
                                break;
                            }
                        }
                    }

                    String query = "SELECT " + selection + ", " + aggregate + "(" + column + ") FROM " + tableName + " WHERE ";

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
                    query += " AND projectId = " + projekt.getID();
                    query += " GROUP BY " + selection + ";";

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
                    SloupecCustomGrafu sloupec = pohledDAO.dotaz(query, preparedVariableValues, firstColumn, isDate);
                    graphData.addNazvySloupcu(sloupec.getName());

                    for(String s : sloupec.getData()){
                        graphData.addData(index, Double.parseDouble(s));
                    }
                    index++;

                    centerTablePanel.add(sloupec, "dock west, grow");
                }
                columnsNumber++;
                detected = new SloupecCustomGrafu("detected", new ArrayList<>(), columnsNumber, preparedVariableValues, false);
                centerTablePanel.add(detected, "dock west, grow");

                graphData.addNazvySloupcu("detected");

                centerPanel.add(centerTablePanel, "grow");
                bottomPanel.remove(runQueryBtn);
                bottomPanel.add(detectBtn);
                bottomPanel.add(goBackBtn);
                bottomPanel.add(showGraphBtn);
                mainFrame.revalidate();
                mainFrame.repaint();
            }

        });

        detectBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> columnData = new ArrayList<>();
                List<List<Boolean>> detectionValues = new ArrayList<>();
                List<SloupecCustomGrafu> sloupce = new ArrayList<>();
                Component[] components = centerTablePanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof SloupecCustomGrafu){
                        sloupce.add((SloupecCustomGrafu) comp);
                    }
                }
                if(!sloupce.isEmpty()){
                    for(SloupecCustomGrafu sloupec : sloupce) {
                        if (sloupec.useColum()) {
                            detectionValues.add(sloupec.detectValues());
                        }
                    }

                    for (int i = 0; i < detectionValues.get(0).size(); i++) {
                        Boolean val = true;
                        for (int j = 0; j < detectionValues.size(); j++) {
                            val &= detectionValues.get(j).get(i);
                        }
                        if(val){
                            columnData.add("1");
                        } else {
                            columnData.add("0");
                        }
                    }
                    centerTablePanel.remove(centerTablePanel.getComponentCount() - 1);

                    detected = new SloupecCustomGrafu("detected", columnData, columnsNumber, preparedVariableValues, false);
                    centerTablePanel.add(detected, "dock west, grow");

                    for(String s : columnData){
                        graphData.addData(columnsNumber, Double.parseDouble(s));
                    }

                    centerTablePanel.revalidate();
                    centerTablePanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "No column for detection selected.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        showGraphBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                OknoCustomGraf okno = new OknoCustomGraf(graphData, projekt);
                okno.setSize(800, 400);
                okno.setLocationRelativeTo(null);
                okno.setVisible(true);
            }
        });

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        bottomPanel.add(runQueryBtn);
    }

    private List<String> prepareDatesForIteration(Iterace iterace){
        List<String> totalDates = new ArrayList<>();
        LocalDate start = LocalDate.parse(iterace.getStartDate().toString());
        LocalDate end = LocalDate.parse(iterace.getEndDate().toString());
        while (!start.isAfter(end)) {
            totalDates.add(start.toString());
            start = start.plusDays(1);
        }
        return totalDates;
    }

    private List<String> prepareDatesBetween(LocalDate datumOd, LocalDate datumDo){
        List<String> totalDates = new ArrayList<>();
        while (!datumOd.isAfter(datumDo)) {
            totalDates.add(datumOd.toString());
            datumOd = datumOd.plusDays(1);
        }
        return totalDates;
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
        String condition;
        Long result = null;
        Component[] components = centerPanel.getComponents();
        List<ConstraintPanel> constraintPanels = new ArrayList<>();
        for(Component comp : components){
            if(comp instanceof ConstraintPanel){
                constraintPanels.add((ConstraintPanel) comp);
            }
        }
        // dotaz nad jednou tabulkou bez ohledu na osu - vytváření proměnných
        if(constraintPanels.size() == 1) {
            ConstraintPanel constraintPanel = constraintPanels.iterator().next();
            JSONObject object = constraintPanel.getJsonConstraint();

            String function = object.getString("aggregate");
            String tableName = object.getString("table");
            String selectedColumn = object.getString("column");
            String query = "select " + function + "(" + selectedColumn + ") from " + tableName + " where ";

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
            result = pohledDAO.createVariable(query);
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
                    centerNorthPanel.add(testVarQueryBtn);

                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");

                    centerPanel.revalidate();
                    centerPanel.repaint();

                    centerNorthPanel.revalidate();
                    centerNorthPanel.repaint();

                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray constraints = (JSONArray) jsonObject.get("constraints");
                    for (Object cons : constraints)
                    {
                        JSONObject object = (JSONObject) cons;

                        ConstraintPanel panel = new ConstraintPanel(object, true);
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
        JSONObject constraints;

        public ConstraintPanel(JSONObject constraints, boolean editing) {
            super();
            thisPanel = this;
            this.constraints = constraints;
            String tableName = constraints.getString("table");

            this.setLayout(new MigLayout());
            JLabel label = new JLabel(tableName.toUpperCase());
            this.add(label, "wrap");

            JSONArray attributes = (JSONArray) constraints.get("attributes");
            for(Object attribute: attributes){
                JSONObject jsonObject = (JSONObject) attribute;
                JLabel lblName = new JLabel(jsonObject.getString("name") + jsonObject.getString("operator") + jsonObject.getString("value"));
                this.add(lblName, "wrap");
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
                            if (components[i] instanceof JLabel) {
                                remove(components[i]);
                            }
                        }
                        JSONObject attributes = form.getFormData();
                        if (!attributes.isEmpty()) {
                            JLabel label = new JLabel(attributes.getString("table").toUpperCase());
                            add(label, "wrap, dock north");
                            setAttributes(attributes);
                            JSONArray atts = (JSONArray) attributes.get("attributes");
                            for (Object attribute : atts) {
                                JSONObject jsonObject = (JSONObject) attribute;
                                JLabel lblName = new JLabel(jsonObject.getString("name") + jsonObject.getString("operator") + jsonObject.getString("value"));
                                add(lblName, "wrap, dock north");
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

        public JSONObject getJsonConstraint() {
            return this.constraints;
        }
    }
}