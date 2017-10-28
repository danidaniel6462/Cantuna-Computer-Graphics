package futuroingeniero.models.staticModel.objConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.staticModel.objConverter.ModelData;

/**
 * 
 * @author Daniel  Loza
 *
 * <b>Clase OBJFileLoader</b> Clase que cargará un modelo formato .obj
 */
public class OBJFileLoader {
	
	private static final String RES_LOC = "res/obj/";

	/**
	 * Método para cargar el modelo .obj desde un directorio
	 * @param objFileName Directorio del cual se cargan los modelos .obj
	 * @return modelo con los datos ordenadas para poder añadir al VAO
	 */
	public static ModelData loadOBJ(String objFileName) {
		// creamos una variable para leer un archivo
		FileReader isr = null;
		// instanciamos el archivo que deseamos cargar desde el directorio de nuestro proyecto 
		File objFile = new File(RES_LOC + objFileName + ".obj");
		try {
			isr = new FileReader(objFile);
		} catch (FileNotFoundException e) {
			System.err.println("File not found in res; don't use any extention");
		}
		// guardamos en memoria la información del archivo y la desglosamos para obtener
		// la información necesaria para poder utilizar el .obj
		BufferedReader reader = new BufferedReader(isr);
		String line;
		// guardamos en listas los vértices, normales, coordenadas de textura y los índices para nuestro modelo
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		try {
			while (true) {
				// lee línea a línea el archivo .obj
				line = reader.readLine();
				// comprobamos si el texto empieza con v, son vectores
				// si es vt, son vértices de texturas
				// si es n, son puntos de normales
				// si es f, son los índices, lo más importante para poder dibujar correctamente el objeto 3D
				if (line.startsWith("v ")) {
					// separamos el texto del archivo en donde encuentre espacios en secciones para recolectar la información en los vértices, normales, texturas e índices :3
					String[] currentLine = line.split(" ");
					// creamos un vector3f que guardará la posición en x, y, z del modelo a renderizar
					Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]),
							(float) Float.valueOf(currentLine[3]));
					// creamos un objeto Vertex para guardar las posiciones de cada vértice del modelo
					Vertex newVertex = new Vertex(vertices.size(), vertex);
					// añadimos a la lista de vértices las posiciones
					vertices.add(newVertex);
				} else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]),
							(float) Float.valueOf(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					// una vez terminada la lectura de los vértices, normales y coordenadas de textura se procede a crear la dimensión de los arreglos de textura y normales 
					break;
				}
			}
			// bucle para leer las líneas que empiezan con f, para guardar las normales
			while (line != null && line.startsWith("f ")) {
				// leemos las líneas y comprobamos que empiece con la letra f, pero si no es así solo le decimos al computador que continúe con la siguient línea
				// comprobado que la línea empieza con la letra f
				// se procede a romper la línea en 3 partes que son los vértices del objeto
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				// se llama al método precessVertex para ordenar la información del archivo .obj para poder dibujar correctamente con OpenGL
				processVertex(vertex1, vertices, indices);
				processVertex(vertex2, vertices, indices);
				processVertex(vertex3, vertices, indices);
				// se lee una línea nueva del archivo .onj
				line = reader.readLine();
			}
			// cerramos el lector una vez que ya se llegó al final del archivo .obj
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading the file");
		}
		removeUnusedVertices(vertices);
		// convertimos la lista de vértices en un arreglo de flotadores para poder incluir la información en el VAO
		// creamos variables para guardar la información de las listas en los arreglos de flotantes

		// inicializar el arreglo de vértices con el tamaño de la lista de vértices posición se multiplica por 3 el tamaño puesto que la posición tiene 3 coordenadas (X, Y, Z)
		float[] verticesArray = new float[vertices.size() * 3];
		// arreglo para los textureCoords multiplicando el tamaño por 2 ya que tiene solo dos coordenadas (s, t)
		float[] texturesArray = new float[vertices.size() * 2];
		// arreglo para guardar las normales, se multiplica el tamaño por 3 que de igual forma tiene 3 coordendas cada vértice posición
		float[] normalsArray = new float[vertices.size() * 3];
		// Punto extremo
		float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
				texturesArray, normalsArray);
		// incializamos el arreglo para los índices con el tamaño de la lista de índices 
		int[] indicesArray = convertIndicesListToArray(indices);
		
		ModelData data = new ModelData(verticesArray, texturesArray, normalsArray, indicesArray,
				furthest);
		// retornamos finalmente el modelo con los datos ordenadas para poder añadir al VAO
		return data;
	}

	/**
	 * Método para procesar la información del archivo .obj y ubicar los datos en la posición correcta para ser procesada y poder dibujar el objeto 3D
	 * NOTA: este método está creado específicamente para archivos .obj creados en el software Blender para modelado 3D 
	 * @param vertex cadena de datos de los vértices
	 * @param vertices lista de vértices del modelo
	 * @param indices índices para unir las caras del modelo
	 */
	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
		} else {
			dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
					vertices);
		}
	}

	/**
	 * Método para convertir la lista de ïndices en un arreglo de índices
	 * @param indices
	 * @return
	 */
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	/**
	 * método para convertir todos los datos en un solo dato flotante
	 * @param vertices lista de vértices posición
	 * @param textures lista de texturas del modelo .obj
	 * @param normals lista de normales
	 * @param verticesArray Arreglo de vértices
	 * @param texturesArray arreglo de texturas
	 * @param normalsArray arreglo de normales
	 * @return retorna un punto extremo del modelo 
	 */
	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
			List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
			float[] normalsArray) {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
		}
		return furthestPoint;
	}

	/**
	 * Método para unir las caras del modelo correctamente y dibujar su textura correctmente
	 * @param previousVertex
	 * @param newTextureIndex
	 * @param newNormalIndex
	 * @param indices
	 * @param vertices
	 */
	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
			int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
						indices, vertices);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
			}

		}
	}
	
	/**
	 * Método para liberar memoria una vez utilizado cada vértice
	 * @param vertices
	 */
	private static void removeUnusedVertices(List<Vertex> vertices){
		for(Vertex vertex:vertices){
			if(!vertex.isSet()){
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}

}