/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Daniel Loza
 *
 */
public class Camara {

	private float distanciaDesdeJugador = 50;	// distancia entre el jugador y la cámara
	private float anguloAlrededorJugador = 0;	// ángulo alrededor del jugador
	
	/**
	 * Variables nombradas según la cinematográfica
	 * @param position variable que incializa la posición de la cámara en el mundo
	 * @param picado variable para la rotación en Y
	 * @param paneo variable para la rotación en X
	 * @param roll variable para la rotación en Z
	 */
	private Vector3f posicion = new Vector3f(0, 0, 0);
	private float picado = 20f;  // pitch
	private float paneo; // yaw
	private float roll; // roll
	
	private Player player;
	
	// private float speed;
	
	/**
	 * Constructor de la clase Cámara
	 * @param player utiliza un objeto player ya que se utilizan métodos del player en esta clase
	 */
	public Camara(Player player)
	{
		this.player = player;
		//	this.speed = 0.5f;
	}
	
	/**
	 * Método para el movimiento de la cámara con respecto al jugador
	 * 3era Persona
	 */
	public void move() {
		calcularZoom();
		if(Mouse.isButtonDown(1)){
			if(!Mouse.isGrabbed())
				Mouse.setGrabbed(true);
			calcularAnguloAlrededorJugador();
			calcularPicado();
			if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)){
				player.increaseRotation(0, anguloAlrededorJugador, 0);
				anguloAlrededorJugador = 0;
			}
		}else if(!Mouse.isButtonDown(1)){
			if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S)){
				anguloAlrededorJugador /= 1.2f;
				if(-0.5f <= anguloAlrededorJugador && anguloAlrededorJugador <= 0.5f)
					anguloAlrededorJugador = 0;
			}
		}
		if(!Mouse.isButtonDown(1) && Mouse.isGrabbed())
			Mouse.setGrabbed(false);

		float horizontalDistancia = calcularDistanciaHorizontal();
		float verticalDistancia = calcularDistanciaVertical();
		calcularPosicionCamara(horizontalDistancia, verticalDistancia);
		this.paneo = 180 - (player.getRotY() + anguloAlrededorJugador);
	}
	
	/**
	 * @return the posicion
	 */
	public Vector3f getPosition() {
		return posicion;
	}

	/**
	 * @return the picado
	 */
	public float getPicado() {
		return picado;
	}

	/**
	 * @return the paneo
	 */
	public float getPaneo() {
		return paneo;
	}

	/**
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}
	
	
	/**
	 * Método para alcular la posición de la cámara
	 * @param horizontalDistancia distancia horizontal calculada
	 * @param verticalDistancia distancia vertical calculada
	 */
	public void calcularPosicionCamara(float horizontalDistancia, float verticalDistancia) {
		float tetha = player.getRotY() + anguloAlrededorJugador;
		float offsetX = (float) (horizontalDistancia * Math.sin(Math.toRadians(tetha)));
		float offsetZ = (float) (horizontalDistancia * Math.cos(Math.toRadians(tetha)));
		// restamos los desplazamiento en X y Z porque la cámara se desplaza en dirección contraria del jugador
		// para posteriormente oider rotar alrededor del personaje de forma correcta
		// además el eje de coordenadas con vista superior apunta el eje de las X a la derecha que es positivo
		// y el eje de la Z apunta hacia abajo como positivo, y como la cámara está tras el personaje es necesario realizar al operación 
		// contraria para poder realizar el cáculo correcto
		posicion.x = player.getPosition().x - offsetX; 
		posicion.y = 5 + player.getPosition().y + verticalDistancia;
		posicion.z = player.getPosition().z - offsetZ;
	}
	
	/**
	 * Método para calcular la distancia horizontal de la cámara al jugador
	 * @return devuelve el valor de la distancia entre el jugador y la cámara
	 */
	private float calcularDistanciaHorizontal() {
		float distanciaHorizontal = (float) (distanciaDesdeJugador * Math.cos(Math.toRadians(picado)));
		if(distanciaHorizontal < 0)
			distanciaHorizontal = 0;
		return distanciaHorizontal;
	}
	
	/**
	 * Método para calcular la distancia vertical de la cámara al terreno
	 * @return devuelve el valor de la distancia entre el jugador y el terreno
	 */
	private float calcularDistanciaVertical() {
		float distanciaVertical = (float) (distanciaDesdeJugador * Math.sin(Math.toRadians(picado)));
		if(distanciaVertical < 0)
			distanciaVertical = 0;
		return distanciaVertical;
	}

	/**
	 * Método para calcular el zoom de la cámara hacia al jugador
	 */
	private void calcularZoom() {
		// tomamos el dato del scroll del mouse para acercarnos al jugador
		// multiplicamos el dato del mouse por 0.1 ya que es una antidad grande 
		// y si se toma ese valor el zoom sería muy rápido.
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		// alejamos la cámara
		distanciaDesdeJugador -= zoomLevel;
		
		if(distanciaDesdeJugador < 10) {
			distanciaDesdeJugador = 10;
		}
	}
	
	/**
	 * Método para calcular el ángulo de picado de la cámara
	 */
	private void calcularPicado() {
			float picadoCambio = Mouse.getDY() * 0.1f;
			picado -= picadoCambio;
			
			if(picado < 0)
				picado = 0;
			else if(picado > 90)
				picado = 90;
	}
	
	/**
	 * Método para calcular el ángulo de rotación alrededor del jugador
	 */
	private void calcularAnguloAlrededorJugador() {
			float anguloCambio = Mouse.getDX() * 0.3f;
			anguloAlrededorJugador -= anguloCambio;
	}
}