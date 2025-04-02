import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
/**
 * A config felület menüpontjainál megjelenő ablakok alapját képező
 * absztrakt osztály. Az frameet és a exit gombot tartalmazó felső panelt
 * konfigurálja be.
 */
public abstract class ConfigWindow {
	/** A frame */
	protected JFrame frame;
	/** A felső panel */
	protected JPanel topBarPanel;
	/** A test panel */
	protected JPanel bodyPanel;
	/** Kilépés gomb */
	protected JButton exitButton;
	
	/**
	 * Alapvető konstruktor
	 * @param frameName A frame neve
	 */
	public ConfigWindow(String frameName) {
		initialiseFrame(frameName);
		initialiseTopPanel();
		initialiseBodyInitial();
		initialiseStartingLayout();
	}
	
	/**
	 * A frameet inicializálja és konfigurálja
	 * @param frameName A frame neve
	 */
	private void initialiseFrame(String frameName) {
		frame = new JFrame(frameName);
		frame.setSize(960, 540);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * A topPanel elemeit inicializálja és konfigurálja.
	 */
	private void initialiseTopPanel() {
		topBarPanel = new JPanel();
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
	}
	
	/**
	 * Csak inicializálja a bodyPanelt
	 */
	private void initialiseBodyInitial() {
		bodyPanel = new JPanel();
	}
	
	/**
	 * A bodyPanel tartalmán kivül beállitja az ablak kinézetét
	 */
	private final void initialiseStartingLayout() {
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		frame.setLayout(new BorderLayout());
		exitButton.setPreferredSize(new Dimension(110, 40));
		exitButton.setFont(myFont);
		topBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topBarPanel.add(exitButton);
		frame.add(topBarPanel, BorderLayout.NORTH);
		frame.add(bodyPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Az ablak testének elemeit inicializálja és konfigurálja be
	 */
	protected abstract void initialiseBody();

	/**
	 * Az ablak testének kinézetét állitja be
	 */
	protected abstract void initialiseBodyLayoutAndAppearance();
	
	/**
	 * A frame által foglalt memóriát felszabaditja
	 */
	protected void dispose() {
		frame.dispose();
	}
}
