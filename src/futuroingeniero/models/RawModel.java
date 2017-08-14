package futuroingeniero.models;

/**
 * @author Daniel Loza
 * @version 1.0
 * 
 * RawModel.java
 * Clase para gestionar los datos de los modelos
 */

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	/**
	 * Clase para gestionar los datos de los modelos
	 * 
	 * @param vaoID Id del VAO
	 * @param vertexCount Número de vértices
	 */
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	/**
	 * @return the vaoID
	 */
	public int getVaoID() {
		return vaoID;
	}
	/**
	 * @return the vertexCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}

}
