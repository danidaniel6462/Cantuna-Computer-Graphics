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
	 * @param picado variable para la rotación en Y
	 * @param paneo variable para la rotación en X
	 * @param roll variable para la rotación en Z
	 */
	private Vector3f posicion = new Vector3f(10, 25, 20);
	private float picado = 20f;  // pitch
	private float paneo; // yaw
	private float roll; // roll
	
	private float speed;
	
	// public Camara() {}
	
	public Camara()
	{
		this.speed = 0.5f;
	}
	
	public void move()
	{
		paneo =  - (Display.getWidth() - Mouse.getX() / 2);
		picado =  (Display.getHeight() / 2) - Mouse.getY();
		
		if (picado >= 90)
		{
			picado = 90;	
		}
		else if (picado <= -90)
		{
			picado = -90;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			posicion.z += -(float)Math.cos(Math.toRadians(paneo)) * speed;
			posicion.x += (float)Math.sin(Math.toRadians(paneo)) * speed;	
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			posicion.z -= -(float)Math.cos(Math.toRadians(paneo)) * speed;
			posicion.x -= (float)Math.sin(Math.toRadians(paneo)) * speed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{	
			posicion.z += (float)Math.sin(Math.toRadians(paneo)) * speed;
			posicion.x += (float)Math.cos(Math.toRadians(paneo)) * speed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{	
			posicion.z -= (float)Math.sin(Math.toRadians(paneo)) * speed;
			posicion.x -= (float)Math.cos(Math.toRadians(paneo)) * speed;
		}
	}
	
	/*
	 * public void movimiento() {
	
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			posicion.z -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			posicion.x += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			posicion.x -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			posicion.z += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			posicion.y += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			posicion.y -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			picado += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
			picado -= speed;
		}
	}
	
	 */
	

	/**
	 * @return the posicion
	 */
	public Vector3f getPosition() {
		return posicion;
	}

	/**
	 * @return the picado
	 */
	public float getPicado() {
		return picado;
	}

	/**
	 * @return the paneo
	 */
	public float getPaneo() {
		return paneo;
	}

	/**
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}	
}