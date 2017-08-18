/**
 * 
 */
package futuroingeniero.textures;

/**
 * @author Daniel Loza
 *
 *<b>Clase pare representar la textura del terreno</b>
 */
public class TerrainTexture {

	private int textureID;

	/**
	 * Contructor de la clase obtener la identificación de las texturas
	 * @param textureID identificación de las texturas de l terreno para generar el blendMap 
	 */
	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}

	/**
	 * @return el textureID
	 */
	public int getTextureID() {
		return textureID;
	}
	
	
}
