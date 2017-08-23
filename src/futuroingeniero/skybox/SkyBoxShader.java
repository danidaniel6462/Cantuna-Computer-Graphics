/**
 * 
 */
package futuroingeniero.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.renderEngine.DisplayManager;
import futuroingeniero.shaders.ShaderProgram;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 *
 */
public class SkyBoxShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/futuroingeniero/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/skybox/skyboxFragmentShader.glsl";
	
	private static final float ROTATE_SPEED = 1f;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
	
	private float rotacion = 0;
	/**
	 * Constructor sin parámetros que recibe la ruta de los archivos Shader.txt
	 */
	public SkyBoxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	/**
	 * Método que devuelve la ubicación de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este método se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}
	
	/**
	 * Método para cargar las diferentes texturas en el SkyBox
	 * donde 0 y 1 representan las diferentes texturas 
	 */
	public void conectarUnidadesTexturas() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	/**
	 * Método que carga el factor de mezcla de las texturas para el SkyBox
	 * @param blendFactor factor de mezcla para las texturas SkyBox
	 */
	public void loadBlendFactor(float blendFactor) {
		super.loadFloat(location_blendFactor, blendFactor);
	}
	
	/**
	 * Método para cargar el color de la niebla para el SkyBox
	 * @param r color rojo
	 * @param g color verde
	 * @param b color azul
	 */
	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r, g, b));
	}
	
	/**
	 * Método que carga la matriz de proyección
	 * @param matrix matriz uniforme de transformación
	 */
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	/**
	 * Método para cargar la matriz del punto de vista hacia la cámara
	 * @param camera variable que carga la cámara para determinar la vista que va a tener la cámara 
	 */
	public void loadViewMatrix(Camara camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotacion += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotacion), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}	

	/**
	 * Método que enlaza los atributos del VAO con las variables de el archivo GLSL VERTEXSHADER
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
