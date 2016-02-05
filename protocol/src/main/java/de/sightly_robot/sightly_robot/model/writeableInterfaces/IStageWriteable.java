package de.sightly_robot.sightly_robot.model.writeableInterfaces;

import de.sightly_robot.sightly_robot.model.interfaces.*;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;

/**
 * Interface that represents all write actions to a Stage. A Stage basically
 * represents the game's map. Unfortunately 'Map' is such ambiguous word, so
 * we'll call it 'Stage'. A Stage contains a two dimensional list of Fields.
 * Additionally it can store a list of potential robot start positions, which
 * will only be used by the game server and control guis.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IStageWriteable extends IStage, IAbstractModelWriteable {

	/**
	 * Returns a writable reference to the Field object at the given
	 * coordinates.
	 * 
	 * @param x
	 *            x-coordinate of the requested Field
	 * @param y
	 *            y-coordinate of the requested Field
	 * @return The Field at this position
	 */
	public IFieldWriteable getFieldWriteable(int x, int y);

	/**
	 * Clears the List of Startpositions
	 */
	public void clearStartPositions();

	/**
	 * Add a position to the list of potential start positions on this Stage.
	 * 
	 * @param x
	 *            x-coordinate of the new start position
	 * @param y
	 *            y-coordinate of the new start position
	 * @param orientation
	 *            Orientation of the robot when starting at this position
	 */
	public void addStartPosition(int x, int y, Orientation orientation);

	/**
	 * Change the number of fields in x- and y-direction of this Stage.
	 * 
	 * A Stage is always assumed to be rectangular. All existing Fields which
	 * are not covered by the given size will be deleted from the Stage, while
	 * new Fields might be created if the increases in one of the dimensions.
	 * 
	 * @param width
	 *            New width of the Stage (number of columns west to east)
	 * @param height
	 *            New height of the Stage (number of rows north to south)
	 */
	public void changeSize(int width, int height);

}
