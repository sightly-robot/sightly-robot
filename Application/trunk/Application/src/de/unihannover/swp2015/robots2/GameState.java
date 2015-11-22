package de.unihannover.swp2015.robots2;

import java.util.ArrayList;
import java.util.List;
 
public class GameState {
	private GameMap map;
	private List <Robot> robots;
	
	public GameMap getMap() {
		return map;
	}
	public void setMap(GameMap map) {
		this.map = map;
	}
	public List<Robot> getRobots() {
		return robots;
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
