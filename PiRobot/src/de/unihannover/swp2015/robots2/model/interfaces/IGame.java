package de.unihannover.swp2015.robots2.model.interfaces;

import java.util.Map;

/**
 * Read-only interface of the Game, which contains a Stage, a map of Robots and
 * some game parameters.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IGame extends IAbstractModel {

	/**
	 * Get a read-only reference to the Game's Stage.
	 * 
	 * @return The Game's Stage
	 */
	public IStage getStage();

	/**
	 * Returns True if the Game is running. (Robots are allowed to move, food
	 * grows and scores are occasionally increased.)
	 * 
	 * @return
	 */
	public boolean isRunning();

	/**
	 * Returns True if the local model is synchronized via MQTT. This flag will
	 * be set to FALSE before the MQTT connection was established of after the
	 * connection to the MQTT broker was lost.
	 * 
	 * @return If the model is in sync
	 */
	public boolean isSynced();

	/**
	 * Returns the maximum speed for virtual robots.
	 * 
	 * @return The speed in seconds per field.
	 */
	public float getVRobotSpeed();

	/**
	 * Returns the maximum rotation speed for virtual robots.
	 * 
	 * @return The rotation speed in seconds per 360°. 
	 */
	public float getVRobotRotationSpeed();

	/**
	 * Returns a map of all Robots in the Game. The map links the robot-id to
	 * its reference.
	 * 
	 * @return A map object with all robots.
	 */
	public Map<String, ? extends IRobot> getRobots();

}
