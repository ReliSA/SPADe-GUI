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
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.*;

public class OknoMigLayout extends JFrame{

    public static OknoMigLayout instance;
    private static int queryPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private Projekt projekt;
    private CustomGraf graphData;
    private static String constantFolderPath = "zdroje\\konstanty\\";
    private static String queryFolderPath = "zdroje\\dotazy\\";
    private static String variableFolderPath = "zdroje\\promenne\\";
    private static JButton addConstantBtn;
    private static JButton addVariableBtn;
    private static JButton addQueryBtn;
    private static JButton createQueryBtn;
    private static JButton loadQueryBtn;
    private static JButton testVarQueryBtn;
    private static JButton runQueryBtn;
    private static JButton goBackBtn;
    private static JButton detectBtn;
    private static JButton showGraphBtn;
    private static JButton saveBtn;
    private static JButton cancelBtn;
    private static JPanel centerNorthPanel;
    private static JPanel centerPanel;
    private static JPanel centerTablePanel;
    private static JPanel bottomPanel;
    private static JPanel axisPanel;
    private static JLabel lblName = new JLabel("Name");
    private static JLabel lblFromDate = new JLabel("From:");
    private static JLabel lblToDate = new JLabel("To:");
    private static PohledDAO pohledDAO = new PohledDAO();
    private static JPanel constantsPanel;
    private static JPanel variablesPanel;
    private static JTextField varOrQueryNameTf = new JTextField(10);
    private JDatePickerImpl dpDatumOD;			//datum od
    private JDatePickerImpl dpDatumDO;			//datum do
    private static JComboBox<String> cboxAxisOptions;
    private static List<Iterace> iteraceList = new ArrayList<>();
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final Map<String, List<Sloupec>> strukturyPohledu = new TreeMap<>();
    private static List<ComboBoxItem> preparedVariableValues;
    private SloupecCustomGrafu detected;
    private List<QueryPanel> queryPanels;
    private int columnsNumber = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null , "Chyba");
                    e.printStackTrace();
                    System.exit(0);
                }
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
        if (instance != null)
            instance.dispose();
        instance = this;
        this.projekt = projekt;

        mainFrame = new JFrame();
        mainFrame.setLayout(new MigLayout());
        mainFrame.setBounds(100,100,1600,800);
        mainFrame.setVisible(true);

        addConstantBtn = new JButton("Add constant");
        addVariableBtn = new JButton("Add variable");
        addQueryBtn = new JButton("Add query");
        createQueryBtn = new JButton("Create query");
        loadQueryBtn = new JButton("Load query");
        testVarQueryBtn = new JButton("Test query");
        runQueryBtn = new JButton("Run");
        goBackBtn = new JButton("Back");
        detectBtn = new JButton("Detect");
        showGraphBtn = new JButton("Show graph");
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        centerNorthPanel = new JPanel(new MigLayout());
        centerPanel = new JPanel(new MigLayout("ins 0"));
        centerTablePanel = new JPanel(new MigLayout("gap rel 0, ins 0"));
        bottomPanel = new JPanel(new MigLayout());
        axisPanel = new JPanel(new MigLayout());
        queryPanels = new ArrayList<>();
        constantsPanel = new JPanel(new MigLayout());
        variablesPanel = new JPanel(new MigLayout());
        preparedVariableValues = new ArrayList<>();

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

        mainFrame.add(constantsPanel,"dock north");

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
                    JSONObject obj = new JSONObject(content);
                    Long variableValue = testQuery(obj);
                    ComboBoxItem comboBoxItem = new ComboBoxItem(varName, "promenna", variableValue + "");
                    preparedVariableValues.add(comboBoxItem);
                    VariablePanel varPanel = new VariablePanel(varName, variableValue, content);
                    System.out.println(preparedVariableValues);
                    variablesPanel.add(varPanel);
                    variablesPanel.revalidate();
                    variablesPanel.repaint();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        mainFrame.add(variablesPanel,"dock north");

        centerPanel.setBackground(Color.PINK);

        JScrollPane scrollFrame = new JScrollPane(centerPanel);
        centerPanel.setAutoscrolls(true);
        mainFrame.add(scrollFrame, "dock center");

        centerNorthPanel.setBackground(Color.orange);
        centerNorthPanel.add(createQueryBtn);
        centerNorthPanel.add(loadQueryBtn);
        centerPanel.add(centerNorthPanel, "dock north, width 100%");
        axisPanel.setBackground(new Color(168, 79, 25));

        cboxAxisOptions = new JComboBox<String>();
        cboxAxisOptions.addItem("Person");
        cboxAxisOptions.addItem("Iteration");
        cboxAxisOptions.addItem("Day");
        cboxAxisOptions.addItem("Week");
        cboxAxisOptions.addItem("Month");
        cboxAxisOptions.addItem("Year");
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

        bottomPanel.setBackground(Color.GRAY);
        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);

        nastavAkce();
    }

    private void nastavAkce(){
        /* Akce pro zrušení rozdělané práce a vrácení se na defaultní obrazovku */
        cancelBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                queryPanels.clear();
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

        /* Akce měnící možnosti na ose X */
        cboxAxisOptions.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                axisPanel.remove(lblFromDate);
                axisPanel.remove(dpDatumOD);
                axisPanel.remove(lblToDate);
                axisPanel.remove(dpDatumDO);
                if(!cboxAxisOptions.getSelectedItem().equals("Person") && !cboxAxisOptions.getSelectedItem().equals("Iteration")){
                    axisPanel.add(lblFromDate);
                    axisPanel.add(dpDatumOD, "wrap");
                    axisPanel.add(lblToDate);
                    axisPanel.add(dpDatumDO);
                }
                axisPanel.revalidate();
                axisPanel.repaint();
                if(queryPanels.size() > 0) {
                    JOptionPane.showMessageDialog(mainFrame, "Changing this may cause problem with joining columns", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Akce pro vrácení se na vytváření dotazu */
        goBackBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.remove(centerTablePanel);
                centerPanel.add(axisPanel, "dock west, height 800");

                for(QueryPanel panel : queryPanels){
                    centerPanel.add(panel, "dock west, width " + queryPanelWidth);
                }
                queryPanels.clear();

                bottomPanel.remove(detectBtn);
                bottomPanel.remove(showGraphBtn);
                bottomPanel.remove(goBackBtn);
                bottomPanel.add(runQueryBtn);

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        /* Akce pro uložení dotazu do souboru */
        saveBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!haveSameColumnNames(queryPanels)) {
                    JOptionPane.showMessageDialog(mainFrame, "Every column has to have an unique name.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    File file;
                    String fileName = varOrQueryNameTf.getText();
                    if (fileName.equals("")) {
                        if (variableCreation) {
                            fileName = "Variable_";
                        } else {
                            fileName = "Query_";
                        }
                        fileName += LocalDateTime.now();
                        fileName = fileName.replaceAll(":", "-");
                    }

                    JSONObject jsonObject = new JSONObject();
                    if (!variableCreation) {
                        file = new File(queryFolderPath + fileName + ".json");
                        jsonObject.put("axis", (String) cboxAxisOptions.getSelectedItem());
                        LocalDate datumOd;
                        LocalDate datumDo;
                        if (dpDatumOD.getModel().getValue() != null) {
                            datumOd = ((Date) dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        } else {
                            datumOd = projekt.getDatumPocatku();
                        }
                        if (dpDatumDO.getModel().getValue() != null) {
                            datumDo = ((Date) dpDatumDO.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        } else {
                            datumDo = LocalDate.now();
                        }
                        jsonObject.put("dateFrom", datumOd);
                        jsonObject.put("dateTo", datumDo);
                    } else {
                        file = new File(variableFolderPath + fileName + ".json");
                    }

                    Component centerComponents[] = centerPanel.getComponents();
                    List<JSONObject> constList = new ArrayList<>();

                    int panelCount = 1;
                    Long variableValue = null;

                    for (QueryPanel queryPanel : queryPanels) {
                        JSONObject query  = queryPanel.getQuery();
                        String columnName = queryPanel.getColumnName().equals("") ? "Col" + panelCount : queryPanel.getColumnName();
                        query.put("columnName", columnName);
                        if (variableCreation) {
                            Component[] components = variablesPanel.getComponents();
                            variableValue = testQuery(query);
                            ComboBoxItem temp = null;

                            for(Component comp : components){
                                if(comp instanceof VariablePanel){
                                    VariablePanel tempPanel = (VariablePanel) comp;
                                    if(tempPanel.getName().equals(fileName)){
                                        tempPanel.setLabel(fileName + " : " + variableValue);
                                    }
                                }
                            }

                            for(ComboBoxItem cbi : preparedVariableValues){
                                if(cbi.getName().equals(fileName)){
                                    temp = cbi;
                                }
                            }
                            if(temp != null){
                                preparedVariableValues.remove(temp);
                            }

                            ComboBoxItem comboBoxItem = new ComboBoxItem(fileName, "promenna", variableValue + "");
                            preparedVariableValues.add(comboBoxItem);
                            System.out.println(preparedVariableValues);
                        }
                        constList.add(query);
                        panelCount++;
                    }
                    JSONObject[] constArray = new JSONObject[constList.size()];
                    constArray = constList.toArray(constArray);
                    jsonObject.put("queries", constArray);
                    String result = jsonObject.toString(2);
                    if (variableCreation) {
                        File variableFolder = new File(variableFolderPath);
                        File[] files = variableFolder.listFiles();
                        if (files != null) {
                            List<String> fileNames = Arrays.asList(files)
                                    .stream()
                                    .map(f -> f.getName().substring(0, f.getName().indexOf('.')))
                                    .collect(Collectors.toList());
                            if (!fileNames.contains(fileName)) {
                                VariablePanel varPanel = new VariablePanel(fileName, variableValue, result);
                                variablesPanel.add(varPanel);
                                variablesPanel.revalidate();
                                variablesPanel.repaint();
                            } else {
                                Component[] components = variablesPanel.getComponents();
                                for (Component comp : components) {
                                    if (comp instanceof VariablePanel) {
                                        VariablePanel varPanel = (VariablePanel) comp;
                                        if (varPanel.getName().equals(varOrQueryNameTf.getText())) {
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
                        try {
                            writer.close();
                        } catch (Exception ex) {/*ignore*/}
                    }
                    centerNorthPanel.removeAll();
                    centerPanel.removeAll();
                    mainFrame.remove(bottomPanel);

                    queryPanels.clear();

                    varOrQueryNameTf.setText("");
                    centerNorthPanel.add(createQueryBtn);
                    centerNorthPanel.add(loadQueryBtn);
                    centerPanel.add(centerNorthPanel, "dock north, width 100%");

                    mainFrame.revalidate();
                    mainFrame.repaint();

                    JOptionPane.showMessageDialog(null, "Save successful");
                }
            }
        });

        /* Akce pro načtení uloženého dotazu ze souboru */
        loadQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.setCurrentDirectory(new File(queryFolderPath));
                int returnVal = fileChooser.showOpenDialog(centerNorthPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    centerPanel.removeAll();
                    centerNorthPanel.removeAll();

                    centerNorthPanel.add(addQueryBtn);
                    varOrQueryNameTf.setText(file.getName().substring(0, file.getName().indexOf('.')));
                    centerNorthPanel.add(varOrQueryNameTf);

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    centerPanel.add(axisPanel, "dock west, height 800, width " + queryPanelWidth);

                    try {
                        String content = FileUtils.readFileToString(file, "utf-8");
                        JSONObject obj = new JSONObject(content);

                        JComboBox axisCBox = (JComboBox) axisPanel.getComponent(0);
                        String axis = obj.getString("axis");
                        String dateTo = obj.getString("dateTo");
                        String dateFrom = obj.getString("dateFrom");
                        axisCBox.setSelectedItem(axis);
                        if(!axis.equals("Person") && !axis.equals("Iteration")){
                            axisPanel.add(lblFromDate);
                            axisPanel.add(dpDatumOD, "wrap");
                            axisPanel.add(lblToDate);
                            axisPanel.add(dpDatumDO);
                            Integer year = Integer.parseInt(dateTo.substring(0, dateTo.indexOf('-')));
                            Integer month = Integer.parseInt(dateTo.substring(dateTo.indexOf('-') + 1, dateTo.lastIndexOf('-')));
                            Integer day = Integer.parseInt(dateTo.substring(dateTo.lastIndexOf('-') + 1, dateTo.length()));
                            dpDatumDO.getModel().setDate(year, month - 1, day);
                            dpDatumDO.getModel().setSelected(true);
                            year = Integer.parseInt(dateFrom.substring(0, dateFrom.indexOf('-')));
                            month = Integer.parseInt(dateFrom.substring(dateFrom.indexOf('-') + 1, dateFrom.lastIndexOf('-')));
                            day = Integer.parseInt(dateFrom.substring(dateFrom.lastIndexOf('-') + 1, dateFrom.length()));
                            dpDatumOD.getModel().setDate(year,month - 1 ,day);
                            dpDatumOD.getModel().setSelected(true);
                        }

                        queryPanels.clear();
                        JSONArray queries = (JSONArray) obj.get("queries");
                        for (Object object : queries)
                        {
                            JSONObject query = (JSONObject) object;
                            QueryPanel panel = new QueryPanel(query, false);
                            queryPanels.add(panel);
                            centerPanel.add(panel, "dock west, height 800, width " + queryPanelWidth);
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

        /* Akce pro vytvoření proměnné */
        addVariableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                variableCreation = true;
                centerPanel.removeAll();
                centerNorthPanel.removeAll();

                centerNorthPanel.add(addQueryBtn);
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

        /* Akce pro vytvoření konstanty */
        addConstantBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FormularVytvoreniKonstanty constForm = new FormularVytvoreniKonstanty();
                if(!constForm.wasCancelled()) {
                    String constName = constForm.getConstName().equals("") ? "Constant_" + LocalDateTime.now().toString().replaceAll(":", "-") : constForm.getConstName();
                    ComboBoxItem comboBoxItem = new ComboBoxItem(constName, "konstanta",constForm.getConstValue());
                    preparedVariableValues.add(comboBoxItem);
                    System.out.println(preparedVariableValues);
                    ConstantPanel constPanel = new ConstantPanel(constName, constForm.getConstValue());
                    String jsonString = new JSONObject()
                            .put("name", constName)
                            .put("value", constForm.getConstValue()).toString(2);
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(constantFolderPath + constName + ".json"), "utf-8"));
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

        /* Akce pro přidání omezení k dotazu */
        addQueryBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                FormularVytvoreniDotazu queryForm = new FormularVytvoreniDotazu(strukturyPohledu, null, preparedVariableValues);
                JSONObject conditions = queryForm.getFormData();
                if (!conditions.isEmpty()) {
                    QueryPanel queryPanel = new QueryPanel(conditions, false);
                    centerPanel.add(queryPanel, "dock west, height 800, width " + queryPanelWidth);
                    queryPanels.add(queryPanel);
                    centerPanel.revalidate();
                }
            }
        });

        /* Akce pro otestování vrácené hodnoty při vytváření proměnné */
        testVarQueryBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(queryPanels.size() == 1) {
                    Long result = testQuery(queryPanels.get(0).getQuery());
                    JOptionPane.showMessageDialog(mainFrame, "Query result is " + result, "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "There can be only 1 query for variable creation.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Akce pro vytvoření nového dotazu */
        createQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.removeAll();
                centerNorthPanel.removeAll();

                centerNorthPanel.add(addQueryBtn);
                centerNorthPanel.add(lblName);
                centerNorthPanel.add(varOrQueryNameTf);

                centerPanel.add(centerNorthPanel, "dock north, width 100%");
                centerPanel.add(axisPanel, "dock west, height 800, width " + queryPanelWidth);

                bottomPanel.add(runQueryBtn);
                mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        /* Akce pro spuštění dotazu a načtení dat do tabulky */
        runQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!haveSameColumnNames(queryPanels)) {
                    JOptionPane.showMessageDialog(mainFrame, "Every column has to have an unique name.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (queryPanels.isEmpty()){
                    JOptionPane.showMessageDialog(mainFrame, "There has to be at least 1 query to run.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    int index = 2;
                    columnsNumber = 0;
                    queryPanels.clear();
                    String condition;
                    String axisTable = "";
                    List<String> firstColumn = new ArrayList<>();

                    Component[] components = centerPanel.getComponents();
                    for (Component comp : components) {
                        if (comp instanceof QueryPanel) {
                            queryPanels.add((QueryPanel) comp);
                        } else if (comp instanceof JPanel) {
                            JPanel axisPanel = (JPanel) comp;
                            for (Component component : axisPanel.getComponents()) {
                                if (component instanceof JComboBox) {
                                    JComboBox cBox = (JComboBox) component;
                                    axisTable = ((String) cBox.getSelectedItem());
                                }
                            }
                        }
                    }


                    graphData = new CustomGraf(queryPanels.size() + 1);
                    graphData.addNazvySloupcu(axisTable);

                    firstColumn = prepareGraphsFirstColumn(axisTable);

                    SloupecCustomGrafu sl = new SloupecCustomGrafu(axisTable, firstColumn, -1, preparedVariableValues, false);
                    centerPanel.removeAll();
                    centerTablePanel.removeAll();
                    centerPanel.add(centerNorthPanel, "dock north");
                    centerTablePanel.add(sl, "dock west, grow");
                    columnsNumber++;

                    for (QueryPanel panel : queryPanels) {
                        columnsNumber++;
                        JSONObject queryJson = panel.getQuery();
                        String tableName = queryJson.getString("table");
                        String aggregate = queryJson.getString("aggregate");
                        String agrColumn = queryJson.getString("agrColumn");
                        String joinColumn = prepareDateFormat(axisTable, queryJson.getString("joinColumn"));

                        String query = "SELECT " + joinColumn + ", " + aggregate + "(" + agrColumn + ") FROM " + tableName + " WHERE ";

                        JSONArray conditions = (JSONArray) queryJson.get("conditions");

                        Iterator<Object> iterator = conditions.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject = (JSONObject) iterator.next();
                            condition = jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
                            if (jsonObject.getString("operator").equals("like")) {
                                condition += "\"" + jsonObject.getString("value") + "\"";
                            } else {
                                condition += jsonObject.getString("value");
                            }
                            if (iterator.hasNext()) {
                                condition += " AND ";
                            }
                            query += condition;
                        }
                        query += " AND projectId = " + projekt.getID();
                        query += " GROUP BY " + joinColumn + ";";

                        System.out.println(query);
                        List<List<String>> data = pohledDAO.dotaz(query, preparedVariableValues, firstColumn);
                        String columnName = panel.getColumnName().equals("") ? "Col" + columnsNumber : panel.getColumnName();
                        SloupecCustomGrafu sloupec;
                        if (axisTable.equals("Iteration")) {
                            sloupec = mapData(data, firstColumn, columnName, true);
                        } else {
                            sloupec = mapData(data, firstColumn, columnName, false);
                        }
                        graphData.addNazvySloupcu(columnName);

                        for (String s : sloupec.getData()) {
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
            }
        });

        /* Akce pro detekci podle zadaných kritérií */
        detectBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> columnData = new ArrayList<>();
                List<List<Boolean>> detectionValues = new ArrayList<>();
                List<SloupecCustomGrafu> sloupce = new ArrayList<>();
                Component[] components = centerTablePanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof SloupecCustomGrafu){
                        SloupecCustomGrafu temp = (SloupecCustomGrafu) comp;
                        if(temp.useColum() && temp.getDetectedValue().length() != 0) {
                            sloupce.add(temp);
                        }
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

                    graphData.getDataSloupec(columnsNumber - 2).clear();
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

        /* Akce pro zobrazení grafu */
        showGraphBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(detected.getData().size() == 0){
                    JOptionPane.showMessageDialog(mainFrame,
                            "You have to detect something.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    OknoCustomGraf okno = new OknoCustomGraf(graphData, projekt);
                    okno.setSize(800, 400);
                    okno.setLocationRelativeTo(null);
                    okno.setVisible(true);
                }
            }
        });
    }

    private boolean haveSameColumnNames(List<QueryPanel> panels){
        Set<String> nameSet = new HashSet<>();
        boolean result = true;
        for(QueryPanel panel : panels){
            nameSet.add(panel.getColumnName().trim());
        }
        if(nameSet.size() < panels.size()){
            result = false;
        }
        return result;
    }

    private SloupecCustomGrafu mapData(List<List<String>> data, List<String> firstColumn, String columnName, boolean iteration){
        List<String> values = new ArrayList<>();
        boolean found = false;

        if(iteration) {
            for (Iterace iterace : iteraceList) {
                Double sum = 0.0;
                for (int i = 0; i < data.get(0).size(); i++) {
                    LocalDate temp = LocalDate.parse(data.get(0).get(i));
                    if (!temp.isBefore(iterace.getStartDate()) && !temp.isAfter(iterace.getEndDate())) {
                        sum += Double.parseDouble(data.get(1).get(i));
                    }
                }
                values.add(sum.toString());
            }
        } else {
            for (String s : firstColumn) {
                for (int i = 0; i < data.get(0).size(); i++) {
                    if (s.equals(data.get(0).get(i))) {
                        values.add(data.get(1).get(i));
                        found = true;
                    }
                }
                if (!found) {
                    values.add("0");
                }
                found = false;
            }
        }
        return new SloupecCustomGrafu(columnName, values, 1, preparedVariableValues, true);
    }

    private List<String> prepareGraphsFirstColumn(String axisTable) {
        List<String> firstColumn = new ArrayList<>();
        LocalDate datumOd;
        LocalDate datumDo;
        if (dpDatumOD.getModel().getValue() != null) {
            datumOd = ((Date) dpDatumOD.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            datumOd = projekt.getDatumPocatku();
        }
        if (dpDatumDO.getModel().getValue() != null) {
            datumDo = ((Date) dpDatumDO.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            datumDo = LocalDate.now();
        }
        switch (axisTable) {
            case "Person":
                firstColumn = pohledDAO.nactiOsobyProProjekt(projekt.getID());
                break;
            case "Iteration":
                for (Iterace iterace : iteraceList) {
                    firstColumn.add(iterace.getName());
                }
                break;
            case "Day":
                firstColumn = prepareDatesBetween(datumOd, datumDo);
                break;
            case "Week":
                firstColumn = prepareWeeksBetween(datumOd, datumDo);
                break;
            case "Month":
                firstColumn = prepareMonthsBetween(datumOd, datumDo);
                break;
            case "Year":
                firstColumn = prepareYearsBetween(datumOd, datumDo);
                break;
        }
        for (String s : firstColumn) {
            graphData.addDatum(s);
        }

        return firstColumn;
    }

    private String prepareDateFormat(String axisTable, String joinColumn) {
        if(!axisTable.equals("Person")){
            switch (axisTable){
                case "Iteration":
                case "Day":
                    joinColumn = "DATE_FORMAT(" + joinColumn + ",\"%Y-%m-%d\")";
                    break;
                case "Week":
                    joinColumn = "DATE_FORMAT(" + joinColumn + ",\"%Y-%u\")";
                    break;
                case "Month":
                    joinColumn = "DATE_FORMAT(" + joinColumn + ",\"%Y-%m\")";
                    break;
                case "Year":
                    joinColumn = "DATE_FORMAT(" + joinColumn + ",\"%Y\")";
                    break;
            }
        }
        return joinColumn;
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

    private List<String> prepareWeeksBetween(LocalDate dateFrom, LocalDate dateTo){
        List<String> totalMonths = new ArrayList<>();
        long weekCount = ChronoUnit.WEEKS.between(dateFrom, dateTo);

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int week = dateFrom.get(woy);
        int year = dateFrom.getYear();
        while (weekCount >= 0) {
            if(week == 53){
                week = 1;
                year++;
            }
            totalMonths.add(year + "-" + week);
            week++;
            weekCount--;
        }
        return totalMonths;
    }

    private List<String> prepareMonthsBetween(LocalDate dateFrom, LocalDate dateTo){
        List<String> totalMonths = new ArrayList<>();
        long monthCount = ChronoUnit.MONTHS.between(YearMonth.from(dateFrom), YearMonth.from(dateTo));
        int month = dateFrom.getMonthValue();
        int year = dateFrom.getYear();
        while (monthCount > 0) {
            if(month == 13){
                month = 1;
                year++;
            }
            totalMonths.add(year + "-" + month);
            month++;
            monthCount--;
        }
        return totalMonths;
    }

    private List<String> prepareYearsBetween(LocalDate yearFrom, LocalDate yearTo){
        List<String> totalYears = new ArrayList<>();
        for (int i = yearFrom.getYear(); i <= yearTo.getYear(); i++){
            totalYears.add(""+i);
        }
        return totalYears;
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

    private Long testQuery(JSONObject object){
        PohledDAO pohledDAO = new PohledDAO();
        String condition;
        Long result;
        JSONObject queryObject;

        if(object.has("queries")) {
            JSONArray queryArray = object.getJSONArray("queries");
            Iterator<Object> objectIterator = queryArray.iterator();
            queryObject = (JSONObject) objectIterator.next();
        } else {
            queryObject = object;
        }

        String function = queryObject.getString("aggregate");
        String tableName = queryObject.getString("table");
        String selectedColumn = queryObject.getString("agrColumn");
        String query = "select " + function + "(" + selectedColumn + ") from " + tableName + " where ";

        JSONArray conditions = (JSONArray) queryObject.get("conditions");

        Iterator<Object> iterator = conditions.iterator();
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
        result = pohledDAO.createVariable(query);

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
        Long value;
        String content;
        JLabel label;

        public VariablePanel(String varName, Long varValue, String varContent) {
            super();
            thisPanel = this;
            name = varName;
            value = varValue;
            content = varContent;
            this.setLayout(new MigLayout());
            label = new JLabel(name + " : " + varValue);
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

                    centerNorthPanel.add(addQueryBtn);
                    varOrQueryNameTf.setText(name);
                    centerNorthPanel.add(varOrQueryNameTf);
                    centerNorthPanel.add(testVarQueryBtn);

                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");

                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray queries = (JSONArray) jsonObject.get("queries");
                    for (Object queryObj : queries)
                    {
                        JSONObject query = (JSONObject) queryObj;

                        QueryPanel panel = new QueryPanel(query, true);
                        queryPanels.add(panel);
                        centerPanel.add(panel, "dock west, height 100%, width " + queryPanelWidth);
                    }

                    mainFrame.revalidate();
                    mainFrame.repaint();
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

        public void setLabel(String label){
            this.label.setText(label);
        }

        public void setContent(String content){
            this.content = content;
        }
    }

    private class QueryPanel extends JPanel {

        QueryPanel thisPanel;
        JSONObject query;
        JTextField columName;
        JButton removeBtn;
        JButton editBtn;

        public QueryPanel(JSONObject query, boolean editing) {
            super();
            thisPanel = this;
            this.query = query;
            this.setLayout(new MigLayout());

            String tableName = query.getString("table");
            String agrColumn = query.getString("agrColumn");
            String aggregate = query.getString("aggregate");
            String columnName;
            if(query.has("columnName")) {
                columnName = query.getString("columnName");
            } else {
                columnName = "";
            }

            columName = new JTextField(columnName);
            this.add(columName, "wrap, width 100%");

            JLabel label1 = new JLabel("SELECT " + aggregate + "(" + agrColumn + ")");
            this.add(label1, "wrap");
            JLabel label2 = new JLabel("FROM " + tableName);
            this.add(label2, "wrap");
            JLabel label3 = new JLabel("WHERE");
            this.add(label3, "wrap");

            JSONArray conditions = (JSONArray) query.get("conditions");
            for(Object object: conditions){
                JSONObject condition = (JSONObject) object;
                JLabel lblName = new JLabel(condition.getString("name") + " " + condition.getString("operator") + " " + condition.getString("value"));
                this.add(lblName, "wrap");
            }

            editBtn = new JButton("Edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    JSONObject tempQuery = getQuery();
                    FormularVytvoreniDotazu form = new FormularVytvoreniDotazu(strukturyPohledu, tempQuery, preparedVariableValues);
                    JSONObject query = form.getFormData();
                    String columnName;
                    if(tempQuery.has("columnName")) {
                        columnName = tempQuery.getString("columnName");
                        query.put("columnName", columnName);
                    } else {
                        columnName = "";
                    }
                    if(!form.wasClosed()) {
                        thisPanel.removeAll();
                        String tableName = query.getString("table");
                        String agrColumn = query.getString("agrColumn");
                        String aggregate = query.getString("aggregate");

                        columName = new JTextField(columnName);
                        add(columName, "wrap, width 100%");

                        JLabel label1 = new JLabel("SELECT " + aggregate + "(" + agrColumn + ")");
                        add(label1, "wrap");
                        JLabel label2 = new JLabel("FROM " + tableName);
                        add(label2, "wrap");
                        JLabel label3 = new JLabel("WHERE");
                        add(label3, "wrap");

                        if (!query.isEmpty()) {
                            setQuery(query);
                            JSONArray conditions = (JSONArray) query.get("conditions");
                            for (Object coditionObject : conditions) {
                                JSONObject condition = (JSONObject) coditionObject;
                                JLabel lblName = new JLabel(condition.getString("name") + " " + condition.getString("operator") + " " + condition.getString("value"));
                                add(lblName, "wrap");
                            }
                        }
                        add(editBtn, "wrap");
                        add(removeBtn);
                    }
                    setBorder(new EmptyBorder(10, 10, 10, 10));
                    centerPanel.revalidate();
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });

            removeBtn = new JButton("Remove");
            removeBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisPanel.getParent().remove(thisPanel);
                    queryPanels.remove(thisPanel);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }

            });

            this.add(editBtn, "wrap");
            this.add(removeBtn);
        }

        public void setQuery(JSONObject query){
            this.query = query;
        }

        public JSONObject getQuery() {
            return this.query;
        }

        public String getColumnName(){
            return columName.getText();
        }
    }
}