import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Profil konfigurációs menüpont ablakának osztálya
 */
public class ProfileConfigWindow extends ConfigWindow{
	/** Minden mentett profil listája */
	ArrayList<AdminProfile> allProfilesList = IO.loadAdminProfiles();
	
	/** A profil adatokat tartalmazó táblázat panele */
	private JPanel profilesPanel;
	/** A profil adatokat tartalmazó táblázat modelje */
	private DefaultTableModel profilesTableModel;
	/** A profil adatokat tartalmazó táblázat */
	private JTable profilesTable;
	/** A profil adatokat tartalmazó táblázat JScrollPaneje */
	private JScrollPane profilesPane;
	
	/** A gombokat tartalmazó panel */
	private JPanel buttonsPanel;
	/** Új profil gomb */
	private JButton newButton;
	/** Profil kitörlése gomb */
	private JButton deleteButton;
	/** Profilok mentése gomb */
	private JButton saveButton;

	/**
	 * Alapvető konstruktor
	 * @param frameName Frame neve
	 */
	public ProfileConfigWindow(String frameName) {
		super(frameName);
		initialiseBody();
		initialiseBodyLayoutAndAppearance();
		super.frame.setVisible(true);
	}

	/**
	 * A bodyPanel elemeit inicializálja és bekonfigurálja
	 */
	protected void initialiseBody() {
		profilesPanel = new JPanel();
		String[] columnNames = { "Name", "Password" };
		profilesTableModel = new DefaultTableModel(loadProfilesIntoStringMatrix(), columnNames);
		profilesTable = new JTable(profilesTableModel);
		profilesTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		profilesTable.setFillsViewportHeight(true);
		profilesPane = new JScrollPane(profilesTable);
		profilesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		profilesPane.setVisible(true);
		
		buttonsPanel = new JPanel();
		newButton = new JButton("New Profile");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				newButtonEvent();
			}
		});
		deleteButton = new JButton("Delete Profile");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				deleteButtonEvent();
			}
		});
		saveButton = new JButton("Save Profiles");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				saveButtonEvent();
			}
		});
	}

	/**
	 * A bodyPanel tartalmának kinézetét beállitja
	 */
	protected void initialiseBodyLayoutAndAppearance() {
		super.bodyPanel.setLayout(new BorderLayout(15, 15));
		
		profilesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		profilesPanel.add(profilesPane);
		
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		newButton.setFont(myFont);
		deleteButton.setFont(myFont);
		saveButton.setFont(myFont);
		
		buttonsPanel.setLayout(new GridLayout(1, 3, 15, 15));
		buttonsPanel.add(newButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(saveButton);
		
		super.bodyPanel.add(profilesPanel, BorderLayout.CENTER);
		super.bodyPanel.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Betölti a profilokat egy string mátrixba, ahol
	 * index 0 = név, 1 = jelszó.
	 * @return A profilok neve és jelszava mátrixban
	 */
	private String[][] loadProfilesIntoStringMatrix(){
		String[][] stringDataMatrix = new String[allProfilesList.size()][2];
		int cntr = 0;
		for(AdminProfile p : allProfilesList) {
			stringDataMatrix[cntr][0] = p.getUsername();
			stringDataMatrix[cntr][1] = p.getPassword();
			cntr++;
		}
		return stringDataMatrix;
	}
	
	/**
	 * New gomb megnyomásához kötött event.
	 * Hozzáad egy új sort a profilesTablehöz.
	 */
	private void newButtonEvent() {
		String[] newRow = { "New Profile Name", "New Profile Password" };
		profilesTableModel.addRow(newRow);
	}
	
	/**
	 * Save gomb megnyomásához kötött event. 
	 * Menti az addigi változásokat fileba
	 */
	private void saveButtonEvent() {
		ArrayList<AdminProfile> profiles = new ArrayList<AdminProfile>();
		for(int i = 0; i < profilesTableModel.getRowCount(); i++) {
			profiles.add(new AdminProfile((String)profilesTableModel.getValueAt(i, 0), (String)profilesTableModel.getValueAt(i, 1)));
		}
		IO.saveAdminProfiles(profiles);
	}
	
	/**
	 * Delete gomb megnyomásához kötött event. Kitörli a 
	 * kiválasztott profilt.
	 */
	private void deleteButtonEvent() {
		try {
			allProfilesList.remove(profilesTable.getSelectedRow()-1);
			profilesTableModel.removeRow(profilesTable.getSelectedRow());
		}catch(Exception e) {
			PopUp.show("Error", "Unable to delete selected profile!");
		}
	}
}
