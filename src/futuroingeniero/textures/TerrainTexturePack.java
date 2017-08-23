/**
 * 
 */
package futuroingeniero.textures;

/**
 * @author Daniel Loza
 *
 */
public class TerrainTexturePack {

	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	/**
	 * Método que carga las diferentes texturas r, g, b y el blendMap para realizar el terreno multiTexture
	 * @param backgroundTexture color de fondo del terreno sobre el cual se mezclan las otras texturas, representada con el color negro
	 * @param rTexture textura uno representada en el blendMap con el color rojo
	 * @param gTexture textura dos representada en el blendMap con el color verde
	 * @param bTexture textura tres representada en el blendMap con el color azul
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	/**
	 * @return el backgroundTexture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	/**
	 * @return el rTexture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	/**
	 * @return el gTexture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	/**
	 * @return el bTexture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}
	
	
}
