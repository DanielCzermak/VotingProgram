import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Szavazás eredményeket lekérő menüpont ablakának osztálya
 */
public class StatisticsWindow extends ConfigWindow{
	/** Lezárt szavazások listája */
	private ArrayList<Vote> closedVotesList = loadClosedVotes();
	/** Kiválasztott szavazás, ha nincs, akkor null */
	private Vote selectedVote = null;
	
	/** Szavazások táblázatának panelje */
	private JPanel voteListPanel;
	/** Szavazások táblázatának modellje */
	private DefaultTableModel voteModel;
	/** Szavazások táblázata */
	private JTable voteListTable;
	/** Szavazások táblázatának JScrollPaneje */
	private JScrollPane voteListPane;
	
	/** Eredmények táblázatának panelje */
	private JPanel resultsListPanel;
	/** Eredmények táblázatának modellje */
	private DefaultTableModel resultsModel;
	/** Eredmények táblázata  */
	private JTable resultsListTable;
	/** Eredmények táblázatának JScrollPaneje */
	private JScrollPane resultsListPane;
	
	/**
	 * Alapvető konstruktor.
	 * Inicializálja a bodyPanel elemeit és kinézetét, 
	 * láthatóvá teszi az ablakot.
	 * @param frameName Frame neve
	 */
	public StatisticsWindow(String frameName) {
		super(frameName);
		initialiseBody();
		initialiseBodyLayoutAndAppearance();
		super.frame.setVisible(true);
	}
	
	/**
	 * A bodyPanel elemeit inicializálja és bekonfigurálja
	 */
	protected void initialiseBody() {
		voteListPanel = new JPanel();
		String[] voteColumnNames = { "Name", "Question", "Possible votes", "Cast votes" };
		voteModel = new DefaultTableModel(loadClosedVotesIntoStringMatrix(), voteColumnNames) {
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
					selectedVote = closedVotesList.get(voteListTable.getSelectedRow());
					voteSelectionEvent();
				}
			}
		});
		voteListPane = new JScrollPane(voteListTable);
		voteListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		voteListPane.setVisible(true);
		
		resultsListPanel = new JPanel();
		String[] resultsColumnNames = { "Name", "Votes", "Percentage" };
		resultsModel = new DefaultTableModel(null, resultsColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		resultsListTable = new JTable(resultsModel);
		resultsListTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		resultsListTable.setFocusable(false);
		resultsListTable.setRowSelectionAllowed(false);
		resultsListTable.setFillsViewportHeight(true);
		resultsListPane = new JScrollPane(resultsListTable);
		resultsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultsListPane.setVisible(true);
	}
	
	/**
	 * A bodyPanel tartalmának kinézetét beállitja
	 */
	protected void initialiseBodyLayoutAndAppearance() {
		super.bodyPanel.setLayout(new BorderLayout(15, 15));
		
		voteListPanel.add(voteListPane);
		resultsListPanel.add(resultsListPane);
		
		super.bodyPanel.add(voteListPanel, BorderLayout.WEST);
		super.bodyPanel.add(resultsListPanel, BorderLayout.EAST);
	}
	
	/**
	 * Kigyűjti a lezárt szavazásokat az összes lista közül
	 * @return Lezárt szavazások listája
	 */
	private ArrayList<Vote> loadClosedVotes(){
		ArrayList<Vote> votesList = new ArrayList<Vote>();
		for(Vote v : IO.loadAllVotesIntoList()) {
			if(v.getState() == 2) {
				votesList.add(v);
			}
		}
		return votesList;
	}
	
	/**
	 * A lezárt szavazásokat beleteszi egy String mátrixba, ahol index
	 * 0 = szavazás neve, 1 = szavazás kérdése,
	 *  2 = lehetséges szavazások száma, 3 = leadott szavazások száma
	 * @return Lezárt szavazások String mátrixban
	 */
	private String[][] loadClosedVotesIntoStringMatrix() {
		String[][] closedVotesMatrix = new String[closedVotesList.size()][4];
		int cntr = 0;
		for(Vote v : closedVotesList) {
			closedVotesMatrix[cntr][0] = v.getName();
			closedVotesMatrix[cntr][1] = v.getQuestion();
			closedVotesMatrix[cntr][2] = String.valueOf(v.getPossibleVotes());
			closedVotesMatrix[cntr][3] = String.valueOf(v.getPossibleVotes() - v.getKeyList().getKeysCount());
			cntr++;
		}
		return closedVotesMatrix;
	}
	
	/**
	 * Egy szavazás jelöltjeit beleteszi egy String mátrixba, ahol index
	 * 0 = jelölt neve, 1 = kapott szavazatok, 2 = szavazatok aránya
	 * @return Egy szavazás eredményei String mátrixban
	 */
	private String[][] loadCandidatesIntoStringMatrix() {
		String[][] candidates = new String[selectedVote.getNumberOfCandidates()][3];
		for(int i = 0; i < selectedVote.getNumberOfCandidates(); i++) {
			candidates[i][0] = selectedVote.getCandidate(i).getName();
			candidates[i][1] = String.valueOf(selectedVote.getCandidate(i).getVotes());
			candidates[i][2] = String.format("%.2f", ((double)selectedVote.getCandidate(i).getVotes() /
					((double)selectedVote.getPossibleVotes() - (double)selectedVote.getKeyList().getKeysCount()) * (double)100)) + "%";
		}
		return candidates;
	}
	
	/**
	 * Egy szavazás kiválasztásakor betölti az eredmények táblázatába
	 * az ahhoz tartozó adatokat
	 */
	private void voteSelectionEvent() {
		String[] resultsColumnNames = { "Name", "Votes", "Percentage" };
		resultsModel.setDataVector(loadCandidatesIntoStringMatrix(), resultsColumnNames);
		resultsModel.fireTableDataChanged();
	}
}
