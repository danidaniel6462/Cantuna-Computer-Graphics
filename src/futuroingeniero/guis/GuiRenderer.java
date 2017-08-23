/**
 * 
 */
package futuroingeniero.guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.models.RawModel;
import futuroingeniero.renderEngine.EntityRenderer;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 *
 * <h1>Clase GuiRenderer</h1>
 * Clase que renderiza los GUI del juego
 */
public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;
	
	/**
	 * Constructor de la Clase GuiRenderer
	 * En este método inicializamos las posiciones de la pantalla a 
	 * 
	 *   (-1, 1)				  (1, 1)
	 * 		________________________
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 *  	-------------------------
	 *   (-1, -1)				 (1, -1)
	 * 
	 * @param loader objeto para cargar los GUI para el videojuego
	 */
	public GuiRenderer(Loader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	/**
	 * Método para renderizar el GUI en la Pantalla del videojuego
	 * <b>Este método es muy parecido al método para renderizar las Entidades, ver la Clase {@link EntityRenderer}</b>
	 * @param guisList lista de GUI para renderizar
	 */
	public void render(List<GuiTexture> guisList){
		shader.start();
		// cargamos el VAO
		GL30.glBindVertexArray(quad.getVaoID());
		/**
		 * activamos los atributos a utilizar en el VAO, 
		 * utilizamos los datos de la posicion 0 del VAO para los vertexPosition del GUI
		 */
		GL20.glEnableVertexAttribArray(0);
		
		// render del GUI
		// activamos Blend para poder ver los objetos con transparencia
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// desactivamos el test de profundidad ya que deseamos ver los GUI que están delante de los GUI anteriores
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		// recorremos la lista de GUI para renderizar
		for (GuiTexture gui: guisList) {
			// activamos el uso de texturas en el videojuego
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			// vinculamos la textura con una llamada y su Identificación
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			// matriz que recibe las transformaciones de posición y escala del GUI
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			// carga la transformación en la matriz al Shader Uniform
			shader.loadTransformation(matrix);
			// dibujamos los elementos que cargamos para el GUI actual del bucle
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		// desactivamos los atributos usados del VAO
		GL20.glDisableVertexAttribArray(0);
		// desactivamos el VAO
		GL30.glBindVertexArray(0);
		shader.stop();
		
	}
	
	/**
	 * Método para limpiar la memoria del uso del Shader una vez que se cierre el juego
	 */
	public void cleanUp(){
		 shader.cleanUp();
	}
}
