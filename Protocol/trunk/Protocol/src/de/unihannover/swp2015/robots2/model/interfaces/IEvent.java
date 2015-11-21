package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * This interface describes an event message, wich can passed between different
 * objects and holds the information about the event.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IEvent {

	/**
	 * This enumeration contains all possible types of update-events which can
	 * occur.
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum UpdateType {
		GAME_STATE, GAME_PARAMETER, STAGE_SIZE, STAGE_WALL, FIELD_STATE, FIELD_FOOD, ROBOT_SCORE, ROBOT_POSITION, ROBOT_ADD, ROBOT_STATE
	}

	/**
	 * Returns the Event-type.
	 * 
	 * @return The event-type
	 */
	public UpdateType getType();

	/**
	 * Returns the related object of the event.
	 * 
	 * @return The Object.
	 */
	public Object getObject();
}
