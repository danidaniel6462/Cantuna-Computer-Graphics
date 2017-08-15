/**
 * 
 */
package futuroingeniero.renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import futuroingeniero.models.RawModel;

/**
 * @author Daniel Loza
 * @version 1.0
 * 
 * Loader.java
 * Clase para cargar los modelos en memoria (VAO y VBO)
 */

public class Loader {
	// variables para guardar los VAO y VBO respectivamente para liberar memoria cuando el juego sea cerrado
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	/**
	 * Método para cargar los vértices en el VAO
	 * 
	 * En primer lugar guardamos el ID del VAO
	 * en segudo lugar guardamos en una posición del VAO
	 * en tercer lugar guardamos los datos de la posición
	 * en cuarto lugar, desvinculamos el VAO
	 * @param positions datos vector posición para guardar en el VAO
	 * @param textureCoords datos coordenadas de textura para guardar en el VAO
	 * @param normals normales de los objetos para guardar en el VAO
	 * @param indices índices para dibujar los modelos 3D correctamente
	 * @return retorna un RawModel para poder ser dibujado en el escenario
	 */

	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		// Crea un VAO vacío
		int vaoID = createVAO();
		// enlazamos los índices del modelo
		bindIndicesBuffer(indices);
		// almacenamos datos en el VAO en la posición 0, con 3 valores (la posición tiene 3 valores X, Y, Z)
		storeDataInAttributeList(0, 3, positions);
		// almacenamos datos en el VAO en la posición 1, con 2 valores (la textura tiene 2 posiciones, s, t)
		storeDataInAttributeList(1, 2, textureCoords);
		// almacenamos datos en el VAO en la posición 2, con 3 valores (las normales tiene 3 coordenadas al igual que la posición)
		storeDataInAttributeList(2, 3, normals);
		// Desactivamos el VAO que ya lo llenamos
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Método para cargar una textura através de un archivo .png
	 * @param fileName ruta de la imagen .png que se encuentra en la carpeta res/ del proyecto
	 * @return el ID de la textura cargada
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/texturas/" + fileName + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Método para limpiar la memoria del computador al cerrar la pantalla del Videojuego :3
	 */
	public void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vaos) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	/**
	 * Método para crear el VAO vació y dar un ID
	 * posteriormente activaremos el VAO creado
	 * @return vaoID valor de identificación (ID) del VAO
	 */
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		// almacenamos el identificador del VAO para poder liberar memoria posteriormente
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Método que almacenará los datos, através del ID del VAO 
	 *
	 * @param attributeNumber posición en el VAO
	 * @param data datos del modelo para guardar en el VAO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();		// creamos un VBO vacio
		// almacenamos el identificador del VBO para poder liberar memoria posteriormente
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // vinculamos un nuevo array Buffer con el vbo que hemos creado anteriormente
		FloatBuffer buffer = storeDataInFloatBuffer(data); // lo que hacemos es transformar los datos flotantes para poder guardarlos en el VBO
		// una vez que tenemos el VBO, tenemos que saber de que tipo datos serán y si el nuevo VBO será estático o editable
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// luego de haber preparado el VBO, especificamos el a que posición del VAO vamos a guardar los datos
		/**
		 *  glVertexAttribPointer
		 *  @param attributeNumber índice del VAO
		 *  @param coordinateSize tamaño de cada vértice, o tamaño de cada coordenada de textura
		 *  @param normalizar utilizamos false porque no realizamos ninguna normalización
		 *  @param GL11.GL_FLOAT tipo de dato
		 *  @param stride separación entre los datos que va a obtener del arreglo, utilizamos 0
		 *  @param buffer escribimos el número desde donde empezará la lectura de datos en el array
		 */
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		// una vez utilizado el VBO es necesario desvinular la memoria
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Método que desvincula el VAO (libera memoria del computador)
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Método para guardar los índices de un modelo 3D
	 * @param indices 
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Método para almacenar en un memoria intermedia el arreglo de enteros que son los índices en un Buffer de Enteros que serán utilizados en el VBO
	 * @param data Datos de los índices para el VBO
	 * @return arreglo transformado para usar los datos y guardarlos en el VBO
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Método para almacenar en un memoria intermedia el arreglo de flotadores en un Buffer de Flotadores que serán utilizados en el VBO 
	 * @param data Datos para el VBO
	 * @return arreglo transformado para usar los datos y guardarlos en el VBO
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data ) {
		// inicializamos un nuevo FloatBuffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		// importamos los datos del arreglo de flotantes y preparamos el buffer para ser leido
		buffer.put(data);
		// con la instrucción flip informamos al sistema que hemos terminado de escribi los datos en el FloatBuffer
		buffer.flip();
		return buffer;
	}
	
}
