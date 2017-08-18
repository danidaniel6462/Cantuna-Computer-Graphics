package futuroingeniero.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author Daniel Loza
 * @version 1.0
 *
 * <h1>DisplayManager.java</h1>
 * Clase que crear, actualiza y elimina la pantalla mostrada al usuario
 */

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
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
			Display.setTitle("Cantuña LWJGL Versión " + org.lwjgl.Sys.getVersion());
			
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
		
		// obtenemos el tiempo actual del videojuego en milisegundos
		long currentFrameTime = getCurrentTime();
		// el delta es el tiempo en el que se actualiza la pantalla para volver a dibujar el escenario
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	/**
	 * Método para retorna el delta que es el tiempo que se demora en actualizar la pantalla del juego
	 * @return devuelve el delta de actualización de la pantalla del juego
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * Método que se ejecuta cuando cerramos el Display
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
	
	/**
	 * Método para calcular los fps en milisegundos del video juego 
	 * @return cáclulo en 1000 segundos de actualización del videojuego
	 */
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
}
