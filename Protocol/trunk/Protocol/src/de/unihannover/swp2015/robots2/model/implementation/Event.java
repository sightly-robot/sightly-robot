package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * Basic implementation of the interface IEvent which is used by the custom
 * observer pattern as implemented in AbstractModel.
 * 
 * Implements a wrapper for an UpdateType and and object pointer.
 * 
 * @version 0.2
 * @author Patrick Kawczynski and Michael Thies
 */
public class Event implements IEvent {

	/** The update type of the emitted event. */
	private final UpdateType type;
	/** A pointer to the object that was updated. */
	private final Object object;

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
