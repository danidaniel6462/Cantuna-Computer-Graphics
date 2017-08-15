/**
 * 
 */
package futuroingeniero.engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.RawModel;
import futuroingeniero.models.TexturedModel;
import futuroingeniero.renderEngine.DisplayManager;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.renderEngine.MasterRenderer;
import futuroingeniero.renderEngine.OBJLoader;
import futuroingeniero.terrains.Terrain;
import futuroingeniero.textures.ModelTexture;

/**
 * @author Daniel Loza
 * @version 1.0
 */

public class MainGameLoop {

	/**
	 * M�todo principal que se ejecuta para iniciar la aplicaci�n
	 * @param args argumento 
	 */
	
	// private static float  x = 0, y = 0, z = 0;
	private static int index = 0; 
	
	private static Random random = new Random();
	private static List<Entity> gaviotas = new ArrayList<Entity>();
	private static TexturedModel modeloStatic; 
	
	
	public static void main(String[] args) {		
		// creamos la pantalla para visualizar lo que pasa en el juego
		DisplayManager.createDisplay();
		// objeto para cargar los modelos en memoria para posteriormente ser renderizados
		Loader loader = new Loader();
		
		// carga de los modelos 3D dentro del escenario 
		RawModel model = OBJLoader.loadObjModel("gaby", loader);
		RawModel modelStall = OBJLoader.loadObjModel("stall", loader);
		RawModel cuboModel = OBJLoader.loadObjModel("cubo", loader);
		RawModel arbolModel = OBJLoader.loadObjModel("tree", loader);
		
		// carga de texturas de cada modelo
		modeloStatic = new TexturedModel(model, new ModelTexture(loader.loadTexture("gabyTexture")));
		TexturedModel staticModelStall = new TexturedModel(modelStall, new ModelTexture(loader.loadTexture("stallTexture")));
		TexturedModel cubo = new TexturedModel(cuboModel, new ModelTexture(loader.loadTexture("BlackColor")));
		TexturedModel arbolTextura = new TexturedModel(arbolModel , new ModelTexture(loader.loadTexture("tree")));
		
		ModelTexture textureStall = staticModelStall.getTexture();
		textureStall.setShineDamper(10);
		textureStall.setReflectivity(1);
		
		// las entidades son los modelos 3D que cargamos en el escenario
		Entity entidad = new Entity(modeloStatic, new Vector3f(5, 0, -20), 0, 0, 0, 1);
		Entity entidadStall = new Entity(staticModelStall, new Vector3f(-5, 0, -20), 0, 0, 0, 1);
		
		List<Entity> variosCubos = new ArrayList<Entity>();
		
		gaviotas.add(entidad);
		
		List<Entity> arboles = new ArrayList<Entity>();
		
		Random random = new Random();
		
		// �rboles
		for (int i = 0; i < 500; i++) {
			arboles.add(new Entity(arbolTextura,
					new Vector3f(random.nextFloat() * 1600 - 800, 0, random.nextFloat() * -800), 0, 0, 0, 3));
		}
		
		// cubos
		for (int i = 0; i < 100; i++) {
			float x = random.nextFloat() * 200 - 100;
			float y = random.nextFloat() * 200 - 100;
			float z = random.nextFloat() * -300;
			variosCubos.add(new Entity(cubo, new Vector3f(x,  y,  z), random.nextFloat() * 180f, random.nextFloat() * 180f, random.nextFloat() * 180f, 1f));			
		}
		
		// creamos una luz en el escenario
		Light luz = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));
		
		Terrain terreno = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("cesped")));
		Terrain terreno2 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		
		// creaci�n de la c�mara a utilizar 
		Camara camera = new Camara();
				
		MasterRenderer renderer = new MasterRenderer();
		// bucle del juego real
		// donde ocurren todas las actualizaciones
		while(!Display.isCloseRequested()) {
			// game logic
			entidad.increaseRotation(0, 1, 0);
			camera.movimiento();
			
			renderer.procesarTerreno(terreno);
			renderer.procesarTerreno(terreno2);
			
			// render
			renderer.render(luz, camera);

			crearEntidad();
			
			
			// �rboles
			for (Entity arbol : arboles) {
				renderer.procesarEntidad(arbol);
				arbol.increaseRotation(0, 1, 0);
			}
			// cubos
			for (Entity cube : variosCubos) {
				renderer.procesarEntidad(cube);
			}
			// bar
			renderer.procesarEntidad(entidadStall);
			// Gaby
			for (Entity gaby : gaviotas) {
				renderer.procesarEntidad(gaby);
			}

			
			DisplayManager.updateDisplay();	
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	// codificaci�n en consola para agregar entidades por medio del teclado
	// creado para posteriormente crear una interfaz para realizar el mismo procedimiento 
	public static void crearEntidad() {
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				    System.out.println("ENTER KEY IS PRESSED");
					float x = random.nextFloat() * 100 - 50;
					float z = random.nextFloat() * -300;   
				    gaviotas.add(new Entity(modeloStatic, new Vector3f(x,  0,  z), 0, 0, random.nextFloat() * 360, 1f));
				    System.out.println("Array Gaviotas tama�o: " + gaviotas.size());
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				    System.out.println("UP Key is pressed");
				    index += 1;
				    System.out.println("index: " + index);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				    System.out.println("SPACE Key is pressed");
				    gaviotas.remove(index);
				    System.out.println("Gaviota eliminada num: " + index);
				    System.out.println("nuevo tama�o de Gaviotas: " + gaviotas.size());
				}
		    } else {
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
				    System.out.println("ENTER Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				    System.out.println("UP Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				    System.out.println("SPACE Key is Released");
				}
		    }
		}
		
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
