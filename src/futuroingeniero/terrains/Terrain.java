/**
 * 
 */
package futuroingeniero.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.RawModel;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.textures.TerrainTexture;
import futuroingeniero.textures.TerrainTexturePack;
import futuroingeniero.toolbox.Maths;

import static futuroingeniero.renderEngine.GlobalConstants.*;

/**
 * @author Daniel Loza
 *
 */
public class Terrain {
														
	// variables para determinar la posición del terreno
	private float x;
	private float z;
	// modelo del terreno
	private RawModel model;
	// textura del terreno
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	private float[][] heights;
	
	/**
	 * <b>Constructor de la clase Terrain</b>
	 * 
	 * Rejilla: es un pequeño espacio del terreno, se lo puede definir como un
	 * espacio del terreno El terreno está constituido por rejillas en el eje x y z,
	 * para saber el tamaño del terreno debemos multiplicar cada rejilla con el
	 * tamaño (SIZE_TERRAIN) del terreno
	 * 
	 * @param gridX
	 *            coordenadas de la rejilla en el eje X
	 * @param gridZ
	 *            coordenadas de la rejilla en el eje Z
	 * @param loader
	 *            variable para poder cargar el terreno
	 * @param texturePack
	 *            variable que texturizará el terreno que se crea en esta clase
	 * @param blendMap Textura con información para la mezcla de las multiTexturas
	 * @param heightMap Textura con información para la altura de los diferetes puntos en el terreno
	 */
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, 
			TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE_TERRAIN;
		this.z = gridZ * SIZE_TERRAIN;
		this.model = generateTerrain(loader, heightMap);
	}
	
	/**
	 * Método que obtiene una coordenada X, Z y devuelve la coordenada en Y 
	 * que será la altura del terreno en ese punto
	 * @param worldX posición en X del terreno en el espacio del videojuego
	 * @param worldZ posición en Z del terreno en el espacio del videojuego  
	 * @return devvuelve la coordenada en Y que será la altura del terreno en el punto X, Z
	 */
	public float getHeighOfTerrain(float worldX, float worldZ) {
		// convertimos las posiciones del las coordenadas del mundo en coordenadas del terreno
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		// calculamos el tamaño de cada cuadrado que tiene el terreno
		float gridSquareSIZE_TERRAIN = SIZE_TERRAIN / (float) (heights.length - 1);
		// averiguamos en qué cuadrícula está el jugador con las posiciones en x, z para el terreno
		// utilizamos la función floor obtener solo el valor entero de la división
		// por ejemplo:
		// (13, 8) coordenadas del personaje sobre el terreno
		// (13, 8) / 5 = (2.6, 1.6) dividimos para 5, que es el tamaño de cada regilla (grid)
		// floor(2.6, 1.6), obtenemos el valor entero de los número ingresados y obtenemos
		// (2, 1) que sería la posición del cuadrada en el terreno
		int gridX = (int) Math.floor(terrainX / gridSquareSIZE_TERRAIN);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSIZE_TERRAIN);
		// calculamos si el valor de griX y griZ de verdad están en una cuadrícula válida del terreno
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			// devolvemos la altura para el jugador sea cero
			return 0;
		}
		// si el jugador está en la cuadricula calculamos la posición del jugador
		// para realizarlo utilizamos el módulo para calcular la distancia del jugador desde la esquina superior izquierda
		float xCoord = (terrainX % gridSquareSIZE_TERRAIN) / gridSquareSIZE_TERRAIN;
		float zCoord = (terrainZ % gridSquareSIZE_TERRAIN) / gridSquareSIZE_TERRAIN;
		// averiguamos en que lado del triçangulo de cada porción del terreno está el personaje
		float respuesta;
		if(xCoord <= (1 - zCoord)) {
			// calculamos la altura del triángulo en la posición que está el jugador 
			respuesta = Maths.
					barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			respuesta = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return respuesta;
	}

	/**
	 * Método para crear el terreno con un HeightMap
	 * 
	 * @param loader
	 *            variable para cargar el terreno
	 * @param heightMap
	 *            variable para cargar el heightMap del terreno, si no se carga el terreno es plano
	 * @return un modelo terreno
	 */
	private RawModel generateTerrain(Loader loader, String heightMap) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/texturas/maps/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("\nMI ERROR: Intentó cargar el heightMap " + heightMap + ".png Pero no funcionó\n");
			System.exit(-1);
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights = new float [VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE_TERRAIN;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE_TERRAIN;
				Vector3f normal = calcularNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	/**
	 * Método para calcular las normales del terreno para poder recibir de forma correcta las luces del videojuego
	 * @param x Variable horizontal del heightMap
	 * @param z Variable de profundidad del heightMap
	 * @param image imagen del HeightMap
	 * @return Devuelve un Vector3 del cálculo de las normales para recibir bien las luces
	 */
	private Vector3f calcularNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	/**
	 *  Calculamos la altura del HeightMap y el color de los pìxeles del terreno
	 * @param x Variable horizontal del heightMap
	 * @param z Variable de profundidad del heightMap
	 * @param image imagen del HeightMap
	 * @return retorna el rango de altura máxima y mínima para el terreno
	 */
	private float getHeight(int x, int z, BufferedImage image) {
		// comprobamos que el heightMap que cargamos esté en el rango que una imagenBuffer
		if(x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}

	/**
	 * @return el x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return el z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @return el model
	 */
	public RawModel getModel() {
		return model;
	}

	/**
	 * @return el texturePack
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * @return el blendMap
	 */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
}
