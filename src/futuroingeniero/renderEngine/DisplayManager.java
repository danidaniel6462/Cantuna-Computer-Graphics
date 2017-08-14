package futuroingeniero.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author Daniel Loza
 * @version 1.0
 *
 * DisplayManager.java
 * Clase que crear, actualiza y elimina la pantalla mostrada al usuario
 */

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	// Métodos públicos
	/**
	 * Método para crear un Display
	 * (pantalla para mostrar lo que sucede en el juego)
	 */
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		// inicializamos el tamaño de la pantalla
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			// creación de la pantalla con un formato específico de pixeles
			// con las versiones que vamos a utilizar de OpenGL
			
			Display.create(new PixelFormat(), attribs);
			//Display.create();
			Display.setTitle("Cantuña");
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		/**
		 * Espacio que se muestra en la pantalla
		 * inicia en la esquina inferior izquierda hasta la parte superior derecha
		 * tomando coordenadas 0,0 y ancho y alto
		 */
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	/**
	 * Método que se ejecuta una vez por frame
	 */
	public static void updateDisplay() {
		// para utilizar el método updateDisplay hay que sincronizar la velocidad de actualizaciones por frame
		Display.sync(FPS_CAP);
		// actualizar pantalla 
		Display.update();
	}
	
	/**
	 * Método que se ejecuta cuando cerramos el Display
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
}
