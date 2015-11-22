package de.unihannover.swp2015.robots2.controller.interfaces;

import java.util.List;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IHardwareRobotControl;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.model.interfaces.*;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IGuiController extends IController {

	/**
	 * 
	 */
	public void sendFood(List<List<Integer>> food);
	
	/**
	 * 
	 * @param growingRates
	 */
	public void sendGrowingRates(List<List<Integer>> growingRates);
	
	/**
	 * 
	 * @param robotSpeed
	 * @param hesitationTime
	 */
	public void sendGameParameters(float robotSpeed, int hesitationTime);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param robot
	 * @param orientation
	 */
	public void setRobotPosition(int x, int y, IRobot robot, IPosition.Orientation orientation);
	
	/**
	 * This method starts the game.
	 */
	public void startGame();
	
	/**
	 * This method stops the game.
	 */
	public void stopGame();
	
	/**
	 * This method reset all game values.
	 */
	public void resetGame();
	
	/**
	 * 
	 * @param id
	 * @param value
	 */
	public void setRobotSettings(String id, String value);
	
	/**
	 * 
	 * @param id
	 */
	public void getRobotSettings(String id);
	
	/**
	 * 
	 * @param value
	 */
	public void setVisualizationSettings(String value);
	
	/**
	 * 
	 */
	public void getVisualizationSettings();
	
	/**
	 * 
	 * @param id
	 */
	public void letRobotBlink(String id);
	
	/**
	 * 
	 * @param control
	 */
	public void registerVisualizationControl(IVisualizationControl control);
	
	/**
	 * 
	 * @param control
	 */
	public void registerHardwareRobotControl(IHardwareRobotControl control);
}
