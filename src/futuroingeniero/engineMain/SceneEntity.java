package futuroingeniero.engineMain;

import static futuroingeniero.utils.GlobalConstants.SIZE_TERRAIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.staticModel.models.RawModel;
import futuroingeniero.models.staticModel.models.TexturedModel;
import futuroingeniero.models.staticModel.objConverter.ModelData;
import futuroingeniero.models.staticModel.objConverter.OBJFileLoader;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.terrains.Terrain;
import futuroingeniero.textures.ModelTexture;
import futuroingeniero.textures.TerrainTexture;
import futuroingeniero.textures.TerrainTexturePack;

/**
 * @author Daniel Loza
 *
 *         Clase que inicializa las entidades, luces terrenos Contiene un método
 *         para inicializar los objetos estáticos y un método para actualizar lo
 *         que sucede en la escena
 */
public class SceneEntity {

	private List<Entity> items;
	private List<Terrain> terrenos;
	private List<Light> luces;

	public static Terrain[][] terrain;

	/**
	 * Contructor de la Clase SceneEntity
	 * 
	 * @param items
	 *            Entidades en la escena
	 * @param terrenos
	 *            Terrenos de la escena
	 * @param luces
	 *            Luces de la escena
	 */
	public SceneEntity(List<Entity> items, List<Terrain> terrenos, List<Light> luces) {
		this.items = items;
		this.terrenos = terrenos;
		this.luces = luces;
	}

	/**
	 * @return the items
	 */
	public List<Entity> getItems() {
		return items;
	}

	/**
	 * @return the terrenos
	 */
	public List<Terrain> getTerrenos() {
		return terrenos;
	}

	/**
	 * @return the luces
	 */
	public List<Light> getLuces() {
		return luces;
	}

	/**
	 * @return the terrain
	 */
	public Terrain[][] getTerrain() {
		return terrain;
	}

	/**
	 * Método para cargar los modelos 3D con su respectiva textura
	 * 
	 * @param loader
	 *            variable para cargar los modelos 3D
	 * @param modeloOBJ
	 *            modelo .OBJ para cargar en la escena
	 * @param texturaOBJ
	 *            textura del modelo 3D .OBJ
	 * @return retorna el modelo listo para poder cargarlo a la escena de
	 *         entidades
	 */
	public static TexturedModel modelReady(Loader loader, String modeloOBJ, String texturaOBJ) {

		// cargamos los datos de los modelos en los VAO correspondientes a su
		// modelo
		ModelData dataModel = OBJFileLoader.loadOBJ(modeloOBJ);
		// cargamos los datos de los modelos en los VAO correspondientes a su
		// modelo
		RawModel rawModel = loader.loadToVAO(dataModel.getVertices(), dataModel.getTextureCoords(),
				dataModel.getNormals(), dataModel.getIndices());
		// modelo final texturizado
		TexturedModel finalModel = new TexturedModel(rawModel,
				new ModelTexture(loader.loadTexture("objetos/" + texturaOBJ)));

		return finalModel;
	}

