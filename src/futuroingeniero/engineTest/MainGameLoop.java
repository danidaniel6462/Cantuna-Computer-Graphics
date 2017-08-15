/**
 * 
 */
package futuroingeniero.engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.RawModel;
import futuroingeniero.models.TextureModel;
import futuroingeniero.renderEngine.DisplayManager;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.renderEngine.MasterRenderer;
import futuroingeniero.renderEngine.OBJLoader;
import futuroingeniero.renderEngine.Renderer;
import futuroingeniero.shaders.StaticShader;
import futuroingeniero.textures.ModelTexture;

/**
 * @author Daniel Loza
 * @version 1.0
 */

public class MainGameLoop {

	/**
	 * Método principal que se ejecuta para iniciar la aplicación
	 * @param args argumento 
	 */
	
	// private static float  x = 0, y = 0, z = 0;
	public static void main(String[] args) {		
		// creamos la pantalla para visualizar lo que pasa en el juego
		DisplayManager.createDisplay();
		// objeto para cargar los modelos en memoria para posteriormente ser renderizados
		Loader loader = new Loader();
		
		// carga de los modelos 3D dentro del escenario 
		RawModel model = OBJLoader.loadObjModel("gaby", loader);
		RawModel modelStall = OBJLoader.loadObjModel("stall", loader);
		RawModel cuboModel = OBJLoader.loadObjModel("cubo", loader);
		// carga de texturas de cada modelo
		TextureModel staticModel = new TextureModel(model, new ModelTexture(loader.loadTexture("gabyTexture")));
		TextureModel staticModelStall = new TextureModel(modelStall, new ModelTexture(loader.loadTexture("stallTexture")));
		TextureModel cubo = new TextureModel(cuboModel, new ModelTexture(loader.loadTexture("BlackColor")));
		
		ModelTexture textureStall = staticModelStall.getTexture();
		textureStall.setShineDamper(10);
		textureStall.setReflectivity(1);
		
		// las entidades son los modelos 3D que cargamos en el escenario
		Entity entidad = new Entity(staticModel, new Vector3f(5, 0, -20), 0, 0, 0, 1);
		Entity entidadStall = new Entity(staticModelStall, new Vector3f(-5, -2.5f, -20), 0, 0, 0, 1);
		
		List<Entity> variosCubos = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i = 0; i < 200; i++) {
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			variosCubos.add(new Entity(cubo, new Vector3f(x,  y,  z), random.nextFloat() * 180f, random.nextFloat() * 180f, random.nextFloat() * 180f, 1f));			
		}
		
		// creamos una luz en el escenario
		Light luz = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));
		
		// creación de la cámara a utilizar 
		Camara camera = new Camara();
				
		MasterRenderer renderer = new MasterRenderer();
		// bucle del juego real
		// donde ocurren todas las actualizaciones
		while(!Display.isCloseRequested()) {
			// game logic
			entidad.increaseRotation(0, 1, 0);
			entidadStall.increaseRotation(0, 1, 0);
			camera.movimiento();
			
			// render
			renderer.render(luz, camera);
			
			for (Entity cube : variosCubos) {
				renderer.procesarEntidad(cube);
			}
			
			renderer.procesarEntidad(entidadStall);
			renderer.procesarEntidad(entidad);

			DisplayManager.updateDisplay();	
		}
		renderer.cleanUp();
		loader.claenUp();
		DisplayManager.closeDisplay();
	}
	
	
	/*
	public static void controles() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
		    System.out.println("SPACE KEY IS RIGHT");
		    x += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
		    System.out.println("SPACE KEY IS LEFT");
		    x -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
		    System.out.println("SPACE KEY IS UP");
		    y += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
		    System.out.println("SPACE KEY IS DOWN");
		    y -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
		    System.out.println("SPACE KEY IS LCONTROL");
		    z += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
		    System.out.println("SPACE KEY IS RCONTROL");
		    z -= 0.2f;
		}
		
	}
	*/
	
	
}
