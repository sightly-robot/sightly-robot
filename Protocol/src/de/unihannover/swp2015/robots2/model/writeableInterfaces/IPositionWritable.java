package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition;

/**
 * Interface providing all setters of a Position object.
 * 
 * A position represents the (potential) position of a robot as coordinates on
 * the Stage, it's orientation and (depending on the available information) it's
 * driving progress to the next field. Position objects are used to encapsulate
 * the position properties of all robots and store the potential start positions
 * on the Stage.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IPositionWritable extends IPosition {

	/**
	 * Set the coordinates.
	 * 
	 * @param x
	 *            New x-coordinate
	 * @param y
	 *            New y-coordinate
	 */
	public void setPosition(int x, int y);

	/**
	 * Set the orientation.
	 * 
	 * @param orientation
	 *            New orientation
	 */
	public void setOrientation(Orientation orientation);

	/**
	 * Set the driving progress.
	 * 
	 * @param progress
	 *            Progress to the next field in per mills: 0 = just started,
	 *            1000 reached next field
	 */
	public void setProgress(int progress);
}
