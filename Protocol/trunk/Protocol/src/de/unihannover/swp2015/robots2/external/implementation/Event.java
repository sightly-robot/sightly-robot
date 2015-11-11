package de.unihannover.swp2015.robots2.external.implementation;

import de.unihannover.swp2015.robots2.external.interfaces.IEvent;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Event implements IEvent {

	private UpdateType type;
	private Object object;
	
	/**
	 * 
	 * @version 0.1
	 * @author Patrick Kawczynski
	 */
	public enum UpdateType {
		GAME_STATE, 
		MAP_SIZE,
		MAP_WALL,
		FIELD_STATE,
		FIELD_FOOD,
		ROBOT_SCORE,
		ROBOT_POSITION,
		ROBOT_ADD
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
