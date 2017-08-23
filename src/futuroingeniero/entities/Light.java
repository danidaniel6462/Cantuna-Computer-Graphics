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
	// este atributo guardará los coeficientes para la ecuación de atenuación
	// fórmula: atenuación = a + b*d + c*d^2
	private Vector3f atenuacion = new Vector3f(1, 0, 0);
	
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
	 * Contructor de la Clase Light
	 * @param position variable para la posición de la luz
	 * @param color variable para el color de la luz
	 * @param atenuacion variable que tiene los coeficientes para calcular la atenuación de la luz
	 * la fórmula que se utiliza es: atenuación = a + b*d + c*d^2
	 */
	public Light(Vector3f position, Vector3f color, Vector3f atenuacion) {
		this.position = position;
		this.color = color;
		this.atenuacion = atenuacion;
	}

	/**
	 * @return el atenuacion
	 */
	public Vector3f getAtenuacion() {
		return atenuacion;
	}
	/**
	 * @param atenuacion el atenuacion a establecer
	 */
	public void setAtenuacion(Vector3f atenuacion) {
		this.atenuacion = atenuacion;
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
