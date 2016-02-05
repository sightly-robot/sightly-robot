package de.sightly_robot.sightly_robot.model.writeableInterfaces;

import java.util.Map;

import de.sightly_robot.sightly_robot.model.interfaces.IGame;

/**
 * Interface providing write access to all the Game's properties.
 * 
 * A Game contains all the shared and synchronized data structures: A Stage, a
 * list of Robots and some general parameters.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IGameWriteable extends IGame, IAbstractModelWriteable {

	/**
	 * Get a writable reference to the Game's Stage.
	 * 
	 * @return The Game's Stage
	 */
	public IStageWriteable getStageWriteable();

	/**
	 * Add a new Robot that appeared in the game.
	 * 
	 * @param robot
	 *            Reference to the new Robot's object
	 */
	public void addRobot(IRobotWriteable robot);

	/**
	 * Remove a Robot from the data set, e.g. when the user removed this robot.
	 * 
	 * @param id
	 *            ID of the robot to be removed.
	 */
	public void removeRobot(String id);

	/**
	 * Set the state of the Game: If set to true the game will start, food will
	 * grow and robots are allowed to move.
	 * 
	 * @param running
	 *            If the game is running
	 */
	public void setRunning(boolean running);

	/**
	 * Set the sync state of the model. True if an MQTT connection is
	 * established, false if not.
	 * 
	 * @param synced
	 *            If the model is synchronized via MQTT
	 */
	public void setSynced(boolean synced);

	/**
	 * Set the maximum speed of virtual Robots in this Game.
	 * 
	 * @param vRobotSpeed
	 *            New maximum speed of virtual robots in seconds per field.
	 */
	public void setVRobotSpeed(float vRobotSpeed);

	/**
	 * Set the maximum rotation speed of virtual robots in this game.
	 * 
	 * @param vRobotRotationSpeed
	 *            The new maximum rotation speed in seconds per 360°.
	 */
	public void setVRobotRotationSpeed(float vRobotRotationSpeed);

	/**
	 * Get a writable reference to all the Robots.
	 * 
	 * @return A Map containing all robots referenced by their ID.
	 */
	public Map<String, IRobotWriteable> getRobotsWriteable();

}
