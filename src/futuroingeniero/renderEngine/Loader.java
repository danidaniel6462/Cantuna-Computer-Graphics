/**
 * 
 */
package futuroingeniero.renderEngine;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import futuroingeniero.models.RawModel;
import futuroingeniero.textures.TextureData;

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
	 * M�todo para cargar el modelo a memoria
	 * 
	 * En primer lugar guardamos el ID del VAO
	 * en segudo lugar guardamos en una posici�n del VAO
	 * en tercer lugar guardamos los datos de la posici�n
	 * en cuarto lugar, desvinculamos el VAO
	 * @param positions vector con los v�rtices posici�n del modelo para guardar en el VAO
	 * @param textureCoords coordenadas de textura para guardar en el VAO
	 * @param normals normales de los objetos para guardar en el VAO
	 * @param indices �ndices para dibujar los modelos 3D correctamente
	 * @return retorna un RawModel para poder ser dibujado en el escenario
	 */

	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		// Crea un VAO vac�o
		int vaoID = createVAO();
		// enlazamos los �ndices del modelo
		bindIndicesBuffer(indices);
		// almacenamos datos en el VAO en la posici�n 0, con 3 valores (la posici�n tiene 3 valores X, Y, Z)
		storeDataInAttributeList(0, 3, positions);
		// almacenamos datos en el VAO en la posici�n 1, con 2 valores (la textura tiene 2 posiciones, s, t)
		storeDataInAttributeList(1, 2, textureCoords);
		// almacenamos datos en el VAO en la posici�n 2, con 3 valores (las normales tiene 3 coordenadas al igual que la posici�n)
		storeDataInAttributeList(2, 3, normals);
		// Desactivamos el VAO que ya lo llenamos
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Cargar al GUI y/o SkyBox a memoria del VAO
	 * 
	 * @param posiciones: Posiciones de
	 * @param dimensiones: valor que indica si vamos a renderizar un GUI o un SkyBox
	 *			siendo: dimensiones = 2, para cargar un modelo 2D, GUI
	 *				 	dimensiones = 3, para cargar un modelo 3D, SkyBox
	 * @return devuelve un RawModel para poder utilizarlo en los GUI o el SkyBox
	 */
	public RawModel loadToVAO(float[] posiciones, int dimensiones) {
		// Crea un VAO vac�o
		int vaoID = createVAO();
		// almacenamos datos en la posici�n 0 del VAO, con 2 valores (la posici�n tiene 2 valores X, Y)
		// la posici�n es de 2 valores xq es un GUI, s�lo se ve en la pantalla
		this.storeDataInAttributeList(0, dimensiones, posiciones);
		// Desactivamos el VAO que ya lo llenamos
		unbindVAO();
		// el vector de 8 V�rtices posici�n es dividido para 2 porque se utiliza TriangleStrip para renderizar el cuadrado
		// es decir que solo utilizamos 4 v�rtices, as� que se necesitan solo 4 �ndices para los GUI
		return new RawModel(vaoID, posiciones.length / dimensiones);
	}
	
	/**
	 * M�todo para cargar una textura atrav�s de un archivo .png
	 * @param fileName ruta de la imagen .png que se encuentra en la carpeta res/ del proyecto
	 * @return el ID de la textura cargada
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/texturas/" + fileName + ".png"));
			// generamos la resoluci�n m�s baja de una textura 
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			// Generamos los par�metros de textura
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nMI ERROR: Intent� cargar la textura " + fileName + ".png Pero no funcion�\n");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	/**
	 * M�todo para limpiar la memoria del computador al cerrar la pantalla del Videojuego :3
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
	 * M�etodo para cargar un CubeMap
	 *  para utilizarlo en el SkyBox
	 * @param textureFiles Arreglo de Strings que contiene los nombre de los seis archivos que formar�n el CubeMpa para el Skybox
	 * @return devuleve la identificaci�n del CubeMap para utilizarlo en el Skybox del videojuego
	 */
	public int loadCubeMap(String[] textureFiles) {
		// generar una textura completamente vac�a para openGL 
		int texID = GL11.glGenTextures();
		// activamos la textura, demodo que se pueda utilizar la informaci�n de las textura como mejor convenga
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// utiilzamos la informaci�n del id de la textura generada para vincular con datos para el CubeMap
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for(int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/texturas/skybox/" + textureFiles[i] + ".png");
			// cargamos los datos de las  texturas al CubeMap como imagenes 2D
			/**
			 * glTexImage2D, par�metros
			 * 1: target: es la cara del CubeMap que se quiere cargar la textura
			 * 2: level: 0
			 * 3: internalFormat: formato que queremos almacenar los datos, las caras son enteros consecutivos que se pueden recorrer f�cilmente en el bucle
			 * 4: width: ancho de la textura
			 * 5: height: alto de la textura
			 * 6: border: frontera de la textura, valor 0, porque no es necesario un borde en las texturas
			 * 7: format: nuevo formato de datos
			 * 8: type: el tipo de datos, asignamos los bites sin signo
			 * 9: p�xeles: N�mero de datos que tenemos en el buffer de la textura
			 */
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
					data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
					data.getBuffer());
		}
		// suavizamos la textura con GL_TEXTURE_MAG_FILTER y GL_TEXTURE_MAG_FILTER, convirtiendo en una suavidad Lineal con GL_LINEAR 
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		// finalmente se a�ade a la lista con el objetivo de que se libere la memoria una vez cerrado el juego
		textures.add(texID);
		return texID;
	}
	
	/**
	 * M�todo que carga una imagen a la memoria para decodificarlo para posteriormente utilizarlo para un SkyBox
	 * utilizado para cargar las im�genes en objetos de datos de textura que lo convierte en una textura suficientemente grande para no perder resoluci�n de la textura 
	 * @param fileName
	 * @return devuelve como un objeto de datos de textura para el SkyBox, almacenando el buffer de datos, el ancho y alto 
	 */
	private TextureData decodeTextureFile(String fileName) {
		// ancho de la imagen
		int width = 0;
		// alto de la imagen
		int height = 0;
		// creaci�n de un byteBuffer
		ByteBuffer buffer = null;
		try {
			// obteniendo el archivo
			FileInputStream in = new FileInputStream(fileName);
			// decodificando la imagen 
			PNGDecoder decoder = new PNGDecoder(in);
			// ancho y alto de la imagen decodificada
			width = decoder.getWidth();
			height = decoder.getHeight();
			// tama�o del buffer 
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\nPruebe a cargar bien la textura " + fileName + ", no funciona!!");
			System.exit(-1);
		}
		// devuleve como un objeto de datos de textura
		return new TextureData(buffer, width, height);
	}
	
	/**
	 * M�todo para crear el VAO vaci� y dar un ID
	 * posteriormente activaremos el VAO creado
	 * @return vaoID valor de identificaci�n (ID) del VAO, valor �nico
	 */
	private int createVAO(){
		// creamos un VAO nuevo para almacenar los datos de una entidad o modelo
		int vaoID = GL30.glGenVertexArrays();
		// almacenamos el identificador del VAO para poder liberar memoria posteriormente
		vaos.add(vaoID);
		// enlazamos el VAO
		GL30.glBindVertexArray(vaoID);
		return vaoID; //ID unico del VAO
	}
	
	/**
	 * M�todo que almacenar� los datos, atrav�s del ID al VAO 
	 *
	 * @param attributeNumber posici�n en el VAO
	 * @param data datos del modelo para guardar en el VAO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();		 // Genera un ID para el VBO (Lista de buffers de Datos) unico.
		// almacenamos el identificador del VBO para poder liberar memoria posteriormente
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // vinculamos un nuevo array Buffer con el vbo que hemos creado anteriormente
		FloatBuffer buffer = storeDataInFloatBuffer(data); // lo que hacemos es transformar los datos flotantes para poder guardarlos en el VBO
		// una vez que tenemos el VBO, tenemos que saber de que tipo datos ser�n y si el nuevo VBO ser� est�tico o editable
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// luego de haber preparado el VBO, especificamos el a que posici�n del VAO vamos a guardar los datos
		/**
		 *  glVertexAttribPointer
		 *  @param attributeNumber �ndice del VAO
		 *  @param coordinateSize tama�o de cada v�rtice, o tama�o de cada coordenada de textura
		 *  @param normalizar utilizamos false porque no realizamos ninguna normalizaci�n
		 *  @param GL11.GL_FLOAT tipo de dato
		 *  @param stride separaci�n entre los datos que va a obtener del arreglo, utilizamos 0
		 *  @param buffer escribimos el n�mero desde donde empezar� la lectura de datos en el array
		 */
		//Asignamos el VBO al VAO 
		//1) Posici�n que ocupar� el VBO en el VAO
		//2) Tama�o del objeto (X,Y,Z)
		//3) Tipo de datos a alamacenar (floats)
		//4) Normalizar la info (Apuntar a una misma direccion)
		//5) Distancia entre puntos
		//6) Compensar informaci�n
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		// Desvinculamos el VBO actual
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * M�todo que desvincula el VAO (libera memoria del computador)
	 * desvinculamos el Array de Objetos
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * M�todo para guardar los �ndices de un modelo 3D
	 * Vinculamos los �ndices del VBO
	 * @param indices �ndices del modelo
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * M�todo para almacenar en un memoria intermedia el arreglo de enteros que son los �ndices en un Buffer de Enteros que ser�n utilizados en el VBO
	 * @param data Datos de los �ndices para el VBO
	 * @return arreglo transformado para usar los datos y guardarlos en el VBO
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * M�todo para almacenar en un memoria intermedia el arreglo de flotadores en un Buffer de Flotadores que ser�n utilizados en el VBO 
	 * @param data Datos para el VBO
	 * @return arreglo transformado para usar los datos y guardarlos en el VBO
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data ) {
		// inicializamos un nuevo FloatBuffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		// importamos los datos del arreglo de flotantes y preparamos el buffer para ser leido
		buffer.put(data);
		// con la instrucci�n flip informamos al sistema que hemos terminado de escribi los datos en el FloatBuffer
		buffer.flip();
		return buffer;
	}
	
}
