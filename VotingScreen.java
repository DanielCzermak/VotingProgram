import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * A szavazó felület ablakának osztálya
 */
public class VotingScreen {
	/** Az ablak frameje */
	private static JFrame frame;
	/** A config felületre váltó gomb panele */
	private JPanel topBar;
	/** A config felületre váltó gomb */
	private JButton screenSwitchButton;
	
	/** Szavazó kulcsot bekérő felület panele */
	private JPanel kPanel;
	/** Szavazó kulcsot bekérő elemek panele */
	private JPanel kElementsPanel;
	/** Szavazó kulcsot bekérő label */
	private JLabel kLabel;
	/** Szavazó kulcsot bekérő beviteli mező */
	private JTextField kField;
	/** Szavazó kulcsot bekérő gomb*/
	private JButton kButton;
	
	/** Szavazó felület panel */
	private JPanel vPanel;
	/** Szavazás kérdésének panele */
	private JPanel questionPanel;
	/** Szavazás kérdésének labele */
	private JLabel questionLabel;
	/** Szavazó gombok panele */
	private JPanel buttonPanel;
	/** Szavazó gombok tömbje */
	private JButton[] votingButtons;
	
	/**
	 * Alapvető konstruktor.
	 */
	public VotingScreen() {
		initialiseFrame();
		initialiseTopBar();
		initialiseKPanel();
		initialiseVotingPanel();
		initialiseLayoutAndAppearance();
	}
	
	/**
	 * A frameet inicializálja és konfigurálja 
	 */
	private void initialiseFrame() {
		frame = new JFrame("Voting");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
	}
	
	/**
	 * A configra váltó gomb felületének elemeit inicializálja 
	 * és konfigurálja be
	 */
	private void initialiseTopBar() {
		topBar = new JPanel();
		
		screenSwitchButton = new JButton();
		screenSwitchButton.setPreferredSize(new Dimension(80 ,80));
		screenSwitchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(ProgramState.getLoginWindowState() == false) {
					new LoginWindow();
				}
			}
		});
	}
	
	/**
	 * A kulcsot bekérő felület elemeit inicializálja és konfigurálja be
	 */
	private void initialiseKPanel() {
		kPanel = new JPanel();
		kElementsPanel = new JPanel();
		kLabel = new JLabel("Enter key:");
		kField = new JTextField();
		kButton = new JButton("Use key");
		
		kButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ProgramState.getRunningVote().getKeyList().useKey(kField.getText())) {
					kField.setText("");
					frame.remove(kPanel);
					frame.add(vPanel, BorderLayout.CENTER);
					frame.revalidate();
					frame.repaint();
				}else {
					PopUp.show("Error", "Wrong key!");
				}
			}
		});
	}
	
	/**
	 * A szavazó felület elemeit inicializálja és konfigurálja be
	 */
	private void initialiseVotingPanel() {
		vPanel = new JPanel();
		
		questionPanel = new JPanel();
		questionLabel = new JLabel(ProgramState.getRunningVote().getQuestion(), SwingConstants.CENTER);
		
		buttonPanel = new JPanel();
		votingButtons = createVotingButtons();
	}
	
	/**
	 * Az ablak minden elemének kinézetét beállitja
	 */
	private void initialiseLayoutAndAppearance() {
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 30);
		
		frame.setLayout(new BorderLayout());
		frame.add(topBar, BorderLayout.NORTH);
		frame.add(kPanel, BorderLayout.CENTER);
		
		//TopBar layout & appearance
		topBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topBar.add(screenSwitchButton);
		IO.loadButtonImage("resources/csbi.png", screenSwitchButton);
		
		//KeyPanel layout & appearance
		kLabel.setFont(myFont);
		kField.setFont(myFont);
		kField.setColumns(8);
		kButton.setFont(myFont);
		
		kElementsPanel.add(kLabel);
		kElementsPanel.add(kField);
		kElementsPanel.add(kButton);
		
		kPanel.setLayout(new BorderLayout(15, 15));
		kPanel.setBorder(BorderFactory.createEmptyBorder(90, 0, 0, 0));
		kPanel.add(kElementsPanel, BorderLayout.CENTER);
		
		//VotingPanel layout & appearance
		questionLabel.setPreferredSize(new Dimension(400, 100));
		questionLabel.setFont(myFont);
		questionPanel.add(questionLabel);
		
		buttonPanel.setLayout(new GridLayout(ProgramState.getRunningVote().getNumberOfCandidates()/3, ProgramState.getRunningVote().getNumberOfCandidates(), 15, 15));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		for(JButton b : votingButtons) {
			b.setFont(myFont);
			buttonPanel.add(b);
		}
		
		vPanel.setLayout(new BorderLayout(15, 15));
		vPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		vPanel.add(questionPanel, BorderLayout.NORTH);
		vPanel.add(buttonPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Létrehozza a szavazó gombokat. Minden gomb egy jelöltet jelent.
	 * @return A szavazó gombok tömbben
	 */
	private JButton[] createVotingButtons() {
		JButton[] bts = new JButton[ProgramState.getRunningVote().getNumberOfCandidates()];
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JButton hb = (JButton)ae.getSource();
				if(PopUp.showYesNo("", "Are you sure?") == 0) {
					ProgramState.getRunningVote().getCandidate(hb.getText()).castVote();
					IO.saveKeys(ProgramState.getRunningVote().getKeyList(), ProgramState.getRunningVote().getName());
					frame.remove(vPanel);
					frame.add(kPanel, BorderLayout.CENTER);
					frame.revalidate();
					frame.repaint();
				}
			}
		};
		for(int i = 0; i < ProgramState.getRunningVote().getNumberOfCandidates(); i++) {
			bts[i] = new JButton(ProgramState.getRunningVote().getCandidate(i).getName());
			bts[i].addActionListener(al);
		}
		return bts;
	}
	
	/**
	 * A voting ablak láthatóságát állitja
	 * @param visible True, ha láthatóvá, false, ha nem láthatóvá akarjuk tenni
	 */
	public static void setVisibility(boolean visible) {
		frame.setVisible(visible);
	}
	
	/**
	 * Felszabaditja a frame által foglalt memóriát
	 */
	public static void dispose() {
		frame.dispose();
	}
	
}
