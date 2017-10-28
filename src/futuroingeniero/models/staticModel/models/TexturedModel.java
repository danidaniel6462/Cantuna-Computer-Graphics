/**
 * 
 */
package futuroingeniero.models.staticModel.models;

import futuroingeniero.textures.ModelTexture;

/**
 * @author Daniel Loza
 *
 */
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	/**
	 * Constructor de la clase Texture Model 
	 * @param model modelo Raw
	 * @param texture textura para el modelo
	 */
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * @return the rawModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * @return the texture
	 */
	public ModelTexture getTexture() {
		return texture;
	}
}
