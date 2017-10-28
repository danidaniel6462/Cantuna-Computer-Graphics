/**
 * 
 */
package futuroingeniero.gameData;

import java.io.*;
import java.util.List;

import futuroingeniero.guis.GuiTexture;

/**
 * @author Daniel Loza
 *
 */
public class Guardar {

	public static void guardarEdicionGUI(List<GuiTexture> guardarListaGUI) {
		String fichero = "res/saves/saveGui.dat";
		try {
			ObjectOutputStream ficheroSalida = new ObjectOutputStream(new FileOutputStream(fichero));
			ficheroSalida.writeObject(guardarListaGUI);
			ficheroSalida.flush();
			ficheroSalida.close();
			System.out.println("GUI guardados correctamente...");
		} catch (FileNotFoundException fnfe) {
			System.out.println("Error: El fichero no existe. ");
		} catch (IOException ioe) {
			System.out.println("Error: Fallo en la escritura en el fichero. ");
		}
	}
}