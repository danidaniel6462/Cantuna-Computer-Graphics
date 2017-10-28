/**
 * 
 */
package futuroingeniero.engineMain;

import futuroingeniero.entities.Camara;

/**
 * @author Daniel Loza
 *
 */
public class Scene {

	private Camara camera;
	private SceneGUI sceneGui;
	private SceneEntity sceneEntity;

	/**
	 * Clase para crear una escena en el videojuego
	 * @param camera Cámara del videojuego
	 * @param sceneGui Guis del videojuego
	 * @param sceneEntity entidades del videojuego 
	 */
	public Scene(Camara camera, SceneGUI sceneGui, SceneEntity sceneEntity) {
		this.sceneGui = sceneGui;
		this.camera = camera;
		this.sceneEntity = sceneEntity;
	}

	/**
	 * @return the sceneGui
	 */
	public SceneGUI getSceneGui() {
		return sceneGui;
	}

	/**
	 * @return the camara
	 */
	public Camara getCamara() {
		return camera;
	}
	/**
	 * @return the sceneGui
	 */
	public SceneEntity getSceneEntity() {
		return sceneEntity;
	}
}