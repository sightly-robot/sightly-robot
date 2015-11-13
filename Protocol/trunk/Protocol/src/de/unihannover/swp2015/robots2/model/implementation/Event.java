package de.unihannover.swp2015.robots2.model.implementation;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Event implements IEvent {

	private final UpdateType type;
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
