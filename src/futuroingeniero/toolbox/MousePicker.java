/**
 * 
 */
package futuroingeniero.toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import futuroingeniero.entities.Camara;
import futuroingeniero.terrains.Terrain;

/**
 * @author Daniel Loza
 *
 */
public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private Vector3f rayoActual;

	private Matrix4f proyeccionMatriz;
	private Matrix4f puntoVistaMatriz;
	private Camara camara;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;

	/**
	 * Constructor de la Clase MousePicker
	 * @param camara Camara en la cual interactua el mouse
	 * @param proyeccionMatriz Matriz de transformación que recibe conversiones
	 */
	public MousePicker(Camara camara, Matrix4f proyeccionMatriz, Terrain terrain) {
		this.camara = camara;
		this.proyeccionMatriz = proyeccionMatriz;
		this.puntoVistaMatriz = Maths.createViewMatrix(camara);
		this.terrain = terrain;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
	
	/**
	 * Método que se actualiza una vez por frame
	 */
	public void update() {
		// creamos una matriz con el punto de Vista de la cámara
		puntoVistaMatriz = Maths.createViewMatrix(camara);
		rayoActual = calcularMouseRayo();
		if (intersectionInRange(0, RAY_RANGE, rayoActual)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, rayoActual);
		} else {
			currentTerrainPoint = null;
		}
	}
	
	/**
	 * Método que realiza la transformación de coordenadas 2D a 3D
	 * @return devuleve un vector 3D de la posición del mouse en el Mundo 
	 */
	public Vector3f calcularMouseRayo() {
		// obtenemos las coordenadas 2D del mouse sobre la pantalla
		// llamado también espacio de Visualización
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		// cambiamos las coordenadas 2D al sistema de coordenadas OpenGL
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		// convertimos las coordenadas de OpenGL en un rayo, o sea con dirección z = -1
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		// cambiamos las coordenadas del espacio Clip a coordenadas para el ojo humano
		Vector4f ojoCoords = toOjoCoords(clipCoords);
		// calculamos las coordenadas en el espacio 3D del juego
		Vector3f rayoWorld = toWorldCoords(ojoCoords);
		return rayoWorld;
	}
	
	/**
	 * Método para convertir las coordenadas del ojo humano a coordenadas del Mundo 3D
	 * @param ojoCoords coordenadas del ojo humano para convertirlos a coordenadas del mundo 3D
	 * @return devuelve el vector rayo en el mundo 3D del videojuego 
	 */
	private Vector3f toWorldCoords(Vector4f ojoCoords) {
		Matrix4f invertidaVista = Matrix4f.invert(puntoVistaMatriz, null);
		Vector4f rayoWorld = Matrix4f.transform(invertidaVista, ojoCoords, null);
		Vector3f rayoMouse = new Vector3f(rayoWorld.x, rayoWorld.y, rayoWorld.z);
		rayoMouse.normalise();
		return rayoMouse;
	}
	
	/**
	 * Método para convertir coordendas del espacio Homogeneo Clip a coordenadas perseptibles para el ojo humano
	 * @param clipCoords coordenadas del espacio Clip
	 * @return devuelve coordenadas para el Ojo humano
	 */
	private Vector4f toOjoCoords(Vector4f clipCoords) {
		// se escribieron valores nulls porque es para almacenar el resultado en otra matriz,
		// pero en este caso ya se obtiene el resutaldo en una variable definida
		Matrix4f invertidaProyeccion = Matrix4f.invert(proyeccionMatriz, null);
		// multiplica las dos matrices: La Matriz Invertida con la del espacio Clip
		Vector4f ojoCoords = Matrix4f.transform(invertidaProyeccion, clipCoords, null);
		return new Vector4f(ojoCoords.x, ojoCoords.y, -1f, 0f);
	}
	
	/**
	 * Método para convertir las coordenadas del Mouse en pixeles para normalizar las coordenadas para OpenGL
	 * La pantalla en OpenGL está desde (-1, 1) en X y (-1, 1) en Y 
	 * @param mouseX coordenada en X del Ratón en píxeles
	 * @param mouseY coordenada en Y del Ratón en píxeles
	 * @return devuelve un vector en 2D con las coordenadas de OpenGL
	 */
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1f;
		float y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	/**
	 * @return el rayoActual
	 */
	public Vector3f getRayoActual() {
		return rayoActual;
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camara.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	private Terrain getTerrain(float worldX, float worldZ) {
		return terrain;
	}


}
