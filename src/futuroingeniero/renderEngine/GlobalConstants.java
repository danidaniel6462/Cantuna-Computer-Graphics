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
	
	/**
	 * <h1>Display en Clase DisplayManager </h1>
	 * @param TITULO Título de la pantalla del Videojuego
	 * @param WIDTH_DISPLAY Ancho de la pantalla del Videojuego
	 * @param HEIGHT_DISPLAY Alto de la pantalla del Videojuego
	 * @param FPS_CAP Fotogramas por segundo del Videojuego 
	 */
	public static final String TITULO = "Cantuña LWJGL Versión " + org.lwjgl.Sys.getVersion() + " OpenGL Versión " + GL11.glGetString(GL11.GL_VERSION);
	public static final int WIDTH_DISPLAY = 1280;
	public static final int HEIGHT_DISPLAY = 720;
	public static final int FPS_CAP = 120; 
	
	//------------------------Cámara en Clase MasterRender-----------------------------
	/**
	 * <h1>Cámara en Clase MasterRender</h1>
	 * @param FOV ángulo de proyección de la cámara
	 * @param PLANO_CERCANO plano que está cercano a la cámara, punto en el eje Z
	 * @param PLANO_LEJANO plano de la profundidad de la cámara.
	 */
	public static final float FOV = 70; 
	public static final float PLANO_CERCANO = 0.1f;
	public static final float PLANO_LEJANO = 5000f;
	
	//------------------------Terrain en Clase Terrain----------------------------
	/**
	 * 
	 * @param SIZE_TERRAIN tamaño del terreno que se renderiza
	 * @param MAX_HEIGHT máxima altura del terreno
	 * @param MAX_PIXEL_COLOR total de colores en el terreno
	 * @value #STATIC_FIELD SIZE_TERRAIN constante que determina el tamaño del terreno
	 * @value #STATIC_FIELD VERTEX_COUNT constante que determina el número de
	 *        vértices a lo largo de cada lado del terreno
	 */
	public static final float SIZE_TERRAIN = 800; // Tamaño del terreno
	public static final float MAX_HEIGHT = 40; // máxima altura del terreno
	// se multiplica 256, 3 veces ya que son el color de cada píxel del terreno
	// y los colores estan entre 0 y 255, total 256, por cada canal RGB, serían 256 * 256 * 256
	public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	//------------------------Background Display en Clase MasterRenderer------------------------------
	
	/**
	 * <b>Background Display en Clase MasterRenderer</b>
	 * Colores del Cielo y neblina del Videojuego
	 * @param RED Color Rojo 
	 * @param GREEN Color Verde
	 * @param BLUE Color Azul
	 */
	public static float RED = 0.5444f; // 146f / 255f;
	public static float GREEN = 0.62f; // 209f / 255f; 
	public static float BLUE =  0.69f;	// 188f / 255f; 
	
	//------------------------Player en Clase Player------------------------------
	
	/**
	 * <b>Player en Clase Player</b>
	 * 
	 * @param WALK_SPEED velocidad de caminar o trotar como yo lo llamo jeje
	 * @param TURN_SPEED Velocidad de giro
	 * @param GRAVITY variable para simular gravedad en el juego
	 * @param JUMP_POWER poder del salto del player
	 * @param RUN_SPEED velocidad de correr del player
	 * 
	 */
	public static final float WALK_SPEED = 20; // velocidad de caminar o trotar como yo lo llamo jeje
	public static final float TURN_SPEED = 160; // Velocidad de giro
	public static final float GRAVITY = -80; // variable para simular gravedad en el juego
	public static final float JUMP_POWER = 30; // poder del salto del jugador
	public static final float RUN_SPEED = 30; // velocidad de correr del player
	
	//------------------------Skybox Data en Clase SkyBoxRenderer------------------------
	/**
	 * <b>SkyBox Data en Clase SkyBoxRenderer</b>
	 * Enlace para descargar más SkyBox 
	 * {@link http://www.custommapmakers.org/skyboxes.php}
	 * @param SIZE tamaño del Skybox
	 * @param TEXTURE_FILES archivos de textura para generar el SkyBox, con el orden específico de las cáras del Cubo porque OpenGL necesita el orden correcto para no distorcionar el SkyBox al momento de Renderizar
	 * @param TEXTURE_FILES_NIGHT archivos de textura para cargar la noch en el videojuego
	 * @param VERTEX_SKYBOX Arreglo de vértices del SkyBox, con la ayuda de estos vértices se crea un cubo 
	 */
	
	public static final float SIZE = 800f;
	// nombre de las imágenes para formar el SkyBox (CubeMap)
	public static final String[] TEXTURE_FILES  = {"right", "left", "top", "bottom", "back", "front"};
	public static final String[] TEXTURE_FILES_NIGHT  = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	// determinamos la totalidad de los vértices del SkyBox (CubeMap) para cargarlo en el VAO
	public static final float[] VERTICES_SKYBOX = {        
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
