/**
 * 
 */
package futuroingeniero.engineMain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import futuroingeniero.guis.GuiTexture;
import futuroingeniero.renderEngine.Loader;

/**
 * @author Daniel Loza
 *
 */
public class SceneGUI {

	private static List<GuiTexture> guis;

	/**
	 * @return the guis
	 */
	public List<GuiTexture> getGuis() {
		return guis;
	}

	public void init(Loader loader) {
		// lista de GUI para el videojuego
		guis = new ArrayList<GuiTexture>();
		GuiTexture guiUceLogo = new GuiTexture(loader.loadTexture("guis/uce"), new Vector2f(0.8f, 0.6f),
				new Vector2f(0.15f, 0.25f));
		GuiTexture guiUceLogo1 = new GuiTexture(loader.loadTexture("guis/uce"), new Vector2f(-0.65f, 0f),
				new Vector2f(0.15f, 0.25f));
		GuiTexture guiVida = new GuiTexture(loader.loadTexture("guis/vida1"), new Vector2f(-0.65f, -0.7f),
				new Vector2f(0.2f, 0.08f));
		// guis.add(guiCartesiano);
		guis.add(guiUceLogo);
		guis.add(guiVida);
		guis.add(guiUceLogo1);
	}
}