/**
 * 
 */
package futuroingeniero.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Entity;
import futuroingeniero.models.RawModel;
import futuroingeniero.models.TextureModel;
import futuroingeniero.shaders.StaticShader;
import futuroingeniero.textures.ModelTexture;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 *
 * Renderer.java
 * Clase que será llamada una vez por frame
 */
public class Renderer {

	/**
	 * @param FOV ángulo de proyección de la cámara
	 * @param PLANO_CERCANO plano que está cercano a la cámara, punto en el eje Z
	 * @param PLANO_LEJANO plano de la profundidad de la cámara.
	 */
	private static final float FOV = 70;
	private static final float PLANO_CERCANO = 0.1f;
	private static final float PLANO_LEJANO = 1000f;
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	/**
	 * Contructor de la clase Renderer, incia la matriz de proyección (vista de la cámara)
	 * @param shader varible de la clase StaticShader 
	 */
	public Renderer(StaticShader shader) {
		// activamos la renderizaciónm de las caras que se ven, pero en este caso sacrificamos la parte posterior de las caras que no se ven
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Método que prepara el espacio donde se dibujarán los objetos 3D 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0.3f, 0.0f, 1);
	}
	
	/**
	 * render 
	 * Método que tiene un hasMap para identificar las texturas de las entidades
	 * @param entidades variable hasmap que tiene como Key el id de la textura y como datos una lista de entidades
	 */
	public void render(Map<TextureModel, List<Entity>> entidades) {
		for(TextureModel model : entidades.keySet()) {
			prepareTextureModel(model);
			List<Entity> batch = entidades.get(model);
			for(Entity entidad : batch) {
				prepareInstance(entidad);
				// realizar el render final en este espacio
				// dibujamos los elementos que cargamos en el modelo 3D
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTextureModel();
		}
	}
	
	/**
	 * Método que prepara el uso de la textura en el modelo, enlaza el modelo con la textura
	 * @param model modelo al cual se utilizará la textura
	 */
	private void prepareTextureModel(TextureModel model) {
		RawModel rawModel = model.getRawModel();
		// cargamos el VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		/**
		 * activamos los atributos a utilizar en el VAO, 
		 * posicion 0 para los vertexPosition,
		 * pos 1 para las coordenadas de la textura,
		 * pos 2 para las normales del modelo
		 */
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		// activamos el uso de texturas en el videojuego
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// vinculamos la textura con una llamada y su Identificación
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Método que desenlaza la textura del modelo
	 */
	private void unbindTextureModel() {
		// desactivamos los atributos usados del VAO
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		// finalmente desactivamos la lista VAO
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Método que prepara la instancia o las entidades de cada uno de los modelos texturizados
	 * @param entidad variable de hace referencia a un modelo 3D que queremos renderizar en el escenario 
	 */
	private void prepareInstance(Entity entidad) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entidad.getPosition(),
				entidad.getRotX(), entidad.getRotY(), entidad.getRotZ(), entidad.getScale());
		shader.loadTranformationMatrix(transformationMatrix);
	}
	
	/**
	 * @deprecated
	 * Método que dibuja todos los objetos dentro de la escena
	 * @param textureModel variable de un modelo y textura para ser renderizado
	 */

	public void render(Entity entidad, StaticShader shader) {
		TextureModel model = entidad.getModel();
		RawModel rawModel = model.getRawModel();
		// cargamos el VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		/**
		 * activamos los atributos a utilizar en el VAO, 
		 * posicion 0 para los vertexPosition,
		 * pos 1 para las coordenadas de la textura,
		 * pos 2 para las normales del modelo
		 */
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entidad.getPosition(),
				entidad.getRotX(), entidad.getRotY(), entidad.getRotZ(), entidad.getScale());
		shader.loadTranformationMatrix(transformationMatrix);
		
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		// activamos el uso de texturas en el videojuego
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// vinculamos la textura con una llamada y su Identificación
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		// dibujamos los elementos que cargamos en el modelo 3D
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		// desactivamos los atributos usados del VAO
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		// finalmente desactivamos la lista VAO
		GL30.glBindVertexArray(0);
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
}