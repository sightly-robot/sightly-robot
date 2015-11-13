package de.unihannover.swp2015.robots2.model.writeableInterfaces;

import de.unihannover.swp2015.robots2.model.implementation.Event;
import de.unihannover.swp2015.robots2.model.interfaces.IAbstractModel;;

/**
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IAbstractModelWriteable extends IAbstractModel {
	
	/**
	 * 
	 */
	public void emitEvent(Event.UpdateType type);
	
}