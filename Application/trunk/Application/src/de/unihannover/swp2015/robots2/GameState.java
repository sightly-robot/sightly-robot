package de.unihannover.swp2015.robots2;

import java.util.ArrayList;
import java.util.List;
 
public class GameState {
	private GameMap map;
	private List <Robot> robots;
	private boolean running;
	
	/**
	 * Get the map.
	 * @return A GameMap object.
	 */
	public GameMap getMap() {
		return map;
	}
	
	/**
	 * Set the map.
	 * @param map A GameMap object.
	 */
	public void setMap(GameMap map) {
		this.map = map;
	}
	
	/**
	 * Get all robots in the game.
	 * @return List of robots.
	 */
	public List<Robot> getRobots() {
		return robots;
	}
	
	/**
	 * Is the game running?
	 * @return true if the game is running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Set the game to running on true.
	 * @param running Game running?
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}


	// Antipattern - Singleton (Eager)
	private static volatile GameState instance = new GameState();
	private GameState() {
		map = new GameMap();
		robots = new ArrayList <Robot>();
	}
	public static GameState getInstance() {
		return instance;
	}
}
