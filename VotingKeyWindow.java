import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Szavazó kulcs lekérdező menüpont ablakának osztálya
 */
public class VotingKeyWindow extends ConfigWindow{
	/** Minden szavazást tartalmazó lista */
	private ArrayList<Vote> allVotesList = IO.loadAllVotesIntoList();
	/** Kiválasztott szavazás, ha nincs, akkor null */
	private Vote selectedVote = null;
	/** Kiválasztott kulcs, ha nincs, akkor null */
	private String selectedKey = null;
	
	/** Szavazások táblázatának panelje */
	private JPanel voteListPanel;
	/** Szavazások táblázatának modellje */
	private DefaultTableModel voteModel;
	/** Szavazások táblázata */
	private JTable voteListTable;
	/** Szavazások táblázatának JScrollPaneje */
	private JScrollPane voteListPane;
	
	/** Kulcsok táblázatát tartalmazó panel */
	private JPanel keysPanel;
	/** Kulcsok táblázatának modellje */
	private DefaultTableModel keysModel;
	/** Kulcsok táblázata */
	private JTable keysTable;
	/** Kulcsok táblázatának JScrollPaneje */
	private JScrollPane keysPane;
	
	/** Másolás gombot tartalmazó panel */
	private JPanel buttonPanel;
	/** Kiválasztott kulcs másolása gomb */
	private JButton copyButton;
	
	/**
	 * Alapvető konstruktor.
	 * Inicializálja az öröklött bodyPanel elemeit és 
	 * láthatóvá teszi az ablakot
	 * @param frameName A frame neve
	 */
	public VotingKeyWindow(String frameName) {
		super(frameName);
		initialiseBody();
		initialiseBodyLayoutAndAppearance();
		super.frame.setVisible(true);
	}

	/**
	 * A bodyPanel elemeit inicializálja és bekonfigurálja
	 */
	protected void initialiseBody() {
		super.bodyPanel.setLayout(new BorderLayout(15, 15));
		
		voteListPanel = new JPanel();
		String[] voteColumnNames = { "Name",  "# of generated keys", "# of remaining keys" };
		voteModel = new DefaultTableModel(loadVotesIntoStringMatrix(), voteColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		voteListTable = new JTable(voteModel);
		voteListTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		voteListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		voteListTable.setFillsViewportHeight(true);
		voteListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(voteListTable.getSelectedRow() > -1 && !lse.getValueIsAdjusting()) {
					selectedVote = allVotesList.get(voteListTable.getSelectedRow());
					voteSelectionEvent();
				}
			}
		});
		voteListPane = new JScrollPane(voteListTable);
		voteListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		voteListPane.setVisible(true);
		
		keysPanel = new JPanel();
		String[] keysColumnNames = { "Key" };
		keysModel = new DefaultTableModel(null, keysColumnNames){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		keysTable = new JTable(keysModel);
		keysTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		keysTable.setFillsViewportHeight(true);
		keysTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(voteListTable.getSelectedRow() > -1 && !lse.getValueIsAdjusting()) {
					selectedKey = (String)keysTable.getValueAt(keysTable.getSelectedRow(), 0);
				}
			} 
		});
		keysPane = new JScrollPane(keysTable);
		keysPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		keysPane.setVisible(true);
		
		buttonPanel = new JPanel(new BorderLayout());
		copyButton = new JButton("Copy Selected Key");
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				copyButtonEvent();
			}
		});
	}

	/**
	 * A bodyPanel elemeinek kinézetét beállitja
	 */
	protected void initialiseBodyLayoutAndAppearance() {
		super.bodyPanel.setLayout(new BorderLayout(15, 15));
		
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		copyButton.setFont(myFont);
		
		voteListPanel.add(voteListPane);
		keysPanel.add(keysPane);
		buttonPanel.add(copyButton);
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		
		super.bodyPanel.add(voteListPanel, BorderLayout.WEST);
		super.bodyPanel.add(keysPanel, BorderLayout.EAST);
		super.bodyPanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Betölti az összes szavazást egy String mátrixba
	 * @return Összes szavazás String mátrixban, ahol index 
	 * 0 = szavazás neve, 1 = összes kulcs száma, 2 = fel nem használt kulcsok száma
	 */
	private String[][] loadVotesIntoStringMatrix(){
		String[][] voteDataMatrix = new String[IO.getNumberOfVotes()][3];
		int cntr = 0;
		for(Vote v : allVotesList) {
			voteDataMatrix[cntr][0] = v.getName();
			voteDataMatrix[cntr][1] = String.valueOf(v.getPossibleVotes());
			voteDataMatrix[cntr][2] = String.valueOf(v.getKeyList().getKeysCount());
			cntr++;
		}
		return voteDataMatrix;
	}
	
	/**
	 * A kiválasztott szavazás kulcsait betölti egy String mátrixba
	 * @return Egy szavazás kulcsai String mátrixban
	 */
	private String[][] loadKeysIntoMatrix() {
		String[][] keysArray = new String[selectedVote.getKeyList().getKeysCount()][1];
		int cntr = 0;
		for(String key : selectedVote.getKeyList().getKeyList()) {
			keysArray[cntr][0] = key;
			cntr++;
		}
		
		return keysArray;
	}
	
	/**
	 * A kiválasztott szavazás kulcsait betölti a kulcsok táblázatába
	 */
	private void voteSelectionEvent() {
		String[] keysColumnNames = { "Key" };
		keysModel.setDataVector(loadKeysIntoMatrix(), keysColumnNames);
		keysModel.fireTableDataChanged();
	}
	
	/**
	 * A kiválasztott kulcsot a vágólapra másolja
	 */
	private void copyButtonEvent() {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selectedKey), null);
	}
}
