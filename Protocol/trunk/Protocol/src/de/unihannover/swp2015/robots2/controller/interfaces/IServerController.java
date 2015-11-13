package de.unihannover.swp2015.robots2.controller.interfaces;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IServerController extends IController {
	
	/**
	 * 
	 * @param robotId
	 * @param score
	 */
	public void updateScore(String robotId, int score);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param value
	 */
	public void updateFood(int x, int y, int value);
}
