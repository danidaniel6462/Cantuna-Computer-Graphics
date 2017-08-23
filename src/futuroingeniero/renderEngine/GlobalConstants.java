/**
 * 
 */
package futuroingeniero.renderEngine;

import org.lwjgl.opengl.GL11;

/**
 * @author Daniel Loza
 *
 *<b>Clase Global Constants</b>
 *Clase que tiene todas las variables p�blicas para el videojuego
 */
public class GlobalConstants {

	/**
	 * Variables globales para el videojuego
	 */
	//------------------------Display en Clase DisplayManager----------------------------
	
	/**
	 * <h1>Display en Clase DisplayManager </h1>
	 * @param TITULO T�tulo de la pantalla del Videojuego
	 * @param WIDTH_DISPLAY Ancho de la pantalla del Videojuego
	 * @param HEIGHT_DISPLAY Alto de la pantalla del Videojuego
	 * @param FPS_CAP Fotogramas por segundo del Videojuego 
	 */
	public static final String TITULO = "Cantu�a LWJGL Versi�n " + org.lwjgl.Sys.getVersion() + " OpenGL Versi�n " + GL11.glGetString(GL11.GL_VERSION);
	public static final int WIDTH_DISPLAY = 1280;
	public static final int HEIGHT_DISPLAY = 720;
	public static final int FPS_CAP = 120; 
	
	//------------------------C�mara en Clase MasterRender-----------------------------
	/**
	 * <h1>C�mara en Clase MasterRender</h1>
	 * @param FOV �ngulo de proyecci�n de la c�mara
	 * @param PLANO_CERCANO plano que est� cercano a la c�mara, punto en el eje Z
	 * @param PLANO_LEJANO plano de la profundidad de la c�mara.
	 */
	public static final float FOV = 70; 
	public static final float PLANO_CERCANO = 0.1f;
	public static final float PLANO_LEJANO = 5000f;
	
	//------------------------Terrain en Clase Terrain----------------------------
	/**
	 * 
	 * @param SIZE_TERRAIN tama�o del terreno que se renderiza
	 * @param MAX_HEIGHT m�xima altura del terreno
	 * @param MAX_PIXEL_COLOR total de colores en el terreno
	 * @value #STATIC_FIELD SIZE_TERRAIN constante que determina el tama�o del terreno
	 * @value #STATIC_FIELD VERTEX_COUNT constante que determina el n�mero de
	 *        v�rtices a lo largo de cada lado del terreno
	 */
	public static final float SIZE_TERRAIN = 800; // Tama�o del terreno
	public static final float MAX_HEIGHT = 40; // m�xima altura del terreno
	// se multiplica 256, 3 veces ya que son el color de cada p�xel del terreno
	// y los colores estan entre 0 y 255, total 256, por cada canal RGB, ser�an 256 * 256 * 256
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
	 * Enlace para descargar m�s SkyBox 
	 * {@link http://www.custommapmakers.org/skyboxes.php}
	 * @param SIZE tama�o del Skybox
	 * @param TEXTURE_FILES archivos de textura para generar el SkyBox, con el orden espec�fico de las c�ras del Cubo porque OpenGL necesita el orden correcto para no distorcionar el SkyBox al momento de Renderizar
	 * @param TEXTURE_FILES_NIGHT archivos de textura para cargar la noch en el videojuego
	 * @param VERTEX_SKYBOX Arreglo de v�rtices del SkyBox, con la ayuda de estos v�rtices se crea un cubo 
	 */
	
	public static final float SIZE = 800f;
	// nombre de las im�genes para formar el SkyBox (CubeMap)
	public static final String[] TEXTURE_FILES  = {"right", "left", "top", "bottom", "back", "front"};
	public static final String[] TEXTURE_FILES_NIGHT  = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	// determinamos la totalidad de los v�rtices del SkyBox (CubeMap) para cargarlo en el VAO
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
