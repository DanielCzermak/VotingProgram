import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;

/**
 * Ez az osztály felel a login ablak létrehozásáért, kezeléséért 
 * és megszüntetéséért. A program elinditásakor és a szavazói
 * felületről a config felületre váltáskor jelenik meg. 
 */

public class LoginWindow {
	/** A login ablak JFrame-je */
	JFrame frame;
	/** A JPanel, ami minden komponenst tartalmaz */
	JPanel panel;
	/** Az ablakot bezáró JButton */
	JButton exitButton;
	/** A mező, ahova a felhasználónevet kell beírni */
	JTextField nameField;
	/** A mező, ahova a jelszót kell beírni */
	JPasswordField passField;
	/** A gomb, amivel a felhasználó beléphet*/
	JButton logInButton;
	
	/** Minden mentett profilt tartalmazó lista */
	private ArrayList<AdminProfile> allProfilesList = IO.loadAdminProfiles();
	
	/**
	 * Alapvető és egyetlen konstruktora az osztálynak.
	 * <p>Megmondja a programnak, hogy létrejött login ablak, 
	 * így nem lehet többet is megnyitni belőle. 
	 * Inicializálja az ablakot, elemket és a kinézetet, valamint
	 * láthatóva teszi. </p>
	 */
	public LoginWindow() {
		ProgramState.setLoginWindowState(true);
		initialiseFrame();
		initialisePanel();
		initialiseLayoutAndAppearance();
		frame.setVisible(true);
	}
	
	/**
	 * A frame-et inicializáló és jellemzőit beállító metódus
	 */
	private void initialiseFrame() {
		frame = new JFrame();
		frame.setSize(200, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * A komponenseket inicializáló metódus. A gombokhoz itt adjuk
	 * hozzá az ActionListener-öket, valamint a feliratot.
	 */
	private void initialisePanel() {
		panel = new JPanel();
		exitButton = new JButton("Exit");
		nameField = new JTextField();
		passField = new JPasswordField();
		logInButton = new JButton("Log In");
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				frame.dispose();
				ProgramState.setLoginWindowState(false);
			}
		});
		
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				logInButtonEvent();
			}
		});
	}
	
	/**
	 * A komponensek elrendezését és kinézetét beállító metódus.
	 */
	private void initialiseLayoutAndAppearance() {
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		exitButton.setFont(myFont);
		nameField.setFont(myFont);
		passField.setFont(myFont);
		logInButton.setFont(myFont);
		
		panel.setLayout(new GridLayout(4, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panel.add(exitButton);
		panel.add(nameField);
		panel.add(passField);
		panel.add(logInButton);
		
		frame.add(panel);
	}
	
	/**
	 * A login gomb ActionListener-jében meghívott metódus.
	 * <p>Itt ellenőrizzük a megadott adatokat és léptetjük
	 * be a felhasználót. Sikeres belépés esetén az ablakot megszűntetjük.</p>
	 */
	private void logInButtonEvent() {
		for(AdminProfile ap : allProfilesList) {
			if(ap.logIn(nameField.getText(), new String(passField.getPassword()))) {
				ProgramState.setLoginWindowState(false);
				switch(ProgramState.getCurrentWindow()) {
					case VOTING:
						IO.saveVote(ProgramState.getRunningVote());
						VotingScreen.setVisibility(false);
						VotingScreen.dispose();
						ConfigScreen.setVisibility(true);
						ProgramState.switchWindow();
						break;
					case CONFIG:
						ConfigScreen.setVisibility(true);
						break;
				}
				frame.dispose();
				return;
			}
		}
		PopUp.show("Failed to log in", "Username or Password was incorrect");
	}
}
