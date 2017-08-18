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
	 * @param backgroundTexture
	 * @param rTexture
	 * @param gTexture
	 * @param bTexture
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
