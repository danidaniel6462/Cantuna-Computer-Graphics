/**
 * 
 */
package futuroingeniero.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import futuroingeniero.models.TexturedModel;
import futuroingeniero.renderEngine.DisplayManager;
import futuroingeniero.terrains.Terrain;

import static futuroingeniero.renderEngine.GlobalConstants.*;

/**
 * @author Daniel Loza
 *
 * <h1>Clase Player</h1>
 * Clase para el movimiento del jugador
 */
public class Player extends Entity{
	
	//private static final float TERRAIN_DEPTH = -800; // profundidad del terreno creado para poder colisionar con el borde
	//private static final float TERRAIN_WIDE = 800; // ancho del terreno creado para poder colisionar con el borde
	
	private float currentSpeed = 0; // velocidad actual del jugador incializado en 0 ya que no se mueve
	private float currenTurnSpeed = 0; // velocidad de la rotación del jugador inicializado en 0 ya que no se mueve
	private float upwardsSpeed = 0; // velocidad del movimieto hacia arriba del jugador inicializado en 0 ya que no se mueve y posicionado en 0 en el eje Y
	
	private boolean isInAir = false;
	
	/**
	 * Constructor de la clase Player
	 * Este método es igual al de la clase Entidad ya que se extiende de la Clase Entity
	 * @param model modelo para renderizar
	 * @param nombre nombre de la entidad o item que se renderiza
	 * @param position vecto3 que incializa la posición del modelo
	 * @param rotX variable que incializa la el ángulo de rotación en X
	 * @param rotY variable que incializa la el ángulo de rotación en Y
	 * @param rotZ variable que incializa la el ángulo de rotación en Z
	 * @param scale variable que inicializa 
	 */
	public Player(TexturedModel model, String nombre, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, nombre, position, rotX, rotY, rotZ, scale);
	}
	
	/**
	 * Método para el movimiento del jugador
	 * En este método se realiza el cálculo de la nueva posición del jugador en el espacio
	 * con ayuda de funciones trigonométricas
	 * @param terreno valor que determina en que terreno se ecuentra el jugador
	 * @param entidad item
	 */
	public void move(Terrain terreno, Entity entidad) {
		// obtenemos los eventos del teclado
		checkInput();
		// enviamos los datos para incrementar la rotación del personaje
		super.increaseRotation(0, currenTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		// variable para calcular la distancia que recorrerá el personaje
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		// variable para calcular el delta X en el triángulo de movimiento
		// la fórmula sería 
		// dx = distancia * sin(rotY)
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		// variable para calcular el delta Z en el triángulo de movimiento
		// dz = distancia * sin(rotY)
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		// enviamos la nueva posición del jugador 
		super.increasePosition(dx, 0, dz);
		// cálculo de la fuerza del jugador para retornar al terreno una vez que salta
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		// enviamos la posición en Y al momento de saltar
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		// obtenemos la altura del terreno con la posición del jugador
		// con este valor podemos realizar la colisión del jugador con el terreno  
		float terrainHeight = terreno.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

		// comprobamos que el personaje colisione con el terreno
		if(super.getPosition().y < terrainHeight) { 
			upwardsSpeed = 0;
			// como el personaje está en el suelo está listo para pegar otro salto por eso la variable booleana es falsa, xq no está en el aire :3
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
		
		/*
		// colisión con los bordes del terreno
		if(super.getPosition().z > TERRAIN_DEPTH - TERRAIN_DEPTH) { 
			super.setPosition(new Vector3f(super.getPosition().x, super.getPosition().y, 0));
		} else if(super.getPosition().z < TERRAIN_DEPTH) {
			super.setPosition(new Vector3f(super.getPosition().x, super.getPosition().y, TERRAIN_DEPTH));
		} 
		
		if(super.getPosition().x > TERRAIN_WIDE) { 
			super.setPosition(new Vector3f(TERRAIN_WIDE, super.getPosition().y, super.getPosition().z));
		} else if(super.getPosition().x < -TERRAIN_WIDE) {
			super.setPosition(new Vector3f(-TERRAIN_WIDE, super.getPosition().y, super.getPosition().z));
		} 
		*/
		
		
	}
	
	/**
	 * Método que salte el jugador
	 * 
	 */
	public void jump() {
		// si es verdad que está en el aire, hacer
		if(!isInAir) {
			// pega el salto
			this.upwardsSpeed = JUMP_POWER;
			// la variable se hace verdadera para que no vuelva a saltar en el aire
			isInAir = true;
		}
	}

	/**
	 * Método para obtener los eventos del teclado para el jugador
	 * a futuro se cambiará el metodo de jugar ya que será con captura de movimiento
	 * a través de una cámara para poder mover al personajes
	 * Claro, si todo sale bien jeje :3 
	 */
	private void checkInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = WALK_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -WALK_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currenTurnSpeed = -TURN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currenTurnSpeed = TURN_SPEED;
		} else {
			this.currenTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.currentSpeed = WALK_SPEED + RUN_SPEED;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.currentSpeed = -WALK_SPEED - RUN_SPEED;
			} else {
				this.currentSpeed = 0;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
}