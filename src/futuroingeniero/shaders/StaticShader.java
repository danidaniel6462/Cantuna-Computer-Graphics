package futuroingeniero.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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
	 * 
	 * <h1>Pasos para utilizar esta clase con los Shader.txt</h1>
	 * 
	 * 1. Creación de un int que será el identificador para la variable del vertexShader
	 * 2. En el método getAllUniformLocation(), se instancia el identificador con la variable del VertexShader.txt, <b>NOTA: </b>la referencia al valor de la variable uniforme en el vertexShader tiene que estar correctamente escrita para no tener problemdas de referencia y así funcione lo que queremos representar con el archivo VertexShader 
	 * 3. Se crea un método para cargar los datos del CPU para el archivo VertexShader
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
	private int location_usaFalsaIluminacion;
	private int location_cieloColor;
	private int location_numeroFilas;
	private int location_offset;
	
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
	 * Método que devuelve la ubicación de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este método se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		
		// igualamos a una variable entera y hacemos referencia a la variable de los archivos Shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_luzColor = super.getUniformLocation("luzColor");
		location_luzPosicion = super.getUniformLocation("luzPosicion");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_usaFalsaIluminacion = super.getUniformLocation("usaFalsaIluminacion");
		location_cieloColor = super.getUniformLocation("cieloColor");
		location_numeroFilas = super.getUniformLocation("numeroFilas");
		location_offset = super.getUniformLocation("offset");
	}
	
	/**
	 * Método para cargar las variables uniformes del número de Filas
	 * @param numeroFilas
	 */
	public void loadNumeroFilas(int numeroFilas) {
		super.loadFloat(location_numeroFilas, numeroFilas);
	}
	
	/**
	 * Método para el desplazamiento x & y de las texturas Atlas
	 * @param x desplazamiento en X de la textura Atlas
	 * @param y desplazamiento en Y de la textura Atlas
	 */
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
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
	 * Método que carga el valor lógico para determinar si se usa falsa iluminación 
	 * @param usarFalso valor lógico 
	 */
	public void loadFalsaIluminacionVariable(boolean usarFalso) {
		super.loadBoolean(location_usaFalsaIluminacion, usarFalso);
		
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
	 * @param projection matriz uniforme de transformación
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
}
