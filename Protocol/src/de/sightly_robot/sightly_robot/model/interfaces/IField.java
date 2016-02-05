package de.sightly_robot.sightly_robot.model.interfaces;

import java.util.Set;

/**
 * Read-only interface of a Labyrinth-Field, which represents a single position
 * on the Stage.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IField extends IAbstractModel {

	/**
	 * This enumeration defines all possible states, the field can get.
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum State {
		FREE, LOCKED, OCCUPIED, OURS, LOCK_WAIT, RANDOM_WAIT
	}

	/**
	 * Returns the x-coordinate of the field.
	 * 
	 * @return The x-coordinate
	 */
	public int getX();

	/**
	 * Returns the y-coordinate of the field.
	 * 
	 * @return The y-coordinate
	 */
	public int getY();

	/**
	 * Returns true, if a wall blocks the way in the given direction (represents
	 * by an orientation).
	 * 
	 * @param orientation
	 *            The direction, where you want to check the wall.
	 * @return true: if there is a wall; false: else
	 */
	public boolean isWall(IPosition.Orientation orientation);
	
	/**
	 * Returns all walls around a field. An orientation will be in the list
	 * if isWall would return true for that direction.
	 *  
	 * @return A set of directions that represent walls.
	 */
	public Set<IPosition.Orientation> getWalls();

	/**
	 * Returns the current amount of food on the field.
	 * 
	 * @return The current amount of food [0-10]
	 */
	public int getFood();

	/**
	 * Returns the current state of the field.
	 * 
	 * @return The field state
	 */
	public State getState();

	/**
	 * Returns the id of the robot, who is locked the field.
	 * 
	 * 
	 * @return The robot-id of the robot, who locked the field.
	 */
	public String getLockedBy();

	/**
	 * Returns the growing rate of the food on the field. Only available on
	 * Server and GUI!
	 * 
	 * @return The growing rate in milliseconds per food level.
	 */
	public int getGrowingRate();
}