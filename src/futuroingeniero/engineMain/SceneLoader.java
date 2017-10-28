/**
 * 
 */
package futuroingeniero.engineMain;

import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Player;
import futuroingeniero.models.staticModel.models.TexturedModel;
import futuroingeniero.renderEngine.Loader;

/**
 * @author Daniel Loza
 *
 */
public class SceneLoader {

	public static Scene loadScene() {
		Loader loader = new Loader();
		TexturedModel cantunaTexture = SceneEntity.modelReady(loader, "cantunaCharacter", "cantunaPonchoTexture");
		Player player = new Player(cantunaTexture, "cantuna", new Vector3f(0, 0, -15f), 0, 90, 0, 1f);
		Camara camara = new Camara(player);
		
		SceneGUI GUIs = new SceneGUI();
		GUIs.init(loader);
		
		Scene scene = new Scene(camara, GUIs, SceneEntity.init(loader));

		return scene;
	}
}
