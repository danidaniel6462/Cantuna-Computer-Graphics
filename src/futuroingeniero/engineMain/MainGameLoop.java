/**
 * 
 */
package futuroingeniero.engineMain;

import static futuroingeniero.utils.GlobalConstants.SIZE_TERRAIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.guis.GuiRenderer;
import futuroingeniero.models.staticModel.models.TexturedModel;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.renderEngine.MasterRenderer;
import futuroingeniero.toolbox.Maths;
import futuroingeniero.toolbox.MousePicker;
import futuroingeniero.utils.DisplayManager;

/**
 * @author Daniel Loza
 * @version 1.0
 */

public class MainGameLoop {

	private static List<Entity> diablillos = new ArrayList<Entity>();
	private static TexturedModel modeloStatic;

	public void gameLoop() {

		// objeto para cargar los modelos en memoria para posteriormente ser
		// renderizados
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		modeloStatic = SceneEntity.modelReady(loader, "bluedevil", "bluedevil");

		Entity diablillo = new Entity(modeloStatic, "diablillo", new Vector3f(0, 0, 0), 0, 0, 0, 1);

		diablillos.add(diablillo);

		Scene scene = SceneLoader.loadScene();

		MousePicker picker = new MousePicker(scene.getCamara(), renderer.getProyeccionMatrix(),
				scene.getSceneEntity().getTerrenos().get(1));

		int gridX, gridZ;

		while (!Display.isCloseRequested()) {
			// game logic
			renderer.controlesRenderizacion(/* guis */);
			scene.getCamara().move();
			picker.update3D();
			// picker.update2D();

			gridX = (int) (scene.getCamara().getPlayer().getPosition().x / SIZE_TERRAIN + 1);
			gridZ = (int) (scene.getCamara().getPlayer().getPosition().z / SIZE_TERRAIN + 1);
			scene.getCamara().getPlayer().move(SceneEntity.terrain[gridX][gridZ]);

			Vector3f terrainPoint = picker.getCurrentTerrainPoint();

			if (terrainPoint != null) {
				for (Entity item : scene.getSceneEntity().getItems()) {
					if (item.getNombre() == "lampMovimiento")
						item.setPosition(terrainPoint);
				}
				for (Light luz : scene.getSceneEntity().getLuces()) {
					if (luz.getNombre() == "lucesita")
						luz.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));
				}
				if (Mouse.isButtonDown(0)) {
					Entity temp = Maths.getElementoEsfera(terrainPoint, diablillos);
					if (temp != null) {
						temp.setPosition(terrainPoint);
					}
				}
			}
			scene.getSceneEntity().update();

			// render
			renderer.render(scene);
			guiRenderer.render(scene);

			// diablillo
			for (Entity diablito : diablillos) {
				renderer.procesarEntidad(diablito);
				diablito.increaseRotation(0, 1, 0);
			}

			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
	}

	// codificación en consola para agregar entidades por medio del teclado
	// creado para posteriormente crear una interfaz para realizar el mismo
	// procedimiento
	public static void crearEntidad() {

		Random random = new Random();

		float x = random.nextFloat() * 100 - 50;
		float z = random.nextFloat() * -300;
		diablillos
				.add(new Entity(modeloStatic, "diablillo", new Vector3f(x, 0, z), 0, random.nextFloat() * 360, 0, 1f));
	}

	/**
	 * Método principal que se ejecuta para iniciar la aplicación
	 * 
	 * @param args
	 *            Argumento del Main
	 */
	public static void main(String[] args) {
		new MainGameLoop().run();
	}

	/**
	 * Método que ejecuta el juego
	 */
	public void run() {
		// creamos la pantalla para visualizar lo que pasa en el juego
		DisplayManager.createDisplay();
		// loop del juego
		gameLoop();
		// terminamos la ejecución del juego
		DisplayManager.closeDisplay();
	}
}
