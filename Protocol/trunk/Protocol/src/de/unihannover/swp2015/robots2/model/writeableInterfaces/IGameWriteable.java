package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import java.util.Map;

import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IGameWriteable extends IGame, IAbstractModelWriteable {

	/**
	 * 
	 * @return
	 */
	public abstract IMapWriteable getMapWriteable();
	
	/**
	 * 
	 * @param id
	 * @param robot
	 */
	public abstract void addRobot(String id, IRobotWriteable robot);
	
	/**
	 * 
	 * @param id
	 */
	public abstract void removeRobot(String id);
	
	/**
	 * 
	 * @param running
	 */
	public abstract void setRunning(boolean running);
	
	/**
	 * 
	 * @return
	 */
	public abstract Map<String, IRobotWriteable> getRobotsWriteable();
	
}
