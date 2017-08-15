/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Daniel Loza
 *
 */
public class Camara {

	/**
	 * Variables nombradas según la cinematográfica
	 * @param position variable que incializa la posición de la cámara en el mundo
	 * @param paneo variable para la rotación en X
	 * @param picado variable para la rotación en Y
	 * @param roll variable para la rotación en Z
	 */
	private Vector3f posicion = new Vector3f(0, 5, 0);
	private float paneo;  // pitch
	private float picado; // yaw
	private float roll; // roll
	
	private float speed;
	
	// public Camara() {}
	
	public Camara()
	{
		
		this.speed = 0.5f;
	}
	
	public void move()
	{
		
		picado =  - (Display.getWidth() - Mouse.getX() / 2);
		paneo =  (Display.getHeight() / 2) - Mouse.getY();
		
		if (paneo >= 90)
		{
			
			paneo = 90;
			
		}
		else if (paneo <= -90)
		{
			
			paneo = -90;
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{

			posicion.z += -(float)Math.cos(Math.toRadians(picado)) * speed;
			posicion.x += (float)Math.sin(Math.toRadians(picado)) * speed;
			
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			posicion.z -= -(float)Math.cos(Math.toRadians(picado)) * speed;
			posicion.x -= (float)Math.sin(Math.toRadians(picado)) * speed;


		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			
			posicion.z += (float)Math.sin(Math.toRadians(picado)) * speed;
			posicion.x += (float)Math.cos(Math.toRadians(picado)) * speed;

		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			
			posicion.z -= (float)Math.sin(Math.toRadians(picado)) * speed;
			posicion.x -= (float)Math.cos(Math.toRadians(picado)) * speed;

		}
		
	}
	
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