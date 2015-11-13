package de.unihannover.swp2015.robots2.model.externalInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IModelObserver {

	/**
	 * 
	 */
	public abstract void onModelUpdate(IEvent event);
}
