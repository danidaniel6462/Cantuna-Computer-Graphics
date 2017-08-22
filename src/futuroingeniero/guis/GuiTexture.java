/**
 * 
 */
package futuroingeniero.guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * @author Daniel Loza
 *
 * <h1>Clase GuiTexture</h1>
 * Clase que carga la textura, posici�n y escala para los GUI de mi juego CANTU�A
 * :3
 */
public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;

	/**
	 * Constructor de la Clase GuiTexture
	 * @param texture identificaci�n de la textura que se utiliza para el GUI
	 * @param position posici�n de la imagen GUI
	 * @param scale escala del GUI
	 * 
	 * 	 *   (-1, 1)				  (1, 1)
	 * 		____________|____________
	 * 		|			|			|
	 * 		|			|			|
	 * 		|			|	  (*) 	|		* posici�n 0.5, 0.5 con 0.25, 0.25 de tama�o en ancho y alto respectivamente 
	 * 		|			|			|		NOTA: la escala del GUI es con relaci�n a la pantalla, es decir
	 * 	   _|___________|___________|___		  Se toma 0.25 del ancho de la pantalla que inicializamos en 1 y para el c�lculo del alto de igual forma 
	 * 		|			|			|
	 * 		|			|			|
	 * 		|			|			|
	 * 		|			|			|
	 *  	------------|------------
	 *   (-1, -1)				 (1, -1)
	 *   
	 */
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * @return el texture
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * @param texture el texture a establecer
	 */
	public void setTexture(int texture) {
		this.texture = texture;
	}

	/**
	 * @return el position
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @param position el position a establecer
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * @return el scale
	 */
	public Vector2f getScale() {
		return scale;
	}

	/**
	 * @param scale el scale a establecer
	 */
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	
}
