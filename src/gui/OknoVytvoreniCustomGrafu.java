package gui;

import data.Projekt;
import data.custom.CustomGraf;
import databaze.IPohledDAO;
import databaze.PohledDAO;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import ostatni.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
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

/**
 * Okno pro vytváření grafů
 */
public class OknoVytvoreniCustomGrafu extends JFrame{

    public static OknoVytvoreniCustomGrafu instance;
    private static int queryPanelWidth = 200;
    private static boolean variableCreation = false;
    private static JFrame mainFrame;
    private Projekt projekt;
    private CustomGraf graphData;
    private static String constantFolderPath = "res\\konstanty\\";
    private static String queryFolderPath = "res\\dotazy\\";
    private static String variableFolderPath = "res\\promenne\\";
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
    private static JButton exportBtn;
    private static JScrollPane scrollPanelNorth;
    private static JPanel northPanel;
    private static JPanel centerNorthPanel;
    private static JPanel centerPanel;
    private static JPanel centerTablePanel;
    private static JPanel bottomPanel;
    private static JPanel axisPanel;
    private static JLabel lblName = new JLabel(Konstanty.POPISY.getProperty("popisNazev"));
    private static JLabel lblFromDate = new JLabel(Konstanty.POPISY.getProperty("popisOd"));
    private static JLabel lblToDate = new JLabel(Konstanty.POPISY.getProperty("popisDo"));
    private static IPohledDAO pohledDAO = new PohledDAO();
    private static JPanel constantsPanel;
    private static JPanel variablesPanel;
    private static JTextField varOrQueryNameTf = new JTextField(10);
    private JDatePickerImpl dpDatumOD;			//datum od
    private JDatePickerImpl dpDatumDO;			//datum do
    private static JComboBox<String> cboxAxisOptions;
    private static List<Iteration> iterationList = new ArrayList<>();
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final Map<String, List<Sloupec>> strukturyPohledu = new TreeMap<>();
    private static List<ComboBoxItem> preparedVariableValues;
    private SloupecCustomGrafu detected;
    private SloupecCustomGrafu axisColumn;
    private List<QueryPanel> queryPanels;
    private List<SloupecCustomGrafu> sloupceCustomGrafu;
    private int columnsNumber = 0;
    private static boolean doInvertValues = false;
    static Logger log = Logger.getLogger(OknoVytvoreniCustomGrafu.class);

