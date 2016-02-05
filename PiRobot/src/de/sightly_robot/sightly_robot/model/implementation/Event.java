package de.sightly_robot.sightly_robot.model.implementation;

import de.sightly_robot.sightly_robot.model.interfaces.IEvent;

/**
 * Basic implementation of the interface IEvent which is used by the custom
 * observer pattern as implemented in AbstractModel.
 * 
 * Implements a wrapper for an UpdateType and and object pointer.
 * 
 * @version 0.2
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public class Event implements IEvent {

	/** The update type of the emitted event. */
	private final UpdateType type;
	/** A pointer to the object that was updated. */
	private final Object object;

	/**
	 * Default constructor which creates an event with the given type and the
	 * target object.
	 * 
	 * @param type
	 *            The type of the event
	 * @param object
	 *            The object which emits this event.
	 */
	public Event(UpdateType type, Object object) {
		this.type = type;
		this.object = object;
	}

	@Override
	public UpdateType getType() {
		return this.type;
	}

	@Override
	public Object getObject() {
		return this.object;
	}

}
