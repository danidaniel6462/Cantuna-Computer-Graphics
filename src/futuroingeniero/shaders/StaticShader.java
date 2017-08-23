package futuroingeniero.shaders;

import java.util.List;

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
	
	// n�mero m�ximo de luces
	private static final int MAX_LIGHTS = 4;
	
	/**
	 * @param VERTEX_FILE archivo que contiene los c�lculos para los VertexSahder
	 * @param FRAGMENT_FILE archivo que contiene los c�lculos para los FragmentShader, para cada pixel
	 * @param location_transformationMatrix guarda la ubicaci�n de la matriz uniforme en un entero para poder ser llamado
	 * 
	 * <h1>Pasos para utilizar esta clase con los Shader.txt</h1>
	 * 
	 * 1. Creaci�n de un int que ser� el identificador para la variable del vertexShader
	 * 2. En el m�todo getAllUniformLocation(), se instancia el identificador con la variable del VertexShader.txt, <b>NOTA: </b>la referencia al valor de la variable uniforme en el vertexShader tiene que estar correctamente escrita para no tener problemdas de referencia y as� funcione lo que queremos representar con el archivo VertexShader 
	 * 3. Se crea un m�todo para cargar los datos del CPU para el archivo VertexShader
	 */
	
	private static final String VERTEX_FILE = "src/futuroingeniero/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/shaders/fragmentShader.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_luzColor[];
	private int location_luzPosicion[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_usaFalsaIluminacion;
	private int location_cieloColor;
	private int location_numeroFilas;
	private int location_offset;
	private int location_atenuacion[];
	
	/**
	 * Constructor sin par�metros que recibe la ruta de los archivos Shader.txt
	 */
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * M�todo que enlaza los atributos del VAO con las variables de el archivo GLSL VERTEXSHADER
	 */
	@Override
	protected void bindAttributes() {
		// enlazamos el VAO con el vertexShader
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/**
	 * M�todo que devuelve la ubicaci�n de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este m�todo se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		
		// igualamos a una variable entera y hacemos referencia a la variable de los archivos Shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_usaFalsaIluminacion = super.getUniformLocation("usaFalsaIluminacion");
		location_cieloColor = super.getUniformLocation("cieloColor");
		location_numeroFilas = super.getUniformLocation("numeroFilas");
		location_offset = super.getUniformLocation("offset");
		
		location_luzPosicion = new int[MAX_LIGHTS];
		location_luzColor = new int[MAX_LIGHTS];
		location_atenuacion = new int[MAX_LIGHTS];
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			location_luzColor[i] = super.getUniformLocation("luzColor[" + i + "]");
			location_luzPosicion[i] = super.getUniformLocation("luzPosicion[" + i + "]");
			location_atenuacion[i] = super.getUniformLocation("atenuacion[" + i + "]");
		}

	}
	
	/**
	 * M�todo para cargar las variables uniformes del n�mero de Filas
	 * @param numeroFilas n�mero de filas de la textura Atlas, para determinar el tama�o de la matriz de la imagen
	 */
	public void loadNumeroFilas(int numeroFilas) {
		super.loadFloat(location_numeroFilas, numeroFilas);
	}
	
	/**
	 * M�todo para el desplazamiento x e y de las texturas Atlas
	 * @param x desplazamiento en X de la textura Atlas
	 * @param y desplazamiento en Y de la textura Atlas
	 */
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
	}
	
	/**
	 * M�todo para cargar el color del cielo
	 * @param r variable para el canal rojo
	 * @param g variable para el canal verde
	 * @param b variable para el canal azul
	 */
	public void loadCieloColor(float r, float g, float b){
		super.loadVector(location_cieloColor, new Vector3f(r, g, b));
	}
	
	/**
	 * M�todo que carga el valor l�gico para determinar si se usa falsa iluminaci�n 
	 * @param usarFalso valor l�gico 
	 */
	public void loadFalsaIluminacionVariable(boolean usarFalso) {
		super.loadBoolean(location_usaFalsaIluminacion, usarFalso);
		
	}
	
	/**
	 * M�todo que carga los valores de brillo en la entidad 3D
	 * @param damper variable de amortiguamiento en el objeto 3D, cu�nta cantidad de brillo recibe el objeto
	 * @param refleccion variable para el reflejo del brillo
	 */
	public void loadShineVariables(float damper, float refleccion) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refleccion);
	}
	
	/**
	 * M�todo que carga la matriz por medio de la ubicaci�n de la matriz y la matriz correspondiente
	 * @param matrix matriz uniforme de transformaci�n
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * M�todo para cargar las luces en el escenario
	 * @param luces lista de luces que se representar�n en el escenario 
	 */
	public void loadLuces(List<Light> luces) {
		// carga las caracter�sticas de las luces en variable enteras que representan la ubicaci�n de los datos
		for(int i = 0; i < MAX_LIGHTS; i++) {
			// comprobamos si existen cada una de las luces
			// caso contrario creamos una nueva luz en la posici�n (0, 0, 0) con color (0, 0, 0)
			if(i < luces.size()) {
				super.loadVector(location_luzPosicion[i], luces.get(i).getPosition());
				super.loadVector(location_luzColor[i], luces.get(i).getColor());
				super.loadVector(location_atenuacion[i], luces.get(i).getAtenuacion());
			} else {
				super.loadVector(location_luzPosicion[i], new Vector3f(0, 0, 0));
				super.loadVector(location_luzColor[i], new Vector3f(0, 0, 0));
				// la nueva atenuaci�n ser� de (1, 0, 0) ya que no se quiere que el shader cometa un error dividiendo para cero (0)
				super.loadVector(location_atenuacion[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	/**
	 * M�todo para cargar la matriz del punto de vista hacia la c�mara
	 * @param camera variable que carga la c�mara para determinar la vista que va a tener la c�mara 
	 */
	public void loadViewMatrix(Camara camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * M�todo que carga la matriz de proyecci�n
	 * @param projection matriz uniforme de transformaci�n
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
}
