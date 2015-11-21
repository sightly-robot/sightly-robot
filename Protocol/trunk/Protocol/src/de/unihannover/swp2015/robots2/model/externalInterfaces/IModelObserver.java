package de.unihannover.swp2015.robots2.model.externalInterfaces;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * This interface describes a simple observer, which can ask for updates of a
 * model.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public interface IModelObserver {

	/**
	 * This method will be called of the model is changed.
	 */
	public abstract void onModelUpdate(IEvent event);
}
