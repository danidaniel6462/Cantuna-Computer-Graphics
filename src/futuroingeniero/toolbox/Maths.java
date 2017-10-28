/**
 * 
 */
package futuroingeniero.toolbox;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;
import futuroingeniero.entities.Entity;
import futuroingeniero.guis.GuiTexture;

/**
 * @author Daniel Loza
 *
 * Clase para realizar los cálculos dentro de nuestro motor de Juego, es una clase fundamental ya que aqui es donde realizaremos diversos cálculos para controlar el videojuego
 */
public class Maths {
	
	//------------------ Métodos auxiliares para colisiones 2D------------------//
	/**
	 * Distancia entre dos objetos 2D
	 * @param posicion0 posición del objeto 0 en espacio 2D
	 * @param posicion1 posición del objeto 1 en espacio 2D
	 * @return devuleve la distancia entre los dos puntos 
	 */
	public static float distancia2D(Vector2f posicion1, Vector2f posicion2){
		float dx = posicion2.x - posicion1.x;
		float dy = posicion2.y - posicion1.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Distancia entre dos puntos 2D
	 * @param x0 posición en x del 1er punto
	 * @param y0 posición en y del 1er punto
	 * @param x1 posición en x del 2do punto
	 * @param y1 posición en y del 2do punto
	 * @return devuleve la distancia entre los dos puntos 
	 */
	public static float distanciaXY2D(float x0, float y0, float x1, float y1){
		float dx = x1 - x0;
		float dy = y1 - y0;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Método para saber si un punto está dentro de un rango definido o no
	 * @param valor valor que se calcula si está en el rango 
	 * @param min mínimo valor del rango
	 * @param max máximo valor del rango
	 * @return devuelve un valor lógico si está o no en el rango definido
	 */
	public static boolean enRango(float valor, float min, float max){
		if(Math.min(min, max) <= valor && valor <= Math.max(min,max)){
			return true;	
		} else {
			return false;
		}
	}
	
	/**
	 * Método para saber si un rango se iterseca con otro
	 * @param min0 valor mínimo 0
	 * @param max0 valor máximo 0
	 * @param min1 valor mínimo 1
	 * @param max1 valor máximo 1
	 * @return devuelve un valor lógico si existe un rango que se interseca
	 */
	public static boolean rangoInterseccion(float min0, float max0, float min1, float max1){
		if(Math.min(min1, max1) <= Math.max(min0, max0) 
				&& Math.min(min0, max0) <= Math.max(min1, max1)){
			return true;
		} else {
			return false;
		}
	}	
	
	//------------------ Métodos para colisiones 2D con un objetos GUI------------------//
	/**
	 * Método boundingCircle, sirve para encontrar la colisión entre dos círculos
	 * @param gui1 objeto1 2D
	 * @param gui2 objeto2 2D
	 * @return devuelve un valor lógico si existe o no colisión entre los círculos
	 */
	public static boolean circuloColision(GuiTexture gui1, GuiTexture gui2){
		if(distancia2D(gui1.getPosition(), gui2.getPosition()) <= gui1.getRadio() + gui2.getRadio()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método boundingCirclePoint, sirve para encontrar la colisión entre un punto en el espacio 2D y una esfera
	 * @param punto Vector2 del punto en la pantalla
	 * @param gui objeto envuelto en un círculo en la pantalla
	 * @return devuleve un valor lógico si existe o no colisión
	 */
	public static boolean circuloPuntoColision(Vector2f punto2D, GuiTexture gui){
		if(distanciaXY2D(punto2D.x, punto2D.y, gui.getPosition().x, gui.getPosition().y)
				<= gui.getRadio()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método boundingRectanglePoint, sirve para calcular colisión entre un rectángulo y un punto 2D
	 * @param punto2D coordenadas 2D del punto
	 * @param guiRect rectángulo con el que colisiona el punto
	 * @return devuelve valor lógico si colisiona o no el punto con el rectángulo
	 */
	public static boolean puntoEnRectangulo(Vector2f punto2D, GuiTexture guiRect){
		if(enRango(punto2D.x, guiRect.getPosition().x - guiRect.getScale().x, guiRect.getPosition().x + guiRect.getScale().x) &&
				enRango(punto2D.y, guiRect.getPosition().y - guiRect.getScale().y, guiRect.getPosition().y + guiRect.getScale().y)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método rectanguloInterseccion, sirve para encontrar la colisión entre dos rectángulos
	 * @param gui1 objeto1 2D
	 * @param gui2 objeto2 2D
	 * @return devuelve un valor lógico si existe o no colisión entre los rectángulos
	 */
	public static boolean rectaguloInterseccion(GuiTexture gui1, GuiTexture gui2){
		if(rangoInterseccion(gui1.getPosition().x - gui1.getScale().x, gui1.getPosition().x + gui1.getScale().x,
				gui2.getPosition().x - gui2.getScale().x, gui2.getPosition().x + gui2.getScale().x) &&
				rangoInterseccion(gui1.getPosition().y - gui1.getScale().y, gui1.getPosition().y + gui1.getScale().y,
						gui2.getPosition().y + gui2.getScale().y, gui2.getPosition().y - gui2.getScale().y) ){
			return true;
		} else {
			return false;
		}
	}
	
	//------------------ Métodos para colisiones 2D con una lista de GUIs------------------//
	
	/**
	 * Método para mover objetos en una lista de GUIs, sirve para encontrar la colisión entre un punto en el espacio 2D y una esfera
	 * @param punto Vector2 del punto en la pantalla
	 * @param listGui lista de objetos con los que colisiona el punto
	 * @return devuleve un Gui
	 */
	public static GuiTexture getElementoGuiCirculo(Vector2f punto2D, List<GuiTexture> listGui){
		GuiTexture miGui = null;
		for(int i = 0; i < listGui.size(); i++){
			if(circuloPuntoColision(punto2D, listGui.get(i))){
				miGui = listGui.get(i);
			}
		}
		return miGui; 
	}
	
	/**
	 * Método para mover objetos en una lista de GUIs, sirve para encontrar la colisión entre un punto en el espacio 2D y una esfera
	 * @param punto Vector2 del punto en la pantalla
	 * @param listGui lista de objetos con los que colisiona el punto
	 * @return devuleve un Gui
	 */
	public static GuiTexture getElementoGuiRectangulo(Vector2f punto2D, List<GuiTexture> listGui){
		GuiTexture miGui = null;
		for(int i = 0; i < listGui.size(); i++){
			if(puntoEnRectangulo(punto2D, listGui.get(i))){
				miGui = listGui.get(i);
			}
		}
		return miGui; 
	}
	
	// ------------------ Métodos para colisiones 3D ------------------//
	
	/**
	 * Distancia entre dos puntos 3D
	 * @param x0 posición en x del 1er punto
	 * @param y0 posición en y del 1er punto
	 * @param z0 posición en z del 1er punto
	 * @param x1 posición en x del 2do punto
	 * @param y1 posición en y del 2do punto
	 * @param z1 posición en z del 2do punto
	 * @return devuleve la distancia entre los dos puntos en 3D 
	 */
	public static float distanciaXYZ3D(float x0, float y0, float z0, float x1, float y1, float z1){
		float dx = x1 - x0;
		float dy = y1 - y0;
		float dz = z1 - z0;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	/**
	 * Distancia entre dos objetos 3D
	 * @param posicion0 posición del objeto 0 en espacio 3D
	 * @param posicion1 posición del objeto 1 en espacio 3D
	 * @return devuleve la distancia entre los dos puntos 
	 */
	public static float distancia3D(Vector3f posicion1, Vector3f posicion2){
		float dx = posicion2.x - posicion1.x;
		float dy = posicion2.y - posicion1.y;
		float dz = posicion2.z - posicion1.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Método boundingSphere, sirve para encontrar la colisión entre dos círculos
	 * @param gui1 objeto1 3D
	 * @param gui2 objeto2 3D
	 * @return devuelve un valor lógico si existe o no colisión entre los círculos
	 */
	public static boolean esferaColision(Entity item1, Entity item2){
		if(distancia3D(item1.getPosition(), item2.getPosition()) <= item1.getRadio() + item2.getRadio()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método boundingSpherePoint, sirve para encontrar la colisión entre un punto en el espacio 3D y una esfera
	 * @param punto Vector3 del punto en la pantalla
	 * @param gui objeto envuelto en un círculo en la pantalla
	 * @return devuleve un valor lógico si existe o no colisión
	 */
	public static boolean esferaPuntoColision(Vector3f punto3D, Entity item){
		if(distanciaXYZ3D(punto3D.x, punto3D.y, punto3D.z, item.getPosition().x, item.getPosition().y, item.getPosition().z)
				<= item.getRadio()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método para mover objetos en una lista de entidades, sirve para encontrar la colisión entre un punto en el espacio 3D y una esfera
	 * @param punto Vector3 del punto en el terreno
	 * @param items lista de objetos con los que colisiona el punto
	 * @return devuleve un item Entity
	 */
	public static Entity getElementoEsfera(Vector3f punto3D, List<Entity> items){
		Entity miItem = null;
		for(int i = 0; i < items.size(); i++){
			if(esferaPuntoColision(punto3D, items.get(i))){
				miItem = items.get(i);
			}
		}
		return miItem; 
	}
	
	//---------------------------------------------------------------------------------------------------
	
	/*
	/**
	 * Método para saber si un rango se iterseca con otro
	 * @param min0 valor mínimo 0
	 * @param max0 valor máximo 0
	 * @param min1 valor mínimo 1
	 * @param max1 valor máximo 1
	 * @return devuelve un valor lógico si existe un rango que se interseca
	 *
	public static boolean rangoInterseccion(float min0, float max0, float min1, float max1){
		if(Math.min(min1, max1) <= Math.max(min0, max0) 
				&& Math.min(min0, max0) <= Math.max(min1, max1)){
			return true;
		} else {
			return false;
		}
	}	
	
	*/
	
	
	//------------------ Métodos para objetos 3D ------------------//
	/**
	 * Método para crear una Matriz de Transformación para escalar y trasladar los Gui del Juego 
	 * @param translation traslación del GUI
	 * @param scale tamaño del GUI
	 * @return devuelve una matriz con las transformaciones lista para ser renderizado el GUI
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		// creamos una matriz nueva
		Matrix4f matrix = new Matrix4f();
		// cargamos la matriz identidad
		matrix.setIdentity();
		// realiza la traslación
		Matrix4f.translate(translation, matrix, matrix);
		// realiza el escalado para la dimensión del GUI
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	/**
	 * Método matemático para encontrar el baricentro
	 * Este método obtiene tres valores que forman un triángulo y la posición del jugador
	 * @param p1 punto 1 del triángulo
	 * @param p2 punto 2 del triángulo
	 * @param p3 punto 3 del triángulo
	 * @param pos posición del jugador en dos coordenadas 
	 * @return devuelve la altura del triángulo y la posición del jugador
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	/**
	 * Método para realizar transformaciones en los objetos, con ayuda de este método se puede crear traslaciones, rotaciones y escalado uniforme
	 * @param translation variable que recibe un vector3 para realizar cálculos para trasladar el objeto 
	 * @param rx variable para controlar la rotación en X
	 * @param ry variable para controlar la rotación en Y
	 * @param rz variable para controlar la rotación en Z
	 * @param scale variable para controlar la escala del objeto
	 * @return devuelve una matriz lista para poder aplicar a los objetos 3D
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		// reestablece la matriz para realizar operaciones entre matrices
		matrix.setIdentity();
		/**
		 * método que realiza los cálculos para la translación
		 * @param translation recibe un vector 3 para realizar el cálculo
		 * @param matrix es la matriz que vamos a calcular
		 * @param matrix es la matriz que nos devueleve como resultado y lo guarda en la variable matrix
		 */
		Matrix4f.translate(translation, matrix, matrix);
		// este método realiza una rotación, recibe el ángulo en el que va a rotar, el vector para realizar el cáclulos sobre la matriz
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		// método para escalar el objeto uniformemente
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		// se devuelve la matrix con las transformaciones especificadas en el ingreso del método
		return matrix;
	}
	
	/**
	 * Método que se usa para movernos los objetos en la dirección opuesta de la cámara para simular el movieminto de la cámara
	 * @param camera Cámara para movernos en el espacio 3D
	 * @return retornamos una nueva matriz de vista, después de realizar los cálculos y movernos en el espacio 3D
	 */
    public static Matrix4f createViewMatrix(Camara camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPicado()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getPaneo()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
    
}
