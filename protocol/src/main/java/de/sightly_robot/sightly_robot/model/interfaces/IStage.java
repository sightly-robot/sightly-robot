package de.sightly_robot.sightly_robot.model.interfaces;

import java.util.List;

/**
 * Interface that represents all read actions to a Stage. A Stage basically
 * represents the game's map. Unfortunately 'Map' is such ambiguous word, so
 * we'll call it 'Stage'. A Stage contains a two dimensional list of Fields.
 * Additionally it can store a list of potential robot start positions, which
 * will only be used by the game server and control guis.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IStage extends IAbstractModel {

	/**
	 * Returns the number of IFields on the x-axis (west to east)
	 * 
	 * @return width of the Stage (number of columns west to east)
	 */
	public int getWidth();

	/**
	 * Returns the number of IFields on the y-axis (north to south)
	 * 
	 * @return height of the Stage (number of rows north to south)
	 */
	public int getHeight();

	/**
	 * Returns a read-only reference to the Field object at the given
	 * coordinates.
	 * 
	 * @param x
	 *            x-coordinate of the requested Field
	 * @param y
	 *            y-coordinate of the requested Field
	 * @return The Field at this position
	 */
	public IField getField(int x, int y);

	/**
	 * Only available on Server and GUI. Returns a list of available
	 * start-positions.
	 * 
	 * @return a list of available start-positions
	 */
	public List<IPosition> getStartPositions();
}
