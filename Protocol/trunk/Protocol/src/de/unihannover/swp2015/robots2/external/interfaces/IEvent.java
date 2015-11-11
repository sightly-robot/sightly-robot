package de.unihannover.swp2015.robots2.external.interfaces;

import de.unihannover.swp2015.robots2.external.implementation.Event.UpdateType;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IEvent {
	
	/**
	 * 
	 * @return
	 */
	public abstract UpdateType getType();
	
	/**
	 * 
	 * @return
	 */
	public abstract Object getObject();
}