    /* For testing only - remove in final version*/
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
                    OknoVytvoreniCustomGrafu frame = new OknoVytvoreniCustomGrafu(new Projekt(12, null, null ,null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Konstruktor hlavního okna
     * @param projekt zvolený projekt
     */
    public OknoVytvoreniCustomGrafu(Projekt projekt) {
//        super(Konstanty.POPISY.getProperty("menuVytvorGraf"));
        if (instance != null)
            instance.dispose();
        instance = this;
        this.projekt = projekt;

        mainFrame = new JFrame();
        mainFrame.setLayout(new MigLayout());
        mainFrame.setBounds(100,100,1600,800);
        mainFrame.setVisible(true);

        mainFrame.setTitle(Konstanty.POPISY.getProperty("menuVytvorGraf"));
        mainFrame.setIconImage(Konstanty.IMG_ICON.getImage());

        addConstantBtn = new JButton(Konstanty.POPISY.getProperty("pridejKonstantu"));
        addVariableBtn = new JButton(Konstanty.POPISY.getProperty("pridejPromennou"));
        addQueryBtn = new JButton(Konstanty.POPISY.getProperty("pridejDotaz"));
        createQueryBtn = new JButton(Konstanty.POPISY.getProperty("vytvorDotaz"));
        loadQueryBtn = new JButton(Konstanty.POPISY.getProperty("nactiDotaz"));
        testVarQueryBtn = new JButton(Konstanty.POPISY.getProperty("otestujDotaz"));
        runQueryBtn = new JButton(Konstanty.POPISY.getProperty("spust"));
        goBackBtn = new JButton(Konstanty.POPISY.getProperty("zpet"));
        detectBtn = new JButton(Konstanty.POPISY.getProperty("detekuj"));
        showGraphBtn = new JButton(Konstanty.POPISY.getProperty("ukazGraf"));
        saveBtn = new JButton(Konstanty.POPISY.getProperty("uloz"));
        cancelBtn = new JButton(Konstanty.POPISY.getProperty("tlacitkoZrusit"));
        exportBtn = new JButton(Konstanty.POPISY.getProperty("exportCSV"));
        northPanel = new JPanel(new MigLayout("ins 0"));
        centerNorthPanel = new JPanel(new MigLayout());
        centerPanel = new JPanel(new MigLayout("ins 0"));
        centerTablePanel = new JPanel(new MigLayout("gap rel 0, ins 0"));
        bottomPanel = new JPanel(new MigLayout());
        axisPanel = new JPanel(new MigLayout());
        queryPanels = new ArrayList<>();
        constantsPanel = new JPanel(new MigLayout("aligny center"));
        variablesPanel = new JPanel(new MigLayout("aligny center"));
        preparedVariableValues = new ArrayList<>();
        sloupceCustomGrafu = new ArrayList<>();

        PohledEnum[] pohledy = PohledEnum.values();
        List<Sloupec> sloupce;

        for(PohledEnum pohled : pohledy){
            sloupce = pohledDAO.getViewStructure(pohled.getViewName());
            strukturyPohledu.put(pohled.getViewName(), sloupce);
        }

        iterationList = pohledDAO.getIterationsForProject(projekt.getID());

//        List<ArtifactView> artifactViews = pohledDAO.nactiArtifactView();
//        List<CommitedConfigView> commitedConfigViews = pohledDAO.nactiCommitedConfigView();
//        List<CommitView> commitViews = pohledDAO.nactiCommitView();
//        List<ConfigurationView> configurationViews = pohledDAO.nactiConfigurationView();
//        List<FieldChangeView> fieldChangeViews = pohledDAO.nactiFieldChangeView();
//        List<WorkUnitView> workUnitViews = pohledDAO.nactiWorkUnitView();

        constantsPanel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
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
                    log.debug(preparedVariableValues);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            constantsPanel.revalidate();
            constantsPanel.repaint();
        }

        constantsPanel.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
        northPanel.add(constantsPanel,"dock north, w 100%, h 55");

        variablesPanel.add(addVariableBtn, "align 50% 50%");

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
                    log.debug(preparedVariableValues);
                    variablesPanel.add(varPanel);
                    variablesPanel.revalidate();
                    variablesPanel.repaint();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        northPanel.add(variablesPanel,"dock north, h 60");
        scrollPanelNorth = new JScrollPane(northPanel);
        scrollPanelNorth.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanelNorth.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        mainFrame.add(scrollPanelNorth, "dock north, h 125");

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainFrame.add(scrollPane, "dock center");

        centerNorthPanel.setBorder(new MatteBorder(0,0,2,0, Color.BLACK));
        centerNorthPanel.add(createQueryBtn);
        centerNorthPanel.add(loadQueryBtn);
        centerPanel.add(centerNorthPanel, "dock north, width 100%");
        axisPanel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));
        axisPanel.setBackground(new Color(200, 200, 200));

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

        bottomPanel.setBorder(new MatteBorder(2,0,0,0, Color.BLACK));
        bottomPanel.add(cancelBtn);

        nastavAkce();
    }

    /**
     * Nastavení akcí pro ovládací prvky
     */
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
                mainFrame.remove(bottomPanel);

                varOrQueryNameTf.setText("");
                sloupceCustomGrafu.clear();
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
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textZmenaOsy"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Akce pro vrácení se na vytváření dotazu */
        goBackBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.remove(centerTablePanel);
                centerPanel.add(axisPanel, "dock west, h 555");

                for(QueryPanel panel : queryPanels){
                    centerPanel.add(panel, "dock west, h 555, width " + queryPanelWidth);
                }