	/**
	 * Método para inicializar la escena de las entidades
	 *
	 * @param loader
	 *            Variable para poder cargar los modelos 3D en la escena
	 */
	public static SceneEntity init(Loader loader) {

		Random random = new Random();
		List<Terrain> terrenos = new ArrayList<Terrain>();
		List<Entity> items = new ArrayList<Entity>();
		List<Light> luces = new ArrayList<Light>();

		// ****** texturas para el blendMap********//

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrains/grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrains/dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrains/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrains/path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture miBlendMap = new TerrainTexture(loader.loadTexture("maps/miBlendMap"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("maps/blendMap"));

		// *******************************************//

		// modelos estáticos
		TexturedModel staticModelStall = modelReady(loader, "stall", "stallTexture");
		TexturedModel boxTexture = modelReady(loader, "box", "box");
		TexturedModel lampara = modelReady(loader, "lampara", "lampara");
		TexturedModel iglesia = modelReady(loader, "SanPanchito", "SanPanchoTextura");

		// vegetación
		TexturedModel pino = modelReady(loader, "pino", "pino");
		TexturedModel arbolTextura = modelReady(loader, "tree", "tree");
		TexturedModel grass = modelReady(loader, "grassModel", "grassTexture");
		TexturedModel flower = modelReady(loader, "grassModel", "flower");
		TexturedModel fern = modelReady(loader, "fern", "fern");

		// ***************************************************************************/
		// textura Atlas !!!!!!!!!
		fern.getTexture().setNumeroFilas(2);
		// ***************************************************************************/
		staticModelStall.getTexture().setShineDamper(10);
		staticModelStall.getTexture().setReflectivity(1);

		// características de las texturas
		grass.getTexture().setTieneTransparencia(true);
		grass.getTexture().setUsaFalsaIluminacion(true);
		flower.getTexture().setTieneTransparencia(true);
		flower.getTexture().setUsaFalsaIluminacion(true);
		fern.getTexture().setTieneTransparencia(true);
		pino.getTexture().setTieneTransparencia(true);
		lampara.getTexture().setUsaFalsaIluminacion(true);

		terrain = new Terrain[2][2];
		terrain[0][0] = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		terrain[1][0] = new Terrain(0, -1, loader, texturePack, miBlendMap, "heightmap");
		terrain[0][1] = new Terrain(-1, 0, loader, texturePack, blendMap, "heightmap");
		terrain[1][1] = new Terrain(0, 0, loader, texturePack, miBlendMap, "heightmap");

		Terrain terrenoActual;

		for (int i = 0; i < terrain.length; i++)
			for (int j = 0; j < terrain[0].length; j++)
				terrenos.add(terrain[i][j]);

		Entity entidadStall = new Entity(staticModelStall, "bar", new Vector3f(-5, 0, -20), 0, 0, 0, 1);
		Entity entidadIglesia = new Entity(iglesia, "iglesia", new Vector3f(0, 0, -20), 0, 0, 0, 7);

		items.add(entidadStall);
		items.add(entidadIglesia);

		for (int i = 0; i < 1000; i++) {
			if (i % 3 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				float y = terrenoActual.getHeightOfTerrain(x, z);
				items.add(new Entity(flower, "flores", new Vector3f(x, y, z), 0, 0, 0, 1f));
				x = random.nextFloat() * 1600 - 800;
				z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				y = terrenoActual.getHeightOfTerrain(x, z);
				items.add(new Entity(grass, "cesped", new Vector3f(x, y, z), 0, 0, 0, 1));
			}
			if (i % 2 == 0) {
				float x = random.nextFloat() * 1600 - 800;
				float z = random.nextFloat() * -800;
				int gridX = (int) (x / SIZE_TERRAIN + 1);
				int gridZ = (int) (z / SIZE_TERRAIN + 1);
				float y = terrain[gridX][gridZ].getHeightOfTerrain(x, z);
				items.add(new Entity(fern, "helecho", random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f));
			}
		}

		for (int i = 0; i < 500; i++) {
			// árbol chistoso
			float x = random.nextFloat() * 1600 - 800;
			float z = random.nextFloat() * -800;
			terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
			float y = terrenoActual.getHeightOfTerrain(x, z);
			if (i % 8 == 0) {
				items.add(new Entity(arbolTextura, "arbol", new Vector3f(x, y, z), 0, 0, 0, 6));
			}
			if (i % 2 == 0) {
				x = random.nextFloat() * 1600 - 800;
				z = random.nextFloat() * -800;
				terrenoActual = terrain[(int) (x / SIZE_TERRAIN + 1)][(int) (z / SIZE_TERRAIN + 1)];
				y = terrenoActual.getHeightOfTerrain(x, z);
				items.add(new Entity(pino, "pino", random.nextInt(4), new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0, 1f));
			}
			// renderizando 5 boxes
			if (i % 100 == 0) {
				items.add(new Entity(boxTexture, "box",
						new Vector3f(90, terrain[1][0].getHeightOfTerrain(90, (-0.5f * i) - 50) + 3, (-0.5f * i) - 50),
						0, 0, 0, 3));
			}
		}

		// creamos una luz en el escenario

		Light sol = new Light(new Vector3f(2000, 2000, 1000), new Vector3f(1, 1, 1));
		luces.add(sol);

		luces.add(new Light(new Vector3f(370, 18, -300), new Vector3f(0, 2, 2f), new Vector3f(1f, 0.01f, 0.002f)));
		luces.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0f), new Vector3f(1f, 0.01f, 0.002f)));

		items.add(new Entity(lampara, "lamp", new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		items.add(new Entity(lampara, "lamp", new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));

		Entity lamp = new Entity(lampara, "lampMovimiento", new Vector3f(185, -4.7f, -293), 0, 0, 0, 1);
		Light lucesita = new Light("lucesita", new Vector3f(185, 10, -293), new Vector3f(2f, 0, 0),
				new Vector3f(1f, 0.01f, 0.002f));

		items.add(lamp);
		luces.add(lucesita);

		return new SceneEntity(items, terrenos, luces);
	}

	/**
	 * Método que actualiza los objetos de la escena
	 */
	public void update() {
		for (Entity item : items) {
			if (item.getNombre().equalsIgnoreCase("arbol")) {
				item.increaseRotation(0, 1, 0);
			}
			if (item.getNombre().equalsIgnoreCase("mmm")) {
				item.increaseRotation(0, 1, 0);
			}
		}
	}
}