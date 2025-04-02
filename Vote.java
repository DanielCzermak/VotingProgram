import java.util.*;

/**
 * Egy szavazást reprezentáló osztály
 */
public class Vote {
	/** Szavazás állapota (0 = létrehozott, 1 = futó, 2 = lezárt) */
	private int state;
	/** Szavazás neve */
	private String name;
	/** Szavazs kérdése */
	private String question;
	/** Szavazás maximum szavazóinak száma */
	private int possibleVoters;
	/** Szavazás jelöltjeinek listája */
	private ArrayList<Candidate> candidates;
	/** Szavazáshoz generált kulcsok listája */
	private VKeyList keys;
	
	/**
	 * Új szavazás létrehozásánál használt konstruktor, ez a változók
	 * beállitása után menti is fileba.
	 * @param name Új szavazás neve
	 * @param question Új szavazás kérdése
	 * @param voters Új szavazás szavazóinak száma
	 * @param candidates Új szavazás jelöltjeinek listája
	 */
	public Vote(String name, String question, int voters, ArrayList<Candidate> candidates) {
		this.state = 0;
		this.name = name;
		this.question = question;
		this.possibleVoters = voters;
		this.candidates = candidates;
		this.keys = new VKeyList(voters);
		IO.saveVote(this);
	}
	
	/**
	 * Betöltésnél használt konstruktor
	 * @param state Betöltendő állapot
	 * @param name Betöltendő név
	 * @param question Betöltendő kérdés
	 * @param voters Betöltendő szavazószám
	 * @param candidates Betöltendő jelöltlista
	 * @param keys Betöltendő kulcslista
	 */
	public Vote(int state, String name, String question, int voters, ArrayList<Candidate> candidates, VKeyList keys) {
		this.state = state;
		this.name = name;
		this.question = question;
		this.possibleVoters = voters;
		this.candidates = candidates;
		this.keys = keys;
	}
	
	/**
	 * Getter egy szavazás állapotához
	 * @return Egy szavazáss állapota
	 */
	public int getState() {
		return this.state;
	}
	
	/**
	 * Getter egy szavazás nevéhez
	 * @return Egy szavazás neve
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter egy szavazás kérdéséhez
	 * @return Egy szavazáshoz tartozó kérdés
	 */
	public String getQuestion() {
		return this.question;
	}
	
	/**
	 * Getter egy szavazás lehetséges szavazóinak számához
	 * @return Egy szavazás lehetséges szavazóinak száma
	 */
	public int getPossibleVotes() {
		return this.possibleVoters;
	}
	
	/**
	 * Getter egy szavazás jelöltjéhez index szerinti kereséssel
	 * @param index Keresett jelölt indexe
	 * @return Egy Candidate vagy, ha OutOfRange az index, null
	 */
	public Candidate getCandidate(int index) {
		if(candidates.size() <= index) {
			return null;
		}else {
			return candidates.get(index);
		}
	}
	
	/**
	 * Getter egy szavazás jelöltjéhez név szerinti kereséssel
	 * @param candidateName Keresett jelölt neve
	 * @return A keresett Candidate vagy, ha nem találtuk null
	 */
	public Candidate getCandidate(String candidateName) {
		for(Candidate c : candidates) {
			if(c.getName().equals(candidateName)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Getter a jelöltek számához
	 * @return A jelöltek száma
	 */
	public int getNumberOfCandidates(){
		return candidates.size();
	}
	
	/**
	 * Getter a kulcsok listájához
	 * @return A szavazás kulcs-listája
	 */
	public VKeyList getKeyList() {
		return keys;
	}
	
	/**
	 * Átállitja egy futó szavazás állapotát futóra, valamint
	 * jelzi a programnak és menti fileba a szavazást.
	 * @return Igaz, ha sikeres az inditás. Hamis, ha nem.
	 */
	public boolean startVote() {
		if(this.state == 0) {
			this.state = 1;
			ProgramState.setRunningVote(this);
			IO.saveVote(this);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Átállitja egy futó szavazás állapotát lezártra, valamint
	 * ezt jelzi a programnak és menti fileba a szavazást.
	 * @return True, ha sikeres a lezárás. False, ha nem.
	 */
	public boolean closeVote() {
		if(this.state == 1) {
			this.state = 2;
			ProgramState.setRunningVote(null);
			IO.saveVote(this);
			return true;
		}else {
			return false;
		}
	}
	
}
