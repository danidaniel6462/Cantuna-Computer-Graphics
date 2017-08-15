/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Daniel Loza
 *
 */
public class Camara {

	/**
	 * Variables nombradas seg�n la cinematogr�fica
	 * @param position variable que incializa la posici�n de la c�mara en el mundo
	 * @param paneo variable para la rotaci�n en X
	 * @param picado variable para la rotaci�n en Y
	 * @param roll variable para la rotaci�n en Z
	 */
	private Vector3f posicion = new Vector3f(0, 0, 0);
	private float paneo; 
	private float picado;
	private float roll;
	
	public Camara() {}
	
	public void movimiento() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			posicion.z -= 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			posicion.x += 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			posicion.x -= 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			posicion.z += 0.5f;
		}
	}

	/**
	 * @return the posicion
	 */
	public Vector3f getPosition() {
		return posicion;
	}

	/**
	 * @return the paneo
	 */
	public float getPaneo() {
		return paneo;
	}

	/**
	 * @return the picado
	 */
	public float getPicado() {
		return picado;
	}

	/**
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}	
}