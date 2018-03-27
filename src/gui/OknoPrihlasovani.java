package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ostatni.Konstanty;

/**
 * Přihlašovací okno, spouští se jako první, zajišťuje spojení s databází
 * @author michalvselko
 *
 */
public class OknoPrihlasovani extends JFrame {

	private JButton btPrihlasit = new JButton(Konstanty.POPISY.getProperty("tlacitkoPrihlasit"));		//tlačítko pro přihlášení
	private JButton btUkoncit = new JButton(Konstanty.POPISY.getProperty("tlacitkoUkoncit"));			//tlačítko pro ukončení
	private JTextField tfLogin = new JTextField();														//pole pro zadaní loginu
	private JPasswordField pfHeslo = new JPasswordField();												//polo pro zadání hesla
	
	public OknoPrihlasovani(){
		setSize(Konstanty.VELIKOST_PRIHLASOVACIHO_OKNA);		
    	setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(Konstanty.POPISY.getProperty("titlePrihlaseni"));
	
		JLabel lblLogin = new JLabel(Konstanty.POPISY.getProperty("popisLogin"));
		JLabel lblHeslo = new JLabel(Konstanty.POPISY.getProperty("popisHeslo"));
				
		tfLogin.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
		pfHeslo.setPreferredSize(Konstanty.VELIKOST_CELA_SIRKA);
		btPrihlasit.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
		btUkoncit.setPreferredSize(Konstanty.VELIKOST_POLOVICNI_SIRKA);
		
		nastavAkce();	//nastaví akce tlačítek
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();	
		g.insets = new Insets(5, 5, 5, 5);
		g.fill = GridBagConstraints.HORIZONTAL;
	    g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 1;
		this.add(lblLogin, g);
		g.gridx = 1;
		g.gridwidth = 4;
		this.add(tfLogin, g);
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 1;
		this.add(lblHeslo, g);
		g.gridx = 1;
		g.gridwidth = 4;
		this.add(pfHeslo, g);
		g.gridx = 1;
		g.gridy = 2;
		g.gridwidth = 1;
		this.add(btPrihlasit, g);
		g.gridx = 4;
		this.add(btUkoncit, g);		
		this.setVisible(true);
	}
	
	/**
	 * Pokud souhlasí login a heslo, přihlásí se do databáze a spustí hlavní okno programu
	 * @param login název uživatelského účtu
	 * @param heslo heslo k uživatelskému účtu
	 */
	private void prihlasit(String login, String heslo){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection pripojeni = DriverManager.getConnection(Konstanty.CESTA_K_DATABAZI, login, heslo); 
			
			/*vlákno zobrazuje okno s progresem načítání*/
			Thread t1 = new Thread(new Runnable(){
				public void run() {
					OknoProgresNacitani oknoProgres = new OknoProgresNacitani();
				    while(Konstanty.CITAC_PROGRESU <= Konstanty.POCET_KROKU_PROGRESU){
						oknoProgres.nastavProgres();
						if(Konstanty.CITAC_PROGRESU >= Konstanty.POCET_KROKU_PROGRESU){
							break;
						}
						Thread.yield();							
					}
				    oknoProgres.setVisible(false);				
				}				
			});
			
			/*vlákno spustí hlavní okno programu*/
			Thread t2 = new Thread(new Runnable(){
				public void run() {
					new OknoHlavni(pripojeni);					
				}				
			});
			t1.start();
			t2.start();			
			
			this.setVisible(false);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaOvereni"));
			tfLogin.setText("");
			pfHeslo.setText("");		    
		} catch (Exception e){
			JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaPrihlaseni"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Nastavení akcí jednotlivých komponent
	 */
	private void nastavAkce(){
		/*akce po kliknutí na tlačítko přihlásit*/
		ActionListener actPrihlasit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					if(tfLogin.getText().equals("") || pfHeslo.getPassword().length == 0 ){
						JOptionPane.showMessageDialog(null , Konstanty.POPISY.getProperty("chybaUdaju"));	
					}
					else
						prihlasit(tfLogin.getText(), new String(pfHeslo.getPassword()));					
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		};
		/*akce po kliknutí na tlačítko ukončit*/
		ActionListener actUkoncit = new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		/*připojení akcí k jednotlivým komponentám*/
		btPrihlasit.addActionListener(actPrihlasit);
		btUkoncit.addActionListener(actUkoncit);
	}
}
