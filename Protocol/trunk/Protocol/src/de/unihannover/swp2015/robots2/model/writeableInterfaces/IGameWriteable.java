package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import java.util.Map;

import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * Interface providing write access to all the Game's properties.
 * 
 * A Game contains all the shared and synchronized data structures: A Stage, a
 * list of Robots and some general parameters.
 * 
 * @version 0.2
 * @author Patrick Kawczynski and Michael Thies
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
	 * Set the maximum speed of virtual Robots in this Game.
	 * 
	 * @param vRobotSpeed
	 *            New maximum speed of virtual robots in seconds per field.
	 */
	public void setVRobotSpeed(float vRobotSpeed);

	/**
	 * Set the maximum hesitation time of robots on a field without moving.
	 * 
	 * @param hesitationTime
	 *            The new maximum hesitation time in milliseconds.
	 */
	public void setHesitationTime(int hesitationTime);

	/**
	 * Get a writable reference to all the Robots.
	 * 
	 * @return A Map containing all robots referenced by their ID.
	 */
	public Map<String, IRobotWriteable> getRobotsWriteable();

}
