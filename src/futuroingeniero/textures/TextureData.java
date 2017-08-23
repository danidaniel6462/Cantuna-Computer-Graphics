/**
 * 
 */
package futuroingeniero.textures;

import java.nio.ByteBuffer;

/**
 * @author Daniel Loza
 *
 * 
 */
public class TextureData {
	private int width;
	private int height;
	// buffer de datos decodificados de una imagen
	private ByteBuffer buffer;
	
	/**
	 * Constructor de la Clase TextureData
	 * @param buffer datos decodificados de una imagen
	 * @param width Ancho de la imgen que se carga
	 * @param height Alto de la imagen que se carga
	 */
	public TextureData(ByteBuffer buffer, int width, int height){
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return el width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return el height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return el buffer
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
