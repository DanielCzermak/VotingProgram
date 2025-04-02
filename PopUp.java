import javax.swing.JOptionPane;

/**
 * JOptionPane-eket létrehozó osztály, könnyebb elérésért lett létrehozva.
 */
public class PopUp {
	/**
	 * 
	 * @param title A felugró ablak titulusa
	 * @param message A felugró ablak szövege
	 */
	public static void show(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 
	 * @param title A felugró ablak titulusa
	 * @param message A felugró ablak szövege
	 * @return Az ablakra adott válasz értéke (-1: bezárt, 0: igen ,1: nem)
	 */
	public static int showYesNo(String title, String message) {
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
	}
}
