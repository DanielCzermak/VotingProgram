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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Szavazás inditó/leállitó menüpont ablakának osztálya
 */
public class VoteStartEndWindow extends ConfigWindow{
	/** Lista minden mentett szavazással */
	ArrayList<Vote> allVotesList = IO.loadAllVotesIntoList();
	/** A szavazásos táblázatban kiválasztott szavazás */
	Vote selectedVote = null;
	
	/** A szavazásos táblázat panele*/
	private JPanel tablePanel;
	/** A gombok panele */
	private JPanel buttonPanel;
	/** A szavazásos táblázat modelje */
	private DefaultTableModel model;
	/** A szavazásos táblázat */
	private JTable votesTable;
	/** A szavazásos táblázat JScrollPaneje */
	private JScrollPane listScrollPane;
	/** Szavazás elinditó gomb */
	private JButton startButton;
	/** Szavazás lezáró gomb */
	private JButton endButton;
	
	/**
	 * Alapvető konstruktor
	 * @param frameName Frame neve
	 */
	public VoteStartEndWindow(String frameName) {
		super(frameName);
		initialiseBody();
		initialiseBodyLayoutAndAppearance();
		super.frame.setVisible(true);
	}
	
	/**
	 * Az ablak testének elemeit inicializálja és bekonfigurálja
	 */
	protected void initialiseBody() {
		tablePanel = new JPanel(new BorderLayout(15, 15));
		buttonPanel = new JPanel(new GridLayout(1, 2, 15, 15));
		
		String[] columnNames = { "Name", "Question", "State" };
		model = new DefaultTableModel(loadVotesIntoStringMatrix(), columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		votesTable = new JTable(model);
		votesTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		votesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		votesTable.setFillsViewportHeight(true);
		votesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(votesTable.getSelectedRow() > -1 && !lse.getValueIsAdjusting()) {
					selectedVote = allVotesList.get(votesTable.getSelectedRow());
				}
			}
		});
		
		listScrollPane = new JScrollPane(votesTable);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setVisible(true);
		
		startButton = new JButton("Start Vote");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				startButtonEvent();
			}
		});
		
		endButton = new JButton("End Vote");
		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				endButtonEvent();
			}
		});
		
	}
	
	/**
	 * A bodyPanel tartalmának kinézetét beállitja
	 */
	protected void initialiseBodyLayoutAndAppearance() {
		super.bodyPanel.setLayout(new BorderLayout(15, 15));
		
		tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		startButton.setFont(myFont);
		endButton.setFont(myFont);
		
		tablePanel.add(listScrollPane);
		buttonPanel.add(startButton);
		buttonPanel.add(endButton);
		
		super.bodyPanel.add(tablePanel, BorderLayout.CENTER);
		super.bodyPanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Betölti a szavazásokat egy String mátrixba, ahol index
	 * 0 = név, 1 = kérdés és 2 = állapot, mint String.
	 * @return A szavazások neve, kérdése és állapota String mátrixban
	 */
	private String[][] loadVotesIntoStringMatrix(){
		String[][] voteDataMatrix = new String[IO.getNumberOfVotes()][3];
		int cntr = 0;
		for(Vote v : allVotesList) {
			voteDataMatrix[cntr][0] = v.getName();
			voteDataMatrix[cntr][1] = v.getQuestion();
			switch(v.getState()) {
			case 0:
				voteDataMatrix[cntr][2] = "Not running";
				break;
			case 1:
				voteDataMatrix[cntr][2] = "Running";
				break;
			case 2:
				voteDataMatrix[cntr][2] = "Ended";
				break;
			}
			cntr++;
		}
		return voteDataMatrix;
	}
	
	/**
	 * Megpróbálja a kiválasztott szavazást elinditani, ha nem sikerül
	 * ezt jelzi felugró ablakkal. Siker esetén ment is.
	 */
	private void startButtonEvent() {
		if(ProgramState.getRunningVote() == null) {
			if(selectedVote.startVote()) {
				IO.saveVote(selectedVote);
				votesTable.setValueAt("Running", allVotesList.indexOf(selectedVote), 2);
			}else {
				PopUp.show("Error", "Unable to start vote!");
			}
		}else {
			PopUp.show("Error", "There is already a vote running!");
		}
	}
	
	/**
	 * Megpróbálja lezárni a kiválasztott szavazást, ha nem sikerül
	 * jelzi egy felugró ablakkal. Siker esetén ment is.
	 */
	private void endButtonEvent() {
		if(selectedVote.closeVote()) {
			IO.saveVote(selectedVote);
			votesTable.setValueAt("Ended", allVotesList.indexOf(selectedVote), 2);
		}else {
			PopUp.show("Error", "Unable to end vote!");
		}
	}
}
