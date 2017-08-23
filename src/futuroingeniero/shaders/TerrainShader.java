/**
 * 
 */
package futuroingeniero.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Light;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 * Clase para gestionar los Shader de los terrenos
 */
public class TerrainShader extends ShaderProgram {

	// número máximo de luces
	private static final int MAX_LIGHTS = 4;
	
	/**
	 * @param VERTEX_FILE archivo que contiene los cálculos para los VertexSahder
	 * @param FRAGMENT_FILE archivo que contiene los cálculos para los FragmentShader, para cada pixel
	 * @param location_transformationMatrix guarda la ubicación de la matriz uniforme en un entero para poder ser llamado
	 */
	
	private static final String VERTEX_FILE = "src/futuroingeniero/shaders/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/shaders/terrainFragmentShader.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_luzColor[];
	private int location_luzPosicion[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_cieloColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_atenuacion[];
	
	/**
	 * Constructor sin parámetros que obtiene la ruta de los archivos Shader.txt
	 */
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Método que enlaza los atributos del VAO con las variables de el archivo GLSL VERTEXSHADER
	 */
	@Override
	protected void bindAttributes() {
		// enlazamos el VAO con el vertexShader
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/**
	 * Método que devuelve la ubicación de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este método se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		// en las siguientes líneas guardamos el nombre exacto que definimos en los archivos
		// VertexShader y FragmentShader
		
		// igualamos a una variable entera y hacemos referencia a la variable de los archivos Shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_cieloColor = super.getUniformLocation("cieloColor");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		
		location_luzPosicion = new int[MAX_LIGHTS];
		location_luzColor = new int[MAX_LIGHTS];
		location_atenuacion = new int[MAX_LIGHTS];
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			location_luzColor[i] = super.getUniformLocation("luzColor[" + i + "]");
			location_luzPosicion[i] = super.getUniformLocation("luzPosicion[" + i + "]");
			location_atenuacion[i] = super.getUniformLocation("atenuacion[" + i + "]");
		}
		
	}
	
	public void conectarUnidadesdTextura() {
		// mostrar 1ero el fondo de la textura
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	
	/**
	 * Método para cargar el color del cielo
	 * @param r variable para el canal rojo
	 * @param g variable para el canal verde
	 * @param b variable para el canal azul
	 */
	public void loadCieloColor(float r, float g, float b){
		super.loadVector(location_cieloColor, new Vector3f(r, g, b));
	}
	
	/**
	 * Método que carga los valores de brillo en la entidad 3D
	 * @param damper variable de amortiguamiento en el objeto 3D, cuánta cantidad de brillo recibe el objeto
	 * @param refleccion variable para el reflejo del brillo
	 */
	public void loadShineVariables(float damper, float refleccion) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refleccion);
	}
	
	/**
	 * Método que carga la matriz por medio de la ubicación de la matriz y la matriz correspondiente
	 * @param matrix matriz uniforme de transformación
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Método para cargar las luces en el escenario
	 * @param luces lista de luces que se representarán en el escenario 
	 */
	public void loadLuces(List<Light> luces) {
		// carga las características de las luces en variable enteras que representan la ubicación de los datos
		for(int i = 0; i < MAX_LIGHTS; i++) {
			// comprobamos si existen cada una de las luces
			// caso contrario creamos una nueva luz en la posición (0, 0, 0) con color (0, 0, 0)
			if(i < luces.size()) {
				super.loadVector(location_luzPosicion[i], luces.get(i).getPosition());
				super.loadVector(location_luzColor[i], luces.get(i).getColor());
				super.loadVector(location_atenuacion[i], luces.get(i).getAtenuacion());
			} else {
				super.loadVector(location_luzPosicion[i], new Vector3f(0, 0, 0));
				super.loadVector(location_luzColor[i], new Vector3f(0, 0, 0));
				// la nueva atenuación será de (1, 0, 0) ya que no se quiere que el shader cometa un error dividiendo para cero (0)
				super.loadVector(location_atenuacion[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	/**
	 * Método para cargar la matriz del punto de vista hacia la cámara
	 * @param camera variable que carga la cámara para determinar la vista que va a tener la cámara 
	 */
	public void loadViewMatrix(Camara camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Método que carga la matriz de proyección
	 * @param projection matriz uniforme de transformación
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
}
