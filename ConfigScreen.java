import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 * A config menü ablakának osztálya
 */
public class ConfigScreen {
	/** Az ablak frameje */
	private static JFrame frame;
	/** A voting felületre váltó és kilépő gomb panele */
	private JPanel topBar;
	/** A menü gombok panele */
	private JPanel menuPanel;
	/** A voting felületre váltó gomb */
	private JButton screenSwitchButton;
	/** A programot bezáró gomb */
	private JButton exitButton;
	/** A menü gombjainak tömbje */
	private JButton[] confButtons;
	
	/**
	 * Alapvető konstruktor.
	 */
	public ConfigScreen() {
		initialiseFrame();
		initialiseTopBar();
		initialiseBody();
		initialiseLayoutAndAppearance();
	}
	
	/**
	 * A frameet inicializálja és konfigurálja 
	 */
	private void initialiseFrame() {
		frame = new JFrame("Configuration Panel");
		frame.setSize(1280, 720);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * A voting felületre váltó és exit gombot inicializálja és bekonfigurálja.
	 */
	private void initialiseTopBar() {
		topBar = new JPanel();
		
		//ScreenSwitchButton
		screenSwitchButton = new JButton();
		screenSwitchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(ProgramState.getRunningVote() != null) {
					if(PopUp.showYesNo("Warning!", "You will be logged out.\n Are you sure?") == 0){
						new VotingScreen();
						ConfigScreen.setVisibility(false);
						VotingScreen.setVisibility(true);
						ProgramState.switchWindow();
					}
				}else {
					PopUp.show("Error", "There is no vote currently in progress!");
				}
			}
		});
		
		//ExitButton
		exitButton = new JButton();
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				ProgramState.saveVotingState();
				System.exit(0);
			}
		});
	}
	
	/**
	 * A menü gombjainak panelét inicializálja és a gombokat
	 * hozzáadja a panelhez
	 */
	private void initialiseBody() {
		menuPanel = new JPanel();
		confButtons = createConfButtons();
		for(JButton b : confButtons) {
			menuPanel.add(b);
		}
	}
	
	/**
	 * Az ablak minden elemének kinézetét beállitja
	 */
	private void initialiseLayoutAndAppearance() {
		frame.setLayout(new BorderLayout());
		frame.add(topBar, BorderLayout.NORTH);
		
		topBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
		topBar.setBackground(Color.GRAY);
		
		topBar.add(screenSwitchButton);
		screenSwitchButton.setPreferredSize(new Dimension(80 ,80));
		screenSwitchButton.setMargin(new Insets(0, 0, 0, 0));
		IO.loadButtonImage("resources/vsbi.png", screenSwitchButton);
		
		IO.loadButtonImage("resources/exit.png", exitButton);
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		exitButton.setPreferredSize(new Dimension(80, 80));
		topBar.add(exitButton);
		
		menuPanel.setLayout(new GridLayout(5, 1, 10, 10));
		menuPanel.setBackground(Color.GRAY);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		frame.add(menuPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Létrehozza a menü gombjait
	 * @return A menü gombjai tömbben
	 */
	private JButton[] createConfButtons() {
		JButton[] bts = new JButton[5];
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				confActionHandler((JButton)ae.getSource());
			}
		};
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 30);
		for(int i = 0; i < 5; i++) {
			bts[i] = new JButton();
			bts[i].addActionListener(al);
			bts[i].setFont(myFont);
		}
		bts[0].setText("Create a New Vote");
		bts[1].setText("Start or End a Vote");
		bts[2].setText("Get Voting Keys");
		bts[3].setText("See Results");
		bts[4].setText("Configure Profiles");
		return bts;
	}
	
	/**
	 * A menü gombjainak megnyomásakor meghivódó metódus.
	 * Attól függően melyik az, megynitja a hozzá tartozó ablakot 
	 * @param btn A menü gombjainak valamelyike
	 */
	private static void confActionHandler(JButton btn) {
		String s = btn.getText();
		if(s.equals("Create a New Vote")) {
			new VoteCreationWindow(s);
		}else if(s.equals("Start or End a Vote")) {
			new VoteStartEndWindow(s);
		}else if(s.equals("Get Voting Keys")) {
			new VotingKeyWindow(s);
		}else if(s.equals("See Results")) {
			new StatisticsWindow(s);
		}else if(s.equals("Configure Profiles")) {
			new ProfileConfigWindow(s);
		}
	}
	
	/**
	 * A config ablak láthatóságát állitja
	 * @param visible True, ha láthatóvá, false, ha nem láthatóvá akarjuk tenni
	 */
	public static void setVisibility(boolean visible) {
		frame.setVisible(visible);
	}
}
