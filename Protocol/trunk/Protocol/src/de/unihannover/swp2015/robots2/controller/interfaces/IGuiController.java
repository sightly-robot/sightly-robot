package de.unihannover.swp2015.robots2.controller.interfaces;

import java.util.List;

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
	
	public void sendGrowingRates(List<List<Integer>> growingRates);
	
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
	 * 
	 */
	public void startGame();
	
	/**
	 * 
	 */
	public void stopGame();
	
	/**
	 * 
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
	
}
