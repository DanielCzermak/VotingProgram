/**
 * Main metódusa a programnak.
 * <p>Ellenőrzi, hogy a default fileok és mappák léteznek-e, 
 * ha nem, létrehozza őket. </p>
 * <p>Ha van elinditott választás, betölti azt.</p>
 * <p>Létrehoz egy login ablakot és a voting screent.</p>
 */
public class Main {

	public static void main(String[] args) {	
		IO.createFolders();
		ProgramState.loadVotingState();
		new LoginWindow();
		new ConfigScreen();
	}

}
