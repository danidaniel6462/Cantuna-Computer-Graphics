/**
 * 
 */
package futuroingeniero.toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.entities.Camara;

/**
 * @author Daniel Loza
 *
 * Clase para realizar los c�lculos dentro de nuestro motor de Juego, es una clase fundamental ya que aqui es donde realizaremos diversos c�lculos para controlar el videojuego
 */
public class Maths {
	
	/**
	 * M�todo para realizar transformaciones en los objetos, con ayuda de este m�todo se puede crear traslaciones, rotaciones y escalado uniforme
	 * @param translation variable que recibe un vector3 para realizar c�lculos para trasladar el objeto 
	 * @param rx variable para controlar la rotaci�n en X
	 * @param ry variable para controlar la rotaci�n en Y
	 * @param rz variable para controlar la rotaci�n en Z
	 * @param scale variable para controlar la escala del objeto
	 * @return devuelve una matriz lista para poder aplicacar a los objetos 3D
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		// reestablece la matriz para realizar operaciones entre matrices
		matrix.setIdentity();
		/**
		 * m�todo que realiza los c�lculos para la translaci�n
		 * @param translation recibe un vector 3 para realizar el c�lculo
		 * @param matrix es la matriz que vamos a calcular
		 * @param matrix es la matriz que nos devueleve como resultado y lo guarda en la variable matrix
		 */
		Matrix4f.translate(translation, matrix, matrix);
		// este m�todo realiza una rotaci�n, recibe el �ngulo en el que va a rotar, el vector para realizar el c�clulos sobre la matriz
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		// m�todo para escalar el objeto uniformemente
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		// se devuelve la matrix con las transformaciones especificadas en el ingreso del m�todo
		return matrix;
	}
	
	/**
	 * M�todo que se usa para movernos los objetos en la direcci�n opuesta de la c�mara para simular el movieminto de la c�mara
	 * @param camera C�mara para movernos en el espacio 3D
	 * @return retornamos una nueva matriz de vista, despu�s de realizar los c�lculos y movernos en el espacio 3D
	 */
    public static Matrix4f createViewMatrix(Camara camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPaneo()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getPicado()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
    
}
