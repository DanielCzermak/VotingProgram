/**
 * A szavazások jelöltjeinek könnyebb kezelésére létrehozott osztály.
 */
public class Candidate {
	/** A jelölt neve */
	private String name;
	/** A jelöltre leadott szavazatok száma */
	private int votes;
	
	/**
	 * Teljesen új jelölt létrehozásakor használt konstruktor
	 * @param n Az új jelölt neve
	 */
	public Candidate(String n) {
		this.name = n;
		this.votes = 0;
	}
	
	/**
	 * Jelölt betöltésénél használt konstruktor
	 * @param nm A betöltött jelölt neve
	 * @param vt A betöltött jelölt szavazatainak száma
	 */
	public Candidate(String nm, int vt) {
		this.name = nm;
		this.votes = vt;
	}
	
	/**
	 * Getter a jelölt nevéhez
	 * @return A jelölt neve
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter a jelölt szavazatainak számához
	 * @return A jelölt szavazatainak száma
	 */
	public int getVotes() {
		return this.votes;
	}
	
	/**
	 * Szavazat leadásánál ezzel növeljük a jelölt szavazatainak számát
	 */
	public void castVote() {
		this.votes += 1;
	}
	
	/**
	 * Mentésnél már menthető formátumú Stringet hoz létre a jelölt adataiból
	 * @return Mentésre kész String
	 */
	public String toPrintable() {
		return name + ";" + votes;
	}
}
