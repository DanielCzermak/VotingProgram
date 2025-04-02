/**
 * Az admin profilok könnyebb kezeléséhez létrehozott osztály.
 */
public class AdminProfile {
	/** Felhasználónév */
	private String username;
	/** Jelszó */
	private String password;
	
	/**
	 * Alapvető konstruktor
	 * @param u Felhasználónév 
	 * @param p Jeszó
	 */
	public AdminProfile(String u, String p) {
		this.username = u;
		this.password = p;
	}
	
	/**
	 * Getter a felhasználónévhez
	 * @return A profil felhasználóneve
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Getter a jelszóhoz
	 * @return A profil jelszava
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Setter a felhasználónévhez
	 * @param newUsername Beállítandó új felhasználónév
	 */
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}
	
	/**
	 * Setter a jelszóhoz
	 * @param newPassword Beállítandó új jelszó
	 */
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	
	/**
	 * Ellenőrzi a paraméterként megadott felhasználónevet és jelszót.
	 * Ha megfelelnek az adott profil adatainak, beállítja
	 * belépett profilnak azt.
	 * @param enteredUsername Ellenőrzendő felhasználónév
	 * @param enteredPassword Ellenőrzendő jelszó
	 * @return Sikeres belépés esetén true, másképp false.
	 */
	public boolean logIn(String enteredUsername, String enteredPassword) {
		if(this.username.equals(enteredUsername) && this.password.equals(enteredPassword)) {
			ProgramState.setLoggedInProfile(this);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * A belépett profilt "kilépteti", ezért a belépett profilt
	 * null-ra állítja.
	 */
	public static void logOut() {
		ProgramState.setLoggedInProfile(null);
	}
}
