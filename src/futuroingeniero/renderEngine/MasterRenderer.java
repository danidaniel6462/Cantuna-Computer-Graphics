/**
 * 
 */
package futuroingeniero.renderEngine;

import static futuroingeniero.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.engineMain.Scene;
import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.staticModel.models.TexturedModel;
import futuroingeniero.shaders.StaticShader;
import futuroingeniero.shaders.TerrainShader;
import futuroingeniero.skybox.SkyBoxRenderer;
import futuroingeniero.terrains.Terrain;

/**
 * @author Daniel Loza
 * Clase que manejar� toda la representaci�n del video juego
 * Procesador Principal
 */
public class MasterRenderer {

	private Matrix4f projectionMatrix;
	
	// programa Shader para los modelos 3D
	private StaticShader shader = new StaticShader();
	// objeto para renderizar los modelos cargados en memoria incuyendo la c�mara
	private EntityRenderer renderer;
	
	// variable para renderizar el terreno
	private TerrainRenderer terrainRenderer;
	// variable para enlazar el programa Shader
	private TerrainShader terrainShader = new TerrainShader();
	
	// variable entidades de tipo MAP que guardar� las entidades que se desean renderizar guardando as� un c�digo para poder encontrarlos a futuro y poder renderizar
	private Map<TexturedModel, List<Entity>> entidades = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyBoxRenderer syboxRenderer;
	
	/**
	 * Constructor de la Clase MasterRenderer
	 * @param loader objeto para cargar los datos al VAO del SkyBox
	 */
	public MasterRenderer(Loader loader) {
		// activamos la renderizaci�nm de las caras que se ven, pero en este caso sacrificamos la parte posterior de las caras que no se ven
		// sacrificamos las caras posteriores
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		syboxRenderer = new SkyBoxRenderer(loader, projectionMatrix);
	}
	
	/**
	 * M�todo que activar� las caras de las entidades con textura con transparencia
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**
	 * M�todo que desactiva la renderizaci�n de las caras posteriores de las entidades
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * M�todo que realizar� la renderizaci�n de varios objetos en el escenario del video juego
	 * �ste m�todo es muy parecido a lo que tenemos en el bucle principal del juego en la clase MainGameLoop   
	 * @param items Lista de GameObjects
	 * @param terrenos Lista de terreno
	 * @param luces Lista de luces que se utlizan para iluminar el escenario
	 * @param camara objeto C�mara
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
        syboxRenderer.render(camara, RED, GREEN, BLUE);
        terrains.clear();
        entidades.clear();
	}	
	
	
	/**
	 * 
	 * @param escena Escena que contiene todos los par�metros para renderizar
	 */
	public void render(Scene escena){
		for (Entity item : escena.getSceneEntity().getItems()) {
			procesarEntidad(item);
		}
		for (Terrain terreno : escena.getSceneEntity().getTerrenos()) {
			procesarTerreno(terreno);
		}
		
		procesarEntidad(escena.getCamara().getPlayer());
				
		prepare();
        shader.start();
        shader.loadCieloColor(RED, GREEN, BLUE);
        shader.loadLuces(escena.getSceneEntity().getLuces());
        shader.loadViewMatrix(escena.getCamara());
        renderer.render(entidades);
        shader.stop();
        terrainShader.start();
        terrainShader.loadCieloColor(RED, GREEN, BLUE);
        terrainShader.loadLuces(escena.getSceneEntity().getLuces());
        terrainShader.loadViewMatrix(escena.getCamara());
        terrainRenderer.render(terrains);
        terrainShader.stop();
        syboxRenderer.render(escena.getCamara(), RED, GREEN, BLUE);
        terrains.clear();
        entidades.clear();
		
	}
	
	/**
	 * M�todo para procesar el terreno para poder mostrarlo en el espacio
	 * @param terrain variable para el modelo del terreno 
	 */
	public void procesarTerreno(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**
	 * M�todo para crear las entidades o modelos 3D
	 * @param entidad variable para determinar el modelo a renderizar 
	 */
	public void procesarEntidad(Entity entidad) {
		TexturedModel entidadModelo = entidad.getModel();
		List<Entity> batch = entidades.get(entidadModelo);
		
		// verificamos si el batch o lote est� vac�o para poder a�adir m�s datos a la lista de entidades al HasMpa
		if(batch != null) {
			batch.add(entidad);
		} else {
			// caso contrario si est� utilizado 
			// debemos crear una lista de lote nuevo con la entidad que deseamos utilizar
			List<Entity> newBatch = new ArrayList<Entity> ();
			newBatch.add(entidad);
			entidades.put(entidadModelo, newBatch);
		}
	}
	
	/**
	 * M�todo para limpiar los programas de Shader (sombreado) una vez cerrado el video juego 
	 */
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	/**
	 * M�todo que prepara el espacio donde se dibujar�n los objetos 3D 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	/**
	 * Retorno la matrix de proyecci�n 
	 * @return Retorna la matrix de proyecci�n 
	 */
	public Matrix4f getProyeccionMatrix() {
		return projectionMatrix;
	}
	
	/**
	 * M�todo para crear la matrix de proyecci�n, 
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
     * M�tdo con Controles adicionales para revisar el juego
     */
    public void controlesRenderizacion(/* List<GuiTexture> listaGUI */){
    	while (Keyboard.next()) {
	    	if (Keyboard.getEventKeyState()) {
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
		
    	/* if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			if(	Keyboard.isKeyDown(Keyboard.KEY_G)){
				Guardar.guardarEdicionGUI(listaGUI);
				System.out.println("logrado?");
			}
		}
		
		*/ 
    }
 
    
}
