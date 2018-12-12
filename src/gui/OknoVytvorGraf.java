package gui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ostatni.Konstanty;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;

public class OknoVytvorGraf extends JFrame {

    private JPanel contentPane;
    private static int indexX = 0;
    private static int indexY = 0;
    private static int windowWidth = 400;
    private static int windowHeight = 140;
    private static int dynamicWindowHeight = 0;
    private static int rowHeight = 30;
    private static int column = 0;
    private static int row = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OknoVytvorGraf frame = new OknoVytvorGraf();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public OknoVytvorGraf() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 100, windowWidth, windowHeight);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

		/*GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{130, 0};
		gbl_contentPane.rowHeights = new int[]{45, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);*/

        GridBagLayout gbl_contentPane = new GridBagLayout();
        contentPane.setLayout(gbl_contentPane);

        JTextField tfVarCreation = new JTextField();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,5,0,5); //padding
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = row++;
        contentPane.add(tfVarCreation, c);
        /*GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0,5,0,5); //padding
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTH;
        getContentPane().add(new ColorPanel(Color.pink, windowWidth, 100), g);*/

        String[] optionStrings = { "People", "Iterations", "Days", "Months", "Years" };
        JComboBox petList = new JComboBox(optionStrings);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(0,5,0,5); //padding
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.anchor = GridBagConstraints.NORTH;
        c1.weightx = 1.0;
        c1.weighty = 0.0;
        c1.gridx = column++;
        c1.gridy = row;
        contentPane.add(petList, c1);


        GridBagConstraints c2 = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;
        //gbl_contentPane.columnWeights = new double[]{1.0};
        //gbl_contentPane.rowWeights = new double[]{1.0};
        JButton btnAddConstr = new JButton("Add constraint");
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.NORTH;
        c2.weightx = 1.0;
        c2.weighty = 1.0;
        c2.gridx = column++;
        c2.gridy = row;
        contentPane.add(btnAddConstr, c2);

        // Adding labels and moving button
        btnAddConstr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Get data from dialog window and add it to form
                AttributesForm attForm = new AttributesForm();
                Map<String, List<JComboBox>> attMap = attForm.getFormData();
                if (!attMap.isEmpty()) {
                    Map.Entry<String, List<JComboBox>> entry = attMap.entrySet().iterator().next();
                    JLabel label = new JLabel(entry.getKey());
                    GridBagConstraints c = new GridBagConstraints();
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.anchor = GridBagConstraints.NORTH;
                    c.weightx = 1.0;
                    c.weighty = 1.0;
                    c.gridx = column;
                    c.gridy = row;
                    contentPane.add(label, c);

                    for (JComboBox att : entry.getValue()) {
                        JLabel button = new JLabel((String) att.getSelectedItem());
                        GridBagConstraints c2 = new GridBagConstraints();
                        c2.fill = GridBagConstraints.HORIZONTAL;
                        c2.anchor = GridBagConstraints.NORTH;
                        c2.weightx = 1.0;
                        c2.weighty = 1.0;
                        c2.gridx = column;
                        c2.gridy = ++row;
                        contentPane.add(button, c2);
                    }
                    column++;
                    row = 1;


                    // Button move
                    windowWidth += 180;

                    if ( windowHeight + (rowHeight * entry.getValue().size()) > dynamicWindowHeight) {
                        dynamicWindowHeight = windowHeight + (rowHeight * entry.getValue().size());
                    }
                    setBounds(0, 100, windowWidth, dynamicWindowHeight);
                    contentPane.remove(btnAddConstr);
                    GridBagConstraints c3 = new GridBagConstraints();
                    c3.fill = GridBagConstraints.HORIZONTAL;
                    c3.anchor = GridBagConstraints.NORTH;
                    c3.weightx = 1.0;
                    c3.weighty = 1.0;
                    c3.gridx = column++;
                    c3.gridy = row;
                    contentPane.add(btnAddConstr, c3);
                    contentPane.revalidate();
                    contentPane.repaint();
                }
            }
        });

		/*button = new JButton("5");
		GridBagConstraints c5 = new GridBagConstraints();
		c5.fill = GridBagConstraints.HORIZONTAL;
		c5.ipady = 0;       //reset to default
		c5.weighty = 1.0;   //request any extra vertical space
		c5.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c5.insets = new Insets(10,0,0,0);  //top padding
		c5.gridx = 1;       //aligned with button 2
		c5.gridwidth = 2;   //2 columns wide
		c5.gridy = 2;       //third row
		contentPane.add(button, c5);*/


        //JButton btnNewButton = new JButton("New button");
        //btnNewButton.addActionListener(new ActionListener() {
        //	public void actionPerformed(ActionEvent arg0) {
				/*JLabel lblNewLabel = new JLabel("test");
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
				gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel.insets = new Insets(0, 0, 0, 0);
				gbc_lblNewLabel.gridx = 1;
				gbc_lblNewLabel.gridy = a++;
				gbc_lblNewLabel.gridwidth = 1;
				contentPane.add(lblNewLabel, gbc_lblNewLabel);*/
				/*GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
				gbc_btnNewButton.fill = GridBagConstraints.BOTH;
				gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
				gbc_btnNewButton.gridx = a++;
				gbc_btnNewButton.gridy = 0;
				contentPane.add(btnNewButton, gbc_btnNewButton);*/
        //		windowWidth += 100;
        //		setBounds(100, 100, windowWidth, windowHeight);
        //contentPane.revalidate();
        //contentPane.repaint();
        //		}
        //	});
		/*GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);*/
    }

    public int getWindowHeight(int attCount) {

        return 0;
    }
}

class AttributesForm extends JDialog
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
    List<JComboBox> attValuesList = new ArrayList<>();
    String[] operators = { "<", "=", ">", "!=" };
    JComboBox cboxOperators = new JComboBox(operators);
    JTextField tfAttValue = new JTextField("Value");

    public AttributesForm()
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


		/*JPanel p1 = new JPanel(new GridLayout(1,2));
		p1.add(new JLabel("Type:    ",JLabel.RIGHT));
		p1.add(tf);
		JPanel p2 = new JPanel();
		JButton btn = new JButton("Add Details");
		p2.add(btn);
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();}});
		getContentPane().add(p1,BorderLayout.CENTER);
		getContentPane().add(p2,BorderLayout.SOUTH);

		pack();
		setVisible(true);*/
    }

    public Map<String, List<JComboBox>> getFormData()
    {
        Map<String, List<JComboBox>> dataMap = new LinkedHashMap<>();

        if(!closed) {
            dataMap.put((String) cboxTables.getSelectedItem(), attValuesList);
        }

        return dataMap;
    }
}
