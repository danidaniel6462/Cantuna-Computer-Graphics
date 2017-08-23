/**
 * 
 */
package futuroingeniero.renderEngine;

import static futuroingeniero.renderEngine.GlobalConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.TexturedModel;
import futuroingeniero.shaders.StaticShader;
import futuroingeniero.shaders.TerrainShader;
import futuroingeniero.skybox.SkyBoxRenderer;
import futuroingeniero.terrains.Terrain;

/**
 * @author Daniel Loza
 * Clase que manejará toda la representación del video juego
 * Procesador Principal
 */
public class MasterRenderer {

	private Matrix4f projectionMatrix;
	
	// programa Shader para los modelos 3D
	private StaticShader shader = new StaticShader();
	// objeto para renderizar los modelos cargados en memoria incuyendo la cámara
	private EntityRenderer renderer;
	
	// variable para renderizar el terreno
	private TerrainRenderer terrainRenderer;
	// variable para enlazar el programa Shader
	private TerrainShader terrainShader = new TerrainShader();
	
	// variable entidades de tipo MAP que guardará las entidades que se desean renderizar guardando así un código para poder encontrarlos a futuro y poder renderizar
	private Map<TexturedModel, List<Entity>> entidades = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyBoxRenderer syboxRenderer;
	
	/**
	 * Constructor de la Clase MasterRenderer
	 */
	public MasterRenderer(Loader loader) {
		// activamos la renderizaciónm de las caras que se ven, pero en este caso sacrificamos la parte posterior de las caras que no se ven
		// sacrificamos las caras posteriores
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		syboxRenderer = new SkyBoxRenderer(loader, projectionMatrix);
	}
	
	/**
	 * Método que activará las caras de las entidades con textura con transparencia
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * Método que realizará la renderización de varios objetos en el escenario del video juego
	 * Éste método es muy parecido a lo que tenemos en el bucle principal del juego en la clase MainGameLoop   
	 * @param sol variable para iluminar la escena
	 * @param camara proyección de la cámara y ubicación de la misma 
	 */
	public void render(List<Entity> items, List<Terrain> terrenos, List<Light> luces, Camara camara) {
		for (Entity item : items) {
			procesarEntidad(item);
		}
		for (Terrain terreno : terrenos) {
			procesarTerreno(terreno);
		}
		prepare();
        shader.start();
        shader.loadCieloColor(RED, GREEN, BLUE);
        shader.loadLuces(luces);
        shader.loadViewMatrix(camara);
        renderer.render(entidades);
        shader.stop();
        terrainShader.start();
        terrainShader.loadCieloColor(RED, GREEN, BLUE);
        terrainShader.loadLuces(luces);
        terrainShader.loadViewMatrix(camara);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        syboxRenderer.render(camara);
        terrains.clear();
        entidades.clear();
	}
	
	/**
	 * Método para procesar el terreno para poder mostrarlo en el espacio
	 * @param terrain variable para el modelo del terreno 
	 */
	public void procesarTerreno(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**
	 * Método para crear las entidades o modelos 3D
	 * @param entidad variable para determinar el modelo a renderizar 
	 */
	public void procesarEntidad(Entity entidad) {
		TexturedModel entidadModelo = entidad.getModel();
		List<Entity> batch = entidades.get(entidadModelo);
		
		// verificamos si el batch o lote está vacío para poder añadir más datos a la lista de entidades al HasMpa
		if(batch != null) {
			batch.add(entidad);
		} else {
			// caso contrario si está utilizado 
			// debemos crear una lista de lote nuevo con la entidad que deseamos utilizar
			List<Entity> newBatch = new ArrayList<Entity> ();
			newBatch.add(entidad);
			entidades.put(entidadModelo, newBatch);
		}
	}
	
	/**
	 * Método para limpiar los programas de Shader (sombreado) una vez cerrado el video juego 
	 */
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	/**
	 * Método que prepara el espacio donde se dibujarán los objetos 3D 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	/**
	 * Método para crear la matrix de proyección, 
	 */
    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = PLANO_LEJANO - PLANO_CERCANO;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((PLANO_LEJANO + PLANO_CERCANO) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * PLANO_CERCANO * PLANO_LEJANO) / frustum_length);
        projectionMatrix.m33 = 0;
    }
	
    /**
     * Métdo con Controles adicionales ara revisar el juego
     */
    public void controlesRenderizacion() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
		       GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		       GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
		       GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		       GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
    }
}
