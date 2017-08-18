/**
 * 
 */
package futuroingeniero.terrains;

import futuroingeniero.models.RawModel;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.textures.TerrainTexture;
import futuroingeniero.textures.TerrainTexturePack;

/**
 * @author Daniel Loza
 *
 */
public class Terrain {
	/**
	 * @value #STATIC_FIELD SIZE constante que determina el tamaño del terreno
	 * @value #STATIC_FIELD VERTEX_COUNT constante que determina el número de
	 *        vértices a lo largo de cada lado del terreno
	 */
	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;

	// variables para determinar la posición del terreno
	private float x;
	private float z;
	// modelo del terreno
	private RawModel model;
	// textura del terreno
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	/**
	 * <b>Constructor de la clase Terrain</b>
	 * 
	 * Rejilla: es un pequeño espacio del terreno, se lo puede definir como un
	 * espacio del terreno El terreno está constituido por rejillas en el eje x y z,
	 * para saber el tamaño del terreno debemos multiplicar cada rejilla con el
	 * tamaño (SIZE) del terreno
	 * 
	 * @param gridX
	 *            coordenadas de la rejilla en el eje X
	 * @param gridZ
	 *            coordenadas de la rejilla en el eje Z
	 * @param loader
	 *            variable para poder cargar el terreno
	 * @param texture
	 *            variable que texturizará el terreno que se crea en esta clase
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader);
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

	/**
	 * Método para crear el terreno totalmente plano
	 * 
	 * @param loader
	 *            variable para cargar el terreno
	 * @return un modelo terreno
	 */
	private RawModel generateTerrain(Loader loader) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
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
}
