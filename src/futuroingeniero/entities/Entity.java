/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.TextureModel;

/**
 * @author Daniel Loza
 *
 * Clase que va a contener las entidades que se renderizan en el videojuego
 */

public class Entity {
	
	private TextureModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	/**
	 * Constructor de la clase entidad
	 * @param model modelo para renderizar
	 * @param position vecto3 que incializa la posición del modelo
	 * @param rotX variable que incializa la el ángulo de rotación en X
	 * @param rotY variable que incializa la el ángulo de rotación en Y
	 * @param rotZ variable que incializa la el ángulo de rotación en Z
	 * @param scale variable que inicializa 
	 */

	public Entity(TextureModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotY = rotZ;
		this.scale = scale;
	}
	
	 /**
	  * Método Incremento de posición que será capaz de mover la entidad en el mundo
	  * @param dx Variable para controlar la posición en el eje X
	  * @param dy Variable para controlar la posición en el eje Y
	  * @param dz Variable para controlar la posición en el eje Z
	  */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	/**
	 * Método incremento de rotación para controlar la rotación del objeto en el mundo
	 * @param dx Variable para controlar la rotación en el eje X
	 * @param dy Variable para controlar la rotación en el eje Y
	 * @param dz Variable para controlar la rotación en el eje Z
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	/**
	 * @return the model
	 */
	public TextureModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TextureModel model) {
		this.model = model;
	}

	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * @return the rotX
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * @param rotX the rotX to set
	 */
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	/**
	 * @return the rotY
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * @param rotY the rotY to set
	 */
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	/**
	 * @return the rotZ
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * @param rotZ the rotZ to set
	 */
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	/**
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	

}
