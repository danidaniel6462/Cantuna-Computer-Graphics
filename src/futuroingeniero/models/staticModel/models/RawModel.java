package futuroingeniero.models.staticModel.models;

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
	 * @param vaoID Id del VAO que se toma para obtener los datos del modelo para ser renderizados 
	 * @param vertexCount N�mero de v�rtices de los modelos, se necesita saber cuantos v�rtices
	 * tiene el modelo ya que utilizamos el m�todo GL11.glDrawElements() que necesita saber el n�mero de v�rtices para renderizar
	 * 
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
