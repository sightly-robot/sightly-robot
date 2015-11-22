package de.unihannover.swp2015.robots2;

import java.util.List;
 
public class GameState {
	private GameMap map;
	private List <Robot> robots;
	
	GameState(GameMap map) {
		this.map = map;
	}
	
	public GameMap getMap() {
		return map;
	}
	public void setMap(GameMap map) {
		this.map = map;
	}
	public List<Robot> getRobots() {
		return robots;
	}
}
