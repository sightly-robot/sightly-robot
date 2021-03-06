package de.sightly_robot.sightly_robot.model.writeableInterfaces;

import java.util.concurrent.Future;

import de.sightly_robot.sightly_robot.model.interfaces.IField;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;

/**
 * Interface providing the write methods to a Field.
 * 
 * A Field represents a square on the game ground where a robot can be located
 * and food may grow.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IFieldWriteable extends IField, IAbstractModelWriteable {

	/**
	 * Set or remove a wall to the specified direction. If a wall is set to true
	 * the visualization will paint some kind of barrier to the next field in
	 * this direction and robots won't be able to leave this Field in this
	 * direction.
	 * 
	 * @param orientation
	 *            Side of the Field to establish or take down the wall.
	 * @param wall
	 *            Whether there will be a barrier now or not
	 */
	public void setWall(IPosition.Orientation orientation, boolean wall);

	/**
	 * Set the current state of food growing on this Field.
	 * 
	 * This operation is thread safe.
	 * 
	 * @param food
	 *            The food's state: 0 = no food, 10 = fully grown food
	 */
	public void setFood(int food);

	/**
	 * Increase the food on this Field by the given number of units.
	 * 
	 * This operation is thread safe.
	 * 
	 * @param value
	 *            Number of food states to add to the current value
	 * @return The new food state on this Field
	 */
	public int incrementFood(int value);

	/**
	 * Set the lock state of this field.
	 * 
	 * This controls whether the field is locked or occupied by another robot or
	 * may be requested to enter by the robot using this model. A lot of logic
	 * and timers may be for reasonably settings this property.
	 * 
	 * @param state
	 *            The current lock state the field is in
	 */
	public void setState(State state);

	/**
	 * Store the ID of the robot locking / occupying this Field.
	 * 
	 * This information may be used by the ai or the gui.
	 * 
	 * @param lockedBy
	 *            ID of the robot locking / occupying this Field
	 */
	public void setLockedBy(String lockedBy);

	/**
	 * Set this Field's food growing rate. This property will only be used at
	 * the game server's model.
	 * 
	 * @param growingRate
	 *            New growing rate of the field in milliseconds per food state
	 *            change
	 */
	public void setGrowingRate(int growingRate);

	/**
	 * Register a Future that is associated with a timer task to change the
	 * field state. It may be cancelled with cancelStateTimer.
	 * 
	 * @param The
	 *            new stateTimerFuture
	 */
	public void setStateTimerFuture(Future<Object> stateTimerFuture);

	/**
	 * Cancel the current timer task that was destined to update the field state
	 * via its Future. The Future will be removed from this Field.
	 * 
	 */
	public void cancelStateTimer();
}
