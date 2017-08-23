/**
 * 
 */
package futuroingeniero.renderEngine;

import org.lwjgl.opengl.GL11;

/**
 * @author Daniel Loza
 *
 *<b>Clase Global Constants</b>
 *Clase que tiene todas las variables públicas para el videojuego
 */
public class GlobalConstants {

	/**
	 * Variables globales para el videojuego
	 */
	//------------------------Display en Clase DisplayManager----------------------------
	public static final String TITULO = "Cantuña LWJGL Versión " + org.lwjgl.Sys.getVersion() + " OpenGL Versión " + GL11.glGetString(GL11.GL_VERSION);
	public static final int WIDTH_DISPLAY = 1280;
	public static final int HEIGHT_DISPLAY = 720;
	public static final int FPS_CAP = 120; 
	
	//------------------------Cámara en Clase MasterRender-----------------------------
	/**
	 * @param FOV ángulo de proyección de la cámara
	 * @param PLANO_CERCANO plano que está cercano a la cámara, punto en el eje Z
	 * @param PLANO_LEJANO plano de la profundidad de la cámara.
	 */
	public static final float FOV = 70; 
	public static final float PLANO_CERCANO = 0.1f;
	public static final float PLANO_LEJANO = 1000f;
	public static final double ZOOM_VALUE = 10.0;
	
	//------------------------Terrain en Clase Terrain----------------------------
	/**
	 * @value #STATIC_FIELD SIZE constante que determina el tamaño del terreno
	 * @value #STATIC_FIELD VERTEX_COUNT constante que determina el número de
	 *        vértices a lo largo de cada lado del terreno
	 */
	public static final float SIZE_TERRAIN = 800; // Tamaño del terreno
	public static final float MAX_HEIGHT = 40; // máxima altura del terreno
	// se multiplica 256, 3 veces ya que son el color de cada píxel del terreno
	// y los colores estan entre 0 y 255, total 256, por cada canal RGB, serían 256 * 256 * 256
	public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	//------------------------Background Display en Clase MasterRenderer------------------------------
	public static final float RED =  100f / 255f;// 146f / 255f;
	public static final float GREEN = 100f / 255f; // 209f / 255f; 
	public static final float BLUE =  100f / 255f;	// 188f / 255f; 
	
	//------------------------Player en Clase Player------------------------------
	public static final float WALK_SPEED = 20; // velocidad de caminar o trotar como yo lo llamo jeje
	public static final float TURN_SPEED = 160; // Velocidad de giro
	public static final float GRAVITY = -80; // varibale para simular gravedad en el juego
	public static final float JUMP_POWER = 30; // poder del salto del jugador
	public static final float RUN_SPEED = 30; // velocidad de correr del player
	
	//------------------------Skybox Data------------------------
	public static final float SIZE = 2000f;
	public static final String[] TEXTURE_FILES  = {"right","left","top","bottom","back","front"};
	public static final String[] TEXTURE_FILES_NIGHT  = {"nightRight","nightLeft","nightTop","nightBottom","nightBack","nightFront"};
	public static final float[] VERTEX_SKYBOX = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
}
