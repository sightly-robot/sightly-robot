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
	 * Update the drive progress of this robot.
	 * 
	 * @param progress
	 *            The new progress
	 */
	public void setProgress(int progress);

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
	 * Set this robot's state.
	 * 
	 * See {@link RobotState} for further details on this. The disconnected
	 * state should not be set with this method as robot state but as
	 * robotConnection flag with {@link #setRobotConnectionState(boolean)}.
	 * 
	 * @param state
	 *            new state of the robot (if connected)
	 */
	public void setRobotState(RobotState state);

	/**
	 * Set the robot connected flag.
	 * 
	 * Changing this flag will not affect the stored robot state but is used by
	 * {@link IRobot#getState()} to overwrite the current state with
	 * Disconnected.
	 * 
	 * @param state
	 *            true if the robot is currently connected
	 */
	public void setRobotConnectionState(boolean state);

}
