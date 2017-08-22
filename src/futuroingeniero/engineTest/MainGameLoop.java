/**
 * 
 */
package futuroingeniero.engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.entities.Player;
import futuroingeniero.guis.GuiRenderer;
import futuroingeniero.guis.GuiTexture;
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

import static futuroingeniero.renderEngine.GlobalConstants.*;

/**
 * @author Daniel Loza
 * @version 1.0
 */

public class MainGameLoop {

	// private static int index = 0;
	
	private static Random random = new Random();
	private static List<Entity> diablillo = new ArrayList<Entity>();
	private static TexturedModel modeloStatic;
	
	public static Player player;
	
	/**
	 * Método principal que se ejecuta para iniciar la aplicación
	 * @param args
	 */
	public static void main(String[] args) {
		MainGameLoop.run();	
	}
	
	/**
	 * Método que ejecuta el juego 
	 */
	public static void run() {
		// creamos la pantalla para visualizar lo que pasa en el juego
		DisplayManager.createDisplay();
		// loop del juego
		gameLoop();
		// terminamos la ejecución del juego
		DisplayManager.closeDisplay();
	}

	public static void gameLoop() {
		// objeto para cargar los modelos en memoria para posteriormente ser renderizados
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		// ****** texturas para el blendMap********//

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrains/grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrains/dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrains/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrains/path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture miBlendMap = new TerrainTexture(loader.loadTexture("maps/miBlendMap"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("maps/blendMap"));

		// *******************************************//

		// carga de los datos de los modelos
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData stallData = OBJFileLoader.loadOBJ("stall");
		ModelData devilData = OBJFileLoader.loadOBJ("bluedevil");
		ModelData steveData = OBJFileLoader.loadOBJ("luchOvejas");
		ModelData boxData = OBJFileLoader.loadOBJ("box");
		ModelData pinoData = OBJFileLoader.loadOBJ("pino");
		
		// cargamos los datos de los modelos en los VAO correspondientes a su modelo
		RawModel stallModel = loader.loadToVAO(stallData.getVertices(), stallData.getTextureCoords(),
				stallData.getNormals(), stallData.getIndices());
		RawModel arbolModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(),
				treeData.getNormals(), treeData.getIndices());
		RawModel devilModel = loader.loadToVAO(devilData.getVertices(), devilData.getTextureCoords(),
				devilData.getNormals(), devilData.getIndices());
		RawModel steveModel = loader.loadToVAO(steveData.getVertices(), steveData.getTextureCoords(),
				steveData.getNormals(), steveData.getIndices());
		RawModel boxModel = loader.loadToVAO(boxData.getVertices(), boxData.getTextureCoords(),
				boxData.getNormals(), boxData.getIndices());
		RawModel pinoModel = loader.loadToVAO(pinoData.getVertices(), pinoData.getTextureCoords(),
				pinoData.getNormals(), pinoData.getIndices());

		// carga de texturas en el modelo
		modeloStatic = new TexturedModel(devilModel, new ModelTexture(loader.loadTexture("objetos/bluedevil")));
		TexturedModel staticModelStall = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("objetos/stallTexture")));
		TexturedModel arbolTextura = new TexturedModel(arbolModel, new ModelTexture(loader.loadTexture("objetos/tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("objetos/grassTexture")));		
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("objetos/flower")));
		TexturedModel steveTexture = new TexturedModel(steveModel, new ModelTexture(loader.loadTexture("objetos/luchovejasTexture")));
		TexturedModel boxTexture = new TexturedModel(boxModel, new ModelTexture(loader.loadTexture("objetos/box")));
		TexturedModel pino = new TexturedModel(pinoModel, new ModelTexture(loader.loadTexture("objetos/pino")));

		ModelTexture arbolLowPolyAtlas = new ModelTexture(loader.loadTexture("objetos/lowPolyTree"));
		ModelTexture helechoTexturaAtlas = new ModelTexture(loader.loadTexture("objetos/fern"));
		// modelo que obtendrá características para las entidades
		ModelTexture textureStall = staticModelStall.getTexture();
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), helechoTexturaAtlas);
		
		helechoTexturaAtlas.setNumeroFilas(2);
		arbolLowPolyAtlas.setNumeroFilas(2);
		textureStall.setShineDamper(10);
		textureStall.setReflectivity(1);

		// características de las texturas
		grass.getTexture().setTieneTransparencia(true);
		grass.getTexture().setUsaFalsaIluminacion(true);
		flower.getTexture().setTieneTransparencia(true);
		flower.getTexture().setUsaFalsaIluminacion(true);
		fern.getTexture().setTieneTransparencia(true);
		pino.getTexture().setTieneTransparencia(true);

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
		
		List<Entity> items = new ArrayList<Entity>();
		
		// las entidades son los modelos 3D que cargamos en el escenario
		Entity diablillos = new Entity(modeloStatic, "diablillo", new Vector3f(10, 0, -20), 0, 0, 0, 1);
		Entity entidadStall = new Entity(staticModelStall, "bar", new Vector3f(-5, 0, -20), 0, 0, 0, 1);
		diablillo.add(diablillos);
		
		for (int i = 0; i < 1000; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				float y = terrenoActual.getHeighOfTerrain(x, z);
				items.add(new Entity(flower, "flores", new Vector3f(x, y, z), 0, 0, 0, 1f));
				x = random.nextFloat() * 1600 - 800;
				z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				y = terrenoActual.getHeighOfTerrain(x, z);
				items.add(new Entity(grass, "cesped", new Vector3f(x, y, z), 0, 0, 0, 1));
			}
			if (i % 2 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				int gridX = (int) (x / SIZE_TERRAIN + 1);
				int gridZ = (int) (z / SIZE_TERRAIN + 1);
				float y = terrain[gridX][gridZ].getHeighOfTerrain(x, z);
				items.add(new Entity(fern, "helecho", random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f));
			}
		}	
		
		for (int i = 0; i < 500; i++) {
			if(i % 2 == 0) {
				// árbol chistoso
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				float y = terrenoActual.getHeighOfTerrain(x, z);
				items.add(new Entity(arbolTextura, "arbol", new Vector3f(x, y, z), 0, 0, 0, 6));
				x = random.nextFloat() * 1600 - 800;
				z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				y = terrenoActual.getHeighOfTerrain(x, z);
				items.add(new Entity(pino, "pino", random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1f));
			}
			// renderizando 5 boxes
			if(i % 100 == 0) {
				items.add(new Entity(boxTexture, "box", new Vector3f(90, terrain[1][0].getHeighOfTerrain(90, (-0.5f * i) - 50) + 3, (-0.5f * i) - 50), 0, 0, 0, 3));
			}
		}
		
		// ejemplo para obtener el nombre de los items del juego :3
		int cont = 0;
	    for (Entity nombre : items) {
	        if (nombre.getNombre().equalsIgnoreCase("box")){
	        	cont++;
	        	System.out.println("box " + cont);
	        }
	    }

		// creamos una luz en el escenario
		Light luz = new Light(new Vector3f(2000, 2000, 1000), new Vector3f(1, 1, 1));

		// Player del videojuego 
		player = new Player(steveTexture, "lucho", new Vector3f(100, 0, -50), 0, 180, 0, 1f);
		// creación de la cámara a utilizar
		Camara camera = new Camara(player);

		// lista de GUI para el videojuego
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui0 = new GuiTexture(loader.loadTexture("guis/uce"), new Vector2f(0.7f, 0.5f), new Vector2f(0.25f, 0.45f));
		GuiTexture gui1 = new GuiTexture(loader.loadTexture("guis/vida"), new Vector2f(-0.7f, -0.7f), new Vector2f(0.25f, 0.25f));
		guis.add(gui0);
		guis.add(gui1);

		// bucle del juego real
		// donde ocurren todas las actualizaciones
		while (!Display.isCloseRequested()) {
			// game logic
			diablillos.increaseRotation(0, 1, 0);
			renderer.controlesRenderizacion();
			camera.move();
			
			int gridX = (int) (player.getPosition().x / SIZE_TERRAIN + 1);
			int gridZ = (int) (player.getPosition().z / SIZE_TERRAIN + 1);
			player.move(terrain[gridX][gridZ], entidadStall);

		    for (Entity item : items) {
		        if (item.getNombre().equalsIgnoreCase("arbol")){
		        	item.increaseRotation(0, 1, 0);
		        }
		    }
		    
			for (Terrain terreno : terrenos) {
				renderer.procesarTerreno(terreno);
			}
			renderer.procesarEntidad(player);
			renderer.procesarEntidad(entidadStall);

			// render
			renderer.render(luz, camera);
			
			for (Entity item : items) {
				renderer.procesarEntidad(item);
			}
			guiRenderer.render(guis);
			// diablillo
			for (Entity diablito : diablillo) {
				renderer.procesarEntidad(diablito);
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
		
		float x = random.nextFloat() * 100 - 50;
		float z = random.nextFloat() * -300;
		diablillo.add(new Entity(modeloStatic, "diablillo", new Vector3f(x, 0, z), 0, random.nextFloat() * 360, 0, 1f));
		
		/*
		 * while (Keyboard.next()) {
		 
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
					System.out.println("ENTER KEY IS PRESSED");
					float x = random.nextFloat() * 100 - 50;
					float z = random.nextFloat() * -300;
					diablillo.add(new Entity(modeloStatic, "diablillo", new Vector3f(x, 0, z), 0, random.nextFloat() * 360, 0, 1f));
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
		*/
	}
}
