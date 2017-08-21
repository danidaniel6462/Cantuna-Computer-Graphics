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
import futuroingeniero.entities.Player;
import futuroingeniero.models.RawModel;
import futuroingeniero.models.TexturedModel;
import futuroingeniero.objConverter.ModelData;
import futuroingeniero.objConverter.OBJFileLoader;
import futuroingeniero.renderEngine.DisplayManager;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.renderEngine.MasterRenderer;
import futuroingeniero.renderEngine.OBJLoader;
import futuroingeniero.terrains.Terrain;
import futuroingeniero.textures.ModelTexture;
import futuroingeniero.textures.TerrainTexture;
import futuroingeniero.textures.TerrainTexturePack;

/**
 * @author Daniel Loza
 * @version 1.0
 */

public class MainGameLoop {

	private static int index = 0;

	private static Random random = new Random();
	private static List<Entity> diablillo = new ArrayList<Entity>();
	private static TexturedModel modeloStatic;
	
	public static Player player;

	public static void ejecutarCantuna() {
		// creamos la pantalla para visualizar lo que pasa en el juego
		DisplayManager.createDisplay();
		// objeto para cargar los modelos en memoria para posteriormente ser
		// renderizados
		Loader loader = new Loader();

		// ****** texturas para el blendMap********//

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture miBlendMap = new TerrainTexture(loader.loadTexture("miBlendMap"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *******************************************//

		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData stallData = OBJFileLoader.loadOBJ("stall");
		ModelData cubeData = OBJFileLoader.loadOBJ("cubo");
		ModelData devilData = OBJFileLoader.loadOBJ("bluedevil");
		ModelData steveData = OBJFileLoader.loadOBJ("luchOvejas");
		ModelData boxData = OBJFileLoader.loadOBJ("box");
		
		// carga de los modelos 3D dentro del escenario
		RawModel stallModel = loader.loadToVAO(stallData.getVertices(), stallData.getTextureCoords(),
				stallData.getNormals(), stallData.getIndices());
		RawModel arbolModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(),
				treeData.getNormals(), treeData.getIndices());
		RawModel cuboModel = loader.loadToVAO(cubeData.getVertices(), cubeData.getTextureCoords(),
				cubeData.getNormals(), cubeData.getIndices());
		RawModel devilModel = loader.loadToVAO(devilData.getVertices(), devilData.getTextureCoords(),
				devilData.getNormals(), devilData.getIndices());
		RawModel steveModel = loader.loadToVAO(steveData.getVertices(), steveData.getTextureCoords(),
				steveData.getNormals(), steveData.getIndices());
		RawModel boxModel = loader.loadToVAO(boxData.getVertices(), boxData.getTextureCoords(),
				boxData.getNormals(), boxData.getIndices());
		

		RawModel arbolLowpolyModel = OBJLoader.loadObjModel("lowPolyTree", loader);

		// carga de texturas de cada modelo
		modeloStatic = new TexturedModel(devilModel, new ModelTexture(loader.loadTexture("bluedevil")));
		TexturedModel staticModelStall = new TexturedModel(stallModel,
				new ModelTexture(loader.loadTexture("stallTexture")));
		TexturedModel cubo = new TexturedModel(cuboModel, new ModelTexture(loader.loadTexture("BlackColor")));
		TexturedModel arbolTextura = new TexturedModel(arbolModel, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel arbolLowPolyTextura = new TexturedModel(arbolLowpolyModel,
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("flower")));
		TexturedModel steveTexture = new TexturedModel(steveModel,
				new ModelTexture(loader.loadTexture("luchovejasTexture")));
		TexturedModel boxTexture = new TexturedModel(boxModel,
				new ModelTexture(loader.loadTexture("box")));

		// modelo que obtendrá características de brillo y material
		ModelTexture textureStall = staticModelStall.getTexture();
		textureStall.setShineDamper(10);
		textureStall.setReflectivity(1);

		// características de las texturas
		grass.getTexture().setTieneTransparencia(true);
		grass.getTexture().setUsaFalsaIluminacion(true);
		flower.getTexture().setTieneTransparencia(true);
		flower.getTexture().setUsaFalsaIluminacion(true);
		fern.getTexture().setTieneTransparencia(true);

		Terrain[][] terrain;
		terrain = new Terrain[2][2];
		terrain[0][0] = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		terrain[1][0] = new Terrain(0, -1, loader, texturePack, miBlendMap, "heightmap");
		terrain[0][1] = new Terrain(-1, 0, loader, texturePack, blendMap, "heightmap");
		terrain[1][1] = new Terrain(0, 0, loader, texturePack, miBlendMap, "heightmap");

		Terrain terrenoActual;
		
		List<Terrain> terrenos = new ArrayList<Terrain>();
		for (int i = 0; i < terrain.length; i++)		
			for (int j = 0; j < terrain[0].length; j++)	
				terrenos.add(terrain[i][j]);
		
		// las entidades son los modelos 3D que cargamos en el escenario
		Entity diablillos = new Entity(modeloStatic, new Vector3f(10, 0, -20), 0, 0, 0, 1);
		Entity entidadStall = new Entity(staticModelStall, new Vector3f(-5, 0, -20), 0, 0, 0, 1);

		List<Entity> boxes = new ArrayList<Entity>();
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> arbolesLowPoly = new ArrayList<Entity>();
		List<Entity> variosCubos = new ArrayList<Entity>();
		diablillo.add(diablillos);
		List<Entity> arboles = new ArrayList<Entity>();

		for (int i = 0; i < 5; i++) {
			boxes.add(new Entity(boxTexture,
					new Vector3f(90, terrain[1][0].getHeighOfTerrain(90, -50 * (i + 1)) + 3, -50 * (i + 1)), 0, 0, 0, 3));
		}
		
		for (int i = 0; i < 6000; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / Terrain.getSize() + 1)][(int) (z / Terrain.getSize() + 1)];
				float y = terrenoActual.getHeighOfTerrain(x, z);
				entities.add(new Entity(flower,
						new Vector3f(x, y, z), 0, 0, 0, 1f));
				x = random.nextFloat() * 1600 - 800;
				z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / Terrain.getSize() + 1)][(int) (z / Terrain.getSize() + 1)];
				y = terrenoActual.getHeighOfTerrain(x, z);
				entities.add(new Entity(grass,
						new Vector3f(x, y, z), 0, 0, 0, 1));
			}
			if (i % 7 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				int gridX = (int) (x / Terrain.getSize() + 1);
				int gridZ = (int) (z / Terrain.getSize() + 1);
				float y = terrain[gridX][gridZ].getHeighOfTerrain(x, z);
				entities.add(new Entity(fern,
						new Vector3f(x, y, z), 0, 0, 0, 0.6f));
			}
		}

		// árboles
		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 1600 - 800;
			float z = random.nextFloat() * -800;
			terrenoActual = terrain[(int) (x / Terrain.getSize() + 1)][(int) (z / Terrain.getSize() + 1)];
			float y = terrenoActual.getHeighOfTerrain(x, z);
			arboles.add(new Entity(arbolTextura,
					new Vector3f(x, y, z), 0, 0, 0, 3));
		}
		
		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 1600 - 800;
			float z = random.nextFloat() * -800;
			terrenoActual = terrain[(int) (x / Terrain.getSize() + 1)][(int) (z / Terrain.getSize() + 1)];
			float y = terrenoActual.getHeighOfTerrain(x, z);
			arbolesLowPoly.add(new Entity(arbolLowPolyTextura,
					new Vector3f(x, y, z), 0, random.nextFloat() * 360,
					0, 0.5f));
		}
		
		// cubos
		for (int i = 0; i < 100; i++) {
			float x = random.nextFloat() * 200 - 100;
			float y = random.nextFloat() * 200 - 100;
			float z = random.nextFloat() * -300;
			variosCubos.add(new Entity(cubo, new Vector3f(x, y, z), random.nextFloat() * 180f,
					random.nextFloat() * 180f, random.nextFloat() * 180f, 1f));
		}

		// creamos una luz en el escenario
		Light luz = new Light(new Vector3f(2000, 2000, 1000), new Vector3f(1, 1, 1));

		// Player del videojuego 
		player = new Player(steveTexture, new Vector3f(100, 0, -50), 0, 180, 0, 1f);
		// creación de la cámara a utilizar
		Camara camera = new Camara(player);

		MasterRenderer renderer = new MasterRenderer();
		
		// bucle del juego real
		// donde ocurren todas las actualizaciones
		while (!Display.isCloseRequested()) {
			// game logic
			diablillos.increaseRotation(0, 1, 0);
			entidadStall.increaseRotation(0, 1, 0);
			renderer.controlesRenderizacion();
			camera.move();
			
			int gridX = (int) (player.getPosition().x / Terrain.getSize() + 1);
			int gridZ = (int) (player.getPosition().z / Terrain.getSize() + 1);
			player.move(terrain[gridX][gridZ], entidadStall);

			for (Terrain terreno : terrenos) {
				renderer.procesarTerreno(terreno);
			}
			
			renderer.procesarEntidad(player);
			for (Entity box : boxes) {
				renderer.procesarEntidad(box);
			}
			
			// render
			renderer.render(luz, camera);

			crearEntidad();
			
			for (Entity entidad1 : entities) {
				renderer.procesarEntidad(entidad1);
			}
			// árboles
			for (Entity arbol : arboles) {
				renderer.procesarEntidad(arbol);
				arbol.increaseRotation(0, 1, 0);
			}
			for (Entity arbol : arbolesLowPoly) {
				renderer.procesarEntidad(arbol);
			}
			// cubos
			for (Entity cube : variosCubos) {
				renderer.procesarEntidad(cube);
			}
			// bar
			renderer.procesarEntidad(entidadStall);
			// Gaby
			for (Entity gaby : diablillo) {
				renderer.procesarEntidad(gaby);
			}

			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	/**
	 * Método principal que se ejecuta para iniciar la aplicación
	 * 
	 * @param args
	 *            argumento
	 */
	public static void main(String[] args) {
		ejecutarCantuna();
	}

	// codificación en consola para agregar entidades por medio del teclado
	// creado para posteriormente crear una interfaz para realizar el mismo
	// procedimiento
	public static void crearEntidad() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
					System.out.println("ENTER KEY IS PRESSED");
					float x = random.nextFloat() * 100 - 50;
					float z = random.nextFloat() * -300;
					diablillo.add(new Entity(modeloStatic, new Vector3f(x, 0, z), 0, random.nextFloat() * 360, 0, 1f));
					System.out.println("Array Gaviotas tamaño: " + diablillo.size());
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					System.out.println("UP Key is pressed");
					index += 1;
					System.out.println("index: " + index);
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL) {
					System.out.println("SPACE Key is pressed");
					diablillo.remove(index);
					System.out.println("Gaviota eliminada num: " + index);
					System.out.println("nuevo tamaño de Gaviotas: " + diablillo.size());
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					System.out.println("ENTER Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					System.out.println("UP Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL) {
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
	*/}
