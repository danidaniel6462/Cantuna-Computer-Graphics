/**
 * 
 */
package futuroingeniero.textures;

/**
 * @author Daniel Loza
 *
 * Clase que cargar� las texturas para los modelos 3D del videojuego
 */
public class ModelTexture {
	/**
	 * @param textureID identificaci�n de la textura que se carga para el objeto 3D 
	 * @param shineDamper variable que determina el cu�nto absorbe de brillo el objeto 3D, en otras palabras es la cantidad de brillo que rebota en el modelo3D
	 * 					  mientras m�s grande el valor menor ser� el reflejo en el objeto 3D
	 * @param reflectivity variable para determinar el brillo del material del objeto 3D
	 * @param tieneTransparencia variable l�gica para saber si la textura tiene transparencia o no
	 * @param usaFalsaIluminacion variable para definir una falsa iluminaci�n a los objetos texturizados 
	 * @param numeroFilas valor para una textura atlas, inicializamos en 1, porque suponemos que todas las texturas son atlas de dimensi�n 1x1
	 */
	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean tieneTransparencia = false;
	private boolean usaFalsaIluminacion = false;

	private int numeroFilas = 1;
	
	/**
	 * @return el numeroColumnas
	 */
	public int getNumeroColumnas() {
		return numeroFilas;
	}

	/**
	 * @param numeroColumnas el numeroColumnas a establecer
	 */
	public void setNumeroFilas(int numeroColumnas) {
		this.numeroFilas = numeroColumnas;
	}

	/**
	 * @return el usaFalsaIluminacion
	 */
	public boolean isUsaFalsaIluminacion() {
		return usaFalsaIluminacion;
	}

	/**
	 * @param usaFalsaIluminacion el usaFalsaIluminacion a establecer
	 */
	public void setUsaFalsaIluminacion(boolean usaFalsaIluminacion) {
		this.usaFalsaIluminacion = usaFalsaIluminacion;
	}

	/**
	 * @return el tieneTransparencia
	 */
	public boolean isTieneTransparencia() {
		return tieneTransparencia;
	}

	/**
	 * @param tieneTransparencia el tieneTransparencia a establecer
	 */
	public void setTieneTransparencia(boolean tieneTransparencia) {
		this.tieneTransparencia = tieneTransparencia;
	}

	/**
	 * Constructor que tiene como par�metro el id de la textura
	 * @param id identificaci�n de la textura
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
	 * M�todo que retorna el ID de la textura
	 * @return id de la textura
	 */
	public int getID() {
		return this.textureID;
	}

}
