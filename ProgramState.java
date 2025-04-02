import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A program állapotváltozóit kezelő osztály.
 */
public class ProgramState {
	
	/** Az éppen bejelentkezett profil, ha nincs =null */
	private static AdminProfile loggedIn = null;
	
	/** A jelenleg futó szavazás, ha nincs =null */
	private static Vote runningVote = null;
	
	/** A login ablak létezése */
	private static boolean loginWindowExists = false;
	
	/** Enum ahhoz, hogy éppen melyik ablakon vagyunk */
	enum window{ CONFIG, VOTING }
	/** A pillanatnyi ablakot eltároló változó */
	private static window currentWindow = window.CONFIG;
	
	/**
	 * Getter a pillanatnyi ablakhoz
	 * @return A jelenlegi ablak
	 */
	public static window getCurrentWindow() {
		return currentWindow;
	}
	
	/**
	 * A jelenlegi ablak tipusát átváltja a másikra 
	 */
	public static void switchWindow() {
		switch(currentWindow) {
			case CONFIG:
				currentWindow = window.VOTING;
				break;
			case VOTING:
				currentWindow = window.CONFIG;
				break;
		}
	}
	
	/**
	 * Getter a login ablak létezéséhez
	 * @return True, ha létezik a login window. False, ha nem.
	 */
	public static boolean getLoginWindowState() {
		return loginWindowExists;
	}
	
	/**
	 * Setter a login ablak állapotához
	 * @param exists Amire állitani szeretnénk (true vagy false)
	 */
	public static void setLoginWindowState(boolean exists) {
		loginWindowExists = exists;
	}
	
	/**
	 * Getter a belépett profilhoz
	 * @return A belépett profil (ha nincs, akkor null)
	 */
	public static AdminProfile getLoggedInProfile() {
		return loggedIn;
	}
	
	/**
	 * Setter a belépett profilhoz
	 * @param ap A belépettnek beállitandó profil
	 */
	public static void setLoggedInProfile(AdminProfile ap) {
		loggedIn = ap;
	}
	
	/**
	 * Getter a futó szavazáshoz
	 * @return A futó szavazás (ha nincs, akkor null)
	 */
	public static Vote getRunningVote() {
		return runningVote;
	}
	
	/**
	 * Setter a futó szavazáshoz
	 * @param rv A futónak beaállitandó szavazás
	*/
	public static void setRunningVote(Vote rv) {
		runningVote = rv;
	}
	
	/**
	 * A szavazás állapotát elmenti egy !helper nevű fileba
	 * (ha nem létezik létrehozza).
	 * <p>Ha van futó szavazás, akkor annak a nevét irja be a fileba, 
	 * ha nincs, akkor pedig egy kötőjelet.</p>
	 */
	public static void saveVotingState() {
		File f = new File("Votes/!helper");
		if(!f.exists()) {
			try {
				f.createNewFile();
			}catch(Exception e) {
				PopUp.show("Error", "Unable to create helper file!");
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath()));
			if(getRunningVote() == null) {
				bw.write("-");
			}else {
				bw.write(getRunningVote().getName());
			}
			bw.close();
		}catch(Exception e) {
			PopUp.show("Error", "Unable to save voting state!");
		}
	}
	
	/**
	 * Betölti a szavazás állapotát a !helper fileból
	 */
	public static void loadVotingState() {
		File f = new File("Votes/!helper");
		if(f.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
				String s = br.readLine();
				if(!s.equals("-")) {
					runningVote = IO.loadVote(s);
				}
				br.close();
			}catch(Exception e) {
				PopUp.show("Error", "Unable to load voting state!");
			}
		}else {
			saveVotingState();
		}
	}
}
