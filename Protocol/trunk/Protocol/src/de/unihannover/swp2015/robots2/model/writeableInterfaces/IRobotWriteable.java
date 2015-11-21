package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.*;

/**
 * Interface for all the write actions / setters on a Robot object to be used by
 * the controllers.
 * 
 * A Robot objects represents any kind of Robot that takes part in the game.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IRobotWriteable extends IRobot, IAbstractModelWriteable {

	/**
	 * Set the current position of this robot.
	 * 
	 * @param x
	 *            x-coordinate of the robot's current position
	 * @param y
	 *            y-coordinate of the robot's current position
	 * @param orientation
	 *            Current orientation of the robot
	 */
	public void setPosition(int x, int y, IPosition.Orientation orientation);

	/**
	 * Change the name of the robot.
	 * 
	 * @param name
	 *            The robot's new name
	 */
	public void setName(String name);

	/**
	 * Add a number of points to the robot's score. A negative value will
	 * decrease the robot's score by the equivalent number of points.
	 * 
	 * This increase/decrease is thread safe.
	 * 
	 * @param score
	 *            Number of points to add to or subtract from the robot's score
	 * @return The robot's new score, after the increase/decrease.
	 */
	public int addScore(int score);

	/**
	 * Set the robot's score to a given number of points.
	 * 
	 * @param score
	 *            The robot's new score.
	 */
	public void setScore(int score);

	/**
	 * Set this Robot to setup state.
	 * 
	 * This should happen after the robot got its start position and should be
	 * revoked when the game is started or the user indicates the robot is ready
	 * and in position.
	 * 
	 * @param setupState
	 *            If the robot is currently in setupState
	 */
	public void setSetupState(boolean setupState);

	/**
	 * Set this Robot to error state or back to normal operation.
	 * 
	 * This should happen when the robot broadcasts an error message.
	 * 
	 * @param errorState
	 */
	public void setErrorState(boolean errorState);

}
