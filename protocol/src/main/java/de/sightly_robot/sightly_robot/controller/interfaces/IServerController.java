package de.sightly_robot.sightly_robot.controller.interfaces;

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
	 * Increase the score of the robot by the given number of points.
	 * 
	 * @param robotId
	 *            Id of the robot
	 * @param points
	 *            Number of points to add
	 */
	public void increaseScore(String robotId, int points);

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

	/**
	 * Increase the food state on this field by the given number of food states.
	 * 
	 * @param x
	 *            x-coordinate of the Field
	 * @param y
	 *            y-coordinate of the Field
	 * @param value
	 *            number of food states to add to the current state
	 */
	public void increaseFood(int x, int y, int value);
}
