/**
 * 
 */
package futuroingeniero.skybox;

import static futuroingeniero.utils.GlobalConstants.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.models.staticModel.models.RawModel;
import futuroingeniero.renderEngine.Loader;
import futuroingeniero.utils.DisplayManager;

/**
 * @author Daniel Loza
 *
 * <h1>Clase SkyBoxRenderer </h1>
 * Clase que renderiza el Skybox en el escenario del Videojuego
 */
public class SkyBoxRenderer {

	private RawModel cubo;
	private int diaTextura;
	private int nocheTextura;
	private SkyBoxShader shader;
	// valor de tiempo variable par realizar el ciclo Día/Noche
	private float time = 0;
	
	/**
	 * Constructor de la Clase SkyBoxRenderer
	 * @param loader objeto para cargar el modelo al VAO
	 * @param proyeccionMatriz Matriz de proyección
	 */
	public SkyBoxRenderer(Loader loader, Matrix4f proyeccionMatriz) {
		cubo = loader.loadToVAO(VERTICES_SKYBOX, 3);
		diaTextura = loader.loadCubeMap(TEXTURE_FILES);
		nocheTextura = loader.loadCubeMap(TEXTURE_FILES_NIGHT);
		shader = new SkyBoxShader();
		shader.start();
		shader.conectarUnidadesTexturas();
		shader.loadProjectionMatrix(proyeccionMatriz);
		shader.stop();
	}
	
	/**
	 * Método que renderiza el SkyBox
	 * @param camara Carga el punto de vista de la cámara para el SkyBox
	 * @param r color rojo
	 * @param g color verde
	 * @param b color azul
	 */
	public void render(Camara camara, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camara);
		shader.loadFogColor(r, g, b);
		// vinculamos el VAO que tiene la id del Cubo
		GL30.glBindVertexArray(cubo.getVaoID());
		// posición de los datos en el VAO
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		// dibuja el Cubo
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubo.getVertexCount());
		// desactivamos el uso del atributo una vez guardada la información
		GL20.glDisableVertexAttribArray(0);
		// deshabilitamos el VAO
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	/**
	 * Método para vincular las texturas en el SkyBox
	 * este método también activa el factor de mezcla entre las dos texturas
	 * el valor 0 carga la textura del día
	 * el valor de 1 cargar la textura de la noche 
	 */
	private void bindTextures() {
		time += DisplayManager.deltaTime() * 1000;
		time %= 24000;
		int texture1;
		int texture2;
		float blendFactor;		
		if(time >= 0 && time < 5000){
			texture1 = nocheTextura;
			texture2 = nocheTextura;
			RED = 0.01f;
			GREEN = 0.01f;
			BLUE = 0.01f;
			blendFactor = (time - 0)/(5000 - 0);
		}else if(time >= 5000 && time < 8000){
			texture1 = nocheTextura;
			texture2 = diaTextura;
		    RED += 0.00157f;
		    GREEN += 0.00157f;
		    BLUE += 0.0018f;
			blendFactor = (time - 5000)/(8000 - 5000);
		}else if(time >= 8000 && time < 21000){
			texture1 = diaTextura;
			texture2 = diaTextura;	
			RED = 0.5444f; 
			GREEN = 0.62f; 
			BLUE =  0.69f;	
			blendFactor = (time - 8000)/(21000 - 8000);
		}else{
			texture1 = diaTextura;
			texture2 = nocheTextura;
		    RED -= 0.002f;
		    GREEN -= 0.002f;
		    BLUE -= 0.002f;
			blendFactor = (time - 21000)/(24000 - 21000);
		}
		
		// activamos la textura 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// unimos el CubeMap con la textura 0 activa
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		// activamos la textura 1
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		// unimos el CubeMap con la textura 1 activa
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		// factor de mezcla entre las texturas del SkyBox (CubeMap) 
		shader.loadBlendFactor(blendFactor);
	}
}
