/**
 * 
 */
package futuroingeniero.deprecated;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.staticModel.models.RawModel;
import futuroingeniero.renderEngine.Loader;

/**
 * 
 * @author Daniel Loza
 *
 *         Clase para cargar modelos .obj de cualquier software de modelado, en
 *         este caso yo uso Blender
 * 
 * @deprecated Esta clase de Obsoleta ya que existe una mejor clase para poder
 *             cargar los modelos .OBJ, ya que se han corregido errores como los
 *             que se tenían que realizar cortes en cada parte de la malla para
 *             que el modelo reciba la textura de forma correcta
 * 
 *             La forma de inicializar el modelo con esta clase en el main o
 *             donde ocurra la inicialización de los modelos estáticos debe ser
 *             la siguiente:
 * 
 *             Principal{
 * 
 *             Loader loader;
 * 
 *             TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("ejemploOBJ", loader), 
 *             new ModelTexture(loader.loadTexture("objetos/texturaOBJ")));
 * 
 *             }
 * 
 */

public class OBJLoader {

	/**
	 * Método para cargar los datos en un VAO de un archivo .obj
	 * 
	 * @param fileName
	 *            ubicación y nombre del archivo el cual se desea importar
	 * @param loader
	 *            variable para cargar los vbo dentro del los vao
	 * @return obtenemos un modelo listo y agregado al VAO para poder renderizar
	 *         en el escenario
	 */
	public static RawModel loadObjModel(String fileName, Loader loader) {
		// creamos una variable para leer un archivo
		FileReader fr = null;
		try {
			// indicamos la ruta del archivo con su respectiva extensión
			fr = new FileReader(new File("res/obj/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("No es posible cargar el archivo .obj");
			e.printStackTrace();
		}
		// guardamos en memoria la información del archivo y la desglosamos para
		// obtener
		// la información necesaria para poder utilizar el .obj
		BufferedReader reader = new BufferedReader(fr);
		String line;
		// guardamos en listas los vértices, normales, coordenadas de textura y
		// los índices para nuestro modelo
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();

		// variables para almacenar los datos del archivo .obj
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try {
			// bucle infinito para leer todo el archivo hasta la última línea
			// del .obj
			while (true) {
				// lee línea a línea el archivo
				line = reader.readLine();
				// separamos el texto del archivo en donde encuentre espacios en
				// secciones para recolectar la información en los vértices,
				// normales, texturas e índices :3
				String[] currentLine = line.split(" ");
				// comprobamos si el texto empieza con v, son vectores
				// si es vt, son vértices de texturas
				// si es n, son puntos de normales
				// si es f, son los índices, lo más importante para poder
				// dibujar correctamente el objeto 3D
				if (line.startsWith("v ")) {
					// creamos un vector3f que guardará la posición en x, y, z
					// del modelo a renderizar
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					// guardamos en la lista las posiciones encontradas en la
					// línea de código anterior
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					// una vez terminada la lectura de los vértices, normales y
					// coordenadas de textura se procede a crear la dimensión de
					// los arreglos de textura y normales
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			// bucle para leer las líneas que empiezan con f, para guardar las
			// normales
			while (line != null) {
				// leemos las líneas y comprobamos que empiece con la letra f,
				// pero si no es así solo le decimos al computador que continúe
				// con la siguient línea
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				// comprobado que la línea empieza con la letra f
				// se procede a romper la línea en 3 partes que son los vértices
				// del objeto
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				// se llama al método precessVertex para ordenar la información
				// del archivo .obj para poder dibujar correctamente con OpenGL
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				// se lee una línea nueva del archivo .onj
				line = reader.readLine();
			}
			// cerramos el lector una vez que ya se llegó al final del archivo
			// .obj
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// convertimos la lista de vértices en un arreglo de flotadores para
		// poder incluir la información en el VAO
		// creamos variables para guardar la información de las listas en los
		// arreglos de flotantes

		// inicializar el arreglo de vértices con el tamaño de la lista de
		// vértices posición se multiplica por 3 el tamaño puesto que la
		// posición tiene 3 coordenadas (X, Y, Z)
		verticesArray = new float[vertices.size() * 3];
		// incializamos el arreglo para los índices con el tamaño de la lista de
		// índices
		indicesArray = new int[indices.size()];

		// bucle creado para realizar la converción de la lista al arreglo de
		// flotantes para los vértices posición e índices respectivamente
		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		// retornamos finalmente el modelo cargado en el VAO
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
	}

	/**
	 * Método para procesar la información del archivo .obj y ubicar los datos
	 * en la posición correcta para ser procesada y poder dibujar el objeto 3D
	 * NOTA: este método está creado específicamente para archivos .obj creados
	 * en el software Blender para modelado 3D
	 * 
	 * @param vertexData
	 *            cadena de datos de los vértices
	 * @param indices
	 *            índices para poder unir los vértices del modelo
	 * @param textures
	 *            coordenadas de la textura del objeto 3D
	 * @param normals
	 *            normales para recibir correctamente la iluminación y
	 *            renderizado de nuestro objeto
	 * @param textureArray
	 *            lista de las coordenadas de la textura
	 * @param normalsArray
	 *            lista de las normales del objeto
	 */
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		// variable para poder asociar los vectores posición, texturas y
		// normales
		// para poder ponerlas en la posición correcta para poder dibujar
		// correctamente el modelo
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		// guaradamos los índices en el orden correcto
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}