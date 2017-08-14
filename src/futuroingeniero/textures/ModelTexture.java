/**
 * 
 */
package futuroingeniero.textures;

/**
 * @author Daniel Loza
 *
 * Clase que cargará las texturas para los modelos 3D del videojuego
 */
public class ModelTexture {
	/**
	 * @param textureID identificación de la textura que se carga para el objeto 3D 
	 * @param shineDamper variable que determina el cuánto absorbe de brillo el objeto 3D, en otras palabras es la cantidad de brillo que rebota en el modelo3D
	 * 					  mientras más grande el valor menor será el reflejo en el objeto 3D
	 * @param reflectivity variable para determinar el brillo del material del objeto 3D
	 * 
	 */
	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	/**
	 * Constructor que tiene como parámetro el id de la textura
	 * @param id identificación de la textura
	 */
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	/**
	 * @return el shineDamper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * @param shineDamper el shineDamper a establecer
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return el reflectivity
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivity el reflectivity a establecer
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * Método que retorna el ID de la textura
	 * @return id de la textura
	 */
	public int getID() {
		return this.textureID;
	}

}
