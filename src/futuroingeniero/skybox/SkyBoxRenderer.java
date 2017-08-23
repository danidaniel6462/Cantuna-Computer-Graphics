/**
 * 
 */
package futuroingeniero.skybox;

import static futuroingeniero.renderEngine.GlobalConstants.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.models.RawModel;
import futuroingeniero.renderEngine.Loader;

/**
 * @author Daniel Loza
 *
 * <h1>Clase SkyBoxRenderer </h1>
 * Clase que renderiza el Skybox en el escenario del Videojuego
 */
public class SkyBoxRenderer {

	private RawModel cubo;
	private int textura;
	private SkyBoxShader shader;
	
	public SkyBoxRenderer(Loader loader, Matrix4f proyeccionMatriz) {
		cubo = loader.loadToVAO(VERTICES_SKYBOX, 3);
		textura = loader.loadCubeMap(TEXTURE_FILES);
		shader = new SkyBoxShader();
		shader.start();
		shader.loadProjectionMatrix(proyeccionMatriz);
		shader.stop();
	}
	
	public void render(Camara camara) {
		shader.start();
		shader.loadViewMatrix(camara);
		// vinculamos el VAO que tiene la id del Cubo
		GL30.glBindVertexArray(cubo.getVaoID());
		// posición de los datos en el VAO
		GL20.glEnableVertexAttribArray(0);
		// activamos una textura
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// unimos el CubeMap con la textura activa
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textura);
		// dibuja el Cubo
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubo.getVertexCount());
		// desactivamos el uso del atributo una vez guardada la información
		GL20.glDisableVertexAttribArray(0);
		// deshabilitamos el VAO
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
}
