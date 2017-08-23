/**
 * 
 */
package futuroingeniero.skybox;

import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.shaders.ShaderProgram;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 *
 */
public class SkyBoxShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/futuroingeniero/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/skybox/skyboxFragmentShader.glsl";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	/**
	 * Constructor sin parámetros que recibe la ruta de los archivos Shader.txt
	 */
	public SkyBoxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	/**
	 * Método que carga la matriz de proyección
	 * @param projection matriz uniforme de transformación
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
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	/**
	 * Método que devuelve la ubicación de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este método se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	/**
	 * Método que enlaza los atributos del VAO con las variables de el archivo GLSL VERTEXSHADER
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
