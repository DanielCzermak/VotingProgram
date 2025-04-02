import java.util.ArrayList;
import java.util.Random;

/**
 * Szavazói kulcsok kezelésére és tárolására létrehozott osztály
 */
public class VKeyList {
	/** Kulcsokat String-ként tároló lista */
	ArrayList<String> keys;
	
	/**
	 * Új szavazás létrehozásakor meghivódó konstruktor
	 * @param n A generálandó kulcsok száma
	 */
	public VKeyList(int n) {
		keys = new ArrayList<String>();
		char[] chars = new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		        '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		int remainingKeys = n;
		Random r = new Random();
		while(remainingKeys != 0) {
			String k = "";
			for(int i = 0; i < 8; i++) {
				k += (chars[r.nextInt(chars.length-1)]);
			}
			if(!keys.contains(k)) {
				keys.add(k);
				remainingKeys--;
			}
		}
	}
	
	/**
	 * Kulcsok betöltésekor használatos konstruktor
	 * @param kl Egy kulcsokat tároló file tartalma ArrayList-ben
	 */
	public VKeyList(ArrayList<String> kl) {
		this.keys = kl;
	}
	
	/**
	 * Getter a kulcsok listájához
	 * @return A kulcsokat tároló lista
	 */
	public ArrayList<String> getKeyList(){
		return this.keys;
	}
	
	/**
	 * Getter a kulcsok számához
	 * @return Egy szavazáshoz kötött kulcsok száma
	 */
	public int getKeysCount() {
		return keys.size();
	}
	
	/**
	 * "Felhasznál" egy kulcsot és kitörli a listából.
	 * Ellenőrzi a kulcs létezését és, ha létezik ezt jelzi return értékként.
	 * @param k Egy kulcsként ellenőrzendő String 
	 * @return Igaz, ha sikeres volt a kulcs felhasználása, hamis, ha nem
	 */
	public boolean useKey(String k) {
		if(keys.contains(k)) {
			keys.remove(k);
			return true;
		}else {
			return false;
		}
	}
}
