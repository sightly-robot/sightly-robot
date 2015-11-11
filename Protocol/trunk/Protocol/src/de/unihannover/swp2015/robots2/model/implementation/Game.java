package de.unihannover.swp2015.robots2.model.implementation;

import java.util.Map;

import de.unihannover.swp2015.robots2.external.interfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IMap;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IMapWriteable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Game extends AbstractModel implements IGame, IGameWriteable {
	
	private IMapWriteable map;
	private Map<String, IRobotWriteable> robots;
	private boolean running;
	
	@Override
	public void observe(IModelObserver observer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void emitEvent() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public IMapWriteable getMapWriteable() {
		return this.map;
	}
	
	@Override
	public void addRobot(String id, IRobotWriteable robot) {
		this.robots.put(id, robot);
	}
	
	@Override
	public void removeRobot(String id) {
		this.robots.remove(id);
	}
	
	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public Map<String, IRobotWriteable> getRobotsWriteable() {
		return this.robots;
	}
	
	@Override
	public IMap getMap() {
		return this.map;
	}
	
	@Override
	public boolean isRunning() {
		return this.running;
	}
	
	@Override
	public Map<String, IRobot> getRobots() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
