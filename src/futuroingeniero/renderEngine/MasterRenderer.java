/**
 * 
 */
package futuroingeniero.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.entities.Light;
import futuroingeniero.models.TextureModel;
import futuroingeniero.shaders.StaticShader;

/**
 * @author Daniel Loza
 * Clase que manejará toda la representación del video juego
 * Procesador Principal
 */
public class MasterRenderer {
	// programa Shader para los modelos 3D
	private StaticShader shader = new StaticShader();
	// objeto para renderizar los modelos cargados en memoria incuyendo la cámara
	private Renderer renderer = new Renderer(shader);
	
	private Map<TextureModel, List<Entity>> entidades = new HashMap<TextureModel, List<Entity>>();
	
	/**
	 * Método que realizará la renderización de varios objetos en el escenario del video juego
	 * Éste método es muy parecido a lo que tenemos en el bucle principal del juego en la clase MainGameLoop   
	 * @param sol variable para iluminar la escena
	 * @param camara proyección de la cámara y ubicación de la misma 
	 */
	public void render(Light sol, Camara camara) {
		renderer.prepare();
		shader.start();
		shader.loadLuz(sol);
		shader.loadViewMatrix(camara);
		renderer.render(entidades);
		shader.stop();
		entidades.clear();
	}
	
	public void procesarEntidad(Entity entidad) {
		TextureModel entidadModelo = entidad.getModel();
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
	 * Método para limpiar el shader del video juego
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
