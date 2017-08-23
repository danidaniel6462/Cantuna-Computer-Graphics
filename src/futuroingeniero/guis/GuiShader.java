package futuroingeniero.guis;

import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.shaders.ShaderProgram;

/**
 * 
 * @author Daniel Loza
 *
 * <h1>Clase GuiShader</h1>
 * Clase para gestionar los Shader de los GUI
 */
public class GuiShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/futuroingeniero/guis/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/futuroingeniero/guis/guiFragmentShader.glsl";
	
	private int location_transformationMatrix;

	/**
	 * Constructor sin parámetros de la clase para enviar los archivos GLSL
	 */
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	/**
	 * Cargamos las transformaciones de la matriz de los GUI
	 * @param matrix
	 */
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/**
	 * Método que devuelve la ubicación de las variables creadas en el VertexShader y FragmentShader 
	 * al utilizar este método se debe escribir el nombre exacto que definimos en los archivos VertexShader y FragementShader
	 */
	@Override
	protected void getAllUniformLocation() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	/**
	 * Método que enlaza o vincula los atributos del VAO con las variables de el archivo GLSL VERTEXSHADER
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
