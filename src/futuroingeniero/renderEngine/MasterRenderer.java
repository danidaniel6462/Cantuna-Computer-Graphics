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
 * Clase que manejar� toda la representaci�n del video juego
 * Procesador Principal
 */
public class MasterRenderer {
	// programa Shader para los modelos 3D
	private StaticShader shader = new StaticShader();
	// objeto para renderizar los modelos cargados en memoria incuyendo la c�mara
	private Renderer renderer = new Renderer(shader);
	
	private Map<TextureModel, List<Entity>> entidades = new HashMap<TextureModel, List<Entity>>();
	
	/**
	 * M�todo que realizar� la renderizaci�n de varios objetos en el escenario del video juego
	 * �ste m�todo es muy parecido a lo que tenemos en el bucle principal del juego en la clase MainGameLoop   
	 * @param sol variable para iluminar la escena
	 * @param camara proyecci�n de la c�mara y ubicaci�n de la misma 
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
	 * M�todo para limpiar el shader del video juego
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
