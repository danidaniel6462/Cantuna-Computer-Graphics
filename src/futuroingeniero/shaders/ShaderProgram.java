/**
 * 
 */
package futuroingeniero.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Daniel Loza
 *
 * ShaderProgram Clase para obtener los archivos GLSL para losmodelos opeGL
 */

public abstract class ShaderProgram {
	/**
	 * @param programID id del programa
	 * @param vertexShaderID identificación del archivo vertexShader
	 * @param fragmentShaderID identificación del archivo fragmentShader
	 */
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Constructor del la Clase ShaderProgram, este constructor obtiene la ruta de los archivos GLSL
	 * @param vertexFile ruta del archivo GLSL vertexShader
	 * @param fragmentFile ruta del archivo GLSL fragmentShader
	 */
	
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocation();
	}

	/**
	 * Método para asegurarnos que todas las variable uniformes han sido ubicadas dentro del programa Shader
	 */
	protected abstract void getAllUniformLocation();
	
	/**
	 * Método para obtener ubicación de la variable uniforme
	 * @param uniformName Normbre de la variable uniforme
	 * @return retorna un entero que representa la ubicación de la variable Uniforme
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * Método para ubicar de forma más simple la variable Uniforme por medio de flotantes
	 * Método para cargar datos flotantes dentro de los archivos Shaders
	 * @param location entero que representa la ubicación de la variable Uniforme
	 * @param value el tipo de Variable Uniforme
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**
	 *  Método para cargar vectores 3D dentro de los archivos Shaders
	 * @param location
	 * @param vector
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Método lógico para determinar si ha encontrado la ubicación de la ariable Uniforme
	 * Método lógico para poder utilizar en los archivos Shaders
	 * @param location entero que representa la ubicación de la variable Uniforme
	 * @param value si el valor es 0 se ha encontrado la variable Uniforme, si es 1 no se ha encontrado nada
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * Método para cargar una matriz 4X4 dentro de los archivos Shaders
	 * @param location entero que representa la ubicación de la variable Uniforme
	 * @param matrix
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	/**
	 * Método para que inicializa el programa de GLSL
	 */
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Método que se ejecuta una vez utilizado y eliminada la memoria
	 */
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	/**
	 * Método que libera la memoria del computador después de usar los shaders GLSL
	 */
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Al crear una clase abstracta, implementamos esta clase debe tener este método abstracto
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Método que enlaza el GLSL con el id del programa Shader
	 * @param attribute
	 * @param variableName
	 */
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Método para cargar el Shader.txt de tipo texto
	 * @param file la ruta del archivo GLSL
	 * @param type El tipo de archivo mediante un entero, si es VertexShader o fragmentShader
	 * @return 
	 */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}
}