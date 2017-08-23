/**
 * 
 */
package futuroingeniero.renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.RawModel;
import futuroingeniero.shaders.TerrainShader;
import futuroingeniero.terrains.Terrain;
import futuroingeniero.textures.TerrainTexturePack;
import futuroingeniero.toolbox.Maths;

/**
 * @author Daniel Loza
 *
 */
public class TerrainRenderer {

	private TerrainShader shader;
	
	/**
	 * Constructor de la clase TerrainRenderer
	 * @param shader variable que tomará el programa shader
	 * @param projectionMatrix variable para la matrix de proyección de la cámara
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.conectarUnidadesdTextura();
		shader.stop();
	}
	
	/**
	 * Método que toma una lista de todos los terrenos que queremos renderizar 
	 * @param terrains lista de terrenos
	 */
    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
         // realizar el render final en este espacio
         			// dibujamos los elementos que cargamos en el modelo 3D
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }
    
	/**
	 * Método que prepara el uso del modelo y activa los datos que existen en el VAO del terreno
	 * @param model modelo del terreno
	 */
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
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
		
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);

	}
	
	/**
	 * Método para mezclar las texturas del terreno, se utilizan 5 texturas diferentes, incluyendo el blendMap.png
	 * @param terrain Terreno al cual se le aplica el multiTexture
	 */
	public void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		// activamos el uso de texturas en el videojuego
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// vinculamos la textura con una llamada y su Identificación
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	/**
	 * Método que desenlaza la textura del terreno
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
	 * Método que prepara la instancia o las entidades de cada uno de los terrenos texturizados
	 * @param entidad variable de hace referencia a un terreno que queremos renderizar en el escenario 
	 */
	private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
