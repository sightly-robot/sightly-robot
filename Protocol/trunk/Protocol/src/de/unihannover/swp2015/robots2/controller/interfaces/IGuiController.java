package de.unihannover.swp2015.robots2.controller.interfaces;

import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IGuiController extends IController {

	/**
	 * 
	 */
	public abstract void setMetaMap();
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param robot
	 * @param orientation
	 */
	public abstract void setRobotPosition(int x, int y, IRobot robot, Orientation orientation);
	
	/**
	 * 
	 */
	public abstract void startGame();
	
	/**
	 * 
	 */
	public abstract void stopGame();
	
	/**
	 * 
	 */
	public abstract void resetGame();
	
	/**
	 * 
	 * @param id
	 * @param value
	 */
	public abstract void setRobotSettings(String id, String value);
	
	/**
	 * 
	 * @param id
	 */
	public abstract void getRobotSettings(String id);
	
	/**
	 * 
	 * @param value
	 */
	public abstract void setVisualizationSettings(String value);
	
	/**
	 * 
	 */
	public abstract void getVisualizationSettings();
	
}
