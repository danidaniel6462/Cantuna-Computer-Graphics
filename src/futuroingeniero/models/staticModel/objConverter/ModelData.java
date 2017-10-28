package futuroingeniero.models.staticModel.objConverter;

/**
 * 
 * @author Daniel Loza
 *
 *	<b>Clase para gestionar los datos del modelo que se cargará</b>
 */
public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;

	/**
	 *  Contructor de la clase Model Data
	 * @param vertices datos de las vértices del modelo
	 * @param textureCoords coordenadas de las texturas del modelo
	 * @param normals normales del modelo
	 * @param indices índices de unir las caras del modelo
	 * @param furthestPoint punto lejano
	 */
	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
			float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	/**
	 * @return el vertices
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * @return el textureCoords
	 */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/**
	 * @return el normals
	 */
	public float[] getNormals() {
		return normals;
	}

	/**
	 * @return el indices
	 */
	public int[] getIndices() {
		return indices;
	}

	/**
	 * @return el furthestPoint
	 */
	public float getFurthestPoint() {
		return furthestPoint;
	}
	

}
