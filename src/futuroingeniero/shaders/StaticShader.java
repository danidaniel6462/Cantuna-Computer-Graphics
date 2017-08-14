package futuroingeniero.shaders;

import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Light;
import futuroingeniero.toolbox.Maths;

/**
 * 
 * @author Daniel Loza
 *
 * Clase StaticShader extendida de ShaderProgram
 */
public class StaticShader extends ShaderProgram{
	
	/**
	 * @param VERTEX_FILE archivo que contiene los cálculos para los VertexSahder
	 * @param FRAGMENT_FILE archivo que contiene los cálculos para los FragmentShader, para cada pixel
	 * @param location_transformationMatrix guarda la ubicación de la matriz uniforme en un entero para poder ser llamado
	 */
	
	private static final String VERTEX_FILE = "src/futuroingeniero/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_luzColor;
	private int location_luzPosicion;
	private int location_shineDamper;
	private int location_reflectivity;
	
	/**
	 * Constructor sin parámetros que recibe la ruta de los archivos Shader.txt
	 */
	public StaticShader() {
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
	 * Método que devuelve la ubicación de la matriz de transformación
	 */
	@Override
	protected void getAllUniformLocation() {
		// en las siguientes líneas guardamos el nombre exacto que definimos en los archivos
		// VertexShader y FragmentShader
		
		// igualamos a una variable entera y hacemos referencia a la variable de los archivos Shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_luzColor = super.getUniformLocation("luzColor");
		location_luzPosicion = super.getUniformLocation("luzPosicion");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}
	
	
	public void loadShineVariables(float damper, float refleccion) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refleccion);
	}
	
	/**
	 * Método que carga la matriz por medio de la ubicación de la matriz y la matriz correspondiente
	 * @param matrix matriz uniforme de transformación
	 */
	public void loadTranformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Método para cargar la luz en el escenario
	 * @param luz variable que obtendrá las características de la luz 
	 */
	public void loadLuz(Light luz) {
		// carga las características de la luz en variable enteras que representan la ubicación de los datos
		super.loadVector(location_luzPosicion, luz.getPosition());
		super.loadVector(location_luzColor, luz.getColor());
	}
	
	/**
	 * Método para cargar la vista de la cámara
	 * @param camera variable que carga la cámara para determinar la vista que va a tener la cámara 
	 */
	public void loadViewMatrix(Camara camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Método que carga la matriz de proyección
	 * @param matrix matriz uniforme de transformación
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
}
