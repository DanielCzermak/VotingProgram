import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

/**
 * Új szavazást létrehozó menüpont ablakának osztálya
 */
public class VoteCreationWindow extends ConfigWindow{
	/** Új szavazás adatait bekérő elemeket tartalmazó panel */
	private JPanel leftPanel;
	/** Új szavazás nevét bekérő panel */
	private JPanel voteNamePanel;
	/** Új szavazás nevét bekérő label */
	private JLabel voteNameLabel;
	/** Új szavazás nevét bekérő mező */
	private JTextField voteNameField;
	/** Új szavazás kérdését bekérő panel */
	private JPanel voteQuestionPanel;
	/** Új szavazás kérdését bekérő label */
	private JLabel voteQuestionLabel;
	/** Új szavazás kérdését bekérő mező */
	private JTextField voteQuestionField;
	/** Új szavazás szavazóinak számát bekérő panel */
	private JPanel voteVotersPanel;
	/** Új szavazás szavazóinak számát bekérő label */
	private JLabel voteVotersLabel;
	/** Új szavazás szavazóinak számát bekérő mező */
	private JFormattedTextField voteVotersField;
	
	/** Jelöltek táblázatát, gombokat tartalmazó panel */
	private JPanel rightPanel;
	/** Jelöltek táblázatának modellje */
	private DefaultTableModel candidateTableModel;
	/** Jelöltek táblázata */
	private JTable candidateTable;
	/** Jelöltek táblázatának JScrollPaneje */
	private JScrollPane candidatePane;
	/** Jelöltes gombok panele */
	private JPanel candidateButtonsPanel;
	/** Új jelölt gomb */
	private JButton newCandidateButton;
	/** Jelölt törlése gomb */
	private JButton deleteCandidateButton;
	
	/** Save gomb panele */
	private JPanel bottomPanel;
	/** Szavazás mentése gomb */
	private JButton saveButton;
	
	/**
	 * Alapvető konstruktor.
	 * Inicializálja a bodyPanel elemeit és kinézetét, 
	 * láthatóvá teszi az ablakot. 
	 * @param frameName
	 */
	public VoteCreationWindow(String frameName) {
		super(frameName);
		initialiseBody();
		initialiseBodyLayoutAndAppearance();
		super.frame.setVisible(true);
	}

	/**
	 * A bodyPanel elemeit inicializálja és bekonfigurálja
	 */
	protected void initialiseBody() {
		leftPanel = new JPanel();
		voteNamePanel = new JPanel();
		voteNameLabel = new JLabel("Vote name:");
		voteNameField = new JTextField();
		voteNameField.setColumns(18);
		voteQuestionPanel = new JPanel();
		voteQuestionLabel = new JLabel("Vote question:");
		voteQuestionField = new JTextField();
		voteQuestionField.setColumns(16);
		voteVotersPanel = new JPanel();
		voteVotersLabel = new JLabel("Total number of voters:");
		NumberFormatter votersFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
		votersFormatter.setAllowsInvalid(false);
		votersFormatter.setMinimum(0);
		voteVotersField = new JFormattedTextField(votersFormatter);
		voteVotersField.setColumns(4);
		
		rightPanel = new JPanel();
		String[] columnNames = { "Candidate" };  
		candidateTableModel = new DefaultTableModel(null, columnNames);
		candidateTable = new JTable(candidateTableModel);
		candidateTable.setDefaultRenderer(Object.class, new NoCellBorderRenderer());
		candidateTable.setFillsViewportHeight(true);
		candidatePane = new JScrollPane(candidateTable);
		candidatePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		candidatePane.setVisible(true);
		candidateButtonsPanel = new JPanel();
		newCandidateButton = new JButton("New candidate");
		newCandidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				newCandidateButtonEvent();
			}
		});
		deleteCandidateButton = new JButton("Delete candidate");
		deleteCandidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				deleteCandidateButtonEvent();
			}
		});
		
		bottomPanel = new JPanel();
		saveButton = new JButton("Save new vote");
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
		
		Font myFont = new Font(Font.DIALOG, Font.PLAIN, 20);
		voteNameLabel.setFont(myFont);
		voteNameField.setFont(myFont);
		voteQuestionLabel.setFont(myFont);
		voteQuestionField.setFont(myFont);
		voteVotersLabel.setFont(myFont);
		voteVotersField.setFont(myFont);
		newCandidateButton.setFont(myFont);
		deleteCandidateButton.setFont(myFont);
		saveButton.setFont(myFont);
		
		leftPanel.setLayout(new GridLayout(3, 1, 15, 15));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 0));
		voteNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		voteNamePanel.add(voteNameLabel);
		voteNamePanel.add(voteNameField);
		voteQuestionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		voteQuestionPanel.add(voteQuestionLabel);
		voteQuestionPanel.add(voteQuestionField);
		voteVotersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 0));
		voteVotersPanel.add(voteVotersLabel);
		voteVotersPanel.add(voteVotersField);
		leftPanel.add(voteNamePanel);
		leftPanel.add(voteQuestionPanel);
		leftPanel.add(voteVotersPanel);
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		candidateButtonsPanel.add(newCandidateButton);
		candidateButtonsPanel.add(deleteCandidateButton);
		rightPanel.add(candidatePane);
		rightPanel.add(Box.createRigidArea(new Dimension(15, 15)));
		rightPanel.add(candidateButtonsPanel);
		
		bottomPanel.setLayout(new BorderLayout(15, 15));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		bottomPanel.add(saveButton);
		
		super.bodyPanel.add(leftPanel, BorderLayout.WEST);
		super.bodyPanel.add(rightPanel, BorderLayout.EAST);
		super.bodyPanel.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Elmenti a beirt adatokat a Vote új szavazást létrehozó konstruktorával
	 */
	private void saveButtonEvent() {
		String name = voteNameField.getText();
		String question = voteQuestionField.getText();
		int voters = Integer.parseInt(voteVotersField.getText());
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		for(int i = 0; i < candidateTableModel.getRowCount(); i++) {
			candidates.add(new Candidate((String)candidateTableModel.getValueAt(i, 0)));
		}
		new Vote(name, question, voters, candidates);
	}
	
	/**
	 * Hozzáad a jelöltek táblázatához egy új sort
	 */
	private void newCandidateButtonEvent() {
		String[] newCandidate = { "New Candidate" }; 
;		candidateTableModel.addRow(newCandidate);
	}

	/**
	 * Kitörli a kiválasztott sort a jelöltek táblázatából
	 */
	private void deleteCandidateButtonEvent() {
		candidateTableModel.removeRow(candidateTable.getSelectedRow());
	}

}
