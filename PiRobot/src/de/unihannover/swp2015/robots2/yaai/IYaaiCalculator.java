package de.unihannover.swp2015.robots2.yaai;

import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * Defines interface functions for the computing engine of {@link YetAnotherAi}.
 * 
 * @author Michael Thies
 */
public interface IYaaiCalculator {

	/**
	 * Get the latest next field calculated by this calculator.
	 * 
	 * @return Next targeted field, as computed by latest calculation
	 */
	public IField getNextField();

	/**
	 * Set the current robot position to be used as start field for next path
	 * calculation.
	 * 
	 * @param field
	 *            The field the robot is currently placed on (or driving to)
	 * @param orientation
	 *            The orientation in that the robot is standing on or driving
	 *            onto the field.
	 */
	public void setCurrentPosition(IField field, Orientation orientation);

	/**
	 * Register a new {@link IComputedFieldHandler} to be informed about each
	 * finished calculation of the next field.
	 * 
	 * @param handler
	 *            The new handler that will be informed by now.
	 */
	public void setHandler(IComputedFieldHandler handler);
}