//                queryPanels.clear();
                sloupceCustomGrafu.clear();

                bottomPanel.removeAll();
                bottomPanel.add(cancelBtn);
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
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textUnikatniNazev"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
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
                        jsonObject.put("invertValues", detected.doInvertValues());
                    } else {
                        file = new File(variableFolderPath + fileName + ".json");
                    }

                    Component centerComponents[] = centerPanel.getComponents();
                    List<JSONObject> queryList = new ArrayList<>();

                    int panelCount = 1;
                    Long variableValue = null;

                    for (QueryPanel queryPanel : queryPanels) {
                        JSONObject query  = queryPanel.getQuery();
                        final String columnName = queryPanel.getColumnName().equals("") ? "Col" + panelCount : queryPanel.getColumnName();
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
                            log.debug(preparedVariableValues);
                            queryList.add(query);
                            panelCount++;
                        } else {

                            SloupecCustomGrafu sloupec = sloupceCustomGrafu.stream()
                                    .filter(sl -> columnName.equals(sl.getName())).findAny().orElse(null);
                            JSONObject detection = new JSONObject();
                            detection.put("operator", sloupec.getOperator());
                            detection.put("detect", sloupec.useColum());
                            detection.put("compareColumns", sloupec.compareColumns());
                            if (sloupec.compareColumns()) {
                                detection.put("column1", sloupec.getCboxColumns().getSelectedItem());
                                if (sloupec.isBetween()) {
                                    detection.put("column2", sloupec.getCboxColumns2().getSelectedItem());
                                }
                            } else {
                                JSONObject firstInput = new JSONObject();
                                firstInput.put("value1", sloupec.getComboBoxValue(sloupec.getCboxVariableValues()));
                                if (!sloupec.getArithmeticOperator(sloupec.getCboxAritmethics()).equals("")) {
                                    firstInput.put("arithmetic", sloupec.getArithmeticOperator(sloupec.getCboxAritmethics()));
                                    firstInput.put("value2", sloupec.getComboBoxValue(sloupec.getCboxVariableValues2()));
                                }
                                detection.put("firstInput", firstInput);
                                if (sloupec.isBetween()) {
                                    JSONObject secondInput = new JSONObject();
                                    secondInput.put("value1", sloupec.getComboBoxValue(sloupec.getCboxVariableValues3()));
                                    if (!sloupec.getArithmeticOperator(sloupec.getCboxAritmethics2()).equals("")) {
                                        secondInput.put("arithmetic", sloupec.getArithmeticOperator(sloupec.getCboxAritmethics2()));
                                        secondInput.put("value2", sloupec.getComboBoxValue(sloupec.getCboxVariableValues4()));
                                    }
                                    detection.put("secondInput", secondInput);
                                }
                            }

                            query.put("detection", detection);
                            queryList.add(query);
                            panelCount++;
                        }
                    }
                    JSONObject[] queryArray = new JSONObject[queryList.size()];
                    queryArray = queryList.toArray(queryArray);
                    jsonObject.put("queries", queryArray);

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
                    bottomPanel.removeAll();
                    mainFrame.remove(bottomPanel);

                    queryPanels.clear();

                    varOrQueryNameTf.setText("");
                    centerNorthPanel.add(createQueryBtn);
                    centerNorthPanel.add(loadQueryBtn);
                    centerPanel.add(centerNorthPanel, "dock north, width 100%");

                    mainFrame.revalidate();
                    mainFrame.repaint();

                    JOptionPane.showMessageDialog(null, Konstanty.POPISY.getProperty("uspesneUlozeno"));
                }
            }
        });

        /* Akce pro načtení uloženého dotazu ze souboru */
        loadQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                doInvertValues = false;
                fileChooser.setCurrentDirectory(new File(queryFolderPath));
                int returnVal = fileChooser.showOpenDialog(centerNorthPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    centerPanel.removeAll();
                    centerNorthPanel.removeAll();

                    centerNorthPanel.add(addQueryBtn);
                    varOrQueryNameTf.setText(file.getName().substring(0, file.getName().indexOf('.')));
                    centerNorthPanel.add(varOrQueryNameTf, "width 10%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    centerPanel.add(axisPanel, "dock west, h 555, width " + queryPanelWidth);

                    try {
                        String content = FileUtils.readFileToString(file, "utf-8");
                        JSONObject obj = new JSONObject(content);

                        JComboBox axisCBox = (JComboBox) axisPanel.getComponent(0);
                        String axis = obj.getString("axis");
                        String dateTo = obj.getString("dateTo");
                        String dateFrom = obj.getString("dateFrom");
                        doInvertValues = obj.getBoolean("invertValues");
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
                            QueryPanel panel = new QueryPanel(query);
                            panel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));
                            queryPanels.add(panel);
                            centerPanel.add(panel, "dock west, grow, width " + queryPanelWidth);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                        }
                    } catch(IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textNelzeNacist"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                        log.warn(ex);
                    }

                    bottomPanel.add(cancelBtn);
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
                centerNorthPanel.add(varOrQueryNameTf, "width 10%");
                centerNorthPanel.add(testVarQueryBtn);

                bottomPanel.remove(runQueryBtn);
                bottomPanel.remove(detectBtn);
                bottomPanel.remove(showGraphBtn);
                bottomPanel.add(saveBtn);
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
                boolean replace = false;
                if(!constForm.wasCancelled()) {
                    String constName = constForm.getConstName().equals("") ? "Constant_" + LocalDateTime.now().toString().replaceAll(":", "-") : constForm.getConstName();
                    preparedVariableValues.removeIf(item -> item.getName().equals(constName));
                    ComboBoxItem comboBoxItem = new ComboBoxItem(constName, "konstanta",constForm.getConstValue());
                    preparedVariableValues.add(comboBoxItem);
                    log.debug(preparedVariableValues);
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

                    for(Component comp : constantsPanel.getComponents()){
                        if(comp instanceof ConstantPanel){
                            ConstantPanel constantPanel = (ConstantPanel) comp;
                            if(constName.equals(constantPanel.getName())){
                                constantPanel.setValue(constForm.getConstValue());
                                constantPanel.repaint();
                                replace = true;
                            }
                        }
                    }
                    if(!replace) {
                        constantsPanel.add(constPanel);
                    }
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
                    QueryPanel queryPanel = new QueryPanel(conditions);
                    queryPanel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));
                    centerPanel.add(queryPanel, "dock west, h 555, width " + queryPanelWidth);
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
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textVysledekDotazu") + result, Konstanty.POPISY.getProperty("info"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textPouzeJedenDotaz"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Akce pro vytvoření nového dotazu */
        createQueryBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                doInvertValues = false;
                centerPanel.removeAll();
                centerNorthPanel.removeAll();

                centerNorthPanel.add(addQueryBtn);
                centerNorthPanel.add(lblName);
                centerNorthPanel.add(varOrQueryNameTf, "width 10%");

                centerPanel.add(centerNorthPanel, "dock north, width 100%");
                centerPanel.add(axisPanel, "dock west, h 555, width " + queryPanelWidth);

                bottomPanel.add(cancelBtn);
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
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textUnikatniNazev"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                } else if (queryPanels.isEmpty()){
                    JOptionPane.showMessageDialog(mainFrame, Konstanty.POPISY.getProperty("textAlesponJedenDotaz"), Konstanty.POPISY.getProperty("upozorneni"), JOptionPane.WARNING_MESSAGE);
                } else {
                    int index = 2;
                    columnsNumber = 0;
                    queryPanels.clear();
                    String condition;
                    String axisTable = "";
                    List<String> firstColumn = new ArrayList<>();
                    List<String> columnNames = new ArrayList<>();
                    boolean doDetect = false;

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

                    for(QueryPanel panel : queryPanels){
                        columnNames.add(panel.getColumnName());
                    }

                    graphData = new CustomGraf(queryPanels.size() + 1);
                    graphData.addNazvySloupcu(axisTable);

                    firstColumn = prepareGraphsFirstColumn(axisTable);

                    axisColumn = new SloupecCustomGrafu(axisTable, firstColumn, -1, preparedVariableValues, false, columnNames, null, false);
                    centerPanel.removeAll();
                    centerTablePanel.removeAll();
                    centerPanel.add(centerNorthPanel, "dock north, width 100%");
                    centerTablePanel.add(axisColumn, "dock west, grow");
                    columnsNumber++;

                    for (QueryPanel panel : queryPanels) {
                        columnsNumber++;
                        String logHeader = "\n==============================\ncolumn: " + panel.getName() + "\n==============================\n";

                        JSONObject queryJson = panel.getQuery();
                        String tableName = queryJson.getString("table");
                        String aggregate = queryJson.getString("aggregate");
                        String agrColumn = queryJson.getString("agrColumn");
                        String joinColumn = prepareDateFormat(axisTable, queryJson.getString("joinColumn"));

                        String query = "SELECT\n\t" + joinColumn + ", " + aggregate + "(" + agrColumn + ")\nFROM\n\t" + tableName + "\nWHERE\n\t";

                        JSONArray conditions = (JSONArray) queryJson.get("conditions");

                        Iterator<Object> iterator = conditions.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject = (JSONObject) iterator.next();
                            condition = jsonObject.getString("name") + " " + jsonObject.getString("operator") + " ";
                            if (jsonObject.getString("operator").equals("like") || jsonObject.getString("operator").equals("not like")) {
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
                        query += "\nGROUP BY\n\t" + joinColumn + ";";

                        log.info(logHeader + query);
                        List<List<String>> data = pohledDAO.runQuery(query);
                        String columnName = panel.getColumnName().equals("") ? "Col" + columnsNumber : panel.getColumnName();
                        SloupecCustomGrafu sloupec;
                        if (axisTable.equals("Iteration")) {
                            sloupec = mapData(data, firstColumn, columnName, true, columnNames, panel.getQuery());
                        } else {
                            sloupec = mapData(data, firstColumn, columnName, false, columnNames, panel.getQuery());
                        }
                        graphData.addNazvySloupcu(columnName);

                        sloupceCustomGrafu.add(sloupec);

                        if (panel.getQuery().has("detection")) {
                            doDetect = panel.getQuery().getJSONObject("detection").getBoolean("detect");
                        }

                        for (String s : sloupec.getData()) {
                            graphData.addData(index, Double.parseDouble(s));
                        }
                        index++;

                        centerTablePanel.add(sloupec, "dock west, width 240");
                    }
                    columnsNumber++;
                    detected = new SloupecCustomGrafu("detected", new ArrayList<>(), columnsNumber, preparedVariableValues, false, columnNames, null, true);
                    centerTablePanel.add(detected, "dock west, grow");

                    graphData.addNazvySloupcu("detected");

                    centerPanel.add(centerTablePanel, "grow");
                    bottomPanel.remove(runQueryBtn);
                    bottomPanel.add(detectBtn);
                    if (doDetect){
                        detectBtn.doClick();
                    }
                    if (doInvertValues){
                        detected.invertValues();
                        doInvertValues = false;
                    }
                    bottomPanel.add(goBackBtn);
                    bottomPanel.add(showGraphBtn);
                    bottomPanel.add(saveBtn);
                    bottomPanel.add(exportBtn);
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
                List<SloupecCustomGrafu> columnsToDetect = new ArrayList<>();
                List<SloupecCustomGrafu> allColumns = new ArrayList<>();
                List<String> columnNames = new ArrayList<>();
                for(QueryPanel panel : queryPanels){
                    columnNames.add(panel.getColumnName());
                }
                Component[] components = centerTablePanel.getComponents();
                for(Component comp : components){
                    if(comp instanceof SloupecCustomGrafu){
                        SloupecCustomGrafu temp = (SloupecCustomGrafu) comp;
                        allColumns.add(temp);
                        if(temp.useColum()) {
                            columnsToDetect.add(temp);
                        }
                    }
                }
                if(!columnsToDetect.isEmpty()){
                    boolean valid = validateInputs(columnsToDetect);
                    if(valid) {
                        for (SloupecCustomGrafu sloupec : columnsToDetect) {
                            if (sloupec.useColum()) {
                                detectionValues.add(sloupec.detectValues(allColumns));
                            }
                        }

                        for (int i = 0; i < detectionValues.get(0).size(); i++) {
                            Boolean val = true;
                            for (int j = 0; j < detectionValues.size(); j++) {
                                val &= detectionValues.get(j).get(i);
                            }
                            if (val) {
                                columnData.add("1");
                            } else {
                                columnData.add("0");
                            }
                        }
                        centerTablePanel.remove(centerTablePanel.getComponentCount() - 1);

                        detected = new SloupecCustomGrafu("detected", columnData, columnsNumber, preparedVariableValues, false, columnNames, null, true);
                        centerTablePanel.add(detected, "dock west, grow");

                        graphData.getDataSloupec(columnsNumber - 2).clear();
                        for (String s : columnData) {
                            graphData.addData(columnsNumber, Double.parseDouble(s));
                        }

                        centerTablePanel.revalidate();
                        centerTablePanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame,
                                Konstanty.POPISY.getProperty("textNevalidniHodnoty"),
                                Konstanty.POPISY.getProperty("upozorneni"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            Konstanty.POPISY.getProperty("textZvoleniSloupce"),
                            Konstanty.POPISY.getProperty("upozorneni"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Akce pro detekci podle zadaných kritérií */
        exportBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (detected.getData().size() == 0) {
                    JOptionPane.showMessageDialog(mainFrame,
                            Konstanty.POPISY.getProperty("textNutnaDetekce"),
                            Konstanty.POPISY.getProperty("upozorneni"),
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    File file;
                    Writer writer = null;
                    boolean first = true;
                    List<List<String>> values = new ArrayList<>();
                    String completePath = "";
                    if (SystemUtils.IS_OS_WINDOWS){
                        completePath += Konstanty.APP.getProperty("exportPathWin");
                    } else {
                        completePath += Konstanty.APP.getProperty("exportPathUnix");
                    }
                    String fileName = "export" + (varOrQueryNameTf.getText().equals("") ? "" : "_" + varOrQueryNameTf.getText() + "_");
                    fileName += LocalDateTime.now();
                    fileName = fileName.replaceAll(":", "-");
                    completePath += fileName;

                    StringBuilder sb = new StringBuilder();
                    sb.append(axisColumn.getName());
                    values.add(axisColumn.getData());
                    for (SloupecCustomGrafu sloupec : sloupceCustomGrafu) {
                        sb.append(",").append(sloupec.getName());
                        values.add(sloupec.getData());
                    }
                    sb.append(",detected\n");
                    values.add(detected.getData());

                    first = true;
                    for (int i = 0; i < values.get(0).size(); i++) {
                        for (int j = 0; j < values.size(); j++) {
                            if (!first) {
                                sb.append(',');
                            }
                            sb.append(values.get(j).get(i));

                            first = false;
                        }
                        sb.append("\n");
                        first = true;
                    }

                    try {
                        file = new File(completePath + ".csv");
                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(file), "utf-8"));
                        writer.write(sb.toString());
                        JOptionPane.showMessageDialog(mainFrame,
                                Konstanty.POPISY.getProperty("textUspesnyExport"),
                                Konstanty.POPISY.getProperty("info"),
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        log.warn(ex);
                        JOptionPane.showMessageDialog(mainFrame,
                                Konstanty.POPISY.getProperty("textNeuspesnyExport"),
                                Konstanty.POPISY.getProperty("upozorneni"),
                                JOptionPane.WARNING_MESSAGE);
                    } finally {
                        try {
                            writer.close();
                        } catch (Exception ex) {/*ignore*/}
                    }
                }
            }
        });

        /* Akce pro zobrazení grafu */
        showGraphBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(detected.getData().size() == 0){
                    JOptionPane.showMessageDialog(mainFrame,
                            Konstanty.POPISY.getProperty("textNutnaDetekce"),
                            Konstanty.POPISY.getProperty("upozorneni"),
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

    /**
     * Zvaliduje uživatelské vstupy při zadávání kritérií pro detekce
     * @param sloupce seznam sloupců v tabulce hodnot
     * @return true pokud mají sloupce validní vstupy
     */
    private boolean validateInputs(List<SloupecCustomGrafu> sloupce){
        boolean result = true;
        for(SloupecCustomGrafu sloupec : sloupce){
            boolean valid = sloupec.validateInput();
            result &= valid;
        }
        return result;
    }

    /**
     * Kontrola jestli mají sloupce unikátní jména
     * @param panels seznam panelů s vytvořenými dotazy
     * @return true pokud mají sloupce unikátní jména
     */
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

    /**
     * Mapuje data podle osy X, pro následné vykreslení do grafu
     * @param data seznam s daty
     * @param firstColumn první sloupec, na který se budou data mapovat
     * @param columnName název vytvářeného sloupce
     * @param isIteration true pokud jsou na ose X iterace
     * @param columnNames názvy všech sloupců
     * @param query object s dotazem
     * @return nový sloupec do tabulky s daty
     */
    private SloupecCustomGrafu mapData(List<List<String>> data, List<String> firstColumn, String columnName, boolean isIteration, List<String> columnNames, JSONObject query){
        List<String> values = new ArrayList<>();
        boolean found = false;

        if(isIteration) {
            for (Iteration iteration : iterationList) {
                Double sum = 0.0;
                for (int i = 0; i < data.get(0).size(); i++) {
                    LocalDate temp = LocalDate.parse(data.get(0).get(i));
                    if (!temp.isBefore(iteration.getStartDate()) && !temp.isAfter(iteration.getEndDate())) {
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
        return new SloupecCustomGrafu(columnName, values, 1, preparedVariableValues, true, columnNames, query, false);
    }

    /**
     * Připraví data pro osu podle zadaných kritérií
     * @param axisTable měřítko pro osu X
     * @return data pro osu X
     */
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
                firstColumn = pohledDAO.getPeopleForProject(projekt.getID());
                break;
            case "Iteration":
                for (Iteration iterace : iterationList) {
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

    /**
     * Připraví formát datumů pro mapovací sloupec
     * @param axisTable měřítko pro osu X
     * @param joinColumn mapovací sloupec
     * @return mapovací sloupec v požadovaném formátu
     */
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

    /**
     * Připraví datumy, po které iterace běžela
     * @param iteration iterace projektu
     * @return seznam datumů pro iteraci
     */
    private List<String> prepareDatesForIteration(Iteration iteration){
        List<String> totalDates = new ArrayList<>();
        LocalDate start = LocalDate.parse(iteration.getStartDate().toString());
        LocalDate end = LocalDate.parse(iteration.getEndDate().toString());
        while (!start.isAfter(end)) {
            totalDates.add(start.toString());
            start = start.plusDays(1);
        }
        return totalDates;
    }

    /**
     * Připraví datumy mezi zadaným intervalem
     * @param dateFrom datum začátku intervalu
     * @param dateTo datum konce intervalu
     * @return seznam datumů
     */
    private List<String> prepareDatesBetween(LocalDate dateFrom, LocalDate dateTo){
        List<String> totalDates = new ArrayList<>();
        while (!dateFrom.isAfter(dateTo)) {
            totalDates.add(dateFrom.toString());
            dateFrom = dateFrom.plusDays(1);
        }
        return totalDates;
    }

    /**
     * Připraví týdny v čísleném vyjádření mezi zadaným intervalem
     * @param dateFrom datum začátku intervalu
     * @param dateTo datum konce intervalu
     * @return seznam týdnů
     */
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

    /**
     * Připraví měsíce v číselném vyjádření mezi zadaným intervalem
     * @param dateFrom datum začátku intervalu
     * @param dateTo datum konce intervalu
     * @return seznam měsíců
     */
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

    /**
     * Připraví roky v číselném vyjádření mezi zadaným intervalem
     * @param yearFrom datum začátku intervalu
     * @param yearTo datum konce intervalu
     * @return seznam roků
     */
    private List<String> prepareYearsBetween(LocalDate yearFrom, LocalDate yearTo){
        List<String> totalYears = new ArrayList<>();
        for (int i = yearFrom.getYear(); i <= yearTo.getYear(); i++){
            totalYears.add(""+i);
        }
        return totalYears;
    }

    /**
     * Vytvoří objekt tlačítka s obrázkem
     * @param buttonOperation operace vykonávaná tlačítkem
     * @return object tlačítka
     */
    private JButton createButtonWithImage(String buttonOperation){
        JButton button = new JButton();
        Image img = null;
        try {
            if(buttonOperation.equals("edit") || buttonOperation.equals("delete")) {
                img = ImageIO.read(Toolkit.getDefaultToolkit().getClass().getResource("/res/" + buttonOperation + "Image.png"));
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
            log.error(ex);
        }
        return button;
    }

    /**
     * Spustí zadaný dotaz a vrátí výslednou hodnotu
     * @param object SQL dotaz v JSON
     * @return výsledná hodnota
     */
    private Long testQuery(JSONObject object){
        PohledDAO pohledDAO = new PohledDAO();
        String condition;
        Long result;
        JSONObject queryObject;
        String logHeader = "\n==============================\n Variable: \n==============================\n";

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
        String query = "SELECT\n\t" + function + "(" + selectedColumn + ")\nfrom\n\t" + tableName + "\nwhere\n\t";

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
        result = pohledDAO.testVariable(query);
        log.info(logHeader + query);

        return result;
    }

    /**
     * Vnitřní třída zobrazující vytvořené konstanty v hlavním okně
     */
    private class ConstantPanel extends JPanel{
        ConstantPanel thisPanel;
        String name;
        String value;
        JLabel label;

        /**
         * Konstruktor panelu
         * @param constName název konstanty
         * @param constValue hodnota konstanty
         */
        public ConstantPanel(String constName, String constValue) {
            super();
            thisPanel = this;
            name = constName;
            value = constValue;
            this.setLayout(new MigLayout());
            label = new JLabel(name + ":" + value);
            this.add(label);

            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            this.setBorder(border);

            JButton editBtn = createButtonWithImage("edit");
            editBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    preparedVariableValues.removeIf(item -> item.getName().equals(name));
                    log.debug(preparedVariableValues);
                    FormularVytvoreniKonstanty constantsForm = new FormularVytvoreniKonstanty(name, value);
                    String oldName = name;
                    name = constantsForm.getConstName();
                    value = constantsForm.getConstValue();
                    label.setText(constantsForm.getConstName() + ":" + constantsForm.getConstValue());
                    ComboBoxItem comboBoxItem = new ComboBoxItem(constantsForm.getConstName(), "konstanta",constantsForm.getConstValue());
                    preparedVariableValues.add(comboBoxItem);
                    log.debug(preparedVariableValues);
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
                    log.debug(preparedVariableValues);
                    thisPanel.getParent().remove(thisPanel);
                    try {
                        File file = new File(constantFolderPath + name + ".json");
                        if( !file.delete() ){
                            log.warn("Delete operation failed.");
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

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            label.setText(name + ":" + value);
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            label.setText(name + ":" + value);
            this.value = value;
        }
    }

    /**
     * Vnitřní třída zobrazující vytvořené proměnné v hlavním okně
     */
    private class VariablePanel extends JPanel{
        VariablePanel thisPanel;
        String name;
        Long value;
        String content;
        JLabel label;

        /**
         * Konstruktor okna
         * @param varName název proměnné
         * @param varValue hodnota proměnné
         * @param varContent zápis proměnné v JSON
         */
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
                    centerNorthPanel.add(varOrQueryNameTf, "width 10%");
                    centerNorthPanel.add(testVarQueryBtn);

                    bottomPanel.add(cancelBtn);
                    bottomPanel.add(saveBtn);
                    mainFrame.add(bottomPanel, "dock south, height 40, width 100%");

                    centerPanel.add(centerNorthPanel, "dock north, width 100%");

                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray queries = (JSONArray) jsonObject.get("queries");
                    for (Object queryObj : queries)
                    {
                        JSONObject query = (JSONObject) queryObj;

                        QueryPanel panel = new QueryPanel(query);
                        panel.setBorder(new MatteBorder(0,0,0,1, Color.BLACK));
                        queryPanels.add(panel);
                        centerPanel.add(panel, "dock west, height 555, width " + queryPanelWidth);
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
                    log.debug(preparedVariableValues);
                    thisPanel.getParent().remove(thisPanel);
                    try {
                        File file = new File(variableFolderPath + name + ".json");
                        if( !file.delete() ){
                            log.warn("Delete operation failed.");
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

        /**
         * Vrací název proměnné
         * @return
         */
        public String getName(){
            return this.name;
        }

        /**
         * Nastaví zobrazený název proměnné v hlavním okně
         * @param label
         */
        public void setLabel(String label){
            this.label.setText(label);
        }

        /**
         * Nastaví zápis promměné v JSON
         * @param content zápis proměnné v JSON
         */
        public void setContent(String content){
            this.content = content;
        }
    }

    /**
     * Vnitřní třída zobrazující vytvořené panely s dotazy v hlavním okně
     */
    private class QueryPanel extends JPanel {

        QueryPanel thisPanel;
        JSONObject query;
        JTextField columName;
        JLabel lblSelect;
        JLabel lblFrom;
        JLabel lblWhere;
        JButton removeBtn;
        JButton editBtn;
        List<JLabel> conditionLabelList;

        /**
         * Konstrukor panelu pro zobrazení vytvořeného dotazu
         * @param query SQL dotaz v JSON
         */
        public QueryPanel(JSONObject query) {
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

            lblSelect = new JLabel("SELECT " + aggregate + "(" + agrColumn + ")");
            this.add(lblSelect, "wrap");
            lblFrom = new JLabel("FROM " + tableName);
            this.add(lblFrom, "wrap");
            lblWhere = new JLabel("WHERE");
            this.add(lblWhere, "wrap");

            conditionLabelList = new ArrayList<>();

            JSONArray conditions = (JSONArray) query.get("conditions");
            for(Object object: conditions){
                JSONObject condition = (JSONObject) object;
                JLabel label = new JLabel(condition.getString("name") + " " + condition.getString("operator") + " " + condition.getString("value"));
                this.add(label, "wrap");
                conditionLabelList.add(label);
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
                        for(JLabel label : conditionLabelList){
                            remove(label);
                        }
                        conditionLabelList.clear();
                        String tableName = query.getString("table");
                        String agrColumn = query.getString("agrColumn");
                        String aggregate = query.getString("aggregate");

                        columName.setText(columnName);

                        lblSelect.setText("SELECT " + aggregate + "(" + agrColumn + ")");
                        lblFrom.setText("FROM " + tableName);
                        lblWhere.setText("WHERE");

                        if (!query.isEmpty()) {
                            setQuery(query);
                            JSONArray conditions = (JSONArray) query.get("conditions");
                            for (Object coditionObject : conditions) {
                                JSONObject condition = (JSONObject) coditionObject;
                                JLabel label = new JLabel(condition.getString("name") + " " + condition.getString("operator") + " " + condition.getString("value"));
                                add(label, "wrap");
                                conditionLabelList.add(label);
                            }
                        }
                        add(editBtn, "wrap");
                        add(removeBtn);
                    }
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }

            });

            removeBtn = new JButton(Konstanty.POPISY.getProperty("odstranit"));
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

        /**
         * Nastaví SQL zapsaný v JSON
         * @param query SQL v JSON podobě
         */
        public void setQuery(JSONObject query){
            this.query = query;
        }

        /**
         * Vrací SQL zapsaný v JSON
         * @return JSON s SQL dotazem
         */
        public JSONObject getQuery() {
            return this.query;
        }

        /**
         * Vrací název sloupce
         * @return název sloupce
         */
        public String getColumnName(){
            return columName.getText();
        }
    }
}