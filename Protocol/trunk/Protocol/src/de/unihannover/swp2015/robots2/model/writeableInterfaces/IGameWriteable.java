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
	public IStageWriteable getStageWriteable();
	
	/**
	 * 
	 * @param id
	 * @param robot
	 */
	public void addRobot(String id, IRobotWriteable robot);
	
	/**
	 * 
	 * @param id
	 */
	public void removeRobot(String id);
	
	/**
	 * 
	 * @param running
	 */
	public void setRunning(boolean running);
	
	public void setVRobotSpeed(float vRobotSpeed);
	
	public void setHesitationTime(int hesitationTime);
	
	/**
	 * 
	 * @return
	 */
	public Map<String,IRobotWriteable> getRobotsWriteable();
	
}
