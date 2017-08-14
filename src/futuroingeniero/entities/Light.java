/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author Daniel Loza
 *
 * Clase para crear una luz en el escenario del videojuego 
 */
public class Light {

	private Vector3f position;
	private Vector3f color;
	/**
	 * Contructor de la Clase Light
	 * @param position variable para la posición de la luz
	 * @param color variable para el color de la luz
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
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
	 * @return the color
	 */
	public Vector3f getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	
}
