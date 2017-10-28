/**
 * 
 */
package futuroingeniero.terrains;

import static futuroingeniero.utils.GlobalConstants.SIZE_TERRAIN;

import java.util.ArrayList;
import java.util.List;

import futuroingeniero.entities.Player; 

/**
 * @author Daniel Loza
 *
 */
public class ManagerTerrain {

	//------------------------------Attributes-------------------------------
	/**
	 * @param terrainList Lista de terrenos
	 * @param actualTerreno Terreno actual en el que el jugador estará ubicado para calcular las colisiones con el mismo
	 * @param actualPosicionPlayer Vector con posiciones actuales del player para actualización
	 * @param Player Jugador actual
	 */
	private List<Terrain> terrainList;
	private Terrain actualTerrain;
	private float[] actualPositionPlayer;
	private Player player;
	
	//------------------------------Constructor-----------------------------
	/**
	 * Constructor de la Clase ManagerTerrain
	 * @param player el constructor recibe el jugador para calcular la posición actual sobre el terreno actual
	 */
	public ManagerTerrain(Player player) {
		this.player = player;
		this.terrainList = new ArrayList<Terrain>();
		this.actualPositionPlayer = new float[2];
		updatePositionAuxiliar();
	}

	//------------------------------Methods---------------------------------
	/**
	 * Actualiza las posiciones auxiliares para calcular el terreno actual
	 */
	private void updatePositionAuxiliar() {
		this.actualPositionPlayer[0] = player.getPosition().x;
		this.actualPositionPlayer[1] = player.getPosition().z;
	}
	
	/**
	 * Agrega un terreno a la lista de terrenos
	 * @param terrain: Terreno a agregar.
	 */
	public void addTerrain(Terrain terrain){
		terrainList.add(terrain);
	}
	
	/**
	 * Busca un terreno en la lista con sus puntos de referencia.
	 * @param x: Punto de referencia en eje X
	 * @param z: Punto de referencia en eje Y
	 * @return: Terreno con los puntos de referencia indicados
	 */
	public Terrain searchTerrain (float x, float z){
		Terrain terrainAux = actualTerrain;
		if(terrainAux.getX() == x && terrainAux.getZ() == z){
			return actualTerrain;
		} else {
			for (Terrain terrain : terrainList) {
				if(terrain.getX() == x && terrain.getZ() == z){
					terrainAux = terrain;
					break;
				}
			}
			return terrainAux;
		}
	}
	
	/**
	 * Metodo que obtiene el terreno actual en que se encuentra el jugador
	 */
	public void obtainTerrein(){
		if(!(actualPositionPlayer[0] == player.getPosition().x && actualPositionPlayer[1] == player.getPosition().z)){
			float x = ((float) Math.floor(player.getPosition().x / SIZE_TERRAIN)) * SIZE_TERRAIN;
			float z = ((float) Math.floor(player.getPosition().z / SIZE_TERRAIN)) * SIZE_TERRAIN;
			updatePositionAuxiliar();
			actualTerrain = searchTerrain(x, z);
		}		
	}

	//-----------------------------Getters & Setters----------------------------
	public List<Terrain> getTerrainList() {
		return terrainList;
	}

	public Terrain getActualTerrain() {
		return actualTerrain;
	}

	public void setActualTerrain(Terrain actualTerrain) {
		this.actualTerrain = actualTerrain;
	}
}
