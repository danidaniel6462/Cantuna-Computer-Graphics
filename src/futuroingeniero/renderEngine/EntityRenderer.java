/**
 * 
 */
package futuroingeniero.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import futuroingeniero.entities.Entity;
import futuroingeniero.models.RawModel;
import futuroingeniero.models.TexturedModel;
import futuroingeniero.shaders.StaticShader;
import futuroingeniero.textures.ModelTexture;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 * 
 * <h1>Clase EntityRender</h1>
 * 
 * Clase que renderiza las diferentes entidades o modelos 3D con sus respectivas características
 * en esta clase también se crea una cámara para poder ver el mundo creado con las entidades
 * <b>Nota:</b>
 * Esta clase carga los Shader de los modelos, se utiliza una vez que se crean las variables en los GLSL 
 */
public class EntityRenderer {

	private StaticShader shader;
	
	/**
	 * Contructor de la clase EntityRenderer, incia la matriz de proyección (vista de la cámara) y su programShader
	 * @param shader varible de la clase StaticShader 
	 * @param projectionMatrix vista de la cámara sobre la entidad
	 */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
	
	/**
	 * render 
	 * Método que tiene un hasMap para identificar las texturas de las entidades
	 * @param entidades variable hasmap que tiene como Key el id de la textura y como datos una lista de entidades
	 */
	public void render(Map<TexturedModel, List<Entity>> entidades) {
		for(TexturedModel model : entidades.keySet()) {
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
	private void prepareTextureModel(TexturedModel model) {
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
		
		shader.loadNumeroFilas(texture.getNumeroColumnas());
		
		// comprobamos si la textura tiene transparecia,
		// Si es verdadero deshabilitamos el sacrificio de las caras de la entidad
		if(texture.isTieneTransparencia()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFalsaIluminacionVariable(texture.isUsaFalsaIluminacion());		
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
		// activamos el sacrificio de las caras posteriores de las entidades, es decir las caras que no se ven
		MasterRenderer.enableCulling();
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
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entidad.getTextureXOffset(), entidad.getTextureYOffset());
	}
	/**
	 * @deprecated
	 * Método que dibuja todas las entidades dentro de la escena
	 * @param entidad objeto 3D para renderizar
	 * @param shader variable que enlaza el programa Shader con la entidad
	 */
	public void render(Entity entidad, StaticShader shader) {
		TexturedModel model = entidad.getModel();
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
		shader.loadTransformationMatrix(transformationMatrix);
		
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
}