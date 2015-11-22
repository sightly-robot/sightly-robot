package de.unihannover.swp2015.robots2.controller.interfaces;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IServerController extends IController {

	/**
	 * Updates the score of the robot with the given id. The passed score-value
	 * will be added on the current score.
	 * 
	 * @param robotId
	 *            the robot, who gains the score
	 * @param score
	 *            the score-value
	 */
	public void updateScore(String robotId, int score);

	/**
	 * Updates the food-value on the field at the given coordinates.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @param value
	 *            the new food-value
	 */
	public void updateFood(int x, int y, int value);
}
