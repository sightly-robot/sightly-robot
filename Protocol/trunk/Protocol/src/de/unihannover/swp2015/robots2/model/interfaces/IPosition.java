package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * A position represents the (potential) position of a robot as coordinates on
 * the Stage, it's orientation and (depending on the available information) it's
 * driving progress to the next field. Position objects are used to encapsulate
 * the position properties of all robots and store the potential start positions
 * on the Stage.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IPosition {

	/**
	 * Contains the four cardinal points as the possible orientations in the
	 * labyrinth.
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 * @author Michael Thies
	 */
	public enum Orientation {
		NORTH, EAST, SOUTH, WEST
	}

	/**
	 * Returns the x-coordinate of the position.
	 * 
	 * @return The x-coordinate
	 */
	public int getX();

	/**
	 * Returns the y-coordinate of the position.
	 * 
	 * @return The y-coordinate
	 */
	public int getY();

	/**
	 * Returns the orientation of the position.
	 * 
	 * @return The orientation
	 */
	public Orientation getOrientation();

	/**
	 * Returns the driving progress.
	 * 
	 * @return Progress to the next field in per mills: 0 = just started, 1000
	 *         reached next field
	 */
	public int getProgress();
}
