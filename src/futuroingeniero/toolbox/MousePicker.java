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

	// valor de veces que el método de recursión se debería de ejecutar
	private static final int RECURSION_COUNT = 200;
	// tamaño del rayo
	private static final float RAY_RANGE = 600;

	// vector rayo
	private Vector3f rayoActual;

	private Matrix4f proyeccionMatriz;
	private Matrix4f puntoVistaMatriz;
	private Camara camara;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;

	/**
	 * Constructor de la Clase MousePicker
	 * @param camara Cámara en la cual interactúa el mouse
	 * @param proyeccionMatriz Matriz de transformación que recibe conversiones
	 * @param terrain
	 */
	
	public MousePicker(Camara camara, Matrix4f proyeccionMatriz, Terrain terrain) {
		this.camara = camara;
		this.proyeccionMatriz = proyeccionMatriz;
		this.puntoVistaMatriz = Maths.createViewMatrix(camara);
		this.terrain = terrain;
	}
	
	/**
	 * Método que se actualiza una vez por frame
	 */
	public void update3D() {
		// creamos una matriz con el punto de Vista de la cámara
		puntoVistaMatriz = Maths.createViewMatrix(camara);
		// obtenemos el cálculo del vector rayo
		rayoActual = calcularMouseRayo();
		if (intersectionInRange(0, RAY_RANGE, rayoActual)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, rayoActual);
		} else {
			currentTerrainPoint = null;
		}
	}
	
	//===========================================================//
	//===========================================================//
	private Vector2f rayo2D;
	
	public void update2D(){
		rayo2D = mousePicking2D();
	}
	
	public Vector2f mousePicking2D(){
		// obtenemos las coordenadas 2D del mouse sobre la pantalla
		// llamado también espacio de Visualización
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		// cambiamos las coordenadas 2D al sistema de coordenadas OpenGL
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		return normalizedCoords;
	}
	
	public Vector2f getRayo2D(){
		return rayo2D;
	}
	
	//===========================================================//
	//===========================================================//
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
	
	
	//---------------------------------------------------------------------//
	
	/**
	 * Método para obtener la posición del rayo en el espacio 3D
	 * @param ray vector rayo
	 * @param distance distancia desde la cámara hasta el punto final del rayo
	 * @return devuelve un vector nuevo con la suma del vector inicial mas el final
	 */
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camara.getPosition();
		Vector3f pInicio = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		// retorna la suma (adición) de los vectores de ziquierda a derecha
		return Vector3f.add(pInicio, scaledRay, null);
	}
	
	/**
	 * Método para búsqueda binaria del objeto que se desea arrastrar
	 * @param count variable para este m{etodo sobre recursividad
	 * @param pInicio punto inicial del rayo
	 * @param pFinal punto final del rayo
	 * @param ray rayo con dirección
	 * @return devuelve un punto en el espacio 3D
	 */
	private Vector3f binarySearch(int count, float pInicio, float pFinal, Vector3f ray) {
		float mitad = pInicio + ((pFinal - pInicio) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, mitad);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(pInicio, mitad, ray)) {
			return binarySearch(count + 1, pInicio, mitad, ray);
		} else {
			return binarySearch(count + 1, mitad, pFinal, ray);
		}
	}

	/**
	 * Método que busca la intersección en un rango
	 * Método lógico para saber si interseca el rayo con el terreno
	 * @param pInicio punto inicial del rayo
	 * @param pFinal punto final del rayo
	 * @param ray vector dirección del rayo
	 * @return devuelve un valor lógico para determinar si el punto está sobre o bajo el terreno 
	 */
	private boolean intersectionInRange(float pInicio, float pFinal, Vector3f ray) {
		// posición y dirección del rayo inicial
		Vector3f inicioPoint = getPointOnRay(ray, pInicio);
		// posición y dirección del rayo final
		Vector3f endPoint = getPointOnRay(ray, pFinal);
		// comprobación si el punto está sobre el terreno o bajo el terreno
		if (!isBajoTierra(inicioPoint) && isBajoTierra(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Método lógico para saber si el punto de intersección está sobre o bajo el terreno
	 * @param testPoint punto de intersección
	 * @return devuelve un valor lógico de si está el punto sobre o bajo el terreno
	 */
	private boolean isBajoTierra(Vector3f testPoint) {
		// obtengo el las coordenadas del punto en el terreno
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		// obtenemos la altura del terreno en el punto definido
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
		}
		// comprobar si el punto está sobre el terreno
		// si el punto es menor que cero quiere decir que está por debajo del terrno
		if (testPoint.y < height) {
			return true;
			// caso contrario está sobre el terreno
		} else {
			return false;
		}
	}

	/**
	 * Método para obtener el terreno en el que está el rayo
	 * @param worldX posición en x del terreno
	 * @param worldZ posición en z del terreno
	 * @return devuleve el terreno
	 */
	private Terrain getTerrain(float worldX, float worldZ) {
		return terrain;
	}

	/**
	 * Método para obtener el punto sobre el terreno actual
	 * @return devuelve un punto 3D en el mundo 
	 */
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
}
