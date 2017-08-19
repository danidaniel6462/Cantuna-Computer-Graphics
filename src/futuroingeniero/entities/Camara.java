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

	private float distanciaDesdeJugador = 50;	// distancia entre el jugador y la c�mara
	private float anguloAlrededorJugador = 0;	// �ngulo alrededor del jugador
	
	/**
	 * Variables nombradas seg�n la cinematogr�fica
	 * @param position variable que incializa la posici�n de la c�mara en el mundo
	 * @param picado variable para la rotaci�n en Y
	 * @param paneo variable para la rotaci�n en X
	 * @param roll variable para la rotaci�n en Z
	 */
	private Vector3f posicion = new Vector3f(0, 0, 0);
	private float picado = 20f;  // pitch
	private float paneo; // yaw
	private float roll; // roll
	
	private Player player;
	
	// private float speed;
	
	/**
	 * Constructor de la clase C�mara
	 * @param player utiliza un objeto player ya que se utilizan m�todos del player en esta clase
	 */
	public Camara(Player player)
	{
		this.player = player;
		//	this.speed = 0.5f;
	}
	
	/**
	 * M�todo para el movimiento de la c�mara con respecto al jugador
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
	 * M�todo para alcular la posici�n de la c�mara
	 * @param horizontalDistancia distancia horizontal calculada
	 * @param verticalDistancia distancia vertical calculada
	 */
	public void calcularPosicionCamara(float horizontalDistancia, float verticalDistancia) {
		float tetha = player.getRotY() + anguloAlrededorJugador;
		float offsetX = (float) (horizontalDistancia * Math.sin(Math.toRadians(tetha)));
		float offsetZ = (float) (horizontalDistancia * Math.cos(Math.toRadians(tetha)));
		// restamos los desplazamiento en X y Z porque la c�mara se desplaza en direcci�n contraria del jugador
		// para posteriormente oider rotar alrededor del personaje de forma correcta
		// adem�s el eje de coordenadas con vista superior apunta el eje de las X a la derecha que es positivo
		// y el eje de la Z apunta hacia abajo como positivo, y como la c�mara est� tras el personaje es necesario realizar al operaci�n 
		// contraria para poder realizar el c�culo correcto
		posicion.x = player.getPosition().x - offsetX; 
		posicion.y = 5 + player.getPosition().y + verticalDistancia;
		posicion.z = player.getPosition().z - offsetZ;
	}
	
	/**
	 * M�todo para calcular la distancia horizontal de la c�mara al jugador
	 * @return devuelve el valor de la distancia entre el jugador y la c�mara
	 */
	private float calcularDistanciaHorizontal() {
		float distanciaHorizontal = (float) (distanciaDesdeJugador * Math.cos(Math.toRadians(picado)));
		if(distanciaHorizontal < 0)
			distanciaHorizontal = 0;
		return distanciaHorizontal;
	}
	
	/**
	 * M�todo para calcular la distancia vertical de la c�mara al terreno
	 * @return devuelve el valor de la distancia entre el jugador y el terreno
	 */
	private float calcularDistanciaVertical() {
		float distanciaVertical = (float) (distanciaDesdeJugador * Math.sin(Math.toRadians(picado)));
		if(distanciaVertical < 0)
			distanciaVertical = 0;
		return distanciaVertical;
	}

	/**
	 * M�todo para calcular el zoom de la c�mara hacia al jugador
	 */
	private void calcularZoom() {
		// tomamos el dato del scroll del mouse para acercarnos al jugador
		// multiplicamos el dato del mouse por 0.1 ya que es una antidad grande 
		// y si se toma ese valor el zoom ser�a muy r�pido.
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		// alejamos la c�mara
		distanciaDesdeJugador -= zoomLevel;
		
		if(distanciaDesdeJugador < 10) {
			distanciaDesdeJugador = 10;
		}
	}
	
	/**
	 * M�todo para calcular el �ngulo de picado de la c�mara
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
	 * M�todo para calcular el �ngulo de rotaci�n alrededor del jugador
	 */
	private void calcularAnguloAlrededorJugador() {
			float anguloCambio = Mouse.getDX() * 0.3f;
			anguloAlrededorJugador -= anguloCambio;
	}
}